package com.example.javed.finalproapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class UserRecyclerAdapter extends RecyclerView.Adapter<UserRecyclerAdapter.ViewHolder> {

    private OnItemClickLister listener;


    public List<Users> users_list;
    public  Context context;

    public UserRecyclerAdapter(List<Users> users_list){
        this.users_list = users_list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.single_user_list_item, viewGroup,false);
        context = viewGroup.getContext();


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

        final Users mUsers = users_list.get(i);



       String desc_data = mUsers.getBio();
        viewHolder.setDescText(desc_data);

        String profileImage = mUsers.getImage();
        viewHolder.setmImageView(profileImage);

        String userName = mUsers.getName();
        String userLastname = mUsers.getLastName();
        String setName = userName+" " +userLastname;
        viewHolder.setNameText(setName);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                SharedPreferences.Editor editor = context.getSharedPreferences("userID",Context.MODE_PRIVATE).edit();
//                editor.putString("proID", mUsers.getUserID());
//                editor.apply();
                listener.onItemClick(mUsers.getUserID());

            }
        });



    }

    @Override
    public int getItemCount() {
        return users_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        public CircleImageView mImageView;
        public TextView mNameTexView;

        private View mView;
        private TextView descView;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            mView = itemView;




            ///////////////////////// PASSING DATA TO ONCLICK


        }


        public void setDescText(String descText){
            descView = mView.findViewById(R.id.main_user_bio);
            descView.setText(descText);
        }
        public void setmImageView(String mImView){
            mImageView = mView.findViewById(R.id.main_user_image);
            Glide.with(context).load(mImView).into(mImageView);
        }
        public void setNameText(String userName){
            mNameTexView = mView.findViewById(R.id.main_user_name);
            mNameTexView.setText(userName);
        }
    }


    public interface OnItemClickLister{

        void onItemClick(String userID);

    }
    public void setOnItemClickLister(OnItemClickLister lister){
        this.listener = lister;
    }
}


