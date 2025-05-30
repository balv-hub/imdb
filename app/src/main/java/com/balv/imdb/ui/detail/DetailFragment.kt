package com.balv.imdb.ui.detail

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.balv.imdb.R
import com.balv.imdb.databinding.DetailFragmentBinding
import com.balv.imdb.domain.models.Movie
import com.bumptech.glide.Glide
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailFragment : Fragment() {
    private lateinit var mViewModel: DetailFragmentViewModel
    private var mBinding: DetailFragmentBinding? = null
    private var mAlertDialog: AlertDialog? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        mBinding = DetailFragmentBinding.inflate(inflater, container, false)
        val view: View = mBinding!!.root
        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mAlertDialog = MaterialAlertDialogBuilder(requireContext()).setTitle("Error").create()

        mViewModel = ViewModelProvider(this)[DetailFragmentViewModel::class.java]
        val movieId = arguments?.let { DetailFragmentArgs.fromBundle(it).getMovieId() }
        movieId?.let {
            lifecycleScope.launch { 
                mViewModel.getMovieDetail(it).collect{ movie -> bind(movie) }
            }
        }

        mViewModel.errorLiveData.observe(
            viewLifecycleOwner
        ) { apiResult ->
            if (!apiResult!!.isSuccess) {
                mAlertDialog!!.setMessage("Something happened, check API key or network condition")
                mAlertDialog!!.show()
            }
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val appBarLayout = activity?.findViewById<AppBarLayout>(R.id.appbar)
        appBarLayout?.setExpanded(false, false)
    }

    override fun onDestroy() {
        mAlertDialog!!.dismiss()
        mAlertDialog = null
        super.onDestroy()
    }

    private fun bind(movie: Movie?) {
        movie?.let { mv ->
            mBinding?.title?.text = mv.title
            mBinding?.year?.text = mv.released
            mBinding?.actors?.text = mv.actors
            mBinding?.director?.text = mv.director
            mBinding?.genre?.text = mv.genre
            mBinding?.time?.text = mv.time
            mBinding?.writer?.text = mv.writer
            mBinding?.imdbRating?.text = mv.imdbRated

            mBinding?.year?.text = mv.released
            mBinding?.plot?.text = mv.plot
            mBinding?.poster?.let {
                Glide.with(requireContext())
                    .load(mv.poster)
                    .centerCrop()
                    .placeholder(R.drawable.mv_place_holder)
                    .into(it)
            }
            
            updateLoadingIndicator(TextUtils.isEmpty(mv.imdbRated))
        } ?: run {
            updateLoadingIndicator(true)
        }
    }

    private fun updateLoadingIndicator(show: Boolean) {
        mBinding?.indeterminateBar?.visibility =
            if (show) View.VISIBLE else View.GONE
    }
}
