package epf.m1.min2.ProjetMaterielsMobiles.api;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {

    String JSONURL = "\n" +
            "https://velib-metropole-opendata.smoove.pro/opendata/Velib_Metropole/";

    @GET("station_information.json")
    Call<String> getString();




}
