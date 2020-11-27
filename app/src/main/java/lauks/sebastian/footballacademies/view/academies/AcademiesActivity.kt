package lauks.sebastian.footballacademies.view.academies

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.material.floatingactionbutton.FloatingActionButton
import kotlinx.android.synthetic.main.activity_academies.*
import lauks.sebastian.footballacademies.R
import lauks.sebastian.footballacademies.model.academy.Academy
import lauks.sebastian.footballacademies.utilities.CustomDialogGenerator
import lauks.sebastian.footballacademies.utilities.InjectorUtils
import lauks.sebastian.footballacademies.view.drawer.DrawerActivity
import lauks.sebastian.footballacademies.view.profile.EditProfileActivity
import lauks.sebastian.footballacademies.viewmodel.academies.AcademiesViewModel

class AcademiesActivity : AppCompatActivity() {


    private lateinit var viewModel: AcademiesViewModel

    lateinit var fab: FloatingActionButton
    lateinit var fabCreate: FloatingActionButton
    lateinit var fabJoin: FloatingActionButton
    private var isFABOpen = false
    private lateinit var loggedUserId: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_academies)

        initUI()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.external_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.mi_edit_profile -> {
                val intent = Intent(applicationContext, EditProfileActivity::class.java)
                val loggedUserId = "user0001" //Todo GET USER IR
                intent.putExtra("loggedUserId", loggedUserId)
                startActivity(intent)
                return true
            }
        }

        return super.onOptionsItemSelected(item)
    }

    private fun initUI() {
        val factory = InjectorUtils.provideAcademiesViewModelFactory()
        viewModel = ViewModelProvider(this, factory).get(AcademiesViewModel::class.java)

        loggedUserId = "user0001" //Todo GET USER IR

        refreshAcademies()

        academies_recycler_view.adapter =
            AcademiesAdapter(viewModel.getAcademies(), onAcademyClick, onAcademyLongClick)
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

    private fun refreshAcademies() {
        academies_swipe_refresh_layout.isRefreshing = true
        viewModel.startListening(loggedUserId, hideRefreshingIncdicator)
    }

    private val hideRefreshingIncdicator = {
        academies_swipe_refresh_layout.isRefreshing = false
    }

    private fun setupFabs() {
        fab = findViewById(R.id.fab)
        fabCreate = findViewById(R.id.fabCreate)
        fabJoin = findViewById(R.id.fabJoin)
        fab.setOnClickListener {
            if (!isFABOpen) {
                showFABMenu()
            } else {
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


    private val onAcademyClick = { id: String ->
        val intent = Intent(applicationContext, DrawerActivity::class.java)
        intent.putExtra("chosenAcademyId", id)
        startActivity(intent)
    }

    private val onAcademyLongClick = { id: String ->

        val academy: Academy? = viewModel.getAcademies().value!!.find { academy ->
            academy.id == id
        }
        if (academy != null) {
            CustomDialogGenerator.createCustomDialog(
                this,
                "Czy chcesz opuścić wybraną akademię?",
                "Tak",
                "Nie"
            ) {
                viewModel.leaveAcademy(academy.id, loggedUserId) {
                    refreshAcademies()
                    Toast.makeText(applicationContext, "Opuszczono akademię", Toast.LENGTH_SHORT).show()
                }

            }
        } else {
            Toast.makeText(
                this,
                "Wystąpił problem podczas opuszczania akademii",
                Toast.LENGTH_SHORT
            ).show()
        }
    }


}
