package xyz.dudedaya.retrofitexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private int postId = 3;
    private Integer userId[] = {1, 4};
    private String sort = "id";
    private String order = "desc";

    private TextView textViewResult;
    private JsonPlaceHolderApi jsonPlaceHolderApi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textViewResult = findViewById(R.id.text_view_result);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

        getPosts(userId, sort, order);
        //getComments(postId);

    }

    private void getPosts(Integer userIds[], String sort, String order) {
//        To pass unspecified parameters we using Map<String, String>
//        Map<String, String> parameters = new HashMap<>();
//        parameters.put("userId", "1"); //But with this approach we can only pass 1 of each query
//        parameters.put("_sort", "id");
//        parameters.put("_order", "desc");
//        Call<List<Post>> call = jsonPlaceHolderApi.getPosts(parameters);


        Call<List<Post>> call = jsonPlaceHolderApi.getPosts(userIds, sort, order); // can be set to null if it doesn't used

        call.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {

                if (!response.isSuccessful()) {
                    String responseCode = "Code " + response.code();
                    textViewResult.setText(responseCode);
                    return;
                }

                List<Post> posts = response.body();

                for (Post post : posts) {
                    StringBuilder content = new StringBuilder();
                    content.append("ID: ").append(post.getId()).append("\n");
                    content.append("User ID: ").append(post.getUserId()).append("\n");
                    content.append("Title : ").append(post.getTitle()).append("\n");
                    content.append("Text : ").append(post.getText()).append("\n\n");
                    textViewResult.append(content);
                }
            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                textViewResult.setText(t.getMessage());
            }
        });
    }

    private void getComments(int postId) {
//        We can pass the whole url or the endpoint here too
//        String ulr = "https://jsonplaceholder.typicode.com/posts?userId=2&_sort=id&_order=desc"
//        --or--
//        String url = "posts?userId=2&_sort=id&_order=desc";
//        Call<List<Comment>> call = jsonPlaceHolderApi.getComments(url);

        Call<List<Comment>> call = jsonPlaceHolderApi.getComments(postId);

        call.enqueue(new Callback<List<Comment>>() {
            @Override
            public void onResponse(Call<List<Comment>> call, Response<List<Comment>> response) {

                if (!response.isSuccessful()) {
                    String responseCode = "Code " + response.code();
                    textViewResult.setText(responseCode);
                    return;
                }

                List<Comment> comments = response.body();

                for (Comment comment : comments) {
                    StringBuilder content = new StringBuilder();
                    content.append("ID: ").append(comment.getId()).append("\n");
                    content.append("Post ID: ").append(comment.getPostId()).append("\n");
                    content.append("Name : ").append(comment.getName()).append("\n");
                    content.append("Email : ").append(comment.getEmail()).append("\n");
                    content.append("Text : ").append(comment.getText()).append("\n\n");
                    textViewResult.append(content);
                }
            }

            @Override
            public void onFailure(Call<List<Comment>> call, Throwable t) {
                textViewResult.setText(t.getMessage());
            }
        });

    }
}
