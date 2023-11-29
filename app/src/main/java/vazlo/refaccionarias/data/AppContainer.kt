package vazlo.refaccionarias.data

import vazlo.refaccionarias.data.bd.ServicesApp
import vazlo.refaccionarias.data.bd.WebServiceRefa
import vazlo.refaccionarias.data.repositorios.NetServicesApp
import vazlo.refaccionarias.data.repositorios.NetworkWebServicesRefacRepository
import vazlo.refaccionarias.data.repositorios.ServicesAppRepository
import vazlo.refaccionarias.data.repositorios.WebServicesRefacRepository
import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import java.util.concurrent.TimeUnit

interface AppContainer {
    val webServicesRefacRepository: WebServicesRefacRepository
    val servicesAppRepository: ServicesAppRepository
}

class DefaultAppContainer : AppContainer {

    private val json = Json { ignoreUnknownKeys = true }

    private val baseUrl =
        "https://www.vazloonline.com/sitio/web_service/LISTAS_PREFERENCIAS/"

    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(Json.asConverterFactory("application/json".toMediaType()))
        .baseUrl(baseUrl)
        .build()

    private val okHttpClient = OkHttpClient.Builder()
        .readTimeout(120, TimeUnit.SECONDS)
        .connectTimeout(120, TimeUnit.SECONDS)
        .build()

    private val retrofit2: Retrofit = Retrofit.Builder()
        .addConverterFactory(json.asConverterFactory("application/json".toMediaType()))
        .baseUrl("https://localhost/")
        .client(okHttpClient)
        .build()

    private val retrofitServicesApp: ServicesApp by lazy {
        retrofit2.create(ServicesApp::class.java)
    }

    private val retrofitService: WebServiceRefa by lazy {
        retrofit.create(WebServiceRefa::class.java)
    }

    override val webServicesRefacRepository: WebServicesRefacRepository by lazy {
        NetworkWebServicesRefacRepository(retrofitService)
    }

    override val servicesAppRepository: ServicesAppRepository by lazy {
        NetServicesApp(retrofitServicesApp)
    }

}