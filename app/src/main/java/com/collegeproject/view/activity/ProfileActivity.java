package com.collegeproject.view.activity;

import android.app.Activity;
import com.github.ybq.android.spinkit.style.Circle;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.collegeproject.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;

public class ProfileActivity extends AppCompatActivity {
    private static final int CHOOSE_IMAGE = 101;
    private ImageView backProfile;
    private CircleImageView profileImage_iV;
    private ArrayList<String> filePaths = new ArrayList<>();
    private FirebaseFirestore db;
    private String uid;
    private EditText mName;
    private EditText mPhone;
    private EditText mEmail;
    private ProgressBar progressBar;
    StorageReference profileImageReference;
    Uri uriProfileImage;
    Uri profileImageUrl;
    String profileUrl;
    private Button btnSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        init();

        backProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        profileImage_iV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //uploadImage();
               // updateData(mName.getText().toString(),mEmail.getText().toString(), mPhone.getText().toString());
                updateProfile();
            }
        });
    }

    /*Image Selection*/
    private void openGallery() {
      /*  FilePickerBuilder.getInstance().setMaxCount(1)
                .setSelectedFiles(filePaths)
                .setActivityTheme(R.style.LibAppTheme)
                .pickPhoto(ProfileActivity.this);*/
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, CHOOSE_IMAGE);
    }

/*    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case FilePickerConst.REQUEST_CODE_PHOTO:
                if (resultCode == Activity.RESULT_OK && data != null) {
                    Log.d("TAG", "Result");
                    filePaths.clear();
                    filePaths.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA));
                    Glide.with(ProfileActivity.this).load(filePaths.get(0)).into(profileImage_iV);
                    uriProfileImage = data.getData();
                }
                break;
        }
    }*/
@Override
protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
    super.onActivityResult(requestCode, resultCode, data);

    if (requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
        uriProfileImage = data.getData();
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriProfileImage);
            profileImage_iV.setImageBitmap(bitmap);


        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}

    /*===============*/

    private void init() {

        backProfile = findViewById(R.id.backProfile);
        profileImage_iV = findViewById(R.id.profileImage_iV);
        mName = findViewById(R.id.name_profile);
        mEmail = findViewById(R.id.email_profile);
        mEmail.setEnabled(false);
        mPhone = findViewById(R.id.phone_profile);
        btnSubmit = findViewById(R.id.submit_profile);
        progressBar=(ProgressBar)findViewById(R.id.progressbar_main);
        Circle wave=new Circle();
        progressBar.setIndeterminateDrawable(wave);


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user!=null){
            uid = user.getUid();
            Log.d("UID","UID : "+uid);
            fetchData(uid);
        }

    }

    private void fetchData(String uid){
        db = FirebaseFirestore.getInstance();
        DocumentReference docRef = db.collection("Users").document(uid);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d("TAG", "DocumentSnapshot data: " + document.getData());
                        String name = document.getString("name");
                        String phone = document.getString("phone");
                        String email = document.getString("email");
                        String imageUrl = document.getString("imageurl");
                        Log.d("TAg","profileUrl:"+imageUrl);
                        updateUI(name, email, phone,imageUrl);
                        Log.d("TAG", "Name : "+name);
                    } else {
                        Log.d("TAG", "No such document");
                    }
                } else {
                    Log.d("TAG", "get failed with ", task.getException());
                }
            }
        });
    }
    private void updateProfile() {
        if (uriProfileImage!=null){
            progressBar.setVisibility(View.VISIBLE);
            profileImageReference = FirebaseStorage.getInstance().getReference("profilePics/").child(System.currentTimeMillis() + "."+getFileExtension(uriProfileImage));
            profileImageReference.putFile(uriProfileImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                        progressBar.setVisibility(View.GONE);
                        profileImageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String download_url=uri.toString();
                                storeUserInfo(download_url);
                            }
                        });

                    }
                    else {
                        progressBar.setVisibility(View.GONE);
                        Toast.makeText(ProfileActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }else {
            storeUserInfo("null");
        }
    }

    private void storeUserInfo(String download_url) {
        String name=mName.getText().toString();
        String email=mEmail.getText().toString();
        String phone=mPhone.getText().toString();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> data = new HashMap<>();
        data.put("name", name);
        data.put("email", email);
        data.put("phone", phone);
        data.put("imageurl",download_url);
        db.collection("Users").document(uid).update(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d("TAG", "Data Saved");
                Toast.makeText(getApplicationContext(), "Data Saved", Toast.LENGTH_SHORT).show();
                finish();
            }
        });

    }

    private void updateUI(String name, String email, String phone, String imageurl){
        Log.d("TAg","profileUrl:"+imageurl);
        if (!imageurl.equals("null")){
            Uri uri = Uri.parse(imageurl);
            Glide.with(this).load(uri).into(profileImage_iV);
        }else {
            Glide.with(this).load(R.drawable.logo).into(profileImage_iV);
        }
        mName.setText(name);
        mEmail.setText(email);
        mPhone.setText(phone);

    }
    private void uploadImage(Uri filepath){
        Log.d("TAG","Uploaded Method");
        profileImageReference = FirebaseStorage.getInstance().getReference();
        profileImageReference.child("ProfilePics").child("test.jpg");
        if (filepath != null) {
            Log.d("TAG","Upload Start");
            StorageTask uploadTask= profileImageReference.putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    profileImageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            profileImageUrl=uri;
                            profileUrl = uri.toString();
                            Log.d("TAG","Uploaded");


                        }
                    });
                    Toast.makeText(getApplicationContext(),"Image Upload Sucess...",Toast.LENGTH_LONG).show();

                }
            })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getApplicationContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

        }else {
            Log.d("TAG","URI not found");
        }
    }
    private void updateData(String name, String email, String phone){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> data = new HashMap<>();
        data.put("name", name);
        data.put("email", email);
        data.put("phone", phone);
        db.collection("Users").document(uid).update(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d("TAG", "Data Saved");
                Toast.makeText(getApplicationContext(), "Data Saved", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    private String getFileExtension(Uri uri){
        ContentResolver cR=getContentResolver();
        MimeTypeMap mime=MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
