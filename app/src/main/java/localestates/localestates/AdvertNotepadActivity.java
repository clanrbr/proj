package localestates.localestates;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.view.View.OnClickListener;
import android.widget.Toast;

import com.nhaarman.listviewanimations.itemmanipulation.DynamicListView;
import com.nhaarman.listviewanimations.itemmanipulation.dragdrop.TouchViewDraggableManager;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.OnDismissCallback;
import com.nhaarman.listviewanimations.itemmanipulation.swipedismiss.undo.SimpleSwipeUndoAdapter;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adapters.DragAndDropAdvertNotepadAdapter;
import adapters.PropertiesArrayAdapter;
import db.AdvertNotepad;
import db.AdvertNotepad_Table;

/**
 * Created by macbook on 2/2/16.
 */
public class AdvertNotepadActivity extends AppCompatActivity implements OnClickListener  {

    private ArrayList<JSONObject> allNotepadAdverts;
    private List<AdvertNotepad> allNotepadAdvertsObj;
    private DynamicListView mDynamicListView;
    private DragAndDropAdvertNotepadAdapter adapter;
    private PropertiesArrayAdapter adapterProperties;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.main_color_700));
        }

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notepad);
//        setContentView(R.layout.activity_start);

        ImageView menuItemSearch = (ImageView) findViewById(R.id.searchActionBar);
        ImageView menuItemFavourite = (ImageView) findViewById(R.id.favouriteActionBar);
        menuItemFavourite.setImageResource(R.drawable.ic_favorite_white_24dp);
        ImageView menuItemNotification = (ImageView) findViewById(R.id.notificationActionBar);
        ImageView menuItemHome = (ImageView) findViewById(R.id.homeActionBar);

        menuItemHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent favouriteIntent = new Intent(getBaseContext(),StartActivity.class);
                finish();
                startActivity(favouriteIntent);
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
                finish();
                startActivity(searchIntent);
            }
        });

//        listView = (ListView) findViewById(R.id.listView);

        allNotepadAdvertsObj = SQLite.select()
                .from(AdvertNotepad.class)
                .where()
                .orderBy(AdvertNotepad_Table.order, true)
                .queryList();

        if (allNotepadAdvertsObj != null) {
            Log.e("HEREHERE", "TUK");
            Log.e("HEREHERE", String.valueOf(allNotepadAdvertsObj.size()));
            allNotepadAdverts = new ArrayList<>();
            Log.e("HEREHERE", "ORDER : ");

            for (int i = 0; i < allNotepadAdvertsObj.size(); i++) {
                Log.e("HEREHERE", String.valueOf(allNotepadAdvertsObj.get(i).order));
                try {
//                    Log.e("HEREHERE", allNotepadAdvertsObj.get(i).advert_list);
                    JSONObject property = new JSONObject(allNotepadAdvertsObj.get(i).advert_list);
                    allNotepadAdverts.add(property);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }


        TextView orderNumber = (TextView) findViewById(R.id.activity_list_view_drag_and_drop_shop_order_number);
        TextView date = (TextView) findViewById(R.id.activity_list_view_drag_and_drop_shop_date);
        TextView price = (TextView) findViewById(R.id.activity_list_view_drag_and_drop_shop_price);
        TextView proceed = (TextView) findViewById(R.id.activity_list_view_drag_and_drop_shop_proceed);

        proceed.setOnClickListener(this);


        if ((allNotepadAdverts != null) && (allNotepadAdverts.size() > 0)) {

            mDynamicListView = (DynamicListView) findViewById(R.id.activity_list_view_drag_and_drop_dynamic_listview);
            mDynamicListView.setDividerHeight(0);

            setUpDragAndDrop();

//            adapterProperties = new PropertiesArrayAdapter(getBaseContext(),R.layout.property_single_item, allNotepadAdverts);
//            listView.setAdapter(adapterProperties);
//            listView.setDivider(null);
        }
    }

    private void setUpDragAndDrop() {
        adapter = new DragAndDropAdvertNotepadAdapter(this,allNotepadAdvertsObj );
        SimpleSwipeUndoAdapter swipeUndoAdapter = new SimpleSwipeUndoAdapter(adapter, this, new OnDismissCallback() {
            @Override
            public void onDismiss(@NonNull final ViewGroup listView, @NonNull final int[] reverseSortedPositions) {
                for (int position : reverseSortedPositions) {
                    adapter.remove(position);
                }
            }
        });

        swipeUndoAdapter.setAbsListView(mDynamicListView);
        mDynamicListView.setAdapter(swipeUndoAdapter);
        mDynamicListView.enableSimpleSwipeUndo();

        mDynamicListView.enableDragAndDrop();
        TouchViewDraggableManager tvdm = new TouchViewDraggableManager(R.id.icon);
        mDynamicListView.setDraggableManager(tvdm);
        mDynamicListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent,
                                           View view, int position, long id) {
                mDynamicListView.startDragging(position);
                return true;
            }
        });
    }

    @Override
    public void onClick(View v) {
        Toast.makeText(AdvertNotepadActivity.this,"dasdas",Toast.LENGTH_LONG).show();
    }
}

