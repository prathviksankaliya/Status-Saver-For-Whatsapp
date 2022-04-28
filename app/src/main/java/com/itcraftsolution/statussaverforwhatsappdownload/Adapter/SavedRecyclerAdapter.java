package com.itcraftsolution.statussaverforwhatsappdownload.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.itcraftsolution.statussaverforwhatsappdownload.Fragments.ImageDetailsFragment;
import com.itcraftsolution.statussaverforwhatsappdownload.Fragments.VideoDetailsFragment;
import com.itcraftsolution.statussaverforwhatsappdownload.Models.Statues;
import com.itcraftsolution.statussaverforwhatsappdownload.R;
import com.itcraftsolution.statussaverforwhatsappdownload.databinding.SampleSavedBinding;

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
        View view = LayoutInflater.from(context).inflate(R.layout.sample_saved , parent , false);
        return new viewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull viewHolder holder, int position) {
        Statues model = list.get(position);

        if(model.getFilename().getName().endsWith(".mp4"))
        {
            holder.binding.igVideo.setVisibility(View.VISIBLE);
            Glide.with(context).load(model.getUri()).into(holder.binding.igResentSample);
        }else {
            holder.binding.igVideo.setVisibility(View.GONE);
            Glide.with(context).load(model.getUri()).into(holder.binding.igResentSample);
        }

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                SharedPreferences spf = context.getSharedPreferences("SendDetails", Context.MODE_PRIVATE);
                SharedPreferences.Editor edit = spf.edit();
                edit.putString("URI", model.getUri().toString());
                edit.putString("FILE_PATH", model.getFilename().getAbsolutePath());
                edit.putBoolean("isSaved" , true);
                edit.apply();

                if(model.getFilename().getName().endsWith(".mp4"))
                {
                    ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frMainContainer , new VideoDetailsFragment())
                            .addToBackStack(null)
                            .commit();
                }else {
                    ((FragmentActivity) context).getSupportFragmentManager().beginTransaction()
                            .replace(R.id.frMainContainer , new ImageDetailsFragment())
                            .addToBackStack(null)
                            .commit();
                }
            }
        });


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public static class  viewHolder extends RecyclerView.ViewHolder {
        SampleSavedBinding binding;
        public viewHolder(@NonNull View itemView) {
            super(itemView);
            binding = SampleSavedBinding.bind(itemView);
        }
    }
}
