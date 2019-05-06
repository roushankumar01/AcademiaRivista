package com.collegeproject.view.activity;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.collegeproject.R;
import com.collegeproject.view.adapter.RecyclerAdapterImageGrid;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class AddStoryActivity extends AppCompatActivity {
    private static final int CHOOSE_IMAGE = 101;
    private ImageView backAddStory, uploadStory;
    private ImageView storyImage;
    private RelativeLayout addImages;
    private RecyclerView imageGridRecycler;
    private RecyclerAdapterImageGrid recyclerAdapterImageGrid;
    private ArrayList<String> filePaths = new ArrayList<>();
    Uri uriStoryImage;
    StorageReference profileImageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_story);
        init();

        backAddStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        uploadStory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(AddStoryActivity.this, "Uploaded !", Toast.LENGTH_SHORT).show();
                //onBackPressed();
                uopload_story();
            }
        });

        StaggeredGridLayoutManager gridLayoutManager =
                new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL);
        imageGridRecycler.setLayoutManager(gridLayoutManager);
        imageGridRecycler.setItemAnimator(new DefaultItemAnimator());
        imageGridRecycler.setNestedScrollingEnabled(false);

        addImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
    }

    private void uopload_story() {
        if (uriStoryImage!=null){
           // progressBar.setVisibility(View.VISIBLE);
            profileImageReference = FirebaseStorage.getInstance().getReference("StoryPics/").child(System.currentTimeMillis() + "."+getFileExtension(uriStoryImage));
            profileImageReference.putFile(uriStoryImage).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                    if (task.isSuccessful()) {
                       // progressBar.setVisibility(View.GONE);
                        profileImageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String download_url=uri.toString();
                                storeStory(download_url);
                            }
                        });

                    }
                    else {
                        //progressBar.setVisibility(View.GONE);
                        Toast.makeText(AddStoryActivity.this,task.getException().getMessage(),Toast.LENGTH_SHORT).show();
                    }

                }
            });
        }else {
            storeStory("null");
        }
    }

    private void storeStory(String download_url) {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String name  = user.getDisplayName();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> data = new HashMap<>();
        data.put("name",name);
        data.put("story_url",download_url);
        db.collection("Status").document().set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d("TAG", "Data Saved");
                Toast.makeText(getApplicationContext(), "Data Saved", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    /*Multiple image picker library*/
    private void openGallery() {
       /* FilePickerBuilder.getInstance().setMaxCount(10) //max count of images
                .setSelectedFiles(filePaths)
                .setActivityTheme(R.style.LibAppTheme)
                .pickPhoto(AddStoryActivity.this);*/
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, CHOOSE_IMAGE);
    }
    private String getFileExtension(Uri uri){
        ContentResolver cR=getContentResolver();
        MimeTypeMap mime=MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }

   /* @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        switch (requestCode) {
            case FilePickerConst.REQUEST_CODE_PHOTO:
                if (resultCode == Activity.RESULT_OK && data != null) {

                    filePaths.clear();
                    filePaths.addAll(data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_MEDIA));

                    showData(filePaths);
                }
                break;
        }
    }*/
   @Override
   protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
       super.onActivityResult(requestCode, resultCode, data);

       if (requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
           uriStoryImage = data.getData();
           try {
               Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uriStoryImage);
               storyImage.setImageBitmap(bitmap);


           } catch (IOException e) {
               e.printStackTrace();
           }


       }
   }

    private void showData(ArrayList<String> filePaths) {
        recyclerAdapterImageGrid = new RecyclerAdapterImageGrid(AddStoryActivity.this, filePaths);
        imageGridRecycler.setAdapter(recyclerAdapterImageGrid);
        recyclerAdapterImageGrid.notifyDataSetChanged();
    }

    /*============================*/

    private void init() {
        backAddStory = findViewById(R.id.backAddStory);
        uploadStory = findViewById(R.id.uploadStory);
        addImages = findViewById(R.id.addImages);
        storyImage=findViewById(R.id.story_img);
        imageGridRecycler = findViewById(R.id.imageGridRecycler);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }
}
