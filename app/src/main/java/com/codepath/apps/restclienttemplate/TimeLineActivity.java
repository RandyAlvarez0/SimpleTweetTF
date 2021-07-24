package com.codepath.apps.restclienttemplate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.os.Bundle;
import android.util.Log;

import com.codepath.apps.restclienttemplate.models.Tweet;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Headers;

public class TimeLineActivity extends AppCompatActivity {
    String TAG = "TimeLineActivity";
    TwitterClient client;
    RecyclerView rvTweets;
    List<Tweet> tweets;
    TweetsAdapter tweetsAdapter;
    SwipeRefreshLayout swipeRefreshLayout;
    EndlessRecyclerViewScrollListener scrollListener;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_line);

        swipeRefreshLayout = findViewById(R.id.swipeContainer);
        swipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);


        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.i(TAG, "fetching new data");
                populateHomeTimeline();
            }
        });



        client = TwitterApp.getRestClient(this);
        rvTweets = findViewById(R.id.rvTweets);
        tweets = new ArrayList<>();
        tweetsAdapter = new TweetsAdapter(this, tweets);
        LinearLayoutManager layout = new LinearLayoutManager(this);
        rvTweets.setLayoutManager(layout);
        rvTweets.setAdapter(tweetsAdapter);

        scrollListener = new EndlessRecyclerViewScrollListener(layout) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                Log.i(TAG, "onLoadMore" + page);
                loadMoreData();
            }
        };
        rvTweets.addOnScrollListener(scrollListener);

        populateHomeTimeline();


    }

    private void loadMoreData() {
        client.getNextPageOfTweets(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.i(TAG, "onSuccess for onLoadMore" + json.toString());
                JSONArray jsonArray = json.jsonArray;
                try {
                    List<Tweet> tweets = Tweet.fromJsonArray(jsonArray);
                    tweetsAdapter.addAll(tweets);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "onFailure for onLoadMore" + throwable);
            }

    }, tweets.get(tweets.size() - 1).maxId);
    }

    private void populateHomeTimeline() {
        client.getHomeTimeline(new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                Log.i(TAG, "onSuccess"+ json.toString());
                JSONArray jsonArray = json.jsonArray;
                try {
                    tweetsAdapter.clear();
                    tweetsAdapter.addAll(Tweet.fromJsonArray(jsonArray));
                    swipeRefreshLayout.setRefreshing(false);
                } catch (JSONException e) {
                    Log.e(TAG, "Json exception", e);
                    e.printStackTrace();
                }


            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e(TAG, "onFailure");

            }
        });
    }

}