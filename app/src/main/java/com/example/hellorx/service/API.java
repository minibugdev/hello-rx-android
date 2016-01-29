package com.example.hellorx.service;

import com.example.hellorx.BuildConfig;
import com.example.hellorx.model.PixaBay;
import com.example.hellorx.model.Quote;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.JacksonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.RxJavaCallAdapterFactory;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import rx.Observable;

public class API {
    private static final String QUOTE_ENDPOINT = "https://andruxnet-random-famous-quotes.p.mashape.com/";
    private static final String IMAGE_ENDPOINT = "https://pixabay.com/api/";

    private static OkHttpClient.Builder getClient() {
        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        if (BuildConfig.DEBUG) {
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);
        }
        else {
            logging.setLevel(HttpLoggingInterceptor.Level.NONE);
        }
        return new OkHttpClient.Builder()
            .addInterceptor(logging);
    }

    private static Retrofit.Builder getREST(OkHttpClient client) {
        ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Converter.Factory converterFactory = JacksonConverterFactory.create(objectMapper);

        return new Retrofit.Builder()
            .addConverterFactory(converterFactory)
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .client(client);
    }

    public static QuoteApi quote() {
        OkHttpClient client = getClient().addInterceptor(chain -> {
            Request request = chain.request();
            Request newReq = request.newBuilder()
                .addHeader("X-Mashape-Key", "5azvsqCwjxmshc14ooQJ5NTKqSkcp1SVs9Fjsnx7wTE8aikjHD")
                .addHeader("Accept", "application/json")
                .build();
            return chain.proceed(newReq);
        }).build();

        return getREST(client)
            .baseUrl(QUOTE_ENDPOINT)
            .build()
            .create(QuoteApi.class);
    }

    public static ImageApi image() {
        return getREST(getClient().build())
            .baseUrl(IMAGE_ENDPOINT)
            .build()
            .create(ImageApi.class);
    }

    public interface QuoteApi {
        @FormUrlEncoded
        @POST("/")
        Observable<Quote> getQuote(@Field("cat") String category);
    }

    public interface ImageApi {
        @GET("?key=1775395-be0f19e8b9b925a2cdd2b3560&image_type=photo&per_page=3")
        Observable<PixaBay> getPhoto(@Query("q") String keyword);
    }
}
