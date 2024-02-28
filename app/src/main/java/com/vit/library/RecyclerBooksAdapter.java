package com.vit.library;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerBooksAdapter extends RecyclerView.Adapter<RecyclerBooksAdapter.ViewHolder>
{
    Context context;
    ArrayList<BookModel> books;
    public RecyclerBooksAdapter(Context context, ArrayList<BookModel> books)
    {
        this.context = context;
        this.books   = books;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.booklist_card, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        BookModel bm = books.get(position);
        holder.setBookModel(bm);

        holder.setName(bm.name);
        holder.setAuthor(bm.author);
        holder.setCopies(bm.copies, bm.lends);
        holder.setCover(bm.cover);
    }

    @Override
    public int getItemCount()
    {
        return books.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView t_author, t_name, t_copies;
        ImageView i_cover;
        LinearLayout l_row;
        BookModel bm;

        public ViewHolder(View v)
        {
            super(v);

            t_author = (TextView )    v.findViewById(R.id.book_card_author);
            t_name   = (TextView )    v.findViewById(R.id.book_card_name);
            i_cover  = (ImageView)    v.findViewById(R.id.book_card_cover);
            t_copies = (TextView )    v.findViewById(R.id.book_card_copies_available);
            l_row    = (LinearLayout) v.findViewById(R.id.l_books_row);

            l_row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, BookActivity.class);
                    i.putExtra("name"    , bm.name);
                    i.putExtra("author"  , bm.author);
                    i.putExtra("copies"  , Integer.toString(bm.copies));
                    i.putExtra("lends"   , Integer.toString(bm.lends));
                    i.putExtra("cover"   , Integer.toString(bm.cover));
                    i.putExtra("position", Integer.toString(bm.position));
                    i.putExtra("id"      , Integer.toString(bm.id));

                    context.startActivity(i);
                }
            });
        }

        void setAuthor(String author)
        {
            t_author.setText("by " + author);
        }

        void setName(String name)
        {
            t_name.setText(name);
        }

        void setCopies(int copies, int lends)
        {
            t_copies.setText(Integer.toString(copies - lends));
        }

        void setCover(int cover)
        {
            i_cover.setImageResource(cover);
        }

        void setBookModel(BookModel bm) { this.bm = bm; }
    }
}
