
package com.itcraftsolution.statussaverforwhatsappdownload.Fragments;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.itcraftsolution.statussaverforwhatsappdownload.Adapter.SavedRecyclerAdapter;
import com.itcraftsolution.statussaverforwhatsappdownload.CustomDialog.Custom_Dialog;
import com.itcraftsolution.statussaverforwhatsappdownload.Models.Statues;
import com.itcraftsolution.statussaverforwhatsappdownload.Utils.Utils;
import com.itcraftsolution.statussaverforwhatsappdownload.databinding.FragmentSaveBinding;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;


public class SaveFragment extends Fragment {

    public SaveFragment() {
        // Required empty public constructor
    }

    private FragmentSaveBinding binding;
    private ArrayList<Statues> list;
    private SavedRecyclerAdapter adapter;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSaveBinding.inflate(getLayoutInflater());

        list = new ArrayList<>();

        binding.savedRefershView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (Utils.STATUS_SAVER_DIR.exists()) {
                    binding.VNotFoundImage.setVisibility(View.GONE);
                    binding.rvSaved.setVisibility(View.VISIBLE);
                    list.clear();
                    getData(Utils.STATUS_SAVER_DIR);
                }
                binding.savedRefershView.setRefreshing(false);

            }
        });

        return binding.getRoot();
    }


    private void getData(File file) {

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

            if (Uri.fromFile(singlefile).toString().endsWith(".png") || Uri.fromFile(singlefile).toString().endsWith(".jpg") || Uri.fromFile(singlefile).toString().endsWith(".mp4")
                    && !Uri.fromFile(singlefile).toString().endsWith(".")) {
                model = new Statues("whats " + i, allfiles[i].getAbsolutePath(), singlefile, Uri.fromFile(singlefile));

                list.add(model);
            }
        }
        setRecyclerView(list);
    }

    private void setRecyclerView(ArrayList<Statues> statues) {
        adapter = new SavedRecyclerAdapter(requireContext(), statues);
        binding.rvSaved.setAdapter(adapter);
        binding.rvSaved.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));

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

    @Override
    public void onResume() {
        super.onResume();
        if (!checkPermission()) {
            showPermission();
        } else {
            if (Utils.STATUS_SAVER_DIR.exists()) {
                binding.rvSaved.setVisibility(View.VISIBLE);
                binding.VNotFoundImage.setVisibility(View.GONE);
                getData(Utils.STATUS_SAVER_DIR);
            }
        }
        if (list.isEmpty()) {
            binding.rvSaved.setVisibility(View.GONE);
            Custom_Dialog dialog = new Custom_Dialog(requireContext());
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
            dialog.setCancelable(false);
            dialog.show();
            binding.VNotFoundImage.setVisibility(View.VISIBLE);
        }
    }
}

