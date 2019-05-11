package com.zeneo.tmdb.Fragments;



import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.zeneo.tmdb.Activities.HomeActivity;
import com.zeneo.tmdb.R;
import com.zeneo.tmdb.util.GetResFromApi;
import com.zeneo.tmdb.util.InternetConnection;


/**
 * A simple {@link Fragment} subclass.
 */
public class HomeFragment extends Fragment {



    RecyclerView firstrecyclerView , secRecyclerView , recyclerView3 ,recyclerView4;
    LinearLayout layout,layout1;
    LinearLayout layout2;
    TextView seemore1,seemore2;



    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);



        seemore1 =(TextView)view.findViewById(R.id.moremoviesbtn);
        seemore2 = (TextView)view.findViewById(R.id.moretvbtn);
        firstrecyclerView = (RecyclerView)view.findViewById(R.id.poprec);
        secRecyclerView = (RecyclerView)view.findViewById(R.id.poptvrec);
        recyclerView3 = (RecyclerView)view.findViewById(R.id.trendpeoplelist);
        //recyclerView4 = (RecyclerView)view.findViewById(R.id.toptvrec);
        layout = (LinearLayout)view.findViewById(R.id.home_content);
        layout2 = view.findViewById(R.id.parent_lt);
        layout1 = (LinearLayout)view.findViewById(R.id.home_load);




        final View view2 = layout.getRootView();

        if(new InternetConnection().chechInternet(getContext())){
            new GetResFromApi(firstrecyclerView,getContext(),
                    "https://api.themoviedb.org/3/discover/movie?api_key=5d173b53167711178472dc9d98603e31&language=en-US&sort_by=popularity.desc&include_adult=false&include_video=false&page=1"
                    ,"movie","hori",layout,layout1,"movie").getDetails();
            new GetResFromApi(secRecyclerView,getContext(),
                    "https://api.themoviedb.org/3/discover/tv?api_key=5d173b53167711178472dc9d98603e31&language=en-US&sort_by=popularity.desc&page=1&timezone=America%2FNew_York&include_null_first_air_dates=false"
                    ,"TV","hori",layout,layout1,"TV").getDetails();
            new GetResFromApi(recyclerView3,getContext(),
                    "https://api.themoviedb.org/3/trending/person/week?api_key=5d173b53167711178472dc9d98603e31"
                    ,"trendpeople","hori",layout,layout1,"people").getDetails();
        }else {
            final View view1 = LayoutInflater.from(getContext()).inflate(R.layout.no_internet,null);
            TextView tryAgain= view1.findViewById(R.id.tryagainbtn);
            tryAgain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (new InternetConnection().chechInternet(getContext())){
                        layout2.removeAllViews();
                        layout2.addView(layout);
                        new GetResFromApi(firstrecyclerView,getContext(),
                                "https://api.themoviedb.org/3/discover/movie?api_key=5d173b53167711178472dc9d98603e31&language=en-US&sort_by=popularity.desc&include_adult=false&include_video=false&page=1"
                                ,"movie","hori",layout,layout1,"movie").getDetails();
                        new GetResFromApi(secRecyclerView,getContext(),
                                "https://api.themoviedb.org/3/discover/tv?api_key=5d173b53167711178472dc9d98603e31&language=en-US&sort_by=popularity.desc&page=1&timezone=America%2FNew_York&include_null_first_air_dates=false"
                                ,"TV","hori",layout,layout1,"TV").getDetails();
                        new GetResFromApi(recyclerView3,getContext(),
                                "https://api.themoviedb.org/3/trending/person/week?api_key=5d173b53167711178472dc9d98603e31"
                                ,"trendpeople","hori",layout,layout1,"people").getDetails();


                    }else {
                        layout2.removeAllViews();
                        layout2.addView(view1);
                    }

                }
            });
            view1.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            layout2.removeAllViews();
            layout2.addView(view1);
        }


        seemore1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((HomeActivity)getContext()).getViewPager().setCurrentItem(1);
            }
        });

        seemore2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((HomeActivity)getContext()).getViewPager().setCurrentItem(2);            }
        });

        return view;
    }



}
