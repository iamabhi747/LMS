package com.vit.library.ui.users;

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
import com.vit.library.RecyclerUsersAdapter;
import com.vit.library.UserModel;
import com.vit.library.databinding.FragmentBooksBinding;
import com.vit.library.databinding.FragmentUsersBinding;

import java.util.ArrayList;

public class UsersFragment extends Fragment
{

    private FragmentUsersBinding binding;
    ArrayList<UserModel> users       = new ArrayList<>();
    ArrayList<UserModel> filterUsers = new ArrayList<>();
    DBAdapter db;
    EditText et_search;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        binding = FragmentUsersBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        db = new DBAdapter(getContext());
        db.open();

        RecyclerView booksView = (RecyclerView) root.findViewById(R.id.rv_users_list);
        booksView.setLayoutManager(new LinearLayoutManager(getContext()));
        Cursor users_cursor = db.getAllUsers();
        if (users_cursor.moveToFirst())
        {
            do {
                users.add(new UserModel(
                        users_cursor.getInt   (0),
                        users_cursor.getString(1),
                        users_cursor.getString(2),
                        users_cursor.getString(3)
                ));
                filterUsers.add(new UserModel(
                        users_cursor.getInt   (0),
                        users_cursor.getString(1),
                        users_cursor.getString(2),
                        users_cursor.getString(3)
                ));
            } while (users_cursor.moveToNext());
        }
        RecyclerUsersAdapter adp = new RecyclerUsersAdapter(getContext(), filterUsers);
        booksView.setAdapter(adp);

        et_search = (EditText) root.findViewById(R.id.et_users_search);
        et_search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                filterUsers.clear();
                for (UserModel um : users)
                {
                    if (um.name.contains(s))
                    {
                        filterUsers.add(um);
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