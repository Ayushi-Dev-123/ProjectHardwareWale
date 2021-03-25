package com.example.hardwaremall.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hardwaremall.bean.OrderItems;
import com.example.hardwaremall.R;
import com.example.hardwaremall.databinding.OrderDetailItemListBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class OrderDetailAdapter extends RecyclerView.Adapter<OrderDetailAdapter.OrderDetailViewHolder> {
    ArrayList<OrderItems> itemList;
    Context context;

    public OrderDetailAdapter(Context context, ArrayList<OrderItems> itemList) {
        this.context = context;
        this.itemList = itemList;
    }

    @NonNull
    @Override
    public OrderDetailViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        OrderDetailItemListBinding binding = OrderDetailItemListBinding.inflate(LayoutInflater.from(context), parent, false);
        return new OrderDetailViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderDetailViewHolder holder, int position) {
        OrderItems item = itemList.get(position);
        holder.binding.tvProductName.setText("" + item.getName());
        holder.binding.tvProductName.setTextColor(context.getResources().getColor(R.color.black));
        holder.binding.tvProductPrice.setText("₹ " + item.getPrice());
        //holder.binding.tvProductPrice.setTextColor(context.getResources().getColor(R.color.black));
        holder.binding.tvProductQty.setText("Qty : " + item.getQty());
        holder.binding.tvProductQty.setTextColor(context.getResources().getColor(R.color.black));
        Picasso.get().load(item.getImageUrl()).placeholder(R.drawable.default_photo_icon).into(holder.binding.ivProductImage);
    }

    @Override
    public int getItemCount() {
        return itemList.size();
    }

    public class OrderDetailViewHolder extends RecyclerView.ViewHolder {
        OrderDetailItemListBinding binding;

        public OrderDetailViewHolder(OrderDetailItemListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
