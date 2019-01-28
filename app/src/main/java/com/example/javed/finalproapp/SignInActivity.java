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

public class SignInActivity extends AppCompatActivity {

    private EditText mSignInEmail;
    private EditText mSignInPassword;
    private Button mSignInBtn;
    private ProgressDialog mSignInProgress;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mSignInEmail = findViewById(R.id.signin_email_input);
        mSignInPassword = findViewById(R.id.sign_password_input);
        mSignInBtn = findViewById(R.id.signin_btn);
        mSignInProgress = new ProgressDialog(this);



        //firebase auth
        mAuth = FirebaseAuth.getInstance();

        mSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String SignInEmailInput = mSignInEmail.getText().toString();
                String SignInPasswordInput = mSignInPassword.getText().toString();


                if(TextUtils.isEmpty(SignInEmailInput)){
                    Toast.makeText(SignInActivity.this,"Please Enter Email and Try Again", Toast.LENGTH_LONG).show();

                }else if(TextUtils.isEmpty(SignInPasswordInput)) {
                    Toast.makeText(SignInActivity.this,"Please Enter Password and Try Again", Toast.LENGTH_LONG).show();
                }
                else{
                    mSignInProgress.setTitle("Signing In");
                    mSignInProgress.setMessage("Please Wait");
                    mSignInProgress.setCanceledOnTouchOutside(false);
                    mSignInProgress.show();
                    signInUser(SignInEmailInput,SignInPasswordInput);
                }


            }
        });


    }

    private void signInUser(String signInEmailInput, String signInPasswordInput) {
        mAuth.signInWithEmailAndPassword(signInEmailInput, signInPasswordInput)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(SignInActivity.this,"Successful", Toast.LENGTH_LONG).show();
                            Intent completeProfileActivity = new Intent(SignInActivity.this, MainActivity.class);
                            completeProfileActivity.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK |Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(completeProfileActivity);
                            finish();
                        } else {
                            mSignInProgress.hide();
                            String getError = task.getException().getMessage();
                            Toast.makeText(SignInActivity.this,"ERROR: "+getError, Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }
}
