package localEstatesHttpRequests;

import android.os.AsyncTask;
import android.support.v4.util.SimpleArrayMap;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import interfaces.AsyncResponse;

/**
 * Created by macbook on 1/19/16.
 */
public class MakeASearchHttpRequest extends AsyncTask<String, Void, String> {
    private Exception exception;
    private ArrayList<JSONObject> advertsJsonArray;
    private String advertsNumber;
    private String advertsSearchText;
    private SimpleArrayMap<String, String> mHeaders = new SimpleArrayMap<String,String>();
    private String responseBody;
    public AsyncResponse delegate = null;

    public String readIt(InputStream stream) throws IOException, UnsupportedEncodingException {

        BufferedReader r = new BufferedReader(new InputStreamReader(stream));
        StringBuilder total = new StringBuilder();
        String line;
        while ((line = r.readLine()) != null) {
            total.append(line);
        }

        return total.toString();
    }

    @Override
    protected String doInBackground(String... urls) {
        InputStream is = null;
        try {
            URL url= new URL(urls[0]);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000); /* milliseconds */
            conn.setConnectTimeout(15000); /* milliseconds */
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            int response = conn.getResponseCode();
            if ( response==200 ) {
                is = conn.getInputStream();
                String contentAsString = readIt(is);
                return contentAsString;
            } else {
                return null;
            }
        } catch (Exception e) {
            this.exception = e;
            return null;
        }
    }

//    @Override
    protected void onPostExecute(String result) {
        if (result!=null) {
            advertsJsonArray = new ArrayList<JSONObject>();
            try {
                JSONObject json = new JSONObject(result);
                advertsNumber=json.getString("search_text");
                advertsSearchText=json.getString("adverts_count");
                JSONArray jsonArray = json.getJSONArray("adverts");
                if (jsonArray != null) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        if (jsonArray.getJSONObject(i) != null) {
                            advertsJsonArray.add(jsonArray.getJSONObject(i));
                        }
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        delegate.processFinish(advertsJsonArray,advertsNumber,advertsSearchText);

    }
}



