package vazlo.refaccionarias.navigation

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import vazlo.refaccionarias.data.LocationService
import vazlo.refaccionarias.ui.eventos.Eventos
import vazlo.refaccionarias.ui.eventos.EventosScreen
import vazlo.refaccionarias.ui.screens.busquedaPorPartes.BusquedaPorPartesDestination
import vazlo.refaccionarias.ui.screens.busquedaPorPartes.BusquedaPorPartesScreen
import vazlo.refaccionarias.ui.screens.cart.CartDestination
import vazlo.refaccionarias.ui.screens.cart.CartScreen
import vazlo.refaccionarias.ui.screens.catalagoElectronico.CatElectronicoScreen
import vazlo.refaccionarias.ui.screens.catalagoElectronico.CatalogoElectronicoDestination
import vazlo.refaccionarias.ui.screens.catalagoElectronico.ResultadosCatElDestination
import vazlo.refaccionarias.ui.screens.catalagoElectronico.ResultadosCatElScreen
import vazlo.refaccionarias.ui.screens.conversiones.ConversionesDestination
import vazlo.refaccionarias.ui.screens.conversiones.ConversionesScreen
import vazlo.refaccionarias.ui.screens.detallesParte.DetallesParteDestination
import vazlo.refaccionarias.ui.screens.detallesParte.DetallesParteScreen
import vazlo.refaccionarias.ui.screens.folletosQuincenales.FolletosQuincelasScreen
import vazlo.refaccionarias.ui.screens.folletosQuincenales.FolletosQuincenalesDestination
import vazlo.refaccionarias.ui.screens.folletosQuincenales.PdfDestination
import vazlo.refaccionarias.ui.screens.folletosQuincenales.PdfScreen
import vazlo.refaccionarias.ui.screens.home.HomeContent
import vazlo.refaccionarias.ui.screens.home.HomeDestination
import vazlo.refaccionarias.ui.screens.login.LoginDestination
import vazlo.refaccionarias.ui.screens.login.LoginScreen
import vazlo.refaccionarias.ui.screens.mamoan.MamoanDestination
import vazlo.refaccionarias.ui.screens.mamoan.MamoanScreen
import vazlo.refaccionarias.ui.screens.notificaciones.NotificacionesDestination
import vazlo.refaccionarias.ui.screens.notificaciones.NotificacionesScreen
import vazlo.refaccionarias.ui.screens.pedidos.DetailsPedidos
import vazlo.refaccionarias.ui.screens.pedidos.DetallesPedidoScreen
import vazlo.refaccionarias.ui.screens.pedidos.PedidoCompartidoViewModel
import vazlo.refaccionarias.ui.screens.pedidos.PedidosDestination
import vazlo.refaccionarias.ui.screens.pedidos.PedidosScreen
import vazlo.refaccionarias.ui.screens.resultadoPorPartes.ProductoCompartidoViewModel
import vazlo.refaccionarias.ui.screens.resultadoPorPartes.ResultadoPorPartesDestination
import vazlo.refaccionarias.ui.screens.resultadoPorPartes.ResultadoPorPartesScreen
import vazlo.refaccionarias.ui.screens.usuarios_y_permisos.PermisosDestination
import vazlo.refaccionarias.ui.screens.usuarios_y_permisos.PermisosScreen
import vazlo.refaccionarias.ui.screens.usuarios_y_permisos.UsuariosScreen
import vazlo.refaccionarias.ui.screens.usuarios_y_permisos.UsuariosYPermisosDestination


@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun RefaccionariNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier,
) {
    val locationService = LocationService()
    val slideIn = slideInHorizontally(
        initialOffsetX = { 1500 },
        animationSpec = tween(500)
    )
    val slideOut = slideOutHorizontally(
        targetOffsetX = { -1500 },
        animationSpec = tween(500)
    )
    val viewModelStoreOwner = checkNotNull(LocalViewModelStoreOwner.current) {
        "No ViewModelStoreOwner was provided via LocalViewModelStoreOwner"
    }
    val viewModelCompartido = viewModel<ProductoCompartidoViewModel>(viewModelStoreOwner = viewModelStoreOwner)
    val pedidoCompartido: PedidoCompartidoViewModel = viewModel()
    NavHost(
        navController = navController,
        startDestination = LoginDestination.route,
        modifier = modifier
    ) {
        composable(route = LoginDestination.route){
            LoginScreen(navigateToHome = { navController.navigate(HomeDestination.route) } )
        }
        composable(route = HomeDestination.route) {
            HomeContent(
                navController = navController,
            )
        }
        composable(route = CatalogoElectronicoDestination.route) {
            CatElectronicoScreen(
                navigateBack = { navController.popBackStack() },
                navigateToResultCatElectronico = { anio: String, marca: String, modelo: String, cilindraje: String, litros: String ->
                    navController.navigate("${ResultadosCatElDestination.route}/$anio/$marca/$modelo/$cilindraje/$litros")
                }
            )
        }
        composable(
            route = ResultadosCatElDestination.routeWithArgs,
            arguments = listOf(
                navArgument(ResultadosCatElDestination.anio) {
                    type = NavType.StringType
                },
                navArgument(ResultadosCatElDestination.marca) {
                    type = NavType.StringType
                },
                navArgument(ResultadosCatElDestination.modelo) {
                    type = NavType.StringType
                },
                navArgument(ResultadosCatElDestination.cilindraje) {
                    type = NavType.StringType
                },
                navArgument(ResultadosCatElDestination.litros) {
                    type = NavType.StringType
                },
            )
        ) {
            ResultadosCatElScreen(
                navigateBack = { navController.popBackStack() },
                navigateToDetallesParte = { criterio: String ->
                    navController.navigate("${DetallesParteDestination.route}/$criterio")
                },
                viewModelCompartido = viewModelCompartido
            )
        }

        composable(
            route = DetallesParteDestination.routeWithArgs,
            enterTransition = { slideIn },
            exitTransition = { slideOut },
            arguments = listOf(
                navArgument(DetallesParteDestination.criterioArg){
                    type = NavType.StringType
                }
            )
        ) {
            DetallesParteScreen(
                navigateBack = { navController.popBackStack() },
                navigateToSelf = { criterio: String ->
                    navController.navigate("${DetallesParteDestination.route}/$criterio")
                },
                navigateToHome = { navController.popBackStack(HomeDestination.route, false) },
                viewModelCompartido = viewModelCompartido
            )
        }

        composable(route = BusquedaPorPartesDestination.route) {
            BusquedaPorPartesScreen(
                navigateBack = { navController.popBackStack() },
                navigateToResultadoParte = { criterio: String, funcArg: String ->
                    navController.navigate("${ResultadoPorPartesDestination.route}/$criterio/$funcArg")
                }
            )
        }
        composable(route = ConversionesDestination.route) {
            ConversionesScreen(
                navigateBack = { navController.popBackStack() },
                navigateToResultadoParte = { criterio: String, funcArg: String ->
                    navController.navigate("${ResultadoPorPartesDestination.route}/$criterio/$funcArg")
                }
            )
        }

        composable(route = MamoanDestination.route) {
            MamoanScreen(
                navigateBack = { navController.popBackStack() },
                navigateToResultadoParte = { criterio: String, funcArg: String ->
                    navController.navigate("${ResultadoPorPartesDestination.route}/$criterio/$funcArg")
                }
            )

        }
        composable(
            route = ResultadoPorPartesDestination.routeWithArgs,
            enterTransition = { slideIn },
            exitTransition = { slideOut },
            arguments = listOf(
                navArgument(ResultadoPorPartesDestination.criterio) {
                    type = NavType.StringType
                },
                navArgument(ResultadoPorPartesDestination.funcArg) {
                    type = NavType.StringType
                },
            )
        ) {
            ResultadoPorPartesScreen(
                navigateBack = { navController.popBackStack() },
                navigateToDetallesParte = { criterio: String ->
                    navController.navigate("${DetallesParteDestination.route}/$criterio")
                },
                viewModelCompartido = viewModelCompartido
            )
        }
        composable(route = PedidosDestination.route) {
            PedidosScreen(
                navigateBack = { navController.popBackStack() },
                navigateToDetallesPedido = { navController.navigate(DetailsPedidos.route) },
                sharedViewModel = pedidoCompartido
            )
        }
        composable(route = DetailsPedidos.route) {
            DetallesPedidoScreen(
                navigateBack = { navController.popBackStack() },
                sharedViewModel = pedidoCompartido
            )
        }
        composable(route = CartDestination.route) {
            CartScreen(
                navigateBack = { navController.popBackStack() },
                navigateToBusquedaParte = { navController.navigate(BusquedaPorPartesDestination.route) }
            )
        }
        composable(route = UsuariosYPermisosDestination.route) {
            UsuariosScreen(
                navigateBack = { navController.popBackStack() },
                navigateToPermisos = {navController.navigate("${PermisosDestination.route}/${it}") }
            )
        }
        composable(
            route = PermisosDestination.routeWithArgs,
            arguments = listOf(navArgument(PermisosDestination.itemIdArg) {
                type = NavType.StringType
            })
        ) {
            PermisosScreen(
                navigateBack = { navController.popBackStack() }
            )
        }
        composable(route = NotificacionesDestination.route) {
            NotificacionesScreen(navigateBack = { navController.popBackStack() })
        }
        composable(route = FolletosQuincenalesDestination.route) {
            FolletosQuincelasScreen(
                navigateBack = { navController.popBackStack() },
                navigateToPdfView = { navController.navigate("${PdfDestination.route}/${it}")}
            )
        }
        composable(
            route = PdfDestination.routeWithArgs,
            enterTransition = { slideIn },
            exitTransition = { slideOut },
            arguments = listOf(
                navArgument(PdfDestination.pdf) {
                    type = NavType.StringType
                },
            )
        ) {
            PdfScreen(
                navigateBack = { navController.popBackStack() },
                navigateHome = {
                    navController.popBackStack(
                        route = HomeDestination.route,
                        inclusive = false
                    )
                })
        }
        composable(route = Eventos.route) {
            EventosScreen(navigateBack = { navController.popBackStack() }, locationServices = locationService)
        }
    }
}