package co.early.password123.feature.networkmonitor;

import android.app.Application;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;

import co.early.asaf.core.Affirm;
import co.early.asaf.core.WorkMode;
import co.early.asaf.core.logging.Logger;
import co.early.asaf.core.observer.ObservableImp;
import co.early.asaf.core.threading.AsafTaskBuilder;


public class NetworkState extends ObservableImp {

    public static final String TAG = NetworkState.class.getSimpleName();

    public enum ConnectionType {

        WIFI(true),
        MOBILE_LOCAL(true),
        MOBILE_ROAMING(true),
        LOGIN_OR_CREDIT_REQUIRED(false),
        OTHER(true),
        NONE(false);

        public final boolean needsVerification;

        ConnectionType(boolean needsVerification) {
            this.needsVerification = needsVerification;
        }
    }

    private final ConnectivityManager connectivityManager;
    private final Application application;
    private final Logger logger;
    private final WorkMode workMode;
    private final DoubleCheckConnection doubleCheckConnection;

    private final Handler handler;



    private final BroadcastReceiver networkStateChangeBroadcastReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            checkConnection();
        }
    };

    private ConnectionType connectionType = ConnectionType.MOBILE_ROAMING; //starting with an assumption we have network




    public NetworkState(ConnectivityManager connectivityManager, Application application, DoubleCheckConnection doubleCheckConnection, Logger logger, WorkMode workMode) {
        super(workMode);
        this.connectivityManager = Affirm.notNull(connectivityManager);
        this.application = Affirm.notNull(application);
        this.doubleCheckConnection = Affirm.notNull(doubleCheckConnection);
        this.logger = Affirm.notNull(logger);
        this.workMode = Affirm.notNull(workMode);

        handler = new Handler();
    }

    private void checkConnection() {

        ConnectionType newConnectionType;

        NetworkInfo info = connectivityManager.getActiveNetworkInfo();

        if (info == null || !info.isConnectedOrConnecting()) {
            newConnectionType = ConnectionType.NONE;
        } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
            if (info.isRoaming()) {
                newConnectionType = ConnectionType.MOBILE_ROAMING;
            } else {
                newConnectionType = ConnectionType.MOBILE_LOCAL;
            }
        } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {
            newConnectionType = ConnectionType.WIFI;
        } else {
            newConnectionType = ConnectionType.OTHER;// might actually be connected via Ethernet or even via MMS
        }

        if (newConnectionType.needsVerification) {
            doubleCheckConnectionAsync(newConnectionType);
        }else{
            setNewConnectionTypeAndNotifyIfReq(newConnectionType);
        }
    }


    private void doubleCheckConnectionAsync(ConnectionType newConnectionType) {
        new AsafTaskBuilder<ConnectionType, ConnectionType>(workMode)
                .doInBackground(connectionType -> doubleCheckConnection.canWeConnect(logger) ?
                                            connectionType[0] : ConnectionType.LOGIN_OR_CREDIT_REQUIRED)
                .onPostExecute(payload -> setNewConnectionTypeAndNotifyIfReq(payload))
                .execute(newConnectionType);
    }

    private void setNewConnectionTypeAndNotifyIfReq(ConnectionType newConnectionType) {
        if (newConnectionType != connectionType) {
            connectionType = newConnectionType;

            logger.i(TAG, "Network state changed to:" + connectionType);

            if (connectionType == ConnectionType.NONE && workMode == WorkMode.ASYNCHRONOUS){
                handler.removeCallbacksAndMessages(null);
                handler.postDelayed(() -> notifyObservers(), 2000);
            } else {
                notifyObservers();
            }

        }
    }

    public ConnectionType getConnectionType() {
        return connectionType;
    }

    public boolean isConnected() {
        return (getConnectionType() != ConnectionType.NONE && getConnectionType() != ConnectionType.LOGIN_OR_CREDIT_REQUIRED);
    }

    public void enable() {
        application.registerReceiver(networkStateChangeBroadcastReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    public void disable() {
        application.unregisterReceiver(networkStateChangeBroadcastReceiver);
    }

    public void checkIfDown() {
        if (!isConnected()) {
            checkConnection();
        }
    }

    public void checkIfUp() {
        if (isConnected()) {
            checkConnection();
        }
    }

}
