package xyz.dudedaya.myfriends;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DataHelper {

    public interface DataEventListener {
        void dataLoaded();
        void errorLoadingData();
    }

    private String token;
    private List<User> users;
    private DataEventListener listener;

    DataHelper(String token, DataEventListener listener) {
        this.token = token;
        this.listener = listener;
    }

    public void loadData() {
        new RetriveDataTask().execute();
    }


    private class RetriveDataTask extends AsyncTask<Void, Void, String> {

        @Override
        protected String doInBackground(Void... voids) {
            StringBuilder response = new StringBuilder();
            try {
                URL request = new URL("https://api.vk.com/method/users.get?fields=nickname,photo_100&v=5.89&access_token=" + token);
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
                request = new URL("https://api.vk.com/method/friends.get?fields=nickname,photo_100,status&order=random&count=5&v=5.8&access_token=" + token);
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
                listener.errorLoadingData();
            } else {
                String testString = "{\"response\":[{\"id\":297428682,\"first_name\":\"Jared\",\"last_name\":\"Leto\",\"nickname\":\"Letovan\",\"photo_100\":\"someurl\"}]} " +
                        "{\"response\":[{\"id\":210700287,\"first_name\":\"first_name1\",\"last_name\":\"last_name1\"}," +
                        "{\"id\":210700287,\"first_name\":\"first_name2\",\"last_name\":\"last_name2\"}," +
                        "{\"id\":210700287,\"first_name\":\"first_name3\",\"last_name\":\"last_name3\"}," +
                        "{\"id\":210700287,\"first_name\":\"first_name4\",\"last_name\":\"last_name4\"}," +
                        "{\"id\":210700287,\"first_name\":\"first_name5\",\"last_name\":\"last_name5\"}]}";
                Pattern pattern = Pattern.compile("(\\{\"id\":[^\\}]*\\})");
                Matcher matcher = pattern.matcher(testString);
                GsonBuilder builder = new GsonBuilder();
                Gson gson = builder.create();
                users = new ArrayList<>();
                while (matcher.find()) {
                    User user = gson.fromJson(matcher.group(), User.class);
                    users.add(user);
                }
                listener.dataLoaded();
            }
        }
    }

    public class User {
        public String getName() {
            if (nickname != null && !nickname.equals("") && !nickname.equals("null")) {
                return first_name + " " + nickname + " " + last_name;
            } else return first_name + " " + last_name;
        }

        public String getPhoto_100() {
            return photo_100;
        }

        public String getStatus() {
            return status;
        }

        private String first_name;
        private String last_name;
        private String nickname;
        private String photo_100;
        private String status;

        public User(String first_name, String last_name, String nickname, String photo_100, String status) {
            this.first_name = first_name;
            this.last_name = last_name;
            this.nickname = nickname;
            this.photo_100 = photo_100;
            this.status = status;
        }

        @Override
        public String toString() {
            return "first_name: " + first_name + " last_name: " + last_name + " nickname: " + nickname + " photo_100: " + photo_100 + " status: " + status;
        }
    }

    public User getUser() {
        if (users.size() > 0) {
            return users.get(0);
        } else return null;
    }

    public List<User> getFriends() {
        if (users.size() > 1) {
            List<User> friends = users;
            friends.remove(0);
            return friends;
        } else return null;
    }
}
