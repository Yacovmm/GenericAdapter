package br.com.enicolas.adapterdelegate.fragments.first

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import br.com.enicolas.adapterdelegate.R
import br.com.enicolas.adapterdelegate.cells.FirstCell
import br.com.enicolas.adapterdelegate.cells.SecondCell
import br.com.enicolas.adapterdelegate.databinding.CellFirstBinding
import br.com.enicolas.adapterdelegate.databinding.CellSecondBinding
import br.com.enicolas.adapterdelegate.databinding.FragmentFirstBinding
import br.com.enicolas.genericadapter.AdapterHolderType
import br.com.enicolas.genericadapter.adapter.GenericRecyclerAdapter
import br.com.enicolas.genericadapter.adapter.GenericRecylerAdapterDelegate
import br.com.enicolas.genericadapter.adapter.Snapshot

class FirstFragment : Fragment() {

    private var _binding: FragmentFirstBinding? = null
    private val binding: FragmentFirstBinding
    get() {
        return _binding!!
    }

    private val viewModel: FirstViewModel by viewModels()

    private val adapter = GenericRecyclerAdapter(snapshot = Snapshot())

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFirstBinding.inflate(inflater)
        return _binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupSearchView()
    }

    /**
     * Setup recycler view
     */
    private fun setupRecyclerView() {
        adapter.delegate = recyclerDelegate
        binding.recyclerView.adapter = adapter
        adapter.snapshot?.updateSnapshot(viewModel.list)
    }

    /**
     * Setup SearchView
     */
    private fun setupSearchView() {
        binding.searchView.setOnQueryTextListener(searchViewListener)
    }

    /**
     * RecyclerView Delegate
     */
    private val recyclerDelegate = object : GenericRecylerAdapterDelegate {

        override fun cellForPosition(adapter: GenericRecyclerAdapter, cell: RecyclerView.ViewHolder, position: Int) {
            val text = getString(R.string.itemString, viewModel.list[position].toString())
            (cell as? FirstCell)?.binding?.textView?.text = text
            (cell as? SecondCell)?.binding?.textView?.text = text
        }

        override fun registerCellAtPosition(adapter: GenericRecyclerAdapter, position: Int): AdapterHolderType {
            return if(position % 2 == 0) {
                AdapterHolderType(
                    viewBinding = CellFirstBinding::class.java,
                    clazz =  FirstCell::class.java,
                    reuseIdentifier = 0
                )
            } else {
                AdapterHolderType(
                    viewBinding = CellSecondBinding::class.java,
                    clazz =  SecondCell::class.java,
                    reuseIdentifier = 1
                )
            }
        }

        override fun didSelectItemAtIndex(adapter: GenericRecyclerAdapter, index: Int) {

        }

        override fun numberOfRows(adapter: GenericRecyclerAdapter): Int {
            return viewModel.list.size
        }

        override fun registerHeaderFor(adapter: GenericRecyclerAdapter): AdapterHolderType? {
            return null
        }

        override fun viewForHeaderAt(position: Int, cell: RecyclerView.ViewHolder, adapter: GenericRecyclerAdapter) {

        }
    }

    /**
     * SearchView Listener
     */
    private val searchViewListener = object : SearchView.OnQueryTextListener {
        override fun onQueryTextSubmit(query: String?): Boolean {
            return true
        }

        override fun onQueryTextChange(newText: String?): Boolean {
            var list = viewModel.originalList.filter {
                it.toString().contains(newText ?: "")
            }.toMutableList()
            if(newText.isNullOrBlank()) { list = viewModel.originalList }
            viewModel.list = list
            adapter.snapshot?.updateSnapshot(viewModel.list)
            return true
        }
    }
}