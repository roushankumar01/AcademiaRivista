package com.collegeproject.view.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.collegeproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;

public class AdPostActivity extends AppCompatActivity {
    private static final int CHOOSE_IMAGE = 101;
    private StorageReference profileImageReference;
    private ImageView backAdPost, uploadAd, adImage;
    private Button chooseImage;
    private ArrayList<String> filePaths = new ArrayList<>();
    private Uri uriProfileImage;
    private String download_url;
    private EditText etTitle, etPrice, etEmail, etPhone, etDes;
    private TextView tvShowProgress;
    View buttonClickView;
    Map<String, Object> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad_post);
        init();
        //hide soft keyboard
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);



        backAdPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        uploadAd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClickView = v;
                tvShowProgress.setText("Please wait, Uploading image");
                tvShowProgress.setVisibility(View.VISIBLE);
                updateProfile();
            }
        });

        chooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageChooser();
            }
        });

    }

    /*Image Selection*/
    private void openGallery() {
        FilePickerBuilder.getInstance().setMaxCount(1)
                .setSelectedFiles(filePaths)
                .setActivityTheme(R.style.LibAppTheme)
                .pickPhoto(AdPostActivity.this);
    }

    /*===============*/

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
    private void init(){
        backAdPost = findViewById(R.id.backAdPost);
        uploadAd = findViewById(R.id.uploadAd);
        chooseImage = findViewById(R.id.chooseImage);
        adImage = findViewById(R.id.adImage);
        etTitle = findViewById(R.id.input_product_title);
        etPrice = findViewById(R.id.input_product_price);
        etEmail = findViewById(R.id.input_product_email);
        etPhone = findViewById(R.id.input_product_phone);
        etDes = findViewById(R.id.input_product_dec);
        tvShowProgress = findViewById(R.id.show_upload_status);

    }

    private void showImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, CHOOSE_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uriProfileImage = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriProfileImage);
                adImage.setImageBitmap(bitmap);


            } catch (IOException e) {
                e.printStackTrace();
            }


        }
    }
    private String getFileExtension(Uri uri){
        ContentResolver cR=getContentResolver();
        MimeTypeMap mime=MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    private void updateProfile() {
        String title = etTitle.getText().toString();
        String price = etPrice.getText().toString();
        String email = etEmail.getText().toString();
        String phone = etPhone.getText().toString();
        String des = etDes.getText().toString();
        if(title.isEmpty()){
            showSnacker("Enter doc Name");
            return;
        }
        if(price.isEmpty()){
            showSnacker("Enter doc Name");
            return;
        }
        if(email.isEmpty()){
            showSnacker("Enter doc Name");
            return;
        }
        if(phone.isEmpty()){
            showSnacker("Enter doc Name");
            return;
        }
        if (uriProfileImage!=null){
            profileImageReference = FirebaseStorage.getInstance().getReference("buyselladd/").child(System.currentTimeMillis() + "."+getFileExtension(uriProfileImage));
            profileImageReference.putFile(uriProfileImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        tvShowProgress.setText("Photo Uploaded, Please wait updating database");
                        profileImageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                download_url=uri.toString();
                                storeUserInfo(download_url);
                            }
                        });

                    }
                    else {
                        Toast.makeText(getApplicationContext(),task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    }

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0 * taskSnapshot.getBytesTransferred()) / taskSnapshot.getTotalByteCount();
                    tvShowProgress.setText("Uploading " + progress + "%");
                }
            });
        }else {
            showSnacker("Select a image");

        }
    }

    private void storeUserInfo(String download_url) {
        String title = etTitle.getText().toString();
        String price = etPrice.getText().toString();
        String email = etEmail.getText().toString();
        String phone = etPhone.getText().toString();
        String des = etDes.getText().toString();
        if(title.isEmpty()){
            showSnacker("Enter doc Name");
            return;
        }
        if(price.isEmpty()){
            showSnacker("Enter doc Name");
            return;
        }
        if(email.isEmpty()){
            showSnacker("Enter doc Name");
            return;
        }
        if(phone.isEmpty()){
            showSnacker("Enter doc Name");
            return;
        }
        String name = "";
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user!=null){
            name = user.getEmail();
        }
        data = new HashMap<>();
        data.put("title",title);
        data.put("price",price);
        data.put("email",email);
        data.put("phone",phone);
        data.put("des",des);
        data.put("image_url", download_url);
        data.put("name", name);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("adds").document().set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()){
                    showSnacker("Add has been posted");
                    finish();
                }else {
                    showSnacker("Doc upload failed due " + task.getException());
                }
            }
        });



    }
    private void showSnacker(String message){
        Snackbar.make(buttonClickView, message, Snackbar.LENGTH_LONG ).show();
    }
}
