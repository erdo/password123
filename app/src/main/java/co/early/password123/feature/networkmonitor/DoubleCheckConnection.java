package co.early.password123.feature.networkmonitor;

import java.io.IOException;

import co.early.asaf.core.logging.AndroidLogger;
import co.early.asaf.retrofit.InterceptorLogging;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class DoubleCheckConnection {

    private final static String TAG = DoubleCheckConnection.class.getSimpleName();


    public boolean canWeConnect() {

        OkHttpClient client = new OkHttpClient().newBuilder()
                .addInterceptor(new InterceptorLogging(new AndroidLogger())).build();

        Request request = new Request.Builder().url("https://google.com").head().build();
        Response response = null;
        try {
            response = client.newCall(request).execute();
            return response.isSuccessful();
        } catch (IOException e) {
            return false;
        } finally {
            if (response != null) {
                response.body().close();
            }
        }
    }

}
