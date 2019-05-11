package com.zeneo.tmdb.UI;


import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.zeneo.tmdb.Models.Videos;
import com.zeneo.tmdb.R;

import java.util.List;

public class VideoSliderAdapter extends PagerAdapter {


    List<Videos> list;
    Context context;
    ImageView imageView;
    WindowManager windowManager;

    public VideoSliderAdapter(List<Videos> list, Context context, WindowManager windowManager) {
        this.list = list;
        this.context = context;

        this.windowManager = windowManager;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view==object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.slider_vid_item,null);
        imageView = (ImageView)view.findViewById(R.id.vid_img);
        TextView textView = (TextView)view.findViewById(R.id.vid_title);
        Glide.with(context).load(list.get(position).getImg_url()).into(imageView);
        textView.setText(list.get(position).getTitle());
        final String id = list.get(position).getId();
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + id));
                Intent webIntent = new Intent(Intent.ACTION_VIEW,
                        Uri.parse("http://www.youtube.com/watch?v="+ id ));
                try {
                    context.startActivity(appIntent);
                } catch (ActivityNotFoundException ex) {
                    context.startActivity(webIntent);
                }
            }
        });
        setImagesSize();
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
    public void setImagesSize() {
        Display display = windowManager.getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        imageView.setMaxHeight((int) (width * 1.7));

    }
}
