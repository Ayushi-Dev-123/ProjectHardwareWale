package com.example.hardwaremall.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hardwaremall.bean.Comment;
import com.example.hardwaremall.R;
import com.example.hardwaremall.databinding.ProductItemListBinding;
import com.example.hardwaremall.databinding.ShowCommentItemListBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Callback;

public class ShowCommentAdapter extends RecyclerView.Adapter<ShowCommentAdapter.ShowCommentViewHolder> {

    Float rate, avgRate = 0f;
    Context context;
    ArrayList<Comment> commentList;

    public ShowCommentAdapter(Context context, ArrayList<Comment> commentList) {
        this.context = context;
        this.commentList = commentList;
    }

    @NonNull
    @Override
    public ShowCommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ShowCommentItemListBinding binding = ShowCommentItemListBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ShowCommentViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ShowCommentViewHolder holder, int position) {
        Comment comment = commentList.get(position);
        holder.binding.tvComment.setText("" + comment.getComment());
        holder.binding.tvUserName.setText("" + comment.getUserName());
        Picasso.get().load(comment.getUserImg()).placeholder(R.drawable.default_photo_icon).into(holder.binding.ivUserImg);
        rate = Float.valueOf(comment.getRating()).floatValue();
        holder.binding.ratingBar.setRating(rate);
        //holder.binding.tvRate.setText("" + rate + " Out of 5 ");
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    public class ShowCommentViewHolder extends RecyclerView.ViewHolder {
        ShowCommentItemListBinding binding;
        public ShowCommentViewHolder(ShowCommentItemListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
