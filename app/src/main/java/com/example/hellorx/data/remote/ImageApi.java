package com.example.hellorx.data.remote;

import com.example.hellorx.model.PixaBay;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface ImageApi {
    @GET("?key=1775395-be0f19e8b9b925a2cdd2b3560&image_type=photo&per_page=3")
    Observable<PixaBay> getPhoto(@Query("q") String keyword);
}
