package vazlo.refaccionarias.bd

import kotlinx.serialization.json.JsonElement
import kotlinx.serialization.json.JsonObject
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET

interface WebServiceRefa {

    @GET("listadeURLparaSeccionesAppRefaccionarias.php?permiso=90aa00387e45abdd033d580a30839bd6")
    suspend fun getWebServices(): Response<JsonObject>

}