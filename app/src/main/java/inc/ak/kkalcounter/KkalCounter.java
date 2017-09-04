package inc.ak.kkalcounter;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import com.testfairy.TestFairy;

import java.util.logging.Level;

import butterknife.ButterKnife;
import inc.ak.kkalcounter.rest.NetworkInterface;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static inc.ak.kkalcounter.Constants.BASE_URL;



public class KkalCounter extends Application {

    private static NetworkInterface mApi;
    private Retrofit retrofit;
    @Override
    public void onCreate() {
        super.onCreate();
        Preferences.init(this);
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
// set your desired log level
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
// add your other interceptors …

// add logging as last interceptor
        httpClient.addInterceptor(logging);
        TestFairy.begin(this, "58cf3592c89bdbf8fa1266af0500da802fb10f8d");
        retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL) //Базовая часть адреса
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())

                .build();
        mApi = retrofit.create(NetworkInterface.class);

    }
    public static NetworkInterface getApi() {
        return mApi;
    }
}
