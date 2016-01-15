package localestates.localestates;

import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import org.json.JSONObject;
import java.util.ArrayList;
import adapters.PropertiesArrayAdapter;
import fragments.CheckAndRadioBoxesFragment;
import fragments.MainPageFragment;

public class StartActivity extends ActionBarActivity {

    private ArrayList<JSONObject> advertsJsonArray = new ArrayList<JSONObject>();
    private PropertiesArrayAdapter adapterProperties;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (android.os.Build.VERSION.SDK_INT>=21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.main_color_700));
        }

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
                Fragment fragment = new CheckAndRadioBoxesFragment();
//                android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//                ft.add(R.id.fragment_container, fragment).commit();
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack("search").commit();
//                Intent intent = new Intent(StartActivity.this, SearchActivity.class);
////                finish();
//                startActivity(intent);
            }
        });


//        Fragment fragment = new CheckAndRadioBoxesFragment();
        Fragment fragment = new MainPageFragment();
        if (savedInstanceState == null) {
            android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment).addToBackStack("mainPage").commit();
//            ft.replace(R.id.fragment_container,fragment).commit();
        }


//        listView = (ListView) findViewById(R.id.listView);
//
//        HTTPGetProperties getProperty = new HTTPGetProperties() {
//            @Override
//            protected void onPostExecute(String result) {
//                if (result != null) {
//                    Log.e("HEREHERE", "OT TUK LI GO PRINTI?");
//                    Log.e("HEREHERE", result);
//                    try {
//                        JSONObject json = new JSONObject(result);
//                        Iterator<String> itCodesets = json.keys();
//
//                        JSONArray jsonArray = json.getJSONArray("adverts");
//                        ;
//                        if (jsonArray != null) {
//                            for (int i = 0; i < jsonArray.length(); i++) {
//                                if (jsonArray.getJSONObject(i) != null) {
////                                    Log.e("HEREHERE", jsonArray.getJSONObject(i).toString());
//                                    advertsJsonArray.add(jsonArray.getJSONObject(i));
//                                }
//                            }
//                        }
//
////                        while (itCodesets.hasNext()) {
////                            Log.e("HEREHERE", itCodesets.next());
////                        }
//
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                    adapterProperties = new PropertiesArrayAdapter(StartActivity.this,
//                            R.layout.property_single_item, advertsJsonArray);
//                    listView.setAdapter(adapterProperties);
//                    listView.setDivider(null);
//
//                } else {
//                    Log.e("HEREHERE", "EMPTY");
//                }
//            }
//        };
//
//        getProperty.execute("http://api.imot.bg/mobile_api/search");
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
