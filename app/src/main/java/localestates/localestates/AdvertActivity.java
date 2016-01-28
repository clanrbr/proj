package localestates.localestates;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.ms.square.android.expandabletextview.ExpandableTextView;
import com.nineoldandroids.view.ViewHelper;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;

import adapters.GridViewPropertyFeaturesAdapter;
import interfaces.AsyncResponseLoadAdvert;
import localEstatesHttpRequests.HTTPGETAdvert;
import utils.ExpandableHeightGridView;
import utils.HelpFunctions;

/**
 * Created by macbook on 1/26/16.
 */
public class AdvertActivity extends AppCompatActivity implements ObservableScrollViewCallbacks ,
        AsyncResponseLoadAdvert {

    private String googleMapsAPI="AIzaSyCNBVx3m2Q0q2oo4FQhA4UdAyuTaCQ0BRg";
    private String advertTitle;
    private ImageView mImageView;
    private Toolbar mToolbarView;
    private TextView priceLabel;
    private TextView placeLabel;
    private TextView brokerName;
    private TextView agencyName;
    private TextView agencyAddress;
    private ExpandableTextView moreInfoLabelExpand;
    private FrameLayout mapBar;
    private ExpandableHeightGridView feturesGridView;
    private ObservableScrollView mScrollView;
    private int mParallaxImageHeight;
    private JSONObject advertInto;
    private HTTPGETAdvert asyncLoadAdvert = new HTTPGETAdvert();

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

        asyncLoadAdvert.delegate=this;

        priceLabel = (TextView) findViewById(R.id.priceLabel);
        placeLabel = (TextView) findViewById(R.id.placeLabel);
        feturesGridView = (ExpandableHeightGridView) findViewById(R.id.features);
        mapBar = (FrameLayout) findViewById(R.id.mapBar);
        moreInfoLabelExpand = (ExpandableTextView) findViewById(R.id.moreInfoLabelExpand);
        brokerName = (TextView) findViewById(R.id.brokerName);
        agencyName = (TextView) findViewById(R.id.agencyName);
        agencyAddress = (TextView) findViewById(R.id.agencyAddress);

        // change color of icon
        ImageView mapImageView = (ImageView) findViewById(R.id.mapImageView);
        mapImageView.setColorFilter(ContextCompat.getColor(AdvertActivity.this, R.color.material_lime_900));

        mapBar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent mapIntent = new Intent(AdvertActivity.this,MapActivity.class);
                if ( advertInto.has("points") ) {
                    try {
                        mapIntent.putExtra("points",advertInto.getString("points"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                if ( advertInto.has("coordinates") ) {
                    try {
                        mapIntent.putExtra("coordinates",advertInto.getString("coordinates"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                if ( advertInto.has("rub") ) {
                    String value= null;
                    try {
                        value = advertInto.getString("rub");
                        if ( advertInto.has("type_home") ) {
                            value=value + " " + advertInto.getString("type_home");
                        }
                        mapIntent.putExtra("title",value);
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
    }

    @Override
    public void onUpOrCancelMotionEvent(ScrollState scrollState) {
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public void processFinishLoadAdvert(JSONObject output) throws JSONException {
        advertInto=new JSONObject();
        if (output!=null) {
            advertInto=output;
            ArrayList<CharSequence> gridValueTitle = new ArrayList<CharSequence>();
            ArrayList<CharSequence> gridValue = new ArrayList<CharSequence>();
            if ( advertInto.has("pictures") ) {
                JSONArray picturesArray = new JSONArray();
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
//                moreInfoLabel.setText(advertInto.getString("additional_information"));

                // IMPORTANT - call setText on the ExpandableTextView to set the text content to displ  ay
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
                int groupNumber= HelpFunctions.returnGroupNumberOfProperty(typeHome,null,0);
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
                int groupNumber = HelpFunctions.returnGroupNumberOfProperty(typeHome,null,0);
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
                        brokerName.setText("Брокер: "+ advertInto.getString("broker_name"));
                    }
                }

                JSONObject agencyObject = advertInto.getJSONObject("agency");
                if ( agencyObject.has("name") ) {
                    agencyName.setText("Агенция: "+ agencyObject.getString("name"));
                }

                if ( agencyObject.has("address") ) {
                    agencyAddress.setText(agencyObject.getString("address"));
                }
             }


            if ( gridValue.size()>0 ) {
                GridViewPropertyFeaturesAdapter adapter = new GridViewPropertyFeaturesAdapter(AdvertActivity.this,gridValueTitle,gridValue);
                feturesGridView.setAdapter(adapter);
            }
        }
    }
}
