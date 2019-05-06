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
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.collegeproject.R;
import com.collegeproject.model.staticModel.CommentModel;
import com.collegeproject.view.adapter.RecyclerAdapterQueryAns;
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

public class QueryDetailActivity extends AppCompatActivity {

    private ImageView backQdetail;
    private RecyclerView queryAnsRecycler;
    private RecyclerAdapterQueryAns recyclerAdapterQueryAns;
    private String query;
    private String postDate;
    private String queryId;
    private String postedBy;
    private TextView tvQuery;
    private TextView tvPostDate;
    private TextView tvPostedBy;
    private EditText commentInput;
    private ImageView addCommentBtn;
    private List<CommentModel> commentModelList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_query_detail);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        init();

        backQdetail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        queryAnsRecycler.setLayoutManager(layoutManager);
        queryAnsRecycler.setItemAnimator(new DefaultItemAnimator());
        queryAnsRecycler.setNestedScrollingEnabled(false);
        queryAnsRecycler.setFocusable(false);
        //setting values to adapter
        fetchComment();

        addCommentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String comment = commentInput.getText().toString();
                commentInput.setText("");
                Log.d("TAG", "Comment : "+ comment);
                addComment(comment);
            }
        });

    }

    private void fetchComment(){

        FirebaseFirestore db;
        db=FirebaseFirestore.getInstance();
        db.collection("Queries").document(queryId).collection("Comment").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (DocumentSnapshot doc : task.getResult()){
                    Log.d("TAG", "Comments : " + doc.getString("comment"));
                    commentModelList.add(new CommentModel(doc.getString("comment"),doc.getString("commentby"),doc.getString("commentdate")));
                }
                recyclerAdapterQueryAns = new RecyclerAdapterQueryAns(getApplicationContext(),commentModelList);
                queryAnsRecycler.setAdapter(recyclerAdapterQueryAns);
                recyclerAdapterQueryAns.notifyDataSetChanged();
            }
        });
    }
    private void addComment(String comment){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> data = new HashMap<>();
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyy-MM-dd");
        String date=dateFormat.format(new Date());
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String name  = user.getDisplayName();
        data.put("comment", comment);
        data.put("commentdate", date);
        data.put("commentby", name);
        db.collection("Queries").document(queryId).collection("Comment").document().set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d("TAG", "Data Saved");
                Toast.makeText(getApplicationContext(), "Comment Added", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void init() {
        backQdetail = findViewById(R.id.backQdetail);
        queryAnsRecycler = findViewById(R.id.queryAnsRecycler);
        tvQuery = findViewById(R.id.questiondetails);
        tvPostDate = findViewById(R.id.posteddate);
        tvPostedBy = findViewById(R.id.postedby);
        commentInput = findViewById(R.id.commentinput);
        addCommentBtn = findViewById(R.id.addcommentbtn);
        Intent i = getIntent();
        query = i.getStringExtra("query");
        queryId = i.getStringExtra("queryid");
        postDate = i.getStringExtra("postdate");
        postedBy = i.getStringExtra("postedby");
        Log.d("TAG", "Qdetails " + query + postDate + postedBy);
        tvQuery.setText(query);
        //tvPostDate.setText("Test");
        tvPostedBy.setText(postedBy);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
