package com.osephodera.babylearn;

import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

public class ShapeCardAdapter extends RecyclerView.Adapter<ShapeCardAdapter.ViewHolder> {

    private List<Card> cards;
    private Listener listener;

    interface Listener {
        void onClick(Card card);
    }

    public ShapeCardAdapter(List<Card> cards) {
        this.cards = cards;
    }

    @Override
    public int getItemCount() {
        return cards.size();
    }

    public void setListener(Listener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public ShapeCardAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        CardView cv = (CardView) LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.color_shape_card, viewGroup, false);
        return new ViewHolder(cv);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final Card card = cards.get(i);
        CardView cardView = viewHolder.cardView;
        ImageView imageView = cardView.findViewById(R.id.shape_image);
        Drawable drawable = ContextCompat.getDrawable(cardView.getContext(), card.getShape());
        imageView.setImageDrawable(drawable);
        cardView.setCardBackgroundColor(ContextCompat.getColor(cardView.getContext(), card.getColor()));
        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (listener != null) {
                    listener.onClick(card);
                }
            }
        });
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private CardView cardView;

        public ViewHolder(CardView v) {
            super(v);
            cardView = v;
        }

    }
}
