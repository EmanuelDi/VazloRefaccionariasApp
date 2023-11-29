package vazlo.refaccionarias.data.model.detallesData

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ListaConversionesData (
    @SerialName("resultado") val resultado: String
)