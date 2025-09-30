package com.zascapay.client.service.api;

import com.zascapay.client.service.dto.response.ScanResponse;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ScanApi {
    @Multipart
    @POST("/scan")
    Call<ScanResponse> scanImage(@Part MultipartBody.Part file);
}
