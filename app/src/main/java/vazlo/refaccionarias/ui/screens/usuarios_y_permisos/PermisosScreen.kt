package vazlo.refaccionarias.ui.screens.usuarios_y_permisos

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import vazlo.refaccionarias.R
import vazlo.refaccionarias.data.model.Permisos
import vazlo.refaccionarias.navigation.NavigationDestination
import vazlo.refaccionarias.ui.AppViewModelProvider


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
    var fabHeight by remember { mutableIntStateOf(0) }
    Scaffold(
        topBar = { TopBarPedidos(navigateBack = navigateBack) },
        floatingActionButton = {
            FloatingActionButton(
                modifier = Modifier.onGloballyPositioned {
                    fabHeight = it.size.height
                }, onClick = { permisosViewModel.actualizarPermisos() }) {
                Icon(imageVector = Icons.Filled.Check, contentDescription = "")
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
        }
    }
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
    ) {
        items(permisosViewModel.permisosList) { permiso ->
            PermisoCard(permiso = permiso, permisosViewModel = permisosViewModel)
        }
    }
}


@Composable
fun PermisoCard(
    permiso: Permisos,
    modifier: Modifier = Modifier,
    permisosViewModel: PermisosViewModel
) {
    var expanded by remember { mutableStateOf(false) }
    val color by animateColorAsState(
        targetValue = if (expanded) MaterialTheme.colorScheme.inverseSurface else MaterialTheme.colorScheme.surfaceVariant,
        label = "",
    )
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
                        style = MaterialTheme.typography.bodyMedium
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
            softWrap = true
        )
    }
}

@Composable
private fun PedidoSwitch(checked: MutableState<Boolean>) {

    // Icon isn't focusable, no need for content description
    val icon: (@Composable () -> Unit)? = if (checked.value) {
        {
            Icon(
                imageVector = Icons.Filled.Check,
                contentDescription = null,
                modifier = Modifier.size(SwitchDefaults.IconSize),
            )
        }
    } else {
        null
    }
    Switch(
        modifier = Modifier.semantics { contentDescription = "Demo with icon" },
        checked = checked.value,
        onCheckedChange = { isChecked ->
            checked.value = isChecked
        },
        thumbContent = icon
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






