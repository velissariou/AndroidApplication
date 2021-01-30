package com.petros.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ViewPostsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_posts);

        ListView listView =  findViewById(R.id.postsListView);
        Intent intent = getIntent();
        ArrayList<Post> posts = intent.getParcelableArrayListExtra(TwitterCalls.SEARCH_TWEETS_CALL);
        Collections.shuffle(posts);
        PostsAdapter postsAdapter = new PostsAdapter(this, R.layout.post_item, posts);
        postsAdapter.setPosts(posts);
        listView.setAdapter(postsAdapter);

    }
}