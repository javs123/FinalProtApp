package com.example.javed.finalproapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.auth.User;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import static android.support.constraint.Constraints.TAG;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private RecyclerView user_list_view;
    private List<Users> users_list;
    private UserRecyclerAdapter userRecyclerAdapter;

    private FirebaseFirestore firebaseFirestore;
    private CollectionReference usersRef;
    private FirebaseAuth mAuth;
    private String firstInterst;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);


        users_list = new ArrayList<>();
        user_list_view = view.findViewById(R.id.other_user_list_view);

        firebaseFirestore = FirebaseFirestore.getInstance();
        usersRef = firebaseFirestore.collection("Users");
        mAuth = FirebaseAuth.getInstance();

        userRecyclerAdapter = new UserRecyclerAdapter(users_list);
        user_list_view.setHasFixedSize(true);
        user_list_view.setLayoutManager(new LinearLayoutManager(getActivity()));
        user_list_view.setAdapter(userRecyclerAdapter);
        FirebaseUser currentUser = mAuth.getCurrentUser();
        final String userID = currentUser.getUid();
        usersRef.document(userID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().exists()) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        List<String> inters = (List<String>) documentSnapshot.get("interest");

                        firstInterst = inters.get(0);

                        getUser(firstInterst, userID);

                    } else {
                        Toast.makeText(getActivity(), "task Not exist", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Task FAiled", Toast.LENGTH_SHORT).show();
                }
            }
        });


        /////


        // Inflate the layout for this fragment

        ///////////// CALLING THE ONCLICK
        userRecyclerAdapter.setOnItemClickLister(new UserRecyclerAdapter.OnItemClickLister() {
            @Override
            public void onItemClick(String userID) {
                Intent userProfile = new Intent(getActivity(), UserProfileActivity.class);
                userProfile.putExtra("userID", userID);
                startActivity(userProfile);

            }
        });


        return view;
    }


    private void getUser(String interst, final String userID) {
        usersRef.whereArrayContains("interest", interst)
                .get().addOnCompleteListener(getActivity(), new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Log.d(TAG, document.getId() + " => " + document.getData());
                        if (document.getData().get("userID").equals(userID)) {
                            //Toast.makeText(getActivity(), "current", Toast.LENGTH_SHORT).show();

                        } else {
                            Users usdd = document.toObject(Users.class);
                            users_list.add(usdd);
                            userRecyclerAdapter.notifyDataSetChanged();
                        }
                    }
                } else {
                    Toast.makeText(getActivity(), "No Interest", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}