package xyz.dudedaya.myfriends;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;

public class DataHelper {

    private String token;
    private FriendsDatabaseHelper databaseHelper;

    DataHelper(String token, Context context) {
        this.token = token;
        databaseHelper = new FriendsDatabaseHelper(context);
    }

    public void loadData() {
        new RetriveDataTask().execute();
    }


    private class RetriveDataTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            StringBuilder response = new StringBuilder();
            try {
                URL request = new URL("https://api.vk.com/method/users.get?fields=nickname,photo_100&v=5.89&token=" + token);
                HttpURLConnection urlConnection = (HttpURLConnection) request.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    response.append(stringBuilder);
                } finally {
                    urlConnection.disconnect();
                }
                request = new URL("https://api.vk.com/method/friends.get?fields=nickname,photo_100,status&order=random&count=5&v=5.8&token=" + token);
                urlConnection = (HttpURLConnection) request.openConnection();
                try {
                    BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    StringBuilder stringBuilder = new StringBuilder();
                    String line;
                    while ((line = bufferedReader.readLine()) != null) {
                        stringBuilder.append(line).append("\n");
                    }
                    bufferedReader.close();
                    response.append(stringBuilder);
                } finally {
                    urlConnection.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return response.length() > 0 ? response.toString() : null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (s == null) {
                // todo: error
            } else {
                new WriteDataTask().execute(s);
            }
        }
    }

    private class WriteDataTask extends AsyncTask<String , Void, String> {

        @Override
        protected String doInBackground(String... stringData) {
            ArrayList<String[]> friendsData = parceFriends(stringData[0]);
            try (SQLiteDatabase db = databaseHelper.getWritableDatabase()) {
                for (int i = 1; i < 6; i++) {

                }

            }
            return null;
        }
    }

    private HashMap<String, Object> parceFriends(String friendsDataString) {
        ArrayList<String[]> friendsData = new ArrayList<>();
        //todo: parce
        return friendsData;
    }
}
