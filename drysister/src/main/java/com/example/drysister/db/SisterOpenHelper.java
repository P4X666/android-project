package com.example.drysister.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.drysister.db.TableDefine;

public class SisterOpenHelper extends SQLiteOpenHelper {

    private static final String DB_NAME    = "sister.db";  //数据库名
    private static final int    DB_VERSION = 1;    //数据库版本号

    public SisterOpenHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTableSql = "CREATE TABLE IF NOT EXISTS " + TableDefine.TABLE_FULI + " ("
                + TableDefine.COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + TableDefine.COLUMN_WIDTH + " int, "
                + TableDefine.COLUMN_HEIGHT + " int, "
                + TableDefine.COLUMN_SIZE + " int, "
                + TableDefine.COLUMN_URL + " TEXT, "
                + TableDefine.COLUMN_PERVIEW + " TEXT"
                + ")";
        db.execSQL(createTableSql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }

}
