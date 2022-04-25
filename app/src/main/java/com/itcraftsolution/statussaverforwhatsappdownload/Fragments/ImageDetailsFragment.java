package com.itcraftsolution.statussaverforwhatsappdownload.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.StrictMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.bumptech.glide.Glide;
import com.itcraftsolution.statussaverforwhatsappdownload.Models.Statues;
import com.itcraftsolution.statussaverforwhatsappdownload.R;
import com.itcraftsolution.statussaverforwhatsappdownload.Utils.Utils;
import com.itcraftsolution.statussaverforwhatsappdownload.databinding.FragmentImageDetailsBinding;

import java.io.File;

public class ImageDetailsFragment extends Fragment {

    private FragmentImageDetailsBinding binding;
    private String ImageUri, filepath;
    private Animation fabOpen, fabClose, rotateForward, rotatebackward;
    private int DURATION = 300;
    private boolean isOpen = false;
    private Uri uri;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentImageDetailsBinding.inflate(getLayoutInflater());

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        LoadData();

        fabOpen = AnimationUtils.loadAnimation(requireContext(), R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(requireContext(), R.anim.fab_close);
        rotateForward = AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_forward);
        rotatebackward = AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_backward);

        rotateForward.setDuration(DURATION);
        rotatebackward.setDuration(DURATION);
        fabOpen.setDuration(DURATION);
        fabClose.setDuration(DURATION);

        binding.fabMainDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateFab();
            }
        });

        binding.fabDetailsShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("image/jpg");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "Check Out This Whatsapp Status From @StatusSaverForWhatsapp #Statues #StatusSaver");
                shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + filepath));
                startActivity(Intent.createChooser(shareIntent, "Share Image"));
            }
        });

        binding.fabDetailsDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File file = new File(filepath);
                Statues status = new Statues(file.getName() , filepath , file , uri);
                Utils.copyFile(status , requireContext());
            }
        });

        return binding.getRoot();
    }

    private void LoadData()
    {
        SharedPreferences spf = requireContext().getSharedPreferences("SendDetails", Context.MODE_PRIVATE);
        ImageUri = spf.getString("URI" , null);
        filepath = spf.getString("FILE_PATH" , null);
        uri = Uri.parse(ImageUri);

//        binding.FullSizeImage.setImageURI(uri);
        Glide.with(requireContext()).load(uri).into(binding.FullSizeImage);

    }

    private void animateFab() {
        if (isOpen) {
            binding.fabMainDetails.startAnimation(rotateForward);
            binding.fabDetailsShare.startAnimation(fabClose);
            binding.fabDetailsDownload.startAnimation(fabClose);

            binding.fabDetailsShare.setClickable(false);
            binding.fabDetailsDownload.setClickable(false);

            isOpen = false;
        } else {
            binding.fabMainDetails.startAnimation(rotatebackward);
            binding.fabDetailsShare.startAnimation(fabOpen);
            binding.fabDetailsDownload.startAnimation(fabOpen);

            binding.fabDetailsShare.setClickable(true);
            binding.fabDetailsDownload.setClickable(true);

            isOpen = true;
        }
    }
}