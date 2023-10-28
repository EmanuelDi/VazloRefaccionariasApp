package vazlo.refaccionarias.ui.screens.login

import android.os.Build
import androidx.annotation.RequiresExtension
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import vazlo.refaccionarias.R
import vazlo.refaccionarias.navigation.NavigationDestination
import vazlo.refaccionarias.ui.AppViewModelProvider
import kotlinx.coroutines.launch
import vazlo.refaccionarias.ui.theme.VazloRefaccionariasTheme
import java.net.UnknownHostException

object LoginDestination : NavigationDestination {
    override val route = "login"
    /*override val titleRes = R.string.item_entry_title*/
}


@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    navigateToHome: () -> Unit,
    loginViewModel: LoginViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val isFocused = remember { mutableStateOf(false) }
    VazloRefaccionariasTheme {
        Surface(color = MaterialTheme.colorScheme.primary) {
            LazyColumn(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = modifier
                    .padding(end = 30.dp, start = 30.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.Center,
            ) {
                item {
                    ImageVazlo(isFocused = isFocused)
                    HorizontalDivider(
                        modifier = modifier.padding(bottom = 30.dp, top = 30.dp),
                        color = MaterialTheme.colorScheme.outline
                    )
                    LoginBody(loginViewModel = loginViewModel, isFocused = isFocused)
                    Spacer(modifier = modifier.height(70.dp))
                    FooterLogin(navigateToHome = navigateToHome, loginViewModel = loginViewModel)
                }
            }
        }
    }
}

@Composable
fun ImageVazlo(modifier: Modifier = Modifier, isFocused: MutableState<Boolean>) {
    Image(
        painter = painterResource(id = R.drawable.vazlo_logos_1_1),
        contentDescription = "",
        modifier = modifier
            .animateContentSize(
                spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            )
            .size(if (isFocused.value) 100.dp else 200.dp),
        contentScale = ContentScale.Crop
    )
}

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun LoginBody(
    modifier: Modifier = Modifier,
    loginViewModel: LoginViewModel,
    isFocused: MutableState<Boolean>,
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(20.dp),
        modifier = modifier
    ) {
        Text(
            text = stringResource(R.string.bienvenido),
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
        FormLogin(loginViewModel = loginViewModel, isFocused = isFocused)
    }
}

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun FormLogin(
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    loginViewModel: LoginViewModel,
    isFocused: MutableState<Boolean>,
) {
    val focusManager = LocalFocusManager.current
    val focusRequester = remember { FocusRequester() }
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(30.dp),
    ) {
        OutlinedTextField(
            value = loginViewModel.usuario,
            onValueChange = { loginViewModel.onUsuarioChanged(it) },
            label = {
                Text(
                    text = stringResource(R.string.usuario),
                    style = MaterialTheme.typography.labelLarge
                )
            },
            modifier = modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .onFocusChanged { focusState ->
                    isFocused.value = focusState.isFocused
                },
            enabled = enabled,
            singleLine = true,
            shape = RoundedCornerShape(10.dp),
            colors = TextFieldDefaults.colors(
                focusedIndicatorColor = MaterialTheme.colorScheme.outline,
                focusedContainerColor = MaterialTheme.colorScheme.primary,
                unfocusedContainerColor = MaterialTheme.colorScheme.primary,
                focusedLabelColor = MaterialTheme.colorScheme.outline,
                unfocusedLabelColor = MaterialTheme.colorScheme.outline,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.outline,
                focusedTextColor = MaterialTheme.colorScheme.outline,
                unfocusedTextColor = MaterialTheme.colorScheme.outline,
                cursorColor = MaterialTheme.colorScheme.outline
            ),
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus(force = true) }),
            textStyle = MaterialTheme.typography.labelLarge

        )
        OutlinedTextField(
            value = loginViewModel.contrasenia,
            onValueChange = { loginViewModel.onPassChanged(it) },
            visualTransformation = PasswordVisualTransformation(),
            label = {
                Text(
                    text = stringResource(R.string.contrasena),
                    style = MaterialTheme.typography.labelLarge
                )
            },
            modifier = modifier
                .fillMaxWidth()
                .focusRequester(focusRequester)
                .onFocusChanged { focusState ->
                    isFocused.value = focusState.isFocused
                },
            enabled = enabled,
            singleLine = true,
            shape = RoundedCornerShape(10.dp),
            colors = TextFieldDefaults.colors(
                focusedTextColor = MaterialTheme.colorScheme.outline,
                unfocusedTextColor = MaterialTheme.colorScheme.outline,
                focusedContainerColor = MaterialTheme.colorScheme.primary,
                unfocusedContainerColor = MaterialTheme.colorScheme.primary,
                disabledContainerColor = MaterialTheme.colorScheme.primary,
                cursorColor = MaterialTheme.colorScheme.outline,
                focusedIndicatorColor = MaterialTheme.colorScheme.outline,
                unfocusedIndicatorColor = MaterialTheme.colorScheme.outline,
                focusedLabelColor = MaterialTheme.colorScheme.outline,
                unfocusedLabelColor = MaterialTheme.colorScheme.outline,
            ),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Done,
                keyboardType = KeyboardType.Password
            ),
            keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus(force = true) }),
            textStyle = MaterialTheme.typography.labelLarge
        )
    }
}


@Composable
private fun NoInicioSesionDialog(
    modifier: Modifier = Modifier,
    show: Boolean,
    mensaje: String,
    onDismiss: () -> Unit,
) {
    if (show) {
        AlertDialog(
            modifier = modifier,
            onDismissRequest = onDismiss,
            title = {
                Text(
                    text = stringResource(R.string.no_se_ha_iniciado),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            text = {
                Text(
                    text = mensaje,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            },
            confirmButton = {
                TextButton(onClick = onDismiss) {
                    Text(text = "Aceptar")
                }
            },
            containerColor = MaterialTheme.colorScheme.background
        )
    }
}

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@Composable
fun FooterLogin(
    modifier: Modifier = Modifier,
    navigateToHome: () -> Unit,
    loginViewModel: LoginViewModel
) {
    val scope = rememberCoroutineScope()
    var show by remember { mutableStateOf(false) }
    var mensaje by remember { mutableStateOf("") }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(60.dp),
        modifier = modifier.padding(start = 50.dp, end = 50.dp)
    ) {
        Button(
            onClick = {
                scope.launch {
                    try {
                        if (loginViewModel.login() == 0) {
                            mensaje = "Verifique que el usuario o contraseña sean correctos"
                            show = true
                        } else {
                            navigateToHome()
                        }
                    } catch (e: UnknownHostException) {
                        mensaje = "Sin conexión a internet"
                        show = true
                    } catch (e: Exception) {
                        mensaje = "Tiempo de espera excedido"
                        show = true
                    }
                }
            },
            shape = RoundedCornerShape(10.dp),
            modifier = modifier
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.secondaryContainer)
        ) {
            Text(
                text = stringResource(R.string.iniciar_sesion),
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }

        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(top = 20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(
                text = stringResource(R.string.recuperar_contrasena),
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
            HorizontalDivider(
                modifier
                    .height(40.dp)
                    .width(2.dp)
            )
            Text(
                text = stringResource(R.string.registrarse),
                fontSize = 15.sp,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
    if (show) {
        NoInicioSesionDialog(
            modifier = modifier,
            show = show,
            onDismiss = { show = false },
            mensaje = mensaje
        )
    }
}


/*@Preview
@Composable
fun FormLoginPreview() {
    RefaccionariasTheme {
        FormLogin()
    }
}

@Preview
@Composable
fun FooterLoginPreview() {
    FooterLogin()
}

@Preview
@Composable
fun LoginBodyPreview() {
    LoginBody()
}*/


/*
@Preview
@Composable
fun LoginScreenDarkPreview() {
    RefaccionariasTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
        ) {
            LoginScreen()
        }
    }
}
*/
