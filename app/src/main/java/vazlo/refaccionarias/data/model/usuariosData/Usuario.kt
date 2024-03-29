package vazlo.refaccionarias.data.model.usuariosData

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


@Serializable
data class Usuario(
    @SerialName("cliente_id")
    val clienteId: String? = "",
    @SerialName("cliente_nombre")
    val clienteNombre: String? = "",
    @SerialName("domicilio")
    val domicilio: String? = "",
    @SerialName("poblacion")
    val poblacion: String? = "",
    @SerialName("correo")
    val correo: String? = "",
    @SerialName("correo1")
    val correo1: String? = "",
)

