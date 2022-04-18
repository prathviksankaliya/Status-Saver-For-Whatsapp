package com.itcraftsolution.statussaverforwhatsappdownload.Fragments;

import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.itcraftsolution.statussaverforwhatsappdownload.Adapter.ImageRecyclerAdapter;
import com.itcraftsolution.statussaverforwhatsappdownload.Adapter.ResentDownloadAdapter;
import com.itcraftsolution.statussaverforwhatsappdownload.Adapter.VideoRecyclerAdapter;
import com.itcraftsolution.statussaverforwhatsappdownload.Models.Recents;
import com.itcraftsolution.statussaverforwhatsappdownload.R;
import com.itcraftsolution.statussaverforwhatsappdownload.Utils.Utils;
import com.itcraftsolution.statussaverforwhatsappdownload.databinding.FragmentVideoBinding;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;

public class VideoFragment extends Fragment {


    private FragmentVideoBinding binding;
    private ArrayList<Recents> list;
    private VideoRecyclerAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        binding = FragmentVideoBinding.inflate(getLayoutInflater());


        list = new ArrayList<>();

        if (Utils.STATUS_DIRECTORY.exists()) {

            getData(Utils.STATUS_DIRECTORY);
            binding.refreshVideo.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    list.clear();
                    getData(Utils.STATUS_DIRECTORY);
                    binding.refreshVideo.setRefreshing(false);
                    adapter.notifyDataSetChanged();
                }
            });

        } else if (Utils.STATUS_DIRECTORY_NEW.exists()) {

            getData(Utils.STATUS_DIRECTORY_NEW);
            binding.refreshVideo.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    list.clear();
                    getData(Utils.STATUS_DIRECTORY_NEW);
                    binding.refreshVideo.setRefreshing(false);
                    adapter.notifyDataSetChanged();
                }
            });

        } else if (Utils.STATUS_DIRECTORY_GBWHATSAPP.exists()) {

            getData(Utils.STATUS_DIRECTORY_GBWHATSAPP);
            binding.refreshVideo.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    list.clear();
                    getData(Utils.STATUS_DIRECTORY_GBWHATSAPP);
                    binding.refreshVideo.setRefreshing(false);
                    adapter.notifyDataSetChanged();
                }
            });
        } else {
            binding.refreshVideo.setRefreshing(false);
            binding.rvVideo.setVisibility(View.GONE);
            binding.llNotFound.setVisibility(View.VISIBLE);
            Toast.makeText(requireContext(), "Can't Whatsapp File Find!! ", Toast.LENGTH_SHORT).show();
        }

        adapter = new VideoRecyclerAdapter(requireContext(), list);
        binding.rvVideo.setAdapter(adapter);
        binding.rvVideo.setLayoutManager(new GridLayoutManager(requireContext(), 3));

        return binding.getRoot();
    }


    private void getData(File file)
    {
        Recents model ;

        File[] allfiles = file.listFiles();

        Arrays.sort(allfiles ,((o1, o2) ->{
            if(o1.lastModified() > o2.lastModified())
            {
                return -1;
            }
            else if(o1.lastModified() < o2.lastModified())
            {
                return +1;
            }
            else {
                return 0;
            }
        }));

        for(int i = 0 ; i < allfiles.length; i++)
        {
            File singlefile = allfiles[i] ;

            if(Uri.fromFile(singlefile).toString().endsWith(".mp4")  )
            {
                model = new Recents("whats "+i,allfiles[i].getAbsolutePath() , singlefile, Uri.fromFile(singlefile));

                list.add(model);
            }
        }

    }
}