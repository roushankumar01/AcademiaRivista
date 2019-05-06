package com.collegeproject.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.collegeproject.R;
import com.collegeproject.model.staticModel.QueryModel;
import com.collegeproject.view.activity.QueryDetailActivity;
import com.collegeproject.view.activity.ViewStory;

import java.util.List;

public class RecyclerAdapterQuery extends RecyclerView.Adapter<RecyclerAdapterQuery.ViewHolder> {

    private Context context;
    private List<QueryModel> queryModelList;

    public RecyclerAdapterQuery(Context context, List<QueryModel> queryModelList){
        this.context = context;
        this.queryModelList = queryModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        //defining layout of adapter
        View itemView = LayoutInflater.from(context).inflate(R.layout.query_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        final ViewHolder holder = (ViewHolder)viewHolder;
        final QueryModel queryModel = queryModelList.get(position);

        holder.question.setText(queryModel.getQues());
        holder.postDate.setText("Posted : "+queryModel.getDate());

        holder.queryLinLay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //context.startActivity(new Intent(context, QueryDetailActivity.class));
                Intent i = new Intent(context, QueryDetailActivity.class);
                i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                i.putExtra("query",queryModel.getQues());
                i.putExtra("queryid",queryModel.getQueryId());
                i.putExtra("postdate",queryModel.getDate());
                i.putExtra("postedby", queryModel.getPostedBy());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (queryModelList != null ? queryModelList.size() : 0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private LinearLayout queryLinLay;
        private TextView question, postDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            question = itemView.findViewById(R.id.question);
            postDate = itemView.findViewById(R.id.postDate);
            queryLinLay = itemView.findViewById(R.id.queryLinLay);
        }
    }
}
