package com.example.hardwaremall;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.hardwaremall.api.OrderService;
import com.example.hardwaremall.bean.Order;
import com.example.hardwaremall.adapter.OrderHistoryAdapter;
import com.example.hardwaremall.databinding.OrderHistoryScreenBinding;
import com.example.hardwaremall.utility.InternetConnectivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderHistoryActivity extends AppCompatActivity {
    OrderHistoryScreenBinding binding;
    OrderHistoryAdapter adapter;
    String userId;
    InternetConnectivity connectivity = new InternetConnectivity();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = OrderHistoryScreenBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        showOrders();

        binding.backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void showOrders() {
        if (connectivity.isConnectedToInternet(this)) {
            OrderService.OrderApi orderApi = OrderService.getOrderApiInstance();
            Call<ArrayList<Order>> call = orderApi.getOrders(userId);
            call.enqueue(new Callback<ArrayList<Order>>() {
                @Override
                public void onResponse(Call<ArrayList<Order>> call, Response<ArrayList<Order>> response) {
                    if (response.code() == 200) {
                        final ArrayList<Order> orderList = response.body();
                        if (orderList.size() == 0) {
                             binding.manageOrderLayout.setVisibility(View.VISIBLE);
                             binding.rvOrderHistory.setVisibility(View.GONE);
                        }
                        adapter = new OrderHistoryAdapter(OrderHistoryActivity.this, orderList);
                        binding.rvOrderHistory.setAdapter(adapter);
                        binding.rvOrderHistory.setLayoutManager(new LinearLayoutManager(OrderHistoryActivity.this));
                    }
                }

                public void onFailure(Call<ArrayList<Order>> call, Throwable t) {
                    Log.e("Error : ", "==> " + t);
                    Toast.makeText(OrderHistoryActivity.this, "Something went wrong", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
}
