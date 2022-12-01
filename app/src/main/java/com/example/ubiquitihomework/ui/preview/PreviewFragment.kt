package com.example.ubiquitihomework.ui.preview

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ubiquitihomework.R
import com.example.ubiquitihomework.databinding.FragmentPreviewBinding
import com.example.ubiquitihomework.listener.AdapterInteractionListener
import com.example.ubiquitihomework.model.api.ApiResult
import com.example.ubiquitihomework.ui.MainActivity
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject


class PreviewFragment : Fragment() {
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
    private val expandListener = object : MenuItem.OnActionExpandListener {
        override fun onMenuItemActionCollapse(item: MenuItem): Boolean {
            binding.rvTopContent.isVisible = true
            val originalList = (viewModel.airStatusResult.value as ApiResult.Success).result
            originalList?.let { verticalAdapter.setData(it) }
            showEmptyView(false)
            return true
        }

        override fun onMenuItemActionExpand(item: MenuItem): Boolean {
            binding.rvTopContent.isVisible = false
            viewModel.getSearchAirStatus("")
            return true
        }
    }
    private lateinit var searchView: SearchView
    private val menuProvider = object : MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            searchView = menu.findItem(R.id.search).actionView as SearchView
            menu.findItem(R.id.search).setOnActionExpandListener(expandListener)
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            if (menuItem.actionView is SearchView) {
                val searchView = (menuItem.actionView as SearchView)
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?): Boolean {
                        return false
                    }

                    override fun onQueryTextChange(newText: String?): Boolean {
                        if (!binding.rvTopContent.isVisible) {
                            newText?.let { viewModel.getSearchAirStatus(it) }
                        }
                        return true
                    }

                })
            }
            return true
        }

    }

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
        initToolbar()
        initRecyclerView()
    }

    private fun initToolbar() {
        (requireActivity() as MainActivity?)?.supportActionBar?.setTitle(R.string.preview_page_title)
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(menuProvider)
    }

    private fun initRecyclerView() {
        val decoration = DividerItemDecoration(requireContext(), RecyclerView.VERTICAL)
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
                                    var pm25Avg=0
                                    list.forEach {
                                        if (it.pm25 != "") {
                                            pm25Avg += it.pm25.toInt()
                                        }
                                    }
                                    pm25Avg /= list.size
                                    val horizontalList =
                                        list.filter { (it.pm25.isNotEmpty() && it.pm25.toInt() <= pm25Avg) }
                                    val verticalList =
                                        list.filter { (it.pm25.isNotEmpty() && it.pm25.toInt() > pm25Avg) }
                                    horizontalAdapter.setData(horizontalList)
                                    verticalAdapter.setData(verticalList)
                                }
                            }
                        }
                    }
                }
                viewModel.searchStatusResult.observe(viewLifecycleOwner) { list ->
                    verticalAdapter.setData(list)
                    when {
                        list.isNotEmpty() -> {
                            showEmptyView(false)
                        }
                        searchView.query.isNotEmpty() -> {
                            showEmptyView(
                                true,
                                getString(
                                    R.string.preview_page_search_empty_message,
                                    searchView.query.toString()
                                )
                            )
                        }
                        searchView.query.isEmpty() -> {
                            showEmptyView(
                                true,
                                getString(R.string.preview_page_search_message)
                            )
                        }
                    }
                }
            }
        }
    }

    private fun showLoading(show: Boolean) {
        binding.pbLoading.isVisible = show
    }

    private fun showEmptyView(
        show: Boolean,
        message: String = getString(R.string.preview_page_empty)
    ) {
        binding.tvEmpty.isVisible = show
        binding.tvEmpty.text = message
    }
}