package vazlo.refaccionarias.ui.screens.home

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import vazlo.refaccionarias.R
import vazlo.refaccionarias.ui.screens.eventos.Eventos
import vazlo.refaccionarias.ui.screens.busquedaPorPartes.BusquedaPorPartesDestination
import vazlo.refaccionarias.ui.screens.cart.CartDestination
import vazlo.refaccionarias.ui.screens.catalagoElectronico.CatalogoElectronicoDestination
import vazlo.refaccionarias.ui.screens.estadisticas.EstadisticasDestination
import vazlo.refaccionarias.ui.screens.conversiones.ConversionesDestination
import vazlo.refaccionarias.ui.screens.folletosQuincenales.FolletosQuincenalesDestination
import vazlo.refaccionarias.ui.screens.guia.GuiaDestination
import vazlo.refaccionarias.ui.screens.login.LoginDestination
import vazlo.refaccionarias.ui.screens.mamoan.MamoanDestination
import vazlo.refaccionarias.ui.screens.notificaciones.NotificacionesDestination
import vazlo.refaccionarias.ui.screens.pedidos.PedidosDestination
import vazlo.refaccionarias.ui.screens.soporteTecnico.SoporteTecnicoDestination
import vazlo.refaccionarias.ui.screens.usuarios_y_permisos.UsuariosYPermisosDestination


data class MenuSection(
    @StringRes val title: Int,
    val items: List<MenuItem>
)

data class MenuItem(
    @DrawableRes val icon: Int,
    @StringRes val label: Int,
    val destination: String,
)


val menuItemsBusquedas = listOf(
    MenuItem(
        icon = R.drawable.cat_electronico1,
        label = R.string.catalogoelectronico,
        destination = CatalogoElectronicoDestination.route ,
    ),
    MenuItem(
        icon = R.drawable.paper,
        label = R.string.busqueda_por_partes,
        destination = BusquedaPorPartesDestination.route,
    ),
    MenuItem(
        icon = R.drawable.baseline_change_circle_24,
        label = R.string.conversiones,
        destination = ConversionesDestination.route,
    ),
    MenuItem(
        icon = R.drawable.mamoan1,
        label = R.string.marca_modelo_a_o_motor,
        destination = MamoanDestination.route,
    ),
)



val menuItemsSecciones = listOf(
    MenuItem(
        icon = R.drawable.info_tecnica1,
        label = R.string.folletos_quincenales,
        destination = FolletosQuincenalesDestination.route,
    ),
    MenuItem(
        icon = R.drawable.baseline_shopping_cart_24,
        label = R.string.carrito,
        destination = CartDestination.route,
    ),
    MenuItem(
        icon = R.drawable.list__2_,
        label = R.string.mis_pedidos,
        destination = PedidosDestination.route,
    ),
    MenuItem(
        icon = R.drawable.baseline_local_shipping,
        label = R.string.seguimiento_de_gu_as,
        destination = GuiaDestination.route,
    ),
    MenuItem(
        icon = R.drawable.calendar,
        label = R.string.eventos,
        destination = Eventos.route,
    ),
    MenuItem(
        icon = R.drawable.baseline_supervisor_account_24,
        label = R.string.usuarios_y_permisos,
        destination = UsuariosYPermisosDestination.route,
    ),
    MenuItem(
        icon = R.drawable.baseline_notifications_24,
        label = R.string.notificaciones,
        destination = NotificacionesDestination.route,
    ),
    MenuItem(
        icon = R.drawable.soporte_tecnico1,
        label = R.string.soporte_t_cnico,
        destination = SoporteTecnicoDestination.route,
    ),
    MenuItem(
        icon = R.drawable.line_chart,
        label = R.string.estadisticas,
        destination = EstadisticasDestination.route
    ),
    MenuItem(
        icon = R.drawable.salir1,
        label = R.string.salir,
        destination = LoginDestination.route,
    ),
)



val menuSections = listOf(
    MenuSection(
        title = R.string.busquedas,
        items = menuItemsBusquedas
    ),
    MenuSection(
        title = R.string.secciones,
        items = menuItemsSecciones
    )
)



