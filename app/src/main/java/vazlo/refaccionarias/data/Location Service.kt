package vazlo.refaccionarias.data

import android.content.Context

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.suspendCancellableCoroutine

class LocationService {
    @OptIn(ExperimentalCoroutinesApi::class)
    suspend fun getUserLocation(context: Context): Location? {
        val fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
            if (ContextCompat.checkSelfPermission(
                    context,
                    Manifest.permission.ACCESS_FINE_LOCATION
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermission(context)
            } else {
                val locationManager =
                    context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
                val isGPSEnabled =
                    locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
                            || locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                if (!isGPSEnabled) {
                    return null
                }
            }
        }

        return suspendCancellableCoroutine { cont ->
            fusedLocationProviderClient.lastLocation.apply {
                if (isComplete) {
                    if (isSuccessful) {
                        cont.resume(result) {}
                    } else {
                        cont.resume(null) {}
                    }
                    return@suspendCancellableCoroutine
                }
                addOnSuccessListener {
                    cont.resume(it) {}
                }
                addOnFailureListener {
                    cont.resume(null) {}
                }
                addOnCanceledListener {
                    cont.resume(null) {}
                }

            }
        }
    }

    private fun requestPermission(context: Context) {
        ActivityCompat.requestPermissions(
            context as Activity,
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
            777
        )
    }
}