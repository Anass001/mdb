package com.zeneo.tmdb.Adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import com.bumptech.glide.Glide;
import com.zeneo.tmdb.Activities.SeasonActivity;
import com.zeneo.tmdb.Models.Movies;
import com.zeneo.tmdb.R;

import java.text.DecimalFormat;
import java.util.List;

import static com.bumptech.glide.request.RequestOptions.centerCropTransform;

public class EpisodesListAdapter extends RecyclerView.Adapter<EpisodesListAdapter.ViewHolder> {

    private List<Movies> list;
    private Context context;

    public EpisodesListAdapter(List<Movies> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).
                inflate(R.layout.episodes_list_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
        Glide.with(context).load(list.get(position).getImgurl()).apply(centerCropTransform()
                .error(R.drawable.bg_null)).thumbnail(0.3f).into(holder.imageView);

        holder.title.setText(list.get(position).getTitle());
        holder.title.setEllipsize(TextUtils.TruncateAt.END);
        holder.title.setLines(1);
        holder.epnum.setText("Episode "+list.get(position).getMovie_id());
        DecimalFormat df = new DecimalFormat("#.#");
        String ratformated = df.format(Float.parseFloat(list.get(position).getRating()));
        holder.rating.setText(ratformated);
        holder.desc.setText(list.get(position).getOverview());
        holder.desc.setSingleLine(false);
        holder.desc.setEllipsize(TextUtils.TruncateAt.END);
        int n = 3;
        holder.desc.setLines(n);
        holder.ratingBar.setRating(Float.parseFloat(list.get(position).getRating())/2);

        if(!list.get(position).getImgurl().equals("https://image.tmdb.org/t/p/w185_and_h278_bestv2/null")){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    ((SeasonActivity)context).intentToEpisodeActivity(list.get(position).getMovie_id(),list.get(position).getTitle());
                }
            });
        }


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView title , rating , desc , epnum;
        RatingBar ratingBar;

        public ViewHolder(View itemView) {
            super(itemView);

            ratingBar = itemView.findViewById(R.id.ratingbar);
            rating = itemView.findViewById(R.id.rattxt);
            title = itemView.findViewById(R.id.eptitle);
            epnum = itemView.findViewById(R.id.epnum);
            desc = itemView.findViewById(R.id.desctxt);
            imageView = itemView.findViewById(R.id.epimg);

        }
    }
}
