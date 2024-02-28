package com.vit.library.ui.lends;

import android.database.Cursor;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.MainThread;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vit.library.BookModel;
import com.vit.library.DBAdapter;
import com.vit.library.Dashboard;
import com.vit.library.LendModel;
import com.vit.library.R;
import com.vit.library.RecyclerBooksAdapter;
import com.vit.library.RecyclerLendsAdapter;
import com.vit.library.RecyclerUsersAdapter;
import com.vit.library.UserModel;
import com.vit.library.databinding.FragmentBooksBinding;
import com.vit.library.databinding.FragmentLendsBinding;
import com.vit.library.databinding.FragmentUsersBinding;

import java.util.ArrayList;

public class LendsFragment extends Fragment
{

    private FragmentLendsBinding binding;
    ArrayList<LendModel> lends       = new ArrayList<>();
    ArrayList<LendModel> filterLends = new ArrayList<>();
    DBAdapter db;
    EditText et_search;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        binding = FragmentLendsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        db = new DBAdapter(getContext());
        db.open();

        RecyclerView booksView = (RecyclerView) root.findViewById(R.id.rv_lends_list);
        booksView.setLayoutManager(new LinearLayoutManager(getContext()));
        Cursor lends_cursor = db.getAllLends();
        if (lends_cursor.moveToFirst())
        {
            do {
                Cursor user_cursor = db.getUser(lends_cursor.getInt(1));
                Cursor book_cursor;
                if (lends_cursor.getInt(3) == 0)
                {
                    book_cursor = db.getBook(lends_cursor.getInt(2));
                }
                else
                {
                    // Magazine
                    book_cursor = db.getMagazine(lends_cursor.getInt(2));
                }
                lends.add(new LendModel(
                        lends_cursor.getInt(0),
                        new BookModel(
                                book_cursor.getString(0),
                                book_cursor.getString(1),
                                book_cursor.getInt   (2),
                                book_cursor.getInt   (3),
                                R.drawable.ic_book,
                                book_cursor.getInt   (4),
                                lends_cursor.getInt(2)
                        ),
                        new UserModel(
                                lends_cursor.getInt(1),
                                user_cursor.getString(0),
                                user_cursor.getString(1),
                                user_cursor.getString(2)
                        )
                ));
                filterLends.add(new LendModel(
                        lends_cursor.getInt(0),
                        new BookModel(
                                book_cursor.getString(0),
                                book_cursor.getString(1),
                                book_cursor.getInt   (2),
                                book_cursor.getInt   (3),
                                R.drawable.ic_book,
                                book_cursor.getInt   (4),
                                lends_cursor.getInt(2)
                        ),
                        new UserModel(
                                lends_cursor.getInt(1),
                                user_cursor.getString(0),
                                user_cursor.getString(1),
                                user_cursor.getString(2)
                        )
                ));
            } while (lends_cursor.moveToNext());
        }
        RecyclerLendsAdapter adp = new RecyclerLendsAdapter(getContext(), filterLends);
        booksView.setAdapter(adp);

        et_search = (EditText) root.findViewById(R.id.et_lends_search);
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                filterLends.clear();
                for (LendModel lm : lends)
                {
                    if (lm.bm.name.contains(s) || lm.um.email.contains(s) || lm.um.name.contains(s))
                    {
                        filterLends.add(lm);
                    }
                }
                adp.notifyDataSetChanged();
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
        return root;
    }

    @Override
    public void onDestroyView() {
        db.close();
        super.onDestroyView();
        binding = null;
    }
}