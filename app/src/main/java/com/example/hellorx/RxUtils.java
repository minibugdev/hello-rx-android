package com.example.hellorx;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

public class RxUtils {

    public static void unsubscribe(Subscription subscription) {
        if (subscription != null) {
            subscription.unsubscribe();
        }
    }

    public static CompositeSubscription newCompositeSubscription(CompositeSubscription subscription) {
        if (subscription == null || subscription.isUnsubscribed()) {
            return new CompositeSubscription();
        }

        return subscription;
    }
}
