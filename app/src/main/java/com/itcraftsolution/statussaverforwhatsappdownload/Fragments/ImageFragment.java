package com.itcraftsolution.statussaverforwhatsappdownload.Fragments;

import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static android.os.Build.VERSION.SDK_INT;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Environment;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.itcraftsolution.statussaverforwhatsappdownload.Adapter.ImageRecyclerAdapter;
import com.itcraftsolution.statussaverforwhatsappdownload.Adapter.ResentDownloadAdapter;
import com.itcraftsolution.statussaverforwhatsappdownload.MainActivity;
import com.itcraftsolution.statussaverforwhatsappdownload.Models.Recents;
import com.itcraftsolution.statussaverforwhatsappdownload.Utils.Utils;
import com.itcraftsolution.statussaverforwhatsappdownload.databinding.FragmentImageBinding;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class ImageFragment extends Fragment {


    private FragmentImageBinding binding;
    private ArrayList<Recents> list;
    private ImageRecyclerAdapter adapter;
    private ProgressDialog dialog;
    private File userFile;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentImageBinding.inflate(getLayoutInflater());

        dialog = new ProgressDialog(requireContext());
        dialog.setMessage("Statuses Loading ....");
        dialog.setCancelable(false);
        dialog.show();
        list = new ArrayList<>();

        if (Utils.STATUS_DIRECTORY.exists()) {

            getData(Utils.STATUS_DIRECTORY);
            dialog.dismiss();
            binding.imageRefershView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    dialog.show();
                    list = new ArrayList<>();
                    getData(Utils.STATUS_DIRECTORY);
                    userFile = Utils.STATUS_DIRECTORY;
                    binding.imageRefershView.setRefreshing(false);
                    adapter.notifyDataSetChanged();
                    dialog.dismiss();
                }
            });

        } else if (Utils.STATUS_DIRECTORY_NEW.exists()) {

            getData(Utils.STATUS_DIRECTORY_NEW);
            dialog.dismiss();
            binding.imageRefershView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    dialog.show();
                    list = new ArrayList<>();
                    getData(Utils.STATUS_DIRECTORY_NEW);
                    userFile = Utils.STATUS_DIRECTORY_NEW;
                    binding.imageRefershView.setRefreshing(false);
                    adapter.notifyDataSetChanged();
                    dialog.dismiss();
                }
            });

        } else if (Utils.STATUS_DIRECTORY_GBWHATSAPP.exists()) {

            getData(Utils.STATUS_DIRECTORY_GBWHATSAPP);
            dialog.dismiss();
            binding.imageRefershView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    dialog.show();
                    list = new ArrayList<>();
                    getData(Utils.STATUS_DIRECTORY_GBWHATSAPP);
                    userFile = Utils.STATUS_DIRECTORY_GBWHATSAPP;
                    binding.imageRefershView.setRefreshing(false);
                    adapter.notifyDataSetChanged();
                    dialog.dismiss();
                }
            });
        } else {
            binding.imageRefershView.setRefreshing(false);
            dialog.dismiss();
            Toast.makeText(requireContext(), "Can't Whatsapp File Find!! ", Toast.LENGTH_SHORT).show();
        }

        if(list.isEmpty())
        {
            binding.rvImage.setVisibility(View.GONE);
            binding.VNotFoundImage.setVisibility(View.VISIBLE);
        }

            adapter = new ImageRecyclerAdapter(requireContext(), list);
            binding.rvImage.setAdapter(adapter);
            binding.rvImage.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
            dialog.dismiss();

        binding.btnAllowPermission.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.R)
            @Override
            public void onClick(View v) {
                if(!checkPermission())
                {
                    showPermissionDialog();
                    if(userFile != null)
                    {
                        getData(userFile);
                        binding.rvImage.setVisibility(View.VISIBLE);
                        binding.VNotFoundImage.setVisibility(View.GONE);
                        adapter.notifyDataSetChanged();
                    }
                    else {
                        Toast.makeText(requireContext(), "Some Went wrong!! user file", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    Toast.makeText(requireContext(), "Already Permission Granted !!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return binding.getRoot();
    }


    private void getData(File file) {

        Recents model;

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

        for (int i = 0; i < allfiles.length; i++) {
            File singlefile = allfiles[i];

            if (Uri.fromFile(singlefile).toString().endsWith(".png") || Uri.fromFile(singlefile).toString().endsWith(".jpg")) {
                model = new Recents("whats " + i, allfiles[i].getAbsolutePath(), singlefile, Uri.fromFile(singlefile));

                list.add(model);
            }
        }

    }


    private void showPermissionDialog() {
        if (SDK_INT >= Build.VERSION_CODES.R) {

            try {
                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                intent.addCategory("android.intent.category.DEFAULT");
                intent.setData(Uri.parse(String.format("package:%s", requireContext().getPackageName())));
                startActivityForResult(intent, 2000);
            } catch (Exception e) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivityForResult(intent, 2000);

            }

        } else
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE}, 333);
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    private boolean checkPermission() {
        if (SDK_INT >= Build.VERSION_CODES.R) {
            return Environment.isExternalStorageManager();
        } else {
            int write = ContextCompat.checkSelfPermission(requireContext(),
                    WRITE_EXTERNAL_STORAGE);
            int read = ContextCompat.checkSelfPermission(requireContext(),
                    READ_EXTERNAL_STORAGE);

            return write == PackageManager.PERMISSION_GRANTED &&
                    read == PackageManager.PERMISSION_GRANTED;
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @org.jetbrains.annotations.NotNull String[] permissions, @NonNull @org.jetbrains.annotations.NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 333) {
            if (grantResults.length > 0) {
                boolean write = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean read = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if (read && write){

                }else {

                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable @org.jetbrains.annotations.Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 2000) {
            if (SDK_INT >= Build.VERSION_CODES.R) {
                if (Environment.isExternalStorageManager()) {

                } else {

                }
            }
        }
    }

}