package com.collegeproject.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.collegeproject.R;
import com.collegeproject.model.staticModel.SellBuyModel;
import com.collegeproject.view.activity.SellDetailActivity;

import java.util.List;

public class RecyclerAdapterSellItem extends RecyclerView.Adapter<RecyclerAdapterSellItem.ViewHolder> {

    private Context context;
    private List<SellBuyModel> sellBuyModelList;

    public RecyclerAdapterSellItem(Context context, List<SellBuyModel> sellBuyModelList){
        this.context = context;
        this.sellBuyModelList = sellBuyModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        //defining layout of adapter
        View itemView = LayoutInflater.from(context).inflate(R.layout.sell_item_list, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int position) {
        final ViewHolder holder = (ViewHolder) viewHolder;
        final SellBuyModel sellBuyModel = sellBuyModelList.get(position);

        Glide.with(context).load(sellBuyModel.getImage_url()).into(holder.sellBuyPic);
        holder.sellBuyName.setText(sellBuyModel.getTitle());
        holder.price.setText("₹"+sellBuyModel.getPrice());

        holder.sellBuyCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, SellDetailActivity.class);
                i.putExtra("title",sellBuyModelList.get(position).getTitle());
                i.putExtra("price",sellBuyModelList.get(position).getPrice());
                i.putExtra("email",sellBuyModelList.get(position).getPhone());
                i.putExtra("phone",sellBuyModelList.get(position).getPhone());
                i.putExtra("des",sellBuyModelList.get(position).getDes());
                i.putExtra("image_url",sellBuyModelList.get(position).getImage_url());
                i.putExtra("name", sellBuyModelList.get(position).getName());
                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return (sellBuyModelList != null ? sellBuyModelList.size() : 0);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private CardView sellBuyCard;
        private ImageView sellBuyPic;
        private TextView sellBuyName;
        private TextView price;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            sellBuyCard = itemView.findViewById(R.id.sellBuyCard);
            sellBuyPic = itemView.findViewById(R.id.sellBuyPic);
            sellBuyName = itemView.findViewById(R.id.sellBuyName);
            price = itemView.findViewById(R.id.view_price);
        }
    }
}
