//package com.balv.imdb.ui.home
//
//import android.os.Bundle
//import android.util.Log
//import android.view.LayoutInflater
//import android.view.View
//import android.view.ViewGroup
//import androidx.appcompat.app.AlertDialog
//import androidx.fragment.app.Fragment
//import androidx.lifecycle.ViewModelProvider
//import androidx.navigation.NavController
//import androidx.navigation.NavDirections
//import androidx.paging.PagingData
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.balv.imdb.R
//import com.balv.imdb.databinding.HomeFragmentBinding
//import com.balv.imdb.domain.models.ApiResult
//import com.balv.imdb.domain.models.Movie
//import com.balv.imdb.ui.home.listener.IHomeItemClickListener
//import com.balv.imdb.ui.home.listview.HomePagingAdapter
//import com.balv.imdb.ui.home.listview.LoadingPagingAdapter
//import com.google.android.material.appbar.AppBarLayout
//import com.google.android.material.dialog.MaterialAlertDialogBuilder
//import dagger.hilt.android.AndroidEntryPoint
//import javax.inject.Inject
//
//@AndroidEntryPoint
//class HomeFragment : Fragment(), IHomeItemClickListener {
//    private var mNavController: NavController? = null
//    private lateinit var mViewModel: HomeFragmentViewModel
//    private var mBinding: HomeFragmentBinding? = null
//
//    @JvmField
//    @Inject
//    var mAdapter: HomePagingAdapter? = null
//
//    override fun onCreateView(
//        inflater: LayoutInflater,
//        container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View {
//        mBinding = HomeFragmentBinding.inflate(inflater, container, false)
//        val view: View = mBinding!!.root
//        return view
//    }
//
//    private var mAlertDialog: AlertDialog? = null
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        mNavController = NavHostFragment.findNavController(this)
//        mViewModel = ViewModelProvider(this)[HomeFragmentViewModel::class.java]
//
//        mViewModel.setup()
//
//        setupListView()
//        mAlertDialog = MaterialAlertDialogBuilder(requireContext()).setTitle("Error").create()
//
//        mViewModel.errorLiveData.observe(
//            viewLifecycleOwner
//        ) { apiResult: ApiResult<*>? ->
//            if (!apiResult!!.isSuccess) {
//                mAlertDialog!!.setMessage("Something happened, check API key or network condition")
//                mAlertDialog!!.show()
//            }
//        }
//
//        mViewModel.pagingLiveData.observe(
//            viewLifecycleOwner
//        ) { pagingData->
//            Log.i(
//                TAG,
//                "PagingLiveData: $pagingData"
//            )
//            pagingData?.let {
//                mAdapter?.submitData(lifecycle, it)
//            }
//            showMoreIcon(true)
//        }
//
//        val loadStateAdapter = LoadingPagingAdapter()
//        mAdapter!!.withLoadStateFooter(loadStateAdapter)
//
//        mViewModel.listLiveData.observe(
//            viewLifecycleOwner
//        ) { list: List<Movie> ->
//            if (list.isEmpty()) {
//                updateLoadingIndicator(true)
//            } else {
//                updateLoadingIndicator(false)
//            }
//        }
//    }
//
//    override fun onResume() {
//        super.onResume()
//        val appBarLayout = activity?.findViewById<AppBarLayout>(R.id.appbar)
//        appBarLayout?.setExpanded(true, false)
//    }
//
//    override fun onDestroy() {
//        mAlertDialog!!.dismiss()
//        mAlertDialog = null
//        super.onDestroy()
//    }
//
//    override fun onItemClick(itemId: String) {
//        Log.i(TAG, "onItemClick: $itemId")
//        val action: NavDirections =
//            HomeFragmentDirections.actionHomeFragmentToDetailFragment(itemId)
//        mNavController!!.navigate(action)
//    }
//
//    private fun setupListView() {
//        mBinding!!.listView.adapter = mAdapter
//        mBinding!!.listView.layoutManager = LinearLayoutManager(context)
//        mBinding!!.listView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
//            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
//                super.onScrollStateChanged(recyclerView, newState)
//
//                if (!recyclerView.canScrollVertically(1)
//                    && newState == RecyclerView.SCROLL_STATE_IDLE
//                ) {
////                    mViewModel.getGetNextMoviePage();
//                }
//            }
//
//            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
//                super.onScrolled(recyclerView, dx, dy)
//                if (dy > 0) {
//                    showMoreIcon(false)
//                }
//            }
//        })
//    }
//
//    private fun updateLoadingIndicator(show: Boolean) {
//        mBinding!!.loadingIndicator.visibility =
//            if (show) View.VISIBLE else View.GONE
//    }
//
//    private fun showMoreIcon(show: Boolean) {
//        mBinding!!.more.visibility = if (show) View.VISIBLE else View.GONE
//    }
//
//    companion object {
//        private const val TAG = "HomeFragment"
//    }
//}
