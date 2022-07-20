package com.itcraftsolution.statussaverforwhatsappdownload.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.itcraftsolution.statussaverforwhatsappdownload.Models.Statues;
import com.itcraftsolution.statussaverforwhatsappdownload.R;
import com.itcraftsolution.statussaverforwhatsappdownload.Utils.Utils;
import com.itcraftsolution.statussaverforwhatsappdownload.VideoDetailActivity;
import com.itcraftsolution.statussaverforwhatsappdownload.databinding.SampleResentBinding;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.ArrayList;

public class VideoRecyclerAdapter extends RecyclerView.Adapter<VideoRecyclerAdapter.viewHolder> {


    Context context;
    ArrayList<Statues> list;
    private String saveFilePath = Utils.RootDirectorywhatsapp + "/";

    public VideoRecyclerAdapter(Context context, ArrayList<Statues> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_resent , parent , false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Statues model = list.get(position);

        Glide.with(context).load(model.getUri()).into(holder.binding.igResentSample);
        holder.binding.igVideo.setVisibility(View.VISIBLE);
        holder.binding.btnDownloadResent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Utils.saveImgIntoGallery(context, model);
//                if (!Utils.STATUS_SAVER_DIR.exists()) {
//                    if (!Utils.STATUS_SAVER_DIR.mkdirs()) {
//                        Toast.makeText(context, "Something went wrong !!", Toast.LENGTH_SHORT).show();
//                    }
//                }
//                final String path = model.getPath();
//                final File file = new File(path);
//                File destFile = new File(saveFilePath);
//                try {
//                    FileUtils.copyFileToDirectory(file, destFile);
//                }catch (Exception e)
//                {
//                    Toast.makeText(context, ""+ e.getMessage(), Toast.LENGTH_SHORT).show();
//                }
            }
        });

        holder.binding.igResentSample.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, VideoDetailActivity.class);
                intent.putExtra("URI", model.getUri().toString());
                intent.putExtra("FILE_PATH", model.getFilename().getAbsolutePath());

                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {

        SampleResentBinding binding;
        public viewHolder(@NonNull View itemView) {
            super(itemView);

            binding = SampleResentBinding.bind(itemView);
        }
    }

}
