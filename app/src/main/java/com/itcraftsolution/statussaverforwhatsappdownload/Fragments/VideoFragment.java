package com.itcraftsolution.statussaverforwhatsappdownload.Fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;

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
        spf = requireContext().getSharedPreferences("FolderPermission", Context.MODE_PRIVATE);

        isFolderPermissionGranted = spf.getBoolean("isGranted", false);
        istreeUri = spf.getString("PATH", null);


            if(istreeUri != null)
            {
                Statues model;

                requireContext().getContentResolver().takePersistableUriPermission(Uri.parse(istreeUri), Intent.FLAG_GRANT_READ_URI_PERMISSION);
                DocumentFile file = DocumentFile.fromTreeUri(requireContext(), Uri.parse(istreeUri));
                DocumentFile[] documentFiles = file.listFiles();

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


        if(list.isEmpty())
        {
            binding.rvVideo.setVisibility(View.GONE);
            binding.VNotFoundImage.setVisibility(View.VISIBLE);
        }
        else {
            adapter = new VideoRecyclerAdapter(requireContext(), list);
            binding.rvVideo.setAdapter(adapter);
            binding.rvVideo.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        }
        return binding.getRoot();
    }
    private void setupRecyclerview(ArrayList<Statues> statusList)
        {
            adapter = new VideoRecyclerAdapter(requireContext(), statusList);
            binding.rvVideo.setAdapter(adapter);
            binding.rvVideo.setLayoutManager(new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL));
        }
}