package com.example.shreya.collapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.RecyclerView.ViewHolder;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import java.util.List;

/**
 * Created by shreya on 28/1/18.
 */

public class ConversationRecyclerView extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<ChatData> items;
    private Context mContext;

    private final int BOT = 1, ME = 2;

    public ConversationRecyclerView(Context context, List<ChatData> items) {
        this.mContext = context;
        this.items = items;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        RecyclerView.ViewHolder viewHolder;
        LayoutInflater inflater = LayoutInflater.from(viewGroup.getContext());

        switch (viewType) {
            case BOT:
                View v1 = inflater.inflate(R.layout.bot_chat, viewGroup, false);
                viewHolder = new HolderBot(v1);
                break;
            default:
                View v = inflater.inflate(R.layout.user_chat, viewGroup, false);
                viewHolder = new HolderMe(v);
                break;
        }
        return viewHolder;

    }

    public void addItem(List<ChatData> item) {
        if(items != null ) {
            items.addAll(item);
            notifyDataSetChanged();
        }
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        switch (holder.getItemViewType()) {
            case BOT:
                HolderBot vh1 = (HolderBot) holder;
                configureViewHolderBot(vh1, position);
                break;
            default:
                HolderMe vh = (HolderMe) holder;
                configureViewHolderMe(vh, position);
                break;
        }
    }


    private void configureViewHolderMe(HolderMe vh1, int position) {
        if(items != null )
        vh1.getChatText().setText(items.get(position).getText());
    }

    private void configureViewHolderBot(HolderBot vh1, int position) {
        if (items != null )
            vh1.getChatText().setText(items.get(position).getText());
    }

    @Override
    public int getItemViewType(int position) {
        if (items != null && items.get(position).getType().equals("1")) {
            return BOT;
        } else if (items != null && items.get(position).getType().equals("2")) {
            return ME;
        }
        return -1;
    }

    @Override
    public int getItemCount() {
        if (items == null) {
            return 1;
        } else {
            return this.items.size();
        }
    }
}