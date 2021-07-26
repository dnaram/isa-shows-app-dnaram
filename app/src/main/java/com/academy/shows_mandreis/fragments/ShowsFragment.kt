package com.academy.shows_mandreis.fragments
import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.academy.shows_mandreis.R
import com.academy.shows_mandreis.databinding.FragmentShowsBinding
import com.academy.shows_mandreis.ui.ShowDetailsActivity
import com.academy.shows_mandreis.ui.ShowsAdapter
import com.academy.shows_mandreis.utility.MockDatabase

class ShowsFragment : Fragment() {

    private var _binding: FragmentShowsBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    private var adapter: ShowsAdapter? = null
    private var showsVisible = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentShowsBinding.inflate(inflater, container, false)
        return binding.root
    }

   override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

       initRecyclerView()
       initLogoutIcon()

       binding.testButton.setOnClickListener {
           switchScreenShowsState()
       }

   }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initLogoutIcon() {
        binding.logoutIcon.setOnClickListener {
            val action = ShowsFragmentDirections.showsToLogin()
            findNavController().navigate(action)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun switchScreenShowsState() {
        showsVisible = !showsVisible
        if (showsVisible) {
            binding.showsRecycler.visibility = View.VISIBLE
            binding.noShowsImage.visibility = View.GONE
            binding.noShowsText.visibility = View.GONE
            binding.testButton.text = "Clear shows!"
        } else {
            binding.showsRecycler.visibility = View.GONE
            binding.noShowsImage.visibility = View.VISIBLE
            binding.noShowsText.visibility = View.VISIBLE
            binding.testButton.text = "Show shows! (pun intended)"
        }
    }

    private fun initRecyclerView() {
        binding.showsRecycler.layoutManager = LinearLayoutManager(view?.context, LinearLayoutManager.VERTICAL, false)

        adapter = ShowsAdapter(MockDatabase.getShows()) { id, name, desc, pic ->
            val action = ShowsFragmentDirections.showsToShowDetails(id, name, desc, pic)
            findNavController().navigate(action)
        }
        binding.showsRecycler.adapter = adapter
    }

}