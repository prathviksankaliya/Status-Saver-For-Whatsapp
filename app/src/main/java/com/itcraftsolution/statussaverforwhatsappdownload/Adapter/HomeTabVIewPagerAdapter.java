package com.itcraftsolution.statussaverforwhatsappdownload.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.itcraftsolution.statussaverforwhatsappdownload.Fragments.ResentFragment;
import com.itcraftsolution.statussaverforwhatsappdownload.Fragments.SaveFragment;

public class HomeTabVIewPagerAdapter extends FragmentStateAdapter {

    private String [] tabTitles = new String[]{"RECENT" , "SAVED"};

    public HomeTabVIewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position)
        {
            case 0 :
                return new ResentFragment();
            case 1:
                return new SaveFragment();
        }

        return new ResentFragment();
    }

    @Override
    public int getItemCount() {
        return tabTitles.length;
    }
}
