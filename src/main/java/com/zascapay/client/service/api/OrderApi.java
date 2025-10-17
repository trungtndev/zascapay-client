package com.zascapay.client.service.api;

import com.zascapay.client.service.dto.request.OrderRequest;
import com.zascapay.client.service.dto.response.OrderResponse;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface OrderApi {
    @POST("/api/orders/")
    Call<OrderResponse> placeOrder(@Body OrderRequest orderRequest);

    // raw body variant for manual JSON serialization (fallback)
    @POST("/api/orders/")
    Call<ResponseBody> placeOrderRaw(@Body RequestBody body);
}
