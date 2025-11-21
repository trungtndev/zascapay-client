package com.zascapay.client.service;

import com.zascapay.client.service.api.PaymentApi;
import com.zascapay.client.service.dto.request.PaymentRequest;
import com.zascapay.client.service.dto.response.PaymentResponse;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class PaymentService {
    private static final Logger LOGGER = Logger.getLogger(PaymentService.class.getName());
    private final PaymentApi paymentApi;

    public PaymentService() {
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

        paymentApi = retrofit.create(PaymentApi.class);
    }

    /**
     * Make payment synchronously and return the server's PaymentResponse, or null on failure.
     */
    public PaymentResponse makePayment(PaymentRequest req) throws Exception {
        Call<PaymentResponse> call = paymentApi.makePayment(req);
        Response<PaymentResponse> resp = call.execute();
        if (resp.isSuccessful()) {
            PaymentResponse body = resp.body();
            LOGGER.info("Payment request successful: " + body);
            return body;
        } else {
            String err = "";
            try (ResponseBody eb = resp.errorBody()) {
                if (eb != null) err = eb.string();
            } catch (IOException e) {
                LOGGER.log(Level.FINE, "Failed to read payment error body", e);
            }
            LOGGER.log(Level.WARNING, "Payment request failed: HTTP {0} body={1}", new Object[]{resp.code(), err});
            return null;
        }
    }

    /**
     * Fetch payment by id synchronously.
     */
    public PaymentResponse getPayment(int id) throws Exception {
        Call<PaymentResponse> call = paymentApi.getPayment(id);
        Response<PaymentResponse> resp = call.execute();
        if (resp.isSuccessful()) {
            PaymentResponse body = resp.body();
            LOGGER.info("PaymentService.getPayment successful: " + body);
            return body;
        } else {
            String err = "";
            try (ResponseBody eb = resp.errorBody()) {
                if (eb != null) err = eb.string();
            } catch (IOException e) {
                LOGGER.log(Level.FINE, "Failed to read payment get error body", e);
            }
            LOGGER.log(Level.WARNING, "PaymentService.getPayment failed: HTTP {0} body={1}", new Object[]{resp.code(), err});
            return null;
        }
    }
}
