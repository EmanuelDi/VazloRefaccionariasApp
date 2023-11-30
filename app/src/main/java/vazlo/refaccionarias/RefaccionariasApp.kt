package vazlo.refaccionarias

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import vazlo.refaccionarias.ui.navigation.RefaccionariNavHost

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "NewApi")
@Composable
fun RefaccionariasApp(navController: NavHostController = rememberNavController()) {
    RefaccionariNavHost(navController = navController)
}


