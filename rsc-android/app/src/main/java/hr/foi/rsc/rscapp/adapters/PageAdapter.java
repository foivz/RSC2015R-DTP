package hr.foi.rsc.rscapp.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import hr.foi.rsc.rscapp.fragments.HelperFragment;
import hr.foi.rsc.rscapp.fragments.GameFragment;

/**
 * Created by hrvoje on 21/11/15.
 */
public class PageAdapter extends FragmentPagerAdapter {
    public PageAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch(position) {
            case 0: return new GameFragment();
            default: return new HelperFragment();
        }
    }

    @Override
    public int getCount() {
        return 2;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch(position) {
            case 0: return "Map";
            default: return "Timer";
        }
    }
}
