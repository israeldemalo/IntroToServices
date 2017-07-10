package israel.introtoservices;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class NotificationService extends IntentService {

    public static final String TAG = "NotificationService";

    public NotificationService() {
        super(TAG);
    }

    BufferedReader reader = null;
    HttpURLConnection con = null;

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        try {
            URL url = new URL("https://github.com/JakeWharton/butterknife");
            con = (HttpURLConnection) url.openConnection();
            InputStream in = con.getInputStream();
            reader = new BufferedReader(new InputStreamReader(in));
            StringBuilder result = new StringBuilder();
            String line = null;

            while ((line = reader.readLine()) != null) {
                result.append(line).append("\n");
            }

            Log.d(TAG, "onHandleIntent:" + result);
            shout(result.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (con != null) {
                con.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void shout(String s) {
        LocalBroadcastManager mgr = LocalBroadcastManager.getInstance(this);
        Intent intent = new Intent("Reshet B");
        intent.putExtra("html", s);

        mgr.sendBroadcast(intent);
    }

}

