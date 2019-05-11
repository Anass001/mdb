package com.zeneo.tmdb.Activities;

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
import android.widget.RatingBar;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
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

public class EpisodeActivity extends AppCompatActivity {

    ImageView bd,pr,bd1;
    TextView tt,rl,desc,posterstxt,eptt,seasontt,rating,epnum;
    RatingBar ratingBar;
                String url,url3,url4,url6;
    String id;
    RecyclerView videosRecycler,castRecycler;
    List<Movies> castList = new ArrayList<>();
    List<Videos> videosList = new ArrayList<>();
    String img_url;
    Toolbar toolbar;
    String name = "", ep = "" ,season = "" ,epname = "" ,seasonnum = "";
    LinearLayout layout1,layout2,layout3;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_episode);

        mAdView = findViewById(R.id.adViewEpisode);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setTitle("");

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

        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        id = getIntent().getStringExtra("id");
        season = getIntent().getStringExtra("season");
        name = getIntent().getStringExtra("name");
        ep = String.valueOf(getIntent().getExtras().getInt("ep"));
        epname = getIntent().getExtras().getString("epname");
        seasonnum = String.valueOf(getIntent().getExtras().getInt("seasonnum"));

        Log.i("Tagme", id);
        Log.i("Tagme", season);
        Log.i("Tagme", name);
        Log.i("Tagme", ep);
        Log.i("Tagme", epname);


        url = "https://api.themoviedb.org/3/tv/"+id+"/season/"+seasonnum+"/episode/"+ep+"?api_key=5d173b53167711178472dc9d98603e31&language=en-US";
        url3 = "https://api.themoviedb.org/3/tv/"+id+"/season/"+seasonnum+"/episode/"+ep+"/images?api_key=5d173b53167711178472dc9d98603e31&language=en%2Cnull";
        url4 = "https://api.themoviedb.org/3/tv/"+id+"/season/"+seasonnum+"/episode/"+ep+"/videos?api_key=5d173b53167711178472dc9d98603e31&language=en-US";
        url6 = "https://api.themoviedb.org/3/tv/"+id+ "/season/"+seasonnum+"/episode/"+ep+"/credits?api_key=5d173b53167711178472dc9d98603e31&language=en-US";

        linkViews();
        setImagesSize();
        tt = findViewById(R.id.tv_title);
        tt.setText(name);
        seasontt = (TextView) findViewById(R.id.seasontt);
        seasontt.setText(season);
        eptt.setText(epname);
        epnum.setText(ep);

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
        }

        bd1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(EpisodeActivity.this, ImagesDisplay.class);
                i.putExtra("url",url3);
                i.putExtra("type","still");
                startActivity(i);
            }
        });

    }


    public void linkViews(){
        bd = findViewById(R.id.tv_backdrop);
        bd1 = findViewById(R.id.bd1);
        eptt = findViewById(R.id.eptt);
        epnum = findViewById(R.id.epnum);
        rl = findViewById(R.id.relase_date);
        rating = findViewById(R.id.rattxt);
        desc = findViewById(R.id.tv_desc);
        videosRecycler = findViewById(R.id.video_list);
        posterstxt = findViewById(R.id.backdropstxt);
        castRecycler = findViewById(R.id.cast_list);
        ratingBar = findViewById(R.id.ratingbar);
        layout1 = findViewById(R.id.layout1);
        layout2 = findViewById(R.id.layout2);
        layout3 = findViewById(R.id.layout3);
    }



    public void getData(){
        new GetTVInfo().execute();
    }

    public void setImagesSize(){
        Display display = getWindowManager(). getDefaultDisplay();
        Point size = new Point();
        display. getSize(size);
        int width = size.x;
        bd.setMaxHeight((int) (width*0.7));
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
                sendIntent.putExtra(Intent.EXTRA_TEXT, "https://www.themoviedb.org/tv/"+id+"/season/"+seasonnum+"/episode/"+epnum);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public class GetTVInfo extends AsyncTask<Void,Void,Void> {
        HttpHandler sh = new HttpHandler();
        String bd,tt,rl,desc;
        String backdrops;
        float rating;

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
                bd = "https://image.tmdb.org/t/p/w500"+ ob.getString("still_path");
                rl = ob.getString("air_date");
                desc = ob.getString("overview");
                rating = (float) ob.getDouble("vote_average");

                JSONArray results1 = ob1.getJSONArray("results");

                for (int i = 0 ; i < results1.length() ; i++ ){
                    String img_url = "https://img.youtube.com/vi/"+results1.getJSONObject(i).getString("key")+"/hqdefault.jpg";
                    String title = results1.getJSONObject(i).getString("name");
                    String vid_url = "https://www.youtube.com/watch?v="+results1.getJSONObject(i).getString("key");
                    videosList.add(new Videos(results1.getJSONObject(i).getString("key"),img_url,title,vid_url));
                }

                backdrops = String.valueOf(ob2.getJSONArray("stills").length()) + " Stills";

                JSONArray results3 = ob3.getJSONArray("cast");
                for (int i = 0 ; i < results3.length() ; i++){
                    String titles = results3.getJSONObject(i).getString("name");
                    String imgurl ="https://image.tmdb.org/t/p/w500"+ results3.getJSONObject(i).getString("profile_path");
                    int id = results3.getJSONObject(i).getInt("id");
                    castList.add(new Movies(titles,imgurl,id,"castp"));
                }



            } catch (JSONException e) {
                e.printStackTrace();
            } catch (NullPointerException e){
                e.printStackTrace();
            } catch (RuntimeException e1){
                e1.printStackTrace();
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
                Glide.with(getApplicationContext()).load(bd).into(EpisodeActivity.this.bd);
                Glide.with(getApplicationContext()).load(bd).into(EpisodeActivity.this.bd1);
            }catch (IllegalArgumentException e){

            }

            layout1.setVisibility(View.GONE);
            layout2.setVisibility(View.VISIBLE);

            EpisodeActivity.this.rl.setText(EpisodeActivity.this.rl.getText()+rl);
            EpisodeActivity.this.desc.setText(EpisodeActivity.this.desc.getText()+desc);
            EpisodeActivity.this.rating.setText(rating+"");
            ratingBar.setRating(rating/2);
            posterstxt.setText(backdrops);

            VideoListAdapter adapter = new VideoListAdapter(videosList,EpisodeActivity.this);
            videosRecycler.setLayoutManager(new LinearLayoutManager(EpisodeActivity.this,LinearLayout.HORIZONTAL,false));
            videosRecycler.setAdapter(adapter);

            PopularListAdapter adapter1 = new PopularListAdapter(castList,EpisodeActivity.this);
            castRecycler.setLayoutManager(new LinearLayoutManager(EpisodeActivity.this,LinearLayout.HORIZONTAL,false));
            castRecycler.setAdapter(adapter1);



            if(desc.equals("")){
                EpisodeActivity.this.desc.setVisibility(View.GONE);
            }

            if (castList.size()==0){
                ((ViewGroup)castRecycler.getParent()).removeAllViews();
            }
            if (videosList.size()==0){
                ((ViewGroup)videosRecycler.getParent()).removeAllViews();
            }



        }

    }



}
