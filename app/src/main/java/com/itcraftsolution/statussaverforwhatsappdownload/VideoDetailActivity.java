package com.itcraftsolution.statussaverforwhatsappdownload;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.MediaController;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.itcraftsolution.statussaverforwhatsappdownload.Models.Statues;
import com.itcraftsolution.statussaverforwhatsappdownload.Utils.Utils;
import com.itcraftsolution.statussaverforwhatsappdownload.databinding.ActivityVideoDetailBinding;

import java.io.File;

public class VideoDetailActivity extends AppCompatActivity {

    private ActivityVideoDetailBinding binding;
    private String VideoUri, filepath;
    private Animation fabOpen, fabClose, rotateForward, rotatebackward;
    private int DURATION = 300;
    private boolean isOpen = false;
    private boolean isSaved;
    private Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityVideoDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        LoadData();

        fabOpen = AnimationUtils.loadAnimation(VideoDetailActivity.this, R.anim.fab_open);
        fabClose = AnimationUtils.loadAnimation(VideoDetailActivity.this, R.anim.fab_close);
        rotateForward = AnimationUtils.loadAnimation(VideoDetailActivity.this, R.anim.rotate_forward);
        rotatebackward = AnimationUtils.loadAnimation(VideoDetailActivity.this, R.anim.rotate_backward);

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
                shareIntent.setType("video/mp4");
                shareIntent.putExtra(Intent.EXTRA_TEXT, "I'm using @StatusSaver app: https://play.google.com/store/apps/details?id=com.itcraftsolution.picturepoint. #Statues #StatusSaver");
                shareIntent.putExtra(Intent.EXTRA_STREAM, Uri.parse(VideoUri));
                startActivity(Intent.createChooser(shareIntent, "Share Statues"));
            }
        });
        binding.fabDetailsDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!checkPermission())
                {
                    showPermission();
                }else{
                    File file = new File(filepath);
                    Statues status = new Statues(file.getName() , filepath , file , uri);
                    Utils.saveImgIntoGallery( VideoDetailActivity.this, status);
                }
            }
        });
    }
    private void LoadData()
    {
        SharedPreferences spf = VideoDetailActivity.this.getSharedPreferences("SendDetails", Context.MODE_PRIVATE);
        VideoUri = spf.getString("URI" , null);
        filepath = spf.getString("FILE_PATH" , null);
        isSaved = spf.getBoolean("isSaved" , false);

        if(isSaved)
        {
            binding.fabDetailsDownload.setVisibility(View.GONE);
        }
        uri = Uri.parse(VideoUri);

        binding.VideoView.setVideoURI(uri);
        MediaController mediaController = new MediaController(VideoDetailActivity.this);
        mediaController.setAnchorView(binding.VideoView);
        mediaController.setMediaPlayer(binding.VideoView);
        binding.VideoView.setMediaController(mediaController);
        binding.VideoView.start();
        binding.VideoView.requestFocus();

        binding.VideoView.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                mp.setLooping(true);
            }
        });
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

    private void showPermission()
    {
        // permission for 23 to 29 SDK
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
        {
            if(ContextCompat.checkSelfPermission(VideoDetailActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(VideoDetailActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
            {
                ActivityCompat.requestPermissions(VideoDetailActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},100);
            }
        }
    }

    private boolean checkPermission() {

        int write = ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int read = ContextCompat.checkSelfPermission(getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE);

        return write == PackageManager.PERMISSION_GRANTED &&
                read == PackageManager.PERMISSION_GRANTED;

    }


}