package com.example.javed.finalproapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        mSignInEmail = findViewById(R.id.signin_email_input);
        mSignInPassword = findViewById(R.id.sign_password_input);
        mSignInBtn = findViewById(R.id.signin_btn);

        //firebase auth
        mAuth = FirebaseAuth.getInstance();

        mSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String SignInEmailInput = mSignInEmail.getText().toString();
                String SignInPasswordInput = mSignInPassword.getText().toString();


                signInUser(SignInEmailInput,SignInPasswordInput);

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
                            startActivity(completeProfileActivity);
                            finish();
                        } else {
                            Toast.makeText(SignInActivity.this,"There was an ERROR", Toast.LENGTH_LONG).show();
                        }
                    }
                });

    }
}
