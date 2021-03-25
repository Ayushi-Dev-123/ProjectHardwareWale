package com.example.hardwaremall.api;

import com.example.hardwaremall.bean.Category;

import java.util.ArrayList;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

import static com.example.hardwaremall.utility.ServerAddress.BASE_URL;

public class CategoryService {
    public static CategoryApi categoryApi;
    public static CategoryApi getCategoryApiInstance(){
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(5000, TimeUnit.SECONDS)
                .readTimeout(5000,TimeUnit.SECONDS).build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_URL).client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        if(categoryApi == null )
            categoryApi = retrofit.create(CategoryApi.class);
    return categoryApi;
    }

    public interface CategoryApi{
        @GET("/category/")
        public Call<ArrayList<Category>> getCategoryList();
    }
}
