package com.itcraftsolution.statussaverforwhatsappdownload.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.itcraftsolution.statussaverforwhatsappdownload.ImageDetailActivity;
import com.itcraftsolution.statussaverforwhatsappdownload.Models.Statues;
import com.itcraftsolution.statussaverforwhatsappdownload.R;
import com.itcraftsolution.statussaverforwhatsappdownload.Utils.Utils;
import com.itcraftsolution.statussaverforwhatsappdownload.databinding.SampleResentBinding;

import java.util.ArrayList;

public class ImageRecyclerAdapter extends RecyclerView.Adapter<ImageRecyclerAdapter.viewHolder> {
    Context context;
    ArrayList<Statues> list;

    public ImageRecyclerAdapter(Context context, ArrayList<Statues> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_resent, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Statues model = list.get(position);

        Glide.with(context).load(model.getUri()).into(holder.binding.igResentSample);
        holder.binding.btnDownloadResent.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                Utils.saveImgIntoGallery(context, model);
            }
        });

        holder.binding.igResentSample.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ImageDetailActivity.class);
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
