package com.zeneo.tmdb.Activities;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Point;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.*;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.zeneo.tmdb.Adapters.PopularListAdapter;
import com.zeneo.tmdb.Adapters.VideoListAdapter;
import com.zeneo.tmdb.DATABASE.DataContract;
import com.zeneo.tmdb.DATABASE.DataHelper;
import com.zeneo.tmdb.ImagesDisplay;
import com.zeneo.tmdb.Models.Movies;
import com.zeneo.tmdb.Models.Videos;
import com.zeneo.tmdb.R;
import com.zeneo.tmdb.util.*;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class TVShowsActivity extends AppCompatActivity {

    ImageView bd,pr,bd1,pr1;
    TextView tt,st,rl,olg,rt,pp,nos,noe,gr,desc,backdropstxt,posterstxt,rating;
    String url,url1,url2,url3,url4,url5,url6;
    String id;
    RatingBar ratingBar;
    RecyclerView recList,simList,seasList;
    RecyclerView videosRecycler,castRecycler,crewRecycler;
    ImageView favor,watchlist;
    SQLiteDatabase db,db2;
    DataHelper.FavoriteHelper sqlfavorite;
    DataHelper.WatchListHelper sqlwatchlist;
    List<Movies> castList = new ArrayList<>();
    List<Videos> videosList = new ArrayList<>();
    List<Movies> crewList = new ArrayList<>();
    String title,img_url;
    Toolbar toolbar;
    LinearLayout layout1,layout2,layout3;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tvshows);

        mAdView = findViewById(R.id.adViewTv);
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

        toolbar = (Toolbar) findViewById(R.id.toolbar);
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
        url = "https://api.themoviedb.org/3/tv/"+id+"?api_key=5d173b53167711178472dc9d98603e31&language=en-US";
        url1 = "https://api.themoviedb.org/3/tv/"+id+"/recommendations?api_key=5d173b53167711178472dc9d98603e31&language=en-US&page=1";
        url2 = "https://api.themoviedb.org/3/tv/"+id+"/similar?api_key=5d173b53167711178472dc9d98603e31&language=en-US&page=1";
        url3 = "https://api.themoviedb.org/3/tv/"+id+"/images?api_key=5d173b53167711178472dc9d98603e31&language=en%2Cnull";
        url4 = "https://api.themoviedb.org/3/tv/"+id+"/videos?api_key=5d173b53167711178472dc9d98603e31&language=en-US";
        url5 = "https://api.themoviedb.org/3/tv/"+id+"";
        url6 = "https://api.themoviedb.org/3/tv/"+id+ "/credits?api_key=5d173b53167711178472dc9d98603e31&language=en-US";


        linkViews();
        setImagesSize();

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

        sqlfavorite = new DataHelper.FavoriteHelper(this);
        db = sqlfavorite.getWritableDatabase();
        sqlwatchlist = new DataHelper.WatchListHelper(this);
        db2 = sqlwatchlist.getWritableDatabase();

        boolean isExist = sqlfavorite.CheckIsDataAlreadyInDBorNot(DataContract.FavoriteEntry.TABLE_NAME,id);
        if (isExist){
            favor.setImageResource(R.drawable.favorite);
        }else {
            favor.setImageResource(R.drawable.favorite_desactive);
        }

        boolean count3 = sqlwatchlist.CheckIsDataAlreadyInDBorNot(DataContract.WatchListEntry.TABLE_NAME,id);
        if (count3){
            watchlist.setImageResource(R.drawable.ic_watchlist_added);
        }else {
            watchlist.setImageResource(R.drawable.ic_watchlist_add);
        }

        pr1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(TVShowsActivity.this, ImagesDisplay.class);
                i.putExtra("url",url3);
                i.putExtra("type","poster");
                startActivity(i);
            }
        });
        bd1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(TVShowsActivity.this, ImagesDisplay.class);
                i.putExtra("url",url3);
                i.putExtra("type","backdrop");
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
                sendIntent.putExtra(Intent.EXTRA_TEXT, "https://www.themoviedb.org/tv/"+id);
                sendIntent.setType("text/plain");
                startActivity(sendIntent);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();



        favor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isExist = sqlfavorite.CheckIsDataAlreadyInDBorNot(DataContract.FavoriteEntry.TABLE_NAME,id);
                title = tt.getText().toString();
                if (isExist){
                    sqlfavorite.delete(DataContract.FavoriteEntry.TABLE_NAME,id,"TV");
                    favor.setImageResource(R.drawable.favorite_desactive);
                }else {
                    sqlfavorite.insert(DataContract.FavoriteEntry.TABLE_NAME,title,img_url,id,"TV");
                    favor.setImageResource(R.drawable.favorite);
                }
            }
        });
        watchlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean count3 = sqlwatchlist.CheckIsDataAlreadyInDBorNot(DataContract.WatchListEntry.TABLE_NAME,id);
                title = tt.getText().toString();
                if (count3){
                    sqlwatchlist.delete(DataContract.WatchListEntry.TABLE_NAME,id,"TV");
                    watchlist.setImageResource(R.drawable.ic_watchlist_add);
                }else {
                    sqlwatchlist.insert(DataContract.WatchListEntry.TABLE_NAME,title,img_url,id,"TV");
                    watchlist.setImageResource(R.drawable.ic_watchlist_added);
                }
            }
        });

    }

    public void linkViews(){
        bd = (ImageView)findViewById(R.id.tv_backdrop);
        pr = (ImageView)findViewById(R.id.tv_poster);
        pr1 = (ImageView)findViewById(R.id.pr1);
        bd1 = (ImageView)findViewById(R.id.bd1);
        tt = (TextView)findViewById(R.id.tv_title);
        st = (TextView)findViewById(R.id.tv_status);
        rl = (TextView)findViewById(R.id.tv_release);
        olg = (TextView)findViewById(R.id.tv_olg);
        rt = (TextView)findViewById(R.id.tv_runtime);
        pp = (TextView)findViewById(R.id.tv_pop);
        nos = (TextView)findViewById(R.id.tv_ns);
        noe = (TextView)findViewById(R.id.tv_ne);
        gr = (TextView)findViewById(R.id.tv_genre);
        desc = (TextView)findViewById(R.id.tv_desc);
        recList = (RecyclerView)findViewById(R.id.tv_rec_list);
        simList = (RecyclerView)findViewById(R.id.tv_sim_list);
        seasList = (RecyclerView)findViewById(R.id.season_list);
        favor = (ImageView)findViewById(R.id.tv_favor);
        rating = (TextView)findViewById(R.id.rattxt);
        watchlist = (ImageView)findViewById(R.id.watch_list_tv);
        ratingBar = findViewById(R.id.ratingbar);
        videosRecycler = findViewById(R.id.video_list);
        backdropstxt = findViewById(R.id.backdropstxt);
        posterstxt = findViewById(R.id.posterstxt);
        castRecycler = findViewById(R.id.cast_list);
        crewRecycler = findViewById(R.id.crew_list);
        layout1 = findViewById(R.id.layout1);
        layout2 = findViewById(R.id.layout2);
        layout3 = findViewById(R.id.layout3);

    }



    public void getData(){
        new GetTVInfo().execute();

        new GetResFromApi(recList,TVShowsActivity.this,url1,"TV","hori",false).getDetails();
        new GetResFromApi(simList,TVShowsActivity.this,url2,"TV","hori",false).getDetails();
        //new GetResFromApi(TVShowsActivity.this,imgPager,url3,getWindowManager()).getImagesforSlider();
        //new GetResFromApi(TVShowsActivity.this,vidPager,url4,getWindowManager()).getVideosForSliders();
        new GetResFromApi(seasList,TVShowsActivity.this,url,"seasons","hori",false).getDetails();

    }

    public void setImagesSize(){
        Display display = getWindowManager(). getDefaultDisplay();
        Point size = new Point();
        display. getSize(size);
        int width = size.x;
        bd.setMaxHeight((int) (width*0.7));
    }

    public void getEpisodes (int i){
        Intent intent = new Intent(TVShowsActivity.this,SeasonActivity.class);
        intent.putExtra("id",id);
        intent.putExtra("name",tt.getText().toString());
        intent.putExtra("season",i);
        startActivity(intent);
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        db.close();
        db2.close();
    }

    public class GetTVInfo extends AsyncTask<Void,Void,Void> {
        HttpHandler sh = new HttpHandler();
        String bd,pr,tt,st,rl,olg,rt,pp,nos,noe,gr="",desc="";
        float rating;
        String backdrops;
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
                bd = "https://image.tmdb.org/t/p/w500"+ ob.getString("backdrop_path");
                img_url = "https://image.tmdb.org/t/p/w500"+ob.getString("poster_path");
                tt = ob.getString("name");
                st = ob.getString("status");
                rl = ob.getString("first_air_date");

                if(!rl.equals("null")){rl = fixDate(rl);}
                else {rl="NO DATA FOUND";}

                olg = ob.getString("original_language");
                rt = new NumToTime().converter(ob.getJSONArray("episode_run_time").getInt(0));
                pp = ob.getString("last_air_date");

                if(!pp.equals("null")){pp = fixDate(pp);}
                else {pp="NO DATA FOUND";}

                noe = String.valueOf(ob.getInt("number_of_episodes"));
                nos = String.valueOf(ob.getInt("number_of_seasons"));
                for (int i = 0 ; i<ob.getJSONArray("genres").length();i++){
                    JSONArray grarr = ob.getJSONArray("genres");
                    if (i==ob.getJSONArray("genres").length()-1){
                        gr += grarr.getJSONObject(i).getString("name");
                    }
                    else {
                        gr += grarr.getJSONObject(i).getString("name")+" • ";
                    }
                }
                rating =(float)ob.getDouble("vote_average");

                desc = ob.getString("overview");

                JSONArray results1 = ob1.getJSONArray("results");

                for (int i = 0 ; i < results1.length() ; i++ ){
                    String img_url = "https://img.youtube.com/vi/"+results1.getJSONObject(i).getString("key")+"/hqdefault.jpg";
                    String title = results1.getJSONObject(i).getString("name");
                    String vid_url = "https://www.youtube.com/watch?v="+results1.getJSONObject(i).getString("key");
                    videosList.add(new Videos(results1.getJSONObject(i).getString("key"),img_url,title,vid_url));
                }

                backdrops = ob2.getJSONArray("backdrops").length() + " Backdrops";
                posters = String.valueOf(ob2.getJSONArray("posters").length()) + " Posters";

                JSONArray results3 = ob3.getJSONArray("cast");
                for (int i = 0 ; i < results3.length() ; i++){
                    String titles = results3.getJSONObject(i).getString("name");
                    String imgurl ="https://image.tmdb.org/t/p/w500"+ results3.getJSONObject(i).getString("profile_path");
                    int id = results3.getJSONObject(i).getInt("id");
                    castList.add(new Movies(titles,imgurl,id,"castp"));
                }
                JSONArray results4 = ob3.getJSONArray("crew");
                for (int i = 0 ; i < results3.length() ; i++){
                    String titles = results4.getJSONObject(i).getString("name");
                    String imgurl ="https://image.tmdb.org/t/p/w500"+ results4.getJSONObject(i).getString("profile_path");
                    int id = results4.getJSONObject(i).getInt("id");
                    crewList.add(new Movies(titles,imgurl,id,"crewp"));
                }



            } catch (JSONException e) {
                e.printStackTrace();
            }

            //Log.i("TAG",desc);

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
            layout1.setVisibility(View.GONE);
            layout2.setVisibility(View.VISIBLE);
            try {
                Glide.with(getApplicationContext()).load(bd).into(TVShowsActivity.this.bd);
                Glide.with(getApplicationContext()).load(img_url).into(TVShowsActivity.this.pr);
                Glide.with(getApplicationContext())
                        .load(bd).apply(RequestOptions.centerCropTransform().override(1200,800))
                        .into(TVShowsActivity.this.bd1);
                Glide.with(getApplicationContext()).load(img_url).into(TVShowsActivity.this.pr1);
            }catch (IllegalArgumentException e){

            }
            posterstxt.setText(posters);
            backdropstxt.setText(backdrops);
            TVShowsActivity.this.tt.setText(TVShowsActivity.this.tt.getText()+tt);
            TVShowsActivity.this.st.setText(TVShowsActivity.this.st.getText()+st);
            TVShowsActivity.this.rl.setText(TVShowsActivity.this.rl.getText()+rl);
            TVShowsActivity.this.olg.setText(TVShowsActivity.this.olg.getText()+olg);
            TVShowsActivity.this.rt.setText(TVShowsActivity.this.rt.getText()+rt);
            TVShowsActivity.this.pp.setText(TVShowsActivity.this.pp.getText()+pp);
            TVShowsActivity.this.nos.setText(TVShowsActivity.this.nos.getText()+nos);
            TVShowsActivity.this.noe.setText(TVShowsActivity.this.noe.getText()+noe);
            TVShowsActivity.this.gr.setText(TVShowsActivity.this.gr.getText()+gr);
            TVShowsActivity.this.desc.setText(TVShowsActivity.this.desc.getText()+desc);
            TVShowsActivity.this.rating.setText(rating+"");
            ratingBar.setRating(rating/2);

            VideoListAdapter adapter = new VideoListAdapter(videosList,TVShowsActivity.this);
            videosRecycler.setLayoutManager(new LinearLayoutManager(TVShowsActivity.this,LinearLayout.HORIZONTAL,false));
            videosRecycler.setAdapter(adapter);

            PopularListAdapter adapter1 = new PopularListAdapter(castList,TVShowsActivity.this);
            castRecycler.setLayoutManager(new LinearLayoutManager(TVShowsActivity.this,LinearLayout.HORIZONTAL,false));
            castRecycler.setAdapter(adapter1);

            PopularListAdapter adapter2 = new PopularListAdapter(crewList,TVShowsActivity.this);
            crewRecycler.setLayoutManager(new LinearLayoutManager(TVShowsActivity.this,LinearLayout.HORIZONTAL,false));
            crewRecycler.setAdapter(adapter2);

            if(videosList.size() == 0){
                ((ViewGroup)videosRecycler.getParent()).removeAllViews();
            }
            if (desc.equals("")){
                TVShowsActivity.this.desc.setVisibility(View.GONE);
            }
            if (castList.size()==0){
                ((ViewGroup)castRecycler.getParent()).removeAllViews();
            }
            if (crewList.size()==0){
                ((ViewGroup)crewRecycler.getParent()).removeAllViews();
            }


        }

    }

    public String fixDate(String date){
        String year = date.substring(0,4);
        String month = date.substring(5,7);
        String month_ = "";
        String day = date.substring(8,10);
        switch(month){
            case "01":
                month_="January";
                break;
            case "02":
                month_="February";
                break;
            case "03":
                month_="March";
                break;
            case "04":
                month_="April";
                break;
            case "05":
                month_="May";
                break;
            case "06":
                month_="June";
                break;
            case "07":
                month_="July";
                break;
            case "08":
                month_="August";
                break;
            case "09":
                month_="September";
                break;
            case "10":
                month_="October";
                break;
            case "11":
                month_="November";
                break;
            case "12":
                month_="December";
                break;
        }
        return day+" "+month_+", "+year;
    }

}
