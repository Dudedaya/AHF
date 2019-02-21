package xyz.dudedaya.myfriends;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

public class FriendsRecyclerAdapter extends RecyclerView.Adapter<FriendsRecyclerAdapter.ViewHolder> {

    private List<DataHelper.User> friends;

    FriendsRecyclerAdapter(List<DataHelper.User> friends) {
        this.friends = friends;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        CardView cardView = (CardView) LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.friend_card, viewGroup, false);
        return new ViewHolder(cardView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        CardView cardView = viewHolder.cardView;
        DataHelper.User friend = friends.get(i);
        ImageView imageView = cardView.findViewById(R.id.image_view);
        if (friend.getPhoto_100() != null) {
            new RetrievePhotoTask(imageView).execute(friend.getPhoto_100());
        }
        imageView.setContentDescription("photo");
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

    static class ViewHolder extends RecyclerView.ViewHolder {

        private CardView cardView;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.cardView = (CardView) itemView;
        }
    }

    private class RetrievePhotoTask extends AsyncTask<String, Void, Bitmap> {
        ImageView imageView;

        RetrievePhotoTask(ImageView imageView) {
            this.imageView = imageView;
        }

        @Override
        protected Bitmap doInBackground(String... urls) {
            String urlString = urls[0];
            try {
                URL url = new URL(urlString);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                try {
                    return BitmapFactory.decodeStream(urlConnection.getInputStream());
                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            imageView.setImageBitmap(bitmap);
        }
    }
}
