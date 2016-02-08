package com.example.hellorx.data;

import com.example.hellorx.data.remote.RestService;
import com.example.hellorx.model.PixaBay;
import com.example.hellorx.model.Quote;
import com.example.hellorx.utils.RxTransformer;

import rx.Observable;

public class DataManager {

    public Observable<Quote> getQuote(String category) {
        return RestService.createQuoteService()
            .getQuote(category)
            .compose(RxTransformer.applyIoSchedulers());
    }

    public Observable<PixaBay> getPhoto(String keyword) {
        return RestService.createImageService()
            .getPhoto(keyword)
            .compose(RxTransformer.applyIoSchedulers());
    }
}
