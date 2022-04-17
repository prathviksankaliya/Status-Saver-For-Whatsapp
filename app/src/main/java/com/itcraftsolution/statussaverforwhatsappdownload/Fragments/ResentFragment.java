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

import com.itcraftsolution.statussaverforwhatsappdownload.Adapter.ResentDownloadAdapter;
import com.itcraftsolution.statussaverforwhatsappdownload.Models.Recents;
import com.itcraftsolution.statussaverforwhatsappdownload.R;
import com.itcraftsolution.statussaverforwhatsappdownload.databinding.FragmentResentBinding;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;


public class ResentFragment extends Fragment {

    public ResentFragment() {
        // Required empty public constructor
    }

    private FragmentResentBinding binding;
    private ArrayList<Recents> list;
    private ResentDownloadAdapter adapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentResentBinding.inflate(getLayoutInflater());

        list = new ArrayList<>();
        getData();
        binding.refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                list = new ArrayList<>();
                getData();
                binding.refresh.setRefreshing(false);
            }
        });

        adapter = new ResentDownloadAdapter(requireContext(), list);
        binding.rvResent.setAdapter(adapter);
        binding.rvResent.setLayoutManager(new GridLayoutManager(requireContext() , 2 ));

        return binding.getRoot();
    }

    private void getData()
    {

        Recents model ;

        String targetpath = Environment.getExternalStorageDirectory().getAbsolutePath()+
                "/Whatsapp/Media/.statuses";
        File targetDicretry = new File(targetpath);
        File[] allfiles = targetDicretry.listFiles();


//        String targetpathBusiness = Environment.getExternalStorageDirectory().getAbsolutePath()+
//                "/Whatsapp Business/Media/.statuses";
//        File targetDicretryBusiness = new File(targetpathBusiness);
//        File[] allfilesBusiness = targetDicretryBusiness.listFiles();

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
            File file = allfiles[i] ;

            if(Uri.fromFile(file).toString().endsWith(".png") || Uri.fromFile(file).toString().endsWith(".jpg") || Uri.fromFile(file).toString().endsWith(".mp4"))
            {
                model = new Recents("whats "+i,allfiles[i].getAbsolutePath() , file, Uri.fromFile(file));

                list.add(model);
            }
        }

//
//        Arrays.sort(allfilesBusiness ,((o1, o2) ->{
//            if(o1.lastModified() > o2.lastModified())
//            {
//                return -1;
//            }
//            else if(o1.lastModified() < o2.lastModified())
//            {
//                return +1;
//            }
//            else {
//                return 0;
//            }
//        }));
//
//        for(int i = 0 ; i < allfilesBusiness.length; i++)
//        {
//            File file = allfilesBusiness[i] ;
//
//            if(Uri.fromFile(file).toString().endsWith(".png") || Uri.fromFile(file).toString().endsWith(".jpg") || Uri.fromFile(file).toString().endsWith(".mp4"))
//            {
//                model = new Recents("whatsBusiness "+i,allfilesBusiness[i].getAbsolutePath() , file.getName(), Uri.fromFile(file));
//
//                list.add(model);
//            }
//        }
    }
}