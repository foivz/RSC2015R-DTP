package hr.foi.rsc.rscapp.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import hr.foi.rsc.rscapp.fragments.CommunicationFragment;
import hr.foi.rsc.rscapp.fragments.HelperFragment;
import hr.foi.rsc.rscapp.fragments.GameFragment;
import hr.foi.rsc.rscapp.fragments.ScanFragment;

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
            case 1: return new CommunicationFragment();
            case 2: return new HelperFragment();
            default: return new ScanFragment();
        }
    }

    @Override
    public int getCount() {
        return 4;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        switch(position) {
            case 0: return "Map";
            case 1: return "Communication";
            case 2: return "Timer";
            default: return "Capture flag";
        }
    }
}
