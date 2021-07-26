package com.academy.shows_mandreis.fragments

import android.content.Context
import com.academy.shows_mandreis.databinding.FragmentLoginBinding

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.academy.shows_mandreis.ui.ShowsActivity
import com.academy.shows_mandreis.view_models.LoginViewModel
import com.academy.shows_mandreis.view_models.RegistrationViewModel

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private val viewModel: LoginViewModel by viewModels()

    val args: LoginFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getLoginResultLiveData().observe(this.viewLifecycleOwner) { isLoginSuccessful  ->
            if (isLoginSuccessful) {
                Toast.makeText(context, "Login successfully", Toast.LENGTH_SHORT).show()
                saveEmailAddress(binding.emailInput.editText?.text.toString())
                updateRememberMeStatus(binding.rememberBeCheckBox.isChecked)
                navigateToShowsFragment()
            } else {
                Toast.makeText(context, "Email or password are incorrect", Toast.LENGTH_SHORT).show()
            }
        }

        val prefs = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        val alreadySeenShows = prefs.getBoolean("alreadyOnShows", false)
        if (alreadySeenShows) {
            navigateToShowsFragment()
        }

        initScreen()

        initLoginButton()
        initPasswordTextField()
        initEmailTextField()
    }

    private fun initScreen() {
        if (args.success == "yes") {
            binding.loginText.text = "Registration successful!"
            binding.registerButton.visibility = View.GONE
        }
    }

    private fun navigateToShowsFragment() {
        val action = LoginFragmentDirections.loginToShows()
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initLoginButton() {
        binding.loginButton.setOnClickListener {
            val email = binding.emailInput.editText?.text.toString()
            val valid = isEmailValid(email)
            if (valid) {
                val password = binding.passwordInput.editText?.text.toString()
                viewModel.login(email, password)
            } else {
                binding.emailInput.error = "Invalid email address format."
            }
        }
    }

    private fun saveEmailAddress(email: String) {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putString("emailAddress", email)
            apply()
        }
    }

    private fun updateRememberMeStatus(status: Boolean) {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        with (sharedPref.edit()) {
            putBoolean("rememberMeStatus", status)
            apply()
        }
    }

    private fun initPasswordTextField() {
        binding.passwordInput.editText?.doAfterTextChanged {
            updateLoginButtonEnabledStatus()
        }
    }

    private fun initEmailTextField() {
        binding.emailInput.editText?.doAfterTextChanged {
            binding.emailInput.error = null
            updateLoginButtonEnabledStatus()
        }
    }

    private fun updateLoginButtonEnabledStatus() {
        val emailCondition = binding.emailInput.editText?.text.toString().isNotEmpty()
        val passwordCondition =  binding.passwordInput.editText?.text.toString().length > 5
        binding.loginButton.isEnabled = emailCondition && passwordCondition
    }

    private fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}