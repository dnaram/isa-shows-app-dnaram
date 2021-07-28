package com.academy.shows_mandreis.fragments
import android.annotation.SuppressLint
import android.app.Activity
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.provider.Settings
import android.util.Log
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.academy.shows_mandreis.R
import com.academy.shows_mandreis.databinding.DialogUserSettingsBinding
import com.academy.shows_mandreis.databinding.FragmentShowsBinding
import com.academy.shows_mandreis.model.Show
import com.academy.shows_mandreis.permissions.preparePermissionsContract
import com.academy.shows_mandreis.ui.ShowsAdapter
import com.academy.shows_mandreis.utility.FileUtil
import com.academy.shows_mandreis.view_models.ShowsViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.google.android.material.bottomsheet.BottomSheetDialog

class ShowsFragment : Fragment() {

    private var _binding: FragmentShowsBinding? = null

    private val binding get() = _binding!!
    private var adapter: ShowsAdapter? = null
    private var showsVisible = true

    private val REQUEST_CODE = 200

    private val viewModel: ShowsViewModel by viewModels()

    private val cameraPermissionForTakingPhoto = preparePermissionsContract(onPermissionsGranted = {
        try {
            capturePhoto()
        } catch (e: ActivityNotFoundException) {
            // display error state to the user
        }
    })

    private fun refreshProfilePhotos() {
        val file = FileUtil.getImageFile(view?.context)
        Log.d("EXISTS", file?.exists().toString())
        if (file != null) {
            Glide.with(this)
                .load(file.toUri())
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .skipMemoryCache(true)
                .into(binding.userIcon)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE && data != null){
            view?.let { FileUtil.createImageFile(it.context) }
            Log.d("WRITE PERMISSSION", Settings.System.canWrite(view?.context).toString())
            refreshProfilePhotos()
        }
    }

    private fun capturePhoto() {
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, REQUEST_CODE)
    }

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

       setAlreadyOnShowsStatus()

       initRecyclerView()
       initUserIcon()

       binding.testButton.setOnClickListener {
           switchScreenShowsState()
       }

       viewModel.initShows()
       viewModel.getShowsLiveData().observe(viewLifecycleOwner, { shows ->
           updateItems(shows)
       })

   }

    private fun updateItems(shows: List<Show>) {
        adapter?.setItems(shows)
    }

    private fun navigateToLoginScreen() {
        val action = ShowsFragmentDirections.showsToLogin()
        findNavController().navigate(action)
    }

    private fun setAlreadyOnShowsStatus() {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        val rememberMeStatus = sharedPref.getBoolean("rememberMeStatus", false)
        with (sharedPref.edit()) {
            putBoolean("alreadyOnShows", rememberMeStatus)
            apply()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initUserIcon() {
        binding.userIcon.setOnClickListener {
            showBottomSheet()
        }
    }

    private fun showBottomSheet() {
        val dialog = view?.let { BottomSheetDialog(it.context) }

        val dialogBinding = DialogUserSettingsBinding.inflate(layoutInflater)
        dialog?.setContentView(dialogBinding.root)

        dialogBinding.logoutButton.setOnClickListener {
            showAlertDialog()
            dialog?.dismiss()
        }

        dialogBinding.changeProfileButton.setOnClickListener {
            // TODO: 24. 07. 2021. implement profile photo changing functionality
            cameraPermissionForTakingPhoto.launch(arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE, android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.CAMERA))
        }

        dialog?.show()
    }

    private fun showAlertDialog() {
        val builder = view?.let { AlertDialog.Builder(it.context) }
        builder?.setTitle("Logout")
        builder?.setMessage("Are you sure you want to logout?")

        builder?.setPositiveButton(android.R.string.yes) { dialog, which ->
            val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE)
            with (sharedPref?.edit()) {
                this?.putBoolean("rememberMeStatus", false)
                this?.putBoolean("alreadyOnShows", false)
                this?.apply()
            }
            navigateToLoginScreen()
        }

        builder?.setNegativeButton(android.R.string.no) { dialog, which ->
        }

        builder?.show()
    }

    private fun switchScreenShowsState() {
        showsVisible = !showsVisible
        if (showsVisible) {
            binding.showsRecycler.visibility = View.VISIBLE
            binding.noShowsImage.visibility = View.GONE
            binding.noShowsText.visibility = View.GONE
            binding.testButton.text = resources.getString(R.string.clear_shows)
        } else {
            binding.showsRecycler.visibility = View.GONE
            binding.noShowsImage.visibility = View.VISIBLE
            binding.noShowsText.visibility = View.VISIBLE
            binding.testButton.text = resources.getString(R.string.show_shows)
        }
    }

    private fun initRecyclerView() {
        binding.showsRecycler.layoutManager = LinearLayoutManager(view?.context, LinearLayoutManager.VERTICAL, false)

        adapter = ShowsAdapter(emptyList()) { id, name, desc, pic ->
            val action = ShowsFragmentDirections.showsToShowDetails(id, name, desc, pic)
            findNavController().navigate(action)
        }
        binding.showsRecycler.adapter = adapter
    }

}