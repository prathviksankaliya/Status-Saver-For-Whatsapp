package com.itcraftsolution.statussaverforwhatsappdownload.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.itcraftsolution.statussaverforwhatsappdownload.ImageDetailActivity;
import com.itcraftsolution.statussaverforwhatsappdownload.Models.Statues;
import com.itcraftsolution.statussaverforwhatsappdownload.R;
import com.itcraftsolution.statussaverforwhatsappdownload.Utils.Utils;
import com.itcraftsolution.statussaverforwhatsappdownload.VideoDetailActivity;
import com.itcraftsolution.statussaverforwhatsappdownload.databinding.SampleSavedBinding;

import java.io.File;
import java.util.ArrayList;

public class SavedRecyclerAdapter extends RecyclerView.Adapter<SavedRecyclerAdapter.viewHolder> {

    Context context;
    ArrayList<Statues> list;

    public SavedRecyclerAdapter(Context context, ArrayList<Statues> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public viewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.sample_saved, parent, false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Statues model = list.get(position);

        if (model.getFilename().getName().endsWith(".mp4")) {
            holder.binding.igVideo.setVisibility(View.VISIBLE);

        } else {
            holder.binding.igVideo.setVisibility(View.GONE);

        }
        Glide.with(context).load(model.getUri()).into(holder.binding.igResentSample);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (model.getFilename().getName().endsWith(".mp4")) {
                    Intent intent = new Intent(context, VideoDetailActivity.class);
                    intent.putExtra("URI", model.getUri().toString());
                    intent.putExtra("FILE_PATH", model.getFilename().getAbsolutePath());
                    intent.putExtra("isSaved", true);
                    context.startActivity(intent);
                } else {
                    Intent intent = new Intent(context, ImageDetailActivity.class);
                    intent.putExtra("URI", model.getUri().toString());
                    intent.putExtra("FILE_PATH", model.getFilename().getAbsolutePath());
                    intent.putExtra("isSaved", true);
                    context.startActivity(intent);
                }
            }
        });
        holder.binding.btnDeleteSaved.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                File fdelete = new File(model.getUri().getPath());

                if (fdelete.exists()) {
                    if (fdelete.delete()) {
                        Toast.makeText(context, "Status Deleted Successfully", Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(context, "file not Deleted ", Toast.LENGTH_LONG).show();
                    }

                    Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    intent.setData(Uri.fromFile(fdelete));
                    context.sendBroadcast(intent);
                }
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class viewHolder extends RecyclerView.ViewHolder {
        SampleSavedBinding binding;

        public viewHolder(@NonNull View itemView) {
            super(itemView);
            binding = SampleSavedBinding.bind(itemView);
        }
    }
}
