package lauks.sebastian.footballacademies.view.squad


import android.os.Bundle
import android.util.Log
import android.view.Display
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_squad.*

import lauks.sebastian.footballacademies.R
import lauks.sebastian.footballacademies.model.Player
import lauks.sebastian.footballacademies.utilities.InjectorUtils
import lauks.sebastian.footballacademies.viewmodel.squad.SquadViewModel

/**
 * A simple [Fragment] subclass.
 */
class SquadFragment : Fragment() {

    private lateinit var viewModel: SquadViewModel
    private lateinit var chosenRolesLiveData: MutableLiveData<Int>
    private lateinit var usersToDisplay: MutableLiveData<List<Player>>

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

        usersToDisplay = MutableLiveData()

        val hideRefreshingIndicator = {
            updateUsersToDisplay()
            squad_swipe_refresh_layout.isRefreshing = false
        }
        squad_swipe_refresh_layout.isRefreshing = true

        viewModel.fetchPlayers(chosenAcademyId, hideRefreshingIndicator)

        setupSpinner()

        squad_recycler_view.adapter = SquadAdapter(usersToDisplay)
//        squad_recycler_view.layoutManager = LinearLayoutManager(activity)
        squad_recycler_view.layoutManager =
            GridLayoutManager(activity, resources.getInteger(R.integer.squad_column_count))
        squad_recycler_view.setHasFixedSize(true)
        viewModel.getPlayers().observe(this, Observer { _ ->

            (squad_recycler_view.adapter as SquadAdapter).notifyDataSetChanged()
        })

        chosenRolesLiveData.observe(this, Observer { _ ->
            updateUsersToDisplay()
            (squad_recycler_view.adapter as SquadAdapter).notifyDataSetChanged()
        })

        squad_swipe_refresh_layout.setOnRefreshListener {
            viewModel.fetchPlayers(chosenAcademyId, hideRefreshingIndicator)
        }
    }

    private fun updateUsersToDisplay(){
        if (chosenRolesLiveData.value == 4)
            usersToDisplay.value =
                viewModel.getPlayers().value!!.filter { it.role == 0 || it.role == null }
        else
            usersToDisplay.value =
                viewModel.getPlayers().value!!.filter { it.role == chosenRolesLiveData.value }
    }

    private fun setupSpinner() {
        chosenRolesLiveData = MutableLiveData()
        chosenRolesLiveData.value = 1
        val spinner = spinner_squad_roles
        val adapter = ArrayAdapter.createFromResource(
            context!!,
            R.array.squad_roles,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onNothingSelected(parent: AdapterView<*>?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                chosenRolesLiveData.value = (position + 1)
            }
        }
    }
}
