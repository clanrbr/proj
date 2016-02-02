package localestates.localestates;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import adapters.PropertiesArrayAdapter;
import fragments.CheckAndRadioBoxesFragment;
import fragments.MainPageFragment;
import localEstatesHttpRequests.HTTPGetProperties;

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
        menuItemHome.setImageResource(R.drawable.ic_home_white_24dp);


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
                Intent searchIntent = new Intent(getBaseContext(),AdvanceSearchActivity.class);
                startActivity(searchIntent);
            }
        });


        listView = (ListView) findViewById(R.id.listView);

        HTTPGetProperties getProperty = new HTTPGetProperties() {
            @Override
            protected void onPostExecute(String result) {
                if (result != null) {
                    try {
                        JSONObject json = new JSONObject(result);

                        JSONArray jsonArray = json.getJSONArray("adverts");
                        ;
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


                    if (advertsJsonArray!=null) {
//                        Attempt to invoke virtual method 'java.lang.Object android.content.Context.getSystemService(java.lang.String)' on a null object reference
                        // nai-veroqtno zaradi getActivity dava ??
                        adapterProperties = new PropertiesArrayAdapter(getBaseContext(),R.layout.property_single_item, advertsJsonArray);
                        listView.setAdapter(adapterProperties);
                        listView.setDivider(null);
                    }
                } else {
                    Log.e("HEREHERE", "EMPTY");
                }
            }
        };

        getProperty.execute("http://api.imot.bg/mobile_api/search");

//        Fragment fragment = new MainPageFragment();
//        if (savedInstanceState == null) {
//            Log.e("HEREHERE","SEE MANY TIMES");
//            android.support.v4.app.FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//            if (ft.isEmpty()) {
//                getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, fragment).addToBackStack("mainPage").commit();
//            } else {
//                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragment).addToBackStack("mainPage").commit();
//            }
////            ft.replace(R.id.fragment_container,fragment).commit();
//        }
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_start, menu);
//        return true;
//    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
////        if (id == R.id.action_settings) {
////            return true;
////        }
//
//        return super.onOptionsItemSelected(item);
//    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();

    }
}
