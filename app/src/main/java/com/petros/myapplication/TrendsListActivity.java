package com.petros.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.util.ArrayList;

public class TrendsListActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trends_list);

        ListView listView = (ListView) findViewById(R.id.trendsListView);
        Intent intent = getIntent();
        ArrayList<String> trendsList = intent.getStringArrayListExtra(MainActivity.TRENDS_LIST);
        listView.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, trendsList));

    }
}