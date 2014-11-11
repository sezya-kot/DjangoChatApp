package com.sezyakot.DjangoChat.db;

import android.net.Uri;
import android.provider.BaseColumns;

public class ChatDatabase implements BaseColumns {
    
    // about authority
    public static final String URI_PREFIX = "content://";
    public static final String AUTHORITY = "com.kth.chattingroom";
    public static final Uri AUTHORITY_URI = Uri.parse(URI_PREFIX + AUTHORITY);
    
    // about Database
    public static final String DB_NAME = "chatroom.db";
    public static final int DB_VERSION = 1;
    
    // about Table
    public static final String TABLE_NAME = "chat";
    public static final String PATH = "chat";
    
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + PATH);
    
    // column name
    public static final String USER_NAME = "user_id";       // string
    public static final String USER_COMMENTS = "comments";  // string
    public static final String TIME = "time";               // time
//    public static final String IS_READ = "is_read";         // boolean
    public static final String IS_MINE = "is_mine";          // my comments
    
}
