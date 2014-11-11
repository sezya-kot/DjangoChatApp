package com.sezyakot.DjangoChat.adapter;


import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import com.sezyakot.DjangoChat.HashTagController;
import com.sezyakot.DjangoChat.R;
import com.sezyakot.DjangoChat.db.ChatDatabase;

public class ChatCursorAdapter extends CursorAdapter {

    public static final int TYPE_ME = 100001;
    public static final int TYPE_SERVER = 100000;
    
    private int viewType;
    private LayoutInflater mInflater;
    
    public ChatCursorAdapter(Context context, Cursor cursor, int flag) {
        super(context, cursor, flag);
        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }


    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {
        return mInflater.inflate(R.layout.my_side, viewGroup, false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        viewType = cursor.getInt(cursor.getColumnIndex(ChatDatabase.IS_MINE));
        TextView content = (TextView) view.findViewById(R.id.tv_my_talk);
        content.setText(HashTagController.check(cursor.getString(cursor.getColumnIndex(ChatDatabase.USER_COMMENTS))));
        if (viewType == TYPE_ME){
            content.setBackgroundResource(R.drawable.balloon_right);
            content.setPadding(30, 0, 45, 0);
        } else {
            content.setBackgroundResource(R.drawable.balloon_left);
            content.setPadding(45, 0, 30, 0);
        }

    }


    
}
