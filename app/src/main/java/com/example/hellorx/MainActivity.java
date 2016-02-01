package com.example.hellorx;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hellorx.model.PixaBay;
import com.example.hellorx.model.Quote;
import com.example.hellorx.service.API;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    public static final String CATEGORY_MOVIES = "movies";
    public static final String CATEGORY_FAMOUS = "famous";

    @Bind(R.id.quote_textview) TextView  mQuoteTextView;
    @Bind(R.id.quote_author)   TextView  mQuoteAuthor;
    @Bind(R.id.quote_image)    ImageView mQuoteImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        getQuote();
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
        API.quote().getQuote(CATEGORY_FAMOUS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnNext(this::updateQuoteView)
            .flatMap(quote -> API.image().getPhoto(quote.getAuthor())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()))
            .subscribe(this::showQuoteImage, this::hideQuoteImage);
    }

    private void updateQuoteView(Quote quote) {
        mQuoteTextView.setText(quote.getQuote());
        mQuoteAuthor.setText(quote.getAuthor());
    }

    private void hideQuoteImage(Throwable e) {
        mQuoteImage.setVisibility(View.GONE);
        Log.e(TAG, e.getMessage());
    }

    private void showQuoteImage(PixaBay image) {
        if (image != null && image.getTotalImages() > 0) {
            mQuoteImage.setVisibility(View.VISIBLE);
            Picasso.with(getBaseContext())
                .load(image.getImages().get(0).getImageURL())
                .fit()
                .centerCrop()
                .noFade()
                .into(mQuoteImage);
        }
        else {
            mQuoteImage.setVisibility(View.GONE);
        }
    }

    private void getQuoteCombind() {
        API.quote().getQuote(CATEGORY_FAMOUS)
            .flatMap(quote -> API.image().getPhoto(quote.getAuthor())
                    .map((Func1<PixaBay, Quote>) pixaBay -> {
                        if (pixaBay.getTotalImages() > 0) {
                            quote.setAuthorImage(pixaBay.getImages().get(0));
                        }
                        return quote;
                    })
            )
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(quote -> {
                updateQuoteView(quote);

                if (quote.getAuthorImage() != null) {
                    mQuoteImage.setVisibility(View.VISIBLE);
                    Picasso.with(getBaseContext())
                        .load(quote.getAuthorImage().getImageURL())
                        .fit()
                        .centerCrop()
                        .into(mQuoteImage);
                }
                else {
                    mQuoteImage.setVisibility(View.GONE);
                }
            }, this::hideQuoteImage);
    }
}

