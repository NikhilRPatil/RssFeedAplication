package com.example.savi.rssfeedaplication;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class DetailActivity extends AppCompatActivity {
    ImageView imageViewDetail;
    TextView textViewTitle;
    TextView textViewDate;
    TextView textViewDescription;
    Button buttonShare;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        imageViewDetail = (ImageView) findViewById(R.id.image_view_detail);
        textViewTitle = (TextView) findViewById(R.id.text_view_name_detail);
        textViewDate = (TextView) findViewById(R.id.text_view_date_detail);
        textViewDescription = (TextView) findViewById(R.id.text_view_description_detail);


        final Intent intent = getIntent();
        final String title = intent.getStringExtra("Title");
        final String date = intent.getStringExtra("Date");
        final String description = intent.getStringExtra("Desc");
        final String imageUrl = intent.getStringExtra("imageUrl");
        final String link = intent.getStringExtra("link");
        textViewTitle.setText(title);
        textViewDate.setText(date);
        textViewDescription.setText(Html.fromHtml(description));
        PicassoClient.downloadImage(this, imageUrl, imageViewDetail);

        buttonShare = (Button) findViewById(R.id.button_share);
buttonShare.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        sendIntent.putExtra(Intent.EXTRA_TEXT, link);
        sendIntent.setType("text/plain");
        startActivity(sendIntent);
    }
});

    }
}
