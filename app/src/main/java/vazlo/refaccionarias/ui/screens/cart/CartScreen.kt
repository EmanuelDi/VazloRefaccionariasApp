@file:OptIn(ExperimentalMaterial3Api::class)

package vazlo.refaccionarias.ui.screens.cart

import android.app.Activity
import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
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
import androidx.core.view.WindowCompat
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import coil.request.ImageRequest
import vazlo.refaccionarias.R
import vazlo.refaccionarias.data.model.ProductoCart
import vazlo.refaccionarias.navigation.NavigationDestination
import vazlo.refaccionarias.ui.AppViewModelProvider
import vazlo.refaccionarias.ui.screens.cart.CarritoUiState
import vazlo.refaccionarias.ui.screens.cart.CartViewModel
import vazlo.refaccionarias.ui.screens.resultadoPorPartes.AltScreen
import vazlo.refaccionarias.ui.theme.Blanco
import vazlo.refaccionarias.ui.theme.Negro
import vazlo.refaccionarias.ui.theme.Rojo_Vazlo
import vazlo.refaccionarias.ui.theme.Verde_Success
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch


object CartDestination : NavigationDestination {
    override val route = "cart"
    /*override val titleRes = R.string.item_entry_title*/
}

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Boolean,
    navigateToBusquedaParte: () -> Unit,
    carritoViewModel: CartViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    LaunchedEffect(key1 = "", block = { carritoViewModel.cargarCarrito() })
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    val sheetState = rememberModalBottomSheetState()
    val scope = rememberCoroutineScope()
    var showBottomSheet by remember { mutableStateOf(false) }

    val subtotal = carritoViewModel.subtotal
    val total = carritoViewModel.total
    val iva = carritoViewModel.iva
    val cantidad = carritoViewModel.cantidad
    var showInputCantidad by remember {
        mutableStateOf(false)
    }
    var showVaciarDialog by remember { mutableStateOf(false) }
    var showEnviarDialog by remember { mutableStateOf(false) }
    val view = LocalView.current

    val window = (view.context as Activity).window
    WindowCompat.getInsetsController(window, view).isAppearanceLightNavigationBars = showBottomSheet

    Scaffold(
        topBar = { CartTopBar(navigateBack = navigateBack) },
        bottomBar = { },
        floatingActionButton = {
            Row(
                horizontalArrangement = Arrangement.SpaceAround,
                modifier = modifier.fillMaxWidth()
            ) {
                FloatingActionButton(
                    onClick = { showVaciarDialog = true },
                    containerColor = MaterialTheme.colorScheme.surface
                ) {
                    Icon(
                        imageVector = Icons.Filled.Clear,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
                FloatingActionButton(onClick = { navigateToBusquedaParte() }) {
                    Icon(
                        imageVector = Icons.Filled.Add,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
                FloatingActionButton(onClick = { showEnviarDialog = true }, containerColor = Verde_Success) {
                    Icon(
                        imageVector = Icons.Filled.Check,
                        contentDescription = "",
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        },
        floatingActionButtonPosition = FabPosition.Center
    ) {
        if (showVaciarDialog) {
            VaciarDialog(onDismiss = { showVaciarDialog = false }
            ) {
                scope.launch {
                    carritoViewModel.vaciarCarrito()
                }
            }
        }
        if (showEnviarDialog == true) {
            EnviarDialog(
                onDismiss = { showEnviarDialog = false },
                onClick = {
                    scope.launch {
                        carritoViewModel.enviarCarrito()
                    }
                },
                cantidad = cantidad.toString(),
                carritoViewModel = carritoViewModel,
                focusManager = focusManager
            )
        }
        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    scope.launch {
                        sheetState.hide()
                        showBottomSheet = false
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
                                        if (carritoViewModel.actualizarCantidadProducto(1)) {
                                            carritoViewModel.cargarCarrito()
                                        }
                                    }
                                    scope.launch {
                                        sheetState.hide()
                                        showBottomSheet = false
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
                                        if (carritoViewModel.actualizarCantidadProducto(2)) {
                                            carritoViewModel.cargarCarrito()
                                        }
                                    }
                                    scope.launch {
                                        sheetState.hide()
                                        showBottomSheet = false
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
                                        if (carritoViewModel.actualizarCantidadProducto(3)) {
                                            carritoViewModel.cargarCarrito()
                                        }
                                    }
                                    scope.launch {
                                        sheetState.hide()
                                        showBottomSheet = false
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
                                        if (carritoViewModel.actualizarCantidadProducto(4)) {
                                            carritoViewModel.cargarCarrito()
                                        }
                                    }
                                    scope.launch {
                                        sheetState.hide()
                                        showBottomSheet = false
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
                                    carritoViewModel.actualizarCantidadProducto(5)
                                }
                                scope.launch {
                                    sheetState.hide()
                                    showBottomSheet = false
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
                                        if (carritoViewModel.actualizarCantidadProducto(6)) {
                                            carritoViewModel.cargarCarrito()
                                        }
                                    }
                                    scope.launch {
                                        sheetState.hide()
                                        showBottomSheet = false
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
                            value = carritoViewModel.nuevaCant,
                            onValueChange = { input ->
                                carritoViewModel.onNuevaCantidadChange(input)
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
                                    if (carritoViewModel.actualizarCantidadProducto()) {
                                        carritoViewModel.cargarCarrito()
                                    }
                                }
                                scope.launch {
                                    sheetState.hide()
                                    showBottomSheet = false
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
                            enabled = carritoViewModel.nuevaCant.isNotBlank()
                        ) {
                            Text(text = "Confirmar")
                        }
                        Spacer(Modifier.padding(200.dp))
                    }
                }
            }
        }

        Column(modifier = modifier.padding(it)) {
            Surface {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .background(MaterialTheme.colorScheme.primary),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Carrito",
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            when (carritoViewModel.carritoUiState) {
                is CarritoUiState.Loading -> {
                    AltScreen(modifier = modifier, texto = "Cargando")
                }

                is CarritoUiState.Success -> {
                    val productos =
                        (carritoViewModel.carritoUiState as CarritoUiState.Success).productos
                    /*Log.i("sos1", "Encontrados: ${productos.size}")*/
                    Column {
                        ProductList(
                            productList = productos,
                            showBottomSheet = showBottomSheet,
                            onClick = { showBottomSheet = true },
                            viewModel = carritoViewModel,
                            scope = scope
                        )
                        FooterCart(
                            subtotal = subtotal,
                            total = total,
                            iva = iva,
                            cantidad = cantidad
                        )
                    }
                }

                is CarritoUiState.Error -> {
                    AltScreen(modifier = modifier, texto = "Error al cargar")
                }
            }
        }
    }
}

@Composable
fun EnviarDialog(
    onDismiss: () -> Unit,
    onClick: () -> Job,
    cantidad: String,
    carritoViewModel: CartViewModel,
    modifier: Modifier = Modifier,
    focusManager: FocusManager
) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            TextButton(onClick = {
                onDismiss()
                onClick()
            }
            ) {
                Text(
                    text = "Aceptar",
                    color = Negro
                )
            }
        },
        dismissButton = {
            TextButton(onClick = {
                onDismiss()
            }
            ) {
                Text(
                    text = "Cancelar",
                    color = Negro
                )
            }
        },
        title = { Text(text = "¿Estas seguro?", color = Negro) },
        text = {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier.fillMaxWidth()
            ) {
                Text(
                    text = "Se realizará el pedido de los ${cantidad} productos del carrito",
                    color = Negro
                )
                OutlinedTextField(
                    value = carritoViewModel.comentarios,
                    onValueChange = { input ->
                        carritoViewModel.onComentariosChange(input)
                    },
                    label = { Text(text = "Comentarios") },
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
                        keyboardType = KeyboardType.Text
                    ),
                    keyboardActions = KeyboardActions(onDone = {
                        focusManager.clearFocus(
                            force = true
                        )
                    }),
                    isError = false,
                    supportingText = { }
                )
            }
        },
        containerColor = MaterialTheme.colorScheme.onSurface
    )
}

@Composable
fun VaciarDialog(onDismiss: () -> Unit, onClick: () -> Job) {
    AlertDialog(
        onDismissRequest = { onDismiss() },
        confirmButton = {
            TextButton(onClick = {
                onDismiss()
                onClick()
            }
            ) {
                Text(
                    text = "Aceptar",
                    color = Negro
                )
            }
        },
        dismissButton = {
            TextButton(onClick = {
                onDismiss()
            }
            ) {
                Text(
                    text = "Cancelar",
                    color = Negro
                )
            }
        },
        title = { Text(text = "¿Estas seguro?", color = Negro) },
        text = { Text(text = "Se eliminaran todos los producto del carrito", color = Negro) },
        containerColor = MaterialTheme.colorScheme.onSurface
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartTopBar(modifier: Modifier = Modifier, navigateBack: () -> Boolean) {
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
                    modifier = modifier.size(30.dp)
                )
                Text(text = stringResource(R.string.refaccionarias))
            }
        },
        actions = {
            IconButton(onClick = { navigateBack() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "",
                    modifier = modifier.size(30.dp),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            IconButton(onClick = { /*TODO*/ }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }

        },
        modifier = modifier.height(50.dp)
        /* colors = TopAppBarDefaults.smallTopAppBarColors(
             containerColor = MaterialTheme.colorScheme.secondaryContainer
         )*/
    )

}


@Composable
fun ProductList(
    modifier: Modifier = Modifier,
    productList: MutableList<ProductoCart>,
    showBottomSheet: Boolean,
    onClick: () -> Unit,
    viewModel: CartViewModel,
    scope: CoroutineScope
) {
    val productsGroup = productList.groupBy {
        it.nombreSoporte.substringAfter("(").substringBefore(")")
    }
    LazyColumn(
        modifier.height(480.dp),
        contentPadding = PaddingValues(horizontal = 20.dp)
    ) {
        productsGroup.forEach { (nombreSoporte, items) ->
            item {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp)
                ) {
                    Text(
                        text = "Sucursal: ",
                        color = MaterialTheme.colorScheme.surface,
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 30.sp
                    )
                    Spacer(modifier = modifier.width(10.dp))
                    Text(
                        text = nombreSoporte,
                        color = MaterialTheme.colorScheme.surface,
                        style = MaterialTheme.typography.titleLarge,
                        fontSize = 30.sp
                    )
                }
            }
            items(items) { producto ->
                ItemProduct(
                    producto = producto,
                    showBottomSheet = showBottomSheet,
                    onClick = onClick,
                    viewModel = viewModel,
                    scope = scope
                )
                HorizontalDivider(
                    modifier = modifier.padding(horizontal = 30.dp, vertical = 10.dp),
                    color = Rojo_Vazlo
                )
            }
        }
    }
}

@Composable
fun ItemProduct(
    producto: ProductoCart,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
    showBottomSheet: Boolean,
    viewModel: CartViewModel,
    scope: CoroutineScope
) {
    if (viewModel.productosCargando.contains(producto.nombreSoporte)) {

        Box(
            contentAlignment = Alignment.Center, modifier = modifier
                .fillMaxWidth()
                .height(190.dp)
        ) {
            CircularProgressIndicator(color = Rojo_Vazlo, modifier = modifier.size(50.dp))
        }
    } else {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                ImageProduct(imageId = producto.url)
                IncrementInput(
                    modifier,
                    cantidad = producto.cantidad,
                    onClick = onClick,
                    onInputClick = {
                        viewModel.onSelectProducto(
                            producto.nombreSoporte
                        )
                    },
                )
            }
            Spacer(modifier = modifier.width(20.dp))
            InfoProduct(id = producto.nombreSoporte, precio = producto.precio) {
                scope.launch {
                    if (viewModel.eliminarProd(producto.nombreSoporte)) {
                        viewModel.cargarCarrito()
                    }
                }
            }
        }
    }
}


@Composable
private fun IncrementInput(
    modifier: Modifier = Modifier,
    cantidad: String,
    onClick: () -> Unit,
    onInputClick: () -> Unit,
) {
    OutlinedCard(
        modifier = modifier
            .padding(top = 10.dp)
            .height(30.dp)
            .clickable {
                onClick()
                onInputClick()
            },
        colors = CardDefaults.outlinedCardColors(
            containerColor = Color.Transparent
        ),
        border = BorderStroke(width = 1.dp, color = Negro),
        shape = RoundedCornerShape(5.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(40.dp),
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier.padding(vertical = 5.dp, horizontal = 10.dp)
        ) {
            Text(text = cantidad, modifier, color = Negro)
            Icon(imageVector = Icons.Filled.ExpandMore, contentDescription = "", tint = Negro)

        }
    }
    /*OutlinedTextField(
        value = cantidad,
        placeholder = { Text(text = cantidad, color = MaterialTheme.colorScheme.onSurfaceVariant,)},
        onValueChange = {},
        modifier = modifier
            .width(40.dp)
            .height(50.dp),
        colors = TextFieldDefaults.colors(
            unfocusedIndicatorColor = MaterialTheme.colorScheme.onSurfaceVariant,
            focusedLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
            focusedContainerColor = MaterialTheme.colorScheme.outline,
            unfocusedContainerColor = MaterialTheme.colorScheme.outline,
            focusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
            unfocusedTextColor = MaterialTheme.colorScheme.onSurfaceVariant,
            cursorColor = MaterialTheme.colorScheme.onSurfaceVariant,
            unfocusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant,
            focusedPlaceholderColor = MaterialTheme.colorScheme.onSurfaceVariant
        ),
        textStyle = TextStyle(
            fontSize = 20.sp,
            textAlign = TextAlign.Justify
        )
    )*/
}


@Composable
fun ImageProduct( imageId: String, modifier: Modifier = Modifier) {
    AsyncImage(
        model = ImageRequest.Builder(context = LocalContext.current)
            .data(imageId)
            .crossfade(true).build(),
        error = painterResource(R.drawable.imagen),
        placeholder = painterResource(R.drawable.imagen),
        contentDescription = "",
        contentScale = ContentScale.Crop,
        modifier = modifier.size(150.dp)
    )
}

@Composable
fun InfoProduct(id: String, precio: String, onClick: () -> Job) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text(
                text = stringResource(id = R.string.product_text),
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = id.substringBefore("("),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        Row(
            horizontalArrangement = Arrangement.spacedBy(5.dp)
        ) {
            Text(
                text = stringResource(id = R.string.precio_text),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = precio,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        TextButton(
            onClick = { onClick() }
        ) {
            Text(
                text = stringResource(R.string.eliminar),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.secondaryContainer
            )
        }
    }
}

@Composable
fun ImageFooter(modifier: Modifier = Modifier) {
    Image(
        imageVector = Icons.Default.ShoppingCart,
        contentDescription = "",
        modifier = modifier.size(100.dp)
    )
}

@Composable
fun InfoFooter(cantidad: Int, subtotal: String, iva: String, total: String) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.Start
    ) {
        Row {
            Text(
                text = stringResource(R.string.productos, cantidad),
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

        }
        Row {
            Text(
                text = subtotal,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Row {
            Text(
                text = iva,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
        Row {
            Text(
                text = total,
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.secondaryContainer
            )
        }
    }
}


@Composable
fun FooterCart(
    modifier: Modifier = Modifier,
    subtotal: String,
    total: String,
    iva: String,
    cantidad: Int
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        HorizontalDivider(
            modifier = modifier.padding(horizontal = 30.dp),
            thickness = 3.dp,
            color = MaterialTheme.colorScheme.secondaryContainer
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = modifier.padding(top = 10.dp)
        ) {
            ImageFooter()
            InfoFooter(cantidad = cantidad, subtotal = subtotal, iva = iva, total = total)
        }
    }
    Spacer(modifier = modifier.height(300.dp))
}

/*@Preview
@Composable
fun InfoPreview() {
    RefaccionariasTheme {
        Surface {
            InfoProduct("1626R", "$704.82")
        }
    }
}*/

/*
@Preview
@Composable
fun ItemPreview() {
    val prod = productList[0]
    RefaccionariasTheme {
        Surface {
            ItemProduct(producto = prod)
        }
    }
}*/

/*@Preview(device = "id:pixel_5")
@Composable
fun ProductListPreview() {
    RefaccionariasTheme {
        Surface(
            color = MaterialTheme.colorScheme.secondary
        ) {
            ProductList()
        }
    }
}*/


/*@Preview
@Composable
fun InfoFooterPreview() {
    RefaccionariasTheme {
        Surface(color = MaterialTheme.colorScheme.secondary) {
            InfoFooter(cantidad = "4", subtotal = 1234f, iva = 342f, total = 43234f)
        }
    }
}*/

/*@Preview
@Composable
fun FooterPreview() {
    RefaccionariasTheme {
        Surface(color = MaterialTheme.colorScheme.secondary) {
            FooterCart()
        }
    }
    
}*/

/*@Preview
@Composable
fun topAppBarPreview() {
    RefaccionariasTheme {
        CartTopBar()
    }
}*/

/*@Preview
@Composable
fun CartScreenPreview() {
    RefaccionariasTheme {
        CartScreen(navigateBack = { navController.popBackStack() }) { navController.navigate(vazlo.refaccionarias.ui.screens.busquedaPorPartes.BusquedaPorPartesDestination.route) }
    }

}*/