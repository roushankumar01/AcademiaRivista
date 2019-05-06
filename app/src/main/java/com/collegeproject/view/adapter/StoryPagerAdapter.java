package com.collegeproject.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.collegeproject.R;
import com.collegeproject.model.staticModel.StoryModel;
import com.collegeproject.view.activity.StoryActivity;

import java.util.List;

public class StoryPagerAdapter extends PagerAdapter {

    private Context mContext;
    private List<StoryModel> storyModelList;

    public StoryPagerAdapter(Context mContext, List<StoryModel> storyModelList) {
        this.mContext = mContext;
        this.storyModelList = storyModelList;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        LayoutInflater layoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.story_pager_layout, container, false);
        final StoryModel storyModel = storyModelList.get(position);

        final ImageView storyImage = view.findViewById(R.id.storyImage);
        final ImageView skipNxt = view.findViewById(R.id.skipNxt);

        Glide.with(mContext).load(storyModel.getImage()).into(storyImage);

        skipNxt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //countDownTimer.start();
                ((StoryActivity) mContext).storyPager.setCurrentItem(position + 1); //go to nxt pager item or nxt item of list
                if (position == (storyModelList.size() - 1)) {
                    ((StoryActivity)mContext).onBackPressed(); //return to home when list is completed
                }
            }
        });


        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return storyModelList.size();
    }


    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object obj) {
        return view == obj;
    }


    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        View view = (View) object;
        container.removeView(view);
    }
}