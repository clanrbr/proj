package localestates.localestates;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.mugen.Mugen;
import com.mugen.MugenCallbacks;
import com.mugen.attachers.BaseAttacher;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import adapters.PropertiesArrayAdapter;
import interfaces.AsyncResponse;
import listeners.InfiniteScrollListener;
import localEstatesHttpRequests.MakeASearchHttpRequest;
import utils.HelpFunctions;

/**
 * Created by macbook on 1/20/16.
 */
public class SearchResultActivity extends ActionBarActivity implements AsyncResponse {

    private ArrayList<JSONObject> results;
    private String numberOfAdverts;
    private String searchText;
    private ListView listView;
    private PropertiesArrayAdapter adapterProperties;
    private TextView resultTextView;
    private TextView editSearchButton;
    private ArrayList<HashMap<String,String>> searchValues;
    private MakeASearchHttpRequest asyncTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.main_color_700));
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);

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
                Intent searchIntent = new Intent(getBaseContext(), AdvanceSearchActivity.class);
                startActivity(searchIntent);
            }
        });

        editSearchButton = (TextView) findViewById(R.id.editSearchButton);
        editSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent advanceSearchResultIntent = new Intent(SearchResultActivity.this,AdvanceSearchActivity.class);
                advanceSearchResultIntent.putExtra("searchValues",searchValues);
                finish();
                startActivity(advanceSearchResultIntent);
            }
        });

        Bundle extras = getIntent().getExtras();
        if(extras == null) {
            results = null;
        } else {
            String results = extras.getString("results");
            numberOfAdverts = extras.getString("results_numberOfAdverts");
            searchText = extras.getString("results_text");

            searchValues = (ArrayList<HashMap<String, String>>) extras.getSerializable("searchValues");
//                    .getSerializableExtra("arraylist");


            if (results!=null) {
                this.results = new ArrayList<>();
                JSONArray jsonArray = null;
                try {
                    jsonArray = new JSONArray(results);
                    if (jsonArray != null) {
                        for (int i = 0; i < jsonArray.length(); i++) {
                            if (jsonArray.getJSONObject(i) != null) {
                                this.results.add(jsonArray.getJSONObject(i));
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("HEREHERE",results);
            } else {
                Log.e("HEREHERE","NULL");
            }
        }

        asyncTask = new MakeASearchHttpRequest();
        asyncTask.delegate = this;

        listView = (ListView) findViewById(R.id.listView);
        if (results!=null) {
            adapterProperties = new PropertiesArrayAdapter(getBaseContext(),R.layout.property_single_item, results);
            listView.setAdapter(adapterProperties);
            listView.setDivider(null);

            int startFromElement=5;

            if ( (numberOfAdverts!=null) && (Integer.parseInt(numberOfAdverts)<10) ) {
                startFromElement=10;
            }

            listView.setOnScrollListener(new InfiniteScrollListener(startFromElement) {
                @Override
                public void loadMore(int page, int totalItemsCount) {
                    if ( (numberOfAdverts!=null) && (totalItemsCount<Integer.parseInt(numberOfAdverts)) ) {
                        if (searchValues!=null) {
                            Log.e("HEREHERE","LOADING MORE");
                            Log.e("HEREHERE",String.valueOf(totalItemsCount));
                            Log.e("HEREHERE",String.valueOf(page));
                            searchValues.get(1).put("page", String.valueOf(Integer.parseInt(searchValues.get(1).get("page")) + 1) );
                            String searchUrl = HelpFunctions.convertToUrl(searchValues);
                            Log.e("HEREHERE",searchUrl);
                            asyncTask.execute(searchUrl);
                        }
                    } else {
                        Log.e("HEREHERE","noMoreToload");
                    }
                }
            });
        }

        if ( (searchText!=null) && (searchText!="") ) {
            resultTextView= (TextView) findViewById(R.id.resultTextView);
            resultTextView.setText(searchText);
        }
    }

    @Override
    public void processFinish(String output, String numberOfAdverts, String searchText) {
        if (output!=null) {
            ArrayList<JSONObject> moreResults = new ArrayList<>();
            JSONArray jsonArray = null;
            try {
                jsonArray = new JSONArray(output);
                if (jsonArray != null) {
                    for (int i = 0; i < jsonArray.length(); i++) {
                        if (jsonArray.getJSONObject(i) != null) {
                            moreResults.add(jsonArray.getJSONObject(i));
                        }
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if ( (moreResults!=null) && (moreResults.size()>0)  ) {
                results.addAll(moreResults);

                adapterProperties.notifyDataSetChanged();

                asyncTask = new MakeASearchHttpRequest();
                asyncTask.delegate = this;
            }
        }

    }
}
