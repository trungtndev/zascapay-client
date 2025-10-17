package com.zascapay.client.service;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.zascapay.client.service.api.OrderApi;
import com.zascapay.client.service.dto.request.OrderRequest;
import com.zascapay.client.service.dto.response.OrderResponse;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.logging.Level;
import java.util.logging.Logger;

public class OrderService {
    private static final Logger LOGGER = Logger.getLogger(OrderService.class.getName());
    private final OrderApi orderApi;

    public OrderService() {
        // add HTTP logging for debugging request/response bodies
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor(System.out::println);
        logging.setLevel(HttpLoggingInterceptor.Level.BODY);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(logging)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://localhost:8888")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        orderApi = retrofit.create(OrderApi.class);
    }

    /**
     * Place order synchronously using Retrofit/Gson converter. Returns OrderResponse on success, null on failure.
     * If the deserialized response is missing a valid order_id, attempt to parse the raw JSON and extract
     * `order_id` or `id` as a fallback.
     */
    public OrderResponse placeOrder(OrderRequest req) throws Exception {
        Call<OrderResponse> call = orderApi.placeOrder(req);
        Response<OrderResponse> resp = call.execute();
        if (resp.isSuccessful()) {
            OrderResponse body = resp.body();
            if (body != null && body.getOrderId() > 0) {
                LOGGER.info("OrderService: placed order, orderId=" + body.getOrderId());
                return body;
            }

            // Fallback: try to read raw response body from the underlying okhttp response and parse order_id/id
            try {
                String raw = null;
                try {
                    try (ResponseBody rb = resp.raw().body()) {
                        if (rb != null) raw = rb.string();
                    }
                } catch (Exception e) {
                    LOGGER.log(Level.FINE, "Unable to read raw okhttp response body", e);
                }

                if (raw != null) {
                    LOGGER.warning("OrderService: raw response body when order deserialized empty or orderId<=0: " + raw);
                    try {
                        JsonParser parser = new JsonParser();
                        JsonElement je = parser.parse(raw);
                        if (je != null && je.isJsonObject()) {
                            JsonObject jo = je.getAsJsonObject();
                            int oid = 0;
                            if (jo.has("order_id") && !jo.get("order_id").isJsonNull()) {
                                oid = jo.get("order_id").getAsInt();
                            } else if (jo.has("id") && !jo.get("id").isJsonNull()) {
                                oid = jo.get("id").getAsInt();
                            }
                            if (oid > 0) {
                                OrderResponse fallback = new OrderResponse(oid);
                                LOGGER.info("OrderService: extracted order_id from raw JSON: " + oid);
                                return fallback;
                            }
                        }
                    } catch (Exception e) {
                        LOGGER.log(Level.FINE, "Failed to parse raw JSON for order_id", e);
                    }
                }
            } catch (Exception ex) {
                LOGGER.log(Level.FINE, "Fallback parsing of order response failed", ex);
            }

            LOGGER.warning("OrderService: placeOrder returned no valid orderId. Deserialized body=" + body);
            return body; // may be null or with orderId==0
        } else {
            // log error body
            String err = "";
            try (ResponseBody eb = resp.errorBody()) {
                if (eb != null) err = eb.string();
            } catch (Exception e) {
                LOGGER.log(Level.FINE, "Failed to read order error body", e);
            }
            LOGGER.log(Level.WARNING, "OrderService: placeOrder failed HTTP {0} body={1}", new Object[]{resp.code(), err});
        }
        return null;
    }
}
