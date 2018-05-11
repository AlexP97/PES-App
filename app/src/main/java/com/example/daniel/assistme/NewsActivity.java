package com.example.daniel.assistme;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.List;

import twitter4j.ResponseList;
import twitter4j.Status;
import twitter4j.Twitter;
import twitter4j.TwitterException;


public class NewsActivity extends AppCompatActivity {
    Context context;
    Twitter twitter;
    twitter4j.User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        context = getApplicationContext();

        super.onCreate(savedInstanceState);
        TwitterConfig twitterConfig = new TwitterConfig();
        twitterConfig.configTwitter();
        twitter = twitterConfig.getTwitterInstance();
        setContentView(R.layout.activity_news);
        AsyncTest asyncTest = new AsyncTest();
        asyncTest.execute();
    }
    private class AsyncTest extends AsyncTask<String, Void, List<twitter4j.Status>> {

        @Override
        protected List doInBackground(String... data) {
            List <twitter4j.Status> last_tweets = null;
            try {
                last_tweets = twitter.getUserTimeline("CEARefugio"); //coge los ultimos 20 tweets
            } catch (TwitterException e) {
                e.printStackTrace();
            }
            return last_tweets;
        }

        @Override
        protected void onPostExecute(List<twitter4j.Status> last_tweets) {
            LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View v = inflater.inflate(R.layout.activity_news, null);
            LinearLayout ll = (LinearLayout) v.findViewById(R.id.linearLayout1);
            for(int i = 0; i < 20; i++){
                final twitter4j.Status status = last_tweets.get(i);
                LinearLayout ll2 = new LinearLayout(context);
                ll.setOrientation(LinearLayout.VERTICAL);
                String statusText = status.getText();
                int startLink = statusText.indexOf("https");
                String text = "";
                TextView tv = new TextView(context);
                if (startLink > 0){
                    text = statusText.substring(0, startLink - 1);
                    tv.setText(text);
                }
                else{
                    tv.setText(statusText);
                }
                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        String link = "https://twitter.com/CEARefugio/status/" + status.getId();
                        startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(link)));
                    }
                });
                tv.isClickable();
                tv.setFocusable(true);
                tv.setTextColor(Color.BLACK);
                tv.setBackgroundColor(0xffe6ffff);
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
                params.topMargin = 20;
                params.bottomMargin = 20;
                params.leftMargin = 20;
                params.rightMargin = 20;
                ll2.setLayoutParams(params);
                ll2.addView(tv);
                ll.addView(ll2);
            }
            setContentView(v);
        }
    }
}
