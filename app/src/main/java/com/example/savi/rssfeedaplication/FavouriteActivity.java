package com.example.savi.rssfeedaplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class FavouriteActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favourite);

        String favListString = getSharedPreferences("favorite",Context.MODE_PRIVATE).getString("favoritelist","");
        List<Article> articleList = new Gson().fromJson(favListString,new TypeToken<List<Article>>(){}.getType());

        ListView listView = (ListView) findViewById(R.id.list_view_favorite);
        CustomAdapter customAdapter = new CustomAdapter(this, articleList);
        listView.setAdapter(customAdapter);
    }
}
