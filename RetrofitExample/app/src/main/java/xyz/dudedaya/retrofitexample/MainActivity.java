package xyz.dudedaya.retrofitexample;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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

        //If we want GSON to include null fields:
//        Gson gson = new GsonBuilder().serializeNulls().create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create())
//                .addConverterFactory(GsonConverterFactory.create(gson)) //pass the created gson object with .serializeNulls()
                .build();
        jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderApi.class);

//        getPosts(userId, sort, order);
//        getComments(postId);
//        createPost();
//        updatePost();
        deletePost();

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

    private void createPost() {
        //To use Url-encoded method we pass each field individually
        //Call<Post> call = jsonPlaceHolderApi.createPost(23, "New title", "New text");
        // ----or----
        //Map<String, String> fields new HashMap<>();
        //fields.put("userId", "25");
        //fields.put("title", "New title");
        //We don't put the "body" in, so we get null there.
        //Call<Post> call = jsonPlaceHolderApi.createPost(fields);


        Post post = new Post(23, "New title", "New text"); //for example. In real case use user values here.

        Call<Post> call = jsonPlaceHolderApi.createPost(post);

        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {

                if (!response.isSuccessful()) {
                    String responseCode = "Code " + response.code();
                    textViewResult.setText(responseCode);
                    return;
                }

                Post postResponse = response.body();

                StringBuilder content = new StringBuilder();
                content.append("Code: ").append(response.code()).append("\n");
                content.append("ID: ").append(postResponse.getId()).append("\n");
                content.append("User ID: ").append(postResponse.getUserId()).append("\n");
                content.append("Title : ").append(postResponse.getTitle()).append("\n");
                content.append("Text : ").append(postResponse.getText()).append("\n\n");
                textViewResult.append(content);
            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                textViewResult.setText(t.getMessage());
            }
        });
    }

    private void updatePost() {
        Post post = new Post(12, null, "New text");

        Call<Post> call = jsonPlaceHolderApi.putPost(5, post);
        //The putPost method will replace the Post object on server completely, so we will have
        // a null as a title there, since gson is ignoring the null fields by default.
        //Call<Post> call = jsonPlaceHolderApi.patchPost(5, post);
        //A patchPost method will only update the passed fields, so the original title still be there.

        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {

                if (!response.isSuccessful()) {
                    String responseCode = "Code " + response.code();
                    textViewResult.setText(responseCode);
                    return;
                }

                Post postResponse = response.body();

                StringBuilder content = new StringBuilder();
                content.append("Code: ").append(response.code()).append("\n");
                content.append("ID: ").append(postResponse.getId()).append("\n");
                content.append("User ID: ").append(postResponse.getUserId()).append("\n");
                content.append("Title : ").append(postResponse.getTitle()).append("\n");
                content.append("Text : ").append(postResponse.getText()).append("\n\n");
                textViewResult.append(content);

            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                textViewResult.setText(t.getMessage());

            }
        });

    }

    private void deletePost() {
        Call<Void> call = jsonPlaceHolderApi.deletePost(5);

        call.enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                String responseCode = "Code :" + response.code();
                textViewResult.setText(responseCode);
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                textViewResult.setText(t.getMessage());

            }
        });
    }
}
