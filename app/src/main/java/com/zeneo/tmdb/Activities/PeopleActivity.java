package com.zeneo.tmdb.Activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.zeneo.tmdb.ImagesDisplay;
import com.zeneo.tmdb.R;
import com.zeneo.tmdb.util.GetResFromApi;
import com.zeneo.tmdb.util.HttpHandler;

import com.zeneo.tmdb.util.InternetConnection;
import org.json.JSONException;
import org.json.JSONObject;

public class PeopleActivity extends AppCompatActivity {

    HttpHandler sh = new HttpHandler();
    String id;
    String url1,url2,url3,url4;
    ImageView pr,pr1;
    TextView nm,beo,kf,gr,bd,bp,pop,posterstxt,readmore;
    RecyclerView castmList,casttList;
    Toolbar toolbar;
    LinearLayout layout1,layout2,layout3;
    int readMoreLines;
    private AdView mAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_people);

        mAdView = findViewById(R.id.adViewPeople);
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
        url1 = "https://api.themoviedb.org/3/person/"+id+"?api_key=5d173b53167711178472dc9d98603e31&language=en-US";
        url2 = "https://api.themoviedb.org/3/person/"+id+"/movie_credits?api_key=5d173b53167711178472dc9d98603e31&language=en-US";
        url3 = "https://api.themoviedb.org/3/person/"+id+"/tv_credits?api_key=5d173b53167711178472dc9d98603e31&language=en-US";
        url4 = "https://api.themoviedb.org/3/person/"+id+"/images?api_key=5d173b53167711178472dc9d98603e31";

        linkViews();
        if(new InternetConnection().chechInternet(getApplicationContext())){
            getDetails("");
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
                        getDetails("");
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

        readmore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tbeo = beo.getText().toString();
                ViewGroup viewGroup =  ((ViewGroup)beo.getParent());
                viewGroup.removeAllViews();
                TextView textView = new TextView(getApplicationContext());
                textView.setText(tbeo);
                textView.setTextColor(getResources().getColor(android.R.color.white));
                viewGroup.addView(textView);
            }
        });
        pr1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(PeopleActivity.this, ImagesDisplay.class);
                i.putExtra("url",url4);
                i.putExtra("type","profile");
                startActivity(i);
            }
        });

    }

    public void linkViews(){
        pr = (ImageView)findViewById(R.id.people_poster);
        pr1 = findViewById(R.id.pr1);
        nm = (TextView)findViewById(R.id.people_name);
        beo = (TextView)findViewById(R.id.people_beo);
        kf = (TextView)findViewById(R.id.people_kf);
        gr = (TextView)findViewById(R.id.people_gr);
        bd = (TextView)findViewById(R.id.people_birthd);
        bp = (TextView)findViewById(R.id.people_birthp);
        pop = (TextView)findViewById(R.id.people_pop);
        readmore = findViewById(R.id.readmore);
        posterstxt = findViewById(R.id.posterstxt);
        castmList = (RecyclerView) findViewById(R.id.castm_list);
        casttList = (RecyclerView) findViewById(R.id.castt_list);
        layout1 = findViewById(R.id.layout1);
        layout2 = findViewById(R.id.layout2);
        layout3 = findViewById(R.id.layout3);
    }


    public void getDetails(String url ){

        //new GetResFromApi.getPersonalInfo(pr,nm,beo,kf,gr,bd,bp,pop,getApplicationContext(),url).execute();
        GetResFromApi res1 = new GetResFromApi(castmList,getApplicationContext(),url2,"castm","hori",false);
        res1.getDetails();
        GetResFromApi res2 = new GetResFromApi(casttList,getApplicationContext(),url3,"castt","hori",false);
        res2.getDetails();
        //GetResFromApi res3 = new GetResFromApi(crewmList,getApplicationContext(),url2,"crewm","hori",false);
        //res3.getDetails();
        //GetResFromApi res4 = new GetResFromApi(crewtList,getApplicationContext(),url3,"crewt","hori",false);
        //res4.getDetails();
        //GetResFromApi res5 = new GetResFromApi(getApplicationContext(),imgpager,url4,getWindowManager());
        //res5.setProfile(true);
        //res5.getImagesforSlider();

        new getPersonalInfo().execute();

    }



    @SuppressLint("StaticFieldLeak")
    public class getPersonalInfo extends AsyncTask<Void,Void,Void> {

        String tnm,tbeo,tkf,tgr,tbd,tbp,tpop,tpr;
        String posters;

        HttpHandler sh = new HttpHandler();
        @Override
        protected Void doInBackground(Void... voids) {
            String jsonStr = sh.makeServiceCall(url1);
            String jsonStr1 = sh.makeServiceCall(url4);

            try {
                JSONObject ob = new JSONObject(jsonStr);
                JSONObject ob1 = new JSONObject(jsonStr1);
                tnm = ob.getString("name");
                tbeo = ob.getString("biography");
                tkf = ob.getString("known_for_department");
                if (ob.getInt("gender")==1){
                    tgr = "female";
                }else tgr = "male";
                tbd = ob.getString("birthday");
                tbp = ob.getString("place_of_birth");
                tpop = String.valueOf(ob.getString("homepage"));
                tpr = "https://image.tmdb.org/t/p/w500" + ob.getString("profile_path");


                posters = ob1.getJSONArray("profiles").length()+" Profiles Images" ;
            } catch (JSONException e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            nm.setText(tnm);
            beo.setText(tbeo);
            kf.setText(tkf);
            gr.setText(tgr);
            bd.setText(tbd);
            bp.setText(tbp);
            pop.setText(tpop);
            Glide.with(getApplicationContext()).load(tpr).apply(RequestOptions.centerCropTransform()).into(pr);
            Glide.with(getApplicationContext()).load(tpr).apply(RequestOptions.centerCropTransform()).into(pr1);
            Log.i("TAG",tpr);
            posterstxt.setText(posters);
            beo.setSingleLine(false);
            beo.setLines(6);
            beo.setEllipsize(TextUtils.TruncateAt.END);
            layout1.setVisibility(View.GONE);
            layout2.setVisibility(View.VISIBLE);
            if (tbeo.equals("")){
                ((ViewGroup)beo.getParent()).removeAllViews();
            }
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            layout1.setVisibility(View.VISIBLE);
            layout2.setVisibility(View.GONE);
        }
    }

}
