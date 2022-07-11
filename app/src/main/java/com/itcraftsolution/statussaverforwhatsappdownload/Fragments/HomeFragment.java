package com.itcraftsolution.statussaverforwhatsappdownload.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.viewpager2.widget.ViewPager2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayoutMediator;
import com.itcraftsolution.statussaverforwhatsappdownload.Adapter.HomeTabVIewPagerAdapter;
import com.itcraftsolution.statussaverforwhatsappdownload.R;
import com.itcraftsolution.statussaverforwhatsappdownload.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private HomeTabVIewPagerAdapter adapter;
    private String [] tabTitles = new String[]{"IMAGES" , "VIDEOS","SAVED"};

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(getLayoutInflater());

        adapter = new HomeTabVIewPagerAdapter(requireActivity());
      binding.vpHome.setAdapter(adapter);

      new TabLayoutMediator(binding.tabHomeLayout, binding.vpHome,(((tab, position) -> tab.setText(tabTitles[position])))).attach();


        return binding.getRoot();
    }
}