package com.example.umasubbiah.booklistingapp;

import java.util.ArrayList;

public class Book {

    private String mTitle;
    private ArrayList<String> mAuthor;
    private String mUrl;

    public Book(String title, ArrayList<String> author, String Url) {
        mTitle = title;
        mAuthor = author;
        mUrl = Url;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getAuthor() {
        String authors = mAuthor.get(0);

        //In case a book has more than one authr:
        if (mAuthor.size() > 1) {
            for (int i = 1; i < mAuthor.size(); i++) {
                authors += "\n" + mAuthor.get(i);
            }
        }

        return authors;
    }

    public String getUrl() {
        return mUrl;
    }
}

