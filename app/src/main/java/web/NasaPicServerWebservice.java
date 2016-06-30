package web;

import api.FeedDTO;
import retrofit2.Callback;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NasaPicServerWebservice {
    @GET("/feed/list")
    void getFeed(@Query("page") int page, Callback<FeedDTO> callback);

    @GET("/feed/best")
    void getBest(@Query("page") int page, Callback<FeedDTO> callback);

}
