package vazlo.refaccionarias.ui.screens.home

import android.content.Context
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.AlertDialogDefaults
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.skydoves.balloon.ArrowPositionRules
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.BalloonHighlightAnimation
import com.skydoves.balloon.BalloonSizeSpec
import com.skydoves.balloon.compose.rememberBalloonBuilder
import com.skydoves.balloon.overlay.BalloonOverlayAnimation
import com.skydoves.balloon.overlay.BalloonOverlayRoundRect
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import vazlo.refaccionarias.R
import vazlo.refaccionarias.data.model.homeData.Producto
import vazlo.refaccionarias.data.model.homeData.Promocion
import vazlo.refaccionarias.ui.navigation.NavigationDestination
import vazlo.refaccionarias.ui.AppViewModelProvider
import vazlo.refaccionarias.ui.screens.detallesNuevos.ProductoNuevoCompartiido
import vazlo.refaccionarias.ui.screens.login.LoginDestination
import vazlo.refaccionarias.ui.theme.Gris_Vazlo


object HomeDestination : NavigationDestination {
    override val route = "home"
    /* override val titleRes = R.string.app_name*/
}


@OptIn(ExperimentalFoundationApi::class, ExperimentalMaterial3Api::class)
@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    drawerState: DrawerState,
    scope: CoroutineScope,
    homeViewModel: HomeViewModel,
    navigateToDetallesNuevo: (String) -> Unit,
    productoNuevoCompartiido: ProductoNuevoCompartiido
) {
    val builder = rememberBalloonBuilder {
        setArrowSize(10)
        setArrowPosition(0.5f)
        setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
        setWidth(BalloonSizeSpec.WRAP)
        setHeight(BalloonSizeSpec.WRAP)
        setPadding(12)
        setMarginHorizontal(12)
        setCornerRadius(8f)
        setBackgroundColorResource(R.color.teal_200)
        setBalloonAnimation(BalloonAnimation.ELASTIC)
        setBalloonHighlightAnimation(BalloonHighlightAnimation.SHAKE)
        setIsVisibleOverlay(true)
        setOverlayColorResource(R.color.overlay)
        setOverlayPadding(6f)
        setOverlayPaddingColorResource(R.color.overlayPadding)
        setBalloonOverlayAnimation(BalloonOverlayAnimation.FADE)
        setDismissWhenOverlayClicked(false)
        setOverlayShape(BalloonOverlayRoundRect(12f, 12f))
        setLifecycleOwner(lifecycleOwner)
    }

    Scaffold(
        topBar = { HomeTopAppBar(drawerState = drawerState, scope = scope, homeViewModel = homeViewModel) }
    ) {
        Surface(modifier = modifier.padding(it), color = MaterialTheme.colorScheme.background) {
            Column(modifier = modifier) {
                when (homeViewModel.bannerState) {
                    is BannersState.Loading -> LoadingScreen(modifier = modifier)
                    is BannersState.Success -> {
                        val pagerState = rememberPagerState(
                            initialPage = 0,
                            initialPageOffsetFraction = 0f,
                            pageCount = { (homeViewModel.bannerState as BannersState.Success).promos.size }
                        )
                        CarrouselHome(
                            pagerState,
                            banners = (homeViewModel.bannerState as BannersState.Success).promos,
                        )
                    }

                    is BannersState.Error -> {}
                    else -> {}
                }
                when (homeViewModel.homeUiState) {
                    is HomeUiState.Loading -> LoadingScreen(modifier = modifier.fillMaxSize())
                    is HomeUiState.Success -> ProdList(
                        listProducts = (homeViewModel.homeUiState as HomeUiState.Success).productos,
                        navigateToDetallesNuevo = navigateToDetallesNuevo,
                        productoNuevoCompartiido = productoNuevoCompartiido,
                        builder = builder
                    )
                    is HomeUiState.Error -> ErrorScreen(modifier = modifier.fillMaxSize())
                    else -> {}
                }
            }
        }
    }
}

@Composable
fun ErrorScreen(modifier: Modifier = Modifier) {
    Text(text = "peto", modifier)
}

@Composable
fun LoadingScreen(modifier: Modifier = Modifier) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = modifier.fillMaxSize()
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun LogoutDialog(
    modifier: Modifier = Modifier,
    onLogoutConfirm: () -> Unit,
    onLogoutCancel: () -> Unit
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = { },
        title = { Text(
            text = "Cerrar Sesión",
            color = MaterialTheme.colorScheme.onSurfaceVariant
        ) },
        text = {
            Text(
                text = "¿Deseas cerrar tu sesión actual?",
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        },
        dismissButton = {
            TextButton(onClick = onLogoutCancel) {
                Text(text = "Rechazar", color = MaterialTheme.colorScheme.onSurfaceVariant)

            }
        },
        confirmButton = {
            TextButton(onClick = {
                onLogoutConfirm.invoke()
            }) {
                Text(text = "Aceptar", color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        },
        containerColor = MaterialTheme.colorScheme.background
    )
}

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeContent(
    modifier: Modifier = Modifier,
    navController: NavController,
    homeViewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory),
    navigateToDetallesNuevo: (String) -> Unit,
    productoNuevoCompartiido: ProductoNuevoCompartiido
) {
    val context = LocalContext.current
    var showUpdateDialog = remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()
    var appVersion: Long = 0
    try {
        val pInfo = context.packageManager.getPackageInfo("vazlo.mecanicos", 0)
        val version: Long = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            pInfo.longVersionCode
        } else {
            pInfo.versionCode.toLong()
        }
        appVersion = version
    } catch (e: Exception) {
        e.printStackTrace()
    }

    LaunchedEffect(Unit) {
        if(appVersion.toInt() < homeViewModel.versionApk) {
            showUpdateDialog.value = true
        }
    }
    //Inicializa las preferencias locales
    val prefs = context.getSharedPreferences("preferencias", Context.MODE_PRIVATE)
    //Inicializa la preferencia en especifico
    val mostrar = prefs.getBoolean("mostrar", true)
    val editor = prefs.edit()

    if(showUpdateDialog.value && mostrar) {
        ActualizacionDialog(
            onLater = {
                showUpdateDialog.value = false
            },
            onNever = {
                editor.putBoolean("mostrar", false)
                editor.apply()
                showUpdateDialog.value = false
            }
        )
    }

    var logoutConfrimation by remember{ mutableStateOf(false) }
    BackHandler(enabled = true) {
        logoutConfrimation = true
    }
    if(logoutConfrimation) {
        LogoutDialog(
            onLogoutConfirm = {
                logoutConfrimation = false
                homeViewModel.logout()
                navController.popBackStack(LoginDestination.route, inclusive = false)
            },
            onLogoutCancel = { logoutConfrimation = false }
        )
    }
    val drawerState = rememberDrawerState(DrawerValue.Closed)
// icons to mimic drawer destinations
    /*val items = listOf(Icons.Default.Favorite, Icons.Default.Face, Icons.Default.Email)*/
    /*val selectedItem = remember { mutableStateOf("") }*/
    ModalNavigationDrawer(
        drawerState = drawerState,

        drawerContent = {
            ModalDrawerSheet(modifier = modifier.width(300.dp)) {
                Column(modifier.fillMaxHeight()) {
                    Row(
                        modifier = modifier
                            .fillMaxWidth()
                            .wrapContentSize(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Box(
                                modifier = modifier
                                    .background(color = MaterialTheme.colorScheme.secondary)
                                    .width(100.dp)
                                    .height(120.dp)
                            ) {

                            }
                            Row {
                                Box(
                                    modifier = modifier
                                        .background(color = MaterialTheme.colorScheme.primary)
                                        .width(50.dp)
                                        .height(30.dp)
                                ) {

                                }
                                Box(
                                    modifier = modifier
                                        .background(color = MaterialTheme.colorScheme.inversePrimary)
                                        .width(50.dp)
                                        .height(30.dp)
                                ) {

                                }
                            }

                        }
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = modifier
                                .fillMaxWidth()
                                .background(color = MaterialTheme.colorScheme.surfaceVariant)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.vazlo_logo),
                                contentDescription = "",
                                modifier = modifier.size(150.dp),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }

                    LazyColumn(
                        verticalArrangement = Arrangement.SpaceBetween,
                        modifier = modifier
                            .background(MaterialTheme.colorScheme.background),
                    ) {
                        item {
                            Text(
                                text = stringResource(R.string.usuarioHome, homeViewModel.usuario),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Normal,
                                modifier = modifier.padding(16.dp),
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            HorizontalDivider(color = MaterialTheme.colorScheme.surfaceVariant)
                        }
                        menuSections.forEach { section ->
                            item {
                                Text(
                                    text = stringResource(id = section.title),
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    modifier = modifier.padding(16.dp),
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                            items(section.items) { item ->
                                if (item.label != R.string.usuarios_y_permisos) {
                                    if (item.label == R.string.salir){
                                        ElementoMenu(
                                            item,
                                            scope,
                                            drawerState,
                                            homeViewModel,
                                            navController,
                                            context,
                                            onClick = {logoutConfrimation = true}
                                        )
                                    } else{
                                        ElementoMenu(
                                            item,
                                            scope,
                                            drawerState,
                                            homeViewModel,
                                            navController,
                                            context,
                                            onClick = {}
                                        )
                                    }

                                } else {
                                    if (homeViewModel.esAdmin) {
                                        ElementoMenu(
                                            item,
                                            scope,
                                            drawerState,
                                            homeViewModel,
                                            navController,
                                            context,
                                            onClick = {}
                                        )
                                    }
                                }
                            }
                            item {
                                HorizontalDivider(
                                    modifier = Modifier.padding(vertical = 5.dp),
                                    thickness = 1.dp,
                                    color = MaterialTheme.colorScheme.surfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        },
        content = {
            HomeScreen(
                modifier,
                drawerState = drawerState,
                scope = scope,
                homeViewModel = homeViewModel,
                navigateToDetallesNuevo = navigateToDetallesNuevo,
                productoNuevoCompartiido = productoNuevoCompartiido
            )

        }
    )

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActualizacionDialog(modifier: Modifier = Modifier, onLater: () -> Unit, onNever: () -> Unit) {
    val uriHandler = LocalUriHandler.current
    AlertDialog(
        onDismissRequest = { }
    ) {
        Surface(
            modifier = Modifier
                .wrapContentWidth()
                .wrapContentHeight(),
            shape = MaterialTheme.shapes.large,
            tonalElevation = AlertDialogDefaults.TonalElevation,
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = modifier
                    .padding(16.dp)
                    .width(350.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = "Actualización disponible",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    modifier = modifier
                        .padding(top = 16.dp),
                    text = "¿Desea actualizar la aplicación?",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    onClick = {
                        uriHandler.openUri("https://play.google.com/store/apps/details?id=vazlo.refaccionarias")
                    },
                    contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
                    colors = ButtonDefaults.outlinedButtonColors(
                        MaterialTheme.colorScheme.primary,
                        disabledContainerColor = MaterialTheme.colorScheme.surfaceVariant
                    ),
                    modifier = modifier
                        .wrapContentWidth()
                        .padding(top = 5.dp, bottom = 5.dp),
                ) {
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text(
                        text = "Actualizar",
                        color = MaterialTheme.colorScheme.onPrimary,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
                Spacer(modifier = Modifier.height(5.dp))
                Row(
                    modifier = modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    TextButton(
                        onClick = onLater
                    ) {
                        Text(
                            text = "Más tarde",
                            style = MaterialTheme.typography.labelLarge
                        )
                    }
                    TextButton(
                        onClick = onNever
                    ) {
                        Text(
                            text = "No volver a mostrar",
                            style = MaterialTheme.typography.labelLarge,
                            color = MaterialTheme.colorScheme.secondary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ElementoMenu(
    item: MenuItem,
    scope: CoroutineScope,
    drawerState: DrawerState,
    homeViewModel: HomeViewModel,
    navController: NavController,
    context: Context,
    onClick: () -> Unit
) {
    NavigationDrawerItem(
        icon = {
            Icon(
                painterResource(id = item.icon),
                contentDescription = "",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.size(25.dp)
            )
        },
        label = {
            Text(
                text = stringResource(item.label),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 15.sp,
                fontWeight = FontWeight.Normal
            )
        },
        selected = false,
        onClick = {
            scope.launch { drawerState.close() }
            if (item.label == R.string.salir) {
                onClick()
                //navController.popBackStack(item.destination, false)
            } else if (item.label == R.string.eventos) {
                if (!homeViewModel.hayEventos) {
                    Toast.makeText(context, "No hay eventos", Toast.LENGTH_SHORT).show()
                } else {
                    navController.navigate(item.destination)
                }
            } else {
                navController.navigate(item.destination)
            }

        },
        colors = NavigationDrawerItemDefaults.colors(
            unselectedContainerColor = MaterialTheme.colorScheme.background
        )
    )
}


@Composable
fun ProdList(
    modifier: Modifier = Modifier,
    listProducts: List<Producto>,
    navigateToDetallesNuevo: (String) -> Unit,
    productoNuevoCompartiido: ProductoNuevoCompartiido,
    builder: Balloon.Builder
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(160.dp),
        contentPadding = PaddingValues(horizontal = 10.dp),
        modifier = modifier
    ) {
        items(items = listProducts) { producto ->
            CardProductoHome(
                producto = producto,
                navigateToDetallesNuevo = navigateToDetallesNuevo,
                productoNuevoCompartiido = productoNuevoCompartiido
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeTopAppBar(modifier: Modifier = Modifier, drawerState: DrawerState, scope: CoroutineScope, homeViewModel: HomeViewModel) {
    var optionButtonExpanded by remember { mutableStateOf(false) }
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(15.dp),
                modifier = modifier.fillMaxHeight()
            ) {
                Image(
                    painter = painterResource(id = R.drawable.vazlo_blanco),
                    contentDescription = "",
                    modifier = modifier.size(33.dp),
                    contentScale = ContentScale.Crop
                )
                Text(text = stringResource(R.string.refaccionarias))
            }
        },
        actions = {
            IconButton(onClick = { optionButtonExpanded = !optionButtonExpanded }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onSurface
                )
                OptionMenu(
                    expanded = optionButtonExpanded,
                    onDismmiss = { optionButtonExpanded = false },
                    onClick = {homeViewModel.restablecerToolTips()}
                )
            }

        },
        modifier = modifier.height(50.dp),
        navigationIcon = {
            IconButton(onClick = { scope.launch { drawerState.open() } }) {
                Icon(
                    painter = painterResource(id = R.drawable.menu),
                    contentDescription = "",
                )
            }
        }
    )
}


@Composable
private fun OptionMenu(
    modifier: Modifier = Modifier,
    expanded: Boolean,
    onDismmiss: () -> Unit,
    onClick: () -> Unit
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismmiss,
        modifier = modifier.background(MaterialTheme.colorScheme.background)
    ) {
        DropdownMenuItem(
            text = { Text(stringResource(R.string.activar_tutorial)) },
            onClick = {
                onClick()
                onDismmiss()
            },
            colors = MenuDefaults.itemColors(
                textColor = MaterialTheme.colorScheme.onSurfaceVariant
            )

        )
    }
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun CarrouselHome(
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    banners: MutableList<Promocion>,
) {
    HorizontalPager(
        modifier = modifier
            .fillMaxWidth(),
        state = pagerState,
        userScrollEnabled = true,
        pageSize = PageSize.Fill,
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 10.dp),
        pageSpacing = 20.dp
    ) {
        ElevatedCard(
            shape = RoundedCornerShape(10.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
            colors = CardDefaults.cardColors(contentColor = Gris_Vazlo),
            modifier = modifier
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
                modifier = modifier.fillMaxWidth()
            ) {
                val scale = remember { mutableStateOf(1f) }
                val offset = remember { mutableStateOf(Offset.Zero) }
                AsyncImage(
                    model = ImageRequest.Builder(context = LocalContext.current)
                        .diskCachePolicy(CachePolicy.DISABLED)
                        .data(banners[it].foto)
                        .crossfade(true).build(),
                    error = painterResource(R.drawable.imagen),
                    placeholder = painterResource(R.drawable.download_file__1_),
                    contentDescription = banners[it].descripcion,
                    contentScale = ContentScale.Fit,
                    modifier = modifier
                        .scale(scale.value)
                        .offset { IntOffset(offset.value.x.toInt(), offset.value.y.toInt()) }
                        .pointerInput(Unit) {
                            detectTapGestures(
                                onDoubleTap = {
                                    scale.value = if (scale.value == 1f) 2f else 1f
                                    if (scale.value == 1f) {
                                        offset.value = Offset.Zero
                                    }
                                }
                            )
                        }.let { if (scale.value > 1f) it.transformable(state = rememberTransformableState { zoomChange, offsetChange, _ ->
                            offset.value += offsetChange
                        }) else it }

                )
            }
        }
    }
}


/*@Composable
fun PIimge(modifier: Modifier = Modifier, image: Int) {
    Image(
        painter = painterResource(id = image),
        contentDescription = "",
        modifier = modifier.size(150.dp)
    )
}*/


@Composable
fun CardProductoHome(
    producto: Producto,
    modifier: Modifier = Modifier,
    navigateToDetallesNuevo: (String) -> Unit,
    productoNuevoCompartiido: ProductoNuevoCompartiido
) {
    ElevatedCard(
        modifier = modifier
            .padding(10.dp)
            .clickable {
                productoNuevoCompartiido.setProducto(producto)
                navigateToDetallesNuevo(producto.nombreArticulo!!)
            },
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primary)
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            AsyncImage(
                model = ImageRequest.Builder(context = LocalContext.current).data(producto.foto)
                    .diskCachePolicy(CachePolicy.DISABLED)
                    .crossfade(true).build(),
                error = painterResource(R.drawable.image_break__blanco),
                placeholder = painterResource(R.drawable.download_file__1_),
                contentDescription = producto.descripcion,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxWidth()
            )
            Text(text = producto.nombreArticulo!!, style = MaterialTheme.typography.bodyMedium)
        }
    }
}










