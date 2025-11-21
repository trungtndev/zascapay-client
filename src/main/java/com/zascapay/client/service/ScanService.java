package com.zascapay.client.service;

import com.zascapay.client.service.api.ScanApi;
import com.zascapay.client.service.dto.response.ScanResponse;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.File;

public class ScanService {
    private final ScanApi scanApi;

    public ScanService() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(System.out::println);
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(chain -> {
                    Request orig = chain.request();
                    Request.Builder rb = orig.newBuilder();
                    String token = ApiConfig.TOKEN_API;
                    if (token != null && !token.isEmpty() && !"REPLACE_WITH_YOUR_TOKEN".equals(token)) {
                        rb.header("Authorization", "Token " + token);
                    }
                    rb.header("Accept", "application/json");
                    return chain.proceed(rb.build());
                })
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ApiConfig.BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        scanApi = retrofit.create(ScanApi.class);
    }

    public ScanResponse scan(File file) throws Exception {
        RequestBody requestFile =
                RequestBody.create(file, MediaType.parse("image/*"));

        MultipartBody.Part body =
                MultipartBody.Part.createFormData("file", file.getName(), requestFile);

        Call<ScanResponse> call = scanApi.scanImage(body);

        return call.execute().body();
    }
}
