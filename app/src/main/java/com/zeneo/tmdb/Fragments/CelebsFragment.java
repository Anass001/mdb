package com.zeneo.tmdb.Fragments;

import android.content.Context;

import android.os.Bundle;
import android.support.v4.app.Fragment;

import android.support.v7.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zeneo.tmdb.R;
import com.zeneo.tmdb.util.GetResFromApi;
import com.zeneo.tmdb.util.InternetConnection;


public class CelebsFragment extends Fragment {



    RecyclerView recyclerView;
    LinearLayout layout,layout1;
    GetResFromApi res;
    String url = "https://api.themoviedb.org/3/person/popular?api_key=5d173b53167711178472dc9d98603e31&language=en-US&page=";




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_celebs, container, false);
        final int[] page = {1};
        recyclerView = (RecyclerView)view.findViewById(R.id.peopleList);
        layout = (LinearLayout)view.findViewById(R.id.people_content);
        layout1 = (LinearLayout)view.findViewById(R.id.celeb_load);
        res = new GetResFromApi(recyclerView,getContext(),
                url+ page[0]
                ,"people","grid",layout,layout1,"people");

        if(new InternetConnection().chechInternet(getContext())){
            res.clearData();
            res.getDetails();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    if (dy > 0 && !recyclerView.canScrollVertically(View.FOCUS_DOWN)){
                        page[0]++;
                        res.setUrl(url+ page[0]);
                        res.setRefreshed(true);
                        res.getDetails();
                    }
                }
            });
        }else {
            final View view1 = LayoutInflater.from(getContext()).inflate(R.layout.no_internet,null);
            TextView tryAgain= view1.findViewById(R.id.tryagainbtn);
            tryAgain.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (new InternetConnection().chechInternet(getContext())){
                        layout.removeAllViews();
                        layout.addView(recyclerView);
                        res.getDetails();
                    }else {
                        layout.removeAllViews();
                        layout.addView(view1);
                    }

                }
            });
            view1.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
            layout.removeAllViews();
            layout.addView(view1);
        }

        return view;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        Log.i("attached","On attach now");

    }
}
