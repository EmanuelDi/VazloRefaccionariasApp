package vazlo.refaccionarias

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.NonNull
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.core.content.ContextCompat.getString
import androidx.core.content.ContextCompat.getSystemService
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.tasks.Task
import com.google.firebase.messaging.FirebaseMessaging
import vazlo.refaccionarias.ui.navigation.RefaccionariNavHost
import com.google.android.gms.tasks.OnCompleteListener

@RequiresApi(Build.VERSION_CODES.O)
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter", "NewApi")
@Composable
fun RefaccionariasApp(navController: NavHostController = rememberNavController()) {
    RefaccionariNavHost(navController = navController)
}


