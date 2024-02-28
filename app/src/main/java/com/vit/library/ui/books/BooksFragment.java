package com.vit.library.ui.books;

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
import com.vit.library.R;
import com.vit.library.RecyclerBooksAdapter;
import com.vit.library.databinding.FragmentBooksBinding;

import java.util.ArrayList;

public class BooksFragment extends Fragment
{

    private FragmentBooksBinding binding;
    ArrayList<BookModel> books       = new ArrayList<>();
    ArrayList<BookModel> filterBooks = new ArrayList<>();
    DBAdapter db;
    EditText et_search;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        binding = FragmentBooksBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        db = new DBAdapter(getContext());
        db.open();

        RecyclerView booksView = (RecyclerView) root.findViewById(R.id.rv_books_list);
        booksView.setLayoutManager(new LinearLayoutManager(getContext()));
        Cursor books_cursor = db.getAllBooks();
        if (books_cursor.moveToFirst())
        {
            do {
                books.add(new BookModel(
                        books_cursor.getString(1),
                        books_cursor.getString(2),
                        books_cursor.getInt   (3),
                        books_cursor.getInt   (4),
                        R.drawable.ic_book,
                        books_cursor.getInt   (5),
                        books_cursor.getInt   (0)
                ));
                filterBooks.add(new BookModel(
                        books_cursor.getString(1),
                        books_cursor.getString(2),
                        books_cursor.getInt   (3),
                        books_cursor.getInt   (4),
                        R.drawable.ic_book,
                        books_cursor.getInt   (5),
                        books_cursor.getInt   (0)
                ));
            } while (books_cursor.moveToNext());
        }
        RecyclerBooksAdapter adp = new RecyclerBooksAdapter(getContext(), filterBooks);
        booksView.setAdapter(adp);

        et_search = (EditText) root.findViewById(R.id.et_books_search);
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                filterBooks.clear();
                for (BookModel bm : books)
                {
                    if (bm.name.contains(s))
                    {
                        filterBooks.add(bm);
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