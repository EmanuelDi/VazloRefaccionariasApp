package vazlo.refaccionarias

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import vazlo.refaccionarias.data.AppContainer
import vazlo.refaccionarias.data.DefaultAppContainer
import vazlo.refaccionarias.data.local.Sesion

private const val NAME = "preferences"
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
    name = NAME
)

class RefaccionariasApplication: Application() {
    lateinit var sesion: Sesion
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        sesion = Sesion(dataStore)
        container = DefaultAppContainer()
    }
}