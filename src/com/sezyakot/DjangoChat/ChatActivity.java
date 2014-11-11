package com.sezyakot.DjangoChat;

import android.annotation.TargetApi;
import android.support.v4.app.FragmentManager;
import android.os.*;
import android.widget.AbsListView;



import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.ContentObserver;
import android.database.Cursor;
import android.util.Log;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ListView;
import com.sezyakot.DjangoChat.adapter.ChatCursorAdapter;
import com.sezyakot.DjangoChat.data.ChattingData;
import com.sezyakot.DjangoChat.db.ChatDatabase;
import com.sezyakot.DjangoChat.system.Debug;

import java.util.Random;

public class ChatActivity extends Activity implements AbsListView.OnScrollListener, TaskFragment.TaskCallbacks{

    private static final String TEMP = "..";
    public static final String TAG = ChatActivity.class.getSimpleName();
    public static final String TAG_TASK_FRAGMENT = "server_imitation";

    private EditText mEtInput;
    
    private Cursor mChatCursor;
    private ChatCursorAdapter mChatCursorAdapter;
    
    private ContentResolver mContentResolver;
    private ChatContentObserver mContentObserver;
    
    private ListView mLvChatting;

    private boolean isLastItemVisible;
    private TaskFragment mTaskFragment;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chat);
        
        mLvChatting = (ListView) findViewById(R.id.lv_chat);

        mContentResolver = getContentResolver();

        mEtInput = (EditText) findViewById(R.id.et_input);

        mChatCursor = mContentResolver.query(ChatDatabase.CONTENT_URI, null, null, null, null);

        mChatCursorAdapter = new ChatCursorAdapter(this, mChatCursor, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        mLvChatting.setOnScrollListener(this);
        //
        mLvChatting.setAdapter(mChatCursorAdapter);

        // content observer
        mContentObserver = new ChatContentObserver(mHandler);
        mContentResolver.registerContentObserver(ChatDatabase.CONTENT_URI, true, mContentObserver);

        if (Debug.MODE) Log.d(TAG, "onCreate in ChatActivity");
    } // end of onCreate method


    @Override
    protected void onResume() {
        super.onResume();
        scrollMyListViewToBottom();
    }
    
    @Override
    protected void onPause() {
        super.onPause();
        
    }
    
    @Override
    protected void onDestroy() {
        // here
        mChatCursor.close();
        super.onDestroy();
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (Debug.MODE) Log.d("kot", "scrollState: " + scrollState);
    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
        if (totalItemCount - visibleItemCount == firstVisibleItem) {
            if (Debug.MODE) Log.d(TAG, "last item visible");
            isLastItemVisible = true;
        } else {
            if (Debug.MODE) Log.d(TAG, "last item invisible");
            isLastItemVisible = false;
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_send:
                sendChattingMessage(ChatCursorAdapter.TYPE_ME, "ME", mEtInput.getText().toString());
                mEtInput.setText("");
                if (Debug.MODE) Log.d(TAG, "onClick in ChatActivity");
                
                android.app.FragmentManager fm = getFragmentManager();
                mTaskFragment = (TaskFragment) fm.findFragmentByTag(TAG_TASK_FRAGMENT);

                // If the Fragment is non-null, then it is currently being
                // retained across a configuration change.
                if (mTaskFragment == null) {
                    mTaskFragment = new TaskFragment();
                    fm.beginTransaction().add(mTaskFragment, TAG_TASK_FRAGMENT).commit();
                }
                break;
        }
    }

    private void sendChattingMessage(int chatType, String name, String message) {
        ChattingData chatting = new ChattingData();

        if (Debug.MODE) Log.d(TAG, "sendChattingMessage in ChatActivity");

            chatting.setComment(message);
            chatting.setImageUrl(null);
            chatting.setName(name);
            chatting.setChatType(chatType);

        if (Debug.MODE) Log.d(TAG, "send my ChattingMessage in ChatActivity");

        insertMessage(chatting);
    }
    
    private void scrollMyListViewToBottom() {
        mLvChatting.post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                mLvChatting.setSelection(mChatCursorAdapter.getCount());
            }
        });
    }
    
    private void insertMessage(ChattingData chattingData) {
        ContentValues cv = new ContentValues();
            cv.put(ChatDatabase.USER_NAME, chattingData.getName());
            cv.put(ChatDatabase.USER_COMMENTS, chattingData.getComment());
            cv.put(ChatDatabase.IS_MINE, chattingData.getChatType());
        mContentResolver.insert(ChatDatabase.CONTENT_URI, cv);

        if (Debug.MODE) Log.d(TAG, "insertMessage in ChatActivity");
    } // end of insertMessage method
    
    class ChatContentObserver extends ContentObserver {
        
        private Handler innerHandler;
        
        public ChatContentObserver(Handler handler) {
            super(handler);
            innerHandler = handler;
            if (Debug.MODE) Log.d(TAG, "Constructor in ChatContentObserver");
        }
        
        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            // requery
            mChatCursor = mContentResolver.query(ChatDatabase.CONTENT_URI, null, null, null, null);
            innerHandler.sendEmptyMessage(0);
            if (Debug.MODE) Log.d(TAG, "onChange in ChatContentObserver");
        }
    } // end of ChatContentObserver class
    
    private Handler mHandler = new Handler() {
        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    if (Debug.MODE) Log.d(TAG, "handleMessage in Handler");
                    mChatCursorAdapter.changeCursor(mChatCursor);
                    if(isLastItemVisible){
                        scrollMyListViewToBottom();
                    }
                    break;
            }
        }
    }; // end of Handler

    @Override
    public void onPreExecute() {

    }

    @Override
    public void onProgressUpdate() {

        sendChattingMessage(ChatCursorAdapter.TYPE_SERVER, "Oguz", generateServerMsg());
    }

        private static Random generator = new Random();
        private static final String[] chatupLines = {
                "I only have 3 months left to live, Let's have a party in your pants",
                "Me. You. Now.",
                "Want to see my extensive and assorted collection of cables?",
                "Would you touch me so I can tell my friends I've been touched by an angel?",
                "Are you accepting applications for your fan club",
                "I miss my teddy bear. Would you sleep with me?",
                "God was just showing off when He made you",
                "I don't speak in tongues, but I kiss that way!",
                "If you've lost your virginity, can I have the box it came in?",
                "There are 256 bones in your body! #Would you like another",
                "Would you like to #stroke my lucky scrotum?",
                "Is your father a thief? Because someone stole the stars from the sky and put them in your eyes",
                "Sexist? What's wrong with being sexy?",
                "Grr baby, Grr",
                "I can implement sort algorithms #which run in N log N time",
                "It takes 2 to tango, let us therefore copulate",
                "I have a 10 year plan",
                "Get down. Make Love.",
                "J'adore vous, Mon petit poisson",
                "Let us run away together, we'll take the M6 to Reading",
                "You could be my precious croissant",
                "I would eat a kitten #to be with you?",
                "Your smile makes my member weep tears of joy",
                "There is no such thing as a slut",
                "Boogie on my bell",
                "Let me configure #you home network?",
                "I need sudo access to your slash var"
        };
    private String generateServerMsg() {

        String chatupLine = chatupLines[generator.nextInt(chatupLines.length)];
        return chatupLine;
    }

    @Override
    public void onCancelled() {

    }

    @Override
    public void onPostExecute() {

    }
}
