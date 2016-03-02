package tp.emazurov.lesson1.network;

import android.app.IntentService;
import android.content.Intent;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Path;
import tp.emazurov.lesson1.MainActivity;

/**
 * Created by Rustam on 25.09.2014.
 */
public class WeatherService extends IntentService {

    public static final String LONGITUDE = "longitude";
    public static final String LATITUDE = "latitude";

    public interface WeatherServiceInterface {
        @Headers("X-Mashape-Key: xvtJ6rRJAVmshbFasYHld3ERssImp1SWJWfjsnVUzLXgfE7U53")
        @GET("weather?lat={latitude}&lng={longitude}")
        Call<String> listRepos(@Path("longitude") String longitude, @Path("longitude") String latitude);
    }

    public String retrofit(String longitude, String latitude) throws IOException {
        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("https://simple-weather.p.mashape.com")
                .build();
        WeatherServiceInterface service = retrofit.create(WeatherServiceInterface.class);
        Call<String> repos = service.listRepos(longitude, latitude);
        return repos.execute().body();
    }

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     *
     * @param name Used to name the worker thread, important only for debugging.
     */
    public WeatherService() {
        super("WeatherService");
    }

    public void urlConnection(String longitude, String latitude) throws IOException {
        URL url = new URL("https://simple-weather.p.mashape.com/weather?lat=" + latitude + "&lng=" + longitude);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("X-Mashape-Key", "xvtJ6rRJAVmshbFasYHld3ERssImp1SWJWfjsnVUzLXgfE7U53");
        connection.connect();
        int code = connection.getResponseCode();
        if (code == 200) {
            InputStream in = connection.getInputStream();
            handleInputStream(in);
        }
    }

    private void handleInputStream(InputStream in) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        String result = "", line = "";
        while ((line = reader.readLine()) != null) {
            result += line;
        }
        Log.e("", result);
        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(MainActivity.Receiver.ACTION);
        broadcastIntent.addCategory(Intent.CATEGORY_DEFAULT);
        broadcastIntent.putExtra(MainActivity.Receiver.WEATHER, result);
        sendBroadcast(broadcastIntent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        String longitude = intent.getStringExtra(LONGITUDE);
        String latitude = intent.getStringExtra(LATITUDE);
        try {
//            urlConnection(longitude, latitude);
            retrofit(longitude, latitude);
        } catch (IOException e) {
            Log.e("", e.getLocalizedMessage(), e);
        }
    }
}
