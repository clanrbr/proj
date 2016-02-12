package localestates.localestates;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by macbook on 2/12/16.
 */
public class FavouriteActivity extends AppCompatActivity {

//    https://github.com/gene-sbay/AndStudio.FragmentDemo1/tree/master/app/src/main/java/com/demo/fragmentdemo

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

//        Configuration config = getResources().getConfiguration();
//
//        FragmentManager fragmentManager = getFragmentManager();
//
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
//        /**
//         * Check the device orientation and act accordingly
//         */
//        if (config.orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            /**
//             * Landscape mode of the device
//             */
//
//            LM_Fragment ls_fragment = new LM_Fragment();
//            fragmentTransaction.replace(android.R.id.content, ls_fragment);
//        }
//        else
//        {
//            /**
//             * Portrait mode of the device
//             */
//            PM_Fragment pm_fragment = new PM_Fragment();
//            fragmentTransaction.replace(android.R.id.content, pm_fragment);
//        }
//
//        fragmentTransaction.commit();


    }
}
