package vazlo.refaccionarias.data.bd
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonElement
import vazlo.refaccionarias.data.model.catalagoElectronicoData.CargarAniosResponse
import vazlo.refaccionarias.data.model.catalagoElectronicoData.CargarMarcasResponse
import vazlo.refaccionarias.data.model.catalagoElectronicoData.CargarModelosResponse
import vazlo.refaccionarias.data.model.catalagoElectronicoData.CargarMotorResponse
import vazlo.refaccionarias.data.model.usuariosData.CargarUsuariosResponse
import vazlo.refaccionarias.data.model.folletosQuinceData.FolletorResponse
import vazlo.refaccionarias.data.model.loginData.Login
import vazlo.refaccionarias.data.model.notifData.NotificacionesResponse
import vazlo.refaccionarias.data.model.users_y_permisosData.PermisosResponse
import vazlo.refaccionarias.data.model.homeData.ProductosNuevosResponse
import vazlo.refaccionarias.data.model.busquedasData.ResponseProductsCart
import vazlo.refaccionarias.data.model.catalagoElectronicoData.ResultadoEmpresasResponse
import vazlo.refaccionarias.data.model.busquedasData.ResultadoPartes
import vazlo.refaccionarias.data.model.detallesData.Verificar360Response
import kotlinx.serialization.json.JsonObject
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PUT
import retrofit2.http.Query
import retrofit2.http.Url
import vazlo.refaccionarias.data.model.detallesData.ListaConversionesData
import vazlo.refaccionarias.data.model.eventosData.CargarMarcadoresClientesResponse
import vazlo.refaccionarias.data.model.notifData.Mensaje
import vazlo.refaccionarias.data.model.homeData.PromosResponse

interface ServicesApp {
    @GET
    suspend fun login(
        @Url url: String,
        @Query("usuario") usuario: String,
        @Query("clave") clave: String,
    ): Login

    @GET
    suspend fun cargarUsuarios(
        @Url url: String,
        @Query("id_cte") idCte: String
    ) : CargarUsuariosResponse

    @GET
    suspend fun nuevoUsuario(
        @Url url: String,
        @Query("cliente_id") cliente: String,
        @Query("cliente_responsable") clienteResponsable: String,
        @Query("cliente_nombre") clienteNonbre: String,
        @Query("cliente_psw") clientePsw: String,
        @Query("domicilio") domicilio: String,
        @Query("poblacion") poblacion: String,
        @Query("correo") correo: String,
        @Query("correo1") correo1: String,
    ) : Response<JsonObject>


    @GET
    suspend fun actualizarUsuario(
        @Url url: String,
        @Query("cliente_id") clienteId: String,
        @Query("cliente_nombre") clienteNonbre: String,
        @Query("domicilio") domicilio: String,
        @Query("poblacion") poblacion: String,
        @Query("correo") correo: String,
        @Query("correo1") correo1: String,
    ): Response<JsonObject>


    @PUT
    suspend fun actualizarPassword(
        @Url url: String,
        @Query("cliente_id") clienteId: String,
        @Query("cliente_psw") clientePsw: String
    ): Response<JsonObject>

    @DELETE
    suspend fun eliminarUsuario(
        @Url url: String,
        @Query("id_cte") cte: String
    ): Response<JsonObject>

    @GET
    suspend fun verificarPermisos(
        @Url url: String,
        @Query("id_cte") idCte: String
    ): PermisosResponse

    @GET
    suspend fun actualizarPermisos(
        @Url url: String,
        @Query("id_cte") idCte: String,
        @Query("p1") p1: Int,
        @Query("p2") p2: Int,
        @Query("p3") p3: Int,
        @Query("p4") p4: Int,
        @Query("p5") p5: Int,
        @Query("p6") p6: Int,
        @Query("p7") p7: Int,
        @Query("p8") p8: Int,
        @Query("p9") p9: Int,
        @Query("p10") p10: Int,
        @Query("p11") p11: Int,
        @Query("p12") p12: Int,

    ): Response<JsonObject>

    @GET
    suspend fun cargarProductosNuevos(
        @Url url: String,
        @Query("id_user") user: String
    ): ProductosNuevosResponse

    @GET
    suspend fun  cargarProductos(
        @Url url: String,
        @Query("id_user") idUser: String,
        @Query("soporte") soporte: String,
        @Query("tipo") tipo: Int
    ): ResultadoPartes

    @GET
    suspend fun cargarProductosMamaoan(
        @Url url: String,
        @Query("id_user") idUser: String,
        @Query("bmamoan") bmamoan: String,
    ): ResultadoPartes

    @GET
    suspend fun  cargarProductosConversiones(
        @Url url: String,
        @Query("id_user") idUser: String,
        @Query("soporte") soporte: String,
        @Query("tipo") tipo: Int
    ): ResultadoPartes

    @GET
    suspend fun cargarPedidos(
        @Url url: String,
        @Query("id_cte") idCte: String
    ): Response<JsonObject>

    @GET
    suspend fun cargarProductosCarrito(
        @Url url: String,
        @Query("id_cte") idCte: String
    ): ResponseProductsCart

    @GET
    suspend fun getAnios(
        @Url url: String,
    ): CargarAniosResponse

    @GET
    suspend fun getMarcas(
        @Url url: String,
        @Query("anio") anio: String
    ): CargarMarcasResponse

    @GET
    suspend fun getModelos(
        @Url url: String,
        @Query("anio") anio: String,
        @Query("marca") marca: String
    ): CargarModelosResponse

    @GET
    suspend fun getMotores(
        @Url url: String,
        @Query("anio") anio: String,
        @Query("marca") marca: String,
        @Query("modelo") modelo: String
    ): CargarMotorResponse

    @GET
    suspend fun getProductosLineas(
        @Url url: String,
        @Query("litros") litros: String,
        @Query("modelo") modelo: String,
        @Query("marca") marca: String,
        @Query("cilindraje") cilindraje: String,
        @Query("anio") anio: String,
        @Query("linea") linea: String,
        @Query("id_user") idUser: String
    ): ResultadoPartes

    @GET
    suspend fun getEmpresas(
        @Url url: String,
        @Query("litros") litros: String,
        @Query("cilindraje") cilindraje: String,
        @Query("anio") anio: String,
        @Query("modelo") modelo: String,
        @Query("marca") marca: String,
        @Query("usuario") usuario: String,
        @Query("tipo") tipo: String
    ): ResultadoEmpresasResponse

    @GET
    suspend fun actualizarCantidadProucto(
        @Url url: String,
        @Query("id_cte") idCte: String,
        @Query("cantidad") cantidad: Int,
        @Query("nomsop") nomsop: String,
    ): Response<JsonObject>

    @GET
    suspend fun eliminarProdCarrito(
        @Url url: String,
        @Query("id_cte") idCte: String,
        @Query("soporte") soporte: String
    ): Response<JsonObject>

    @GET
    suspend fun vaciarCarrito(
        @Url url: String,
        @Query("id_cte") idCte: String,
    ): Response<JsonObject>

    @GET
    suspend fun enviarCarrito(
        @Url url: String,
        @Query("id_cte") idCte: String,
        @Query("iteracion") iteracion: String,
        @Query("subtotal") subtotal: String,
        @Query("iva") iva: String,
        @Query("total") total: String,
        @Query("comentarios") comentarios: String
    ): Response<JsonObject>

    @GET suspend fun getProductosDetalle(
        @Url url: String,
        @Query("soporte") soporte: String
    ): ResultadoPartes

    @GET
    suspend fun verificar360(
        @Url url: String,
        @Query("soporte") soporte: String
    ): Verificar360Response

    @GET
    suspend fun getSugeridoGuias(
        @Url url: String,
        @Query("marca") marca: String,
        @Query("modelo") modelo: String,
        @Query("cil") cilindraje: String,
        @Query("lit") litros: String,
        @Query("aIni") anioInicial: String,
        @Query("aFin") anioFinal: String,
        @Query("id_cte") idCte: String
    ): ResultadoPartes



    @GET
    suspend fun getNotificaciones(
        @Url url: String,
        @Query("id_cliente") icCte: String,
        @Query("token") token: String
    ): NotificacionesResponse

    @GET
    suspend fun obtenerMensajesService (
        @Url url: String,
        @Query("id_cliente") icCte: String,
        @Query("token") token: String
    ): Call<List<Mensaje>>

    @GET
    suspend fun darBajaNotificaciones(
        @Url url: String,
        @Query("id") id: String
    ): Response<JsonObject>


    @GET
    suspend fun getFolletos(
        @Url url: String,
        @Query("id_user") idUser: String
    ): FolletorResponse

    @GET
    suspend fun getMarcadores(
        @Url url: String,
        @Query("lat") lat: Double,
        @Query("long") long: Double
    ): CargarMarcadoresClientesResponse

    @GET
    suspend fun getPromos(
        @Url url: String,
        @Query("id_user") idUser: String
    ): PromosResponse

    @GET
    suspend fun subirABackorder(
        @Url url: String,
        @Query("id_cte") idCte: String,
        @Query("prod") nomsop: String,
        @Query("cant") cant: Int,
        @Query("id_sucursal") sucursal: String
    ): Response<JsonObject>

    @GET
    suspend fun getListaConversiones(
        @Url url: String,
        @Query("s") soporte: String,
        @Query("c") cliente: String
    ): ListaConversionesData

}