package vazlo.refaccionarias.data.model.users_y_permisosData

import androidx.annotation.StringRes
import androidx.compose.runtime.MutableState

data class Permisos(
    @StringRes val title: Int?=null,
    @StringRes val infExtra: Int?=null,
    var checkState: MutableState<Boolean>,
)




