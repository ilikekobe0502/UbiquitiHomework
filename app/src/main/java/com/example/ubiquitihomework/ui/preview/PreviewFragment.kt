package com.example.ubiquitihomework.ui.preview

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.VERTICAL
import com.example.ubiquitihomework.MainActivity
import com.example.ubiquitihomework.R
import com.example.ubiquitihomework.databinding.FragmentPreviewBinding
import org.koin.android.ext.android.inject


class PreviewFragment : Fragment() {
    companion object {
        fun newInstance() = PreviewFragment()
    }

    private var _binding: FragmentPreviewBinding? = null
    private val binding get() = _binding!!

    private val viewModel: PreviewViewModel by inject()

    private val verticalAdapter = VerticalRecyclerViewAdapter()
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

    }
}