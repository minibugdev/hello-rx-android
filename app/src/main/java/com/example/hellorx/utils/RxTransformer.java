package com.example.hellorx.utils;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by https://github.com/ReactiveX/RxJava/issues/3600
 * Article : http://blog.danlew.net/2015/03/02/dont-break-the-chain/
 */
public class RxTransformer {

    public static <T> Observable.Transformer<T, T> applyIoSchedulers() {
        return observable -> observable.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread());
    }
}