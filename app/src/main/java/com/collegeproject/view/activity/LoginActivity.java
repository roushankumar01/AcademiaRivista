package com.collegeproject.view.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private Button login_btn;
    private TextView goRegister_tV;
    private FirebaseAuth mAuth;
    private EditText emailInput;
    private EditText passwordInput;
    private ProgressBar progressbar;
    private TextView loginProgress;
    private View buttonClickView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user!=null){
            gotoHome();
        }
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        emailInput = findViewById(R.id.userId_eT);
        passwordInput = findViewById(R.id.userPass_eT);
        loginProgress = findViewById(R.id.show_login_progress);
        init();

        requestAllPermission();
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                buttonClickView = v;
                loginProgress.setText("Please wait, authenticating email password");
                loginProgress.setVisibility(View.VISIBLE);
                String email = emailInput.getText().toString();
                String password = passwordInput.getText().toString();
                Log.d("TAG", "Email , password : " + email +","+ password);
                login(email, password);
            }
        });

        goRegister_tV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }

    private void init() {
        login_btn = findViewById(R.id.login_btn);
        goRegister_tV = findViewById(R.id.goRegister_tV);
    }

    @Override
    public void onBackPressed() {
        //for performing back button action of device
        //no "super.onBackPressed()" called so that user can't go back
    }

    /*Application Runtime Permissions*/
    private void requestAllPermission() {
        Dexter.withActivity(this)
                .withPermissions(
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            // Toast.makeText(getApplicationContext(), "All permissions are granted!", Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings
                            showSettingsDialog();
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(getApplicationContext(), "Error occurred! " + error.toString(), Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    private void showSettingsDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        builder.setTitle("Need Permissions");
        builder.setMessage("This app needs permission to use this feature. You can grant them in app settings.");
        builder.setPositiveButton("GOTO SETTINGS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                openSettings();
            }
        });
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        builder.show();
    }

    // navigating user to app settings
    private void openSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        Uri uri = Uri.fromParts("package", getPackageName(), null);
        intent.setData(uri);
        startActivityForResult(intent, 101);
    }

    private void login(String email, String password){
        if (email.isEmpty()){
            showSnacker("Enter email");
            return;
        }
        if (password.isEmpty()){
            showSnacker("Enter Password");
            return;
        }
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Log.d("TAG", "Login Success");
                    loginProgress.setText("Login success");
                    Toast.makeText(getApplicationContext(), "Login Succes",Toast.LENGTH_SHORT).show();
                    gotoHome();
                }else {
                    Log.w("TAG", "signInWithEmail:failure", task.getException());
                    Toast.makeText(getApplicationContext(), "Login Failed due to "+ task.getException(),Toast.LENGTH_SHORT).show();
                    loginProgress.setText("Login Failed due to "+ task.getException());

                }
            }
        });
    }

    private void startProgressBar() {

        progressbar=(ProgressBar)findViewById(R.id.progressbar_login);
        Circle wave=new Circle();
        progressbar.setIndeterminateDrawable(wave);
        progressbar.setVisibility(View.VISIBLE);
    }
    private void showSnacker(String message){
        Snackbar.make(buttonClickView, message, Snackbar.LENGTH_LONG ).show();
    }
    private void gotoHome(){
        startActivity(new Intent(LoginActivity.this, HomeActivity.class));
    }
    /*===============================*/
}
