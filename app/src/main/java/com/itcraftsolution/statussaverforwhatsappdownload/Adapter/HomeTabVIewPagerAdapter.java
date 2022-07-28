package com.itcraftsolution.statussaverforwhatsappdownload.Adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.itcraftsolution.statussaverforwhatsappdownload.Fragments.ImageFragment;
import com.itcraftsolution.statussaverforwhatsappdownload.Fragments.SaveFragment;
import com.itcraftsolution.statussaverforwhatsappdownload.Fragments.VideoFragment;

public class HomeTabVIewPagerAdapter extends FragmentStateAdapter {

    private String[] tabTitles = new String[]{"IMAGES", "VIDEOS", "SAVED"};

    public HomeTabVIewPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new ImageFragment();
            case 1:
                return new VideoFragment();
            case 2:
                return new SaveFragment();

        }

        return new ImageFragment();
    }

    @Override
    public int getItemCount() {
        return tabTitles.length;
    }
}
