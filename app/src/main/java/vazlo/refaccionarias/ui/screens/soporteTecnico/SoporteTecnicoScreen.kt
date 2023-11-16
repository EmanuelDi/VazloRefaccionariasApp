package vazlo.refaccionarias.ui.screens.soporteTecnico

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import androidx.activity.compose.ManagedActivityResultLauncher
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresExtension
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import vazlo.refaccionarias.R
import vazlo.refaccionarias.ui.navigation.NavigationDestination
import vazlo.refaccionarias.ui.AppViewModelProvider
import vazlo.refaccionarias.ui.theme.VazloRefaccionariasTheme


object SoporteTecnicoDestination : NavigationDestination {
    override val route = "soporte-tecnico"
}

@RequiresExtension(extension = Build.VERSION_CODES.S, version = 7)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SoporteTecnicoScreen(
    modifier: Modifier = Modifier, navigateBack: () -> Unit,
    viewModel: SoporteTecnicoViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    VazloRefaccionariasTheme {
        Scaffold(
            topBar = {
                SoporteTecnicoTopBar(modifier, navigateBack)
            },
            containerColor = MaterialTheme.colorScheme.background
        ) {
            Column(modifier = modifier.padding(it)) {
                Surface(color = MaterialTheme.colorScheme.primary) {
                    Row(
                        horizontalArrangement = Arrangement.Center,
                        modifier = modifier
                            .fillMaxWidth()
                            .height(50.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.soporte_t_cnico),
                            style = MaterialTheme.typography.bodyLarge,
                            color = MaterialTheme.colorScheme.outline,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Content(soporteTecnicoViewModel = viewModel)
            }
        }
    }
}

@Composable
private fun Content(
    modifier: Modifier = Modifier,
    soporteTecnicoViewModel: SoporteTecnicoViewModel
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        //type: true equals full width

        item {
            Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                SoporteCard(
                    title = "Atención a cliente",
                    name = soporteTecnicoViewModel.atencionNombre,
                    mail = soporteTecnicoViewModel.atencionCorreo,
                    phone = soporteTecnicoViewModel.atencionNumero,
                    whatsapp = "",
                    soporteTecnicoViewModel = soporteTecnicoViewModel,
                    //builder = builder
                )
                SoporteCard(
                    title = "Soporte para func. de la app",
                    name = soporteTecnicoViewModel.soporteNombre,
                    mail = "",
                    phone = soporteTecnicoViewModel.soporteNumero,
                    whatsapp = soporteTecnicoViewModel.numeroWhatsapp,
                    soporteTecnicoViewModel = soporteTecnicoViewModel,
                    //builder = builder
                )

                SoporteCard(
                    title = "Soporte técnico",
                    name = soporteTecnicoViewModel.soporteTecnicoNombre,
                    mail = soporteTecnicoViewModel.soporteTecnicoCorreo,
                    phone = "",
                    whatsapp = "",
                    soporteTecnicoViewModel = soporteTecnicoViewModel,
                    //builder = builder
                )

                SoporteCard(
                    title = "Corporativo VAZLO",
                    name = "",
                    mail = "",
                    phone = soporteTecnicoViewModel.empresaNumero,
                    whatsapp = "",
                    soporteTecnicoViewModel = soporteTecnicoViewModel,
                    //builder = builder
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SoporteTecnicoTopBar(
    modifier: Modifier, navigateBack: () -> Unit
) {
    TopAppBar(title = {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(15.dp),
        ) {
            Image(
                painter = painterResource(id = R.drawable.logovazloblanco_sin_texto),
                contentDescription = "",
                modifier = modifier.size(30.dp)
            )
            Text(
                text = stringResource(R.string.refaccionarias),
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.outline
            )
        }
    }, actions = {
        IconButton(onClick = navigateBack) {
            Icon(
                imageVector = Icons.Default.ArrowBack,
                contentDescription = "",
                modifier = modifier.size(30.dp),
                tint = MaterialTheme.colorScheme.outline
            )
        }

    }, colors = TopAppBarDefaults.topAppBarColors(
        containerColor = MaterialTheme.colorScheme.secondary
    )
    )
}

//Tiene que recibir un obj
@Composable
private fun SoporteCard(
    modifier: Modifier = Modifier,
    title: String,
    name: String,
    mail: String,
    phone: String,
    whatsapp: String,
    soporteTecnicoViewModel: SoporteTecnicoViewModel,
) {
    val context = LocalContext.current
    val dialPhoneLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) { _ ->
            // Este código se ejecutará después de que el usuario haya completado la acción en la aplicación de teléfono.
        }
    val mailLauncher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {

        }

    val openWhatsAppLauncher: ManagedActivityResultLauncher<Intent, ActivityResult> =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.StartActivityForResult()
        ) { _ ->
            // Aquí puedes manejar el resultado si es necesario
        }

    var expanded by remember { mutableStateOf(false) }
    val color by animateColorAsState(
        targetValue = if (expanded) MaterialTheme.colorScheme.surfaceVariant else MaterialTheme.colorScheme.surfaceVariant,
        label = "",
    )
    Card(

    ) {
        Column(
            Modifier
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy, stiffness = Spring.StiffnessLow
                    )
                )
                .background(color = color)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(15.dp)
            ) {

                Column(modifier = modifier.padding(start = 10.dp)) {
                    Text(
                        text = title,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.secondary,
                        fontSize = 17.sp
                    )
                    Text(
                        text = name,
                        color = MaterialTheme.colorScheme.onTertiary,
                        fontWeight = FontWeight.Bold,
                        fontSize = 17.sp
                    )
                }

                Spacer(Modifier.weight(1f))

                ExpandedButton(expanded = expanded, onClick = { expanded = !expanded })
            }

            if (expanded) {
                Divider(
                    color = MaterialTheme.colorScheme.onTertiary,
                    thickness = 1.dp,
                    modifier = Modifier.padding(horizontal = 15.dp)
                )
                Column(
                    modifier = Modifier.padding(horizontal = 15.dp, vertical = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    if (mail.isNotEmpty()) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier.clickable {
                            val clipboard =
                                context.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clip = ClipData.newPlainText("", mail)
                            clipboard.setPrimaryClip(clip)
                            val intent = Intent(Intent.ACTION_VIEW)
                            intent.data = Uri.parse("mailto:?subject=$mail")
                            mailLauncher.launch(intent)
                        }) {
                            Image(painter = painterResource(id = R.drawable.mail_icon),
                                contentDescription = "",
                                modifier = modifier
                                    .padding(end = 10.dp)
                                    .size(38.dp)
                                    )
                            Text(
                                text = mail,
                                color = MaterialTheme.colorScheme.onTertiary,
                                textAlign = TextAlign.Justify,
                                fontWeight = FontWeight.Normal,
                                fontSize = 17.sp
                            )
                        }
                    }

                    if (phone.isNotEmpty()) {
                        Row(verticalAlignment = Alignment.CenterVertically, modifier = modifier.clickable {
                            val intent = Intent(Intent.ACTION_DIAL)
                            intent.data =
                                Uri.parse("tel:$phone") // Reemplaza esto con el número de teléfono deseado
                            dialPhoneLauncher.launch(intent)
                        }) {
                            Image(painter = painterResource(id = R.drawable.llamada_icon),
                                contentDescription = "",
                                modifier = modifier
                                    .padding(end = 10.dp)
                                    .size(38.dp)

                            )
                            Text(
                                text = phone,
                                color = MaterialTheme.colorScheme.onTertiary,
                                textAlign = TextAlign.Justify,
                                fontWeight = FontWeight.Normal,
                                fontSize = 17.sp
                            )
                        }
                    }
                    if (whatsapp.isNotEmpty()) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = modifier.clickable {
                                val message =
                                    "Soy la refaccionaria. Necesito ayuda en la aplicación Vazlo Refaccionarias" // Puedes dejar esto en blanco si no deseas un mensaje predefinido
                                val uri = Uri.parse(
                                    "${soporteTecnicoViewModel.urlSoporteWhatsapp}&text=${
                                        Uri.encode(message)
                                    }"
                                )

                                val whatsappIntent = Intent(Intent.ACTION_VIEW, uri)

                                // Inicia la actividad de WhatsApp
                                openWhatsAppLauncher.launch(whatsappIntent)
                            }
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.whatsapp_icon),
                                contentDescription = "",
                                modifier = modifier
                                    .padding(end = 10.dp)
                                    .size(38.dp)
                            )
                            Text(
                                text = whatsapp,
                                color = MaterialTheme.colorScheme.onTertiary,
                                textAlign = TextAlign.Justify,
                                fontWeight = FontWeight.Normal,
                                fontSize = 17.sp
                            )
                        }
                    }
                }
            }

        }
    }
}

@Composable
private fun ExpandedButton(
    expanded: Boolean, onClick: () -> Unit
) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
            tint = MaterialTheme.colorScheme.onTertiary,
            contentDescription = "Expandir"
        )
    }
}
