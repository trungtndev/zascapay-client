package com.zascapay.client.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zascapay.client.service.api.PaymentApi;
import com.zascapay.client.service.dto.request.PaymentRequest;
import com.zascapay.client.service.dto.response.PaymentResponse;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class PaymentClient {
    private final PaymentApi api;
    private final Gson gson;
    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    public PaymentClient(String baseUrl) {
        this.gson = new GsonBuilder().create();

        String resolvedBase = (baseUrl == null || baseUrl.isEmpty()) ? ApiConfig.BASE_URL : baseUrl;

        OkHttpClient client = new OkHttpClient.Builder()
                .callTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(chain -> {
                    Request orig = chain.request();
                    Request.Builder rb = orig.newBuilder();
                    String token = ApiConfig.TOKEN_API;
                    if (token != null && !token.isEmpty() && !"REPLACE_WITH_YOUR_TOKEN".equals(token)) {
                        rb.header("Authorization", "Bearer " + token);
                    }
                    rb.header("Accept", "application/json");
                    return chain.proceed(rb.build());
                })
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(resolvedBase)
                .client(client)
                .build();

        this.api = retrofit.create(PaymentApi.class);
    }

    /**
     * Create a payment for the given order id.
     * @param orderId order id to pay
     * @return parsed PaymentResponse
     * @throws IOException on network or non-success HTTP response
     */
    public PaymentResponse createPayment(int orderId) throws IOException {
        PaymentRequest req = new PaymentRequest(orderId, null, null);
        String json = gson.toJson(req);
        RequestBody body = RequestBody.create(json, JSON);

        Call<ResponseBody> call = api.makePaymentRaw(body);
        Response<ResponseBody> response = call.execute();

        // handle non-successful response and ensure error body is closed
        if (!response.isSuccessful()) {
            String err = "Payment API error: HTTP " + response.code();
            ResponseBody eb = response.errorBody();
            if (eb != null) {
                try (ResponseBody closeable = eb) {
                    String errBody = closeable.string();
                    if (!errBody.isEmpty()) {
                        err += " - " + errBody;
                    }
                }
            }
            throw new IOException(err);
        }

        // read and parse the successful response body safely
        ResponseBody rb = response.body();
        if (rb == null) {
            throw new IOException("Payment API returned empty body");
        }

        try (ResponseBody closeable = rb) {
            String respJson = closeable.string();
            return gson.fromJson(respJson, PaymentResponse.class);
        }
    }
}
