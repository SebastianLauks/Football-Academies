package lauks.sebastian.footballacademies.view.squad

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
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
        holder.tvSquadHeightUnit.visibility = View.VISIBLE
        holder.tvSquadWeightUnit.visibility = View.VISIBLE
        holder.tvFirstname.text = currentItem.firstname ?: ""
        holder.tvLastname.text = currentItem.lastname ?: ""
        holder.tvAge.text = if(currentItem.age!=null)currentItem.age.toString() else ""
        holder.tvHeight.text = if (currentItem.height!= null) currentItem.height.toString() else "".also { holder.tvSquadHeightUnit.visibility = View.GONE }
        holder.tvWeight.text = if (currentItem.weight!= null) currentItem.weight.toString() else "".also { holder.tvSquadWeightUnit.visibility = View.GONE }


        holder.rvUserDetails.visibility = View.GONE
        if(currentItem.role == 1){
            holder.rvUserDetails.visibility = View.VISIBLE
        }

        val prefFootText = when(currentItem.prefFoot){
            1 -> "prawa"
            2 -> "lewa"
            3 -> "obie"
            else -> ""
        }
        holder.tvPrefFoot.text = prefFootText

        holder.ivImage.setImageResource(R.drawable.avatar_default)
        if(currentItem.imageUrl != null && currentItem.imageUrl != ""){
            Picasso.get().load(currentItem.imageUrl).into(holder.ivImage)
        }

    }


    class SquadViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvFirstname = itemView.tv_squad_firstname
        val tvLastname = itemView.tv_squad_lastname
        val tvHeight = itemView.tv_squad_height_value
        val tvWeight = itemView.tv_squad_weight_value
        val tvPrefFoot = itemView.tv_squad_pref_foot_value
        val tvAge = itemView.tv_squad_age_value
        val tvSquadHeightUnit = itemView.tv_squad_height_unit
        val tvSquadWeightUnit = itemView.tv_squad_weight_unit
        val ivImage = itemView.iv_image
        val rvUserDetails = itemView.user_details
    }
}