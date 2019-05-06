package com.collegeproject.view.activity;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.collegeproject.R;

public class SellDetailActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private RelativeLayout callNowSell, msgNowSell;
    private String title;
    private String price;
    private String email;
    private String phone;
    private String des;
    private String image_url;
    private String name;
    private TextView tvPostedBy, tvPrice, tvDes;
    private ImageView pImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_detail);
        getIntentData();
        toolbar = findViewById(R.id.toolbar);
        callNowSell = findViewById(R.id.callNowSell);
        msgNowSell = findViewById(R.id.msgNowSell);
        tvPostedBy = findViewById(R.id.posted_by);
        tvPrice = findViewById(R.id.price);
        tvDes = findViewById(R.id.des);
        pImage = findViewById(R.id.restaurantImage);

        /*ToolBar Setting*/
        toolbar.setTitle(title);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        tvPostedBy.setText("Posted by " + name);
        tvPrice.setText("₹"+price);
        tvDes.setText(des);

        Glide.with(getApplicationContext()).load(image_url).into(pImage);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        /*============*/

        callNowSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:"+phone));
                startActivity(intent);
            }
        });

        msgNowSell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("smsto:"+phone);
                Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                //intent.putExtra("I want to buy", "I want to buy the laptop");
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    private void getIntentData(){
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle!=null){
            title = bundle.getString("title");
            price = bundle.getString("price");
            email = bundle.getString("email");
            phone = bundle.getString("phone");
            des = bundle.getString("des");
            image_url = bundle.getString("image_url");
            name = bundle.getString("name");
        }
    }

}
