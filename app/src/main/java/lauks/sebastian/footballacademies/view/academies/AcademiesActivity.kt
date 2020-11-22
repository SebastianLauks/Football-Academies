package lauks.sebastian.footballacademies.view.academies

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_academies.*
import lauks.sebastian.footballacademies.R
import lauks.sebastian.footballacademies.utilities.InjectorUtils
import lauks.sebastian.footballacademies.viewmodel.academies.AcademiesViewModel

class AcademiesActivity : AppCompatActivity() {


    private lateinit var viewModel: AcademiesViewModel

    lateinit var fab: FloatingActionButton
    lateinit var fabCreate: FloatingActionButton
    lateinit var fabJoin: FloatingActionButton
    private var isFABOpen = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_academies)

        initUI()
    }

    private fun initUI(){
        val factory = InjectorUtils.provideAcademiesViewModelFactory()
        viewModel = ViewModelProvider(this, factory).get(AcademiesViewModel::class.java)


        val hideRefreshingIncdicator ={
            academies_swipe_refresh_layout.isRefreshing = false
        }


        val loggedUserId = "user0001" //Todo GET USER IR

        academies_swipe_refresh_layout.isRefreshing = true
        viewModel.startListening(loggedUserId, hideRefreshingIncdicator)

        academies_recycler_view.adapter = AcademiesAdapter(viewModel.getAcademies())
        academies_recycler_view.layoutManager = GridLayoutManager(this, 2)
        academies_recycler_view.setHasFixedSize(true)
        viewModel.getAcademies().observe(this, Observer { _ ->
            (academies_recycler_view.adapter as AcademiesAdapter).notifyDataSetChanged()
         })


        academies_swipe_refresh_layout.setOnRefreshListener {
            viewModel.startListening(loggedUserId, hideRefreshingIncdicator)
        }

        setupFabs()
    }


    private fun setupFabs(){
        fab = findViewById(R.id.fab)
        fabCreate = findViewById(R.id.fabCreate)
        fabJoin = findViewById(R.id.fabJoin)
        fab.setOnClickListener {
            if(!isFABOpen){
                showFABMenu()
            }else{
                closeFABMenu()
            }

        }
        fabCreate.setOnClickListener {
            val intent = Intent(this, CreateAcademyActivity::class.java)
            startActivity(intent)
        }

        fabJoin.setOnClickListener {
            val intent = Intent(this, JoinAcademyActivity::class.java)
            startActivity(intent)
        }
    }

    fun showFABMenu() {
        isFABOpen = true
        fabCreate.animate().translationY(-resources.getDimension(R.dimen.standard_55))
        fabJoin.animate().translationY(-resources.getDimension(R.dimen.standard_105))
    }

    fun closeFABMenu() {
        isFABOpen = false
        fabCreate.animate().translationY(0f)
        fabJoin.animate().translationY(0f)
    }

}
