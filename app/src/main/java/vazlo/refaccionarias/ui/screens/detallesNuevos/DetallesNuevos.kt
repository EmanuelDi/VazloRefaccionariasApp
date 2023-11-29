package vazlo.refaccionarias.ui.screens.detallesNuevos

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Build
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
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
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
import vazlo.refaccionarias.data.model.busquedasData.ProductosResult
import vazlo.refaccionarias.data.model.detallesData.Sucursal
import vazlo.refaccionarias.data.model.homeData.Producto
import vazlo.refaccionarias.ui.AppViewModelProvider
import vazlo.refaccionarias.ui.navigation.NavigationDestination
import vazlo.refaccionarias.ui.screens.detallesParte.*
import vazlo.refaccionarias.ui.screens.resultadoPorPartes.ProductoCompartidoViewModel
import vazlo.refaccionarias.ui.theme.Negro
import vazlo.refaccionarias.ui.theme.VazloRefaccionariasTheme
import vazlo.refaccionarias.ui.theme.Verde_Success


object DetallesNuevoDestination : NavigationDestination {
    override val route = "detalles-nuevo"
    const val criterioArg = "criterio"
    val routeWithArgs = "$route/{$criterioArg}"
}



@OptIn(ExperimentalMaterial3Api::class)
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

    var showCanitadError by remember {
        mutableStateOf(false)
    }

    var showBackOrderDialog by remember {
        mutableStateOf(false)
    }

    var showConversiones by remember { mutableStateOf( false) }

    val view = LocalView.current

    val window = (view.context as Activity).window
    WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = showBottomSheet

    var showAlert by remember { mutableStateOf(false) }
    var showAlertError by remember { mutableStateOf(false) }

    val prod = viewModelCompartido.getProducto()
    val producto = rememberSaveable(saver = Producto.Saver) {
        mutableStateOf(prod).value
    }

    val suc = viewModelCompartido.getSuc()
    val sucursales = rememberSaveable(saver = Sucursal.Saver) {
        mutableStateOf(suc).value
    }


    var url360 by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()

    var cantidadAlt by remember {
        mutableStateOf(0)
    }

    val setCantidadAlt: (Int) -> Unit = { cantidad -> cantidadAlt = cantidad }

    val sheetState = rememberModalBottomSheetState()

    dPViewModel.verificar360(producto.nombreArticulo!!)

    LaunchedEffect(viewModelCompartido.getProducto().nombreArticulo) {
        dPViewModel.get360()
        url360 = dPViewModel.url360 + "?soporte=" + viewModelCompartido.getProducto().nombreArticulo
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
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                        )

                    }
                }
                DetalleParte(
                    producto = producto,
                    detallesParteViewModel = dPViewModel,
                    onClick = { openedDialog = !openedDialog },
                    url360 = url360,
                    onClickConver = {showConversiones = true}
                )

                if (openedDialog)
                    DialogOpcionesCompra(
                        onDismiss = { openedDialog = !openedDialog },
                        sucursales = sucursales,
                        precio = producto.precio,
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
                    showCantidadError = {showCanitadError = true},
                    showBackOrderDialog = { showBackOrderDialog = true },
                    setCantidadAlt = setCantidadAlt,
                    sheetState = sheetState
                )

                DialogCantidadNoDisp(
                    onDismiss = { showCanitadError = false },
                    showAlert = showCanitadError,
                    navigateToCart = navigateToCart,
                    cantidad = cantidadAlt,
                    viewModel = dPViewModel,
                    scope = scope,
                    showSucces = { showAlert = true },
                    showError = { showAlertError = true }
                )

                MensajeAlert(
                    onDismiss = {
                        scope.launch { sheetState.hide() }
                        showAlert = false
                    },
                    showAlert,
                    navigateToCart = navigateToCart
                )

                BackOrderDialog(
                    onDismiss = {
                        showBackOrderDialog = false
                    },
                    showBackOrderDialog
                )

                ErrorAlert(
                    onDismiss = {
                        showAlertError = false
                    },
                    showAlert = showAlertError,
                )

                if (showConversiones) {
                    DialogConversiones(
                        onDismiss = {showConversiones = false},
                        conversiones = dPViewModel.listConversiones
                    )
                }

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
                            viewModelCompartido = productoCompartidoViewModel
                        )
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



// Recibe el objeto de la parte
@Composable
private fun DetalleParte(
    modifier: Modifier = Modifier,
    producto: Producto,
    detallesParteViewModel: DetallesParteViewModel,
    onClick: () -> Unit,
    url360: String,
    onClickConver: () -> Unit
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

    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier.padding(bottom = 10.dp).fillMaxWidth()) {
        Row(
            modifier = modifier
                .padding(vertical = 30.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(20.dp)
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


            Button(onClick = { onClickConver() }, modifier) {
                Text(
                    text = "Ver conversiones",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
            }
        }
        Row {
            Button(onClick = { onClick() }, modifier.height(40.dp)) {
                Text(
                    text = stringResource(R.string.ver_opciones),
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface,
                    fontWeight = FontWeight.Bold
                )
            }
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
    navigateToSelf: (String) -> Unit
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
                producto.nombreMarca,
                producto.nombreModeloCarro,
                producto.nombreCilidraje!!,
                producto.nombreLitro,
                producto.aIni,
                producto.aFin
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
                    navigateToSelf.invoke(producto.nombreSoporte)
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
                    text = producto.nombreSoporte,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary,
                    fontSize = 17.sp
                )
                Text(
                    text = stringResource(
                        R.string.desc_prod_resultados,
                        producto.nombrePosicion,
                        producto.nombreCilidraje!!,
                        producto.nombreLitro,
                        producto.aIni,
                        producto.aFin,
                        producto.descripcion
                    ),
                    fontWeight = FontWeight.Bold,
                    fontSize = 17.sp,
                    color = Negro
                )
            }
        }
    }
}
