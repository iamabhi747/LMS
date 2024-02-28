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

public class RecyclerUsersAdapter extends RecyclerView.Adapter<RecyclerUsersAdapter.ViewHolder>
{
    Context context;
    ArrayList<UserModel> users;
    public RecyclerUsersAdapter(Context context, ArrayList<UserModel> users)
    {
        this.context = context;
        this.users   = users;
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
        UserModel um = users.get(position);
        holder.setUserModel(um);

        holder.setName(um.name);
        holder.setEmail(um.email);
    }

    @Override
    public int getItemCount()
    {
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView t_author, t_name, t_copies;
        ImageView i_cover;
        LinearLayout l_row;
        UserModel um;

        public ViewHolder(View v)
        {
            super(v);

            t_author = (TextView )    v.findViewById(R.id.book_card_author);
            t_name   = (TextView )    v.findViewById(R.id.book_card_name);
            i_cover  = (ImageView)    v.findViewById(R.id.book_card_cover);
            t_copies = (TextView )    v.findViewById(R.id.book_card_copies_available);
            l_row    = (LinearLayout) v.findViewById(R.id.l_books_row);

            t_copies.setText("");

            l_row.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent i = new Intent(context, UserActivity.class);
                    i.putExtra("name"    , um.name);
                    i.putExtra("email"  , um.email);
                    i.putExtra("id", Integer.toString(um.id));

                    context.startActivity(i);
                }
            });
        }

        void setEmail(String email)
        {
            t_author.setText(email);
        }

        void setName(String name)
        {
            t_name.setText(name);
        }

        void setUserModel(UserModel um) { this.um = um; }
    }
}
