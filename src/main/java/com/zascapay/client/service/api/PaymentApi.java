package com.zascapay.client.service.api;

import com.zascapay.client.service.dto.request.PaymentRequest;
import com.zascapay.client.service.dto.response.PaymentResponse;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface PaymentApi {
    @POST("/api/payments/")
    Call<PaymentResponse> makePayment(@Body PaymentRequest paymentRequest);

    // raw body variant for manual JSON serialization (fallback)
    @POST("/api/payments/")
    Call<ResponseBody> makePaymentRaw(@Body RequestBody body);

    @GET("/api/payments/{id}/")
    Call<PaymentResponse> getPayment(@Path("id") int id);
}
