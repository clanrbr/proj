package Adapters;

//import android.app.Fragment;
//import android.app.FragmentManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.Locale;

import fragments.CheckAndRadioBoxesFragment;

//import utils.CheckAndRadioBoxesFragment;

/**
 * Created by Ado on 12/22/2015.
 */
public class SectionPageAdapter extends FragmentPagerAdapter {
    // END_INCLUDE (fragment_pager_adapter)

    public SectionPageAdapter(FragmentManager fm) {
        super(fm);
    }

    // BEGIN_INCLUDE (fragment_pager_adapter_getitem)
    /**
     * Get fragment corresponding to a specific position. This will be used to populate the
//     //* contents of the {@link V i ew Pager}.
//     *
     * @param position Position to fetch fragment for.
     * @return Fragment for specified position.
     */
    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a DummySectionFragment (defined as a static inner class
        // below) with the page number as its lone argument.
        Fragment fragment = new CheckAndRadioBoxesFragment();
        Bundle args = new Bundle();
        args.putInt(CheckAndRadioBoxesFragment.ARG_SECTION_NUMBER, position + 1);
        fragment.setArguments(args);
        return fragment;
    }
    // END_INCLUDE (fragment_pager_adapter_getitem)

    // BEGIN_INCLUDE (fragment_pager_adapter_getcount)
    /**
     * Get number of pages the {@link ViewPager} should render.
     *
     * @return Number of fragments to be rendered as pages.
     */
    @Override
    public int getCount() {
        // Show 3 total pages.
        return 1;
    }
    // END_INCLUDE (fragment_pager_adapter_getcount)

    // BEGIN_INCLUDE (fragment_pager_adapter_getpagetitle)
    /**
     * Get title for each of the pages. This will be displayed on each of the tabs.
     *
     * @param position Page to fetch title for.
     * @return Title for specified page.
     */
    @Override
    public CharSequence getPageTitle(int position) {
        Locale l = Locale.getDefault();
        switch (position) {
            case 0:
                return "Da";
//                return getString(R.string.title_section1).toUpperCase(l);
            case 1:
                return "Da 1";
//                return getString(R.string.title_section2).toUpperCase(l);
            case 2:
                return "Da 2";
//                return getString(R.string.title_section3).toUpperCase(l);
        }
        return null;
    }
    // END_INCLUDE (fragment_pager_adapter_getpagetitle)
}
