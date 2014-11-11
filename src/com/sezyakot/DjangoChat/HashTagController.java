package com.sezyakot.DjangoChat;

import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.BackgroundColorSpan;
import android.text.style.ForegroundColorSpan;
import android.util.Log;
import com.sezyakot.DjangoChat.system.Debug;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Android on 11.11.2014.
 */
public class HashTagController {
    private static final String TAG = HashTagController.class.getSimpleName();
    private String mMsg;

    public static SpannableStringBuilder check(String msg) {
        if (Debug.MODE) Log.d(TAG, "check() start!");

        Pattern p = Pattern.compile("#[\\w]+");
        Matcher m = p.matcher(msg);
        SpannableStringBuilder newMsg = new SpannableStringBuilder(msg);
        int count = 0;
            while (m.find()) {
                count++;
                if (Debug.MODE) Log.d(TAG, "found: " + count + " : "
                        + m.start() + " - " + m.end());
                newMsg.setSpan(new ForegroundColorSpan(Color.RED), m.start(),m.end(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
               // if (Debug.MODE) Log.d(TAG, "group: " + m.group());
            }
        return newMsg;
    }

    public String getMsg() {
        return mMsg;
    }

    public void setMsg(String pMsg) {
        mMsg = pMsg;
    }
}
