package vazlo.refaccionarias.ui.screens.usuarios_y_permisos


import android.os.Build
import android.util.Log
import androidx.annotation.RequiresExtension
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import okhttp3.internal.notify
import vazlo.refaccionarias.R
import vazlo.refaccionarias.data.model.usuarios.Usuario
import vazlo.refaccionarias.navigation.NavigationDestination
import vazlo.refaccionarias.ui.AppViewModelProvider
import vazlo.refaccionarias.ui.theme.Amarillo_Vazlo
import vazlo.refaccionarias.ui.theme.Blanco
import vazlo.refaccionarias.ui.theme.Negro
import vazlo.refaccionarias.ui.theme.Verde_Success

object UsuariosYPermisosDestination : NavigationDestination {
    override val route = "usuarios_y_permisos"
    /*override val titleRes = R.string.item_entry_title*/
}

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun UsuariosScreen(
    modifier: Modifier = Modifier,
    navigateBack: () -> Unit,
    navigateToPermisos: (String) -> Unit,
    usuariosViewModel: UsuariosViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    var showDialog2 by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    Di(
        onDismiss = {

            showDialog2 = false
        },
        showDialog = showDialog2,
        usuariosViewModel = usuariosViewModel,
        scope = scope

    )
    Scaffold(
        topBar = { UsuariosTopBar(navigateBack = navigateBack) },
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog2 = true }) {
                Icon(
                    imageVector = Icons.Filled.Add,
                    contentDescription = "",
                    tint = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    ) {
        Column(
            modifier = modifier
                .padding(it)
        ) {
            Surface(
                color = MaterialTheme.colorScheme.background
            ) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    modifier = modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .background(MaterialTheme.colorScheme.primary),
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = stringResource(R.string.usuarios),
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

            }
            when (usuariosViewModel.usuariosUiState) {
                is UsuariosUiState.Loading -> LoadingScreen(modifier = modifier.fillMaxSize())
                is UsuariosUiState.Success -> ListaUsuarios(
                    navigateToPermisos = navigateToPermisos,
                    listUsuarios = (usuariosViewModel.usuariosUiState as UsuariosUiState.Success).usuarios,
                    usuariosViewModel = usuariosViewModel,
                )

                is UsuariosUiState.Error -> ErrorScreen(modifier = modifier.fillMaxSize())
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UsuariosTopBar(modifier: Modifier = Modifier, navigateBack: () -> Unit) {
    TopAppBar(
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(15.dp),
                modifier = modifier.fillMaxSize()
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
        },
        modifier = modifier.height(50.dp)
        /*colors = TopAppBarDefaults.smallTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer
        )*/
    )
}


@Composable
fun ErrorScreen(modifier: Modifier) {
    Text(text = "Peto", modifier = modifier)
}

@Composable
fun LoadingScreen(modifier: Modifier) {
    Text(text = "Cargando", modifier = modifier)
}


@Composable
fun ListaUsuarios(
    modifier: Modifier = Modifier,
    navigateToPermisos: (String) -> Unit,
    listUsuarios: List<Usuario>,
    usuariosViewModel: UsuariosViewModel
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(10.dp),
        modifier = modifier
            .padding(start = 20.dp, end = 20.dp, top = 20.dp)
            .fillMaxSize()
    ) {
        items(listUsuarios, key = { it.clienteId.toString() }) { usuario ->
            UsuariosBody(
                usuario = usuario,
                navigateToPermisos = { navigateToPermisos(usuario.clienteId!!) },
                usuariosViewModel = usuariosViewModel,
                onSelectUser = { usuariosViewModel.onSelectUser(usuario.clienteId!!) }
            )
        }
        item {
            Spacer(modifier = Modifier.height(50.dp)) // Ajusta este valor según tus necesidades
        }
    }
}

@Composable
fun UsuariosBody(
    modifier: Modifier = Modifier,
    usuario: Usuario,
    navigateToPermisos: () -> Unit,
    usuariosViewModel: UsuariosViewModel,
    onSelectUser: () -> Unit
) {
    var expanded by remember { mutableStateOf(false) }
    OutlinedCard(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.onSurface
        ),//relleno de las cards, color
        modifier = modifier.border(
            width = 2.dp,//grosor del borde
            color = MaterialTheme.colorScheme.onSurfaceVariant,//color del contorno de las cards
            shape = RoundedCornerShape(10.dp) //curvita
        )
    ) {
        Column(
            verticalArrangement = Arrangement.Center,//centrar la columna
            modifier = modifier.animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy, //rebote al desplegar
                    stiffness = Spring.StiffnessLow, //que tan rapido se regresa
                )
            )
        ) {
            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = modifier
                    .padding(10.dp)
                    .fillMaxWidth()
            ) {
                DatosUsuario(
                    usuario = usuario.clienteId!!,
                    nombre = usuario.clienteNombre!!,
                    correo = usuario.correo!!
                )
                Column(

                    verticalArrangement = Arrangement.Top
                ) {
                    SimpleDialogExample(
                        navigateToPermisos = navigateToPermisos,
                        usuariosViewModel,
                        onSelectUser = onSelectUser
                    )
                    BtnExpandible(expanded = expanded, onClick = {
                        expanded = !expanded
                    })
                }
            }
            if (expanded) {
                InfoExtra(modifier, usuario)
            }
        }
    }
}

@Composable
private fun InfoExtra(
    modifier: Modifier,
    usuario: Usuario
) {
    Column(
        modifier = modifier
            .padding(10.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Row{
            Text(
                text = stringResource(R.string.cliente, usuario.clienteId!!),
                color = Color.Black,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Normal,
                fontSize = 17.sp
            )
        }
        Row{
            Text(
                text = stringResource(R.string.nombre, usuario.clienteNombre!!),
                color = Color.Black,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Normal,
                fontSize = 17.sp
            )
        }
        Row {
            Text(
                text = stringResource(R.string.domicilio, usuario.domicilio!!),//control+w Y alt + intro, extract
                color = Color.Black,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Normal,
                fontSize = 17.sp
            )
        }
        Row{
            Text(
                text = stringResource(R.string.poblacion, usuario.poblacion!!),
                color = Color.Black,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Normal,
                fontSize = 17.sp
            )
        }
        Row {
            Text(
                text = stringResource(R.string.correo, usuario.correo!!),
                color = Color.Black,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Normal,
                fontSize = 17.sp
            )
        }
        Row {
            Text(
                text = stringResource(R.string.correo_alternativo, usuario.correo1!!),//control+w Y alt + intro, extract
                color = Color.Black,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Normal,
                fontSize = 17.sp
            )
        }
    }
}

@Composable
fun DatosUsuario(usuario: String, correo: String, nombre: String, modifier: Modifier = Modifier) {
    Column(
        modifier = modifier
            .width(230.dp)
            .padding(top = 10.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    )
    {
        Row{
            Text(
                text = stringResource(R.string.usuario2, usuario),
                color = Color.Black,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Normal,
                fontSize = 17.sp,
            )
        }
        Row{
            Text(
                text = stringResource(R.string.nombre, nombre),
                color = Color.Black,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Normal,
                fontSize = 17.sp,
            )
        }
        Row {
            Text(
                text = stringResource(R.string.correo, correo),//control+w Y alt + intro, extract
                color = Color.Black,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Normal,
                fontSize = 17.sp
            )
        }
    }
}

@Composable
fun ImageVazlo(modifier: Modifier = Modifier) {
    Image(
        painter = painterResource(id = R.drawable.logovazloblanco_sin_texto),
        contentDescription = "",
        modifier = modifier
            .size(50.dp)
            .padding(horizontal = 10.dp)
            .clickable { },
        contentScale = ContentScale.Fit
    )
}

@Composable
fun ImageFlecha(modifier: Modifier = Modifier, navigateBack: () -> Unit) {
    IconButton(onClick = {
        Log.i("Amanda", "Atra")
        navigateBack()
    }) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
            contentDescription = " ",
            tint = MaterialTheme.colorScheme.outlineVariant,
            modifier = modifier
                .size(50.dp)
                .padding(horizontal = 10.dp)
                .clickable { }
        )
    }
}

@Composable
fun BtnExpandible(
    expanded: Boolean,
    onClick: () -> Unit,
) {
    IconButton(onClick = { onClick() }) {
        Icon(
            imageVector = if (expanded) Icons.Filled.ExpandLess else Icons.Filled.ExpandMore,
            contentDescription = " ",
            tint = MaterialTheme.colorScheme.onSurfaceVariant,
        )
    }
}

@Composable
fun SimpleDialogExample(
    navigateToPermisos: () -> Unit,
    usuariosViewModel: UsuariosViewModel,
    onSelectUser: () -> Unit
) {
    var showDialog by remember { mutableStateOf(false) }//si es false, al abrir la app siempre saldrá por defecto el menú
    Column{
        IconButton(onClick = {
            showDialog = true
            onSelectUser()
        }) {
            Icon(
                Icons.Filled.Menu,
                contentDescription = " ",
                tint = MaterialTheme.colorScheme.primary
            )
        }
        if (showDialog) {
            SimpleDialog(
                showDialog = showDialog,
                onDismiss = { showDialog = false },//funcionamiento de close,
                navigateToPermisos = navigateToPermisos,
                usuariosViewModel = usuariosViewModel

            )
        }
    }
}


@Composable
fun Di(
    onDismiss: () -> Unit,
    showDialog: Boolean,
    modifier: Modifier = Modifier,
    usuariosViewModel: UsuariosViewModel,
    scope: CoroutineScope
) {
    var showSuccess by remember {
        mutableStateOf(false)
    }
    var showError by remember {
        mutableStateOf(false)
    }
    var showValidate by remember { mutableStateOf(false) }
    if (showDialog) {
        AlertDialog(
            title = {
                Text(
                    text = "Nuevo Usuario",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            },
            onDismissRequest = {
                usuariosViewModel.limpiarEntry()
                onDismiss()
            },
            text = {
                BodyFormDialog(
                    modifier,
                    usuariosViewModel
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (usuariosViewModel.entryUsuario.isEmpty() || usuariosViewModel.entryNombre.isEmpty() || usuariosViewModel.entryPassword.isEmpty() || usuariosViewModel.entryDomicilio.isEmpty() || usuariosViewModel.entryPoblacion.isEmpty() || usuariosViewModel.entryCorreo.isEmpty() || usuariosViewModel.entryCorreo1.isEmpty()) {
                            showValidate = true
                        } else {
                            scope.launch {
                                if (usuariosViewModel.setNuevoUsuario()) {
                                    showSuccess = true
                                } else {
                                    showError = false
                                }
                                onDismiss()
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(Color.White)
                ) {
                    Text(
                        text = "Aceptar",
                        color = Color.Black
                    )
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        usuariosViewModel.limpiarEntry()
                        onDismiss()
                    },
                    colors = ButtonDefaults.buttonColors(Color.White)
                ) {
                    Text(
                        text = "Cancelar",
                        color = Color.Black
                    )
                }
            },
            containerColor = Color.White
        )
    }
    ValidateDialog(onDismiss = { showValidate = false }, showValidate)
    SuccessMessage(onDismiss = { showSuccess = false }, showDialog = showSuccess, mensaje = "Se agregó el usuario correctamente")

    ErrorMessage(onDismiss = { showError = false }, showDialog = showError, mensaje = "No se pudo agregar el usuario")
}

@Composable
fun ValidateDialog(onDismiss: () -> Unit, showValidate: Boolean) {
    if (showValidate) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            confirmButton = { Button(
                onClick = {
                    onDismiss()
                },
                colors = ButtonDefaults.buttonColors(Color.White)
            ) {
                Text(
                    text = "Cerrar",
                    color = Color.Black
                )
            } },
            title = { Text(text = "Advertencia", color = Color.Black) },
            text = { Text(text = "Todos los campos deben estar llenos!") },
            containerColor = Color.White
        )
    }
}

@Composable
private fun BodyFormDialog(modifier: Modifier = Modifier, usuariosViewModel: UsuariosViewModel) {
    Surface(
        color = Color.White
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
        ) {

            Text(
                modifier = Modifier
                    .fillMaxWidth(),
                text = "Los datos para accesar serán tu número de cliente-usuario y la contraseña que registres.",
                color = Color.Black,
                fontSize = 18.sp
            )


            Spacer(modifier = modifier.height(10.dp))


            Text(
                text = "EJEMPLO",
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                fontSize = 15.sp,
                modifier = Modifier
            )

            Text(
                text = "Cliente:5",
                color = Color.Black,
                fontSize = 13.sp,
                modifier = Modifier
            )

            Text(
                text = "Usuario: José",
                color = Color.Black,
                fontSize = 13.sp,
            )


            Text(
                text = "Usuario a pasar: 5-José",
                color = Color.Black,
                fontSize = 13.sp,
            )


            OutlinedTextField(
                value = usuariosViewModel.entryUsuario,
                label = { Text(text = "Usuario", fontWeight = FontWeight.Normal) },
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedContainerColor = Blanco,
                    focusedContainerColor = Blanco,
                    unfocusedTextColor = Negro
                ),
                onValueChange = {
                    usuariosViewModel.onUserChange(it)
                },
                textStyle = TextStyle(fontWeight = FontWeight.Normal, fontSize = 20.sp)
            )


            OutlinedTextField(
                value = usuariosViewModel.entryNombre,
                label = { Text(text = "Nombre", fontWeight = FontWeight.Normal) },
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedContainerColor = Blanco,
                    focusedContainerColor = Blanco,
                    unfocusedTextColor = Negro
                ),
                onValueChange = {
                    usuariosViewModel.onNombreChange(it)
                },
                textStyle = TextStyle(fontWeight = FontWeight.Normal, fontSize = 20.sp)
            )
            OutlinedTextField(
                value = usuariosViewModel.entryPassword,
                label = { Text(text = "Contraseña", fontWeight = FontWeight.Normal) },
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedContainerColor = Blanco,
                    focusedContainerColor = Blanco,
                    unfocusedTextColor = Negro
                ),
                textStyle = TextStyle(fontWeight = FontWeight.Normal, fontSize = 20.sp),
                onValueChange = {
                    usuariosViewModel.onPassChange(it)
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
            )

            OutlinedTextField(
                value = usuariosViewModel.entryDomicilio,
                label = { Text(text = "Domicilio", fontWeight = FontWeight.Normal) },
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedContainerColor = Blanco,
                    focusedContainerColor = Blanco,
                    unfocusedTextColor = Negro
                ),
                textStyle = TextStyle(fontWeight = FontWeight.Normal, fontSize = 20.sp),
                onValueChange = {
                    usuariosViewModel.onDomicilioChange(it)
                }
            )
            OutlinedTextField(
                value = usuariosViewModel.entryPoblacion,
                label = { Text(text = "Población", fontWeight = FontWeight.Normal) },

                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedContainerColor = Blanco,
                    focusedContainerColor = Blanco,
                    unfocusedTextColor = Negro
                ),
                textStyle = TextStyle(fontWeight = FontWeight.Normal, fontSize = 20.sp),
                onValueChange = {
                    usuariosViewModel.onPoblacionChange(it)
                }
            )

            OutlinedTextField(
                value = usuariosViewModel.entryCorreo,
                label = { Text(text = "Correo", fontWeight = FontWeight.Normal) },
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedContainerColor = Blanco,
                    focusedContainerColor = Blanco,
                    unfocusedTextColor = Negro
                ),
                textStyle = TextStyle(fontWeight = FontWeight.Normal, fontSize = 20.sp),
                onValueChange = {
                    usuariosViewModel.onCorreoChange(it)
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )

            OutlinedTextField(
                value = usuariosViewModel.entryCorreo1,
                label = { Text(text = "Correo Alternativo", fontWeight = FontWeight.Normal) },
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedContainerColor = Blanco,
                    focusedContainerColor = Blanco,
                    unfocusedTextColor = Negro
                ),
                textStyle = TextStyle(fontWeight = FontWeight.Normal, fontSize = 20.sp),
                onValueChange = {
                    usuariosViewModel.onCorreo1Change(it)
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
            )
        }
    }
}


@Composable
fun CambiarContra(
    onDismiss: () -> Unit,
    nuevaContrasenia: Boolean,
    usuariosViewModel: UsuariosViewModel
) {

    var showSuccess by remember {
        mutableStateOf(false)
    }

    var showError by remember {
        mutableStateOf(false)
    }
    var showAlert by remember {
        mutableStateOf(false)
    }
    val scope = rememberCoroutineScope()
    if (nuevaContrasenia) {
        AlertDialog(
            onDismissRequest = {
                onDismiss()
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (usuariosViewModel.entryPassword.isEmpty()) {
                            showAlert = true
                        } else {
                            scope.launch {
                                if (usuariosViewModel.actualizarPassword()) {
                                    showSuccess = true
                                } else {
                                    showError = false
                                }
                                onDismiss()
                            }

                        }
                    },
                    colors = ButtonDefaults.buttonColors(Color.White)
                ) {
                    Text(
                        text = "Actualizar",
                        color = Color.Black
                    )
                }
            },
            text = {
                CambiarContraseniaDialog(
                    usuariosViewModel = usuariosViewModel
                )
            },
            dismissButton = {
                Button(
                    onClick = onDismiss, colors = ButtonDefaults.buttonColors(Color.White)
                ) {
                    Text(
                        text = "Cancelar",
                        color = Color.Black
                    )
                }
            },
            title = {
                Text(
                    text = "Editar Contraseña",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 25.dp, top = 15.dp, bottom = 15.dp)
                )
            },
            containerColor = Color.White
        )
    }
    ValidateDialog(onDismiss = { showAlert = false }, showAlert)

    SuccessMessage(onDismiss = { showSuccess = false }, showDialog = showSuccess, mensaje = "Se ha actualizado correctamente la contraseña")

    ErrorMessage(onDismiss = { showError = false }, showDialog = showError, mensaje = "Ha ocurrido un error al actualizar la contraseña")
}

@Composable
private fun CambiarContraseniaDialog(usuariosViewModel: UsuariosViewModel) {
    Surface(
        color = Color.White
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            OutlinedTextField(
                value = usuariosViewModel.userSeleccionado,
                label = { Text(text = "Cliente: ",fontSize = 15.sp, fontWeight = FontWeight.Bold) },
                readOnly = true,
                colors = TextFieldDefaults.colors(
                    unfocusedTextColor = Color.Black,
                    disabledTextColor = Color.Black,
                    disabledLabelColor = Color.Black,
                    disabledContainerColor = Blanco,
                ),
                onValueChange = { },
                enabled = false
            )

            OutlinedTextField(
                value = usuariosViewModel.entryPassword,
                label = { Text(text = "Nueva Contraseña", fontWeight = FontWeight.Normal) },
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedContainerColor = Blanco,
                    focusedContainerColor = Blanco,
                    unfocusedTextColor = Negro
                ),
                onValueChange = {
                    usuariosViewModel.onPassChange(it)
                },
                textStyle = TextStyle(fontWeight = FontWeight.Normal, fontSize = 20.sp)
            )
        }
    }
}

@Composable
fun EditarUsuario(onDismiss: () -> Unit, editUser: Boolean, userViewModel: UsuariosViewModel) {
    var showAlert by remember {
        mutableStateOf(false)
    }
    val scope = rememberCoroutineScope()
    var showSuccess by remember {
        mutableStateOf(false)
    }

    var showError by remember {
        mutableStateOf(false)
    }

    if (editUser) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            text = { EditarDialogBody(userViewModel) },
            confirmButton = {
                Button(
                    onClick = {
                        if (userViewModel.entryNombre.isEmpty() || userViewModel.entryDomicilio.isEmpty() || userViewModel.entryPoblacion.isEmpty() || userViewModel.entryCorreo.isEmpty() || userViewModel.entryCorreo1.isEmpty()) {
                            showAlert = true
                        } else {
                            scope.launch {
                                if (userViewModel.actualizarUsuario()) {
                                    showSuccess = true
                                } else {
                                    showError = true
                                }
                                onDismiss()
                            }
                        }

                    }, colors = ButtonDefaults.buttonColors(Color.White)
                ) {
                    Text(
                        text = "Actualizar",
                        color = Color.Black
                    )
                }
            },
            dismissButton = {
                Button(
                    onClick = onDismiss, colors = ButtonDefaults.buttonColors(Color.White)
                ) {
                    Text(
                        text = "Cancelar",
                        color = Color.Black
                    )
                }
            },
            containerColor = Color.White,
            title = {
                Text(
                    text = "Editar Usuario",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp, top = 15.dp, bottom = 15.dp)
                )
            }
        )
    }
    ValidateDialog(onDismiss = { showAlert = false }, showValidate = showAlert)

    SuccessMessage(onDismiss = { showSuccess = false }, showDialog = showSuccess, mensaje = "Se  ha actualizado con exito el usuario")

    ErrorMessage(onDismiss = { showError = false }, showDialog = showError, mensaje = "Ha ocurrido un error al actualizar")

}

@Composable
fun ErrorMessage(onDismiss: () -> Unit, showDialog: Boolean, mensaje: String) {
    if (showDialog) {
        AlertDialog(
            icon = { Icon(imageVector = Icons.Filled.Error, contentDescription = "", tint = Amarillo_Vazlo, modifier = Modifier.size(100.dp)) },
            onDismissRequest = { onDismiss() },
            confirmButton = {
                Button(
                    onClick = onDismiss, colors = ButtonDefaults.buttonColors(Color.White)
                ) {
                    Text(
                        text = "Aceptar",
                        color = Color.Black
                    )
                }
            },
            text = {
                Text(text = mensaje, textAlign = TextAlign.Center)
            },
            title = { Text(text = "Alerta") },
            containerColor = MaterialTheme.colorScheme.background
        )
    }
}

@Composable
fun SuccessMessage(onDismiss: () -> Unit, showDialog: Boolean, mensaje: String) {
    if (showDialog) {
        AlertDialog(
            icon = { Icon(imageVector = Icons.Filled.CheckCircle, contentDescription = "", tint = Verde_Success, modifier = Modifier.size(100.dp)) },
            onDismissRequest = { onDismiss() },
            confirmButton = {
                Button(
                    onClick = onDismiss, colors = ButtonDefaults.buttonColors(Color.White)
                ) {
                    Text(
                        text = "Aceptar",
                        color = Color.Black
                    )
                }
            },
            text = {
                Text(text = mensaje, fontSize = 20.sp, textAlign = TextAlign.Center)
            },
            title = { Text(text = "Alerta") },
            containerColor = MaterialTheme.colorScheme.background
        )
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun EditarDialogBody(usuariosViewModel: UsuariosViewModel) {
    Surface(
        color = Color.White
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {


            OutlinedTextField(
                value = usuariosViewModel.userSeleccionado,
                label = { Text(text = "Usuario", fontSize = 15.sp, fontWeight = FontWeight.Bold) },
                colors = TextFieldDefaults.colors(
                    unfocusedTextColor = Color.Black,
                    disabledTextColor = Color.Black,
                    disabledLabelColor = Color.Black,
                    disabledContainerColor = Blanco// Color del borde cuando el campo no está enfocado
                ),
                onValueChange = {},
                enabled = false,
                textStyle = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp)
            )

            OutlinedTextField(
                value = usuariosViewModel.entryNombre,
                label = { Text(text = "Nombre", fontWeight = FontWeight.Normal) },
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    unfocusedContainerColor = Blanco,
                    focusedContainerColor = Blanco
                ),
                onValueChange = {
                    usuariosViewModel.onNombreChange(it)
                },
                textStyle = TextStyle(fontWeight = FontWeight.Normal, fontSize = 20.sp)
            )

            OutlinedTextField(
                value = usuariosViewModel.entryDomicilio,
                label = { Text(text = "Domicilio", fontWeight = FontWeight.Normal) },
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    unfocusedContainerColor = Blanco,
                    focusedContainerColor = Blanco
                ),
                onValueChange = {
                    usuariosViewModel.onDomicilioChange(it)
                },
                textStyle = TextStyle(fontWeight = FontWeight.Normal, fontSize = 20.sp)
            )


            OutlinedTextField(
                value = usuariosViewModel.entryPoblacion,
                label = { Text(text = "Población", fontWeight = FontWeight.Normal) },
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    unfocusedContainerColor = Blanco,
                    focusedContainerColor = Blanco
                ),
                onValueChange = {
                    usuariosViewModel.onPoblacionChange(it)
                },
                textStyle = TextStyle(fontWeight = FontWeight.Normal, fontSize = 20.sp)
            )
            OutlinedTextField(
                value = usuariosViewModel.entryCorreo,
                label = { Text(text = "Correo", fontWeight = FontWeight.Normal) },
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    unfocusedContainerColor = Blanco,
                    focusedContainerColor = Blanco
                ),
                onValueChange = {
                    usuariosViewModel.onCorreoChange(it)
                },
                textStyle = TextStyle(fontWeight = FontWeight.Normal, fontSize = 20.sp)
            )
            OutlinedTextField(
                value = usuariosViewModel.entryCorreo1,
                label = { Text(text = "Correo Alternativo", fontWeight = FontWeight.Normal) },
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    unfocusedTextColor = Color.Black,
                    unfocusedContainerColor = Blanco,
                    focusedContainerColor = Blanco
                ),
                onValueChange = {
                    usuariosViewModel.onCorreo1Change(it)
                },
                textStyle = TextStyle(fontWeight = FontWeight.Normal, fontSize = 20.sp)
            )
        }
    }
}

@Composable
fun EliminarUsuario(
    onDismiss: () -> Unit,
    deleteUser: Boolean,
    usuariosViewModel: UsuariosViewModel,
    onDelete: () -> Unit
) {
    var showSuccess by remember {
        mutableStateOf(false)
    }
    var showError by remember {
        mutableStateOf(false)
    }
    val scope = rememberCoroutineScope()
    if (deleteUser) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            text = { EliminarUsuarioDialogBody(usuariosViewModel = usuariosViewModel) },
            confirmButton = {
                Button(
                    onClick = {
                        scope.launch {
                            usuariosViewModel.eliminarUsuario()
                            onDismiss()
                            onDelete()
                        }
                    },
                    colors = ButtonDefaults.buttonColors(Color.White)
                ) {
                    Text(
                        text = "Confirmar",
                        color = Color.Black
                    )
                }
            },
            dismissButton = {
                Button(
                    onClick = onDismiss, colors = ButtonDefaults.buttonColors(Color.White)
                ) {
                    Text(
                        text = "Cancelar",
                        color = Color.Black
                    )
                }
            },
            title = {
                Text(
                    text = "Eliminar Usuario",
                    color = MaterialTheme.colorScheme.primary,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 20.dp, end = 20.dp, top = 15.dp)
                )
            },
            containerColor = Color.White
        )
    }
}

@Composable
private fun EliminarUsuarioDialogBody(usuariosViewModel: UsuariosViewModel) {
    Surface(
        color = Color.White
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "¿Desea eliminar el usuario ${usuariosViewModel.userSeleccionado} ?",
                color = MaterialTheme.colorScheme.primary,
                fontSize = 15.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 20.dp, end = 20.dp, top = 10.dp)
            )
        }
    }
}


@Composable
fun SimpleDialog(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    navigateToPermisos: () -> Unit,
    usuariosViewModel: UsuariosViewModel

) {
    var nuevaContrasenia by remember { mutableStateOf(false) }
    var editUser by remember { mutableStateOf(false) }
    var deleteUser by remember { mutableStateOf(false) }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = onDismiss,
            properties = DialogProperties(dismissOnBackPress = false),
            confirmButton = {
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(Color.White)
                ) {
                    Text(
                        text = "Cancelar",
                        color = Color.Black
                    )
                }
            },
            containerColor = Color.White,
            title = {
                Text(
                    text = "Seleccione una opción",
                    color = MaterialTheme.colorScheme.primary,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            },
            text = {
                Column(
                    modifier = Modifier
                        .background(color = MaterialTheme.colorScheme.onSurface)
                        .padding(15.dp),//fondo del dialog
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth(),
//                        .background(color = MaterialTheme.colorScheme.onSurface),//color de la barra de refaccionarias
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        var textoColor by remember { mutableStateOf(Color.Black) }
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clickable {
                                    textoColor = Color.Red
                                }
                                .fillMaxWidth()
                        ) {

                            TextButton(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                onClick = {
                                    onDismiss()
                                    navigateToPermisos()
                                },
                            ) {
                                Text(
                                    text = "PERMISOS DE USUARIO",
                                    color = textoColor,
                                )
                            }
                        }
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = MaterialTheme.colorScheme.onSurface),//color de la barra de refaccionarias
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        val textoColor by remember { mutableStateOf(Color.Black) }
                        Row {
                            TextButton(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                onClick = { nuevaContrasenia = true },
                            ) {
                                Text(
                                    text = "CAMBIAR CONTRASEÑA",
                                    color = textoColor,
                                )
                            }
                            CambiarContra(
                                onDismiss = { nuevaContrasenia = false },
                                nuevaContrasenia = nuevaContrasenia,
                                usuariosViewModel = usuariosViewModel
                            )
                        }
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = MaterialTheme.colorScheme.onSurface),//color de la barra de refaccionarias
                        horizontalArrangement = Arrangement.SpaceBetween

                    ) {
                        var textoColor by remember { mutableStateOf(Color.Black) }
                        Row(verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clickable {
                                    textoColor = Color.Red
                                }
                                .fillMaxWidth()
                        ) {
                            TextButton(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                onClick = { editUser = true },
                            ) {
                                Text(
                                    text = "EDITAR USUARIO",
                                    color = textoColor
                                )
                            }
                            EditarUsuario(
                                onDismiss = { editUser = false },
                                editUser = editUser,
                                userViewModel = usuariosViewModel
                            )
                        }
                    }
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(color = MaterialTheme.colorScheme.onSurface),//color de la barra de refaccionarias
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        var textoColor by remember { mutableStateOf(Color.Black) }
                        Row(verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clickable {
                                    textoColor = Color.Red
                                }
                                .fillMaxWidth()
                        ) {
                            TextButton(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                onClick = { deleteUser = true },
                            ) {
                                Text(
                                    text = "ELIMINAR USUARIO",
                                    color = textoColor,
                                )
                            }
                            EliminarUsuario(
                                onDismiss = { deleteUser = false },
                                onDelete = onDismiss ,
                                deleteUser = deleteUser,
                                usuariosViewModel = usuariosViewModel
                            )
                        }
                    }
                }
            }
        )
    }
}

//@Preview
//@Composable
//fun DatosUsuarioPreview() {
//    RepasoTheme {
////        DatosUsuario(usuario="5-Angel", nombre="Miguel Serrano", correo="a@a.com")
//            UsuariosBody(cliente= "5-cliente", nombre= "Angel",domicilio= "calle besos", poblacion="contigo", correo="l@ve.t&y", correo_alternativo="tusbes@s.tam")
//
//    }
//}

//@Preview(device = "id:pixel_5", showBackground = true, backgroundColor = 0xFF0C2340)
//@Composable
//fun UsuariosScreenPreview() {
//    RepasoTheme {
//        Surface(
//            modifier = Modifier.fillMaxSize(),
//
//
//        ) {
//            UsuariosScreen()
//        }
//    }
//}
///*modo oscuro*/
//@Preview(device = "id:pixel_5", showBackground = true)
//@Composable
//fun UsuariosScreenDarkPreview() {
//    RepasoTheme(darkTheme = true) {
//        Surface(
//            modifier = Modifier.fillMaxSize(),
//        ) {
//            UsuariosScreen()
//        }
//    }
//}
