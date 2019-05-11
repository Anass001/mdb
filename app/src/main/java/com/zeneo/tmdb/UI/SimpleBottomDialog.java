package com.zeneo.tmdb.UI;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.zeneo.tmdb.DATABASE.DataContract;
import com.zeneo.tmdb.DATABASE.DataHelper;
import com.zeneo.tmdb.R;

public class SimpleBottomDialog extends BottomSheetDialog {

    TextView favortxt , watchlisttxt;
    ImageView favoricon , watchicon;
    LinearLayout layout1,layout2,layout3;
    SQLiteDatabase db,db2;
    DataHelper.FavoriteHelper sqlfavorite;
    DataHelper.WatchListHelper sqlwatchlist;
    Context context;


    private String type,id,title,imgurl;

    public SimpleBottomDialog(@NonNull Context context, String type, String id,String title,String imgurl) {
        super(context);
        this.context = context;
        this.type = type;
        this.id = id;
        this.title = title;
        this.imgurl = imgurl;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_bottom_simple);


        layout1 = findViewById(R.id.layout1);
        layout2 = findViewById(R.id.layout2);
        layout3 = findViewById(R.id.sharelt);
        favortxt = findViewById(R.id.isfavortxt);
        watchlisttxt = findViewById(R.id.islisttxt);
        favoricon = findViewById(R.id.addfavoricon);
        watchicon = findViewById(R.id.addlisticon);

        sqlfavorite = new DataHelper.FavoriteHelper(context);
        db = sqlfavorite.getWritableDatabase();
        sqlwatchlist = new DataHelper.WatchListHelper(context);
        db2 = sqlwatchlist.getWritableDatabase();

        boolean isExist = sqlfavorite.CheckIsDataAlreadyInDBorNot(DataContract.FavoriteEntry.TABLE_NAME,id);
        if (isExist){
            favoricon.setImageResource(R.drawable.favourite_icon_red);
            favortxt.setText("Remove from favorite");
        }else {
            favoricon.setImageResource(R.drawable.favorite_desactive);
            favortxt.setText("Add from favorite");
        }

        boolean count3 = sqlwatchlist.CheckIsDataAlreadyInDBorNot(DataContract.WatchListEntry.TABLE_NAME,id);
        if (count3){
            watchicon.setImageResource(R.drawable.ic_watchlist_added);
            watchlisttxt.setText("Remove from watch list");
        }else {
            watchicon.setImageResource(R.drawable.ic_watchlist_add);
            watchlisttxt.setText("Add to watch list");
        }

        layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean isExist = sqlfavorite.CheckIsDataAlreadyInDBorNot(DataContract.FavoriteEntry.TABLE_NAME,id);
                if (isExist){
                    sqlfavorite.delete(DataContract.FavoriteEntry.TABLE_NAME,id,type);
                    favoricon.setImageResource(R.drawable.favorite_desactive);
                    favortxt.setText("Add to favorite");
                }else {
                    sqlfavorite.insert(DataContract.FavoriteEntry.TABLE_NAME,title,imgurl,id,type);
                    favoricon.setImageResource(R.drawable.favourite_icon_red);
                    favortxt.setText("Remove from favorite");
                }
            }
        });
        layout2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean count3 = sqlwatchlist.CheckIsDataAlreadyInDBorNot(DataContract.WatchListEntry.TABLE_NAME,id);
                if (count3){
                    sqlwatchlist.delete(DataContract.WatchListEntry.TABLE_NAME,id,type);
                    watchicon.setImageResource(R.drawable.ic_watchlist_add);
                    watchlisttxt.setText("Add to watch list");
                }else {
                    sqlwatchlist.insert(DataContract.WatchListEntry.TABLE_NAME,title,imgurl,id,type);
                    watchicon.setImageResource(R.drawable.ic_watchlist_added);
                    watchlisttxt.setText("Remove from watch list");
                }
            }
        });

        layout3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);
                sendIntent.putExtra(Intent.EXTRA_TEXT, "https://www.themoviedb.org/"+type.toLowerCase()+"/"+id);
                sendIntent.setType("text/plain");
                context.startActivity(sendIntent);
            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}
