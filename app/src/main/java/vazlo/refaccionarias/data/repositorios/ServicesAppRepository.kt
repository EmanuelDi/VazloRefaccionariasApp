package vazlo.refaccionarias.data.repositorios

import vazlo.refaccionarias.bd.ServicesApp
import vazlo.refaccionarias.data.model.CargarAniosResponse
import vazlo.refaccionarias.data.model.CargarMarcasResponse
import vazlo.refaccionarias.data.model.CargarModelosResponse
import vazlo.refaccionarias.data.model.CargarMotorResponse
import vazlo.refaccionarias.data.model.CargarUsuariosResponse
import vazlo.refaccionarias.data.model.FolletorResponse
import vazlo.refaccionarias.data.model.Login
import vazlo.refaccionarias.data.model.NotificacionesResponse
import vazlo.refaccionarias.data.model.PermisosResponse
import vazlo.refaccionarias.data.model.ProductosNuevosResponse
import vazlo.refaccionarias.data.model.ResponseProductsCart
import vazlo.refaccionarias.data.model.ResultadoEmpresasResponse
import vazlo.refaccionarias.data.model.ResultadoPartes
import vazlo.refaccionarias.data.model.Verificar360Response
import kotlinx.serialization.json.JsonObject
import retrofit2.Call
import retrofit2.Response
import vazlo.refaccionarias.data.model.CargarMarcadoresClientesResponse
import vazlo.refaccionarias.data.model.Mensaje
import vazlo.refaccionarias.data.model.PromosResponse
import vazlo.refaccionarias.data.model.Sucursal

interface ServicesAppRepository {
    suspend fun login(url: String?, usuario: String, clave: String): Login
    suspend fun cargarUsuarios(url: String?, idCte: String): CargarUsuariosResponse
    suspend fun nuevoUsuario(
        url: String?,
        cliente: String,
        clienteResponsable: String,
        clienteNonbre: String,
        clientePsw: String,
        domicilio: String,
        poblacion: String,
        correo: String,
        correo1: String,
    ): Response<JsonObject>

    suspend fun actualizarUsuario(
        url: String,
        clienteId: String,
        clienteNonbre: String,
        domicilio: String,
        poblacion: String,
        correo: String,
        correo1: String,
    ): Response<JsonObject>

    suspend fun actualizarPassword(
        url: String,
        clienteId: String,
        clientePsw: String
    ): Response<JsonObject>

    suspend fun eliminarUsuario(
        url: String,
        cte: String
    ): Response<JsonObject>

    suspend fun verificarPermisos(
        url: String,
        idCte: String
    ): PermisosResponse

    suspend fun actualizarPermisos(
        url: String,
        idCte: String,
        p1: Int,
        p2: Int,
        p3: Int,
        p4: Int,
        p5: Int,
        p6: Int,
        p7: Int,
        p8: Int,
        p9: Int,
        p10: Int,
        p11: Int,
        p12: Int,
    ): Response<JsonObject>

    suspend fun cargarProductosNuevos(
        url: String,
        idUser:String
    ): ProductosNuevosResponse

    suspend fun cargarProductoss(
        url: String,
        soporte: String,
        idUser:String,
        tipo: Int
    ): ResultadoPartes

    suspend fun cargarProductosConversiones(
        url: String,
        soporte: String,
        idUser:String,
        tipo: Int
    ): ResultadoPartes

    suspend fun cargarProductossMamoan(
        url: String,
        bmamoan: String,
        idUser:String,
    ): ResultadoPartes

    suspend fun cargarPedidos(
        url: String,
        idCte: String
    ): Response<JsonObject>

    suspend fun cargarProductosCarrito(
        url: String,
        idCte: String
    ): ResponseProductsCart

    suspend fun getAnios(
        url: String
    ): CargarAniosResponse

    suspend fun getMarcas(
        url: String,
        anio: String
    ): CargarMarcasResponse

    suspend fun getModelos(
        url: String,
        anio: String,
        marca: String
    ): CargarModelosResponse

    suspend fun getMotores(
        url: String,
        anio: String,
        marca: String,
        modelo: String
    ): CargarMotorResponse

    suspend fun getProductosLineas(
        url: String,
        litros: String,
        modelo: String,
        marca: String,
        cilindraje: String,
        anio: String,
        linea: String,
        idUser: String
    ): ResultadoPartes

    suspend fun getEmpresas(
        url: String,
        litros: String,
        cilindraje: String,
        anio: String,
        modelo: String,
        marca: String,
        usuario: String,
        tipo: String
    ): ResultadoEmpresasResponse

    suspend fun actualizarCantidadProucto(
        url: String,
        idCte: String,
        cantidad: Int,
        nomsop: String
    ): Response<JsonObject>

    suspend fun eliminarProdCarrito(
        url: String,
        idCte: String,
        soporte: String
    ): Response<JsonObject>

    suspend fun vaciarCarrito(
        url: String,
        idCte: String
    ): Response<JsonObject>

    suspend fun enviarCarrito(
        url: String,
        idCte: String,
        iteracion: String,
        subtotal: String,
        iva: String,
        total: String,
        comentarios: String
    ): Response<JsonObject>

    suspend fun getProductosDetalle(url: String, soporte: String): ResultadoPartes

    suspend fun getSugeridoGuias(
        url: String,
        marca: String,
        modelo: String,
        cilindraje: String,
        litros: String,
        anioInicial: String,
        anioFinal: String,
        idCte: String
    ): ResultadoPartes

    suspend fun verificar360(url: String, soporte: String): Verificar360Response


    suspend fun cargarNotificaciones(url: String, idCte: String, token: String): NotificacionesResponse

    suspend fun obtenerMensajes(url: String, idCliente: String, token: String): Call<List<Mensaje>>

    suspend fun getFolletos(url: String, idUser: String): FolletorResponse

    suspend fun getMarcadores(url: String, lat: Double, long: Double): CargarMarcadoresClientesResponse

    suspend fun getPromos(url: String, idUser: String): PromosResponse

    suspend fun agregarBackOrder(url: String, idCte: String, nomsop: String, cant: Int, sucursal: String): Response<JsonObject>

    suspend fun bajarNotificacion(url: String, id: String): Response<JsonObject>

}

class NetServicesApp(
    private val servicesApp: ServicesApp
) : ServicesAppRepository {
    override suspend fun login(url: String?, usuario: String, clave: String): Login {
        return servicesApp.login(url!!, usuario, clave)
    }

    override suspend fun cargarUsuarios(url: String?, idCte: String): CargarUsuariosResponse {
        return servicesApp.cargarUsuarios(url!!, idCte)
    }

    override suspend fun nuevoUsuario(
        url: String?,
        cliente: String,
        clienteResponsable: String,
        clienteNonbre: String,
        clientePsw: String,
        domicilio: String,
        poblacion: String,
        correo: String,
        correo1: String
    ): Response<JsonObject> {
        return servicesApp.nuevoUsuario(
            url = url!!,
            cliente,
            clienteResponsable,
            clienteNonbre,
            clientePsw,
            domicilio,
            poblacion,
            correo,
            correo1
        )
    }

    override suspend fun actualizarUsuario(
        url: String,
        clienteId: String,
        clienteNonbre: String,
        domicilio: String,
        poblacion: String,
        correo: String,
        correo1: String
    ): Response<JsonObject> {
        return servicesApp.actualizarUsuario(url, clienteId, clienteNonbre, domicilio, poblacion, correo, correo1)
    }

    override suspend fun actualizarPassword(url: String, clienteId: String, clientePsw: String): Response<JsonObject> {
        return servicesApp.actualizarPassword(url, clienteId, clientePsw)
    }

    override suspend fun eliminarUsuario(url: String, cte: String): Response<JsonObject> {
        return servicesApp.eliminarUsuario(url, cte)
    }

    override suspend fun verificarPermisos(
        url: String,
        idCte: String
    ): PermisosResponse {
        return servicesApp.verificarPermisos(url, idCte)
    }

    override suspend fun actualizarPermisos(
        url: String,
        idCte: String,
        p1: Int,
        p2: Int,
        p3: Int,
        p4: Int,
        p5: Int,
        p6: Int,
        p7: Int,
        p8: Int,
        p9: Int,
        p10: Int,
        p11: Int,
        p12: Int,

    ): Response<JsonObject> {
        return servicesApp.actualizarPermisos(url,idCte, p1, p2, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12)
    }

    override suspend fun cargarProductosNuevos(url: String, idUser: String): ProductosNuevosResponse {
        return servicesApp.cargarProductosNuevos(url, idUser)
    }


    override suspend fun cargarProductoss(
        url: String,
        soporte: String,
        idUser: String,
        tipo: Int
    ): ResultadoPartes {
        return servicesApp.cargarProductos(url, idUser, soporte, tipo)
    }

    override suspend fun cargarProductosConversiones(
        url: String,
        soporte: String,
        idUser: String,
        tipo: Int
    ): ResultadoPartes {
        return servicesApp.cargarProductosConversiones(url, idUser, soporte, tipo)
    }

    override suspend fun cargarProductossMamoan(
        url: String,
        bmamoan: String,
        idUser: String
    ): ResultadoPartes {
        return servicesApp.cargarProductosMamaoan(url, idUser, bmamoan)
    }

    override suspend fun cargarPedidos(url: String, idCte: String): Response<JsonObject> {
        return servicesApp.cargarPedidos(url, idCte)
    }

    override suspend fun cargarProductosCarrito(url: String, idCte: String): ResponseProductsCart {
        return servicesApp.cargarProductosCarrito(url, idCte)
    }

    override suspend fun getAnios(
        url: String): CargarAniosResponse {
        return servicesApp.getAnios(url)
    }

    override suspend fun getMarcas(
        url: String,
        anio: String
    ): CargarMarcasResponse {
        return servicesApp.getMarcas(url, anio)
    }

    override suspend fun getModelos(
        url: String,
        anio: String,
        marca: String
    ): CargarModelosResponse {
        return servicesApp.getModelos(url, anio, marca)
    }

    override suspend fun getMotores(
        url: String,
        anio: String,
        marca: String,
        modelo: String
    ): CargarMotorResponse {
        return servicesApp.getMotores(url, anio, marca, modelo)
    }

    override suspend fun getProductosLineas(
        url: String,
        litros: String,
        modelo: String,
        marca: String,
        cilindraje: String,
        anio: String,
        linea: String,
        idUser: String
    ): ResultadoPartes {
        return servicesApp.getProductosLineas(url, litros, modelo, marca, cilindraje, anio, linea, idUser)
    }

    override suspend fun getEmpresas(
        url: String,
        litros: String,
        cilindraje: String,
        anio: String,
        modelo: String,
        marca: String,
        usuario: String,
        tipo: String
    ): ResultadoEmpresasResponse {
        return servicesApp.getEmpresas(url, litros, cilindraje, anio, modelo, marca, usuario, tipo)
    }

    override suspend fun actualizarCantidadProucto(
        url: String,
        idCte: String,
        cantidad: Int,
        nomsop: String
    ): Response<JsonObject> {
        return servicesApp.actualizarCantidadProucto(url, idCte, cantidad, nomsop)
    }

    override suspend fun eliminarProdCarrito(url: String, idCte: String, soporte: String): Response<JsonObject> {
        return servicesApp.eliminarProdCarrito(url, idCte, soporte)
    }

    override suspend fun vaciarCarrito(url: String, idCte: String): Response<JsonObject> {
        return servicesApp.vaciarCarrito(url, idCte)
    }

    override suspend fun enviarCarrito(
        url: String,
        idCte: String,
        iteracion: String,
        subtotal: String,
        iva: String,
        total: String,
        comentarios: String
    ): Response<JsonObject> {
        return servicesApp.enviarCarrito(url, idCte, iteracion, subtotal, iva, total, comentarios)
    }

    override suspend fun getProductosDetalle(url: String, soporte: String): ResultadoPartes {
        return servicesApp.getProductosDetalle(url, soporte)
    }

    override suspend fun getSugeridoGuias(
        url: String,
        marca: String,
        modelo: String,
        cilindraje: String,
        litros: String,
        anioInicial: String,
        anioFinal: String,
        idCte: String
    ): ResultadoPartes {
        return servicesApp.getSugeridoGuias(url, marca, modelo, cilindraje, litros, anioInicial, anioFinal, idCte)
    }

    override suspend fun verificar360(url: String, soporte: String): Verificar360Response {
        return servicesApp.verificar360(url, soporte)
    }

    override suspend fun cargarNotificaciones(
        url: String,
        idCte: String,
        token: String
    ): NotificacionesResponse {
        return servicesApp.getNotificaciones(url, idCte, token)
    }

    override suspend fun obtenerMensajes(
        url: String,
        idCliente: String,
        token: String
    ): Call<List<Mensaje>> {
        return servicesApp.obtenerMensajesService(url, idCliente, token)
    }

    override suspend fun getFolletos(url: String, idUser: String): FolletorResponse {
        return servicesApp.getFolletos(url, idUser)
    }

    override suspend fun getMarcadores(url: String, lat: Double, long: Double): CargarMarcadoresClientesResponse {
        return servicesApp.getMarcadores(url,  lat, long)
    }

    override suspend fun getPromos(url: String, idUser: String): PromosResponse {
        return servicesApp.getPromos(url, idUser)
    }

    override suspend fun agregarBackOrder(url: String, idCte: String, nomsop: String, cant: Int, sucursal: String): Response<JsonObject> {
        return servicesApp.subirABackorder(url, idCte, nomsop, cant, sucursal)
    }

    override suspend fun bajarNotificacion(url: String, id: String): Response<JsonObject> {
        return servicesApp.darBajaNotificaciones(url, id)
    }
}

