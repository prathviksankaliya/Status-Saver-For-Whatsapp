package com.itcraftsolution.statussaverforwhatsappdownload.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.itcraftsolution.statussaverforwhatsappdownload.Models.Recents;
import com.itcraftsolution.statussaverforwhatsappdownload.R;
import com.itcraftsolution.statussaverforwhatsappdownload.Utils.Utils;
import com.itcraftsolution.statussaverforwhatsappdownload.databinding.SampleSavedBinding;

import java.util.ArrayList;

public class SavedRecyclerAdapter extends RecyclerView.Adapter<SavedRecyclerAdapter.viewHolder> {

    Context context;
    ArrayList<Recents> list;
    String saveFilePath = Utils.RootDirectorywhatsapp+"/";


    public SavedRecyclerAdapter(Context context, ArrayList<Recents> list) {
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
        Recents model = list.get(position);

        Glide.with(context).load(model.getUri()).into(holder.binding.igResentSample);

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
