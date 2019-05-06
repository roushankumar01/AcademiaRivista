package com.collegeproject.view.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageView;

import com.collegeproject.R;
import com.collegeproject.model.staticModel.SellBuyModel;
import com.collegeproject.view.adapter.RecyclerAdapterSellItem;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class SellBuyActivity extends AppCompatActivity {

    private ImageView backSell;
    private FloatingActionButton addNewItem;
    private RecyclerView itemRecycler;
    private RecyclerAdapterSellItem recyclerAdapterSellItem;
    private StaggeredGridLayoutManager gaggeredGridLayoutManager;
    private List<SellBuyModel> sellBuyModelList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_buy);

        backSell = findViewById(R.id.backSell);
        itemRecycler = findViewById(R.id.itemRecycler);
        addNewItem = findViewById(R.id.addNewItem);

        backSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        addNewItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SellBuyActivity.this, AdPostActivity.class));
            }
        });

        gaggeredGridLayoutManager = new StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL);
        itemRecycler.setLayoutManager(gaggeredGridLayoutManager);
        //itemRecycler.setItemAnimator(new DefaultItemAnimator());
        itemRecycler.setNestedScrollingEnabled(false);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("adds").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                for (DocumentSnapshot doc : task.getResult()){
                    SellBuyModel sellBuyModel = new SellBuyModel(doc.getString("title"),doc.getString("price"),doc.getString("email"),doc.getString("phone"),doc.getString("des"),doc.getString("image_url"), doc.getString("name"));
                    sellBuyModelList.add(sellBuyModel);
                }
                recyclerAdapterSellItem = new RecyclerAdapterSellItem(getApplicationContext(), sellBuyModelList);
                itemRecycler.setAdapter(recyclerAdapterSellItem);
                recyclerAdapterSellItem.notifyDataSetChanged();
            }
        });
    }

    /*private void getStaticSellBuy() {
        sellBuyModelList.add(new SellBuyModel("https://img.purch.com/o/aHR0cHM6Ly93d3cubGFwdG9wbWFnLmNvbS9pbWFnZXMvdXBsb2Fkcy80NjE0L2cvaHAtZW52eS0xMy0wMDEuanBn", "HP Laptop (grey, 8GB) for Sale"));
        sellBuyModelList.add(new SellBuyModel("https://tr1.cbsistatic.com/hub/i/2015/06/03/f94abbf7-098c-11e5-940f-14feb5cc3d2a/r00320040303shu01_b.jpg","Hitachi Hard Drive 512 GB for Sale"));
        sellBuyModelList.add(new SellBuyModel("https://btmedia.whsmith.co.uk/pws/client/images/catalogue/products/9780/75/1565355/xlarge/9780751565355_1.jpg","Harry Potter book for sale"));
        sellBuyModelList.add(new SellBuyModel("https://cdn.pocket-lint.com/r/s/1200x/assets/images/147383-news-motorola-foldable-phone-details-revealed-razr-dual-screen-design-confirmed-image1-8pdsi5vfbo.jpg","Moto Razr vintage mobile sale"));
        sellBuyModelList.add(new SellBuyModel("https://image.dhgate.com/0x0/f2/albu/g7/M01/DF/14/rBVaSVq9-COADjrPABCGDcflHek019.jpg","PS3 Game Controller sale"));
        sellBuyModelList.add(new SellBuyModel("https://images.reverb.com/image/upload/s--xdNLenMy--/a_exif,c_limit,e_unsharp_mask:80,f_auto,fl_progressive,g_south,h_620,q_90,w_620/v1456506129/w5bqhehdcjyzxminlaji.jpg", "Gibson Guitar (Acoustic)"));
        sellBuyModelList.add(new SellBuyModel("https://btmedia.whsmith.co.uk/pws/client/images/catalogue/products/9780/75/1565355/xlarge/9780751565355_1.jpg","Harry Potter book for sale"));
        sellBuyModelList.add(new SellBuyModel("https://cdn.pocket-lint.com/r/s/1200x/assets/images/147383-news-motorola-foldable-phone-details-revealed-razr-dual-screen-design-confirmed-image1-8pdsi5vfbo.jpg","Moto Razr vintage mobile sale"));
        sellBuyModelList.add(new SellBuyModel("https://images-na.ssl-images-amazon.com/images/I/91Gts7fdk7L.jpg","Gate Book for sale"));
        sellBuyModelList.add(new SellBuyModel("https://images-na.ssl-images-amazon.com/images/I/813trMr0iZL.jpg","Computer Science HandBook"));
    }*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
