package com.angelote.testimagenes.Utilerias;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseSqlite extends SQLiteOpenHelper {
    private static final String COMMENTS_TABLE_PERSON = "CREATE TABLE favoritos(Id TEXT)";
    private static final String DB_NAME = "bdtest.sqlite";
    private static final int DB_VERSION = 1;

    public DatabaseSqlite(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(COMMENTS_TABLE_PERSON);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}