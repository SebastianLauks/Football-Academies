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

        news_recycler_view.adapter = NewsAdapter(viewModel.getNewss())
        news_recycler_view.layoutManager = LinearLayoutManager(activity)
        news_recycler_view.setHasFixedSize(true)
        viewModel.getNewss().observe(this, Observer { _ ->
            (news_recycler_view.adapter as NewsAdapter).notifyDataSetChanged()
        })

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
