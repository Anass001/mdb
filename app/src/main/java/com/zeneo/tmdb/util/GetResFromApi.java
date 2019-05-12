package com.zeneo.tmdb.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.zeneo.tmdb.Adapters.PopularListAdapter;
import com.zeneo.tmdb.Adapters.RateListAdapter;
import com.zeneo.tmdb.Adapters.RecListAdapter;
import com.zeneo.tmdb.Models.Movies;
import com.zeneo.tmdb.Models.Videos;
import com.zeneo.tmdb.UI.ImageSliderAdapter;
import com.zeneo.tmdb.UI.VideoSliderAdapter;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;
import android.R;

public class GetResFromApi {

    private List<Movies> list = new ArrayList<>();
    private RecyclerView recyclerView;
    private Context context;
    private String url;
    private String media_type;
    private String listType;
    List<String> img_url = new ArrayList<>();
    private LinearLayout layout,layout2;
    private boolean isRefreshed = false, isOnHome = true;
    private String type;
    String image_url;

    public String getImage_url() {
        return image_url;
    }

    public void setRefreshed(boolean refreshed) {
        isRefreshed = refreshed;
    }



    public GetResFromApi(RecyclerView recyclerView, Context context, String url, String media_type, String listType, LinearLayout layout, LinearLayout layout2, String type) {
        this.recyclerView = recyclerView;
        this.context = context;
        this.url = url;
        this.media_type = media_type;
        this.listType = listType;
        this.layout = layout;
        this.layout2 = layout2;
        this.type = type;
    }

    public GetResFromApi(RecyclerView recyclerView, Context context, String url, String media_type, String listType, boolean isOnHome) {
        this.recyclerView = recyclerView;
        this.context = context;
        this.url = url;
        this.media_type = media_type;
        this.listType = listType;
        this.isOnHome = isOnHome;
    }

    public void getDetails (){
        new getMoviewsFromApi().execute();
    }

    public void clearData(){
        list.clear();
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setMedia_type(String media_type) {
        this.media_type = media_type;
    }

    public class getMoviewsFromApi extends AsyncTask< Void , Void , Void > {
        HttpHandler sh = new HttpHandler();
        int postitionToScrool;
        // Making a request to url and getting response


        @SuppressLint({"WrongThread", "StaticFieldLeak"})
        @Override
        protected Void doInBackground(Void... voids) {
            String jsonStr = sh.makeServiceCall(url);


            String titles;
            String imgurl;
            String rating;
            String desc;
            int id;
            if (jsonStr != null){
                switch (media_type){
                    case "movie":
                        try {
                            JSONObject mainObject = new JSONObject(jsonStr);
                            JSONArray results = mainObject.getJSONArray("results");
                            for (int i = 0 ; i < results.length() ; i++){
                                titles = results.getJSONObject(i).getString("title");
                                imgurl ="https://image.tmdb.org/t/p/w185_and_h278_bestv2/"+ results.getJSONObject(i).getString("poster_path");
                                id = results.getJSONObject(i).getInt("id");
                                list.add(new Movies(titles,imgurl,id,media_type));
                            }

                        }catch (JSONException e){
                            e.printStackTrace();
                        }

                        break;
                    case "TV":
                        try {
                            JSONObject mainObject = new JSONObject(jsonStr);
                            JSONArray results = mainObject.getJSONArray("results");
                            for (int i = 0 ; i < results.length() ; i++){
                                titles = results.getJSONObject(i).getString("name");
                                imgurl = "https://image.tmdb.org/t/p/w185_and_h278_bestv2/" + results.getJSONObject(i).getString("poster_path");
                                id = results.getJSONObject(i).getInt("id");
                                list.add(new Movies(titles, imgurl, id,media_type));
                            }

                        }

                        catch (JSONException e){
                            e.printStackTrace();
                        }
                        break;
                    case "people":
                    case "trendpeople":
                        try {
                            JSONObject mainObject = new JSONObject(jsonStr);
                            JSONArray results = mainObject.getJSONArray("results");
                            for (int i = 0 ; i < results.length() ; i++){
                                titles = results.getJSONObject(i).getString("name");
                                imgurl ="https://image.tmdb.org/t/p/w500/"+ results.getJSONObject(i).getString("profile_path");
                                id = results.getJSONObject(i).getInt("id");
                                list.add(new Movies(titles,imgurl,id ,media_type));
                            }
                            //list.add(new Movies("plus","",696969,"trendpeople"));


                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }
                        break;
                    case "topMovies":
                        try {

                            JSONObject mainObject = new JSONObject(jsonStr);
                            JSONArray results = mainObject.getJSONArray("results");
                            for (int i = 0 ; i < results.length() ; i++){
                                titles = results.getJSONObject(i).getString("title");
                                imgurl ="https://image.tmdb.org/t/p/w185_and_h278_bestv2/"+ results.getJSONObject(i).getString("poster_path");
                                id = results.getJSONObject(i).getInt("id");
                                rating = String.valueOf(results.getJSONObject(i).getDouble("vote_average"));
                                desc = results.getJSONObject(i).getString("overview");
                                JSONArray genres = results.getJSONObject(i).getJSONArray("genre_ids");
                                String genre = "";
                                for (int y = 0 ; y < genres.length() ; y++){
                                    JSONArray genresids = new JSONObject(json_Genres_ids).getJSONArray("genres");
                                    for (int z = 0 ; z < genresids.length() ; z++){
                                        if(genresids.getJSONObject(z).getInt("id")==genres.getInt(y)){
                                            if(y==genres.length()-1){
                                                genre += genresids.getJSONObject(z).getString("name");
                                            }
                                            else
                                            genre += genresids.getJSONObject(z).getString("name")+" • ";
                                        }
                                    }
                                }
                                String date = results.getJSONObject(i).getString("release_date");
                                char [] chardate = date.toCharArray();
                                String year = date.substring(0,4);
                                String month = months[Integer.parseInt(date.substring(5,7))-1];
                                String day = date.substring(8,10);

                                String lang = results.getJSONObject(i).getString("original_language");
                                list.add(new Movies(titles,imgurl,id,rating,desc,media_type,lang,month+" "+day+", "+year,genre));
                            }

                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }
                        break;
                    case "toptv":
                        try {
                            JSONObject mainObject = new JSONObject(jsonStr);
                            JSONArray results = mainObject.getJSONArray("results");
                            for (int i = 0 ; i < results.length() ; i++){
                                titles = results.getJSONObject(i).getString("name");
                                imgurl ="https://image.tmdb.org/t/p/w185_and_h278_bestv2/"+ results.getJSONObject(i).getString("poster_path");
                                id = results.getJSONObject(i).getInt("id");
                                rating = String.valueOf(results.getJSONObject(i).getDouble("vote_average"));
                                desc = results.getJSONObject(i).getString("overview");
                                JSONArray genres = results.getJSONObject(i).getJSONArray("genre_ids");
                                String genre = "";
                                for (int y = 0 ; y < genres.length() ; y++){
                                    JSONArray genresids = new JSONObject(json_TVGenres_ids).getJSONArray("genres");
                                    for (int z = 0 ; z < genresids.length() ; z++){
                                        if(genresids.getJSONObject(z).getInt("id")==genres.getInt(y)){
                                            if(y==genres.length()-1){
                                                genre += genresids.getJSONObject(z).getString("name");
                                            }
                                            else
                                                genre += genresids.getJSONObject(z).getString("name")+" • ";
                                        }
                                    }
                                }
                                String date = results.getJSONObject(i).getString("first_air_date");
                                String year = date.substring(0,4);
                                String month = months[Integer.parseInt(date.substring(5,7))-1];
                                String day = date.substring(8,10);

                                String lang = results.getJSONObject(i).getString("original_language");
                                list.add(new Movies(titles,imgurl,id,rating,desc,media_type,lang,month+" "+day+", "+year,genre));
                            }



                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }
                        break;
                    case "movierec":
                        try {
                            JSONObject mainObject = new JSONObject(jsonStr);
                            JSONArray results = mainObject.getJSONArray("results");
                            for (int i = 0 ; i < results.length() ; i++){
                                titles = results.getJSONObject(i).getString("title");
                                imgurl ="https://image.tmdb.org/t/p/w500"+ results.getJSONObject(i).getString("poster_path");
                                id = results.getJSONObject(i).getInt("id");
                                list.add(new Movies(titles,imgurl,id,media_type));
                            }

                        }catch (JSONException e){
                            e.printStackTrace();
                        }
                        break;
                    case "TVrec":
                        try {
                            JSONObject mainObject = new JSONObject(jsonStr);
                            JSONArray results = mainObject.getJSONArray("results");
                            for (int i = 0 ; i < results.length() ; i++){
                                titles = results.getJSONObject(i).getString("name");
                                imgurl = "https://image.tmdb.org/t/p/w500" + results.getJSONObject(i).getString("poster_path");
                                id = results.getJSONObject(i).getInt("id");
                                list.add(new Movies(titles, imgurl, id,media_type));
                            }

                        }

                        catch (JSONException e){
                            e.printStackTrace();
                        }
                        break;
                    case "seasons":
                        try {
                            JSONObject mainObject = new JSONObject(jsonStr);
                            JSONArray results = mainObject.getJSONArray("seasons");
                            for (int i = 0 ; i < results.length() ; i++){
                                titles = results.getJSONObject(i).getString("name");
                                imgurl ="https://image.tmdb.org/t/p/w500"+ results.getJSONObject(i).getString("poster_path");
                                id = results.getJSONObject(i).getInt("id");
                                int num = results.getJSONObject(i).getInt("season_number");
                                list.add(new Movies(titles,imgurl,id,"seasons",num));
                            }
                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }
                        break;
                    case "episodes":
                        try {
                            JSONObject mainObject = new JSONObject(jsonStr);
                            String name = mainObject.getString("name");
                            JSONArray results = mainObject.getJSONArray("episodes");
                            for (int i = 0 ; i < results.length() ; i++){
                                titles = name +" : "+ results.getJSONObject(i).getString("name");
                                imgurl ="https://image.tmdb.org/t/p/w500"+ results.getJSONObject(i).getString("still_path");
                                id = results.getJSONObject(i).getInt("id");
                                list.add(new Movies(titles,imgurl,id,"episodes"));
                                Log.i("TAG",imgurl);
                            }

                        }
                        catch (JSONException e){
                            e.printStackTrace();
                        }
                        break;
                    case "castm":
                        try {
                            JSONObject mainObject = new JSONObject(jsonStr);
                            JSONArray results = mainObject.getJSONArray("cast");
                            for (int i = 0 ; i < results.length() ; i++){
                                titles = results.getJSONObject(i).getString("title");
                                imgurl ="https://image.tmdb.org/t/p/w500"+ results.getJSONObject(i).getString("poster_path");
                                id = results.getJSONObject(i).getInt("id");
                                list.add(new Movies(titles,imgurl,id,media_type));
                            }

                        }catch (JSONException e){
                            e.printStackTrace();
                        }

                        break;
                    case "castt":
                        try {
                            JSONObject mainObject = new JSONObject(jsonStr);
                            JSONArray results = mainObject.getJSONArray("cast");
                            for (int i = 0 ; i < results.length() ; i++){
                                titles = results.getJSONObject(i).getString("name");
                                imgurl ="https://image.tmdb.org/t/p/w500"+ results.getJSONObject(i).getString("poster_path");
                                id = results.getJSONObject(i).getInt("id");
                                list.add(new Movies(titles,imgurl,id,media_type));
                            }

                        }catch (JSONException e){
                            e.printStackTrace();
                        }

                        break;
                    case "crewm":
                        try {
                            JSONObject mainObject = new JSONObject(jsonStr);
                            JSONArray results = mainObject.getJSONArray("crew");
                            for (int i = 0 ; i < results.length() ; i++){
                                titles = results.getJSONObject(i).getString("title");
                                imgurl ="https://image.tmdb.org/t/p/w500"+ results.getJSONObject(i).getString("poster_path");
                                id = results.getJSONObject(i).getInt("id");
                                list.add(new Movies(titles,imgurl,id,media_type));
                            }

                        }catch (JSONException e){
                            e.printStackTrace();
                        }

                        break;
                    case "crewt":
                        try {
                            JSONObject mainObject = new JSONObject(jsonStr);
                            JSONArray results = mainObject.getJSONArray("crew");
                            for (int i = 0 ; i < results.length() ; i++){
                                titles = results.getJSONObject(i).getString("name");
                                imgurl ="https://image.tmdb.org/t/p/w500"+ results.getJSONObject(i).getString("poster_path");
                                id = results.getJSONObject(i).getInt("id");
                                list.add(new Movies(titles,imgurl,id,media_type));
                            }

                        }catch (JSONException e){
                            e.printStackTrace();
                        }

                        break;
                }
            }
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if (isOnHome){
                if (!isRefreshed){
                    layout.setVisibility(View.INVISIBLE);
                    layout2.setVisibility(View.VISIBLE);
                }
            }
            postitionToScrool =list.size()-6;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            recyclerView.setHasFixedSize(true);
            PopularListAdapter adapter1 = new PopularListAdapter(list,context);
            RateListAdapter adapter2 = new RateListAdapter(list,context);
            if (!isRefreshed){
                if (listType.equals("hori")){
                    if (media_type.equals("movierec")||media_type.equals("TVrec")){
                        RecListAdapter adapter = new RecListAdapter(list,context);
                        recyclerView.setLayoutManager(new LinearLayoutManager(context,LinearLayout.HORIZONTAL,false));
                        recyclerView.setAdapter(adapter);
                    }else {
                        recyclerView.setLayoutManager(new LinearLayoutManager(context,LinearLayout.HORIZONTAL,false));
                        recyclerView.setAdapter(adapter1);
                    }
                    if(list.size()==0){
                        ((ViewGroup)recyclerView.getParent()).removeAllViews();
                    }


                }else if (listType.equals("grid")){
                    recyclerView.setLayoutManager(new GridLayoutManager(context,2));
                    recyclerView.setAdapter(adapter1);
                } else if (listType.equals("list")){
                    recyclerView.setLayoutManager(new LinearLayoutManager(context));
                    recyclerView.setAdapter(adapter2);
                }
                if (isOnHome){
                    layout.setVisibility(View.VISIBLE);
                    layout2.setVisibility(View.INVISIBLE);
                }


            } else {
                if (listType.equals("list")){
                    adapter2.setList(list);
                    recyclerView.getAdapter().notifyDataSetChanged();
                    adapter2.notifyDataSetChanged();
                }else {
                    recyclerView.getAdapter().notifyDataSetChanged();
                    adapter1.notifyDataSetChanged();
                }

            }
            



        }
    }


    public void getImagesforSlider(){
        new getImages().execute();
    }

    ViewPager viewPager;
    WindowManager windowManager;
    private boolean isProfile = false;

    public void setProfile(boolean profile) {
        isProfile = profile;
    }

    public GetResFromApi(Context context, ViewPager viewPager, String url, WindowManager windowManager) {
        this.context = context;
        this.viewPager = viewPager;
        this.url = url;
        this.windowManager = windowManager;
    }

    public class getImages extends AsyncTask<Void,Void,Void>{
        HttpHandler sh = new HttpHandler();

        @Override
        protected Void doInBackground(Void... voids) {
            Log.i("TAG", String.valueOf(url));
            String jsonStr = sh.makeServiceCall(url);
            try {
                if(!isProfile){
                    JSONObject ob = new JSONObject(jsonStr);
                    JSONArray bd=ob.getJSONArray("backdrops");
                    for (int i=0;i<bd.length();i++){
                        img_url.add("https://image.tmdb.org/t/p/original"+bd.getJSONObject(i).getString("file_path"));
                    }
                    Log.i("TAG", String.valueOf(img_url.size()));
                }else {
                    JSONObject ob = new JSONObject(jsonStr);
                    JSONArray bd=ob.getJSONArray("profiles");
                    for (int i=0;i<bd.length();i++){
                        img_url.add("https://image.tmdb.org/t/p/original"+bd.getJSONObject(i).getString("file_path"));
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
            ImageSliderAdapter adapter = new ImageSliderAdapter(img_url,context);
            viewPager.setAdapter(adapter);
        }

    }

    List<Videos> videosList = new ArrayList<>();

    public GetResFromApi(Context context, String url, ViewPager viewPager, WindowManager windowManager) {
        this.context = context;
        this.url = url;
        this.viewPager = viewPager;
        this.windowManager = windowManager;
    }

    public class getVideos extends AsyncTask<Void,Void,Void>{
        HttpHandler sh = new HttpHandler();
        @Override
        protected Void doInBackground(Void... voids) {
            String jsonStr = sh.makeServiceCall(url);
            try {
                JSONObject ob = new JSONObject(jsonStr);
                JSONArray results = ob.getJSONArray("results");

                for (int i = 0 ; i < results.length() ; i++ ){
                    String img_url = "https://img.youtube.com/vi/"+results.getJSONObject(i).getString("key")+"/hqdefault.jpg";
                    String title = results.getJSONObject(i).getString("name");
                    String vid_url = "https://www.youtube.com/watch?v="+results.getJSONObject(i).getString("key");
                    videosList.add(new Videos(results.getJSONObject(i).getString("key"),img_url,title,vid_url));
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            VideoSliderAdapter adapter = new VideoSliderAdapter(videosList,context,windowManager);
            viewPager.setAdapter(adapter);
        }
    }

    public void getVideosForSliders() {
        new getVideos().execute();
    }

    ImageView bd,pr;
    TextView tt,st,rl,olg,rt,pp,nos,noe,gr,desc;

    public GetResFromApi(Context context, String url, ImageView bd, ImageView pr, TextView tt, TextView st, TextView rl, TextView olg, TextView rt, TextView pp, TextView nos, TextView noe, TextView gr, TextView desc) {
        this.context = context;
        this.url = url;
        this.bd = bd;
        this.pr = pr;
        this.tt = tt;
        this.st = st;
        this.rl = rl;
        this.olg = olg;
        this.rt = rt;
        this.pp = pp;
        this.nos = nos;
        this.noe = noe;
        this.gr = gr;
        this.desc = desc;
    }



    String[] months = {
            "January",
            "February",
            "March",
            "April",
            "May",
            "June",
            "July",
            "August",
            "September",
            "October",
            "November",
            "December"
    };

    String json_Genres_ids = "{\n" +
            "  \"genres\": [\n" +
            "    {\n" +
            "      \"id\": 28,\n" +
            "      \"name\": \"Action\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 12,\n" +
            "      \"name\": \"Adventure\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 16,\n" +
            "      \"name\": \"Animation\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 35,\n" +
            "      \"name\": \"Comedy\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 80,\n" +
            "      \"name\": \"Crime\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 99,\n" +
            "      \"name\": \"Documentary\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 18,\n" +
            "      \"name\": \"Drama\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 10751,\n" +
            "      \"name\": \"Family\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 14,\n" +
            "      \"name\": \"Fantasy\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 36,\n" +
            "      \"name\": \"History\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 27,\n" +
            "      \"name\": \"Horror\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 10402,\n" +
            "      \"name\": \"Music\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 9648,\n" +
            "      \"name\": \"Mystery\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 10749,\n" +
            "      \"name\": \"Romance\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 878,\n" +
            "      \"name\": \"Science Fiction\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 10770,\n" +
            "      \"name\": \"TV Movie\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 53,\n" +
            "      \"name\": \"Thriller\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 10752,\n" +
            "      \"name\": \"War\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 37,\n" +
            "      \"name\": \"Western\"\n" +
            "    }\n" +
            "  ]\n" +
            "}";
    String json_TVGenres_ids = "{\n" +
            "  \"genres\": [\n" +
            "    {\n" +
            "      \"id\": 10759,\n" +
            "      \"name\": \"Action & Adventure\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 16,\n" +
            "      \"name\": \"Animation\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 35,\n" +
            "      \"name\": \"Comedy\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 80,\n" +
            "      \"name\": \"Crime\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 99,\n" +
            "      \"name\": \"Documentary\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 18,\n" +
            "      \"name\": \"Drama\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 10751,\n" +
            "      \"name\": \"Family\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 10762,\n" +
            "      \"name\": \"Kids\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 9648,\n" +
            "      \"name\": \"Mystery\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 10763,\n" +
            "      \"name\": \"News\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 10764,\n" +
            "      \"name\": \"Reality\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 10765,\n" +
            "      \"name\": \"Sci-Fi & Fantasy\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 10766,\n" +
            "      \"name\": \"Soap\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 10767,\n" +
            "      \"name\": \"Talk\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 10768,\n" +
            "      \"name\": \"War & Politics\"\n" +
            "    },\n" +
            "    {\n" +
            "      \"id\": 37,\n" +
            "      \"name\": \"Western\"\n" +
            "    }\n" +
            "  ]\n" +
            "}";


}
