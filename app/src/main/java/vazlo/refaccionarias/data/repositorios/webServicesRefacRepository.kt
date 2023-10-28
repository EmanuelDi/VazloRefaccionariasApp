package vazlo.refaccionarias.data.repositorios

import vazlo.refaccionarias.bd.WebServiceRefa
import kotlinx.serialization.json.JsonObject
import retrofit2.Response

interface WebServicesRefacRepository {
    suspend fun getWebServices(): Response<JsonObject>
}

class NetworkWebServicesRefacRepository(
    private val webServiceRefa: WebServiceRefa
) : WebServicesRefacRepository {
    override suspend fun getWebServices(): Response<JsonObject> {
        return webServiceRefa.getWebServices()
    }
}