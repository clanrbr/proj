package localestates.localestates;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;

import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import android.support.v7.widget.Toolbar;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.mikhaellopez.circularprogressbar.CircularProgressBar;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.nineoldandroids.view.ViewHelper;
import com.raizlabs.android.dbflow.runtime.TransactionManager;
import com.raizlabs.android.dbflow.runtime.transaction.process.InsertModelTransaction;
import com.raizlabs.android.dbflow.sql.language.Delete;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.sql.language.property.IProperty;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import adapters.GridViewPropertyFeaturesAdapter;
import constants.LocalEstateConstants;
import db.AdvertNotepad;
import db.AdvertNotepad_Table;
import imagemanipulation.path.parser.PathInfo;
import interfaces.AsyncResponseLoadAdvert;
import localEstatesHttpRequests.HTTPGETAdvert;
import utils.ExpandableHeightGridView;
import utils.HelpFunctions;
import utils.MaterialRippleLayout;

/**
 * Created by macbook on 1/26/16.
 */
public class AdvertActivity extends AppCompatActivity implements ObservableScrollViewCallbacks,
        AsyncResponseLoadAdvert {

    private AdvertNotepad existsAdvertNotepad;
    private int startScroll;
    private String advertID;
    private String advertOutput;
    private int stopScroll;
    private String googleMapsAPI = "AIzaSyCNBVx3m2Q0q2oo4FQhA4UdAyuTaCQ0BRg";
    private JSONArray picturesArray;
    private String advertTitle;
    private String advertPhone;
    private ImageView mImageView;
    private Toolbar mToolbarView;
    private TextView priceLabel;
    private TextView placeLabel;
    private TextView brokerName;
    private TextView agencyName;
    private TextView agencyAddress;
    private MaterialRippleLayout callAction;
    private ExpandableTextView moreInfoLabelExpand;
    private FrameLayout mapBar;
    private FrameLayout mapNavBar;
    private FrameLayout mapStreetViewBar;
    private ExpandableHeightGridView feturesGridView;
    private ObservableScrollView mScrollView;
    private int mParallaxImageHeight;
    private JSONObject advertInto;
    private HTTPGETAdvert asyncLoadAdvert;
    private Menu mMenu;
    private CircularProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        if (android.os.Build.VERSION.SDK_INT >= 21) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.setStatusBarColor(getResources().getColor(R.color.main_color_700));
        }
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advert);
        setSupportActionBar((Toolbar) findViewById(R.id.toolbar));

        progressBar = (CircularProgressBar)findViewById(R.id.progressBar);
        progressBar.setColor(ContextCompat.getColor(this, R.color.main_color_500));
        int animationDuration = 2500; // 2500ms = 2,5s
        progressBar.setProgressWithAnimation(65, animationDuration);

        asyncLoadAdvert = new HTTPGETAdvert(progressBar);
        asyncLoadAdvert.delegate = this;

        priceLabel = (TextView) findViewById(R.id.priceLabel);
        placeLabel = (TextView) findViewById(R.id.placeLabel);
        feturesGridView = (ExpandableHeightGridView) findViewById(R.id.features);
        mapBar = (FrameLayout) findViewById(R.id.mapBar);
        mapNavBar = (FrameLayout) findViewById(R.id.mapNavBar);
        mapStreetViewBar = (FrameLayout) findViewById(R.id.mapStreetViewBar);
        moreInfoLabelExpand = (ExpandableTextView) findViewById(R.id.moreInfoLabelExpand);
        brokerName = (TextView) findViewById(R.id.brokerName);
        agencyName = (TextView) findViewById(R.id.agencyName);
        agencyAddress = (TextView) findViewById(R.id.agencyAddress);
        callAction = (MaterialRippleLayout) findViewById(R.id.callAction);


        callAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            if (ContextCompat.checkSelfPermission(AdvertActivity.this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(AdvertActivity.this, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, LocalEstateConstants.MY_PEMISSION_PHONE_CODE);
            } else {
                callActionFunction();
            }

            }
        });

        // change color of icon
        ImageView mapImageView = (ImageView) findViewById(R.id.mapImageView);
        mapImageView.setColorFilter(ContextCompat.getColor(AdvertActivity.this, R.color.material_lime_900));

        mapBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            Intent mapIntent = new Intent(AdvertActivity.this, MapActivity.class);
            if (advertInto.has("points")) {
                try {
                    mapIntent.putExtra("points", advertInto.getString("points"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if (advertInto.has("coordinates")) {
                try {
                    mapIntent.putExtra("coordinates", advertInto.getString("coordinates"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            if (advertInto.has("rub")) {
                String value = null;
                try {
                    value = advertInto.getString("rub");
                    if (advertInto.has("type_home")) {
                        value = value + " " + advertInto.getString("type_home");
                    }
                    mapIntent.putExtra("title", value);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            startActivity(mapIntent);
            }
        });

        ImageView navigationImageView = (ImageView) findViewById(R.id.mapNavImageView);
        navigationImageView.setColorFilter(ContextCompat.getColor(AdvertActivity.this, R.color.material_lime_900));

        mapNavBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (advertInto.has("coordinates")) {
                    try {
                        if (ActivityCompat.checkSelfPermission(AdvertActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED ) {
                            if ( ActivityCompat.checkSelfPermission(AdvertActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                                getLocation(advertInto.getString("coordinates"));
                            } else {
                                ActivityCompat.requestPermissions(AdvertActivity.this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, LocalEstateConstants.MY_PEMISSION_ACCESS_COARSE_LOCATION);
                            }
                        } else {
                            ActivityCompat.requestPermissions(AdvertActivity.this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LocalEstateConstants.MY_PEMISSION_ACCESS_FINE_LOCATION);
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(AdvertActivity.this,"Този обект не е оказан на картата",Toast.LENGTH_LONG).show();
                }
            }
        });

        ImageView mapStrettViewImageView = (ImageView) findViewById(R.id.mapStreetViewImageView);
        mapStrettViewImageView.setColorFilter(ContextCompat.getColor(AdvertActivity.this, R.color.material_lime_900));

        mapStreetViewBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(AdvertActivity.this, StreetViewActivity.class);
                if (advertInto.has("coordinates")) {
                    try {
                        mapIntent.putExtra("coordinates", advertInto.getString("coordinates"));

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                if (advertInto.has("rub")) {
                    String value = null;
                    try {
                        value = advertInto.getString("rub");
                        if (advertInto.has("type_home")) {
                            value = value + " " + advertInto.getString("type_home");
                        }
                        mapIntent.putExtra("title", value);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                startActivity(mapIntent);
            }
        });


        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            String advertID = extras.getString("advertID");
            if ( (advertID!=null) && (!advertID.equals("")) ) {
                String urlGetAdvert="http://api.imot.bg/mobile_api/details?id="+advertID;
                asyncLoadAdvert.execute(urlGetAdvert);
            }
        }

        mImageView = (ImageView) findViewById(R.id.bigImage);
        mImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent largeImageIntent = new Intent(AdvertActivity.this,AdvertImagesActivity.class);
                if ( (picturesArray!=null) && (picturesArray.length()>0) ) {
                    largeImageIntent.putExtra("advertPictures",picturesArray.toString());
                }
                startActivity(largeImageIntent);
            }
        });

        mToolbarView = (Toolbar) findViewById(R.id.toolbar);
        mToolbarView.setBackgroundColor(ScrollUtils.getColorWithAlpha(0, getResources().getColor(R.color.main_color_500)));
        if (mToolbarView != null) {
            mToolbarView.setNavigationIcon(R.drawable.ic_arrow_back_white_24dp);
            mToolbarView.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });
        }

        mScrollView = (ObservableScrollView) findViewById(R.id.scroll);
        mScrollView.setScrollViewCallbacks(this);

        mParallaxImageHeight = getResources().getDimensionPixelSize(R.dimen.parallax_image_height);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case LocalEstateConstants.MY_PEMISSION_PHONE_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0  && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    callActionFunction();
                } else {
                    Toast.makeText(AdvertActivity.this, "Обаждането изисква разрешение", Toast.LENGTH_LONG).show();
                }
                return;
            }
            case LocalEstateConstants.MY_PEMISSION_ACCESS_COARSE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0  && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    try {
                        getLocation(advertInto.getString("coordinates"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    Toast.makeText(AdvertActivity.this, "Навигацията изисква разрешение", Toast.LENGTH_LONG).show();
                }
                return;
            }

            case LocalEstateConstants.MY_PEMISSION_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0  && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if ( ActivityCompat.checkSelfPermission(AdvertActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                        try {
                            getLocation(advertInto.getString("coordinates"));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
                        ActivityCompat.requestPermissions(AdvertActivity.this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION}, LocalEstateConstants.MY_PEMISSION_ACCESS_COARSE_LOCATION);
                    }
                } else {
                    Toast.makeText(AdvertActivity.this, "Навигацията изисква разрешение", Toast.LENGTH_LONG).show();
                }
                return;
            }
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        onScrollChanged(mScrollView.getCurrentScrollY(), false, false);
    }

    @Override
    public void onScrollChanged(int scrollY, boolean firstScroll, boolean dragging) {
        int baseColor = getResources().getColor(R.color.main_color_500);
        float alpha = Math.min(1, (float) scrollY / mParallaxImageHeight);
        mToolbarView.setBackgroundColor(ScrollUtils.getColorWithAlpha(alpha, baseColor));
        ViewHelper.setTranslationY(mImageView, scrollY / 2);
    }

    @Override
    public void onDownMotionEvent() {
        startScroll = mScrollView.getScrollY();
    }

    public void getLocation(String coordinates) {
        if (ContextCompat.checkSelfPermission(AdvertActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(AdvertActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                String[] coordArray = coordinates.split(",");
                final Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("http://maps.google.com/maps?" +  "&daddr=" + coordArray[0] + "," + coordArray[1]));
                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                startActivity(intent);
            }
        }
    }

    public void callActionFunction() {
        if (ContextCompat.checkSelfPermission(AdvertActivity.this, android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            LayoutInflater inflater = AdvertActivity.this.getLayoutInflater();
            View dialoglayout = inflater.inflate(R.layout.dialog_phone_permission, null);
            final AlertDialog.Builder builderDialog = new AlertDialog.Builder(AdvertActivity.this);
            builderDialog.setView(dialoglayout);
            final AlertDialog show = builderDialog.show();
            Button closeButton= (Button) dialoglayout.findViewById(R.id.closeDialog);
            closeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    show.dismiss();
                }
            });
            Button callButton= (Button) dialoglayout.findViewById(R.id.callDialog);
            callButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    show.dismiss();
                    if (ContextCompat.checkSelfPermission(AdvertActivity.this, android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                        Intent phoneIntent = new Intent(Intent.ACTION_CALL);
                        phoneIntent.setData(Uri.parse("tel:" + advertPhone));
                        startActivity(phoneIntent);
                    }
                }
            });
        }
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
        stopScroll = mScrollView.getScrollY();

        if ( (startScroll==stopScroll)  && (startScroll==0) && (stopScroll==0) ) {
            Intent largeImageIntent = new Intent(AdvertActivity.this,AdvertImagesActivity.class);
            if ( (picturesArray!=null) && (picturesArray.length()>0) ) {
                largeImageIntent.putExtra("advertPictures",picturesArray.toString());
            }
            startActivity(largeImageIntent);

        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_start, menu);
        mMenu=menu;

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.addFavouritesBar) {
            int orderAdvert=1;

            AdvertNotepad existsAdvert = SQLite.select()
                    .from(AdvertNotepad.class)
                    .where(AdvertNotepad_Table.advert_id.is(advertID))
                    .querySingle();
            if ( existsAdvert!=null ) {
                SQLite.delete()
                        .from(AdvertNotepad.class)
                        .where(AdvertNotepad_Table.advert_id.is(advertID))
                        .query();

                MenuItem itemFavourite = mMenu.findItem(R.id.addFavouritesBar);
                if ( itemFavourite!=null ) {
                    itemFavourite.setIcon(R.drawable.ic_favorite_black_24dp);
                }
                Toast.makeText(AdvertActivity.this,"Обявата беше успешно изтрита от бележника!",Toast.LENGTH_LONG).show();
            } else {
                AdvertNotepad lastAdvertNotepad = SQLite.select()
                        .from(AdvertNotepad.class)
                        .where()
                        .orderBy(AdvertNotepad_Table.order,true)
                        .querySingle();

                if ( lastAdvertNotepad!=null ) {
                    orderAdvert=lastAdvertNotepad.order+1;
                }

                if ( (advertOutput!=null) && (advertID!=null) ) {
                    long unixTime = System.currentTimeMillis() / 1000L;

                    AdvertNotepad advnote = new AdvertNotepad();
                    advnote.advert_id=advertID;
                    advnote.advert_note="";
                    advnote.advert_list=advertOutput;
                    advnote.advert_time=unixTime;
                    advnote.advert_version_time=unixTime;
                    advnote.order=orderAdvert;
                    advnote.save();

                    MenuItem itemFavourite = mMenu.findItem(R.id.addFavouritesBar);
                    if ( itemFavourite!=null ) {
                        itemFavourite.setIcon(R.drawable.ic_favorite_white_24dp);
                    }
                    Toast.makeText(AdvertActivity.this,"Обявата беше успешно добавена в бележника!",Toast.LENGTH_LONG).show();
                }
            }

            return true;
        } else if (id == R.id.shareActionButton) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, advertTitle);
            String advertLink="http://www.imot.bg/pcgi/imot.cgi?act=5&adv="+advertID;
            sendIntent.putExtra(Intent.EXTRA_TEXT, advertLink);
            sendIntent.putExtra("com.facebook.platform.extra.APPLICATION_ID","78949132252");
            sendIntent.setType("text/plain");
            AdvertActivity.this.startActivity(sendIntent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void processFinishLoadAdvert(JSONObject output) throws JSONException {
        progressBar.setVisibility(View.GONE);
        advertInto=new JSONObject();
        if (output!=null) {
            advertOutput=output.toString();
            advertInto=output;
            ArrayList<CharSequence> gridValueTitle = new ArrayList<CharSequence>();
            ArrayList<CharSequence> gridValue = new ArrayList<CharSequence>();
            if ( advertInto.has("id") ) {
                advertID=advertInto.getString("id");
            }


            if ( advertInto.has("pictures") ) {
                picturesArray = new JSONArray();
                picturesArray=advertInto.getJSONArray("pictures");
                if (picturesArray.length()>0) {
                    Picasso.with(AdvertActivity.this).load(picturesArray.get(0).toString()).placeholder(R.drawable.noproperty).error(R.drawable.noproperty).into(mImageView);
                }
            }

            if ( advertInto.has("rub") ) {
                String value=advertInto.getString("rub");
                if ( advertInto.has("type_home") ) {
                    value=value + " " + advertInto.getString("type_home");
                }
                advertTitle=value;
                setTitle(advertTitle);
            }

            if ( advertInto.has("price") ) {
                String value = advertInto.getString("price");
                if ( advertInto.has("currency") ) {
                    value=value + " " + advertInto.getString("currency");
                }
                priceLabel.setText(value);
            }

            if ( advertInto.has("quadrature") ) {
                String value = advertInto.getString("quadrature");
                if ( advertInto.has("metric") ) {
                    value=value +" "+ advertInto.getString("metric");
                }
                gridValueTitle.add("Квадратура:");
                gridValue.add(value);
            }

            if ( advertInto.has("town") ) {
                String value = advertInto.getString("town");
                if ( advertInto.has("raion") ) {
                    value=value + ", " + advertInto.getString("raion");
                }
                placeLabel.setText(value);
            }

            if ( advertInto.has("additional_information") ) {
                moreInfoLabelExpand.setText(advertInto.getString("additional_information"));
            }

            if ( advertInto.has("floor_regulation") ) {
                String value=advertInto.getString("floor_regulation");
                if ( advertInto.has("floor_max") ) {
                    value=value + " от " + advertInto.getString("floor_max");
                }
                gridValueTitle.add("Етаж:");
                gridValue.add(value);
            }

            if ( advertInto.has("phone_electricity") )  {
                String typeHome = advertInto.getString("type_home");
                if ( typeHome.contains(",")) {
                    typeHome = typeHome.replace(",", "");
                }
                int groupNumber= HelpFunctions.returnGroupNumberOfProperty(typeHome,null);
                if ( groupNumber==4 ) {
                    gridValueTitle.add("Електричество:");
                } else {
                    gridValueTitle.add("Телефон:");
                }
                gridValue.add(advertInto.getString("phone_electricity"));
            }

            if ( advertInto.has("tec_watter") ) {
                String typeHome = advertInto.getString("type_home");
                if ( typeHome.contains(",")) {
                    typeHome = typeHome.replace(",", "");
                }
                int groupNumber = HelpFunctions.returnGroupNumberOfProperty(typeHome,null);
                if ( groupNumber==4 ) {
                    gridValueTitle.add("Вода:");
                } else {
                    gridValueTitle.add("ТEЦ:");
                }
                gridValue.add(advertInto.getString("tec_watter"));
            }

            if (advertInto.has("agency")) {
                if (advertInto.has("broker_id")) {
                    if ( advertInto.has("broker_name") ) {
                        brokerName.setVisibility(View.VISIBLE);
                        brokerName.setText("Брокер: "+ advertInto.getString("broker_name"));
                    }
                }

                JSONObject agencyObject = advertInto.getJSONObject("agency");
                if ( agencyObject.has("name") ) {
                    agencyName.setVisibility(View.VISIBLE);
                    agencyName.setText("Агенция: "+ agencyObject.getString("name"));
                }

                if ( agencyObject.has("address") ) {
                    agencyAddress.setVisibility(View.VISIBLE);
                    agencyAddress.setText(agencyObject.getString("address"));
                }
             }

            if ( advertInto.has("extri") ) {
                JSONArray extriArr = advertInto.getJSONArray("extri");
                if ( extriArr.length()>0 ) {
                    gridValueTitle.add("Вид строителство:");
                    gridValue.add(extriArr.getString(0));
                }
            }

            if ( advertInto.has("phone") ) {
                advertPhone=advertInto.getString("phone");
            }


            if ( gridValue.size()>0 ) {
                GridViewPropertyFeaturesAdapter adapter = new GridViewPropertyFeaturesAdapter(AdvertActivity.this,gridValueTitle,gridValue);
                feturesGridView.setAdapter(adapter);
            }


            existsAdvertNotepad = SQLite.select().from(AdvertNotepad.class)
                    .where(AdvertNotepad_Table.advert_id.is(advertID))
                    .querySingle();

            MenuItem itemFavourite = mMenu.findItem(R.id.addFavouritesBar);
            if ( itemFavourite!=null ) {
                if (existsAdvertNotepad!=null) {
                    itemFavourite.setIcon(R.drawable.ic_favorite_white_24dp);
                } else {
                    itemFavourite.setIcon(R.drawable.ic_favorite_black_24dp);
                }
            }
        }
    }

//    @Override
//    public void onLocationChanged(Location location) {
//        latitude=String.valueOf(location.getLatitude());
//        longitude=String.valueOf(location.getLongitude());
//    }
}
