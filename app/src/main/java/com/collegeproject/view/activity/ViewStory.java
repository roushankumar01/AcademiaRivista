package com.collegeproject.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.collegeproject.R;

public class ViewStory extends AppCompatActivity {

    private ImageView storyView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_story);
        storyView = findViewById(R.id.story_view);
        Intent i = getIntent();
        String imageUrl =i.getStringExtra("imgurl");
        Log.d("TAG", "URl " + imageUrl);
        Uri uri = Uri.parse(imageUrl);
        Glide.with(getApplicationContext()).load(uri).into(storyView);
    }
}
