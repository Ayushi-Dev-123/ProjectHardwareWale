package    com.example.hardwaremall.adapter;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.hardwaremall.api.ProductService;
import com.example.hardwaremall.bean.OrderItems;
import com.example.hardwaremall.bean.Product;
import com.example.hardwaremall.bean.Reorder;
import com.example.hardwaremall.databinding.AlertDialogBinding;
import com.example.hardwaremall.utility.InternetConnectivity;
import com.example.hardwaremall.R;
import com.example.hardwaremall.databinding.CartItemListBinding;
import com.google.gson.internal.$Gson$Preconditions;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ReOrderAdapter extends RecyclerView.Adapter<ReOrderAdapter.ReOrderViewHolder> {
    private OnRecyclerViewClick listener;
    Context context;
    OrderItems orderItems;
    ArrayList<Reorder> reorderlist;
    TextView tvAmt;
    int qtyInStock;
    double price, total, discount, offerprice;
//    ArrayList<OrderItems> itemList;

    public ReOrderAdapter(Context context, ArrayList<Reorder> reorderlist, TextView tvAmt) {
        this.context = context;
        this.reorderlist = reorderlist;
        this.tvAmt = tvAmt;
//        this.itemList = itemList;
    }

    @NonNull
    @Override
    public ReOrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CartItemListBinding binding = CartItemListBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ReOrderViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull final ReOrderViewHolder holder, final int position) {
        final Reorder reorder = reorderlist.get(position);
        orderItems = reorder.getOrderItems();
//        itemList.add(orderItems);
//        Log.e("adapter size ","====>"+itemList.size());// size =1
        Log.e("SS",""+reorderlist.size());
        qtyInStock = reorder.getQtyInStock();
        holder.binding.tvProductQty.setText("In stock : "+qtyInStock);
        price = reorder.getPrice();
        discount = reorder.getDiscount();
        double dis = price * (discount / 100);
        offerprice = price - dis;

        int qty = 1;
        holder.binding.tvQty.setText("" + qty);
        Picasso.get().load(orderItems.getImageUrl()).placeholder(R.drawable.default_photo_icon).into(holder.binding.productImage);
        holder.binding.tvProductName.setText(""+orderItems.getName());
        holder.binding.tvProductPrice.setText(""+offerprice);

        holder.binding.ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                price = reorder.getPrice();
                discount = reorder.getDiscount();
                double dis = price * (discount / 100);
                double offer = price - dis;
                int qt = reorder.getQtyInStock();

                int q = Integer.parseInt(holder.binding.tvQty.getText().toString());
                if (q < qt) {
                    q++;
                    holder.binding.tvQty.setText("" + q);
                    orderItems.setQty(q);
                    total = Double.parseDouble(tvAmt.getText().toString());
                    total = total + (offer);
                    tvAmt.setText("" + total);
                    orderItems.setTotal(q*offer);
                }
            }
        });

        holder.binding.ivSubrtact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                price = reorder.getPrice();
                discount = reorder.getDiscount();
                double dis = price * (discount / 100);
                double offer = price - dis;

                int q = Integer.parseInt(holder.binding.tvQty.getText().toString());
                if (q > 1) {
                    q--;
                    holder.binding.tvQty.setText("" + q);
                    orderItems.setQty(q);
                    total = Double.parseDouble(tvAmt.getText().toString());
                    total = total - (offer);
                    tvAmt.setText("" + total);
                    orderItems.setTotal(q*offer);
                }
            }
        });

        holder.binding.ivCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog ab = new AlertDialog.Builder(context).create();
                AlertDialogBinding alertDialogBinding = AlertDialogBinding.inflate(LayoutInflater.from(context));
                ab.setView(alertDialogBinding.getRoot());
                alertDialogBinding.btnOkay.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        reorderlist.remove(position);
                        ab.dismiss();
                        notifyDataSetChanged();
                    }
                });
                alertDialogBinding.btnCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ab.dismiss();
                    }
                });
                ab.show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return reorderlist.size();
    }

    public class ReOrderViewHolder extends RecyclerView.ViewHolder {
        CartItemListBinding binding;

        public ReOrderViewHolder(CartItemListBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    //OrderItems c = reorderlist.get(position);
                    if (position != RecyclerView.NO_POSITION && listener != null){}
                        //listener.onItemClick(c, position);
                }
            });
        }
    }

    public interface OnRecyclerViewClick {
        void onItemClick(OrderItems cart, int position);
    }

    public void setOnItemClicklistner(OnRecyclerViewClick listener) {
        this.listener = listener;
    }
}
