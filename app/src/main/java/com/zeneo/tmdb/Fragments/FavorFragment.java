package com.zeneo.tmdb.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.zeneo.tmdb.Adapters.PopularListAdapter;
import com.zeneo.tmdb.DATABASE.DataContract;
import com.zeneo.tmdb.DATABASE.DataHelper;
import com.zeneo.tmdb.Models.Movies;
import com.zeneo.tmdb.R;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavorFragment extends Fragment {


    RecyclerView recyclerView;
    Bundle bundle;
    String type="",databasename="";
    List<Movies> list = new ArrayList<>();
    public FavorFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favor, container, false);
        recyclerView = (RecyclerView)view.findViewById(R.id.favor_list);
        bundle = getArguments();
        try {
            type = bundle.getString("type");
            databasename = bundle.getString("database");
        }
        catch (NullPointerException e){ }

        getDataFromDataBase(type);
        PopularListAdapter adapter = new PopularListAdapter(list,getContext());
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(),3));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter);

        Log.i("TAG", String.valueOf(list.size()));

        return view;
    }

    public void getDataFromDataBase(String type){
        if(databasename.equals("Favorite")){
            DataHelper.FavoriteHelper sqlHelper = new DataHelper.FavoriteHelper(getContext());
            list.addAll(sqlHelper.getAllItems(DataContract.FavoriteEntry.TABLE_NAME,type));
        }else if (databasename.equals("Watch List")){
            DataHelper.WatchListHelper sqlHelper = new DataHelper.WatchListHelper(getContext());
            list.addAll(sqlHelper.getAllItems(DataContract.WatchListEntry.TABLE_NAME,type));
        }
    }



}
