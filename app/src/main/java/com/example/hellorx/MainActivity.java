package com.example.hellorx;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.hellorx.model.PixaBay;
import com.example.hellorx.model.Quote;
import com.example.hellorx.data.DataManager;
import com.squareup.picasso.Picasso;
import com.trello.rxlifecycle.components.support.RxAppCompatActivity;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends RxAppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    public static final String CATEGORY_MOVIES = "movies";
    public static final String CATEGORY_FAMOUS = "famous";

    @Bind(R.id.quote_textview) TextView  mQuoteTextView;
    @Bind(R.id.quote_author)   TextView  mQuoteAuthor;
    @Bind(R.id.quote_image)    ImageView mQuoteImage;

    private DataManager mDataManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        mDataManager = new DataManager();

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
        mDataManager.getQuote(CATEGORY_FAMOUS)
            .doOnNext(this::updateQuoteView)
            .flatMap(quote -> mDataManager.getPhoto(quote.getAuthor()))
            .compose(bindToLifecycle())
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
}

