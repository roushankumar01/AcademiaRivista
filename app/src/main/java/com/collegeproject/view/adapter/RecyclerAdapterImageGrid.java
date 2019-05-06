package com.collegeproject.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.collegeproject.R;

import java.util.ArrayList;

public class RecyclerAdapterImageGrid extends RecyclerView.Adapter<RecyclerAdapterImageGrid.ViewHolder> {

    private Context context;
    private ArrayList<String> imageValues;

    public RecyclerAdapterImageGrid(Context context, ArrayList<String> imageValues){
        this.context = context;
        this.imageValues = imageValues;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.gridview_layout, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        final String mobileres = imageValues.get(position);

        Glide.with(context).load(mobileres).into(holder.grid_item_image);
        holder.imageClose_iV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageValues.remove(position);
                notifyItemRemoved(position);
                notifyItemRangeChanged(position, imageValues.size());
            }
        });
    }

    @Override
    public int getItemCount() {
        return (imageValues != null ? imageValues.size() : 0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private ImageView grid_item_image, imageClose_iV;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            grid_item_image = itemView.findViewById(R.id.grid_item_image);
            imageClose_iV = itemView.findViewById(R.id.imageClose_iV);
        }
    }
}
