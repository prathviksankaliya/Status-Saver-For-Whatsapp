package com.itcraftsolution.statussaverforwhatsappdownload.Fragments;

import android.app.ProgressDialog;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.itcraftsolution.statussaverforwhatsappdownload.Adapter.ImageRecyclerAdapter;
import com.itcraftsolution.statussaverforwhatsappdownload.Adapter.ResentDownloadAdapter;
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
                    binding.imageRefershView.setRefreshing(false);
                    adapter.notifyDataSetChanged();
                    dialog.dismiss();
                }
            });
        } else {
            binding.imageRefershView.setRefreshing(false);
            binding.rvImage.setVisibility(View.GONE);
            binding.llNotFound.setVisibility(View.VISIBLE);
            dialog.dismiss();
            Toast.makeText(requireContext(), "Can't Whatsapp File Find!! ", Toast.LENGTH_SHORT).show();
        }

        adapter = new ImageRecyclerAdapter(requireContext(), list);
        binding.rvImage.setAdapter(adapter);
        binding.rvImage.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        dialog.dismiss();
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
}