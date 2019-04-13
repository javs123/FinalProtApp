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
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.support.constraint.Constraints.TAG;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private FirebaseAuth mAuth;
    private NavigationView navigationView;
    private FirebaseFirestore firebaseFirestore;

    private String ageFilter;
    private String genderFilter;

    private String userID;

    private CircleImageView drawerImage;
    private TextView userName;
    private TextView userEmail;


    private List<Users> usersList = new ArrayList<>();
    private RecyclerView recyclerView;
    private UserRecyclerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


//        recyclerView = (RecyclerView) findViewById(R.id.other_user_list_view);
//
//        mAdapter = new UserRecyclerAdapter(usersList);
//        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));
//        recyclerView.setAdapter(mAdapter);



        //check if user has completed profile
        mAuth = FirebaseAuth.getInstance();
        firebaseFirestore = FirebaseFirestore.getInstance();

//        userID = mAuth.getCurrentUser().getUid();
//        final String email = mAuth.getCurrentUser().getEmail();




        //add toolbar to the main
        Toolbar toolbar = findViewById(R.id.toolbarP);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("FriendMe");



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
                Intent edit = new Intent(MainActivity.this,SetupActivity.class);
                startActivity(edit);
                break;
            case R.id.nav_logout:
                sendToStart();
                FirebaseAuth.getInstance().signOut();
                break;
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
//
//    ///////////toolbar menu btns
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_buttons,menu);
//
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//
//        switch (item.getItemId()){
//            case R.id.action_filter_btn:
//                //filter users by age , gender
//
//               // Toast.makeText(this, "Filter", Toast.LENGTH_SHORT).show();
//                openDialog();
//
//                break;
//            case R.id.action_search_btn:
//
//                View search_input = findViewById(R.id.action_search_btn);
//
//
//
//                /// search user by #tags
//                break;
//        }
//
//        return true;
//
//    }
//
//    private void openDialog() {
//
//
//        FilterDialog filterDialog = new FilterDialog();
//        filterDialog.show(getSupportFragmentManager(),"Filter Dialog");
//
//    }
//
//    @Override
//    public void getFilterResultBoth(String age, String gender) {
//
//        ageFilter = age;
//        genderFilter = gender;
//
//
//        Toast.makeText(this, "Both "+ageFilter+genderFilter, Toast.LENGTH_SHORT).show();
//
//    }
//
//    @Override
//    public void getFilterResultAge(String age) {
//
//        ageFilter = age;
//        Toast.makeText(this, "Age "+ageFilter, Toast.LENGTH_SHORT).show();
//    }
//
//    @Override
//    public void getFilterResultGender(String gender) {
//        genderFilter = gender;
//
////        firebaseFirestore.collection("Users").get().addOnCompleteListener(MainActivity.this,new OnCompleteListener<QuerySnapshot>() {
////            @Override
////            public void onComplete(@NonNull Task<QuerySnapshot> task) {
////                if (task.isSuccessful()) {
////                    for (QueryDocumentSnapshot document : task.getResult()) {
////                        Log.d(TAG, document.getId() + " => " + document.getData());
////                        if (document.getData().get("userID").equals(userID)) {
////                            Toast.makeText(MainActivity.this, "current", Toast.LENGTH_SHORT).show();
////
////
////                        } else {
////                            Users usdd = document.toObject(Users.class);
////                            usersList.add(usdd);
////                            mAdapter.notifyDataSetChanged();
////                        }
////                    }
////                } else {
////                    Toast.makeText(MainActivity.this, "No Interest", Toast.LENGTH_SHORT).show();
////                }
////            }
////        });
//
//
//        Toast.makeText(this, "Gender", Toast.LENGTH_SHORT).show();
//    }

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
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment()).commit();
            navigationView.setCheckedItem(R.id.nav_home);

        }
    }

    private void getUserProComplete() {
        String user_id = mAuth.getCurrentUser().getUid();
        final String email = mAuth.getCurrentUser().getEmail();

        firebaseFirestore.collection("Users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
             if (task.isSuccessful()){

                 if (!task.getResult().exists()){
//                     String name = task.getResult().getString("name");
                     Toast.makeText(MainActivity.this, "Please Complete Profile ", Toast.LENGTH_SHORT).show();
                     Intent sIntent = new Intent(MainActivity.this, CompleteProfileActivity.class);
                     startActivity(sIntent);
                     finish();
                 }
                 else {
                     String name = task.getResult().getString("name");
                        String image = task.getResult().getString("image");
                        String getlastname = task.getResult().getString("lastName");
////                        String getAge = task.getResult().getString("age");
////                        String getBio = task.getResult().getString("bio");
////                        DocumentSnapshot documentSnapshot = task.getResult();
////                        Uri mainImageUri = Uri.parse(image);
////                        setupName.setEnabled(false); //cant change name
                        userName.setText(name+" "+getlastname);
                        userEmail.setText(email);
//                        //Can add Placeholder if needed blog prt 6 25mns
                        Glide.with(MainActivity.this).load(image).into(drawerImage);
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
