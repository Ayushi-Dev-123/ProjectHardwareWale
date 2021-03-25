package com.example.hardwaremall;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.hardwaremall.adapter.ReOrderAdapter;
import com.example.hardwaremall.api.OrderService;
import com.example.hardwaremall.bean.Order;
import com.example.hardwaremall.bean.OrderItems;
import com.example.hardwaremall.bean.Reorder;
import com.example.hardwaremall.databinding.BuyScreenBinding;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReOrderActivity extends AppCompatActivity {
    BuyScreenBinding binding;
    Order order;
    int qtyInStock;
    double discount, price, total = 0;
    ReOrderAdapter adapter;
    ArrayList<Reorder> reorder;
    OrderItems items;
    ArrayList<OrderItems>itemList;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = BuyScreenBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        binding.tvAppName.setText("Reorder");

        Intent in = getIntent();
        order = (Order) in.getSerializableExtra("order");

        OrderService.OrderApi api = OrderService.getOrderApiInstance();
        Call<ArrayList<Reorder>> call = api.getQuantityOfOrderItems(order);
        call.enqueue(new Callback<ArrayList<Reorder>>() {
            @Override
            public void onResponse(Call<ArrayList<Reorder>> call, Response<ArrayList<Reorder>> response) {
                Log.e("api response ", "==============>" + response.code());
                if (response.code() == 200) {
                    reorder = response.body();

                    itemList = new ArrayList<>();

                    for (Reorder re : reorder) {
                        qtyInStock = re.getQtyInStock();
                        items = re.getOrderItems();

                        itemList.add(items);
                        price = re.getPrice();
                        discount = re.getDiscount();
                        double dis = price * (discount / 100);
                        double offerPrice = price - dis;
                        total = total + offerPrice;
                        binding.tvAmt.setText("" + total);
                    }


                    for(OrderItems o : itemList)
                        Log.e("items","===>"+o.getProductId());

                    adapter = new ReOrderAdapter(ReOrderActivity.this, reorder, binding.tvAmt);
                    binding.rvBuy.setAdapter(adapter);
                    binding.rvBuy.setLayoutManager(new LinearLayoutManager(ReOrderActivity.this));
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Reorder>> call, Throwable t) {

            }
        });

        binding.ivContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (reorder.size() != 0) {
                    Intent in = new Intent(ReOrderActivity.this, PlaceReorderActivity.class);
                    //in.putExtra("reorder", reorder);
                    in.putExtra("itemList",itemList);
                    //Log.e("size","===->"+itemList.size());
                    String tot = binding.tvAmt.getText().toString();
                    double amount = Double.parseDouble(tot);
                    in.putExtra("amount", amount);
                    startActivity(in);
                }
            }
        });

        binding.backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }
}
