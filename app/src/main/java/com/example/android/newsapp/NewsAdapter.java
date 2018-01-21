package com.example.android.newsapp;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;


public class NewsAdapter extends ArrayAdapter<News> {

    public NewsAdapter(Activity context, List<News> news) {
        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, news);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.list_item, parent, false);
        }

        // Get the {@link AndroidFlavor} object located at this position in the list
        News currentNews = getItem(position);

        // Find the title TextView in the list_item.xml layout with the ID news_title
        TextView titleView = (TextView) listItemView.findViewById(R.id.news_title);
        // Get the title from the current News object and
        // set this text on the News title TextView
        titleView.setText(currentNews.getTitle());

        // Find the title TextView in the list_item.xml layout with the ID news_time
        TextView timeTextView = (TextView) listItemView.findViewById(R.id.news_time);
        // Get the publication time from the current News object and
        // set this text on the time TextView
        timeTextView.setText(formatTime(currentNews.getDate()));

        // Find the title TextView in the list_item.xml layout with the ID trail_text
        TextView trailTextView = (TextView) listItemView.findViewById(R.id.trail_text);
        // Get the trailText from the current News object and
        // set this text on the trail text TextView
        trailTextView.setText(formatTrailText(currentNews.getTrail()));

        // Return the whole list item layout
        // so that it can be shown in the ListView
        return listItemView;
    }

    // Format the publication time
    private String formatTime(String time){
        String[] dividedTimeArray = time.split("T");
        String formatedTime = dividedTimeArray[0]+"  " + dividedTimeArray[1];
        return formatedTime.substring(0,formatedTime.length()-1);
    }

    // Format the trail Text
    private String formatTrailText(String trailText){
        String[] dividedTextArray = trailText.split("</strong>");
        String formatedText;
        if (dividedTextArray.length > 1){
            formatedText = dividedTextArray[1].split("<br>")[0];
            return formatedText;
        }
        else{
            formatedText = dividedTextArray[0].split("<br>")[0];
            return formatedText;
        }
    }
}
