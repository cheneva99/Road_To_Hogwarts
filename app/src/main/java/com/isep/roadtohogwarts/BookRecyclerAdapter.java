package com.isep.roadtohogwarts;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;


public class BookRecyclerAdapter extends RecyclerView.Adapter<BookRecyclerAdapter.ViewHolder>  {


    List<Book> books;


    public BookRecyclerAdapter(List<Book> books ) {
        this.books = books;

    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView bookDescription;
        TextView bookVolume;
        ImageView bookCover;

        public ViewHolder(@NonNull View itemView ) {
            super(itemView);
            bookVolume = itemView.findViewById(R.id.bookVolume);
            bookDescription = itemView.findViewById(R.id.bookDescription);

            bookCover = itemView.findViewById(R.id.bookCover);
        }

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.book_recyclerview_item,parent,false);

        return new ViewHolder(view);


    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bookVolume.setText("Volume "+ books.get(position).getId());
        holder.bookDescription.setText("Publish date "+books.get(position).getPublishingDateUK());
        try {
            String url = books.get(position).getUrl();

            Picasso.get().load(url).into(holder.bookCover);

        }catch(Exception e){



        }


    }

    @Override
    public int getItemCount() {
        return books.size();
    }


}



