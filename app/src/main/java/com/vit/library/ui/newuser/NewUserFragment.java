package com.vit.library.ui.newuser;

import android.os.Bundle;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.vit.library.DBAdapter;
import com.vit.library.R;
import com.vit.library.databinding.FragmentNewuserBinding;

public class NewUserFragment extends Fragment
{
    private FragmentNewuserBinding binding;
    Button  b_submit;
    TextInputEditText et_name, et_email, et_phone;
    DBAdapter db;

    void newUser()
    {
        db.open();
        String name  = et_name .getText().toString();
        String email = et_email.getText().toString();
        String phone = et_phone.getText().toString();

        if (name == "")
        {
            Toast.makeText(getContext(), "Name is Required", Toast.LENGTH_SHORT).show();
            db.close();
            return;
        }
        if (email == "" || !Patterns.EMAIL_ADDRESS.matcher(email).matches())
        {
            Toast.makeText(getContext(), "Enter valid email", Toast.LENGTH_SHORT).show();
            db.close();
            return;
        }
        if (phone == "" || !Patterns.PHONE.matcher(phone).matches())
        {
            Toast.makeText(getContext(), "Enter valid phone", Toast.LENGTH_SHORT).show();
            db.close();
            return;
        }

        long id = db.newUser(name, email, phone);
        if (id == -1)
        {
            Toast.makeText(getContext(), "Failed to Add User", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(getContext(), "User added ("+id+")", Toast.LENGTH_SHORT).show();
        }
        db.close();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        binding = FragmentNewuserBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        db = new DBAdapter(getContext());

        et_name  = (TextInputEditText) root.findViewById(R.id.et_newuser_name);
        et_email = (TextInputEditText) root.findViewById(R.id.et_newuser_email);
        et_phone = (TextInputEditText) root.findViewById(R.id.et_newuser_phone);

        b_submit = (Button) root.findViewById(R.id.b_newuser_submit);
        b_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newUser();
            }
        });

        return root;
    }
}
