package vazlo.refaccionarias.ui.screens.usuarios_y_permisos

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DoNotDisturbOn
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.skydoves.balloon.compose.Balloon
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import vazlo.refaccionarias.R
import vazlo.refaccionarias.data.model.users_y_permisosData.Permisos
import vazlo.refaccionarias.ui.AppViewModelProvider
import vazlo.refaccionarias.ui.navigation.NavigationDestination
import vazlo.refaccionarias.ui.screens.cart.CarritoUiState
import vazlo.refaccionarias.ui.screens.cart.FooterCart
import vazlo.refaccionarias.ui.screens.cart.ProductList
import vazlo.refaccionarias.ui.screens.resultadoPorPartes.AltScreen
import vazlo.refaccionarias.ui.theme.Gris_Vazlo
import vazlo.refaccionarias.ui.theme.Negro
import vazlo.refaccionarias.ui.theme.Rojo_Vazlo
import vazlo.refaccionarias.ui.theme.Verde_Success


object PermisosDestination : NavigationDestination {
    override val route = "permisos"
    const val itemIdArg = "itemId"
    val routeWithArgs = "$route/{$itemIdArg}"
}

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun PermisosScreen(
    modifier: Modifier = Modifier, navigateBack: () -> Unit,
    permisosViewModel: PermisosViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {

    val scope = rememberCoroutineScope()
    var showSuccess by remember {
        mutableStateOf(false)
    }
    var showError by remember {
        mutableStateOf(false)
    }
    var fabHeight by remember { mutableIntStateOf(0) }
    Scaffold(
        topBar = { TopBarPedidos(navigateBack = navigateBack) },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.onGloballyPositioned {
                    fabHeight = it.size.height
                },
                onClick = {
                    scope.launch {
                        if (permisosViewModel.actualizarPermisos()) {
                            showSuccess = true
                        } else {
                            showError = true
                        }
                    }
                }) {
                Icon(
                    imageVector = Icons.Filled.Check,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.outline
                )
            }
        },
        floatingActionButtonPosition = FabPosition.End
    ) {
        Column(modifier = modifier.padding(it)) {
            Surface {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .background(MaterialTheme.colorScheme.primary),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = stringResource(
                            R.string.permisos_de_usuario,
                            permisosViewModel.itemId
                        ),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
            ListaPermisos(permisosViewModel = permisosViewModel, fabHeight = fabHeight)
            when (permisosViewModel.permisosUiState) {
                is PermisosUiState.Loading -> {
                    LoadingEstados()
                }

                is PermisosUiState.Success -> {

                }

                is PermisosUiState.Error -> {

                }

                else -> {}
            }

        }
    }
    SuccessMessage(onDismiss = { showSuccess = false }, showDialog = showSuccess, mensaje = "Permisos Actualizados")
    ErrorMessage(onDismiss = { showError = false }, showDialog = showError, mensaje = "Ocurrio un erro al actualizar")
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoadingEstados(modifier: Modifier = Modifier) {
    AlertDialog(
        onDismissRequest = {},
        modifier = modifier.background(Color.Transparent),
        content = {
            Column(
                modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
            }
        }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarPedidos(modifier: Modifier = Modifier, navigateBack: () -> Unit) {
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
            IconButton(onClick = { navigateBack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "",
                    modifier = modifier.size(30.dp),
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
            /*            IconButton(onClick = { *//*TODO*//* }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }*/

        },
        modifier = modifier.height(50.dp)
        /*colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )*/
    )
}


@Composable
fun ListaPermisos(
    modifier: Modifier = Modifier,
    permisosViewModel: PermisosViewModel,
    fabHeight: Int
) {
    val heightInDp = with(LocalDensity.current) { fabHeight.toDp() }
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(20.dp),
        contentPadding = PaddingValues(bottom = heightInDp + 16.dp, start = 10.dp, end = 10.dp, top = 20.dp),
        modifier = modifier,
    ) {
        items(permisosViewModel.permisosList) { permiso ->
            PermisoCard(permiso = permiso)
        }
    }
}


@Composable
fun PermisoCard(
    permiso: Permisos,
    modifier: Modifier = Modifier
) {
    var expanded by remember { mutableStateOf(false) }
    OutlinedCard(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onSurface
        ),
        modifier = modifier.border(
            width = 2.dp,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            shape = RoundedCornerShape(10.dp)
        )
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = modifier
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                )
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = modifier
                    .padding(10.dp)
                    .fillMaxWidth()
            ) {
                TituloPermiso(permiso)
                PermisoButtonExpand(expanded = expanded, onClick = { expanded = !expanded })
            }
            if (expanded) {
                Column(
                    modifier = modifier.padding(10.dp)
                ) {
                    Text(
                        text = stringResource(id = permiso.infExtra!!),
                        style = MaterialTheme.typography.bodyMedium,
                        color = Negro
                    )
                }
            }
        }
    }
}

@Composable
private fun TituloPermiso(permiso: Permisos, modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier.width(250.dp)
    ) {
        PedidoSwitch(checked = permiso.checkState)
        Text(
            text = stringResource(id = permiso.title!!),
            style = MaterialTheme.typography.bodyMedium,
            softWrap = true,
            color = Negro,

            )
    }
}

@Composable
private fun PedidoSwitch(checked: MutableState<Boolean>) {

    // Icon isn't focusable, no need for content description
    val icon: (@Composable () -> Unit) = if (checked.value) {
        {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = null,
                modifier = Modifier.size(SwitchDefaults.IconSize),
            )
        }
    } else {
        {
            Icon(
                imageVector = Icons.Filled.DoNotDisturbOn,
                contentDescription = null,
                modifier = Modifier.size(SwitchDefaults.IconSize),
            )
        }
    }
    Switch(
        modifier = Modifier.semantics { contentDescription = "Demo with icon" },
        checked = checked.value,
        onCheckedChange = { isChecked ->
            checked.value = isChecked
        },
        thumbContent = icon,
        colors = SwitchDefaults.colors(
            checkedThumbColor = Verde_Success,
            checkedTrackColor = Gris_Vazlo,
            uncheckedThumbColor = Rojo_Vazlo,
            uncheckedTrackColor = Gris_Vazlo,
        )
    )
}

@Composable
fun PermisoButtonExpand(expanded: Boolean, onClick: () -> Unit, modifier: Modifier = Modifier) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
            contentDescription = "",
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = modifier
        )
    }
}


/*@Preview(device = "id:pixel_5")
@Composable
fun PermisosScreenPreview() {
    RefaccionariasTheme {
        PermisosScreen()
    }
}*/






