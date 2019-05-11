package com.zeneo.tmdb.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zeneo.tmdb.Activities.MoviesActivity;
import com.zeneo.tmdb.Activities.TVShowsActivity;
import com.zeneo.tmdb.Models.Movies;
import com.zeneo.tmdb.R;

import java.util.List;

import static com.bumptech.glide.request.RequestOptions.centerCropTransform;

public class RecListAdapter extends RecyclerView.Adapter<RecListAdapter.ViewHolder> {

    List<Movies> list ;
    Context context ;

    public RecListAdapter(List<Movies> list, Context context) {
        this.list = list;
        this.context = context;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.horizon_list_item,parent,false);
        return new RecListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        holder.textView.setText(list.get(position).getTitle());
        if (list.get(position).getImgurl() != null){
            Glide.with(context).load(list.get(position).getImgurl()).apply(centerCropTransform()
                    .error(R.drawable.bg_null)
            ).into(holder.imageView);

        }

        holder.layout.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                if(list.get(position).getType().equals("movierec")){
                    Intent intent = new Intent(context,MoviesActivity.class);
                    intent.putExtra("id",String.valueOf(list.get(position).getMovie_id()));
                    context.startActivity(intent);
                } else if (list.get(position).getType().equals("TVrec")){
                    Intent intent = new Intent(context,TVShowsActivity.class);
                    intent.putExtra("id",String.valueOf(list.get(position).getMovie_id()));
                    context.startActivity(intent);
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;
        ImageView imageView;
        LinearLayout layout;
        LinearLayout layout2;


        public ViewHolder(View itemView) {
            super(itemView);

            textView = (TextView)itemView.findViewById(R.id.hortitle);
            imageView = (ImageView) itemView.findViewById(R.id.horposter);
            layout = (LinearLayout)itemView.findViewById(R.id.mov_lt);
            layout2 =(LinearLayout)itemView.findViewById(R.id.horaddtodata);
        }
    }
}
