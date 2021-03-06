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

    private final String FRIENDS_COUNT = "5";

    public interface DataEventListener {
        void dataLoaded();

        void errorLoadingData();
    }

    public void setListener(DataEventListener listener) {
        this.listener = listener;
    }

    public void removeListener(DataEventListener listener) {
        if (this.listener.equals(listener)) {
            this.listener = null;
        }
    }

    private static String token = "";
    private List<User> users;
    private DataEventListener listener;
    private static DataHelper instance;

    public static void setToken(String newToken) {
        token = newToken;
    }

    public static DataHelper initInstance(String newToken) {
        if (instance == null && !token.equals(newToken)) {
            setToken(newToken);
            instance = new DataHelper();
            return instance;
        } else return instance;
    }

    public static DataHelper getInstance() {
        return instance;
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
                request = new URL("https://api.vk.com/method/friends.get?fields=nickname,photo_100,status&order=random&count=" + FRIENDS_COUNT + "&v=5.8&access_token=" + token);
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
                Pattern pattern = Pattern.compile("(\\{\"id\":[^\\}]*\\})");
                Matcher matcher = pattern.matcher(s);
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
            if (photo_100 != null && !photo_100.equals("") && !photo_100.equals("null") && !photo_100.equals("https:\\/\\/vk.com\\/images\\/camera_100.png?ava=1")) {
                return photo_100.replaceAll("\\\\/", "/");
            } else return null;
        }

        public String getStatus() {
            if (status != null && !status.equals("") && !status.equals("null")) {
                return status;
            } else return null;
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
        if (users != null && users.size() > 1) {
            List<User> friends = users;
            friends.remove(0);
            return friends;
        } else return null;
    }
}
