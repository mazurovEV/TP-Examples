package tp.emazurov.lesson1.network;

import android.util.Log;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.conn.params.ConnPerRoute;
import org.apache.http.conn.routing.HttpRoute;
import org.apache.http.conn.scheme.PlainSocketFactory;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.scheme.SchemeRegistry;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.conn.tsccm.ThreadSafeClientConnManager;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.HttpContext;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Rustam on 25.09.2014.
 */
public class Network {

    public void urlConnection() throws IOException {
        URL url = new URL("https://simple-weather.p.mashape.com/weather?lat=55.865314&lng=37.603341");
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
    }

    public void httpGet() throws IOException {
        HttpGet request = new HttpGet("https://simple-weather.p.mashape.com/weather?lat=55.865314&lng=37.603341");
        request.setHeader("X-Mashape-Key", "xvtJ6rRJAVmshbFasYHld3ERssImp1SWJWfjsnVUzLXgfE7U53");

        SchemeRegistry schemeRegistry = new SchemeRegistry();
        schemeRegistry.register(new Scheme("http", PlainSocketFactory.getSocketFactory(), 80));
        schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
        HttpParams params = new BasicHttpParams();
        ConnManagerParams.setMaxTotalConnections(params, 10);
        ConnManagerParams.setMaxConnectionsPerRoute(params,
                new ConnPerRoute() {
                    @Override
                    public int getMaxForRoute(HttpRoute route) {
                        return 5;
                    }
                });
        ThreadSafeClientConnManager cm = new ThreadSafeClientConnManager(params, schemeRegistry);
        DefaultHttpClient httpClient = new DefaultHttpClient(cm, params);
        httpClient.setKeepAliveStrategy(new ConnectionKeepAliveStrategy() {
            @Override
            public long getKeepAliveDuration(HttpResponse httpResponse, HttpContext httpContext) {
                return 5000;
            }
        });
        HttpResponse response = httpClient.execute(request);
        int code = response.getStatusLine().getStatusCode();
        if (code == 200) {
            handleInputStream(response.getEntity().getContent());
        }
    }

}
