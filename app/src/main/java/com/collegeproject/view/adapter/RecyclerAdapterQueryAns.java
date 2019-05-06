package com.collegeproject.view.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.collegeproject.R;
import com.collegeproject.model.staticModel.CommentModel;

import java.util.List;

public class RecyclerAdapterQueryAns extends RecyclerView.Adapter<RecyclerAdapterQueryAns.ViewHolder> {

    private Context context;
    List<CommentModel> commentModelList;

    public RecyclerAdapterQueryAns(Context context, List<CommentModel> commentModelList){
        this.context = context;
        this.commentModelList = commentModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.query_ans_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        final ViewHolder holder = (ViewHolder)viewHolder;
        final CommentModel commentModel = commentModelList.get(position);
        holder.tvComment.setText(commentModel.getComment());
        holder.tvDateName.setText(commentModel.getCommentBy()+ " , Date : "+commentModel.getCommentDate() );
    }

    @Override
    public int getItemCount() {
        return (commentModelList != null ? commentModelList.size() : 0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvComment, tvDateName;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvComment = itemView.findViewById(R.id.commentview);
            tvDateName = itemView.findViewById(R.id.namedateview);
        }
    }
}
