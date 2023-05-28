import com.google.maps.GeoApiContext
import com.google.maps.DirectionsApi
import com.google.maps.PendingResult
import com.google.maps.model.DirectionsResult
import com.google.maps.model.LatLng as GeoApiLatLng

class DirectionsApiService {
    private val context = GeoApiContext.Builder()
        .apiKey("AIzaSyD0qbO6ky4GKGxY3swBPUXXHIJRexoPuJM") // Replace with your own API key
        .build()

    fun getDirections(
        origin: GeoApiLatLng,
        destination: GeoApiLatLng,
        onSuccess: (DirectionsResult?) -> Unit,
        onFailure: (Throwable?) -> Unit
    ) {
        DirectionsApi.newRequest(context)
            .origin(origin)
            .destination(destination)
            .setCallback(object : PendingResult.Callback<DirectionsResult> {
                override fun onResult(result: DirectionsResult?) {
                    onSuccess(result)
                }

                override fun onFailure(e: Throwable?) {
                    onFailure(e)
                }
            })
    }
}
