package com.sezyakot.DjangoChat.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class ChatDBHelper extends SQLiteOpenHelper {

    public ChatDBHelper(Context context) {
        super(context, ChatDatabase.DB_NAME, null, ChatDatabase.DB_VERSION);
        
    }
    
    public ChatDBHelper(Context context, String name, CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE IF NOT EXISTS ");
        sb.append(ChatDatabase.TABLE_NAME).append(" (");
        sb.append(ChatDatabase._ID).append(" INTEGER PRIMARY KEY AUTOINCREMENT ,");
//        sb.append(ChatDatabase.TIME).append(" DATETIME ,");
        sb.append(ChatDatabase.USER_NAME).append(" VARCHAR(20) ,");
        sb.append(ChatDatabase.USER_COMMENTS).append(" VARCHAR(1000) ,");
        sb.append(ChatDatabase.IS_MINE).append(" INTEGER").append(");");
        
        db.execSQL(sb.toString());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + ChatDatabase.TABLE_NAME);
        
        onCreate(db);
    }

}
