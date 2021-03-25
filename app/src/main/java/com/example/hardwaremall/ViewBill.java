package com.example.hardwaremall;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hardwaremall.bean.Cart;
import com.example.hardwaremall.bean.OrderItems;
import com.example.hardwaremall.adapter.ViewBillAdapter;
import com.example.hardwaremall.databinding.BillBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

public class ViewBill extends BottomSheetDialogFragment {
    BillBinding binding;
    ViewBillAdapter billAdapter;
    ArrayList<Cart> cart;
    ArrayList<OrderItems>items;

    public ViewBill() {
    }

    public ViewBill(ArrayList<Cart> cart) {
        this.cart = cart;
    }
    /*
    public ViewBill(ArrayList<OrderItems>items){
        this.items = items;
    }
    */

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = BillBinding.inflate(LayoutInflater.from(getActivity()));
        View v = inflater.inflate(R.layout.bill,container,false);
        RecyclerView rvBill = v.findViewById(R.id.rvBill);
        billAdapter = new ViewBillAdapter(getContext(),cart);
        rvBill.setAdapter(billAdapter);
        rvBill.setLayoutManager(new LinearLayoutManager(getContext()));
        return v;
    }
}
