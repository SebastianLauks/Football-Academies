package lauks.sebastian.footballacademies.view.news


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetBehavior
import kotlinx.android.synthetic.main.activity_create_news.*
import kotlinx.android.synthetic.main.fragment_news.*

import lauks.sebastian.footballacademies.R
import lauks.sebastian.footballacademies.utilities.InjectorUtils
import lauks.sebastian.footballacademies.view.drawer.DrawerActivity
import lauks.sebastian.footballacademies.viewmodel.news.NewsViewModel

/**
 * A simple [Fragment] subclass.
 */
class NewsFragment : Fragment() {

    private lateinit var viewModel: NewsViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_news, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        initUI()
    }

    private fun initUI() {
        val factory = InjectorUtils.provideNewsViewModelFactory()
        viewModel = ViewModelProvider(this, factory).get(NewsViewModel::class.java)

        val chosenAcademyId = activity!!.intent.extras!!.get("chosenAcademyId").toString()

        val hideRefreshingIndicator ={
            news_swipe_refresh_layout.isRefreshing = false
        }
        news_swipe_refresh_layout.isRefreshing = true
        viewModel.startListening(chosenAcademyId, hideRefreshingIndicator)


        news_recycler_view.adapter = NewsAdapter(viewModel.getNewss())
        val linearLayoutManager = LinearLayoutManager(activity)
//        linearLayoutManager.reverseLayout = true
//        linearLayoutManager.stackFromEnd = true
        news_recycler_view.layoutManager = linearLayoutManager
        news_recycler_view.setHasFixedSize(true)
        viewModel.getNewss().observe(this, Observer { _ ->
            (news_recycler_view.adapter as NewsAdapter).notifyDataSetChanged()
        })

        news_swipe_refresh_layout.setOnRefreshListener {
            viewModel.startListening(chosenAcademyId, hideRefreshingIndicator)
        }

        setupFab()
    }


    private fun setupFab(){
        val fab = news_fab_create
        fab.setOnClickListener {
            val intent = Intent(activity, CreateNewsActivity::class.java)
            activity!!.startActivity(intent)
        }
    }



}
