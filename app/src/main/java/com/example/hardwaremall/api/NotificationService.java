package com.example.hardwaremall.api;

import com.example.hardwaremall.bean.MyResponse;
import com.example.hardwaremall.bean.NotificationSender;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public class NotificationService {

    public static String API_URL = "https://fcm.googleapis.com/";
    public static NotificationApi notificationApi;

    public static NotificationApi getNotificationInstance() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        if (notificationApi == null) {
            notificationApi = retrofit.create(NotificationApi.class);
        }
        return notificationApi;
    }

    public interface NotificationApi {
        @Headers(
                {
                        "Content-Type:application/json",
                        "Authorization:key=AAAAXXkBES8:APA91bFdWTiLI42FIS8Mp6CekHxJDYMuAASGv48ZapQueqUGTrefImQ0vwqNOzpXDxohkrbXMiODrz75eklgN7mSnmr3pwwMKX6gvnvGgC_enSDCGKP9wZrwTO8YhUeCIrs_X_rmJk30"
                }
        )
        @POST("fcm/send")
        public Call<MyResponse> sendNotification(@Body NotificationSender body);
    }
}
