package com.vit.library.ui.home;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.vit.library.DBAdapter;
import com.vit.library.R;
import com.vit.library.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState)
    {
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        DBAdapter db = new DBAdapter(getContext());
        db.open();
//        int total = db.getTotalCount();
        int total = 123;
        int lends = db.getLendCount();

        ImageView iv = (ImageView) root.findViewById(R.id.i_graph);
        Bitmap bm    = Bitmap.createBitmap(400, 400, Bitmap.Config.ARGB_8888);

        TextView t_total = (TextView) root.findViewById(R.id.t_total);
        t_total.setText(Integer.toString(total));

        TextView t_lends = (TextView) root.findViewById(R.id.t_lends);
        t_lends.setText(Integer.toString(lends));

        iv.setImageBitmap(bm);
        Canvas cv = new Canvas(bm);
        displayGraph(total, lends, cv);

        return root;
    }

    public void displayGraph(int total, int lends, Canvas cv)
    {
        Paint  pt = new Paint();

        int remain = total - lends;
        float remain_angle  = 360 * remain/total;
        float lends_angle   = 360 - remain_angle;

        int books_color        = Color.GREEN;
        int books_border_color = Color.RED;

        int magazines_color        =  Color.argb(255, 25, 120, 0);
        int magazines_border_color =  Color.argb(255, 255, 120, 0);

        float inner_start = 60;
        float inner_end   = 340;
        float outer_start = 55;
        float outer_end   = 345;

        float highlight_inner_start = 50;
        float highlight_inner_end   = 350;
        float highlight_outer_start = 45;
        float highlight_outer_end   = 355;

        pt.setColor(books_border_color);
        cv.drawArc(highlight_outer_start, highlight_outer_start, highlight_outer_end, highlight_outer_end, -90, lends_angle, true, pt);

        pt.setColor(books_color);
        cv.drawArc(highlight_inner_start, highlight_inner_start, highlight_inner_end, highlight_inner_end, -90, lends_angle, true, pt);


        pt.setColor(magazines_border_color);
        cv.drawArc(outer_start, outer_start, outer_end, outer_end, -90+lends_angle, remain_angle, true, pt);

        pt.setColor(magazines_color);
        cv.drawArc(inner_start, inner_start, inner_end, inner_end, -90+lends_angle, remain_angle, true, pt);


    }

    @Override
    public void onDestroyView()
    {
        super.onDestroyView();
        binding = null;
    }
}