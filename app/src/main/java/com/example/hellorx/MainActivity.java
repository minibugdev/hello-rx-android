package com.example.hellorx;

import android.os.Bundle;
import android.widget.TextView;

import com.example.hellorx.service.API;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends RxAppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    public static final String CATEGORY_MOVIES = "movies";
    public static final String CATEGORY_FAMOUS = "famous";

    @Bind(R.id.quote_textview) TextView mQuoteTextView;
    @Bind(R.id.quote_author)   TextView mQuoteAuthor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        getQuote();
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @OnClick(R.id.quote_button)
    public void onClickGetQuote() {
        getQuote();
    }

    private void getQuote() {
        API.quote().getQuote(CATEGORY_MOVIES)
            .flatMap(quote -> API.image().getPhoto(quote.getAuthor()))
            .filter(pixaBay -> pixaBay.getTotalImages() > 0)
            .map(pixaBay -> pixaBay.getImages().get(0).getImageURL())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(url -> mQuoteAuthor.setText(url));
    }
}

