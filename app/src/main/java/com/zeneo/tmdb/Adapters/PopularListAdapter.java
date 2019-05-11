package com.zeneo.tmdb.Adapters;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.zeneo.tmdb.Activities.HomeActivity;
import com.zeneo.tmdb.Activities.MoviesActivity;
import com.zeneo.tmdb.Activities.PeopleActivity;
import com.zeneo.tmdb.Activities.TVShowsActivity;
import com.zeneo.tmdb.DATABASE.DataHelper;
import com.zeneo.tmdb.Models.Movies;
import com.zeneo.tmdb.R;

import java.util.ArrayList;
import java.util.List;

import static com.bumptech.glide.request.RequestOptions.centerCropTransform;
import static com.bumptech.glide.request.RequestOptions.circleCropTransform;
import static com.bumptech.glide.request.RequestOptions.fitCenterTransform;

public class PopularListAdapter extends RecyclerView.Adapter<PopularListAdapter.ViewHolder> {

    List<Movies> list ;
    Context context ;
    SQLiteDatabase db,db2;
    DataHelper.FavoriteHelper sqlfavorite;
    DataHelper.WatchListHelper sqlwatchlist;


    List<Boolean> selectedItems = new ArrayList<>();


    public PopularListAdapter(List<Movies> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        if(list.get(0).getType().equals("trendpeople")){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.circle_list_item,parent,false);
        }else if (list.get(0).getType().equals("people")){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_list_item,parent,false);
        }
        else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizon_list_item,parent,false);

        }
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {



        if(list.get(0).getType().equals("trendpeople")){

            if (list.get(position).getImgurl() != null){

                if(list.get(position).getMovie_id() == 696969){
                    Glide.with(context).load("").apply(circleCropTransform()
                            .error(R.drawable.bg_null)
                    ).into(holder.imageView);
                }
                else{
                Glide.with(context).load(list.get(position).getImgurl()).apply(circleCropTransform()
                        .error(R.drawable.bg_null)
                ).into(holder.imageView);}

            }
            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(list.get(position).getMovie_id() == 696969){
                        ((HomeActivity)context).getViewPager().setCurrentItem(3);
                    }
                    else{
                Intent i = new Intent(context,PeopleActivity.class);
                i.putExtra("id",String.valueOf(list.get(position).getMovie_id()));
                context.startActivity(i);
                    }
                }
            });
        }else if (list.get(0).getType().equals("people")){
            if (list.get(position).getImgurl() != null){

                Glide.with(context).load(list.get(position).getImgurl()).apply(centerCropTransform()
                        .error(R.drawable.bg_null)
                ).into(holder.imageView);

            }
            holder.textView.setText(list.get(position).getTitle());
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(context,PeopleActivity.class);
                    i.putExtra("id",String.valueOf(list.get(position).getMovie_id()));
                    context.startActivity(i);
                }
            });
        } else {
            holder.textView.setText(list.get(position).getTitle());
            if (list.get(position).getImgurl() != null){

                Glide.with(context).load(list.get(position).getImgurl()).apply(centerCropTransform()
                        .error(R.drawable.bg_null)
                ).into(holder.imageView);

            }


            holder.layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(list.get(position).getType().equals("movie")||list.get(position).getType().equals("castm")||list.get(position).getType().equals("crewm")){
                        Intent i = new Intent(context,MoviesActivity.class);
                        i.putExtra("id",String.valueOf(list.get(position).getMovie_id()));
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);
                    }

                    else if (list.get(position).getType().equals("TV")||list.get(position).getType().equals("castt")||list.get(position).getType().equals("crewt")){
                        Intent i = new Intent(context,TVShowsActivity.class);
                        i.putExtra("id",String.valueOf(list.get(position).getMovie_id()));
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(i);
                    } else if (list.get(position).getType().equals("people")||list.get(position).getType().equals("castp")||list.get(position).getType().equals("crewp")){
                        Intent i = new Intent(context,PeopleActivity.class);
                        i.putExtra("id",String.valueOf(list.get(position).getMovie_id()));
                        context.startActivity(i);
                    } else if (list.get(position).getType().equals("seasons")){
                        ((TVShowsActivity)context).getEpisodes(list.get(position).getSeason_number());
                    }
                }
            });
            
        }


    }



    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView textView;
        ImageView imageView;
        LinearLayout layout;

        public ViewHolder(View itemView) {
            super(itemView);

            if(list.get(0).getType().equals("trendpeople")){
                imageView = (ImageView) itemView.findViewById(R.id.circleprof);
                layout = (LinearLayout)itemView.findViewById(R.id.mov_lt);
            }else {
                textView = (TextView)itemView.findViewById(R.id.hortitle);
                imageView = (ImageView) itemView.findViewById(R.id.horposter);
                layout = (LinearLayout)itemView.findViewById(R.id.mov_lt);
            }


        }
    }
    @Override
    public long getItemId(int position) { return position; }
}
