package vazlo.refaccionarias.data.model.usuariosData

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CargarUsuariosResponse(
    @SerialName("estado")
    val estado: Int,
    @SerialName("mensaje")
    val mensaje: String,
    @SerialName("Info_Usuarios")
    val infoUsuarios: MutableList<Usuario>

)
