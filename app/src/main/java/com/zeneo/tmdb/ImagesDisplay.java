package com.zeneo.tmdb;

import android.annotation.SuppressLint;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import com.zeneo.tmdb.UI.ImageSliderAdapter;
import com.zeneo.tmdb.util.HttpHandler;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class ImagesDisplay extends AppCompatActivity{

    ViewPager viewPager;
    String url;
    String type;
    List<String> list;
    List<String> imageKeysList = new ArrayList<>();
    int page;

    @SuppressLint("StaticFieldLeak")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_images_display);

        viewPager = findViewById(R.id.viewpager);
        page = 0;
        url = getIntent().getStringExtra("url");
        list = new ArrayList<>();
        type = getIntent().getStringExtra("type");

        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                page = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        new AsyncTask<Void, Void, Void>() {
            HttpHandler sh = new HttpHandler();
            @SuppressLint("WrongThread")
            @Override
            protected Void doInBackground(Void... voids) {
                String jsonStr = sh.makeServiceCall(url);
                try {
                    switch (type) {
                        case "backdrop": {
                            JSONObject ob = new JSONObject(jsonStr);
                            JSONArray bd = ob.getJSONArray("backdrops");
                            for (int i = 0; i < bd.length(); i++) {
                                list.add("https://image.tmdb.org/t/p/original" + bd.getJSONObject(i).getString("file_path"));
                                imageKeysList.add(bd.getJSONObject(i).getString("file_path"));
                            }
                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                            break;
                        }
                        case "profile": {
                            JSONObject ob = new JSONObject(jsonStr);
                            JSONArray bd = ob.getJSONArray("profiles");
                            for (int i = 0; i < bd.length(); i++) {
                                list.add("https://image.tmdb.org/t/p/original" + bd.getJSONObject(i).getString("file_path"));
                                imageKeysList.add(bd.getJSONObject(i).getString("file_path"));
                            }
                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

                            break;
                        }
                        case "poster": {
                            JSONObject ob = new JSONObject(jsonStr);
                            JSONArray bd = ob.getJSONArray("posters");
                            for (int i = 0; i < bd.length(); i++) {
                                list.add("https://image.tmdb.org/t/p/original" + bd.getJSONObject(i).getString("file_path"));
                                imageKeysList.add(bd.getJSONObject(i).getString("file_path"));
                            }
                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                            break;
                        }
                        case "still": {
                            JSONObject ob = new JSONObject(jsonStr);
                            JSONArray bd = ob.getJSONArray("stislls");
                            for (int i = 0; i < bd.length(); i++) {
                                list.add("https://image.tmdb.org/t/p/original" + bd.getJSONObject(i).getString("file_path"));
                                imageKeysList.add(bd.getJSONObject(i).getString("file_path"));
                            }
                            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
                            break;
                        }
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
                return null;
            }
            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                viewPager.setAdapter(new ImageSliderAdapter(list,getApplicationContext()));
            }
        }.execute();
    }
}
