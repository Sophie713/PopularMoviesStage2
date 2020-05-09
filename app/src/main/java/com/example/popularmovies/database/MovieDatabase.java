package com.example.popularmovies.database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.popularmovies.objects.MovieDetailObject;
import com.example.popularmovies.utils.Const;

@Database(entities = {MovieDetailObject.class}, version = 1)
public abstract class MovieDatabase extends RoomDatabase {

    private static MovieDatabase database;
    private static final Object OBJECT = new Object();

    public static MovieDatabase getInstance(Context context) {
        if(database==null){
            synchronized (OBJECT){
                database = Room.databaseBuilder(context.getApplicationContext(), MovieDatabase.class,
                        Const.DATABASE_NAME).build();
            }
        }
        return database;
    }

    public abstract MovieDao  movieDao();
}
