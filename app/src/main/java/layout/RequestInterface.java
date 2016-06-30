package layout;

import model.ServerRequest;
import model.ServerResponse;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;


public interface RequestInterface {
    @POST("AppEmiolo/")
    Call<ServerResponse> operation(@Body ServerRequest request);
}
