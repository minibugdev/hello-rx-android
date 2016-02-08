package com.example.hellorx.data.remote;

import com.example.hellorx.model.Quote;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;
import rx.Observable;

public interface QuoteApi {
    @FormUrlEncoded
    @POST("/")
    Observable<Quote> getQuote(@Field("cat") String category);
}
