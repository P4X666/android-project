package com.example.drysister.db;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.drysister.DrySisterApp;
import com.example.drysister.bean.Sister;

import java.util.ArrayList;
import java.util.List;

public class SisterDBHelper {

    private static final String TAG = "SisterDBHelper";

    private static SisterDBHelper   dbHelper;
    private SisterOpenHelper sqlHelper;
    private SQLiteDatabase   db;

    private SisterDBHelper() {
        sqlHelper = new SisterOpenHelper(DrySisterApp.getContext());
    }

    /** 单例 */
    public static SisterDBHelper getInstance() {
        if(dbHelper == null) {
            synchronized (SisterDBHelper.class) {
                if(dbHelper == null) {
                    dbHelper = new SisterDBHelper();
                }
            }
        }
        return dbHelper;
    }

    private ContentValues getContentValues(Sister sister){
        ContentValues contentValues = new ContentValues();
        contentValues.put(TableDefine.COLUMN_ID,sister.getId());
        contentValues.put(TableDefine.COLUMN_WIDTH,sister.getWidth());
        contentValues.put(TableDefine.COLUMN_HEIGHT,sister.getHeight());
        contentValues.put(TableDefine.COLUMN_SIZE,sister.getSize());
        contentValues.put(TableDefine.COLUMN_URL,sister.getUrl());
        contentValues.put(TableDefine.COLUMN_PERVIEW,sister.getPreview());
        return contentValues;
    }

    /** 插入一个妹子 */
    public void insertSister(Sister sister) {
        db = getWritableDB();
        db.insert(TableDefine.TABLE_FULI,null,getContentValues(sister));
        closeIO(null);
    }

    /** 插入一堆妹子(使用事务) */
    public void insertSisters(ArrayList<Sister> sisters) {
        db = getWritableDB();
        db.beginTransaction();
        try{
            for (Sister sister: sisters) {
                db.insert(TableDefine.TABLE_FULI,null,getContentValues(sister));
            }
            db.setTransactionSuccessful();
        } finally {
            if(db != null && db.isOpen()) {
                db.endTransaction();
                closeIO(null);
            }
        }
    }

    /** 删除妹子(根据_id) */
    public void deleteSister(String _id) {
        db = getWritableDB();
        db.delete(TableDefine.TABLE_FULI,"_id =?",new String[]{_id});
        closeIO(null);
    }

    /** 删除所有妹子 */
    public void deleteAllSisters() {
        db = getWritableDB();
        db.delete(TableDefine.TABLE_FULI,null,null);
        closeIO(null);
    }

    /** 更新妹子信息(根据_id) */
    public void deleteSister(String _id,Sister sister) {
        db = getWritableDB();
        db.update(TableDefine.TABLE_FULI,getContentValues(sister),"_id =?",new String[]{_id});
        closeIO(null);
    }

    /** 查询当前表中有多少个妹子 */
    public int getSistersCount() {
        db = getReadableDB();
        Cursor cursor = db.rawQuery("SELECT COUNT (*) FROM " + TableDefine.TABLE_FULI,null);
        cursor.moveToFirst();
        int count = cursor.getInt(0);
        Log.v(TAG,"count：" + count);
        closeIO(cursor);
        return count;
    }

    @SuppressLint("Range")
    private Sister getSister(Cursor cursor){
        Sister sister = new Sister();
        sister.setId(cursor.getInt(cursor.getColumnIndex(TableDefine.COLUMN_ID)));
        sister.setWidth(cursor.getInt(cursor.getColumnIndex(TableDefine.COLUMN_WIDTH)));
        sister.setHeight(cursor.getInt(cursor.getColumnIndex(TableDefine.COLUMN_HEIGHT)));
        sister.setSize(cursor.getInt(cursor.getColumnIndex(TableDefine.COLUMN_SIZE)));
        sister.setUrl(cursor.getString(cursor.getColumnIndex(TableDefine.COLUMN_URL)));
        sister.setPreview(cursor.getString(cursor.getColumnIndex(TableDefine.COLUMN_PERVIEW)));
        return sister;
    }

    /** 分页查询妹子，参数为当前页和每一个的数量，页数从0开始算 */
    public List<Sister> getSistersLimit(int curPage, int limit) {
        db =  getReadableDB();
        List<Sister> sisters = new ArrayList<>();
        String startPos = String.valueOf(curPage * limit);  //数据开始位置
        if(db != null) {
            Cursor cursor = db.query(TableDefine.TABLE_FULI,new String[] {
                    TableDefine.COLUMN_ID, TableDefine.COLUMN_WIDTH,
                    TableDefine.COLUMN_HEIGHT, TableDefine.COLUMN_SIZE,
                    TableDefine.COLUMN_URL, TableDefine.COLUMN_PERVIEW
            },null,null,null,null,TableDefine.COLUMN_ID,startPos + "," + limit);
            while (cursor.moveToNext()) {
                sisters.add(getSister(cursor));
            }
            closeIO(cursor);
        }
        return sisters;
    }

    /** 查询所有妹子 */
    public List<Sister> getAllSisters() {
        db = getReadableDB();
        List<Sister> sisters = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM "+TableDefine.TABLE_FULI,null);
        cursor.moveToFirst();
        while (cursor.moveToNext()) {
            sisters.add(getSister(cursor));
        }
        closeIO(cursor);
        return sisters;
    }

    /** 获得可写数据库的方法 */
    private SQLiteDatabase getWritableDB() {
        return sqlHelper.getWritableDatabase();
    }

    /** 获得可读数据库的方法 */
    private SQLiteDatabase getReadableDB() {
        return sqlHelper.getReadableDatabase();
    }

    /** 关闭cursor和数据库的方法 */
    private void closeIO(Cursor cursor) {
        if(cursor != null) {
            cursor.close();
        }
        if(db != null) {
            db.close();
        }
    }

}
