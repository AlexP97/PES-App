package com.example.daniel.assistme;

import android.content.Context;
import android.content.Intent;
import android.database.DataSetObserver;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import twitter4j.Twitter;
import twitter4j.TwitterException;


public class NewsActivity extends AppCompatActivity {
    Context context;
    Twitter twitter;
    twitter4j.User user;
    ListView recyclerView;
    NewAdapter newAdapter;
    ArrayList<New> newsList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        context = getApplicationContext();

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_news);

        TwitterConfig twitterConfig = new TwitterConfig();
        twitterConfig.configTwitter();
        twitter = twitterConfig.getTwitterInstance();
        AsyncTest asyncTest = new AsyncTest();
        asyncTest.execute();

        recyclerView = (ListView) findViewById(R.id.recyclerView1);

        newAdapter = new NewAdapter(this, newsList);

        recyclerView.setAdapter(newAdapter);

        recyclerView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final int pos = position;

                startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(newsList.get(pos).getUrl())));
            }
        });
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
            for(int i = 0; i < 20; i++){
                final twitter4j.Status status = last_tweets.get(i);
                String statusText = status.getText();
                int startLink = statusText.indexOf("https");
                String text = "";
                New news = null;
                if (startLink > 0){
                    text = statusText.substring(0, startLink - 1);
                }
                else{
                   text = statusText;
                }
                String link = "https://twitter.com/CEARefugio/status/" + status.getId();
                news = new New(text, link);
                newsList.add(news);
                newAdapter.addAll(news);
            }
        }
    }
}
