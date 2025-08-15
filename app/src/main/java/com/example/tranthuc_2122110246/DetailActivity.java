package com.example.tranthuc_2122110246;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;

public class DetailActivity extends AppCompatActivity {

    ImageView imgDetail;
    TextView txtTitle, txtPrice, txtDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        imgDetail = findViewById(R.id.imgDetail);
        txtTitle = findViewById(R.id.txtDetailTitle);
        txtPrice = findViewById(R.id.txtDetailPrice);
        txtDescription = findViewById(R.id.txtDetailDescription);

        String title = getIntent().getStringExtra("title");
        double price = getIntent().getDoubleExtra("price", 0);
        String image = getIntent().getStringExtra("image");
        String description = getIntent().getStringExtra("description");

        txtTitle.setText(title);
        txtPrice.setText("$" + price);
        txtDescription.setText(description);
        Glide.with(this).load(image).into(imgDetail);
    }
}
