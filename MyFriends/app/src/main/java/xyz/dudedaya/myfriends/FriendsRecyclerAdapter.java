package xyz.dudedaya.myfriends;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.URL;
import java.util.List;

public class FriendsRecyclerAdapter extends RecyclerView.Adapter<FriendsRecyclerAdapter.ViewHolder> {

    private List<DataHelper.User> friends;

    public FriendsRecyclerAdapter(List<DataHelper.User> friends) {
        this.friends = friends;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        CardView cardView = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.friend_card, viewGroup, null);
        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        CardView cardView = viewHolder.cardView;
        DataHelper.User friend = friends.get(i);
        ImageView imageView = cardView.findViewById(R.id.image_view);
        try {
            URL url = new URL(friend.getPhoto_100());
            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            imageView.setImageBitmap(bmp);
        } catch (Exception e) {
            e.printStackTrace();
        }
        TextView nameTextView = cardView.findViewById(R.id.name);
        nameTextView.setText(friend.getName());
        if (friend.getStatus() != null) {
            TextView statusTextView = cardView.findViewById(R.id.status);
            statusTextView.setText(friend.getStatus());
        }

    }

    @Override
    public int getItemCount() {
        return friends.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        private CardView cardView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.cardView = (CardView)itemView;
        }
    }
}
