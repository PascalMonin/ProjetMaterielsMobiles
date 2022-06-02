package epf.m1.min2.ProjetMaterielsMobiles

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.android.gms.maps.*
import com.google.android.gms.maps.model.BitmapDescriptor
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.gson.Gson
import epf.m1.min2.ProjetMaterielsMobiles.databinding.ActivityMapsBinding
import kotlinx.android.synthetic.main.activity_maps.*
import kotlinx.android.synthetic.main.liste_bornes.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import java.io.InputStream


class MapsActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityMapsBinding
    var arr= arrayListOf<String>()
    var name= arrayListOf<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        read_json()
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Add a marker in Paris and move the camera
        val paris = LatLng(48.86281, 2.34324)
        mMap.addMarker(MarkerOptions().position(paris).title("Marker in Paris"))
        //.icon(BitmapFromVector(getApplicationContext(), R.drawable.ic_flag)));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(paris))
        mMap.moveCamera(CameraUpdateFactory.zoomTo(12F))

// Sets the map type to be "hybrid"
        mMap.mapType = GoogleMap.MAP_TYPE_HYBRID

    }

    private fun BitmapFromVector(context: Context, vectorResId: Int): BitmapDescriptor? {
        // below line is use to generate a drawable.
        val vectorDrawable = ContextCompat.getDrawable(context, vectorResId)

        // below line is use to set bounds to our vector drawable.
        vectorDrawable!!.setBounds(
            0,
            0,
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight
        )

        // below line is use to create a bitmap for our
        // drawable which we have added.
        val bitmap = Bitmap.createBitmap(
            vectorDrawable.intrinsicWidth,
            vectorDrawable.intrinsicHeight,
            Bitmap.Config.ARGB_8888
        )

        // below line is use to add bitmap in our canvas.
        val canvas = Canvas(bitmap)

        // below line is use to draw our
        // vector drawable in canvas.
        vectorDrawable.draw(canvas)

        // after generating our bitmap we are returning our bitmap.
        return BitmapDescriptorFactory.fromBitmap(bitmap)
    }


    fun read_json(){
        var json:String?=null
        try{
            val InputStream:InputStream=assets.open("bornes.json")
            json=InputStream.bufferedReader().use { it.readText() }

            var jsonarray=JSONArray(json)
            for (i in 0..jsonarray.length()-1){
                var jsonobj=jsonarray.getJSONObject(i)
                arr.add(jsonobj.getString("station_id"))
                name.add(jsonobj.getString("name"))
            }
            var adpt=ArrayAdapter(this,android.R.layout.simple_list_item_1,arr)
            json_list.adapter=adpt
            json_list.onItemClickListener=AdapterView.OnItemClickListener { parent, view, position, id ->
                Toast.makeText(applicationContext,"name selected is"+name[position],Toast.LENGTH_LONG).show()
            }
        }
        catch (e:IOException){

        }
    }

}