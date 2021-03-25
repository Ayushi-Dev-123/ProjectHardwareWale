package com.example.hardwaremall;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;

import com.example.hardwaremall.api.ProductService;
import com.example.hardwaremall.bean.Category;
import com.example.hardwaremall.bean.Product;
import com.example.hardwaremall.adapter.ProductAdapter;
import com.example.hardwaremall.databinding.ProductsScreenBinding;
import com.example.hardwaremall.utility.InternetConnectivity;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductActivity extends AppCompatActivity {
    ProductsScreenBinding binding;
    ProductAdapter adapter;
    Category category;
    String product = "";
    InternetConnectivity connectivity = new InternetConnectivity();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ProductsScreenBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        Intent in = getIntent();
        category = (Category) in.getSerializableExtra("category");
        showProducts();

        backPress();
    }
    public void backPress(){
        binding.backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void showProducts() {
        if (connectivity.isConnectedToInternet(this)) {
            ProductService.ProductApi productApi = ProductService.getProductApiInstance();
            Call<ArrayList<Product>> call = productApi.viewProductByCategory(category.getCategoryId());
            call.enqueue(new Callback<ArrayList<Product>>() {
                @Override
                public void onResponse(Call<ArrayList<Product>> call, Response<ArrayList<Product>> response) {
                    if (response.code() == 200) {
                        ArrayList<Product> productList = response.body();
                        binding.rvProduct.setVisibility(View.VISIBLE);
                        adapter = new ProductAdapter(ProductActivity.this, productList);
                        binding.rvProduct.setAdapter(adapter);
                        binding.rvProduct.setLayoutManager(new GridLayoutManager(ProductActivity.this, 2));

                        adapter.setOnItemClicklistner(new ProductAdapter.OnRecyclerViewClick() {
                            @Override
                            public void onItemClick(Product product, int position) {
                                Intent in = new Intent(ProductActivity.this, ProductDescriptionActivity.class);
                                in.putExtra("product", product);
                                startActivity(in);
                            }
                        });
                    }
                }

                @Override
                public void onFailure(Call<ArrayList<Product>> call, Throwable t) {

                }
            });
        } else
            Toast.makeText(this, "Please check your connection", Toast.LENGTH_SHORT).show();
    }
}
