package co.early.password123.feature.networkmonitor

import android.app.Application
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.os.Handler

import co.early.asaf.core.Affirm
import co.early.asaf.core.WorkMode
import co.early.asaf.core.logging.Logger
import co.early.asaf.core.observer.ObservableImp
import co.early.asaf.core.threading.AsafTaskBuilder


class NetworkState(connectivityManager: ConnectivityManager, application: Application, doubleCheckConnection: DoubleCheckConnection, logger: Logger, workMode: WorkMode) : ObservableImp(workMode) {

    private val connectivityManager: ConnectivityManager
    private val application: Application
    private val logger: Logger
    private val workMode: WorkMode
    private val doubleCheckConnection: DoubleCheckConnection

    private val handler: Handler


    private val networkStateChangeBroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            checkConnection()
        }
    }

    var connectionType = ConnectionType.MOBILE_ROAMING
        private set //starting with an assumption we have network

    val isConnected: Boolean
        get() = connectionType != ConnectionType.NONE && connectionType != ConnectionType.LOGIN_OR_CREDIT_REQUIRED

    enum class ConnectionType private constructor(val needsVerification: Boolean) {

        WIFI(true),
        MOBILE_LOCAL(true),
        MOBILE_ROAMING(true),
        LOGIN_OR_CREDIT_REQUIRED(false),
        OTHER(true),
        NONE(false)
    }


    init {
        this.connectivityManager = Affirm.notNull(connectivityManager)
        this.application = Affirm.notNull(application)
        this.doubleCheckConnection = Affirm.notNull(doubleCheckConnection)
        this.logger = Affirm.notNull(logger)
        this.workMode = Affirm.notNull(workMode)

        handler = Handler()
    }

    private fun checkConnection() {

        val newConnectionType: ConnectionType

        val info = connectivityManager.activeNetworkInfo

        if (info == null || !info.isConnectedOrConnecting) {
            newConnectionType = ConnectionType.NONE
        } else if (info.type == ConnectivityManager.TYPE_MOBILE) {
            if (info.isRoaming) {
                newConnectionType = ConnectionType.MOBILE_ROAMING
            } else {
                newConnectionType = ConnectionType.MOBILE_LOCAL
            }
        } else if (info.type == ConnectivityManager.TYPE_WIFI) {
            newConnectionType = ConnectionType.WIFI
        } else {
            newConnectionType = ConnectionType.OTHER// might actually be connected via Ethernet or even via MMS
        }

        if (newConnectionType.needsVerification) {
            doubleCheckConnectionAsync(newConnectionType)
        } else {
            setNewConnectionTypeAndNotifyIfReq(newConnectionType)
        }
    }


    private fun doubleCheckConnectionAsync(newConnectionType: ConnectionType) {
        AsafTaskBuilder<ConnectionType, ConnectionType>(workMode)
                .doInBackground { connectionType ->
                    if (doubleCheckConnection.canWeConnect(logger))
                        connectionType[0]
                    else
                        ConnectionType.LOGIN_OR_CREDIT_REQUIRED
                }
                .onPostExecute { payload -> setNewConnectionTypeAndNotifyIfReq(payload) }
                .execute(newConnectionType)
    }

    private fun setNewConnectionTypeAndNotifyIfReq(newConnectionType: ConnectionType) {
        if (newConnectionType != connectionType) {
            connectionType = newConnectionType

            logger.i(TAG, "Network state changed to:" + connectionType)

            if (connectionType == ConnectionType.NONE && workMode == WorkMode.ASYNCHRONOUS) {
                handler.removeCallbacksAndMessages(null)
                handler.postDelayed({ notifyObservers() }, 2000)
            } else {
                notifyObservers()
            }

        }
    }

    fun enable() {
        application.registerReceiver(networkStateChangeBroadcastReceiver, IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION))
    }

    fun disable() {
        application.unregisterReceiver(networkStateChangeBroadcastReceiver)
    }

    fun checkIfDown() {
        if (!isConnected) {
            checkConnection()
        }
    }

    fun checkIfUp() {
        if (isConnected) {
            checkConnection()
        }
    }

    companion object {
        val TAG = NetworkState::class.java.simpleName
    }

}
