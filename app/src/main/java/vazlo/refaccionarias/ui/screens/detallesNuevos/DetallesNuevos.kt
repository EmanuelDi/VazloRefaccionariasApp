package vazlo.refaccionarias.ui.screens.detallesNuevos

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
import android.util.Log
import android.view.ViewGroup
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DoNotDisturbAlt
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.ListItem
import androidx.compose.material3.ListItemColors
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import kotlinx.coroutines.launch
import vazlo.refaccionarias.R
import vazlo.refaccionarias.data.model.Producto
import vazlo.refaccionarias.data.model.ProductosResult
import vazlo.refaccionarias.data.model.Sucursal
import vazlo.refaccionarias.navigation.NavigationDestination
import vazlo.refaccionarias.ui.AppViewModelProvider
import vazlo.refaccionarias.ui.screens.detallesParte.DetallesParteViewModel
import vazlo.refaccionarias.ui.screens.detallesParte.ProductosUiState
import vazlo.refaccionarias.ui.screens.resultadoPorPartes.ProductoCompartidoViewModel
import vazlo.refaccionarias.ui.theme.Blanco
import vazlo.refaccionarias.ui.theme.Negro
import vazlo.refaccionarias.ui.theme.Rojo_Vazlo
import vazlo.refaccionarias.ui.theme.VazloRefaccionariasTheme
import vazlo.refaccionarias.ui.theme.Verde_Success


object DetallesNuevoDestination : NavigationDestination {
    override val route = "detalles-nuevo"
    const val criterioArg = "criterio"
    val routeWithArgs = "$route/{$criterioArg}"
}

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun DetallesNuevoScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
    navigateToDetallesParte: (String) -> Unit,
    navigateToHome: () -> Unit,
    navigateToCart: () -> Unit,
    dPViewModel: DetallesParteViewModel = viewModel(factory = AppViewModelProvider.Factory),
    viewModelCompartido: ProductoNuevoCompartiido,
    productoCompartidoViewModel: ProductoCompartidoViewModel,
) {

    var showBottomSheet by remember {
        mutableStateOf(false)
    }
    var agregar by remember {
        mutableStateOf(false)
    }

    val view = LocalView.current

    val window = (view.context as Activity).window
    WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = showBottomSheet

    var showAlert by remember { mutableStateOf(false) }
    var showAlertError by remember { mutableStateOf(false) }

    val prod = viewModelCompartido.getProducto()
    val producto = rememberSaveable(saver = Producto.Saver) {
        mutableStateOf(prod).value
    }

    var url360 by remember { mutableStateOf("") }

    var scope = rememberCoroutineScope()

    dPViewModel.verificar360(producto.nombreArticulo!!)

    LaunchedEffect(viewModelCompartido.getProducto().nombreArticulo) {
        dPViewModel.get360()
        url360 = dPViewModel.url360 + "?soporte=" + viewModelCompartido.getProducto().nombreArticulo
        Log.i("360", url360)
    }

    var openedDialog by remember { mutableStateOf(false) }
    VazloRefaccionariasTheme {
        Scaffold(
            topBar = {
                DetallesPTopAppBar(
                    navigateBack = navigateBack,
                    navigateToHome = navigateToHome
                )
            }
        ) {
            Column(modifier.padding(it)) {
                Surface {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .background(color = MaterialTheme.colorScheme.primary),
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Text(
                            text = producto.lineaPos!!,
                            style = MaterialTheme.typography.bodyLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                        )

                    }
                }
                DetalleParte(
                    producto = producto,
                    detallesParteViewModel = dPViewModel,
                    onClick = { openedDialog = !openedDialog },
                    url360 = url360
                )

                if (openedDialog)
                    DialogOpcionesCompra(
                        onDismiss = { openedDialog = !openedDialog },
                        sucursales = producto.sucursales!!,
                        precio = producto.precio!!,
                        viewModel = dPViewModel,
                        abrirSheetCantidad = { showBottomSheet = true },
                        aCarrito = { agregar = true },
                        aBackOrder = { agregar = false }
                    )

                BottomCantidad(
                    showBottomSheet = showBottomSheet,
                    cerrarBottomSheet = { showBottomSheet = false },
                    agregar = agregar,
                    viewModel = dPViewModel,
                    cerrarOptions = { openedDialog = false },
                    showSucces = { showAlert = true },
                    showError = { showAlertError = true },
                    backOption = { openedDialog = true },
                )


                MensajeAlert(
                    onDismiss = {
                        showAlert = false
                    },
                    showAlert,
                    navigateToCart = navigateToCart
                )


                ErrorAlert(
                    onDismiss = {
                        showAlertError = false
                    },
                    showAlert = showAlertError,
                )

                when (dPViewModel.productosUiState) {
                    is ProductosUiState.Loading -> {
                        AltTable(mensaje = "Cargando")
                    }

                    is ProductosUiState.Success -> {
                        val productos =
                            (dPViewModel.productosUiState as ProductosUiState.Success).productos

                        DetallesParteContent(
                            productos = productos,
                            viewModel = dPViewModel,
                            navigateToSelf = navigateToDetallesParte,
                            viewModelCompartido = productoCompartidoViewModel,
                            showBottomSheet = showBottomSheet,
                            cerrarBottomSheet = { showBottomSheet = false },
                            agregar = agregar
                        ) { openedDialog = false }
                    }

                    is ProductosUiState.Error -> {
                        AltTable(mensaje = "Error")
                    }

                    else -> {}
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun DetallesPTopAppBar(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
    navigateToHome: () -> Unit
) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(15.dp)
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logovazloblanco_sin_texto),
                    contentDescription = "",
                    modifier = modifier.size(30.dp)
                )
                Text(
                    text = "Detalles",
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
            }
        },
        actions = {
            IconButton(onClick = navigateToHome) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = "",
                    modifier = modifier.size(30.dp),
                    tint = MaterialTheme.colorScheme.outline
                )
            }
            IconButton(
                onClick = navigateBack
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Regresar",
                    modifier = modifier.size(30.dp),
                    tint = MaterialTheme.colorScheme.outline
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = MaterialTheme.colorScheme.secondary,
        )
    )
}


@Composable
fun DialogOpcionesCompra(
    onDismiss: () -> Unit,
    modifier: Modifier = Modifier,
    sucursales: List<Sucursal>,
    precio: String,
    viewModel: DetallesParteViewModel,
    aCarrito: () -> Unit,
    aBackOrder: () -> Unit,
    abrirSheetCantidad: () -> Unit
) {
    AlertDialog(
        modifier = modifier.padding(horizontal = 10.dp),
        confirmButton = { },
        onDismissRequest = { onDismiss() },
        properties = DialogProperties(usePlatformDefaultWidth = false),
        text = {
            OptionCompraItem(
                sucursalesList = sucursales,
                precio = precio,
                viewModel = viewModel,
                onDismiss = onDismiss,
                aCarrito = aCarrito,
                aBackOrder = aBackOrder,
                abrirSheetCantidad = abrirSheetCantidad
            )
        },
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = { onDismiss() }) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = modifier.size(30.dp)
                    )
                }
                Text(
                    text = "Opciones de Compra",
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.surfaceVariant,
    )
}

@Composable
fun OptionCompraItem(
    sucursalesList: List<Sucursal>,
    modifier: Modifier = Modifier,
    precio: String,
    viewModel: DetallesParteViewModel,
    onDismiss: () -> Unit,
    abrirSheetCantidad: () -> Unit,
    aCarrito: () -> Unit,
    aBackOrder: () -> Unit
) {
    val sucursalesFiltro = sucursalesList.filter { it.nombre == "MATRIZ" || it.nombre == "CDMX" }
    LazyColumn(modifier.padding()) {
        items(sucursalesFiltro) { sucursal ->
            OptionItem(
                sucursal,
                precio = precio,
                onDismiss = onDismiss,
                onClick = {
                    abrirSheetCantidad()
                    aCarrito()
                    viewModel.onSucursalSelected(sucursal.nombre!!, sucursal.idSuc!!)
                    onDismiss()
                },
                onBackOrder = {
                    abrirSheetCantidad()
                    aBackOrder()
                    viewModel.onSucursalSelected(sucursal.nombre!!, sucursal.idSuc!!)
                    onDismiss()
                },
                permisoCotizacion = viewModel.permisoCotizacion,
                permisoExistencias = viewModel.permisoCotizacion,
                permisoPrecio = viewModel.permisoPrecio,
            )
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MensajeAlert(onDismiss: () -> Unit, showAlert: Boolean, navigateToCart: () -> Unit) {

    if (showAlert) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            confirmButton = {
                Button(
                    onClick = {
                        onDismiss()
                        navigateToCart()
                        //onDismissPadre()
                    },
                    colors = ButtonDefaults.buttonColors(Color.White)
                ) {
                    Text(
                        text = "Cerrar",
                        color = Color.Black
                    )
                }
            },
            title = { Text(text = "Aviso", color = Negro) },
            text = { Text(text = "El producto se agregó a tu carrito", color = Negro) },
            icon = {
                Icon(
                    imageVector = Icons.Filled.CheckCircle,
                    contentDescription = "",
                    modifier = Modifier.size(70.dp)
                )
            },
            iconContentColor = Verde_Success,
            containerColor = Blanco
        )
    }

}

@Composable
fun ErrorAlert(onDismiss: () -> Unit, showAlert: Boolean) {

    if (showAlert) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            confirmButton = {
                Button(
                    onClick = {
                        onDismiss()
                        //onDismissPadre()
                    },
                    colors = ButtonDefaults.buttonColors(Color.White)
                ) {
                    Text(
                        text = "Cerrar",
                        color = Color.Black
                    )
                }
            },
            title = { Text(text = "Aviso") },
            text = { Text(text = "El producto no se pudo agregar al carrito") },
            icon = {
                Icon(
                    imageVector = Icons.Filled.Error,
                    contentDescription = "",
                    modifier = Modifier.size(70.dp)
                )
            },
            iconContentColor = Rojo_Vazlo,
            containerColor = Blanco
        )
    }

}

@Composable
fun OptionItem(
    sucursal: Sucursal,
    modifier: Modifier = Modifier,
    precio: String,
    onClick: () -> Unit,
    onBackOrder: () -> Unit,
    onDismiss: () -> Unit,
    permisoCotizacion: String,
    permisoExistencias: String,
    permisoPrecio: String,
) {
    var color = Negro
    var disponibilidad = "Sin Permisos"
    var precioText = "Precio: Sin Permiso"
    if (permisoExistencias == "1") {
        color =
            if (sucursal.existencia?.toInt()!! > 0) Verde_Success else MaterialTheme.colorScheme.surface
        disponibilidad = if (sucursal.existencia!!.toInt() > 0) "Disponible" else "No disponible"
    }
    if (permisoPrecio == "1") {
        precioText = "Precio: $ $precio"
    }
    ListItem(
        overlineContent = {
            Text(
                text = sucursal.nombre!!,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        },
        headlineContent = {
            Text(
                text = disponibilidad,
                color = color,
                fontSize = 17.sp
            )
        },
        supportingContent = { Text(text = precioText) },
        trailingContent = {
            if (permisoCotizacion == "1") {
                vazlo.refaccionarias.ui.screens.detallesParte.ActionCartButton(
                    disponible = sucursal.existencia!!,
                    onCarrito = onClick,
                    onDismiss = onDismiss,
                    onBackorder = onBackOrder
                )
            } else {
                Icon(
                    imageVector = Icons.Filled.DoNotDisturbAlt,
                    contentDescription = "",
                    modifier.size(30.dp)
                )
            }
        },
        colors = ListItemDefaults.colors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    )
    HorizontalDivider(color = MaterialTheme.colorScheme.onSurfaceVariant)
}

@Composable
fun ActionCartButton(
    modifier: Modifier = Modifier,
    disponible: String,
    onCarrito: () -> Unit,
    onDismiss: () -> Unit,
    onBackorder: () -> Unit
) {


    if (disponible.toInt() > 0) {
        IconButton(onClick = {
            onCarrito()
        }) {
            Icon(
                painter = painterResource(id = R.drawable.add_cart_icon),
                contentDescription = "",
                modifier = modifier.size(30.dp)
            )
        }
    } else {
        Button(
            onClick = { onBackorder() },
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            modifier = modifier
        ) {
            Text(text = "BackOrder", color = MaterialTheme.colorScheme.onSurface, fontSize = 17.sp)
        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BottomCantidad(
    modifier: Modifier = Modifier,
    showBottomSheet: Boolean,
    cerrarBottomSheet: () -> Unit,
    agregar: Boolean,
    viewModel: DetallesParteViewModel,
    cerrarOptions: () -> Unit,
    showSucces: () -> Unit,
    showError: () -> Unit,
    backOption: () -> Unit,
) {
    val focusManager = LocalFocusManager.current
    val scope = rememberCoroutineScope()
    val sheetState = rememberModalBottomSheetState()
    var showInputCantidad by remember {
        mutableStateOf(false)
    }
    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                scope.launch {
                    sheetState.hide()
                    cerrarBottomSheet()
                    backOption()
                }
            },
            sheetState = sheetState,
            containerColor = Blanco,
            contentColor = Negro,
            windowInsets = WindowInsets(0, 0, 0, 0)
        ) {
            // Sheet content
            if (!showInputCantidad) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center,
                    modifier = modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "1 unidad",
                        color = Negro,
                        modifier = modifier
                            .clickable {
                                scope.launch {
                                    if (agregar) {
                                        if (viewModel.agregarProducto(1)) {
                                            sheetState.hide()
                                            cerrarBottomSheet()
                                            cerrarOptions()
                                            showSucces()
                                        } else {
                                            sheetState.hide()
                                            cerrarBottomSheet()
                                            cerrarOptions()
                                            showError()
                                        }
                                    } else {
                                        if (viewModel.agregarABackOrder(1)
                                        ) {
                                            sheetState.hide()
                                            cerrarBottomSheet()
                                            cerrarOptions()
                                            showSucces()
                                        } else {
                                            sheetState.hide()
                                            cerrarBottomSheet()
                                            cerrarOptions()
                                            showError()
                                        }
                                    }
                                }
                            }
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    HorizontalDivider(
                        modifier
                            .fillMaxWidth()
                            .padding(horizontal = 30.dp, vertical = 10.dp),
                        thickness = 1.dp,
                        color = Negro
                    )
                    Text(
                        text = "2 unidades",
                        color = Negro,
                        modifier = modifier
                            .clickable {
                                scope.launch {
                                    if (agregar) {
                                        if (viewModel.agregarProducto(2)) {
                                            sheetState.hide()
                                            cerrarBottomSheet()
                                            cerrarOptions()
                                            showSucces()
                                        } else {
                                            sheetState.hide()
                                            cerrarBottomSheet()
                                            cerrarOptions()
                                            showError()
                                        }
                                    } else {
                                        if (viewModel.agregarABackOrder(2)
                                        ) {
                                            sheetState.hide()
                                            cerrarBottomSheet()
                                            cerrarOptions()
                                            showSucces()
                                        } else {
                                            sheetState.hide()
                                            cerrarBottomSheet()
                                            cerrarOptions()
                                            showError()
                                        }
                                    }
                                }
                            }
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    HorizontalDivider(
                        modifier
                            .fillMaxWidth()
                            .padding(horizontal = 30.dp, vertical = 10.dp),
                        thickness = 1.dp,
                        color = Negro
                    )
                    Text(
                        text = "3 unidades",
                        color = Negro,
                        modifier = modifier
                            .clickable {
                                scope.launch {
                                    if (agregar) {
                                        if (viewModel.agregarProducto(3)) {
                                            sheetState.hide()
                                            cerrarBottomSheet()
                                            cerrarOptions()
                                            showSucces()
                                        } else {
                                            sheetState.hide()
                                            cerrarBottomSheet()
                                            cerrarOptions()
                                            showError()
                                        }
                                    } else {
                                        if (viewModel.agregarABackOrder(3)
                                        ) {
                                            sheetState.hide()
                                            cerrarBottomSheet()
                                            cerrarOptions()
                                            showSucces()
                                        } else {
                                            sheetState.hide()
                                            cerrarBottomSheet()
                                            cerrarOptions()
                                            showError()
                                        }
                                    }
                                }
                            }
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    HorizontalDivider(
                        modifier
                            .fillMaxWidth()
                            .padding(horizontal = 30.dp, vertical = 10.dp),
                        thickness = 1.dp,
                        color = Negro
                    )
                    Text(
                        text = "4 unidades",
                        color = Negro,
                        modifier = modifier
                            .clickable {
                                scope.launch {
                                    if (agregar) {
                                        if (viewModel.agregarProducto(4)) {
                                            sheetState.hide()
                                            cerrarBottomSheet()
                                            cerrarOptions()
                                            showSucces()
                                        } else {
                                            sheetState.hide()
                                            cerrarBottomSheet()
                                            cerrarOptions()
                                            showError()
                                        }
                                    } else {
                                        if (viewModel.agregarABackOrder(4)
                                        ) {
                                            sheetState.hide()
                                            cerrarBottomSheet()
                                            cerrarOptions()
                                            showSucces()
                                        } else {
                                            sheetState.hide()
                                            cerrarBottomSheet()
                                            cerrarOptions()
                                            showError()
                                        }
                                    }
                                }
                            }
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    HorizontalDivider(
                        modifier
                            .fillMaxWidth()
                            .padding(horizontal = 30.dp, vertical = 10.dp),
                        thickness = 1.dp,
                        color = Negro
                    )
                    Text(
                        text = "5 unidades",
                        color = Negro,
                        modifier = modifier.clickable {
                            scope.launch {
                                if (agregar) {
                                    if (viewModel.agregarProducto(5)) {
                                        sheetState.hide()
                                        cerrarBottomSheet()
                                        cerrarOptions()
                                        showSucces()
                                    } else {
                                        sheetState.hide()
                                        cerrarBottomSheet()
                                        cerrarOptions()
                                        showError()
                                    }
                                } else {
                                    if (viewModel.agregarABackOrder(5)) {
                                        sheetState.hide()
                                        cerrarBottomSheet()
                                        cerrarOptions()
                                        showSucces()
                                    } else {
                                        sheetState.hide()
                                        cerrarBottomSheet()
                                        cerrarOptions()
                                        showError()
                                    }
                                }
                            }
                        })
                    HorizontalDivider(
                        modifier
                            .fillMaxWidth()
                            .padding(horizontal = 30.dp, vertical = 10.dp),
                        thickness = 1.dp,
                        color = Negro
                    )
                    Text(
                        text = "6 unidades",
                        color = Negro,
                        modifier = modifier
                            .clickable {
                                scope.launch {
                                    if (agregar) {
                                        if (viewModel.agregarProducto(6)) {
                                            sheetState.hide()
                                            cerrarBottomSheet()
                                            cerrarOptions()
                                            showSucces()
                                        } else {
                                            sheetState.hide()
                                            cerrarBottomSheet()
                                            cerrarOptions()
                                            showError()
                                        }
                                    } else {
                                        if (viewModel.agregarABackOrder(6)
                                        ) {
                                            sheetState.hide()
                                            cerrarBottomSheet()
                                            cerrarOptions()
                                            showSucces()
                                        } else {
                                            sheetState.hide()
                                            cerrarBottomSheet()
                                            cerrarOptions()
                                            showError()
                                        }
                                    }
                                }
                            }
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    HorizontalDivider(
                        modifier
                            .fillMaxWidth()
                            .padding(horizontal = 30.dp, vertical = 10.dp),
                        thickness = 1.dp,
                        color = Negro
                    )
                    Text(
                        text = "Más de 6 unidades",
                        color = Negro,
                        modifier = modifier
                            .clickable { showInputCantidad = true }
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                    Spacer(Modifier.navigationBarsPadding())
                }
            } else {
                Column(
                    modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    Text(text = "Ingresa una cantidad")
                    OutlinedTextField(
                        value = viewModel.nuevaCant,
                        onValueChange = { input ->
                            viewModel.onNuevaCantidadChange(input)
                        },
                        label = { Text(text = "Cantidad") },
                        colors = TextFieldDefaults.colors(
                            focusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unfocusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            focusedContainerColor = MaterialTheme.colorScheme.background,
                            unfocusedContainerColor = MaterialTheme.colorScheme.background,
                            disabledContainerColor = MaterialTheme.colorScheme.background,
                            focusedIndicatorColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            errorLabelColor = MaterialTheme.colorScheme.error,
                            errorSupportingTextColor = MaterialTheme.colorScheme.error,
                        ),
                        modifier = modifier
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        keyboardOptions = KeyboardOptions(
                            imeAction = ImeAction.Done,
                            keyboardType = KeyboardType.Number
                        ),
                        keyboardActions = KeyboardActions(onDone = {
                            focusManager.clearFocus(
                                force = true
                            )
                        }),
                        isError = false,
                        supportingText = { }
                    )
                    Button(
                        onClick = {
                            scope.launch {
                                if (agregar) {
                                    if (viewModel.agregarProducto()) {
                                        sheetState.hide()
                                        cerrarBottomSheet()
                                        cerrarOptions()
                                        showSucces()
                                    } else {
                                        sheetState.hide()
                                        cerrarBottomSheet()
                                        cerrarOptions()
                                        showError()
                                    }
                                } else {
                                    if (viewModel.agregarABackOrder()) {
                                        sheetState.hide()
                                        cerrarBottomSheet()
                                        cerrarOptions()
                                        showSucces()
                                    } else {
                                        sheetState.hide()
                                        cerrarBottomSheet()
                                        cerrarOptions()
                                        showError()
                                    }
                                }
                            }
                        },
                        modifier = modifier.fillMaxWidth(),
                        colors = ButtonColors(
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onSurface,
                            disabledContentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                            disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant
                        ),
                        shape = RoundedCornerShape(10.dp),
                        enabled = viewModel.nuevaCant.isNotBlank()
                    ) {
                        Text(text = "Confirmar")
                    }
                    Spacer(Modifier.padding(200.dp))
                }
            }
            Spacer(modifier = modifier.height(50.dp))
        }
    }

}



// Recibe el objeto de la parte
@Composable
private fun DetalleParte(
    modifier: Modifier = Modifier,
    producto: Producto,
    detallesParteViewModel: DetallesParteViewModel,
    onClick: () -> Unit,
    url360: String
) {
    Surface(
        color = MaterialTheme.colorScheme.surfaceVariant,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = modifier
                .height(250.dp)
        ) {
            ImageParte(
                modifier = modifier,
                producto = producto
            )
            Spacer(modifier = modifier.width(20.dp))
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = modifier.padding(horizontal = 10.dp)
            ) {
                Text(
                    text = producto.nombreArticulo!!,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary,
                    fontSize = 17.sp
                )
                Text(
                    text = stringResource(
                        R.string.desc_prod_resultados_nuevos,
                        producto.lineaPos!!,
                    ),
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp
                )
            }
        }
    }

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier.padding(10.dp)) {
        Row(
            modifier = modifier
                .padding(vertical = 30.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            val show360 = remember { mutableStateOf(false) }
            if (detallesParteViewModel.hay360) {
                Button(
                    onClick = {
                        show360.value = true
                    },
                    modifier = modifier
                        .width(130.dp),
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.vista_360),
                        contentDescription = "360",
                        modifier = modifier.size(30.dp),
                        tint = MaterialTheme.colorScheme.outline
                    )
                }
            }


            if (show360.value) {
                Show360Dialog(
                    close = { show360.value = false },
                    url360 = url360
                )
            }
            Spacer(modifier = modifier.width(20.dp))
        }
        Button(onClick = { onClick() }, modifier.height(40.dp)) {
            Text(
                text = stringResource(R.string.ver_opciones),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }

    HorizontalDivider(
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp),
        thickness = 1.dp,
        color = MaterialTheme.colorScheme.secondary
    )
}


@Composable
private fun ImageParte(
    modifier: Modifier = Modifier,
    producto: Producto
) {
    val show = remember { mutableStateOf(false) }

    AsyncImage(
        model = ImageRequest.Builder(context = LocalContext.current)
            .diskCachePolicy(CachePolicy.DISABLED)
            .data(producto.foto)
            .crossfade(true).build(),
        error = painterResource(R.drawable.imagen),
        placeholder = painterResource(R.drawable.imagen),
        contentDescription = producto.descripcion,
        contentScale = ContentScale.Crop,
        modifier = modifier
            .size(200.dp)
            .clickable { show.value = true },
    )

    if (show.value) {
        ImageDialog(
            close = { show.value = false },
            producto = producto
        )
    }
}

@Composable
private fun ImageDialog(
    modifier: Modifier = Modifier,
    producto: Producto,
    close: () -> Unit
) {
    var scale by remember { mutableFloatStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }
    val minScale = 0.75f
    val maxScale = 3f

    val state = rememberTransformableState { zoomChange, offsetChange, _ ->
        val newScale = scale * zoomChange
        if (newScale in minScale..maxScale) {
            scale = newScale
        }
        if (scale > 1f) {
            offset += offsetChange
        }
    }

    Dialog(
        onDismissRequest = close,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Surface(
            modifier = modifier
                .fillMaxSize(),
            color = MaterialTheme.colorScheme.onTertiary
        ) {
            Column(
                modifier = modifier
                    .fillMaxSize()
            ) {
                Row(horizontalArrangement = Arrangement.End, modifier = modifier.fillMaxWidth()) {
                    IconButton(onClick = close) {
                        /*Icon(
                            painter = painterResource(id = R.drawable.close_icon),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary
                        )*/
                    }
                }

                Spacer(modifier = modifier.height(50.dp))

                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .height(400.dp)
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(context = LocalContext.current)
                            .diskCachePolicy(CachePolicy.DISABLED)
                            .data(producto.foto)
                            .crossfade(true).build(),
                        error = painterResource(R.drawable.imagen),
                        placeholder = painterResource(R.drawable.imagen),
                        contentDescription = producto.descripcion,
                        contentScale = ContentScale.Crop,
                        modifier =
                        modifier
                            .fillMaxWidth()
                            .transformable(state = state)
                            .graphicsLayer(
                                scaleX = scale,
                                scaleY = scale,
                                translationX = offset.x,
                                translationY = offset.y
                            )
                    )
                }
            }
        }
    }
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
private fun Show360Dialog(
    modifier: Modifier = Modifier,
    close: () -> Unit,
    url360: String
) {
    val context = LocalContext.current
    Toast.makeText(context, "Cargando vista...", Toast.LENGTH_SHORT).show()
    Dialog(
        onDismissRequest = close,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Surface(
            modifier = modifier
                .fillMaxSize(),
            color = MaterialTheme.colorScheme.onTertiary
        ) {
            Column(
                modifier = modifier
                    .fillMaxSize()
            ) {
                Row(horizontalArrangement = Arrangement.End, modifier = modifier.fillMaxWidth()) {
                    IconButton(onClick = close) {
                        /*Icon(
                            painter = painterResource(id = R.drawable.close_icon),
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onPrimary
                        )*/
                    }
                }

                Spacer(modifier = modifier.height(50.dp))

                Row(
                    modifier = modifier
                        .fillMaxWidth()
                        .height(400.dp)
                ) {
                    var isLoading by remember { mutableStateOf(true) }

                    Box(modifier = modifier.fillMaxSize()) {
                        AndroidView(factory = {
                            WebView(it).apply {
                                layoutParams = ViewGroup.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                    ViewGroup.LayoutParams.MATCH_PARENT,
                                )
                                settings.javaScriptEnabled = true
                                webViewClient = object : WebViewClient() {
                                    override fun onPageFinished(view: WebView, url: String) {
                                        super.onPageFinished(view, url360)
                                        isLoading = false
                                    }

                                }
                                loadUrl(url360)
                            }
                        }, update = {
                            it.loadUrl(url360)
                        })
                    }

                }
            }
        }
    }
}

@Composable
private fun DetallesParteContent(
    modifier: Modifier = Modifier,
    productos: List<ProductosResult>,
    viewModel: DetallesParteViewModel,
    viewModelCompartido: ProductoCompartidoViewModel,
    navigateToSelf: (String) -> Unit,
    showBottomSheet: Boolean,
    agregar: Boolean,
    cerrarBottomSheet: () -> Unit,
    cerrarOptions: () -> Unit
) {
    val showDialog = remember { mutableStateOf(false) }
    val productoCurrent = remember { mutableStateOf<ProductosResult?>(null) }
    LazyColumn(
        modifier = modifier
            .padding(start = 20.dp, top = 20.dp, end = 20.dp),
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        item {
            Text(
                text = stringResource(R.string.guia_de_comprador),
                fontWeight = FontWeight.Bold,
                modifier = modifier.padding(bottom = 20.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        items(productos) { producto ->
            val linea = if (producto.linea?.isNotEmpty() == true) {
                producto.linea
            } else {
                producto.cillitModSopId
            }
            ListItem(
                modifier = modifier,
                headlineContent = {
                    Text(
                        text = "${producto.nombreMarca} ${producto.nombreModeloCarro}  ${producto.aIni} - ${producto.aFin}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                    )
                },
                supportingContent = {
                    Text(
                        text = "${producto.nombreCilidraje} ${producto.nombreLitro} $linea \n${producto.nombrePosicion}",
                        fontSize = 15.sp
                    )
                },
                trailingContent = {
                    IconButton(
                        onClick = {
                            productoCurrent.value = producto
                            showDialog.value = true
                        },
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = Verde_Success
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Filled.Add,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                },
                colors = ListItemColors(
                    containerColor = MaterialTheme.colorScheme.onSurface,
                    disabledHeadlineColor = MaterialTheme.colorScheme.onSurface,
                    disabledLeadingIconColor = MaterialTheme.colorScheme.onSurface,
                    disabledTrailingIconColor = MaterialTheme.colorScheme.onSurface,
                    headlineColor = MaterialTheme.colorScheme.onSurfaceVariant,
                    leadingIconColor = MaterialTheme.colorScheme.onSurface,
                    overlineColor = MaterialTheme.colorScheme.onSurface,
                    supportingTextColor = MaterialTheme.colorScheme.onTertiary,
                    trailingIconColor = MaterialTheme.colorScheme.onSurface
                )
            )
            HorizontalDivider(
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.surfaceVariant
            )
        }
    }
    if (showDialog.value) {
        ResultadosDialog(
            modifier = modifier,
            viewModel = viewModel,
            viewModelCompartido = viewModelCompartido,
            navigateToSelf = navigateToSelf,
            producto = productoCurrent.value
        ) {
            showDialog.value = false
        }
    }
}

@Composable
private fun ResultadosDialog(
    modifier: Modifier = Modifier,
    producto: ProductosResult?,
    viewModel: DetallesParteViewModel,
    viewModelCompartido: ProductoCompartidoViewModel,
    navigateToSelf: (String) -> Unit,
    close: () -> Unit
) {
    LaunchedEffect(producto) {
        if (producto != null) {
            viewModel.cargarProductosExtra(
                producto.nombreMarca!!,
                producto.nombreModeloCarro!!,
                producto.nombreCilidraje!!,
                producto.nombreLitro!!,
                producto.aIni!!,
                producto.aFin!!
            )
        }
    }
    Dialog(
        onDismissRequest = close,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Surface(
            modifier = modifier
                .fillMaxSize(),
            color = MaterialTheme.colorScheme.onPrimary
        ) {
            Column(
                modifier = modifier
                    .fillMaxSize()
            ) {
                Row(
                    modifier
                        .fillMaxWidth()
                        .background(color = MaterialTheme.colorScheme.secondary),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = modifier
                            .padding(start = 8.dp),
                        text = "${producto!!.nombreMarca} ${producto.nombreModeloCarro}",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.outline
                    )
                    IconButton(onClick = close) {
                        Icon(imageVector = Icons.Filled.Close, contentDescription = "", tint = MaterialTheme.colorScheme.outline)
                    }
                }
                Column {
                    when (viewModel.productosExtraUiState) {
                        is ProductosUiState.Loading -> {
                            AltTable(mensaje = "Cargando")
                        }

                        is ProductosUiState.Success -> {
                            val productosExtra =
                                (viewModel.productosExtraUiState as ProductosUiState.Success).productos
                            DialogContent(
                                productos = productosExtra,
                                viewModelCompartido = viewModelCompartido,
                                navigateToSelf = navigateToSelf
                            )
                        }

                        is ProductosUiState.Error -> {
                            AltTable(mensaje = "Error al cargar los datos")
                        }

                        else -> {}
                    }
                }
            }
        }
    }
}

@Composable
private fun DialogContent(
    modifier: Modifier = Modifier,
    productos: List<ProductosResult>,
    viewModelCompartido: ProductoCompartidoViewModel,
    navigateToSelf: (String) -> Unit
) {
    val productosList = productos.groupBy {
        Triple(
            first = it.nombreMarca,
            second = it.nombreModeloCarro,
            third = if (it.linea?.isNotEmpty() == true) {
                it.linea
            } else {
                it.cillitModSopId
            }
        )
    }
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onPrimary),
        verticalArrangement = Arrangement.spacedBy(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        productosList.forEach { (keys, productos) ->
            item {
                HeaderTipo(titulo = keys.third!!)
            }
            items(productos) { producto ->
                Productos(
                    producto = producto,
                    viewModelCompartido = viewModelCompartido,
                    navigateToSelf = navigateToSelf
                )
            }
        }
    }
}

@Composable
private fun HeaderTipo(
    modifier: Modifier = Modifier,
    titulo: String
) {
    Surface(color = MaterialTheme.colorScheme.primary) {
        Row(
            horizontalArrangement = Arrangement.Center,
            modifier = modifier
                .fillMaxWidth()
                .height(50.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = titulo,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onPrimary,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Composable
private fun Productos(
    modifier: Modifier = Modifier,
    producto: ProductosResult,
    viewModelCompartido: ProductoCompartidoViewModel,
    navigateToSelf: (String) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .background(color = MaterialTheme.colorScheme.surfaceVariant)
            .padding(10.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .clickable {
                    viewModelCompartido.setProducto(producto)
                    navigateToSelf.invoke(producto.nombreSoporte!!)
                }
        ) {

            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current)
                    .diskCachePolicy(CachePolicy.DISABLED)
                    .data(producto.urlSoporte)
                    .crossfade(true).build(),
                error = painterResource(R.drawable.imagen),
                placeholder = painterResource(R.drawable.imagen),
                contentDescription = producto.descripcion,
                contentScale = ContentScale.Crop,
                modifier = Modifier.size(150.dp)
            )
            Spacer(modifier = modifier.width(20.dp))
            Column(
                verticalArrangement = Arrangement.Center,
                modifier = modifier.padding(horizontal = 10.dp)
            ) {
                Text(
                    text = producto.nombreSoporte!!,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary,
                    fontSize = 17.sp
                )
                Text(
                    text = stringResource(
                        R.string.desc_prod_resultados,
                        producto.nombrePosicion!!,
                        producto.nombreCilidraje!!,
                        producto.nombreLitro!!,
                        producto.aIni!!,
                        producto.aFin!!,
                        producto.descripcion!!
                    ),
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = Negro
                )
            }
        }
    }
}

@Composable
private fun AltTable(
    modifier: Modifier = Modifier,
    mensaje: String
) {
    Column(
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        if (mensaje == "Cargando") {
            CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally))
        } else {
            Text(text = mensaje, fontSize = 25.sp, textAlign = TextAlign.Center)
        }
    }
}