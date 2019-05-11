package com.zeneo.tmdb.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Point;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.*;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.zeneo.tmdb.Adapters.EpisodesListAdapter;
import com.zeneo.tmdb.Adapters.PopularListAdapter;
import com.zeneo.tmdb.Adapters.VideoListAdapter;
import com.zeneo.tmdb.ImagesDisplay;
import com.zeneo.tmdb.Models.Movies;
import com.zeneo.tmdb.Models.Videos;
import com.zeneo.tmdb.R;
import com.zeneo.tmdb.util.HttpHandler;
import com.zeneo.tmdb.util.InternetConnection;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class SeasonActivity extends AppCompatActivity {

    ImageView bd,pr,pr1;
    TextView tt,rl,desc,posterstxt,stt;
    String url,url3,url4,url6;
    String id;
    RecyclerView episodesRecycler;
    LinearLayout layout1,layout2,layout3;
    RecyclerView videosRecycler,castRecycler;
    List<Movies> castList = new ArrayList<>();
    List<Movies> episodeList = new ArrayList<>();
    List<Videos> videosList = new ArrayList<>();
    String img_url;
    Toolbar toolbar;
    int season=1;
    String name = "";
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_season);

        mAdView = findViewById(R.id.adViewSeason);
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

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("");


        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        id = getIntent().getStringExtra("id");
        season = (int) getIntent().getExtras().getInt("season");
        name = getIntent().getStringExtra("name");
        url = "https://api.themoviedb.org/3/tv/"+id+"/season/"+season+"?api_key=5d173b53167711178472dc9d98603e31&language=en-US";
        url3 = "https://api.themoviedb.org/3/tv/"+id+"/season/"+season+"/images?api_key=5d173b53167711178472dc9d98603e31&language=en%2Cnull";
        url4 = "https://api.themoviedb.org/3/tv/"+id+"/season/"+season+"/videos?api_key=5d173b53167711178472dc9d98603e31&language=en-US";
        url6 = "https://api.themoviedb.org/3/tv/"+id+ "/season/"+season+"/credits?api_key=5d173b53167711178472dc9d98603e31&language=en-US";


        linkViews();
        if(new InternetConnection().chechInternet(getApplicationContext())){
            getData();
            layout3.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        }else {
            final View view1 = LayoutInflater.from(getApplicationContext()).inflate(R.layout.no_internet,null);
            TextView tryAgain= view1.findViewById(R.id.tryagainbtn);
            tryAgain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (new InternetConnection().chechInternet(getApplicationContext())){
                        layout3.removeAllViews();
                        layout3.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                        getData();
                    }else {
                        layout3.removeAllViews();
                        layout3.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
                        layout3.addView(view1);
                    }
                }
            });
            view1.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            layout3.removeAllViews();
            layout3.addView(view1);
        }        setImagesSize();

        tt.setText(name);

        pr1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SeasonActivity.this, ImagesDisplay.class);
                i.putExtra("url",url3);
                i.putExtra("type","poster");
                startActivity(i);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main2,menu);
        menu.findItem(R.id.action_share).setIcon(R.drawable.ic_share_white);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {
            case R.id.action_share:
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "https://www.themoviedb.org/tv/"+id+"/season/"+season);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void linkViews(){
        bd = findViewById(R.id.tv_backdrop);
        pr = findViewById(R.id.tv_poster);
        pr1 = findViewById(R.id.pr1);
        stt = findViewById(R.id.tv_season_title);
        tt = findViewById(R.id.tv_title);
        rl = findViewById(R.id.relase_date);
        desc = findViewById(R.id.tv_desc);
        episodesRecycler = findViewById(R.id.episodes_list);
        videosRecycler = findViewById(R.id.video_list);
        posterstxt = findViewById(R.id.posterstxt);
        castRecycler = findViewById(R.id.cast_list);
        episodesRecycler = findViewById(R.id.episodes_list);
        layout1 = findViewById(R.id.layout1);
        layout2 = findViewById(R.id.layout2);
        layout3 = findViewById(R.id.layout3);
    }



    public void getData(){
        new GetTVInfo().execute();

        //new GetResFromApi(TVShowsActivity.this,imgPager,url3,getWindowManager()).getImagesforSlider();
        //new GetResFromApi(TVShowsActivity.this,vidPager,url4,getWindowManager()).getVideosForSliders();
    }

    public void setImagesSize(){
        Display display = getWindowManager(). getDefaultDisplay();
        Point size = new Point();
        display. getSize(size);
        int width = size.x;
        bd.setMaxHeight((int) (width*0.7));
    }




    @SuppressLint("StaticFieldLeak")
    public class GetTVInfo extends AsyncTask<Void,Void,Void> {
        HttpHandler sh = new HttpHandler();
        String bd,tt,rl,desc;
        String posters;

        @Override
        protected Void doInBackground(Void... voids) {
            String jsonStr = sh.makeServiceCall(url);
            String jsonStr1 = sh.makeServiceCall(url4);
            String jsonStr2 = sh.makeServiceCall(url3);
            String jsonStr3 = sh.makeServiceCall(url6);
            try {
                JSONObject ob = new JSONObject(jsonStr);
                JSONObject ob1 = new JSONObject(jsonStr1);
                JSONObject ob2 = new JSONObject(jsonStr2);
                JSONObject ob3 = new JSONObject(jsonStr3);
                bd = "https://image.tmdb.org/t/p/w500"+ ob.getJSONArray("episodes").getJSONObject(0).getString("still_path");
                img_url = "https://image.tmdb.org/t/p/w500"+ob.getString("poster_path");
                tt = ob.getString("name");
                rl = ob.getString("air_date");
                desc = ob.getString("overview");

                JSONArray results1 = ob1.getJSONArray("results");

                for (int i = 0 ; i < results1.length() ; i++ ){
                    String img_url = "https://img.youtube.com/vi/"+results1.getJSONObject(i).getString("key")+"/hqdefault.jpg";
                    String title = results1.getJSONObject(i).getString("name");
                    String vid_url = "https://www.youtube.com/watch?v="+results1.getJSONObject(i).getString("key");
                    videosList.add(new Videos(results1.getJSONObject(i).getString("key"),img_url,title,vid_url));
                }

                posters = String.valueOf(ob2.getJSONArray("posters").length()) + " Poster";

                JSONArray results3 = ob3.getJSONArray("cast");
                for (int i = 0 ; i < results3.length() ; i++){
                    String titles = results3.getJSONObject(i).getString("name");
                    String imgurl ="https://image.tmdb.org/t/p/w500"+ results3.getJSONObject(i).getString("profile_path");
                    int id = results3.getJSONObject(i).getInt("id");
                    castList.add(new Movies(titles,imgurl,id,"castp"));
                }

                JSONArray result4 = ob.getJSONArray("episodes");
                for (int i = 0 ; i < result4.length() ; i++){
                    String titles = result4.getJSONObject(i).getString("name");
                    String imgurl = "https://image.tmdb.org/t/p/w185_and_h278_bestv2/"+ result4.getJSONObject(i).getString("still_path");
                    int id = result4.getJSONObject(i).getInt("episode_number");
                    String rating = String.valueOf(result4.getJSONObject(i).getDouble("vote_average"));
                    desc = result4.getJSONObject(i).getString("overview");
                    episodeList.add(new Movies(titles,imgurl,id,rating,desc,""));
                }


            } catch (JSONException e) {
                e.printStackTrace();
            }

            Log.i("TAG",desc);

            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            layout1.setVisibility(View.VISIBLE);
            layout2.setVisibility(View.GONE);
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            try {
                Glide.with(getApplicationContext()).load(bd).into(SeasonActivity.this.bd);
                Glide.with(getApplicationContext()).load(img_url).into(SeasonActivity.this.pr);
                Glide.with(getApplicationContext()).load(img_url).into(SeasonActivity.this.pr1);
            }catch (IllegalArgumentException e){

            }
            layout1.setVisibility(View.GONE);
            layout2.setVisibility(View.VISIBLE);

            posterstxt.setText(posters);
            SeasonActivity.this.stt.setText(tt);
            SeasonActivity.this.rl.setText(rl);
            SeasonActivity.this.desc.setText(desc);

            VideoListAdapter adapter = new VideoListAdapter(videosList,SeasonActivity.this);
            videosRecycler.setLayoutManager(new LinearLayoutManager(SeasonActivity.this, LinearLayout.HORIZONTAL,false));
            videosRecycler.setAdapter(adapter);

            PopularListAdapter adapter1 = new PopularListAdapter(castList,SeasonActivity.this);
            castRecycler.setLayoutManager(new LinearLayoutManager(SeasonActivity.this,LinearLayout.HORIZONTAL,false));
            castRecycler.setAdapter(adapter1);

            EpisodesListAdapter adapter2 = new EpisodesListAdapter(episodeList,SeasonActivity.this);
            episodesRecycler.setLayoutManager(new LinearLayoutManager(SeasonActivity.this));
            episodesRecycler.setAdapter(adapter2);


            if(videosList.size() == 0){
                ((ViewGroup)videosRecycler.getParent()).removeAllViews();
            }
            if (desc.equals("")){
                SeasonActivity.this.desc.setVisibility(View.GONE);
            }
            if (castList.size()==0){
                ((ViewGroup)castRecycler.getParent()).removeAllViews();
            }
            if (episodeList.size()==0){
                ((ViewGroup)episodesRecycler.getParent()).removeAllViews();
            }


        }

    }
    public void intentToEpisodeActivity(int ep,String title){
        Intent intent = new Intent(SeasonActivity.this, EpisodeActivity.class);
        intent.putExtra("ep", ep);
        intent.putExtra("epname", title);
        intent.putExtra("name", name);
        intent.putExtra("season", stt.getText().toString());
        intent.putExtra("seasonnum", season);
        intent.putExtra("id", id);
        startActivity(intent);
    }
}
