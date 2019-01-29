package com.example.javed.finalproapp;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ToolbarWidgetWrapper;
import android.widget.Toolbar;

public class ChatActivity extends AppCompatActivity {

    private android.support.v7.widget.Toolbar mToolbar;
    private ViewPager mviewpager;
    private SectionsPagerAdaptor mSectionsPagerAdaptor;
    private TabLayout tabLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        mToolbar = findViewById(R.id.chatToolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Messages");

        mviewpager = (ViewPager) findViewById(R.id.main_tabPager);
        mSectionsPagerAdaptor = new SectionsPagerAdaptor(getSupportFragmentManager());
        mviewpager.setAdapter(mSectionsPagerAdaptor);

        tabLayout = findViewById(R.id.main_tabs);
        tabLayout.setupWithViewPager(mviewpager);


    }
}
