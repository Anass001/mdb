package com.zeneo.tmdb.util;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.zeneo.tmdb.Adapters.SearchListAdapter;
import com.zeneo.tmdb.Models.Search;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class SearchingOP {


    private RecyclerView recyclerView;
    private List<Search> list = new ArrayList<>();
    private String url;
    private Context context;
    private String media_type;
    private TextView textView1,textView2;
    String url1 = "https://api.themoviedb.org/3/search/movie?api_key=5d173b53167711178472dc9d98603e31&language=en-US&page=1&include_adult=false&query=";
    String url2 = "https://api.themoviedb.org/3/search/person?api_key=5d173b53167711178472dc9d98603e31&language=en-US&page=1&include_adult=false&query=";
    String url3 = "https://api.themoviedb.org/3/search/tv?api_key=5d173b53167711178472dc9d98603e31&language=en-US&page=1&query=";

    public SearchingOP(RecyclerView recyclerView, String url, Context context, String media_type, TextView textView, TextView textView2) {
        this.recyclerView = recyclerView;
        this.url = url;
        this.context = context;
        this.media_type = media_type;
        this.textView1 = textView;
        this.textView2 = textView2;
    }


    public void setUrl(String url) {
        this.url = url;
    }

    public void getDetails(){
        new Searcher().execute();
    }

    public class Searcher extends AsyncTask<Void,Void,Void> {

        String title;
        String imgurl;
        String type ;
        int id;
        HttpHandler sh = new HttpHandler();

        @Override
        protected Void doInBackground(Void... voids) {
            list.clear();
            if (!url.equals(url1)){
                String jsonStr = sh.makeServiceCall(url);

                if (jsonStr != null){

                    try {
                        JSONObject mainObject = new JSONObject(jsonStr);
                        JSONArray results = mainObject.getJSONArray("results");

                        for (int i = 0;i < results.length() ; i++){
                            title = results.getJSONObject(i).getString("title");
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
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.i("TAG title",title);
            final SearchListAdapter[] adapter = new SearchListAdapter[1];
            final SearchListAdapter[] adapter1 = new SearchListAdapter[1];
            if (list.size() > 0){
                textView1.setVisibility(View.GONE);
            }
            if (list.size()<3){
                textView2.setVisibility(View.GONE);
                adapter[0] = new SearchListAdapter(list,context);
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
                recyclerView.setHasFixedSize(true);
                recyclerView.setAdapter(adapter[0]);
            }else {
                try {
                    List<Search> list1 = new ArrayList<>();
                    textView2.setVisibility(View.VISIBLE);
                    list1.add(list.get(0));
                    list1.add(list.get(1));
                    list1.add(list.get(2));
                    adapter[0] = new SearchListAdapter(list1,context);
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setAdapter(adapter[0]);
                }catch (IndexOutOfBoundsException e){
                    e.printStackTrace();
                }

            }
            textView2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    adapter1[0] = new SearchListAdapter(list,context);
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    recyclerView.setHasFixedSize(true);
                    recyclerView.setAdapter(adapter1[0]);
                    textView2.setVisibility(View.GONE);
                }
            });



        }
    }

}
