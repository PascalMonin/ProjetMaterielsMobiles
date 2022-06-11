package epf.m1.min2.ProjetMaterielsMobiles.adapter

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import epf.m1.min2.ProjetMaterielsMobiles.MapsActivity
import epf.m1.min2.ProjetMaterielsMobiles.R
import epf.m1.min2.ProjetMaterielsMobiles.api.RetroModel

class StationAdapter(
    private val context: MapsActivity,
    private val stationList:List<RetroModel>,
    private val layoutId:Int
    ) : RecyclerView.Adapter<StationAdapter.ViewHolder>(){
    class ViewHolder(view:View):RecyclerView.ViewHolder(view){
        val stationInfo:TextView?=view.findViewById(R.id.info_station)
        val starIcon=view.findViewById<ImageView>(R.id.star_icon)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view=LayoutInflater
            .from(parent.context)
            .inflate(layoutId,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //recup infos station
        val currentStation=stationList[position]
        holder.stationInfo?.text=currentStation.name

        if(currentStation.liked){
            holder.starIcon.setImageResource(R.drawable.ic_like)
        }
        else{
            holder.starIcon.setImageResource(R.drawable.ic_unlike)
        }
    }

    override fun getItemCount(): Int =stationList.size
}