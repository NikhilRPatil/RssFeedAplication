package com.example.savi.rssfeedaplication;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    final static String urlAddress = "http://bicsl.com/firstwireappblog/feed/";
    List<Article> favoriteList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        String favListString = getSharedPreferences("favorite", Context.MODE_PRIVATE).getString("favoritelist", "");
        favoriteList = new Gson().fromJson(favListString, new TypeToken<List<Article>>() {}.getType());
        if (favListString != null) {
            favoriteList = new ArrayList<>();
               }

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        final ListView lv = (ListView) findViewById(R.id.listView);

        Downloader downloader = new Downloader(MainActivity.this, urlAddress);
        downloader.setOnOperationCompleteListener(new OnOperationCompleteListener() {
            @Override
            public void onOperationComplete(final List<Article> articleList) {

                for (Article article : articleList) {
                    for (Article favArticle : favoriteList) {
                        if (article.equals(favArticle)) {
                            article.setFavorite(true);
                        }
                    }
                }

                final CustomAdapter customAdapter = new CustomAdapter(MainActivity.this, articleList);
                customAdapter.setOnFavoriteClickListener(new CustomAdapter.OnFavoriteItemClickListener() {
                    @Override
                    public void onFavoriteClick(Article article) {
                        if (favoriteList.contains(article)) {
                            favoriteList.remove(article);
                        } else {
                            favoriteList.add(article);
                        }
                    }
                });
                lv.setAdapter(customAdapter);
            }

            @Override
            public void onOperationFailed() {

            }
        });
        downloader.execute();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                savePref();
                Intent intent = new Intent(MainActivity.this, FavouriteActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        savePref();
    }

    public void savePref() {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("favorite", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("favoritelist", new Gson().toJson(favoriteList));
        editor.apply();

    }

}
