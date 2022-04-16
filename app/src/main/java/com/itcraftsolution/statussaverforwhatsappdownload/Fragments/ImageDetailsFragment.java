package com.itcraftsolution.statussaverforwhatsappdownload.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bumptech.glide.Glide;
import com.itcraftsolution.statussaverforwhatsappdownload.R;
import com.itcraftsolution.statussaverforwhatsappdownload.databinding.FragmentImageDetailsBinding;

public class ImageDetailsFragment extends Fragment {

    private FragmentImageDetailsBinding binding;
    private String ImageUri;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentImageDetailsBinding.inflate(getLayoutInflater());

        LoadData();

        return binding.getRoot();
    }

    private void LoadData()
    {
        SharedPreferences spf = requireContext().getSharedPreferences("SendDetails", Context.MODE_PRIVATE);
        ImageUri = spf.getString("URI" , null);
        Uri uri = Uri.parse(ImageUri);

//        binding.FullSizeImage.setImageURI(uri);
        Glide.with(requireContext()).load(uri).into(binding.FullSizeImage);
    }
}