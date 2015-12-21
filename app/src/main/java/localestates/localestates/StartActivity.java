package localestates.localestates;

import android.app.ActionBar;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
//import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import Adapters.PropertiesArrayAdapter;
import localEstatesHttpRequests.HTTPGetProperties;
import utils.RobotoTextView;

public class StartActivity extends Activity {

    private ArrayList<JSONObject> advertsJsonArray = new ArrayList<JSONObject>();
    private PropertiesArrayAdapter adapterProperties;
    private ListView listView;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        ImageView menuItemSearch = (ImageView) findViewById(R.id.searchActionBar);
        ImageView menuItemFavourite = (ImageView) findViewById(R.id.favouriteActionBar);
        ImageView menuItemNotification = (ImageView) findViewById(R.id.notificationActionBar);
        ImageView menuItemHome = (ImageView) findViewById(R.id.homeActionBar);


        menuItemHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            }
        });

        menuItemFavourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        menuItemNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        menuItemSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this, SearchActivity.class);
                finish();
                startActivity(intent);
            }
        });

//        getSupportActionBar().setDisplayShowTitleEnabled(false);


//        if (Build.VERSION.SDK_INT >= 11) {
//            setTheme(android.R.style.Theme_Holo_NoActionBar);
//        } else {
//            setTheme(android.R.style.Theme_Black_NoTitleBar);
//        }

        listView = (ListView) findViewById(R.id.listView);

        HTTPGetProperties getProperty = new HTTPGetProperties() {
            @Override
            protected void onPostExecute(String result) {
                if (result != null) {
                    Log.e("HEREHERE", "OT TUK LI GO PRINTI?");
                    Log.e("HEREHERE", result);
                    try {
                        JSONObject json = new JSONObject(result);
                        Iterator<String> itCodesets = json.keys();

                        JSONArray jsonArray = json.getJSONArray("adverts");
                        ;
                        if (jsonArray != null) {
                            for (int i = 0; i < jsonArray.length(); i++) {
                                if (jsonArray.getJSONObject(i) != null) {
//                                    Log.e("HEREHERE", jsonArray.getJSONObject(i).toString());
                                    advertsJsonArray.add(jsonArray.getJSONObject(i));
                                }
                            }
                        }

//                        while (itCodesets.hasNext()) {
//                            Log.e("HEREHERE", itCodesets.next());
//                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    adapterProperties = new PropertiesArrayAdapter(StartActivity.this,
                            R.layout.property_single_item, advertsJsonArray);
                    listView.setAdapter(adapterProperties);
                    listView.setDivider(null);

                } else {
                    Log.e("HEREHERE", "EMPTY");
                }
            }
        };

        getProperty.execute("http://api.imot.bg/mobile_api/search");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

    }
}
