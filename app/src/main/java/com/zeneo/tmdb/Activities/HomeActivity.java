package com.zeneo.tmdb.Activities;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.drawable.DrawableCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.*;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.zeneo.tmdb.Adapters.SearchListAdapter;
import com.zeneo.tmdb.Adapters.ViewPagerAdapter;
import com.zeneo.tmdb.Fragments.CelebsFragment;
import com.zeneo.tmdb.Fragments.HomeFragment;
import com.zeneo.tmdb.Fragments.MoviesFragment;
import com.zeneo.tmdb.Fragments.NewsFragment;
import com.zeneo.tmdb.Fragments.TvFragment;
import com.zeneo.tmdb.Models.Search;
import com.zeneo.tmdb.R;
import com.zeneo.tmdb.util.HttpHandler;
import com.zeneo.tmdb.util.SearchingOP;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    ViewPagerAdapter fragAdapter;
    LinearLayout sLayout, mLayout;
    RecyclerView recyclerView1, recyclerView2, recyclerView3;
    SearchingOP op1, op2, op3;
    String url1 = "https://api.themoviedb.org/3/search/multi?api_key=5d173b53167711178472dc9d98603e31&language=en-US&page=1&include_adult=false&query=";
    TextView textView;
    List<Search> list = new ArrayList<>();
    SearchView searchView;
    int page = 0;
    String url = "";
    boolean searchViewIsExpanded = false;
    private AdView mAdView;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        mAdView = findViewById(R.id.adViewHome);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        mAdView.setAdListener(new AdListener() {
            @Override
            public void onAdClosed() {
            }

            @Override
            public void onAdFailedToLoad(int error) {
                mAdView.setVisibility(View.GONE);
            }

            @Override
            public void onAdLeftApplication() {
            }

            @Override
            public void onAdOpened() {
            }

            @Override
            public void onAdLoaded() {
                mAdView.setVisibility(View.VISIBLE);
            }
        });

        MoviesFragment fragment1 = new MoviesFragment();
        Bundle bundle = new Bundle();
        bundle.putString("url", "https://api.themoviedb.org/3/discover/movie?api_key=5d173b53167711178472dc9d98603e31&language=en-US&sort_by=popularity.desc&include_adult=false&include_video=false&page=");
        fragment1.setArguments(bundle);

        linkViews();
        ImageView searchIcon = searchView.findViewById(android.support.v7.appcompat.R.id.search_button);
        searchIcon.setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_search));

        fragAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        fragAdapter.addFrag(new HomeFragment(), "");
        fragAdapter.addFrag(fragment1, "");
        fragAdapter.addFrag(new TvFragment(), "");
        fragAdapter.addFrag(new CelebsFragment(), "");
        fragAdapter.addFrag(new NewsFragment(), "");

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sLayout.setVisibility(View.VISIBLE);
                mLayout.setVisibility(View.GONE);
                searchViewIsExpanded = true;
            }
        });
        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                sLayout.setVisibility(View.GONE);
                mLayout.setVisibility(View.VISIBLE);
                searchViewIsExpanded = false;
                return false;
            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {

                //op1.setUrl(url1 + URLEncoder.encode(query, "UTF-8"));
                //op1.getDetails();


                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText != null) {
                    textView.setVisibility(View.GONE);
                    try {
                        url = url1 + URLEncoder.encode(newText, "UTF-8");
                        new Searcher().execute();

                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }


                }

                return false;
            }
        });


        viewPager.setAdapter(fragAdapter);
        tabLayout.setupWithViewPager(viewPager);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            switch (i) {
                case 0:
                    tabLayout.getTabAt(i).setIcon(R.drawable.home);
                    break;
                case 1:
                    tabLayout.getTabAt(i).setIcon(R.drawable.movies);
                    break;
                case 2:
                    tabLayout.getTabAt(i).setIcon(R.drawable.tv);
                    break;
                case 3:
                    tabLayout.getTabAt(i).setIcon(R.drawable.people);
                    break;
                case 4:
                    tabLayout.getTabAt(i).setIcon(R.drawable.library);
                    break;
            }

        }

        DrawableCompat.setTint(tabLayout.getTabAt(page).getIcon(), ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));


        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {

                if (position == 4) {
                    searchView.setVisibility(View.GONE);
                } else searchView.setVisibility(View.VISIBLE);

                DrawableCompat.setTint(tabLayout.getTabAt(position).getIcon(), ContextCompat.getColor(getApplicationContext(), R.color.colorAccent));
                DrawableCompat.setTint(tabLayout.getTabAt(page).getIcon(), ContextCompat.getColor(getApplicationContext(), android.R.color.white));
                page = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchView.setIconified(false);
                searchViewIsExpanded = true;
            }
        });


    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (Integer.parseInt(android.os.Build.VERSION.SDK) > 5
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            Log.d("CDA", "onKeyDown Called");

            if(searchViewIsExpanded){
                searchView.setIconified(true);
                return true;
            }else {
                if (viewPager.getCurrentItem()!=0){
                    viewPager.setCurrentItem(0);
                    return true;
                }
            }

        }

        return super.onKeyDown(keyCode, event);
    }


    public void linkViews() {
        tabLayout = (TabLayout) findViewById(R.id.tablt);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        sLayout = (LinearLayout) findViewById(R.id.search_layout);
        mLayout = (LinearLayout) findViewById(R.id.main_layout);
        recyclerView1 = (RecyclerView) findViewById(R.id.list_mov_search);
        textView = (TextView) findViewById(R.id.simpletxtview);
        searchView = (SearchView) findViewById(R.id.searchView);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return super.onCreateOptionsMenu(menu);
    }

    public ViewPager getViewPager() {
        return viewPager;
    }


    public class Searcher extends AsyncTask<Void, Void, Void> {

        LinearLayout layout;
        String title;
        String imgurl;
        String type;
        int id;
        HttpHandler sh = new HttpHandler();

        @Override
        protected Void doInBackground(Void... voids) {
            list.clear();
            String jsonStr = sh.makeServiceCall(url);
            if (jsonStr != null) {

                try {
                    JSONObject mainObject = new JSONObject(jsonStr);
                    JSONArray results = mainObject.getJSONArray("results");

                    for (int i = 0; i < results.length(); i++) {
                        Log.i("TAGme",String.valueOf(results.length()));
                        type = results.getJSONObject(i).getString("media_type");
                        if (type.equals("movie")){
                            title = results.getJSONObject(i).getString("title");
                            imgurl = "https://image.tmdb.org/t/p/w500" + results.getJSONObject(i).getString("poster_path");
                            id = results.getJSONObject(i).getInt("id");
                        }else if (type.equals("tv")){
                            title = results.getJSONObject(i).getString("name");
                            imgurl = "https://image.tmdb.org/t/p/w500" + results.getJSONObject(i).getString("poster_path");
                            id = results.getJSONObject(i).getInt("id");
                        }else if (type.equals("person")){
                            title = results.getJSONObject(i).getString("name");
                            imgurl = "https://image.tmdb.org/t/p/w500" + results.getJSONObject(i).getString("profile_path");
                            id = results.getJSONObject(i).getInt("id");
                        }
                        list.add(new Search(title, imgurl, type,id));

                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            layout = findViewById(R.id.sltprogress);
            layout.setVisibility(View.VISIBLE);
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            final SearchListAdapter[] adapter = new SearchListAdapter[1];
            if (list.size() > 0) {
                textView.setVisibility(View.GONE);
            }else {
                textView.setVisibility(View.VISIBLE);
            }

            adapter[0] = new SearchListAdapter(list, getApplicationContext());
            recyclerView1.setLayoutManager(new LinearLayoutManager(HomeActivity.this));
            recyclerView1.setHasFixedSize(true);
            recyclerView1.setAdapter(adapter[0]);
            layout.setVisibility(View.GONE);

        }

    }









}







