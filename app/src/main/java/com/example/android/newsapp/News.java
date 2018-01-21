package com.example.android.newsapp;

public class News {
    // String value
    private String mNewsTitle;
    private String mTrailText;
    private String mPublicationDate;
    private String mUrl;

    /**
     * Constructs a new object with three Texts.
     */
    public News(String newsTitle, String trailText, String publicationDate, String url) {
        mNewsTitle = newsTitle;
        mTrailText = trailText;
        mPublicationDate = publicationDate;
        mUrl = url;
    }

    /**
     * Gets the string value of news title.
     */
    public String getTitle() {
        return mNewsTitle;
    }

    /**
     * Gets the String value of trail text.
     */
    public String getTrail() {
        return mTrailText;
    }

    /**
     * Gets the String value of publication time.
     */
    public String getDate() {
        return mPublicationDate;
    }

    /**
     * Gets the String value of url of the news.
     */
    public String getUrl() {
        return mUrl;
    }
}
