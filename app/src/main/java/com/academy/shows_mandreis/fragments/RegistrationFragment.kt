package com.academy.shows_mandreis.fragments

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.widget.doAfterTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.academy.shows_mandreis.databinding.FragmentRegistrationBinding
import com.academy.shows_mandreis.view_models.RegistrationViewModel

class RegistrationFragment : Fragment() {

    private var _binding: FragmentRegistrationBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RegistrationViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRegistrationBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getRegistrationResultLiveData().observe(this.viewLifecycleOwner) { isRegisterSuccessful  ->
            if (isRegisterSuccessful) {
                Toast.makeText(context, "Registered successfully", Toast.LENGTH_SHORT).show()
                navigateToLoginFragment()
            } else {
                Toast.makeText(context, "Email has already been taken", Toast.LENGTH_SHORT).show()
            }
        }

        initRegisterButton()
        initPasswordTextField()
        initEmailTextField()
        initRepeatPasswordTextField()
    }

    private fun initRepeatPasswordTextField() {
        binding.repeatPasswordInput.editText?.doAfterTextChanged {
            updateRegisterButtonEnabledStatus()
            binding.passwordInput.error = null
            binding.repeatPasswordInput.error = null
        }
    }

    private fun navigateToLoginFragment() {
        val action = RegistrationFragmentDirections.registerToLogin("yes")
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRegisterButton() {
        binding.registerButton.setOnClickListener {
            val email = binding.emailInput.editText?.text.toString()
            val validEmail = isEmailValid(email)
            val samePasswords = checkPasswords()
            if (validEmail) {
                if (samePasswords) {
                    binding.apply {
                        val password = passwordInput.editText?.text.toString()
                        viewModel.register(email, password)
                    }
                } else {
                    binding.passwordInput.error = "Passwords must be the same."
                    binding.repeatPasswordInput.error = "Passwords must be the same."
                }
            } else {
                binding.emailInput.error = "Invalid email address."
            }
        }
    }

    private fun checkPasswords(): Boolean {
        val password = binding.passwordInput.editText?.text.toString()
        val repeatedPassword = binding.repeatPasswordInput.editText?.text.toString()
        return password == repeatedPassword
    }


    private fun initPasswordTextField() {
        binding.passwordInput.editText?.doAfterTextChanged {
            updateRegisterButtonEnabledStatus()
            binding.passwordInput.error = null
            binding.repeatPasswordInput.error = null
        }
    }

    private fun initEmailTextField() {
        binding.emailInput.editText?.doAfterTextChanged {
            binding.emailInput.error = null
            updateRegisterButtonEnabledStatus()
        }
    }

    private fun updateRegisterButtonEnabledStatus() {
        val emailCondition = binding.emailInput.editText?.text.toString().isNotEmpty()
        val passwordCondition =  binding.passwordInput.editText?.text.toString().length > 5
        val repeatPasswordCondition = binding.repeatPasswordInput.editText?.text.toString().length > 5
        binding.registerButton.isEnabled = emailCondition && passwordCondition && repeatPasswordCondition
    }

    private fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}