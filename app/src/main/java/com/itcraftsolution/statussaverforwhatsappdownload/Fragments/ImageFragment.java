package com.itcraftsolution.statussaverforwhatsappdownload.Fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.documentfile.provider.DocumentFile;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.itcraftsolution.statussaverforwhatsappdownload.Adapter.ImageRecyclerAdapter;
import com.itcraftsolution.statussaverforwhatsappdownload.Models.Statues;
import com.itcraftsolution.statussaverforwhatsappdownload.Utils.Utils;
import com.itcraftsolution.statussaverforwhatsappdownload.databinding.FragmentImageBinding;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class ImageFragment extends Fragment {

    private FragmentImageBinding binding;
    private ArrayList<Statues> list;
    private ImageRecyclerAdapter adapter;
    private ProgressDialog dialog;
    private SharedPreferences spf;
    private boolean isFolderPermissionGranted;
    private String istreeUri;

    @RequiresApi(api = Build.VERSION_CODES.Q)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentImageBinding.inflate(getLayoutInflater());

        dialog = new ProgressDialog(requireContext());
        dialog.setMessage("Statuses Loading ....");
        dialog.setCancelable(false);

        list = new ArrayList<>();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            spf = requireContext().getSharedPreferences("FolderPermission", Context.MODE_PRIVATE);

            isFolderPermissionGranted = spf.getBoolean("isGranted", false);

            istreeUri = spf.getString("PATH", null);

            if (isFolderPermissionGranted) {
                getData();
            }
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
                    binding.imageRefershView.setRefreshing(false);
                    Toast.makeText(requireContext(), "Can't Whatsapp Files Find!! ", Toast.LENGTH_SHORT).show();
                }
            }
        }

        binding.imageRefershView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    dialog.show();
                    getData();
                    binding.imageRefershView.setRefreshing(false);
                    dialog.dismiss();
                } else {
                    dialog.show();
                    list = new ArrayList<>();
                    if (Utils.STATUS_DIRECTORY.exists()) {
                        loadData(Utils.STATUS_DIRECTORY);
                    } else if (Utils.STATUS_DIRECTORY_NEW.exists()) {
                        loadData(Utils.STATUS_DIRECTORY_NEW);
                    } else if (Utils.STATUS_DIRECTORY_GBWHATSAPP.exists()) {
                        loadData(Utils.STATUS_DIRECTORY_GBWHATSAPP);
                    }
                    binding.imageRefershView.setRefreshing(false);
                    dialog.dismiss();
                }
            }
        });

        if (list.isEmpty()) {
            binding.rvImage.setVisibility(View.GONE);
            binding.VNotFoundImage.setVisibility(View.VISIBLE);
        }

        return binding.getRoot();
    }

    private void setupRecyclerview(ArrayList<Statues> statusList) {
        adapter = new ImageRecyclerAdapter(requireContext(), statusList);
        binding.rvImage.setAdapter(adapter);
        binding.rvImage.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
    }

    private void getData() {
        if (istreeUri != null) {
            requireContext().getContentResolver().takePersistableUriPermission(Uri.parse(istreeUri), Intent.FLAG_GRANT_READ_URI_PERMISSION);
            Statues model;

            DocumentFile file = DocumentFile.fromTreeUri(requireContext(), Uri.parse(istreeUri));
            DocumentFile[] documentFiles = file.listFiles();
            list.clear();
            for (int i = 0; i < documentFiles.length; i++) {
                documentFiles[i].getUri().toString();
                DocumentFile singlefile = documentFiles[i];

                if (singlefile.getUri().toString().endsWith(".png") || singlefile.getUri().toString().endsWith(".jpg")) {
                    File file1 = new File(singlefile.getUri().toString());
                    model = new Statues("whats " + i, documentFiles[i].getName(), file1, singlefile.getUri());
                    list.add(model);

                }
            }
            setupRecyclerview(list);
        }
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

            if (Uri.fromFile(singlefile).toString().endsWith(".png") || Uri.fromFile(singlefile).toString().endsWith(".jpg")) {
                model = new Statues("whats " + i, allfiles[i].getAbsolutePath(), singlefile, Uri.fromFile(singlefile));

                list.add(model);
            }
        }
        setupRecyclerview(list);
    }


    @Override
    public void onStart() {
        super.onStart();
        if (!checkPermission()) {
            showPermission();
        }
    }
}
