package com.collegeproject.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.collegeproject.R;
import com.collegeproject.model.staticModel.StoryModel;
import com.collegeproject.view.activity.StoryActivity;
import com.collegeproject.view.activity.ViewStory;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class RecyclerAdapterStory extends RecyclerView.Adapter<RecyclerAdapterStory.ViewHolder> {

    private Context context;
    private List<StoryModel> storyModelList;

    //this constructor connects Activity to Adapter
    public RecyclerAdapterStory(Context context, List<StoryModel> storyModelList){
        this.context = context;
        this.storyModelList = storyModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        //defining layout of adapter
        View itemView = LayoutInflater.from(context).inflate(R.layout.story_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        //Type Casting of viewHolder
        final ViewHolder holder = (ViewHolder) viewHolder;
        final StoryModel storyModel = storyModelList.get(position);

        //setting values
        Glide.with(context).load(storyModel.getImage()).into(holder.storyImage);

        holder.storyName.setText(storyModel.getName());

        //performing action over view
        holder.storyImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //context.startActivity(new Intent(context, StoryActivity.class));
                Intent i = new Intent(context, ViewStory.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("imgurl",storyModel.getImage());
                context.startActivity(i);
            }
        });

    }

    @Override
    public int getItemCount() {
        return (storyModelList != null ? storyModelList.size() : 0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CircleImageView storyImage;
        private TextView storyName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            storyImage = itemView.findViewById(R.id.storyImage);
            storyName = itemView.findViewById(R.id.storyName);
        }
    }
}
