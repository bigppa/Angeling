package org.sopt.angeling.Network;

/**
 * Created by DongHyun on 2016-01-13.
 */

import org.sopt.angeling.Model.Thumbnail;
import org.sopt.angeling.Model.Voluntary;

import java.util.List;

import retrofit.Call;
import retrofit.http.GET;
import retrofit.http.Path;

public interface NetworkService {

    @GET("/Search/{keyword}")
    Call<List<Thumbnail>> getSearchItem(@Path("keyword") String keyword);

    @GET("/Detail/{registno}")
    Call<Voluntary> getDetail(@Path("registno") long registno);

    @GET("/Address/{addressparams}")
    Call<List<Thumbnail>> getAddressItem(@Path("addressparams") String addressparams);

    @GET("/Thema/{type}")
    Call<List<Thumbnail>> getThemaItem(@Path("type") String type);
}
