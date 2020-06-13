package com.mnn.wallpapex


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment(), (WallpapersModel) -> Unit {
    private val firebaseRepository = FirebaseRepository()

    private var navController: NavController?=null

    private var wallpapersList: List<WallpapersModel> = ArrayList()
    private var wallpapersListAdapter: WallpapersListAdapter = WallpapersListAdapter(wallpapersList, this)
    private val wallpapersViewModel : WallpapersViewModel by viewModels()

    private var isLoading: Boolean = true
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //initialize action bar
        (activity as AppCompatActivity).setSupportActionBar(main_toolbar)
        val actionBar = (activity as AppCompatActivity).supportActionBar
        actionBar!!.title = "Wallpapex"
        //Initialize nav controller
        navController = Navigation.findNavController(view)

        //check if user is logged in
        if (firebaseRepository.getUser()==null){
            //user not logged in, Go to Register Page
            navController!!.navigate(R.id.action_homeFragment_to_registerFragment)
        }
        //initialize recycler view
        wallpapers_list_view.setHasFixedSize(true)
        wallpapers_list_view.layoutManager = GridLayoutManager(context, 3)
        wallpapers_list_view.adapter = wallpapersListAdapter
        //reached bottom of recyclerview
        wallpapers_list_view.addOnScrollListener(object: RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (!recyclerView.canScrollVertically(1) && newState == RecyclerView.SCROLL_STATE_IDLE){
                    //reached at bottom and not scrolling anymore
                    if (!isLoading){
                        //load next page
                        wallpapersViewModel.loadWallpapersData()
                        isLoading = true
//                        progress_home.visibility = View.VISIBLE
                    }
//                    progress_home.visibility = View.GONE
                }
            }
        })
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        wallpapersViewModel.getWallpapersList().observe(viewLifecycleOwner, Observer {
            wallpapersList = it
            wallpapersListAdapter.wallpapersList = wallpapersList
            wallpapersListAdapter.notifyDataSetChanged()
            //loading complete
            isLoading = false
        })

    }

    override fun invoke(wallpaper: WallpapersModel) {
        //clicked on wallpaper Item from the list. Navigate to details fragment
        val action = HomeFragmentDirections.actionHomeFragmentToDetailFragment(wallpaper.wpURL)
        navController!!.navigate(action)
    }
}
