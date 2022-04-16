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

import com.itcraftsolution.statussaverforwhatsappdownload.Adapter.ResentDownloadAdapter;
import com.itcraftsolution.statussaverforwhatsappdownload.Adapter.VideoRecyclerAdapter;
import com.itcraftsolution.statussaverforwhatsappdownload.Models.Recents;
import com.itcraftsolution.statussaverforwhatsappdownload.R;
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

        File STATUS_DIRECTORY = new File(Environment.getExternalStorageDirectory() +
                File.separator + "WhatsApp/Media/.Statuses");

        File STATUS_DIRECTORY_NEW = new File(Environment.getExternalStorageDirectory() +
                File.separator + "Android/media/com.whatsapp/WhatsApp/Media/.Statuses");

        File STATUS_DIRECTORY_GBWHATSAPP = new File(Environment.getExternalStorageDirectory() +
                File.separator + "/GBWhatsapp/Media/.statuses");

        if (STATUS_DIRECTORY.exists()) {

            getData(STATUS_DIRECTORY);
            binding.refreshVideo.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    list = new ArrayList<>();
                    getData(STATUS_DIRECTORY);
                    binding.refreshVideo.setRefreshing(false);
                }
            });

        } else if (STATUS_DIRECTORY_NEW.exists()) {

            getData(STATUS_DIRECTORY_NEW);
            binding.refreshVideo.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    list = new ArrayList<>();
                    getData(STATUS_DIRECTORY_NEW);
                    binding.refreshVideo.setRefreshing(false);
                }
            });

        } else if (STATUS_DIRECTORY_GBWHATSAPP.exists()) {

            getData(STATUS_DIRECTORY_GBWHATSAPP);
            binding.refreshVideo.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    list = new ArrayList<>();
                    getData(STATUS_DIRECTORY_GBWHATSAPP);
                    binding.refreshVideo.setRefreshing(false);
                }
            });
        }
        else {
//            messageTextView.setVisibility(View.VISIBLE);
//            messageTextView.setText(R.string.cant_find_whatsapp_dir);
//            Toast.makeText(getActivity(), getString(R.string.cant_find_whatsapp_dir), Toast.LENGTH_SHORT).show();
//            swipeRefreshLayout.setRefreshing(false);

            Toast.makeText(requireContext(), "Can't Whatsapp File Find!! ", Toast.LENGTH_SHORT).show();
        }



        adapter = new VideoRecyclerAdapter(requireContext(), list);
        binding.rvVideo.setAdapter(adapter);
        binding.rvVideo.setLayoutManager(new GridLayoutManager(requireContext() , 3 ));



        return binding.getRoot();
    }


    private void getData(File file)
    {

        Recents model ;



//        String Whatsapp = Environment.getExternalStorageDirectory().getAbsolutePath()+
//                "/Whatsapp/Media/.statuses";
//
//       File STATUS_DIRECTORY = new File(Environment.getExternalStorageDirectory() +
//                File.separator + "WhatsApp/Media/.Statuses");
//
//         File STATUS_DIRECTORY_NEW = new File(Environment.getExternalStorageDirectory() +
//                File.separator + "Android/media/com.whatsapp/WhatsApp/Media/.Statuses");

//        String Akkuwp = Environment.getExternalStorageDirectory().getAbsolutePath()+
//                "/Android/media/com.whatsapp/WhatsApp/Media/";
//
//        String GbWhatsapp = Environment.getExternalStorageDirectory().getAbsolutePath()+
//                "/GBWhatsapp/Media/.statuses";


        File[] allfiles = file.listFiles();


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
            File singlefile = allfiles[i] ;

            if(Uri.fromFile(singlefile).toString().endsWith(".mp4")  )
            {
                model = new Recents("whats "+i,allfiles[i].getAbsolutePath() , singlefile.getName(), Uri.fromFile(singlefile));

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