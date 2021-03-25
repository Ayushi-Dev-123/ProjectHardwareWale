package com.example.hardwaremall;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.hardwaremall.bean.OrderItems;
import com.example.hardwaremall.adapter.OrderDetailAdapter;
import com.example.hardwaremall.databinding.OrderDetailItemListBinding;
import com.example.hardwaremall.databinding.OrderDetailScreenBinding;

import java.util.ArrayList;

public class OrderDetailsActivity extends AppCompatActivity {
    OrderDetailScreenBinding binding;
    OrderDetailItemListBinding itemBinding;
    OrderDetailAdapter adapter;
    ArrayList<OrderItems> items;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = OrderDetailScreenBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        Intent in = getIntent();
        items = (ArrayList<OrderItems>) in.getSerializableExtra("item");

        for (OrderItems o : items) {
            Log.e("items : ", "==>" + o);
            adapter = new OrderDetailAdapter(this, items);
            binding.rvOrderDetails.setAdapter(adapter);
            binding.rvOrderDetails.setLayoutManager(new LinearLayoutManager(this));
        }

        /*binding.btnbuy.setVisibility(View.VISIBLE);
        binding.btnbuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent in = new Intent(OrderDetailsActivity.this,ReOrderActivity.class);
                in.putExtra("items",items);
                startActivity(in);
            }
        });
*/
        binding.backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}