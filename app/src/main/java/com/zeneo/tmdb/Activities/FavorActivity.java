package com.zeneo.tmdb.Activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.TextView;

import com.zeneo.tmdb.Adapters.ViewPagerAdapter;
import com.zeneo.tmdb.Fragments.FavorFragment;
import com.zeneo.tmdb.Fragments.FavorFragment2;
import com.zeneo.tmdb.R;

public class FavorActivity extends AppCompatActivity {

    String toolbartitle;
    ViewPager pager;
    TabLayout tabLayout;
    ViewPagerAdapter pagerAdapter;
    Bundle bundle1,bundle2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favor);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);


        toolbartitle = getIntent().getStringExtra("title");
        getSupportActionBar().setTitle("");
        pager = (ViewPager)findViewById(R.id.viewpager);
        tabLayout = (TabLayout)findViewById(R.id.tablt);
        TextView tv = toolbar.findViewById(R.id.toolbartextview);
        tv.setText(toolbartitle);

        setupViewPager();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

    }

    @Override
    protected void onResume() {
        super.onResume();
        setupViewPager();
    }

    public void setupViewPager(){
        FavorFragment favorFragment = new FavorFragment();
        FavorFragment2 favorFragment2 = new FavorFragment2();
        bundle1 = new Bundle();
        bundle1.putString("type","movie");
        bundle1.putString("database",toolbartitle);
        favorFragment.setArguments(bundle1);
        bundle2 = new Bundle();
        bundle2.putString("type","TV");
        bundle2.putString("database",toolbartitle);
        favorFragment2.setArguments(bundle2);
        Log.i("TAG","resumed");
        pagerAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        pagerAdapter.addFrag(favorFragment,"Movies");
        pagerAdapter.addFrag(favorFragment2,"TV SHOWS");



        pager.setAdapter(pagerAdapter);
        tabLayout.setupWithViewPager(pager);
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}
