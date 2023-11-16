package vazlo.refaccionarias.data.model.eventosData

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CargarMarcadoresClientesResponse(
    @SerialName("estado")
    val estado: Int,
    @SerialName("mensaje")
    val mensaje: String,
    @SerialName("MarcadoresClientes")
    val marcadoresCliente: List<Marcador>
)
