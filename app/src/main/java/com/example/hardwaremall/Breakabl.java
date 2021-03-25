package com.example.hardwaremall;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.hardwaremall.api.NotificationService;
import com.example.hardwaremall.bean.Data;
import com.example.hardwaremall.bean.MyResponse;
import com.example.hardwaremall.bean.NotificationSender;
import com.google.firebase.iid.FirebaseInstanceId;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Breakabl extends AppCompatActivity {
    Button btnsendNotif;
    TextView tvTtile, tvMessage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notifier);
        btnsendNotif = findViewById(R.id.sendnotifcation);
        tvTtile = findViewById(R.id.tvTitle);
        tvMessage = findViewById(R.id.tvMessage);
        btnsendNotif.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Data data = new Data();
                data.setTitle("" + tvTtile.getText().toString());
                data.setMessage("" + tvMessage.getText().toString());
                data.setImage("https://homepages.cae.wisc.edu/~ece533/images/airplane.png");
                String to = "eR50yds-QRKgLu4_7_tZoh:APA91bEsiApagBkxU1bbgLFpuxlUlrkVgUQ7PKlt2go3mm-_3caEv2tJs4oc20LpjVvjij_LLy3Ca5Rm6mASeLYnhfxnun0DDU-YbyD6ss0mKr4cO1bulWoqMnv5IziEzqJxo1P9XZVJ";
                String token = FirebaseInstanceId.getInstance().getToken();
                Log.e("token", "==>" + token);
                //String to = "dsC8Q0FI10I:APA91bHrGPsGmGAduD9FnHuafPf7Z_s1EA4vc2i6wOoiV9Xwfunzs6NmYzs1Mpg08goUdNmVxPHBIlBYVOns2YN42hxBdy2mOGHu-rMlixZ3OdHbmeZMK5jMv6oZq3_QTC7KjpYam6L5";

                NotificationSender sender = new NotificationSender(to, data);
                NotificationService.NotificationApi obj = NotificationService.getNotificationInstance();
                Call<MyResponse> call = obj.sendNotification(sender);
                call.enqueue(new Callback<MyResponse>() {
                    @Override
                    public void onResponse(Call<MyResponse> call, Response<MyResponse> response) {
                        if (response.code() == 200) {
                            Log.e("**", "1 in onresponse");
                            if (response.body().success != 1) {
                                Toast.makeText(Breakabl.this, "API failure !!", Toast.LENGTH_SHORT).show();
                            } else
                                Toast.makeText(Breakabl.this, "Success", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.e("response body", "" + response.body());
                            Log.e("response code", "" + response.code());
                        }
                    }

                    @Override
                    public void onFailure(Call<MyResponse> call, Throwable t) {
                        Toast.makeText(Breakabl.this, "FAILED", Toast.LENGTH_SHORT).show();
                    }
                });//api call end
            }
        });
    }
}
