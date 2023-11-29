package vazlo.refaccionarias.data.model.detallesData

import androidx.compose.runtime.saveable.listSaver
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Sucursal(
    @SerialName("nombre")
    val nombre: String,
    @SerialName("existencia")
    val existencia: String,
    @SerialName("id")
    val idSuc: String
) {
    companion object {
        val Saver = listSaver<List<Sucursal>, Any>(
            save = { lista ->
                lista.flatMap { sucursal ->
                    listOf(
                        sucursal.nombre,
                        sucursal.existencia,
                        sucursal.idSuc
                    )
                }
            },
            restore = { lista ->
                lista.chunked(3).map {
                    Sucursal(
                        it[0] as String,
                        it[1] as String,
                        it[2] as String
                    )
                }
            }
        )
    }
}
