
package com.itcraftsolution.statussaverforwhatsappdownload.Fragments;

import android.graphics.drawable.ColorDrawable;
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

import com.itcraftsolution.statussaverforwhatsappdownload.Adapter.SavedRecyclerAdapter;
import com.itcraftsolution.statussaverforwhatsappdownload.CustomDialog.Custom_Dialog;
import com.itcraftsolution.statussaverforwhatsappdownload.Models.Statues;
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

        File STATUS_DIRECTORY = new File(Environment.getExternalStorageDirectory() +
                File.separator + "StatusSaverForWhatsapp/");


        if (STATUS_DIRECTORY.exists()) {

            getData(STATUS_DIRECTORY);
            adapter = new SavedRecyclerAdapter(requireContext(), list);
            binding.rvSaved.setAdapter(adapter);
            binding.rvSaved.setLayoutManager(new StaggeredGridLayoutManager(2 , LinearLayoutManager.VERTICAL));

            binding.savedRefershView.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    list.clear();
                    getData(STATUS_DIRECTORY);
                    adapter.notifyDataSetChanged();
                    binding.savedRefershView.setRefreshing(false);
                }
            });

        }  else {
            binding.savedRefershView.setRefreshing(false);
            binding.rvSaved.setVisibility(View.GONE);
            Toast.makeText(requireContext(), "Can't Whatsapp File Find!! ", Toast.LENGTH_SHORT).show();
        }

        if(list.isEmpty())
        {
            binding.rvSaved.setVisibility(View.GONE);
            Custom_Dialog dialog = new Custom_Dialog(requireContext());
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));
            dialog.setCancelable(false);
            dialog.show();
            binding.VNotFoundImage.setVisibility(View.VISIBLE);
        }

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

        for (int i = 0; i < allfiles.length; i++) {
            File singlefile = allfiles[i];

            if (Uri.fromFile(singlefile).toString().endsWith(".png") || Uri.fromFile(singlefile).toString().endsWith(".jpg") || Uri.fromFile(singlefile).toString().endsWith(".mp4")) {
                model = new Statues("whats " + i, allfiles[i].getAbsolutePath(), singlefile, Uri.fromFile(singlefile));

                list.add(model);
            }
        }

    }
}

