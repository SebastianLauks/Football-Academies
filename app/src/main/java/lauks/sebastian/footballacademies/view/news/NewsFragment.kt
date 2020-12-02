package lauks.sebastian.footballacademies.view.news


import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.synthetic.main.fragment_news.*

import lauks.sebastian.footballacademies.R
import lauks.sebastian.footballacademies.model.news.News
import lauks.sebastian.footballacademies.utilities.CustomDialogGenerator
import lauks.sebastian.footballacademies.utilities.InjectorUtils
import lauks.sebastian.footballacademies.viewmodel.news.NewsViewModel


/**
 * A simple [Fragment] subclass.
 */
class NewsFragment : Fragment() {

    private lateinit var viewModel: NewsViewModel
    private lateinit var loggedUserId: String
    private lateinit var chosenAcademyId: String


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

        chosenAcademyId = activity!!.intent.extras!!.get("chosenAcademyId").toString()
        val sharedPref = activity!!.getSharedPreferences(resources.getString(R.string.app_name),
            Context.MODE_PRIVATE)
        loggedUserId = sharedPref.getString("loggedUserId", "unknown").toString()

        refreshNews()

        news_recycler_view.adapter = NewsAdapter(viewModel.getNewss(), viewModel.getUsers(), onNewsLongClick, scrollToPosition, viewModel )
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

    private val scrollToPosition = { position: Int ->
        news_recycler_view.scrollToPosition(position)

    }

    override fun onResume() {
        super.onResume()
        refreshNews()

    }

    private fun setupFab() {
        val fab = news_fab_create
        fab.setOnClickListener {
            val intent = Intent(activity, CreateNewsActivity::class.java)
            activity!!.startActivity(intent)
        }
    }

    private val hideRefreshingIndicator = {
        news_swipe_refresh_layout?.isRefreshing = false
    }

    private val onNewsLongClick = { id: String ->

        val news: News? = viewModel.getNewss().value!!.find { news ->
            news.id == id
        }
        if (news != null && news.authorId == loggedUserId) {
            CustomDialogGenerator.createCustomDialog(
                context!!,
                "Czy chcesz usunąć wybrany post?",
                "Tak",
                "Nie"
            ) {
                viewModel.removeNews(news.id){
                    Toast.makeText(context, "Post został usunięty", Toast.LENGTH_SHORT).show()
                    refreshNews()
                }

                if(news.imageName != null){
                    viewModel.removeImage(news.imageName){
                        if (it){

                        }else{

                        }
                    }
                }

                if(news.videoName != null && news.videoName != ""){
                    viewModel.removeVideo(news.videoName){
                        if(it){

                        }else{

                        }
                    }
                }

            }
        } else {
            Toast.makeText(context, "Nie można usuwać cudzych postów", Toast.LENGTH_SHORT).show()
        }
    }




    private fun refreshNews(){
        news_swipe_refresh_layout.isRefreshing = true
        viewModel.startListening(chosenAcademyId, hideRefreshingIndicator)
    }


}
