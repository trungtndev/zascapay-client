package com.zascapay.client.service;

import com.zascapay.client.service.api.ScanApi;
import com.zascapay.client.service.dto.response.ScanResponse;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.File;

public class ScanService {
    private final ScanApi scanApi;

    public ScanService() {
        OkHttpClient client = new OkHttpClient.Builder().build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost:8888") // URL của FastAPI server
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
