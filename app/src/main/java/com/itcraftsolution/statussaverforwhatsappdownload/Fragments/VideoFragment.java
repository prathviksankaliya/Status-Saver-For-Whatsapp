package com.itcraftsolution.statussaverforwhatsappdownload.Fragments;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.itcraftsolution.statussaverforwhatsappdownload.Adapter.VideoRecyclerAdapter;
import com.itcraftsolution.statussaverforwhatsappdownload.Models.Statues;
import com.itcraftsolution.statussaverforwhatsappdownload.Utils.Utils;
import com.itcraftsolution.statussaverforwhatsappdownload.databinding.FragmentVideoBinding;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class VideoFragment extends Fragment {


    private FragmentVideoBinding binding;
    private ArrayList<Statues> list;
    private VideoRecyclerAdapter adapter;
    private SharedPreferences spf;
    private boolean isFolderPermissionGranted;
    private String istreeUri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentVideoBinding.inflate(getLayoutInflater());


        list = new ArrayList<>();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            getData();
        } else {
            if (!checkPermission()) {
                showPermission();
            } else {
                if (Utils.STATUS_DIRECTORY.exists()) {
                    loadData(Utils.STATUS_DIRECTORY);

                } else if (Utils.STATUS_DIRECTORY_NEW.exists()) {

                    loadData(Utils.STATUS_DIRECTORY_NEW);

                } else if (Utils.STATUS_DIRECTORY_GBWHATSAPP.exists()) {

                    loadData(Utils.STATUS_DIRECTORY_GBWHATSAPP);

                } else {
                    Toast.makeText(requireContext(), "Can't Whatsapp Files Find!! ", Toast.LENGTH_SHORT).show();
                }
            }

        }

        binding.refreshVideo.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    getData();
                } else {

                    list = new ArrayList<>();
                    if (Utils.STATUS_DIRECTORY.exists()) {
                        loadData(Utils.STATUS_DIRECTORY);
                    } else if (Utils.STATUS_DIRECTORY_NEW.exists()) {
                        loadData(Utils.STATUS_DIRECTORY_NEW);
                    } else if (Utils.STATUS_DIRECTORY_GBWHATSAPP.exists()) {
                        loadData(Utils.STATUS_DIRECTORY_GBWHATSAPP);
                    }
                }
                binding.refreshVideo.setRefreshing(false);
            }
        });
        if (list.isEmpty()) {
            binding.rvVideo.setVisibility(View.GONE);
            binding.VNotFoundImage.setVisibility(View.VISIBLE);
        } else {
            adapter = new VideoRecyclerAdapter(requireContext(), list);
            binding.rvVideo.setAdapter(adapter);
            binding.rvVideo.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        }
        return binding.getRoot();
    }

    private void setupRecyclerview(ArrayList<Statues> statusList) {
        adapter = new VideoRecyclerAdapter(requireContext(), statusList);
        binding.rvVideo.setAdapter(adapter);
        binding.rvVideo.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
    }

    private void getData() {
        spf = requireContext().getSharedPreferences("FolderPermission", Context.MODE_PRIVATE);
        isFolderPermissionGranted = spf.getBoolean("isGranted", false);
        istreeUri = spf.getString("PATH", null);

        if (istreeUri != null) {
            Statues model;

            requireContext().getContentResolver().takePersistableUriPermission(Uri.parse(istreeUri), Intent.FLAG_GRANT_READ_URI_PERMISSION);
            DocumentFile file = DocumentFile.fromTreeUri(requireContext(), Uri.parse(istreeUri));
            DocumentFile[] documentFiles = file.listFiles();
            list.clear();
            for (int i = 0; i < documentFiles.length; i++) {
                documentFiles[i].getUri().toString();
                DocumentFile singlefile = documentFiles[i];

                if (singlefile.getUri().toString().endsWith(".mp4")) {
                    File file1 = new File(singlefile.getUri().toString());
                    model = new Statues("whats " + i, documentFiles[i].getName(), file1, singlefile.getUri());
                    list.add(model);

                }
            }
            setupRecyclerview(list);
        }
    }


    private void loadData(File file) {

        Statues model;

        File[] allfiles = file.listFiles();

        Arrays.sort(allfiles, ((o1, o2) -> {
            if (o1.lastModified() > o2.lastModified()) {
                return -1;
            } else if (o1.lastModified() < o2.lastModified()) {
                return +1;
            } else {
                return 0;
            }
        }));
        list.clear();
        for (int i = 0; i < allfiles.length; i++) {
            File singlefile = allfiles[i];

            if (Uri.fromFile(singlefile).toString().endsWith(".mp4")) {
                model = new Statues("whats " + i, allfiles[i].getAbsolutePath(), singlefile, Uri.fromFile(singlefile));

                list.add(model);
            }
        }
        setupRecyclerview(list);
    }

    private void showPermission() {
        // permission for 23 to 29 SDK
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED &&
                    ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
            }
        }

    }

    private boolean checkPermission() {

        int write = ContextCompat.checkSelfPermission(requireContext().getApplicationContext(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int read = ContextCompat.checkSelfPermission(requireContext().getApplicationContext(),
                Manifest.permission.READ_EXTERNAL_STORAGE);

        return write == PackageManager.PERMISSION_GRANTED &&
                read == PackageManager.PERMISSION_GRANTED;

    }
}