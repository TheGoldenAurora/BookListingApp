package com.example.umasubbiah.booklistingapp;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder> {

    ArrayList<Book> books;
    Context activity;
    private static OnItemClickListener itemSelected;

    public interface OnItemClickListener {
        void onItemClick(Book book);
    }

    public BookAdapter(Context context, ArrayList<Book> book, OnItemClickListener selected) {
        activity = context;
        books = book;
        itemSelected = selected;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.search_results, parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(BookAdapter.ViewHolder h, int position) {
        Book book = books.get(position);
        h.bookTitleTextView.setText(book.getTitle());
        h.bookAuthorTextView.setText(book.getAuthor());
        h.bind(books.get(position), itemSelected);
    }

    @Override
    public int getItemCount() {
        return books.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        TextView bookTitleTextView;
        TextView bookAuthorTextView;

        public ViewHolder(View view) {
            super(view);
            bookTitleTextView = (TextView) view.findViewById(R.id.Title);
            bookAuthorTextView = (TextView) view.findViewById(R.id.Author);
        }

        public void bind(final Book book, final OnItemClickListener l) {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    l.onItemClick(book);
                }
            });
        }
    }

    public void clear() {
        books.clear();
        notifyDataSetChanged();
    }

    public void addAll(List<Book> book) {
        books.addAll(book);
        notifyDataSetChanged();
    }
}