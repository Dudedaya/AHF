package xyz.dudedaya.retrofitexample;

import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface JsonPlaceHolderApi {

    @GET("posts")
    Call<List<Post>> getPosts(
            @Query("userId") Integer[] userId, //We use Integer because it can be null, if this query is not in use.
                                               //An array make it possible to use multiple queries.
            @Query("_sort") String sort,
            @Query("_order") String order
    );

    @GET("posts")
    Call<List<Post>> getPosts(@QueryMap Map<String, String> parameters); // If we don't want to specify
                                                                        // all parameter we can pass them
                                                                        // as a Map<String, String>

    @GET("posts/{id}/comments")
    Call<List<Comment>> getComments(@Path("id") int postId);

    @GET()
    Call<List<Comment>> getComments(@Url String url); //Also we can pass the whole url string.
}
