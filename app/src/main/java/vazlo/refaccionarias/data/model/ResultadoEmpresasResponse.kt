package vazlo.refaccionarias.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import vazlo.refaccionarias.data.model.Empresas

@Serializable
data class ResultadoEmpresasResponse(
    @SerialName("estado")
    val estado: Int,
    @SerialName("mensaje")
    val mensaje: String,
    @SerialName("total")
    val total: String,
    @SerialName("lineas")
    val empresas: List<Empresas>
)