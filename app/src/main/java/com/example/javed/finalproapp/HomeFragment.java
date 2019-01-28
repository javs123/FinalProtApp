package com.example.javed.finalproapp;
import android.os.Bundle;
        import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {

    private RecyclerView user_list_view;
    private List<Users> users_list;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        users_list = new ArrayList<>();
        user_list_view = getActivity().findViewById(R.id.other_user_list_view);


        // Inflate the layout for this fragment
        return view;
    }

}