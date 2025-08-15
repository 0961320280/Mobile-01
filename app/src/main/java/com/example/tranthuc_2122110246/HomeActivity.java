package com.example.tranthuc_2122110246;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class HomeActivity extends AppCompatActivity {

    private ViewPager2 bannerViewPager;
    private DotsIndicator dotsIndicator;
    private RecyclerView productRecyclerView;

    private List<Product> productList = new ArrayList<>();
    private ProductAdapter productAdapter;

    private final String PRODUCT_API = "https://fakestoreapi.com/products";  // Thay API sản phẩm thật ở đây

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        bannerViewPager = findViewById(R.id.bannerViewPager);
        dotsIndicator = findViewById(R.id.dotsIndicator);
        productRecyclerView = findViewById(R.id.productRecyclerView);

        // Thiết lập RecyclerView để hiển thị sản phẩm
        productAdapter = new ProductAdapter(this, productList);
        productRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));  // Lưới 2 cột
        productRecyclerView.setAdapter(productAdapter);

        // Load banner (giả lập)
        List<Integer> banners = new ArrayList<>();
        banners.add(R.drawable.banner1);
        banners.add(R.drawable.banner2);
        banners.add(R.drawable.banner3);
        BannerAdapter bannerAdapter = new BannerAdapter(banners);
        bannerViewPager.setAdapter(bannerAdapter);
        dotsIndicator.setViewPager2(bannerViewPager);

        // Load sản phẩm từ API
        loadProductsFromAPI();
    }

    private void loadProductsFromAPI() {
        new Thread(() -> {
            try {
                // Kết nối với API
                URL url = new URL(PRODUCT_API);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                InputStream is = conn.getInputStream();
                Scanner scanner = new Scanner(is).useDelimiter("\\A");
                String response = scanner.hasNext() ? scanner.next() : "";
                Log.d("HomeActivity", "Response: " + response);

                // Kiểm tra phản hồi từ API
                JSONArray products = new JSONArray(response);
                if (products.length() == 0) {
                    runOnUiThread(() -> Toast.makeText(this, "Không có sản phẩm", Toast.LENGTH_SHORT).show());
                    return;
                }

                // Lấy dữ liệu sản phẩm
                for (int i = 0; i < products.length(); i++) {
                    JSONObject obj = products.getJSONObject(i);
                    productList.add(new Product(
                            obj.getString("id"),
                            obj.getString("name"),
                            obj.getString("image"),
                            obj.getDouble("price")
                    ));
                }

                // Làm mới dữ liệu RecyclerView
                runOnUiThread(() -> {
                    productAdapter.notifyDataSetChanged();
                });

                conn.disconnect();
            } catch (Exception e) {
                e.printStackTrace();
                runOnUiThread(() -> Toast.makeText(this, "Lỗi kết nối: " + e.getMessage(), Toast.LENGTH_LONG).show());
            }
        }).start();
    }
}
