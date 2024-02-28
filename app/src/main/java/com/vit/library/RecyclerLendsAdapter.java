package com.vit.library;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class RecyclerLendsAdapter extends RecyclerView.Adapter<RecyclerLendsAdapter.ViewHolder>
{
    Context context;
    ArrayList<LendModel> lends;
    public RecyclerLendsAdapter(Context context, ArrayList<LendModel> lends)
    {
        this.context = context;
        this.lends   = lends;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        View view = LayoutInflater.from(context).inflate(R.layout.lendlist_card, parent, false);
        ViewHolder vh = new ViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LendModel lm = lends.get(position);
        holder.setLendModel(lm);

        holder.setItemName(lm.bm.name);
        holder.setUserEmail(lm.um.email);
        holder.setCover(lm.bm.cover);
    }

    @Override
    public int getItemCount()
    {
        return lends.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder
    {
        TextView t_item_name, t_user_email;
        ImageView i_cover;
        Button b_check;
        LendModel lm;

        public ViewHolder(View v)
        {
            super(v);

            t_user_email  = (TextView )    v.findViewById(R.id.lend_card_user_email);
            t_item_name   = (TextView )    v.findViewById(R.id.lend_card_item_name);
            i_cover       = (ImageView)    v.findViewById(R.id.lend_card_cover);
            b_check       = (Button)       v.findViewById(R.id.lend_card_check);

            b_check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DBAdapter db = new DBAdapter(context);
                    db.open();
                    if (db.removeLend(lm.id))
                    {
                        lends.remove(lm);
                        notifyDataSetChanged();
                    }
                }
            });

        }

        void setItemName(String item_name)
        {
            t_item_name.setText(item_name);
        }

        void setUserEmail(String email) { t_user_email.setText(email); }

        void setCover(int cover)
        {
            i_cover.setImageResource(cover);
        }

        void setLendModel(LendModel lm) { this.lm = lm; }
    }
}
