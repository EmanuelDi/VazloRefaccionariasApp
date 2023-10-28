package vazlo.refaccionarias.ui.screens.soporteTecnico

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults.topAppBarColors
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import vazlo.refaccionarias.R
import vazlo.refaccionarias.navigation.NavigationDestination
import vazlo.refaccionarias.ui.theme.VazloRefaccionariasTheme


object SoporteTecnicoDestination : NavigationDestination {
    override val route = "soporte_tecnico"
    /*override val titleRes = R.string.item_entry_title*/
}


@OptIn(ExperimentalMaterial3Api::class)
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun SoporteTecnicoScreen(modifier: Modifier = Modifier, navController: NavController) {
    VazloRefaccionariasTheme {
        Scaffold(topBar = {
            SoporteTecnicoTopBar(modifier, navController)
        }) {
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
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Content()
            }
        }
    }
}

@Composable
fun Content(modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.onPrimary)
            .padding(10.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp),
        horizontalAlignment = Alignment.CenterHorizontally,

        ) {
        //type: true equals full width

        item{
            Column(verticalArrangement = Arrangement.spacedBy(20.dp)){
                SoporteCard(
                    title = "Atención a cliente",
                    name = "Virginia Cruz",
                    mail = "atención.clientes@vazlo.com.mx",
                    phone = "4931202279",
                    whatsapp = ""
                )
                SoporteCard(
                    title = "Soporte para func. de la app",
                    name = "Antonio Pichardo",
                    mail = "",
                    phone = "4931202279",
                    whatsapp = "4931370436"
                )
                SoporteCard(
                    title = "Soporte técnico",
                    name = "Tomás Santos",
                    mail = "supervisortecnico@vazlo.com",
                    phone = "",
                    whatsapp = ""
                )
                SoporteCard(
                    title = "Corporativo VAZLO",
                    name = "",
                    mail = "",
                    phone = "4939331660",
                    whatsapp = ""
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SoporteTecnicoTopBar(modifier: Modifier, navController: NavController) {
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
            )
        }
    }, actions = {
        IconButton(onClick = { navController.popBackStack()}) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                contentDescription = "",
                modifier = modifier.size(30.dp),
                tint = MaterialTheme.colorScheme.onPrimary
            )
        }/*IconButton(onClick = { *//*TODO*//* }) {
                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = ""
                )
            }*/

    },
        colors = topAppBarColors(
            containerColor = MaterialTheme.colorScheme.secondary
        )
    )
}

//Tiene que recibir un obj
@Composable
fun SoporteCard(
    modifier: Modifier = Modifier,
    title: String,
    name: String,
    mail: String,
    phone: String,
    whatsapp: String
) {
    var expanded by remember { mutableStateOf(false) }
    val color by animateColorAsState(
        targetValue = if (expanded) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
        label = "",
    )
    Card {
        Column(
            Modifier
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessLow
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
                        color = MaterialTheme.colorScheme.secondary
                    )
                    Text(
                        text = name,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(Modifier.weight(1f))

                ExpandedButton(expanded = expanded, onClick = { expanded = !expanded })
            }

            if (expanded) {
                HorizontalDivider(
                    modifier = Modifier.padding(horizontal = 15.dp),
                    thickness = 1.dp,
                    color = MaterialTheme.colorScheme.onTertiary
                )
                Column(
                    modifier = Modifier.padding(horizontal = 15.dp, vertical = 10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    if (mail.isNotEmpty()){
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(id = R.drawable.mail_icon),
                                contentDescription = "",
                                modifier = modifier
                                    .padding(end = 10.dp)
                                    .size(38.dp)
                            )
                            Text(
                                text = mail,
                                textAlign = TextAlign.Justify
                            )
                        }
                    }
                    if (phone.isNotEmpty()){
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(id = R.drawable.llamada_icon),
                                contentDescription = "",
                                modifier = modifier
                                    .padding(end = 10.dp)
                                    .size(38.dp)
                            )
                            Text(
                                text = phone,
                                textAlign = TextAlign.Justify
                            )
                        }
                    }
                    if (whatsapp.isNotEmpty()){
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Image(
                                painter = painterResource(id = R.drawable.whatsapp_icon),
                                contentDescription = "",
                                modifier = modifier
                                    .padding(end = 10.dp)
                                    .size(38.dp)
                            )
                            Text(
                                text = whatsapp,
                                textAlign = TextAlign.Justify
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
    expanded: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(onClick = onClick) {
        Icon(
            imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
            contentDescription = "Expandir",
            modifier = modifier
        )
    }
}

/*@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun SoporteTecnicoPreview() {
    RefaccionariasTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            SoporteTecnicoScreen()
        }
    }
}*/
