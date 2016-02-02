package localestates.localestates;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;

import com.raizlabs.android.dbflow.sql.language.SQLite;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adapters.PropertiesArrayAdapter;
import db.AdvertNotepad;
import db.AdvertNotepad_Table;

/**
 * Created by macbook on 2/2/16.
 */
public class AdvertNotepadActivity extends AppCompatActivity {

    private ArrayList<JSONObject> allNotepadAdverts;
    private PropertiesArrayAdapter adapterProperties;
    private ListView listView ;
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

        List<AdvertNotepad> allNotepadAdvertsObj = SQLite.select()
                .from(AdvertNotepad.class)
                .where()
                .orderBy(AdvertNotepad_Table.order,true)
                .queryList();

        if (allNotepadAdvertsObj!=null) {
            Log.e("HEREHERE","TUK");
            allNotepadAdverts = new ArrayList<>();
            for (int i=0;i<allNotepadAdvertsObj.size();i++) {
                try {
                    Log.e("HEREHERE",allNotepadAdvertsObj.get(i).advert_list);
                    JSONObject property = new JSONObject(allNotepadAdvertsObj.get(i).advert_list);
                    allNotepadAdverts.add(property);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }

        if ( (allNotepadAdverts!=null) && (allNotepadAdverts.size()>0) ) {

            adapterProperties = new PropertiesArrayAdapter(getBaseContext(),R.layout.property_single_item, allNotepadAdverts);
            listView.setAdapter(adapterProperties);
            listView.setDivider(null);
        }

    }



}
