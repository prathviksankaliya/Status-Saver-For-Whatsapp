package com.itcraftsolution.statussaverforwhatsappdownload.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.itcraftsolution.statussaverforwhatsappdownload.R;
import com.itcraftsolution.statussaverforwhatsappdownload.databinding.FragmentSpalshBinding;

public class SpalshFragment extends Fragment {


    public SpalshFragment() {
        // Required empty public constructor
    }

    private FragmentSpalshBinding binding;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSpalshBinding.inflate(getLayoutInflater());

        String TagLine = "Status Saver";

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                FragmentTransaction fragmentTransaction = getParentFragmentManager().beginTransaction();
                fragmentTransaction.replace(R.id.frMainContainer , new HomeFragment());
                fragmentTransaction.commit();
            }
        }, 2100);
        return binding.getRoot();
    }
}