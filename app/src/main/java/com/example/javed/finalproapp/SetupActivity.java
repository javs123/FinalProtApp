package com.example.javed.finalproapp;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class SetupActivity extends AppCompatActivity {

    private CircleImageView setupImage;
    private Uri mainImageUri = null;

    private String user_id;
    private Boolean isChanged = false;

    private EditText setupName;
    private EditText setupLastname;
    private EditText setupBio;
    private EditText setupAge;
    private Button setupBtn;
    private ProgressBar setupProgrss;
    private Button addInterestBtn;

    private TextView showInterest;
    private String[] list_items;
    private boolean[] checkedItems;
    private ArrayList<Integer> mUserItems = new ArrayList<>();
    private String user_interestsSelected="";


    private StorageReference storageReference;
    private FirebaseAuth firebaseAuth;
    private FirebaseFirestore firebaseFirestore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setup);

        firebaseAuth = FirebaseAuth.getInstance();
        user_id = firebaseAuth.getCurrentUser().getUid();
        firebaseFirestore = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        //assign
        setupImage = findViewById(R.id.setup_image);
        setupName = findViewById(R.id.setup_name);
        setupLastname = findViewById(R.id.setup_lastname);
        setupBio = findViewById(R.id.setup_bio);
        setupAge = findViewById(R.id.setup_age);
        setupBtn = findViewById(R.id.setup_btn);
        setupProgrss = findViewById(R.id.setup_progress);
        addInterestBtn = findViewById(R.id.add_interest_btn);
        showInterest = findViewById(R.id.show_interest_textView);

        list_items = getResources().getStringArray(R.array.user_interest);
        checkedItems = new boolean[list_items.length];


        setupProgrss.setVisibility(View.VISIBLE);
        setupBtn.setEnabled(false);

        //add user Interest Button///////
        addInterestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder mBuilder = new AlertDialog.Builder(SetupActivity.this);
                mBuilder.setTitle("Choose Your Interests");
                mBuilder.setMultiChoiceItems(list_items, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (isChecked){
                            if (!mUserItems.contains(which)){
                                mUserItems.add(which);
                            }
                        } else if (mUserItems.contains(which)){
                            mUserItems.remove((Integer) which);
                        }
                    }
                });

                mBuilder.setCancelable(false);
                mBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        user_interestsSelected = "";
                        for (int i=0; i<mUserItems.size();i++){
                            user_interestsSelected = user_interestsSelected + list_items[mUserItems.get(i)];
                            if (i != mUserItems.size() -1){
                                user_interestsSelected = user_interestsSelected + ", ";
                            }
                        }
                        showInterest.setText(user_interestsSelected);
                    }
                });
                mBuilder.setNegativeButton("Dismiss", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                mBuilder.setNeutralButton("Clear All", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        for (int i = 0; i < checkedItems.length; i++){
                            checkedItems[i] = false;
                            mUserItems.clear();
                            showInterest.setText(getString(R.string.user_interests));
                        }
                    }
                });
                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });

        ////////////////////////////

        //click on save button to upload user data to database
        setupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String user_name = setupName.getText().toString();
                final String user_lastname = setupLastname.getText().toString();

                String[] interestArray = user_interestsSelected.split("\\s*,\\s*");
                final List<String> interests = Arrays.asList(interestArray);

                //brake in separate ifs
                if (!TextUtils.isEmpty(user_name) && mainImageUri != null&&interestArray[0]!="") {
                    setupProgrss.setVisibility(View.VISIBLE);
                    if (isChanged) {

                        final StorageReference image_path = storageReference.child("profile_images").child(user_id + ".jpg");
                        UploadTask uploadTask = image_path.putFile(mainImageUri);
                        /////////////////////////////
                        Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                            @Override
                            public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                                if (!task.isSuccessful()) {
                                    throw task.getException();
                                }
                                // Continue with the task to get the download URL
                                return image_path.getDownloadUrl();
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    storeFirestore(task, user_name, user_lastname, interests);
                                } else {
                                    String error = task.getException().getMessage();
                                    Toast.makeText(SetupActivity.this, "Error: " + error, Toast.LENGTH_LONG).show();
                                }
                            }
                        }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                setupProgrss.setVisibility(View.INVISIBLE);
                            }
                        });
                    }
// else if (){
//                        setupProgrss.setVisibility(View.INVISIBLE);
//                        Toast.makeText(SetupActivity.this, "Please select Interest", Toast.LENGTH_SHORT).show();
//                    }
                    else {

                        storeFirestore(null, user_name, user_lastname, interests);
                    }
                } else {
                    Toast.makeText(SetupActivity.this, "add Image and Name", Toast.LENGTH_LONG).show();

                }

            }
        });


        //click on image view
        setupImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if (ContextCompat.checkSelfPermission(SetupActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(SetupActivity.this, "permission Denied", Toast.LENGTH_LONG).show();
                        ActivityCompat.requestPermissions(SetupActivity.this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
                    } else {
                        imagePicker();
                    }
                } else {
                    imagePicker();
                }
            }
        });





        //get Profile information from database to show
        firebaseFirestore.collection("Users").document(user_id).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {

                    if (task.getResult().exists()) {

                        user_interestsSelected = "";
                        String name = task.getResult().getString("name");
                        String image = task.getResult().getString("image");
                        String getlastname = task.getResult().getString("lastName");
                        String getAge = task.getResult().getString("age");
                        String getBio = task.getResult().getString("bio");
                        DocumentSnapshot documentSnapshot = task.getResult();
                        List<String> inters = (List<String>) documentSnapshot.get("interest");
                        for (int i =0; i<inters.size();i++){
                            user_interestsSelected += inters.get(i)+", ";
                        }

                        mainImageUri = Uri.parse(image);
                        setupName.setEnabled(false); //cant change name
                        setupName.setText(name);
                        setupLastname.setText(getlastname);
                        setupAge.setText(getAge);
                        setupBio.setText(getBio);
                        showInterest.setText(user_interestsSelected);

                        //Can add Placeholder if needed blog prt 6 25mns
                        Glide.with(SetupActivity.this).load(image).into(setupImage);

                    }
                } else {

                    String error = task.getException().getMessage();
                    Toast.makeText(SetupActivity.this, "Error: " + error, Toast.LENGTH_LONG).show();

                }
                setupProgrss.setVisibility(View.INVISIBLE);
                setupBtn.setEnabled(true);
            }
        });
    }

    private void storeFirestore(Task<Uri> task, String user_name, String user_lastname, List<String> interests) {

        String user_bio = setupBio.getText().toString();
        String user_age = setupAge.getText().toString();

        Uri download_url;
        if (task != null) {
            download_url = task.getResult();
        } else {
            download_url = mainImageUri;
        }

        //map User name and Image
        Map<String, Object> userMap = new HashMap<>();
        userMap.put("name", user_name);
        userMap.put("lastName", user_lastname);
        userMap.put("bio", user_bio);
        userMap.put("age", user_age);
        userMap.put("image", download_url.toString());
        userMap.put("userID", user_id);
        userMap.put("interest",interests);

        //Add Image and Name to Database
        firebaseFirestore.collection("Users").document(user_id).set(userMap).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {

                    Toast.makeText(SetupActivity.this, "Profile details Updated ", Toast.LENGTH_LONG).show();
//                    Intent main = new Intent(SetupActivity.this, MainActivity.class);
//                    startActivity(main);
//                    finish();

                } else {
                    String error = task.getException().getMessage();
                    Toast.makeText(SetupActivity.this, "Error: " + error, Toast.LENGTH_LONG).show();
                }

            }
        });
    }

    private void imagePicker() {
        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .setAspectRatio(1, 1)
                .start(SetupActivity.this);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mainImageUri = result.getUri();
                setupImage.setImageURI(mainImageUri);
                isChanged = true;
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
                Toast.makeText(SetupActivity.this, "Error: " + error, Toast.LENGTH_LONG).show();
            }
        }
    }
}
