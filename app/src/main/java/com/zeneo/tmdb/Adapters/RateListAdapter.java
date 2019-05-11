package com.zeneo.tmdb.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zeneo.tmdb.Activities.MoviesActivity;
import com.zeneo.tmdb.Activities.TVShowsActivity;
import com.zeneo.tmdb.R;


import com.zeneo.tmdb.Models.Movies;
import com.zeneo.tmdb.UI.SimpleBottomDialog;

import java.util.List;

import static com.bumptech.glide.request.RequestOptions.centerCropTransform;

public class RateListAdapter extends RecyclerView.Adapter<RateListAdapter.ViewHolder> {

    List<Movies> list;
    Context context;

    public RateListAdapter(List<Movies> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public RateListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_item,parent,false);
        return new ViewHolder(view);
    }
    @Override
    public long getItemId(int position) { return position; }

    @Override
    public void onBindViewHolder(@NonNull RateListAdapter.ViewHolder holder, final int position) {
        Glide.with(context).load(list.get(position).getImgurl()).apply(centerCropTransform()
                .error(R.drawable.bg_null)).thumbnail(0.3f).into(holder.imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(list.get(position).getType().equals("topMovies")){
                    Intent i = new Intent(context,MoviesActivity.class);
                    i.putExtra("id",String.valueOf(list.get(position).getMovie_id()));
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                } else if (list.get(position).getType().equals("toptv")){
                    Intent i = new Intent(context,TVShowsActivity.class);
                    i.putExtra("id",String.valueOf(list.get(position).getMovie_id()));
                    i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(i);
                }
            }
        });
        final String type;
        if(list.get(position).getType().equals("toptv")){
            type = "TV";
        }else {
            type = "movie";
        }
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {


                SimpleBottomDialog dialog = new SimpleBottomDialog(context,type,String.valueOf(list.get(position).getMovie_id()),
                        list.get(position).getTitle(),list.get(position).getImgurl());

                dialog.setCancelable(true);
                dialog.show();
                return true;
            }
        });
        holder.title.setText(list.get(position).getTitle());
        holder.title.setEllipsize(TextUtils.TruncateAt.END);
        holder.title.setLines(1);
        holder.rating.setText(list.get(position).getRating());
        holder.desc.setText(list.get(position).getOverview());
        holder.desc.setSingleLine(false);
        holder.desc.setEllipsize(TextUtils.TruncateAt.END);
        int n = 3; // the exact number of lines you want to display
        holder.desc.setLines(n);
        holder.genre.setText(list.get(position).getGenre());
        holder.genre.setMaxLines(1);
        holder.genre.setEllipsize(TextUtils.TruncateAt.END);
        holder.lang.setText(list.get(position).getLang());
        holder.lang.setAllCaps(true);
        holder.date.setText(list.get(position).getDate());
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView title , rating , desc,lang,date,genre;
        LinearLayout layout;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView)itemView.findViewById(R.id.ratimg);
            title = (TextView)itemView.findViewById(R.id.rattitle);
            rating = (TextView)itemView.findViewById(R.id.rattxt);
            desc = (TextView)itemView.findViewById(R.id.desctxt);
            lang = (TextView)itemView.findViewById(R.id.langtxt);
            date = (TextView)itemView.findViewById(R.id.datetxt);
            genre = (TextView)itemView.findViewById(R.id.genretxt);

        }
    }

    public void setList(List<Movies> list) {
        this.list = list;
    }
}
