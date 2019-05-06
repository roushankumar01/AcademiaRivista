package com.collegeproject.view.activity;

import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.collegeproject.R;
import com.collegeproject.model.staticModel.StoryModel;
import com.collegeproject.view.adapter.StoryPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class StoryActivity extends AppCompatActivity {

    public ViewPager storyPager;
    private StoryPagerAdapter storyPagerAdapter;
    private List<StoryModel> storyModelList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //flag for fullscreen
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_story);

        storyPager = findViewById(R.id.storyPager);

        //disable swiping of pager
        /*storyPager.setOnTouchListener(new View.OnTouchListener()
        {
            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                return true;
            }
        });*/

        getStoryItems();
        storyPagerAdapter = new StoryPagerAdapter(this, storyModelList);
        storyPager.setAdapter(storyPagerAdapter);

    }

    private void getStoryItems() {
        storyModelList.add(new StoryModel("https://www.w3schools.com/w3css/img_lights.jpg",""));
        storyModelList.add(new StoryModel("https://www.loudgraphicdesign.com.au/wp-content/uploads/Photography-Beach-Portrait.jpg",""));
        storyModelList.add(new StoryModel("http://photoncollective.com/wp-content/uploads/2015/06/simple-sunset-portraits-11.jpg",""));
        storyModelList.add(new StoryModel("https://cdn.dribbble.com/users/882244/screenshots/4391763/01-beach-scene-dribbble.gif",""));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
