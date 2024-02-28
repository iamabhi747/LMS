package com.vit.library.ui.newbook;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.vit.library.DBAdapter;
import com.vit.library.R;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.vit.library.databinding.FragmentNewbookBinding;

public class NewBookFragment extends Fragment {

    private FragmentNewbookBinding binding;
    private int selection = 0;
    private TextInputEditText et_bookName, et_authorName, et_copies, et_section, et_shelf, et_serial;
    private TextInputLayout    t_bookName,  t_authorName,  t_copies,  t_section,  t_shelf,  t_serial;
    private Button b_submit;
    DBAdapter db;

    void setBookInformation()
    {
        t_bookName.setHint("Book Name");
        et_bookName.setText("");

        t_authorName.setHint("Author");
        et_authorName.setText("");

        selection = 0;
    }

    void setMagazineInformation()
    {
        t_bookName.setHint("Magazine Name");
        et_bookName.setText("");

        t_authorName.setHint("Publisher");
        et_authorName.setText("");

        selection = 1;
    }

    String getText(TextInputEditText et, String _default)
    {
        String out = et.getText().toString();
        if (out == "")
        {
            return _default;
        }
        return out;
    }

    void newBook()
    {
        db.open();
        String name     = et_bookName.getText().toString();
        String author   = et_authorName.getText().toString();
        int    copies   =   Integer.parseInt(getText(et_copies , "0"));
        int    position =   Integer.parseInt(getText(et_section, "0")) * Integer.parseInt(db.getMetadata("section_size"))
                          + Integer.parseInt(getText(et_shelf  , "0")) * Integer.parseInt(db.getMetadata("shelf_size"))
                          + Integer.parseInt(getText(et_serial , "0"));


        if (name == "")
        {
            Toast.makeText(getContext(), "Book Name is Required", Toast.LENGTH_SHORT).show();
            db.close();
            return;
        }
        if (author == "")
        {
            Toast.makeText(getContext(), "Author Name is Required", Toast.LENGTH_SHORT).show();
            db.close();
            return;
        }

        long id = db.newBook(name, author, position, copies);
        if (id == -1)
        {
            Toast.makeText(getContext(), "Failed to Add Book", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(getContext(), "Book added ("+id+")", Toast.LENGTH_SHORT).show();
        }
        db.close();
    }

    void newMagazine()
    {
        db.open();
        String name     = et_bookName.getText().toString();
        String author   = et_authorName.getText().toString();
        int    copies   =   Integer.parseInt(getText(et_copies , "0"));
        int    position =   Integer.parseInt(getText(et_section, "0")) * Integer.parseInt(db.getMetadata("section_size"))
                + Integer.parseInt(getText(et_shelf  , "0")) * Integer.parseInt(db.getMetadata("shelf_size"))
                + Integer.parseInt(getText(et_serial , "0"));


        if (name == "")
        {
            Toast.makeText(getContext(), "Magazine Name is Required", Toast.LENGTH_SHORT).show();
            db.close();
            return;
        }
        if (author == "")
        {
            Toast.makeText(getContext(), "Publisher Name is Required", Toast.LENGTH_SHORT).show();
            db.close();
            return;
        }

        long id = db.newMagazine(name, author, position, copies);
        if (id == -1)
        {
            Toast.makeText(getContext(), "Failed to Add Magazine", Toast.LENGTH_SHORT).show();
        }
        else
        {
            Toast.makeText(getContext(), "Magazine added ("+id+")", Toast.LENGTH_SHORT).show();
        }
        db.close();
    }

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        binding = FragmentNewbookBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        db = new DBAdapter(getContext());

        et_bookName   = (TextInputEditText) root.findViewById(R.id.et_newbook_book);
        et_authorName = (TextInputEditText) root.findViewById(R.id.et_newbook_author);
        et_copies     = (TextInputEditText) root.findViewById(R.id.et_newbook_copies);
        et_section    = (TextInputEditText) root.findViewById(R.id.et_newbook_section);
        et_shelf      = (TextInputEditText) root.findViewById(R.id.et_newbook_shelf);
        et_serial     = (TextInputEditText) root.findViewById(R.id.et_newbook_serial);

        t_bookName   = (TextInputLayout) root.findViewById(R.id.t_newbook_book);
        t_authorName = (TextInputLayout) root.findViewById(R.id.t_newbook_author);
        t_copies     = (TextInputLayout) root.findViewById(R.id.t_newbook_copies);
        t_section    = (TextInputLayout) root.findViewById(R.id.t_newbook_section);
        t_shelf      = (TextInputLayout) root.findViewById(R.id.t_newbook_shelf);
        t_serial     = (TextInputLayout) root.findViewById(R.id.t_newbook_serial);

        b_submit     = (Button)   root.findViewById(R.id.b_newbook_submit);


        String[] type = new String[] {"Book", "Magazine"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, type);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        AutoCompleteTextView assetType = root.findViewById(R.id.filled_exposed_dropdown);
        assetType.setAdapter(adapter);
        assetType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0)
                {
                    setBookInformation();
                }
                else if (position == 1)
                {
                    setMagazineInformation();
                }
            }
        });
        assetType.setText("Book", false);


        b_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selection == 0)
                {
                    newBook();
                }
                else if (selection == 1)
                {
                    newMagazine();
                }
            }
        });

        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}