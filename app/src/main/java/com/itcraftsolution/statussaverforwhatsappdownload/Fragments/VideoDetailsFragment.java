package com.itcraftsolution.statussaverforwhatsappdownload.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.media.session.MediaController;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.itcraftsolution.statussaverforwhatsappdownload.R;
import com.itcraftsolution.statussaverforwhatsappdownload.databinding.FragmentVideoBinding;
import com.itcraftsolution.statussaverforwhatsappdownload.databinding.FragmentVideoDetailsBinding;


public class VideoDetailsFragment extends Fragment {

    public VideoDetailsFragment() {
        // Required empty public constructor
    }

    private FragmentVideoDetailsBinding binding;
    private String VideoUri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentVideoDetailsBinding.inflate(getLayoutInflater());

        LoadData();


        return binding.getRoot();
    }

    private void LoadData()
    {
        SharedPreferences spf = requireContext().getSharedPreferences("SendDetails", Context.MODE_PRIVATE);
        VideoUri = spf.getString("URI" , null);
        Uri uri = Uri.parse(VideoUri);

        binding.VideoView.setVideoURI(uri);
        binding.VideoView.start();
        binding.VideoView.requestFocus();

        binding.VideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
    }
}
