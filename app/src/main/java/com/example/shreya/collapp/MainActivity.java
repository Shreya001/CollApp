package com.example.shreya.collapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.github.kittinunf.fuel.Fuel;
import com.github.kittinunf.fuel.core.FuelError;
import com.github.kittinunf.fuel.core.Handler;
import com.github.kittinunf.fuel.core.Request;
import com.github.kittinunf.fuel.core.Response;
import com.ibm.watson.developer_cloud.conversation.v1.ConversationService;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageRequest;
import com.ibm.watson.developer_cloud.conversation.v1.model.MessageResponse;
import com.ibm.watson.developer_cloud.http.ServiceCallback;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private ConversationRecyclerView mAdapter;
    private EditText text;
    private Button send;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ConversationRecyclerView(this,setData());
        mRecyclerView.setAdapter(mAdapter);

        final ConversationService myConversationService =
                new ConversationService(
                        "2018-01-27",
                        getString(R.string.username),
                        getString(R.string.password)
                );
        text = (EditText)findViewById(R.id.et_message);

        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mRecyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mRecyclerView.smoothScrollToPosition(mRecyclerView.getAdapter().getItemCount() - 1);
                    }
                }, 500);
            }
        });
        text.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView tv, int action, KeyEvent keyEvent) {

                if(action == EditorInfo.IME_ACTION_DONE) {
                    final String inputText = text.getText().toString();
                     List<ChatData> data = new ArrayList<ChatData>();
                    ChatData item = new ChatData();
                    item.setType("2");
                    item.setText(inputText);
                    data.add(item);
                    mAdapter.addItem(data);
                    mRecyclerView.smoothScrollToPosition(mRecyclerView.getAdapter().getItemCount() -1);
                    text.setText("");

                    //send request
                    MessageRequest request = new MessageRequest.Builder()
                            .inputText(inputText)
                            .build();

                    myConversationService.message(getString(R.string.workspace), request).enqueue(new ServiceCallback<MessageResponse>() {
                                @Override
                                public void onResponse(MessageResponse response) {
                                    final String outputText = response.getText().get(0);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            List<ChatData> data = new ArrayList<ChatData>();
                                            ChatData item = new ChatData();
                                            item.setType("1");
                                            item.setText(outputText);
                                            data.add(item);
                                            mAdapter.addItem(data);
                                            mRecyclerView.smoothScrollToPosition(mRecyclerView.getAdapter().getItemCount() -1);
                                        }
                                    });

                                    if(response.getIntents().get(0).getIntent()
                                            .endsWith("RequestQuote")) {
                                        String quotesURL =
                                                "https://api.forismatic.com/api/1.0/" +
                                                        "?method=getQuote&format=text&lang=en";

                                        Fuel.get(quotesURL)
                                                .responseString(new Handler<String>() {
                                                    @Override
                                                    public void success(Request request,
                                                            Response response, String quote) {
                                                        List<ChatData> data = new ArrayList<ChatData>();
                                                        ChatData item = new ChatData();
                                                        item.setType("1");
                                                        item.setText(quote);
                                                        data.add(item);
                                                        mAdapter.addItem(data);
                                                        mRecyclerView.smoothScrollToPosition(mRecyclerView.getAdapter().getItemCount() -1);
                                                    }

                                                    @Override
                                                    public void failure(Request request,
                                                            Response response,
                                                            FuelError fuelError) {
                                                    }
                                                });
                                    }
                                }

                                @Override
                                public void onFailure(Exception e) {}
                            });
                }
                return false;
            }
        });


    }

    public List<ChatData> setData(){
        List<ChatData> data = new ArrayList<>();

        String text[] = {"15 September","Hi, Julia! How are you?", "Hi, Joe, looks great! :) ", "I'm fine. Wanna go out somewhere?", "Yes! Coffe maybe?", "Great idea! You can come 9:00 pm? :)))", "Ok!", "Ow my good, this Kit is totally awesome", "Can you provide other kit?", "I don't have much time, :`("};
        String type[] = {"0", "2", "1", "1", "2", "1", "2", "2", "2", "1"};

        for (int i=0; i<text.length; i++){
            ChatData item = new ChatData();
            item.setType(type[i]);
            item.setText(text[i]);
            data.add(item);
        }

        return data;
    }


}
