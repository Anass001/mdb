package com.zeneo.tmdb.Adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zeneo.tmdb.Activities.MoviesActivity;
import com.zeneo.tmdb.Activities.PeopleActivity;
import com.zeneo.tmdb.Activities.TVShowsActivity;
import com.zeneo.tmdb.Models.Search;
import com.zeneo.tmdb.R;

import java.util.List;

import static com.bumptech.glide.request.RequestOptions.centerCropTransform;

public class SearchListAdapter extends RecyclerView.Adapter<SearchListAdapter.ViewHolder> {

    private List<Search> list;
    private Context context;
    String mt;

    public SearchListAdapter(List<Search> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public SearchListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_list_item,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchListAdapter.ViewHolder holder, final int position) {
        holder.title.setText(list.get(position).getTitle());
        switch (list.get(position).getMedia_type()) {
            case "tv":
                holder.mediaType.setText("TV Show");
                break;
            case "movie":
                holder.mediaType.setText("Movie");
                break;
            case "person":
                holder.mediaType.setText("Person");
                break;
        }
        Glide.with(context).load(list.get(position).getImgurl())
                .apply(centerCropTransform().error(R.drawable.bg_null_search))
                .into(holder.imageView);
        final Intent[] intent = new Intent[1];
        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                if (list.size() > 0){
                    switch (list.get(position).getMedia_type()) {
                        case "tv":
                            intent[0] = new Intent(context, TVShowsActivity.class);
                            break;
                        case "movie":
                            intent[0] = new Intent(context, MoviesActivity.class);

                            break;
                        case "person":
                            intent[0] = new Intent(context, PeopleActivity.class);
                            break;
                    }
                intent[0].putExtra("id", String.valueOf(list.get(position).getId()));
                intent[0].setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intent[0]);
            }
                }
                catch (IndexOutOfBoundsException e){
                    Log.e("adapter_error", e.getMessage());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView title,mediaType;
        LinearLayout layout;
        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView)itemView.findViewById(R.id.search_img);
            title = (TextView)itemView.findViewById(R.id.search_title);
            mediaType = (TextView)itemView.findViewById(R.id.search_type);
            layout = (LinearLayout)itemView.findViewById(R.id.search_layout);

        }
    }
}
