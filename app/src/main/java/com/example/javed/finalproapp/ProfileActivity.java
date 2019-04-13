package com.example.javed.finalproapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
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
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

public class ProfileActivity extends AppCompatActivity {

    private ImageView imageView;
    private TextView textViewName;
    private TextView textViewBio;
    private TextView textViewAge;
    private TextView textViewGender;
    private TextView textViewInterests;
    private Button setupEditBtn;
    private ProgressBar setupProgrss;
    private TextView deleteProfile;
    private String user_id;
    private String user_interestsSelected;
    private ImageView userLikebtn;
    private TextView userLikecount;


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
        textViewAge = findViewById(R.id.profile_age);
        textViewGender = findViewById(R.id.profile_gender);
        textViewInterests = findViewById(R.id.profile_interests);

        userLikebtn = findViewById(R.id.like_btn_show);
        userLikecount = findViewById(R.id.user_like_count_show);


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
                        String getGender = task.getResult().getString("gender");
                        DocumentSnapshot documentSnapshot = task.getResult();
                        List<String> inters = (List<String>) documentSnapshot.get("interest");
                        String intrest = inters.toString();
//                        for (int i =0; i<inters.size();i++){
//                            user_interestsSelected = inters.get(i)+", ";
//                        }


                        //mainImageUri = Uri.parse(image);
                        //setupName.setEnabled(false); //cant change name
                        textViewName.setText(name + " " + getlastname);
                        textViewAge.setText(getAge);
                        textViewBio.setText(getBio);
                        textViewGender.setText(getGender);
                        textViewInterests.setText(intrest);
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


        //get like count
        firebaseFirestore.collection("Users/" + user_id + "/Likes").addSnapshotListener(ProfileActivity.this, new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot queryDocumentSnapshots, @Nullable FirebaseFirestoreException e) {
                String countLikes;
                if (!queryDocumentSnapshots.isEmpty()) {

                    int count = queryDocumentSnapshots.size();
                    countLikes = count + " Likes";
                    userLikecount.setText(countLikes);


                } else {
                    countLikes = 0 + " Likes";
                    userLikecount.setText(countLikes);
                }

            }
        });


        setupEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent edit = new Intent(ProfileActivity.this, SetupActivity.class);
                startActivity(edit);
            }
        });


    }


}
