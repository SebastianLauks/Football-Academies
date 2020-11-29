package lauks.sebastian.footballacademies.view.squad


import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_squad.*

import lauks.sebastian.footballacademies.R
import lauks.sebastian.footballacademies.utilities.InjectorUtils
import lauks.sebastian.footballacademies.viewmodel.squad.SquadViewModel

/**
 * A simple [Fragment] subclass.
 */
class SquadFragment : Fragment() {

    private lateinit var viewModel: SquadViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_squad, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
    }

    private fun initUI() {
        val factory = InjectorUtils.provideSquadViewModelFactory()
        viewModel = ViewModelProvider(this, factory).get(SquadViewModel::class.java)


        val chosenAcademyId = activity!!.intent.extras!!.get("chosenAcademyId").toString()

        val hideRefreshingIndicator = {
            squad_swipe_refresh_layout.isRefreshing = false
        }
        squad_swipe_refresh_layout.isRefreshing = true

        viewModel.fetchPlayers(chosenAcademyId, hideRefreshingIndicator)



        squad_recycler_view.adapter = SquadAdapter(viewModel.getPlayers())
//        squad_recycler_view.layoutManager = LinearLayoutManager(activity)
        squad_recycler_view.layoutManager = GridLayoutManager(activity, resources.getInteger(R.integer.squad_column_count))
        squad_recycler_view.setHasFixedSize(true)
        viewModel.getPlayers().observe(this, Observer { _ ->
            (squad_recycler_view.adapter as SquadAdapter).notifyDataSetChanged()
        })

        squad_swipe_refresh_layout.setOnRefreshListener {
            viewModel.fetchPlayers(chosenAcademyId, hideRefreshingIndicator)
        }
}


}
