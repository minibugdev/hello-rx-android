package com.example.hellorx.data.remote;

import com.example.hellorx.BuildConfig;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Converter;
import retrofit2.JacksonConverterFactory;
import retrofit2.Retrofit;
import retrofit2.RxJavaCallAdapterFactory;

public class RestService {
    private static final String QUOTE_ENDPOINT = "https://andruxnet-random-famous-quotes.p.mashape.com/";
    private static final String IMAGE_ENDPOINT = "https://pixabay.com/api/";

    private static QuoteApi mQuoteService;
    private static ImageApi mImageService;

    private static OkHttpClient.Builder createOkHttpClient() {
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

    private static Retrofit.Builder createRestClient(OkHttpClient client) {
        ObjectMapper objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Converter.Factory converterFactory = JacksonConverterFactory.create(objectMapper);

        return new Retrofit.Builder()
            .addConverterFactory(converterFactory)
            .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
            .client(client);
    }

    public static QuoteApi createQuoteService() {
        if (mQuoteService == null) {
            OkHttpClient client = createOkHttpClient()
                .addInterceptor(chain -> {
                    Request request = chain.request();
                    Request newReq = request.newBuilder()
                        .addHeader("X-Mashape-Key", "5azvsqCwjxmshc14ooQJ5NTKqSkcp1SVs9Fjsnx7wTE8aikjHD")
                        .addHeader("Accept", "application/json")
                        .build();
                    return chain.proceed(newReq);
                }).build();

            mQuoteService = createRestClient(client)
                .baseUrl(QUOTE_ENDPOINT)
                .build()
                .create(QuoteApi.class);
        }

        return mQuoteService;
    }

    public static ImageApi createImageService() {
        if (mImageService == null) {
            mImageService = createRestClient(createOkHttpClient().build())
                .baseUrl(IMAGE_ENDPOINT)
                .build()
                .create(ImageApi.class);
        }

        return mImageService;
    }
}
