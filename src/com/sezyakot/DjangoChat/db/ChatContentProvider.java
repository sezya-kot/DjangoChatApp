package com.sezyakot.DjangoChat.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.util.Log;
import com.sezyakot.DjangoChat.system.Debug;

public class ChatContentProvider extends ContentProvider {

    public static final int ALL_RECORD = 100001;
    public static final String TAG = ChatContentProvider.class.getSimpleName();

    private SQLiteDatabase mDB;
    
    static final UriMatcher Matcher;
    static {
        Matcher = new UriMatcher(UriMatcher.NO_MATCH);
        Matcher.addURI(ChatDatabase.AUTHORITY, ChatDatabase.PATH, ALL_RECORD);
    }
    
    private ContentObserver mContentObserver;
    
    @Override
    public boolean onCreate() {
        ChatDBHelper helper = new ChatDBHelper(getContext());
        mDB = helper.getWritableDatabase();
        if (Debug.MODE) Log.d(TAG, "onCreate in CP");
        return (mDB != null);
    }
    
    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        return 0;
    }

    @Override
    public String getType(Uri uri) {
        return null;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {
        long row = mDB.insert(ChatDatabase.TABLE_NAME, null, values);
        if (Debug.MODE) Log.d(TAG, "insert in CP");
        if (row > 0) {
            Uri notiUri = ContentUris.withAppendedId(ChatDatabase.CONTENT_URI, row);
            getContext().getContentResolver().notifyChange(notiUri, mContentObserver);
            if (Debug.MODE) Log.d(TAG, "insert success in CP");
            return notiUri;
        }
        return null;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs,
            String sortOrder) {
        int match = Matcher.match(uri);
        String sql = "SELECT * FROM " + ChatDatabase.TABLE_NAME;
        if (Debug.MODE) Log.d(TAG, "query in CP");
//        if (match == ALL_RECORD) {}
        return mDB.rawQuery(sql, null);
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int count = 0;
        
        if ((Matcher.match(uri)) == ALL_RECORD) {
            count = mDB.update(ChatDatabase.TABLE_NAME, values, selection, selectionArgs);
        }
        if (Debug.MODE) Log.d(TAG, "update " + count + " in CP");
        getContext().getContentResolver().notifyChange(uri, mContentObserver);
        
        return count;
    }

    public void setContentObserver(ContentObserver co) {
        mContentObserver = co;
        if (Debug.MODE) Log.d(TAG, "setContentObserver in CP");
    }
}
