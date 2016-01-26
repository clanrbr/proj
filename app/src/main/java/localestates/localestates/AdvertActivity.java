package localestates.localestates;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import android.support.v7.widget.Toolbar;
import android.widget.AbsListView;
import android.widget.TextView;

import com.github.ksoichiro.android.observablescrollview.ObservableScrollView;
import com.github.ksoichiro.android.observablescrollview.ObservableScrollViewCallbacks;
import com.github.ksoichiro.android.observablescrollview.ScrollState;
import com.github.ksoichiro.android.observablescrollview.ScrollUtils;
import com.nineoldandroids.view.ViewHelper;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import interfaces.AsyncResponseLoadAdvert;
import localEstatesHttpRequests.HTTPGETAdvert;

/**
 * Created by macbook on 1/26/16.
 */
public class AdvertActivity extends AppCompatActivity implements ObservableScrollViewCallbacks , AsyncResponseLoadAdvert {

    private String advertTitle;
    private View mImageView;
    private View mToolbarView;
    private TextView priceLabel;
    private TextView placeLabel;
    private TextView moreInfoLabel;
    private TextView quadratureLabel;
    private TextView floorLabel;
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
        moreInfoLabel = (TextView) findViewById(R.id.moreInfoLabel);
        quadratureLabel = (TextView) findViewById(R.id.quadratureLabel);
        floorLabel = (TextView) findViewById(R.id.floorLabel);

        Bundle extras = getIntent().getExtras();
        if(extras != null) {
            String advertID = extras.getString("advertID");
            if ( (advertID!=null) && (!advertID.equals("")) ) {
                String urlGetAdvert="http://api.imot.bg/mobile_api/details?id="+advertID;
                asyncLoadAdvert.execute(urlGetAdvert);
            }
        } else {

        }

        mImageView = findViewById(R.id.image);
        mToolbarView = findViewById(R.id.toolbar);
        mToolbarView.setBackgroundColor(ScrollUtils.getColorWithAlpha(0, getResources().getColor(R.color.main_color_500)));

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
    public void processFinishLoadAdvert(JSONObject output) throws JSONException {
        advertInto=new JSONObject();
        if (output!=null) {
            advertInto=output;
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
                    value=value + advertInto.getString("metric");
                }
                quadratureLabel.setText(value);
            }

            if ( advertInto.has("town") ) {
                String value = advertInto.getString("town");
                if ( advertInto.has("raion") ) {
                    value=value + ", " + advertInto.getString("raion");
                }
                placeLabel.setText(value);
            }

            if ( advertInto.has("additional_information") ) {
                moreInfoLabel.setText(advertInto.getString("additional_information"));
            }

            if ( advertInto.has("floor_regulation") ) {
                String value=advertInto.getString("floor_regulation");
                if ( advertInto.has("floor_max") ) {
                    value=value + " от " + advertInto.getString("floor_max");
                }
                floorLabel.setText(value);
            }

            if ( advertInto.has("floor_regulation") )  {

            }

        }

    }
}
