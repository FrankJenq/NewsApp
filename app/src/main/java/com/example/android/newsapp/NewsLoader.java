package com.example.android.newsapp;

import android.content.AsyncTaskLoader;
import android.content.Context;

import java.util.List;

public class NewsLoader extends AsyncTaskLoader<List<News>> {

    private String[] mNewsUrl;

    NewsLoader(Context context, String... urls) {
        super(context);
        mNewsUrl = urls;
    }

    @Override
    protected void onStartLoading() {
        forceLoad();
    }

    @Override
    public List<News> loadInBackground() {

        // do not execute if there is no valid url, or the value of the first url is null
        if (mNewsUrl.length < 1 || mNewsUrl[0] == null) {
            return null;
        }
        List<News> news = QueryUtils.fetchNewsData(mNewsUrl[0]);
        return news;
    }
}
