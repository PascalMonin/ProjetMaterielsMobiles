package epf.m1.min2.ProjetMaterielsMobiles.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import epf.m1.min2.ProjetMaterielsMobiles.MapsActivity
import epf.m1.min2.ProjetMaterielsMobiles.R
import epf.m1.min2.ProjetMaterielsMobiles.adapter.StationAdapter
import epf.m1.min2.ProjetMaterielsMobiles.api.RetroModel
import epf.m1.min2.ProjetMaterielsMobiles.db.AppDatabase

class HomeFragment(
    private val context:MapsActivity
) : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view=inflater?.inflate(R.layout.fragment_home,container,false)

        val db = Room.databaseBuilder(
            context,
            AppDatabase::class.java, "stationDb"
        ).allowMainThreadQueries().build()
        val stationList= ArrayList(db.stationDao().getAll())

        //recuperer recyclerview
        val horizontalRecyclerView=view.findViewById<RecyclerView>(R.id.horizontal_recycler_view)
        horizontalRecyclerView.adapter=StationAdapter(context, stationList,R.layout.item_horizontal_station)

        /*//recuperer second recyclerview
        val verticalRecyclerView=view.findViewById<RecyclerView>(R.id.vertical_recycler_view)
        verticalRecyclerView.adapter=StationAdapter(context,stationList,R.layout.item_vertical_station)*/

        return view
    }
}