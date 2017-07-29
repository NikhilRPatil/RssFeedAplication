package com.example.savi.rssfeedaplication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

class CustomAdapter extends BaseAdapter {
    private Context context;
    private List<Article> articles;
    private OnFavoriteItemClickListener onFavoriteItemClickListener;

    public CustomAdapter(Context context, List<Article> articles) {
        this.context = context;
        this.articles = articles;
    }

    public interface OnFavoriteItemClickListener {
        void onFavoriteClick(Article article);
    }

    public void setOnFavoriteClickListener(OnFavoriteItemClickListener listener) {
        onFavoriteItemClickListener = listener;
    }


    @Override
    public int getCount() {
        return articles.size();
    }

    @Override
    public Object getItem(int position) {
        return articles.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.model, parent, false);
        }
        TextView titleTxt = (TextView) convertView.findViewById(R.id.titleTxt);
        TextView dateTxt = (TextView) convertView.findViewById(R.id.dateTxt);
        ImageView img = (ImageView) convertView.findViewById(R.id.articleImage);
        ImageView imageViewFavorite = (ImageView) convertView.findViewById(R.id.imageview_favorite);

        final Article article = (Article) this.getItem(position);
        final String title = article.getTitle();
        final String desc = article.getDescription();
        final String date = article.getDate();
        final String imageUrl = article.getImageUrl();
        final String link = article.getLink();
        titleTxt.setText(title);
        dateTxt.setText(date);

        imageViewFavorite.setBackground(article.isFavorite() ? ContextCompat.getDrawable(context,R.mipmap.ic_favorite_selected) : ContextCompat.getDrawable(context,R.mipmap.ic_favorite_unselected));
        imageViewFavorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onFavoriteItemClickListener != null) {
                    if (articles.get(position).isFavorite()) {
                        article.setFavorite(false);
                    } else {
                        article.setFavorite(true);
                    }
                    onFavoriteItemClickListener.onFavoriteClick(articles.get(position));
                    notifyDataSetChanged();
                }
            }
        });

        PicassoClient.downloadImage(context, imageUrl, img);
        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, DetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("Title", title);
                bundle.putString("Desc", desc);
                bundle.putString("Date", date);
                bundle.putString("imageUrl", imageUrl);
                bundle.putString("link",link);
                intent.putExtras(bundle);
                context.startActivity(intent);
            }
        });
        return convertView;
    }
}
