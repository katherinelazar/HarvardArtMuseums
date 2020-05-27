package hu.bme.aut.bottomnavfragmentsdemo.network

import hu.bme.aut.bottomnavfragmentsdemo.data.Base
import retrofit2.http.GET
import retrofit2.http.Query

//host: https://api.harvardartmuseums.org/
// 7822fc70-8d6a-11ea-bfab-8bccea6df484

interface ArtMuseumsAPI {
    @GET("/object")
    fun getArtDetails(
        @Query("apikey") apikey: String
    ) : retrofit2.Call<Base>
}