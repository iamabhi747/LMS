package com.vit.library.ui.newlend;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.vit.library.DBAdapter;
import com.vit.library.R;
import com.vit.library.databinding.FragmentHomeBinding;
import com.vit.library.databinding.FragmentNewlendBinding;

public class NewLendFragment extends Fragment {

    private FragmentNewlendBinding binding;

    TextInputLayout t_item_id;
    TextInputEditText et_item_id, et_user_id;
    Button b_submit;
    DBAdapter db;

    boolean isBook = true;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        binding = FragmentNewlendBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        db = new DBAdapter(getContext());

        t_item_id  = (TextInputLayout)   root.findViewById(R.id.t_newlend_itemid);
        et_item_id = (TextInputEditText) root.findViewById(R.id.et_newlend_itemid);
        et_user_id = (TextInputEditText) root.findViewById(R.id.et_newlend_userid);
        b_submit   = (Button)            root.findViewById(R.id.b_newlend_submit);

        db.open();


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
                    t_item_id.setHint("Book ID");
                    isBook = true;
                }
                else if (position == 1)
                {
                    t_item_id.setHint("Magazine ID");
                    isBook = false;
                }
            }
        });
        assetType.setText("Book", false);


        b_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int itemID = !et_item_id.getText().toString().equals("") ? Integer.parseInt( et_item_id.getText().toString() ) : -1;
                int userID = !et_user_id.getText().toString().equals("") ? Integer.parseInt( et_user_id.getText().toString() ) : -1;

                if (itemID == -1 || userID == -1)
                {
                    Toast.makeText(getContext(), "Both IDs are required", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (isBook)
                {
                    long rowId = db.newLend(0, itemID, userID);

                    if (rowId == -1)
                    {
                        Toast.makeText(getContext(), "Failed to Regester new lend", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else
                    {
                        Toast.makeText(getContext(), "New Lend added", Toast.LENGTH_SHORT).show();
                    }
                }


            }
        });

        return root;
    }

    @Override
    public void onDestroyView()
    {
        db.close();
        super.onDestroyView();
        binding = null;
    }
}
