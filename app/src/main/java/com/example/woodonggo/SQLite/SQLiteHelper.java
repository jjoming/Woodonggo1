package com.example.woodonggo.SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.Nullable;

public class SQLiteHelper extends android.database.sqlite.SQLiteOpenHelper{

    public static final int DATABASE_VERSION = 1; //데이터베이스의 버전
    public static final String DATABASE_NAME = "UdongoUser.db"; //데이터베이스의 이름

    //테이블 생성 및 삭제 SQL 쿼리
    public static final String SQL_CREATE_TABLE_1 =
            "CREATE TABLE" + TableInfo_User.TABLE_1_NAME + " (" +
            TableInfo_User.TABLE_1_COLUMN_NAME_ID + " VARCHAR(15) PRIMARY KEY," +
            TableInfo_User.TABLE_1_COLUMN_NAME_PASSWORD + " VARCHAR(20) PRIMARY KEY," +
            TableInfo_User.TABLE_1_COLUMN_NAME_AREA1 + " TEXT," +
            TableInfo_User.TABLE_1_COLUMN_NAME_AREA2 + " TEXT," +
            TableInfo_User.TABLE_1_COLUMN_NAME_NAME + "TEXT PRIMARY KEY," +
            TableInfo_User.TABLE_1_COLUMN_NAME_PROFILE + "" + //TODO
            TableInfo_User.TABLE_1_COLUMN_NAME_MANNERDEGREE + "REAL DEFAULT 36.5)";


    public SQLiteHelper(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version, String user) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    //데이터베이스를 최초로 생성할 때 호출
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE_1); //SQL 쿼리 실행
    }

    @Override
    //데이터 스키마를 업데이트할 때 호출
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        super.onDowngrade(db, oldVersion, newVersion);
    }
}
