package atmus.atmus.rest;

import atmus.atmus.model.RegResponse;
import atmus.atmus.model.UserModel;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface ApiInterface {
    @POST("/app/auth")
    Call<UserModel> doLogin(@Query("email") String mail, @Query("password") String password);

    @GET("/gcm/reg/{token}/{reg_id}")
    Call<RegResponse> reg(@Path("token") String token, @Path("reg_id") String reg_id);

    @GET("/gcm/unreg/{token}")
    Call<RegResponse> unreg(@Path("token") String token);
}
