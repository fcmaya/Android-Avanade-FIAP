package br.com.equipequatro.traveltips.view.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBindings
import br.com.equipequatro.traveltips.R
import br.com.equipequatro.traveltips.adapter.FeedPostAdapter
import br.com.equipequatro.traveltips.databinding.FragmentFeedsBinding
import br.com.equipequatro.traveltips.repository.FeedPostItemRepository
import br.com.equipequatro.traveltips.viewmodel.FeedsViewModel

class FeedsFragment : Fragment() {

    private lateinit var rvFeedPosts: RecyclerView
    private lateinit var postAdapter: FeedPostAdapter
    private lateinit var dashboardViewModel: FeedsViewModel
    private var _binding: FragmentFeedsBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        dashboardViewModel =
            ViewModelProvider(this).get(FeedsViewModel::class.java)

        _binding = FragmentFeedsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        rvFeedPosts = binding.rvValoresFeed
        postAdapter = FeedPostAdapter(FeedPostItemRepository.getPostItem())
        rvFeedPosts.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        rvFeedPosts.adapter = postAdapter

        // val textView: TextView = binding.tvNomeCidadeFeed
        /* dashboardViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        }) */
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}