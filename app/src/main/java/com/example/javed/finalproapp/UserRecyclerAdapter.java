package com.example.javed.finalproapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

public class UserRecyclerAdapter extends RecyclerView.Adapter<UserRecyclerAdapter.ViewHolder> {

    public List<Users> users_list;
    public UserRecyclerAdapter(List<Users> users_list){

        this.users_list = users_list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return users_list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        View mView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            mView = itemView;
        }
    }
}
