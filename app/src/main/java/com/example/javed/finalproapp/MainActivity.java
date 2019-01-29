package com.example.javed.finalproapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private FirebaseAuth mAuth;
    private NavigationView navigationView;
    private FirebaseFirestore firebaseFirestore;
    private String userID;

    private CircleImageView drawerImage;
    private TextView userName;
    private TextView userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //check if user has completed profile
        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();
        userID = mAuth.getCurrentUser().getUid();
        final String email = mAuth.getCurrentUser().getEmail();


        //add toolbar to the main
        Toolbar toolbar = findViewById(R.id.toolbarP);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Links");


        //add the drawer to the main
        drawer = findViewById(R.id.drawer_layout);

        //make fragments clickable
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        View header = navigationView.getHeaderView(0);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        drawerImage = header.findViewById(R.id.drawar_profile_image);
        userName = header.findViewById(R.id.drawar_username);
        userEmail = header.findViewById(R.id.drawar_useremail);
        //getting data
        firebaseFirestore.collection("Users").document(userID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {

                    if (task.getResult().exists()) {
                        String name = task.getResult().getString("name");
                        String image = task.getResult().getString("image");
                        String getlastname = task.getResult().getString("lastName");
                        String getAge = task.getResult().getString("age");
                        String getBio = task.getResult().getString("bio");
                        DocumentSnapshot documentSnapshot = task.getResult();

//                        Uri mainImageUri = Uri.parse(image);
//                        setupName.setEnabled(false); //cant change name
                        userName.setText(name+" "+getlastname);
                        userEmail.setText(email);
                        //Can add Placeholder if needed blog prt 6 25mns
                        Glide.with(MainActivity.this).load(image).into(drawerImage);

                    }
                } else {

                    String error = task.getException().getMessage();
                    Toast.makeText(MainActivity.this, "Error: " + error, Toast.LENGTH_LONG).show();

                }
            }
        });
    }
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId()){
            case R.id.nav_home:
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        new HomeFragment()).commit();
                break;
            case R.id.nav_profile:
                Intent proIntent = new Intent(MainActivity.this, ProfileActivity.class);
                startActivity(proIntent);
                break;
            case R.id.nav_message:
                Intent chat = new Intent(MainActivity.this,ChatActivity.class);
                startActivity(chat);
                break;
            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                sendToStart();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    ///////////toolbar menu btns
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_buttons,menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.action_filter_btn:
                //filter users by age , gender

                break;
            case R.id.action_search_btn:

                View search_input = findViewById(R.id.action_search_btn);

                /// search user by #tags
                break;
        }

        return true;

    }
///////////////////////////////////end menu btn toolbar
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //if user is not loggedIn send to start page
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            sendToStart();
        }
        else {
            getUserProComplete();
//            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
//                    new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);

        }
    }

    private void getUserProComplete() {
        String user_id = mAuth.getCurrentUser().getUid();
        firebaseFirestore.collection("Users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
             if (task.isSuccessful()){

                 if (!task.getResult().exists()){
//                     String name = task.getResult().getString("name");
                     Toast.makeText(MainActivity.this, "Please Complete Profile ", Toast.LENGTH_SHORT).show();
                     Intent sIntent = new Intent(MainActivity.this, SetupActivity.class);
                     startActivity(sIntent);
                     finish();
                 }
             }else {
                 String error = task.getException().getMessage();
                 Toast.makeText(MainActivity.this, "Error: " + error, Toast.LENGTH_LONG).show();
             }

            }
        });

    }

    //Send User to Start Page Method
    private void sendToStart() {
        Intent startIntent = new Intent(MainActivity.this, StartActivity.class);
        startActivity(startIntent);
        finish();
    }

}
