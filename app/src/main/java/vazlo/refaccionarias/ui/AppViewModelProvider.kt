package vazlo.refaccionarias.ui

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import vazlo.refaccionarias.RefaccionariasApplication
import vazlo.refaccionarias.ui.screens.busquedaPorPartes.BusquedaPorParteViewModel
import vazlo.refaccionarias.ui.screens.eventos.RefacCercanasViewModel
import vazlo.refaccionarias.ui.screens.cart.CartViewModel
import vazlo.refaccionarias.ui.screens.catalagoElectronico.CatElectronicoViewModel
import vazlo.refaccionarias.ui.screens.catalagoElectronico.ResultadosCatElViewModel
import vazlo.refaccionarias.ui.screens.conversiones.ConversionesViewModel
import vazlo.refaccionarias.ui.screens.detallesParte.DetallesParteViewModel
import vazlo.refaccionarias.ui.screens.estadisticas.EstadisticaWebViewModel
import vazlo.refaccionarias.ui.screens.estadisticas.EstadisticaApartadosViewModel
import vazlo.refaccionarias.ui.screens.folletosQuincenales.FolletosQuincenalesViewModel
import vazlo.refaccionarias.ui.screens.folletosQuincenales.PdfViewViewModel
import vazlo.refaccionarias.ui.screens.guia.GuiasViewModel
import vazlo.refaccionarias.ui.screens.home.HomeViewModel
import vazlo.refaccionarias.ui.screens.login.LoginViewModel
import vazlo.refaccionarias.ui.screens.mamoan.MamoanViewModel
import vazlo.refaccionarias.ui.screens.notificaciones.NotificacionesViewModel
import vazlo.refaccionarias.ui.screens.pedidos.PedidosViewModel
import vazlo.refaccionarias.ui.screens.resultadoPorPartes.ResultadoPorPartesViewModel
import vazlo.refaccionarias.ui.screens.soporteTecnico.SoporteTecnicoViewModel
import vazlo.refaccionarias.ui.screens.usuarios_y_permisos.PermisosViewModel
import vazlo.refaccionarias.ui.screens.usuarios_y_permisos.UsuariosViewModel

object AppViewModelProvider {
    @RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
    val Factory = viewModelFactory {
        // Initializer for ItemEditViewModel
        initializer {
            val webServicesRefacRepository = refaccionariasApplication().container.webServicesRefacRepository
            val sesion = refaccionariasApplication().sesion
            val servicesAppRepository = refaccionariasApplication().container.servicesAppRepository
            LoginViewModel(
                webServicesRefacRepository = webServicesRefacRepository,
                sesion = sesion,
                servicesAppRepository = servicesAppRepository
            )
        }
        initializer {
            val sesion = refaccionariasApplication().sesion
            val servicesAppRepository = refaccionariasApplication().container.servicesAppRepository
            UsuariosViewModel(
                sesion = sesion,
                servicesAppRepository = servicesAppRepository
            )
        }
        initializer {
            val sesion = refaccionariasApplication().sesion
            val servicesAppRepository = refaccionariasApplication().container.servicesAppRepository
            HomeViewModel(
                sesion = sesion,
                servicesAppRepository = servicesAppRepository
            )
        }
        initializer {
            val sesion = refaccionariasApplication().sesion
            val servicesAppRepository = refaccionariasApplication().container.servicesAppRepository
            PermisosViewModel(
                this.createSavedStateHandle(),
                sesion = sesion,
                servicesAppRepository = servicesAppRepository
            )
        }
        initializer {
            val sesion = refaccionariasApplication().sesion
            val servicesAppRepository = refaccionariasApplication().container.servicesAppRepository
            ResultadoPorPartesViewModel(
                this.createSavedStateHandle(),
                sesion = sesion,
                servicesAppRepository = servicesAppRepository
            )
        }
        initializer {
            val sesion = refaccionariasApplication().sesion
            val servicesAppRepository = refaccionariasApplication().container.servicesAppRepository
            PedidosViewModel(
                sesion = sesion,
                servicesAppRepository = servicesAppRepository
            )
        }
        initializer {
            val sesion = refaccionariasApplication().sesion
            val servicesAppRepository = refaccionariasApplication().container.servicesAppRepository
            CartViewModel(
                sesion = sesion,
                servicesAppRepository = servicesAppRepository
            )
        }
        initializer{
            CatElectronicoViewModel(
                refaccionariasApplication().sesion,
                refaccionariasApplication().container.servicesAppRepository
            )
        }
        initializer{
            val sesion = refaccionariasApplication().sesion
            val servicesAppRepository = refaccionariasApplication().container.servicesAppRepository
            ResultadosCatElViewModel(
                this.createSavedStateHandle(),
                sesion = sesion,
                servicesAppRepository = servicesAppRepository
            )
        }
        initializer {
            val sesion = refaccionariasApplication().sesion
            val servicesAppRepository = refaccionariasApplication().container.servicesAppRepository
            DetallesParteViewModel(
                this.createSavedStateHandle(),
                sesion = sesion,
                servicesAppRepository = servicesAppRepository
            )
        }
        initializer {
            val sesion = refaccionariasApplication().sesion
            val servicesAppRepository = refaccionariasApplication().container.servicesAppRepository
            NotificacionesViewModel(
                sesion = sesion,
                servicesAppRepository = servicesAppRepository
            )
        }
        initializer {
            val sesion = refaccionariasApplication().sesion
            val servicesAppRepository = refaccionariasApplication().container.servicesAppRepository
            FolletosQuincenalesViewModel(
                sesion = sesion,
                servicesAppRepository = servicesAppRepository
            )
        }
        initializer {
            PdfViewViewModel(this.createSavedStateHandle())
        }
        initializer {
            val sesion = refaccionariasApplication().sesion
            val servicesAppRepository = refaccionariasApplication().container.servicesAppRepository
            RefacCercanasViewModel(
                sesion, servicesAppRepository
            )
        }
        initializer {
            val sesion = refaccionariasApplication().sesion

            GuiasViewModel(
                sesion=sesion
            )
        }
        initializer {
            val sesion = refaccionariasApplication().sesion
            refaccionariasApplication().container.servicesAppRepository

            SoporteTecnicoViewModel(
                sesion=sesion,
            )
        }
        initializer {
            val sesion = refaccionariasApplication().sesion
            refaccionariasApplication().container.servicesAppRepository
            EstadisticaWebViewModel(
                sesion=sesion,
                savedStateHandle = this.createSavedStateHandle()
            )
        }
        initializer {
            val sesion = refaccionariasApplication().sesion
            val servicesAppRepository = refaccionariasApplication().container.servicesAppRepository
            EstadisticaApartadosViewModel(
                sesion=sesion,
                servicesAppRepository = servicesAppRepository
            )
        }
        initializer {
            BusquedaPorParteViewModel()
        }
        initializer {
            ConversionesViewModel()
        }
        initializer {
            MamoanViewModel()
        }
    }
}


fun CreationExtras.refaccionariasApplication(): RefaccionariasApplication =
    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as RefaccionariasApplication)

