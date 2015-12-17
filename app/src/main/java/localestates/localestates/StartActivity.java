package localestates.localestates;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

import Adapters.PropertiesArrayAdapter;
import localEstatesHttpRequests.HTTPGetProperties;
import utils.RobotoTextView;

public class StartActivity extends AppCompatActivity {

    private ArrayList<JSONObject> advertsJsonArray = new ArrayList<JSONObject>();
    private PropertiesArrayAdapter adapterProperties;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        listView = (ListView) findViewById(R.id.listView);


//        Typeface.createFromAsset(this.getAssets(), "fonts/Roboto-Black.ttf");
//        this.getAssets().open("fonts/Roboto-Black.ttf");

        HTTPGetProperties getProperty = new HTTPGetProperties()
        {
            @Override
            protected void onPostExecute(String result) {
                if (result!=null) {
                    Log.e("HEREHERE", "OT TUK LI GO PRINTI?");
                    Log.e("HEREHERE",result);
                    try {
                        JSONObject json = new JSONObject(result);
                        Iterator<String> itCodesets = json.keys();

                        JSONArray jsonArray = json.getJSONArray("adverts");;
                        if (jsonArray != null) {
                            for (int i=0;i<jsonArray.length();i++){
                                if ( jsonArray.getJSONObject(i)!=null ) {
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
}
