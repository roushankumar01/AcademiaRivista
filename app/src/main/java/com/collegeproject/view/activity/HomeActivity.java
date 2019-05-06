package com.collegeproject.view.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.collegeproject.R;
import com.collegeproject.model.staticModel.StoryModel;
import com.collegeproject.view.adapter.RecyclerAdapterStory;
import com.collegeproject.view.fragment.SideMenuFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView storyRecycler;
    private RecyclerAdapterStory recyclerAdapterStory;
    private List<StoryModel> storyModelList = new ArrayList<>();

    private LinearLayout queryLay, mapLay, sellBuyLay;
    //Navigation Bar
    private SlidingMenu menu;
    private ImageView navImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();

        /*Side Bar*/
        setSideBar();
        SideMenuFragment sideMenuFragment = new SideMenuFragment();
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.side_menu_container, sideMenuFragment, "SideMenuFragment")
                .commit();
        navImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menu.toggle();
            }
        });
        /*===========*/

        //setting up recyclerView
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        storyRecycler.setLayoutManager(layoutManager);
        storyRecycler.setItemAnimator(new DefaultItemAnimator());
        storyRecycler.setNestedScrollingEnabled(false);
        //setting values to adapter
        getDynamicStory();

        queryLay.setOnClickListener(this);
        mapLay.setOnClickListener(this);
        sellBuyLay.setOnClickListener(this);

    }

    private void init() {
        storyRecycler = findViewById(R.id.storyRecycler);
        queryLay = findViewById(R.id.queryLay);
        mapLay = findViewById(R.id.mapLay);
        sellBuyLay = findViewById(R.id.sellBuyLay);
        navImage = findViewById(R.id.navImage);
    }

    private void getDynamicStory(){
        Log.d("TAG","Method called");
        FirebaseFirestore db;
        db=FirebaseFirestore.getInstance();
        db.collection("Status").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (DocumentSnapshot doc : task.getResult()){
                    storyModelList.add(new StoryModel(doc.getString("story_url"),doc.getString("name")));
                }
                recyclerAdapterStory = new RecyclerAdapterStory(getApplicationContext(), storyModelList);
                storyRecycler.setAdapter(recyclerAdapterStory);
                recyclerAdapterStory.notifyDataSetChanged();
            }
        });
    }

    private void getStaticStory(){
        storyModelList.add(new StoryModel("https://images.pexels.com/photos/736716/pexels-photo-736716.jpeg?auto=compress&cs=tinysrgb&dpr=1&w=500","Jhon"));
        storyModelList.add(new StoryModel("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcTTinYwmu3-FeP69I9VsvnnxdINvejoC5P3pb4P435p5D7HFtUj","Peter"));
        storyModelList.add(new StoryModel("http://timedotcom.files.wordpress.com/2018/04/time-100-trevor-noah.jpg?quality=85&crop=0px%2C89px%2C1804px%2C1804px&resize=600%2C600&strip&zoom=2","Rick"));
        storyModelList.add(new StoryModel("https://www.law.ox.ac.uk/sites/files/oxlaw/styles/square_large/public/field/field_person_photo/profile-douglas-simon.JPG?itok=hKekeaww","Jhonas"));
        storyModelList.add(new StoryModel("http://www.tdlab.usys.ethz.ch/content/specialinterest/usys/tdlab/en/team/person-detail.person_image.jpeg?persid=MTg1Njkx","Becky"));
        storyModelList.add(new StoryModel("https://secure.gravatar.com/avatar/81c8847e3d52f5cab58ce0a0b923f11f?s=400&d=mm&r=g","Sarah"));
        storyModelList.add(new StoryModel("http://www.greyfoxz.in/upload/profile_1403800115.jpeg","Janet"));
        storyModelList.add(new StoryModel("https://s3.amazonaws.com/fixd-wordpress-cdn/wp-content/uploads/2018/04/Picture-of-person.png","Saurabh"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.queryLay :

                startActivity(new Intent(HomeActivity.this, QueryActivity.class));

                break;
            case R.id.mapLay :

                startActivity(new Intent(HomeActivity.this, MapActivity.class));

                break;
            case R.id.sellBuyLay :

                startActivity(new Intent(HomeActivity.this, SellBuyActivity.class));

                break;
        }
    }

    @Override
    public void onBackPressed() {

    }

    private void setSideBar() {
        menu = new SlidingMenu(this);
        menu.setShadowWidthRes(R.dimen.shadow_width);
        menu.setShadowDrawable(R.drawable.shadow);
        menu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_MARGIN);
        menu.setFadeDegree(0.75f);
        menu.attachToActivity(this, SlidingMenu.SLIDING_CONTENT);
        menu.setMenu(R.layout.ly_frame_layout);
    }
}
