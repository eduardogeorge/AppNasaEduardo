package web;

import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by George on 30/06/2016.
 */
public interface NasaPicServerWebservice {
    @GET("/feed/list")
    void getFeed(@Query("page") int page, Callback<FeedDTO> callback);

    @GET("/feed/best")
    void getBest(@Query("page") int page, Callback<FeedDTO> callback);

}
