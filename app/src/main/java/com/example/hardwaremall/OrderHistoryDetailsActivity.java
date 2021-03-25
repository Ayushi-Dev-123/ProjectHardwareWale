package com.example.hardwaremall;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.hardwaremall.bean.OrderItems;
import com.example.hardwaremall.adapter.OrderDetailAddCommentAdapter;
import com.example.hardwaremall.databinding.OrderDetailScreenBinding;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderHistoryDetailsActivity extends AppCompatActivity {
    OrderDetailScreenBinding binding;
    OrderDetailAddCommentAdapter adapter;
    ArrayList<OrderItems> items;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = OrderDetailScreenBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        Intent in = getIntent();
        items = (ArrayList<OrderItems>) in.getSerializableExtra("item");
        String shippingStatus = in.getStringExtra("shippingStatus");

        for (OrderItems o : items) {
            adapter = new OrderDetailAddCommentAdapter(this, items, shippingStatus);
            binding.rvOrderDetails.setAdapter(adapter);
            binding.rvOrderDetails.setLayoutManager(new LinearLayoutManager(this));
        }

        binding.backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        /*binding.btnbuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(OrderHistoryDetailsActivity.this,ReOrderActivity.class);
                in.putExtra("items",items);
                startActivity(in);
            }
        });*/
    }

   /* private void reorder() {
        if (connectivity.isConnectedToInternet(this)) {
            for (String id : idList) {
                ProductService.ProductApi api = ProductService.getProductApiInstance();
                Call<Product> call = api.viewProduct(id);
                call.enqueue(new Callback<Product>() {
                    @Override
                    public void onResponse(Call<Product> call, Response<Product> response) {

                    }

                    @Override
                    public void onFailure(Call<Product> call, Throwable t) {

                    }
                });
            }
        }
    }*/
}