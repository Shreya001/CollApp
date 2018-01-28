package com.example.shreya.collapp;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

/**
 * Created by shreya on 28/1/18.
 */

public class HolderMe extends RecyclerView.ViewHolder {

    private TextView chatText;

    public HolderMe(View v) {
        super(v);
        chatText = (TextView) v.findViewById(R.id.tv_chat_text);
    }
    public TextView getChatText() {
        return chatText;
    }

    public void setChatText(TextView chatText) {
        this.chatText = chatText;
    }
}
