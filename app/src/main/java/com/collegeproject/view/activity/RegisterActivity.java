package com.collegeproject.view.activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.collegeproject.R;
import com.github.ybq.android.spinkit.style.Circle;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private TextView goLogin_tV;
    private Button register_btn;
    private ImageView backRegister_iV;
    private EditText emailInput;
    private EditText nameInput;
    private EditText phoneInput;
    private EditText passwordInput;
    private FirebaseAuth mAuth;
    private ProgressBar progressbar;
    private TextView registerProgress;
    private View buttonClickView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();

        backRegister_iV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        goLogin_tV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                RegisterActivity.this.finish();
            }
        });

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClickView = v;
                registerProgress.setText("Registering as new user");
                registerProgress.setVisibility(View.VISIBLE);
                String name  = nameInput.getText().toString();
                String email = emailInput.getText().toString();
                String phone = phoneInput.getText().toString();
                String password = passwordInput.getText().toString();
                registor(name, email, phone, password);
            }
        });
    }

    private void init() {
        goLogin_tV = findViewById(R.id.goLogin_tV);
        register_btn= findViewById(R.id.register_btn);
        backRegister_iV = findViewById(R.id.backRegister_iV);
        nameInput = findViewById(R.id.name_register);
        emailInput = findViewById(R.id.email_register);
        phoneInput = findViewById(R.id.phone_register);
        passwordInput = findViewById(R.id.password_register);
        mAuth = FirebaseAuth.getInstance();
        registerProgress = findViewById(R.id.show_register_progress);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish(); //It clears the activity from backstack
    }
    private void registor(final String name, final String email, final String phone, String password){
        if (email.isEmpty()){
            showSnacker("Enter Email");
            return;
        }
        if (password.isEmpty()){
            showSnacker("Enter Password");
            return;
        }
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Log.d("TAG","Register Success");
                    registerProgress.setText("Register Success updating other details to db");
                    Toast.makeText(getApplicationContext(),"Register Success",Toast.LENGTH_SHORT).show();
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user!=null){
                        final String uid = user.getUid();
                        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                .setDisplayName(name)
                                .build();

                        user.updateProfile(profileUpdates)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Log.d("TAG", "User profile updated.");
                                            Log.d("TAG","UID : "+uid);
                                            updateDataToDB(name, email, phone, uid);
                                        }
                                    }
                                });


                    }
                }else {
                    registerProgress.setText("Register Failed" + task.getException());
                    Log.d("TAG", "Register Failed" + task.getException());
                    Toast.makeText(getApplicationContext(), "Register Failed Due to : "+task.getException(),Toast.LENGTH_SHORT);

                }
            }
        });
    }
    private void updateDataToDB(String name, String email, String phone, String uid){
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> data = new HashMap<>();
        data.put("name", name);
        data.put("email", email);
        data.put("phone", phone);
        data.put("uid", uid);
        db.collection("Users").document(uid).set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                Log.d("TAG", "Data Saved");
                Toast.makeText(getApplicationContext(), "Data Saved", Toast.LENGTH_SHORT).show();
                gotoHome();
            }
        });
    }
    private void gotoHome(){
        startActivity(new Intent(RegisterActivity.this, HomeActivity.class));
        RegisterActivity.this.finish();
    }

    private void startProgressBar() {

        progressbar=(ProgressBar)findViewById(R.id.progressbar_register);
        Circle wave=new Circle();
        progressbar.setIndeterminateDrawable(wave);
        progressbar.setVisibility(View.VISIBLE);
    }
    private void showSnacker(String message){
        Snackbar.make(buttonClickView, message, Snackbar.LENGTH_LONG ).show();
    }
}
