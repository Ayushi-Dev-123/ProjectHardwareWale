package com.example.hardwaremall;

import android.content.Intent;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.hardwaremall.api.CartService;
import com.example.hardwaremall.api.NotificationService;
import com.example.hardwaremall.api.OrderService;
import com.example.hardwaremall.api.ShopkeeperService;
import com.example.hardwaremall.api.UserService;
import com.example.hardwaremall.bean.Cart;
import com.example.hardwaremall.bean.Data;
import com.example.hardwaremall.bean.MyResponse;
import com.example.hardwaremall.bean.NotificationSender;
import com.example.hardwaremall.bean.OrderCart;
import com.example.hardwaremall.bean.Shopkeeper;
import com.example.hardwaremall.bean.User;
import com.example.hardwaremall.databinding.DeliveryDetailBinding;
import com.example.hardwaremall.utility.InternetConnectivity;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaceOrderActivity<list> extends AppCompatActivity {
    DeliveryDetailBinding binding;
    List<Cart> cartList;
    User user;
    ArrayList<String> deliveryOptions, paymentMode, tokenList = null;
    String date, deliveryOption, paymentOption, userId, userName, userAddress, userMobile, userEmail;
    long timestamp;
    double total;
    int flag = 0;
    InternetConnectivity connectivity;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DeliveryDetailBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());
        final Intent in = getIntent();
        cartList = (List<Cart>) in.getSerializableExtra("updatedCartList");
        total = in.getDoubleExtra("total", 0.0);
        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        binding.backPress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        createTokenList();

        for (Cart c : cartList) {
            c.setTotal((c.getQty() * c.getPrice()));
        }

        Calendar cdate = Calendar.getInstance();
        SimpleDateFormat sd = new SimpleDateFormat("dd/MM/yyyy");
        date = sd.format(cdate.getTime());
        timestamp = Calendar.getInstance().getTimeInMillis();

        UserService.UserApi userApi = UserService.getUserApiInstance();
        Call<User> call = userApi.getUserDetails(userId);
        call.enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                if (response.isSuccessful()) {
                    user = response.body();
                    userName = user.getName();
                    userMobile = user.getMobile();
                    userAddress = user.getAddress();
                    userEmail = user.getEmail();

                    binding.tvEmail.setText("" + user.getEmail());
                    binding.tvAddress.setText("" + user.getAddress());
                    binding.tvName.setText("" + user.getName());
                    binding.tvContact.setText("" + user.getMobile());
                }
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {

            }
        });

        SpannableString content = new SpannableString("Change Details");
        content.setSpan(new UnderlineSpan(),0,content.length(),0);
        binding.tvChangeDetails.setText(content);

        binding.tvChangeDetails.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final AlertDialog ab = new AlertDialog.Builder(PlaceOrderActivity.this).create();
                LayoutInflater inflater = getLayoutInflater();
                final View view = inflater.inflate(R.layout.change_details, null);
                ab.setView(view);
                final EditText etName = view.findViewById(R.id.etName);
                final EditText etAddress = view.findViewById(R.id.etAddress);
                final EditText etEmail = view.findViewById(R.id.etEmail);
                final EditText etMobile = view.findViewById(R.id.etMobile);

                etAddress.setText("" + userAddress);
                etEmail.setText("" + userEmail);
                etMobile.setText("" + userMobile);
                etName.setText("" + userName);

                CardView btnChange = view.findViewById(R.id.btnChangedetails);
                ImageView ivCancel = view.findViewById(R.id.ivCancel);
                btnChange.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        userMobile = etEmail.getText().toString();
                        userName = etName.getText().toString();
                        userAddress = etAddress.getText().toString();
                        userMobile = etMobile.getText().toString();

                        binding.tvEmail.setText("" + userEmail);
                        binding.tvAddress.setText("" + userAddress);
                        binding.tvName.setText("" + userName);
                        binding.tvContact.setText("" + userMobile);
                        ab.dismiss();
                    }
                });
                ivCancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ab.dismiss();
                    }
                });
                ab.show();
            }
        });

        deliveryOptions = new ArrayList<>();
        deliveryOptions.add("Fast");
        deliveryOptions.add("Regular");
        ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, deliveryOptions);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.deliveryOption.setAdapter(arrayAdapter);
        binding.deliveryOption.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                deliveryOption = parent.getItemAtPosition(position).toString();
                if (deliveryOption.equals("Fast")) {
                    binding.tvDeliveryOption.setText("Delivered within 2 days & charges = ??? 100");
                    flag = 1;
                    if (flag == 1) {
                        total = total + 100;
                    }
                    binding.tvTotal.setText("??? " + total);
                } else {
                    binding.tvDeliveryOption.setText("Delivered within 5 days");
                    if (flag == 1) {
                        total = total - 100;
                    }
                    binding.tvTotal.setText("??? " + total);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        paymentMode = new ArrayList<>();
        paymentMode.add("Cash on delivery");
        paymentMode.add("Other");
        ArrayAdapter<String> arrayAdapter1 = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, paymentMode);
        arrayAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.paymentOption.setAdapter(arrayAdapter1);
        binding.paymentOption.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                paymentOption = parent.getItemAtPosition(position).toString();
                if (paymentOption.equals("Cash on delivery ")) {

                } else if (paymentOption.equals("Other")) {
                    Intent intent2 = new Intent(PlaceOrderActivity.this, PaymentGateWay.class);
                    startActivity(intent2);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        binding.btnPalceOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OrderCart orderCart = new OrderCart(userId, userName, date, userAddress, total, userMobile,
                        deliveryOption, "Onway", paymentOption, cartList, timestamp);
                OrderService.OrderApi orderApi = OrderService.getOrderApiInstance();
                Call<OrderCart> call = orderApi.placeCartOrder(orderCart);
                call.enqueue(new Callback<OrderCart>() {
                    @Override
                    public void onResponse(Call<OrderCart> call, Response<OrderCart> response) {
                        if (response.isSuccessful()) {
                            OrderCart o = response.body();
                            sendNotification();
                            final AlertDialog ab = new AlertDialog.Builder(PlaceOrderActivity.this).create();
                            LayoutInflater inflater = getLayoutInflater();
                            final View view = inflater.inflate(R.layout.order_succes_layout, null);
                            ab.setView(view);
                            CardView btnContinueShopping = view.findViewById(R.id.btnContinueShopping);
                            btnContinueShopping.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    ab.dismiss();
                                    Intent in = new Intent(PlaceOrderActivity.this, HomeActivity.class);
                                    in.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(in);
                                    clearCart();
                                }
                            });
                            ab.show();
                        }
                    }

                    @Override
                    public void onFailure(Call<OrderCart> call, Throwable t) {
                        Log.e("Error", "==>" + t);
                        Toast.makeText(PlaceOrderActivity.this, "" + t, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void clearCart() {
        for (Cart c : cartList) {
            CartService.CartApi cartApi = CartService.getCartApiInstance();
            Call<Cart> call = cartApi.removeProductFormCart(c.getCartId());
            Log.e("cart id ", "===>" + c.getCartId());
            call.enqueue(new Callback<Cart>() {
                @Override
                public void onResponse(Call<Cart> call, Response<Cart> response) {
                    Log.e("data", "========>" + response);
                    if (response.code() == 200) {
                        Cart c = response.body();
                    }
                }

                @Override
                public void onFailure(Call<Cart> call, Throwable t) {
                    Log.e("failed", "=========>" + t);
                }
            });
        }
    }

    private void createTokenList() {
        for (Cart cart : cartList) {
            try {
                ShopkeeperService.ShopkeeperApi shopkeeperApi = ShopkeeperService.getShopkeeperApiInstance();
                Call<Shopkeeper> shopkeeperCall = shopkeeperApi.viewShopkeeper(cart.getShopkeeperId());
                shopkeeperCall.enqueue(new Callback<Shopkeeper>() {
                    @Override
                    public void onResponse(Call<Shopkeeper> call, Response<Shopkeeper> response) {
                        if (response.isSuccessful()) {
                            Shopkeeper shopkeeper = response.body();
                            Log.e("shopkeeper token ", "====>" + shopkeeper.getToken());
                            String tokenOfShopkeeper = shopkeeper.getToken();
                            tokenList = new ArrayList<>();
                            tokenList.add(tokenOfShopkeeper);

                        } else
                            Log.e("Response Failure", "=====> adding token in arraylist");
                    }

                    @Override
                    public void onFailure(Call<Shopkeeper> call, Throwable t) {

                    }
                });
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("Error", "==>" + e);
            }
        }
    }

    private void sendNotification() {
        for (String shopkeeperToken : tokenList) {
            Data data = new Data();
            data.setTitle("Order received ");
            data.setMessage("Congratulation !! you have a new order");

            NotificationSender sender = new NotificationSender(shopkeeperToken, data);
            NotificationService.NotificationApi obj = NotificationService.getNotificationInstance();
            Call<MyResponse> call1 = obj.sendNotification(sender);
            call1.enqueue(new Callback<MyResponse>() {
                @Override
                public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                    if (response.code() == 200) {
                        Log.e("**", "1 in onresponse");
                        if (response.body().success != 1) {
                            Toast.makeText(PlaceOrderActivity.this, "API failure !!", Toast.LENGTH_SHORT).show();
                        } else
                            Toast.makeText(PlaceOrderActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    } else {
                        Log.e("response body", "" + response.body());
                    }
                }

                @Override
                public void onFailure(Call<MyResponse> call, Throwable t) {
                    Toast.makeText(PlaceOrderActivity.this, "FAILED", Toast.LENGTH_SHORT).show();
                }
            });//api call end
        }
    }
}