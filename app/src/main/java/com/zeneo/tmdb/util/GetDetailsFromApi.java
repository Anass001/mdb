package com.zeneo.tmdb.util;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import com.zeneo.tmdb.Models.Movies;

import java.util.List;

public class GetDetailsFromApi {


    private String url;
    private String type;
    private String RecyvlerView;
    List<Movies> list;
    private ImageView bd,pr;
    private TextView tt,st,rl,olg,rt,bg,gr,rv,desc;
    Context context;
    String imageurl;

    public String getImageurl() {
        return imageurl;
    }

    public GetDetailsFromApi(ImageView bd, ImageView pr, TextView tt, TextView st, TextView rl, TextView olg, TextView rt, TextView bg, TextView desc, TextView gr, TextView rv) {
        this.bd = bd;
        this.pr = pr;
        this.tt = tt;
        this.st = st;
        this.rl = rl;
        this.olg = olg;
        this.rt = rt;
        this.bg = bg;
        this.desc = desc;
        this.gr = gr;
        this.rv = rv;
    }

    public void setContext(Context context) {
        this.context = context;
    }


    public void setUrl(String url) {
        this.url = url;
    }








}
