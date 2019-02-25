package com.osephodera.babylearn;


import android.content.res.Configuration;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 */
public class ColorsFragment extends Fragment {

    private List<Card> cards;
    private Toast toast;


    public ColorsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        cards = Card.getColorCards();

        LinearLayout linearLayout = (LinearLayout) inflater.inflate(R.layout.fragment_colors, container, false);
        RecyclerView recyclerView = linearLayout.findViewById(R.id.recycler_view);

        ShapeCardAdapter adapter = new ShapeCardAdapter(cards);
        recyclerView.setAdapter(adapter);

        if(getActivity().getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT){
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        }
        else{
            recyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 5));
        }

        adapter.setListener(new ShapeCardAdapter.Listener() {
            @Override
            public void onClick(Card card) {
                showToast(card);
            }
        });

        TextView questionTextView = linearLayout.findViewById(R.id.question_textview);
        questionTextView.setText("text");

        // Inflate the layout for this fragment
        return linearLayout;
    }

    private void showToast(Card card) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(getActivity(), getActivity().getResources().getString(card.getColorNameId(card.getColor())), Toast.LENGTH_SHORT);
        toast.show();
    }

}
