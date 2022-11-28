package com.example.ubiquitihomework.ui.preview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.example.ubiquitihomework.MainActivity
import com.example.ubiquitihomework.R
import com.example.ubiquitihomework.databinding.FragmentPreviewBinding
import com.example.ubiquitihomework.listener.AdapterInteractionListener
import com.example.ubiquitihomework.model.api.ApiResult
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject


class PreviewFragment : Fragment() {
    companion object {
        fun newInstance() = PreviewFragment()
        const val PM25_LIMIT: Int = 20
    }

    private var _binding: FragmentPreviewBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PreviewViewModel by inject()
    private val verticalAdapterInteractionListener: AdapterInteractionListener =
        object : AdapterInteractionListener {
            override fun onClick(s: String) {
                Snackbar.make(
                    binding.root,
                    getString(R.string.preview_page_status_bad_toast_message, s),
                    Snackbar.LENGTH_SHORT
                ).show()
            }
        }
    private val verticalAdapter = VerticalRecyclerViewAdapter(verticalAdapterInteractionListener)
    private val horizontalAdapter = HorizontalRecyclerViewAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPreviewBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getAirStatus()
        initView()
        initObserve()
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun initView() {
        (requireActivity() as MainActivity?)?.supportActionBar?.setTitle(R.string.preview_page_title)
        val decoration = DividerItemDecoration(requireContext(), VERTICAL)
        binding.rvContent.apply {
            addItemDecoration(decoration)
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
            adapter = verticalAdapter
        }

        binding.rvTopContent.apply {
            layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
            adapter = horizontalAdapter
        }
    }

    private fun initObserve() {
        viewLifecycleOwner.lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.airStatusResult.observe(viewLifecycleOwner) { response ->
                    when (response) {
                        is ApiResult.Error -> {
                            showLoading(false)
                            showEmptyView(true)
                        }
                        is ApiResult.Loading -> {
                            showLoading(true)
                        }
                        is ApiResult.Success -> {
                            showLoading(false)
                            response.result?.let { list ->
                                if (list.isEmpty()) {
                                    showEmptyView(true)
                                } else {
                                    showEmptyView(false)

                                    val horizontalList =
                                        list.filter { (it.pm2_5.isNotEmpty() && it.pm2_5.toInt() <= PM25_LIMIT) }
                                    val verticalList =
                                        list.filter { (it.pm2_5.isNotEmpty() && it.pm2_5.toInt() > PM25_LIMIT) }
                                    horizontalAdapter.setData(horizontalList)
                                    verticalAdapter.setData(verticalList)
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun showLoading(show: Boolean) {
        binding.pbLoading.isVisible = show
    }

    private fun showEmptyView(show: Boolean) {
        binding.tvEmpty.isVisible = show
    }
}