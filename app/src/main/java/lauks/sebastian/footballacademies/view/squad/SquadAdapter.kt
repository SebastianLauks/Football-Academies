package lauks.sebastian.footballacademies.view.squad

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.squad_item.view.*
import lauks.sebastian.footballacademies.R
import lauks.sebastian.footballacademies.model.Player

class SquadAdapter(private val playersList: LiveData<List<Player>>) :
    RecyclerView.Adapter<SquadAdapter.SquadViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SquadViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.squad_item, parent, false)
        val holder = SquadViewHolder(itemView)

        //HERE e.g. holder.itemView.setOnClickListener{....

        return holder
    }

    override fun getItemCount() = playersList.value!!.size

    override fun onBindViewHolder(holder: SquadViewHolder, position: Int) {
        val currentItem = playersList.value!![position]
        holder.tvFirstname.text = currentItem.firstname
        holder.tvLastname.text = currentItem.lastname
        holder.tvAge.text = currentItem.age.toString()
        holder.tvHeight.text = currentItem.height.toString()
        holder.tvWeight.text = currentItem.weight.toString()

        val prefFootText = when(currentItem.prefFoot){
            0 -> "prawa"
            1 -> "lewa"
            2 -> "obie"
            else -> "nieznana"
        }
        holder.tvPrefFoot.text = prefFootText

    }


    class SquadViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvFirstname = itemView.tv_squad_firstname
        val tvLastname = itemView.tv_squad_lastname
        val tvHeight = itemView.tv_squad_height_value
        val tvWeight = itemView.tv_squad_weight_value
        val tvPrefFoot = itemView.tv_squad_pref_foot_value
        val tvAge = itemView.tv_squad_age_value
    }
}