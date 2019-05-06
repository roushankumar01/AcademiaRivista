package com.collegeproject.view.activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.collegeproject.R;
import com.collegeproject.model.staticModel.QueryModel;
import com.collegeproject.model.staticModel.StoryModel;
import com.collegeproject.view.adapter.RecyclerAdapterQuery;
import com.collegeproject.view.adapter.RecyclerAdapterStory;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QueryActivity extends AppCompatActivity {

    private ImageView backQuery;
    private RecyclerView queryRecycler;
    private RecyclerAdapterQuery recyclerAdapterQuery;
    private List<QueryModel> queryModelList = new ArrayList<>();
    private FloatingActionButton addQueryFab;
    private View view;
    private LayoutInflater layoutInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query);
        layoutInflater = LayoutInflater.from(this);
        //hide soft keyboard on start of activity
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        init();

        backQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        queryRecycler.setLayoutManager(layoutManager);
        queryRecycler.setItemAnimator(new DefaultItemAnimator());
        queryRecycler.setNestedScrollingEnabled(false);
        //setting values to adapter
        getDynamicQuery();


        addQueryFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
    }

    private void openDialog(){
        view = layoutInflater.inflate(R.layout.add_query_dialog, null, false);
        final AlertDialog dialog = new AlertDialog.Builder(QueryActivity.this)
                .setView(view)
                .create();

        final EditText queryTextBox = view.findViewById(R.id.queryTextBox);
        final Button submitQuery =view.findViewById(R.id.submitQuery);

        submitQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = queryTextBox.getText().toString().trim();
                Log.d("TAG", "Query : " +query);
                storeQuery(query);
                dialog.dismiss();
                // submit api
            }
        });
        dialog.show();
    }

    private void storeQuery(String query){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> data = new HashMap<>();
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
        String date=dateFormat.format(new Date());
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String name  = user.getDisplayName();
        data.put("query", query);
        data.put("postdate", date);
        data.put("postedby", name);
        db.collection("Queries").document().set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d("TAG", "Data Saved");
                Toast.makeText(getApplicationContext(), "Query Posted", Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void getDynamicQuery(){
        FirebaseFirestore db;
        db=FirebaseFirestore.getInstance();
        db.collection("Queries").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (DocumentSnapshot doc : task.getResult()){
                    queryModelList.add(new QueryModel(doc.getString("query"),doc.getString("postdate"), doc.getId(),doc.getString("postedby")));
                }
                recyclerAdapterQuery = new RecyclerAdapterQuery(getApplicationContext(), queryModelList);
                queryRecycler.setAdapter(recyclerAdapterQuery);
                recyclerAdapterQuery.notifyDataSetChanged();
            }
        });
    }

    /*private void getStaticQuery() {
        queryModelList.add(new QueryModel("How do you double your value when there is no extra time?","01 March, 2019"));
        queryModelList.add(new QueryModel("What's the last thing you did for the first time in your life and didn't regret trying it?","02 March, 2019"));
        queryModelList.add(new QueryModel("What is the weirdest thing you have ever experienced?","04 March, 2019"));
        queryModelList.add(new QueryModel("What is a something you can teach me in a few minutes that will change my life/days?","08 March, 2019"));
        queryModelList.add(new QueryModel("What's the last thing you did for the first time in your life and didn't regret trying it?","09 March, 2019"));
        queryModelList.add(new QueryModel("What is the weirdest thing you have ever experienced?","09 March, 2019"));
        queryModelList.add(new QueryModel("What's the last thing you did for the first time in your life and didn't regret trying it?","10 March, 2019"));
        queryModelList.add(new QueryModel("What is the weirdest thing you have ever experienced?","11 March, 2019"));
    }*/

    private void init() {
        backQuery = findViewById(R.id.backQuery);
        queryRecycler = findViewById(R.id.queryRecycler);
        addQueryFab = findViewById(R.id.addQueryFab);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        QueryActivity.this.finish();
    }
}
