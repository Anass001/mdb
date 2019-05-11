package com.zeneo.tmdb.DATABASE;

import android.provider.BaseColumns;

public final class DataContract {

    public class FavoriteEntry implements BaseColumns{
        public static final String TABLE_NAME = "favorite";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_IMG_URL = "image";
        public static final String COLUMN_NAME_MOV_ID = "movieid";
        public static final String COLUMN_NAME_TYPE = "type";
    }
    public class WatchListEntry implements BaseColumns {
        public static final String TABLE_NAME = "watchlist";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_IMG_URL = "image";
        public static final String COLUMN_NAME_MOV_ID = "movieid";
        public static final String COLUMN_NAME_TYPE = "type";
    }
}
