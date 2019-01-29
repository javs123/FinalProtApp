package com.example.javed.finalproapp;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfileActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView textViewName;
    private TextView textViewBio;
    private Button setupEditBtn;
    private ProgressBar setupProgrss;
    private String user_id;


    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        firebaseAuth = FirebaseAuth.getInstance();
        user_id = firebaseAuth.getCurrentUser().getUid();
        firebaseFirestore = FirebaseFirestore.getInstance();

        setupProgrss = findViewById(R.id.profile_progressBar);
        imageView = findViewById(R.id.pro_user_image);
        textViewName = findViewById(R.id.pro_user_name);
        textViewBio = findViewById(R.id.pro_user_bio);
        setupEditBtn = findViewById(R.id.pro_edit_setup_btn);


        setupProgrss.setVisibility(View.VISIBLE);
        setupEditBtn.setEnabled(false);
        //get Profile information from database to show
        firebaseFirestore.collection("Users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {

                    if (task.getResult().exists()) {

                        String name = task.getResult().getString("name");
                        String image = task.getResult().getString("image");
                        String getlastname = task.getResult().getString("lastName");
                        String getAge = task.getResult().getString("age");
                        String getBio = task.getResult().getString("bio");

                        //mainImageUri = Uri.parse(image);
                        //setupName.setEnabled(false); //cant change name
                        textViewName.setText(name + " "+getlastname);
                        //setupAge.setText(getAge);
                        textViewBio.setText(getBio);
                        //Can add Placeholder if needed blog prt 6 25mns
                        Glide.with(ProfileActivity.this).load(image).into(imageView);

                    }
                } else {

                    String error = task.getException().getMessage();
                    Toast.makeText(ProfileActivity.this, "Error: " + error, Toast.LENGTH_LONG).show();

                }
                setupProgrss.setVisibility(View.INVISIBLE);
                setupEditBtn.setEnabled(true);
            }
        });


        setupEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent edit = new Intent(ProfileActivity.this,SetupActivity.class);
                startActivity(edit);
            }
        });


    }
}
