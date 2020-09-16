package lauks.sebastian.footballacademies.view.academies

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.academy_item.view.*
import lauks.sebastian.footballacademies.R
import lauks.sebastian.footballacademies.model.academy.Academy

class AcademiesAdapter(
    private val academiesList: LiveData<List<Academy>>
): RecyclerView.Adapter<AcademiesAdapter.AcademiesViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AcademiesViewHolder {
        val itemView =LayoutInflater.from(parent.context).inflate(R.layout.academy_item, parent, false)
        val holder =AcademiesViewHolder(itemView)

        //HERE e.g. holder.itemView.setOnClickListener{....

        return holder
    }

    override fun getItemCount(): Int = academiesList.value!!.size

    override fun onBindViewHolder(holder: AcademiesViewHolder, position: Int) {
        val currentItem = academiesList.value!![position]
        holder.tvAcademyName.text = currentItem.name

    }


    class AcademiesViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val tvAcademyName = itemView.tv_academy_name
    }
}