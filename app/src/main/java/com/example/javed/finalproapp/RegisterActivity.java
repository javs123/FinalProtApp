package com.example.javed.finalproapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class RegisterActivity extends AppCompatActivity {

    private EditText mEmail;
    private EditText mPassword;
    private Button mCreateBtn;

    private FirebaseAuth mAuth;
    private ProgressDialog mProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //firebase auth
        mAuth = FirebaseAuth.getInstance();

        mEmail = findViewById(R.id.reg_email);
        mPassword = findViewById(R.id.reg_password);
        mCreateBtn = findViewById(R.id.reg_btn);

        mProgress = new ProgressDialog(this);

        mCreateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = mEmail.getText().toString();
                String password = mPassword.getText().toString();

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(RegisterActivity.this, "Please Enter Email", Toast.LENGTH_LONG).show();
                } else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(RegisterActivity.this, "Please Enter Password", Toast.LENGTH_LONG).show();
                } else {
                    mProgress.setTitle("Registering User");
                    mProgress.setMessage("Wait while we set up your account");
                    mProgress.setCanceledOnTouchOutside(false);
                    mProgress.show();
                    createAccount(email, password);
                }

            }
        });
    }

    private void createAccount(String email, String password) {
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    mProgress.dismiss();
                    Toast.makeText(RegisterActivity.this, "Register Successful", Toast.LENGTH_LONG).show();
                    setup();
                } else {
                    mProgress.hide();
                    String error;
                    error = task.getException().getMessage();
                    Toast.makeText(RegisterActivity.this, "ERROR: " + error, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void setup() {
        Intent completeProfileActivity = new Intent(RegisterActivity.this, CompleteProfileActivity.class);
        completeProfileActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(completeProfileActivity);
        finish();
    }


}

