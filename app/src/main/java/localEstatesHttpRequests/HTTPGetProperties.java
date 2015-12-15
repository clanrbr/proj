package localEstatesHttpRequests;

import android.os.AsyncTask;
import android.support.v4.util.SimpleArrayMap;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import Utils.HelpFunctions;

/**
 * Created by Ado on 11/30/2015.
 */
public class HTTPGetProperties extends AsyncTask<String, Void, String> {
    private Exception exception;

    private SimpleArrayMap<String, String> mHeaders = new SimpleArrayMap<>();
    private String responseBody;

    @Override
    protected String doInBackground(String... urls) {
        try {
            URL url= new URL(urls[0]);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setDoOutput(true);

            InputStream inputStream = null;
            int responseCode = conn.getResponseCode();
            try {
                if (responseCode == 200) {
                    inputStream=conn.getInputStream();
                    return HelpFunctions.getString(inputStream);
                } else {
                    inputStream = conn.getErrorStream();
                }
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e) {
                        // Ignore.
                    }
                }
            }
            return null;
        } catch (Exception e) {
            this.exception = e;
            return null;
        }
    }

//    @Override
//    protected void onPostExecute(String result) {
//        if (result!=null) {
//            Log.e("HEREHERE", "OT TUK LI GO PRINTI?");
//            Log.e("HEREHERE",result);
//        } else {
//            Log.e("HEREHERE", "EMPTY");
//        }
//
//    }
}
