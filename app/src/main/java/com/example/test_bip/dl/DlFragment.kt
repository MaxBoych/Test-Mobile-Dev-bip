package com.example.test_bip.dl

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.ui.text.toUpperCase
import androidx.fragment.app.Fragment
import com.example.test_bip.MainActivity
import com.example.test_bip.R
import com.example.test_bip.data.CommonData
import com.example.test_bip.data.CommonData.Companion.dlKey
import com.example.test_bip.databinding.DlLayoutBinding
import com.example.test_bip.result.ResultFragment
import com.google.code.regexp.Pattern

class DlFragment: Fragment() { // Driver's license (ВУ)

    private lateinit var binding: DlLayoutBinding

    private val dlPattern: Regex = "^(${CommonData.digits}{2}((${CommonData.dlAllowedCyrillic}|${CommonData.allowedLatin}){2}|${CommonData.digits}{2})${CommonData.digits}{6})$".toRegex()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DlLayoutBinding.inflate(layoutInflater)
        setListeners()
        return binding.root
    }

    private fun setListeners() {
        binding.skipButton.setOnClickListener {
            (activity as MainActivity).showDialogWindow(ResultFragment())
        }

        binding.continueButton.setOnClickListener {
            validate()
        }
    }

    private fun validate() {
        var dl = binding.dlInput.text.toString().uppercase()

        if (dl.isEmpty()) {
            (activity as MainActivity).showToast(R.string.empty_input_error)
            return
        }

        if (isValid(dl)) {
            dl = (activity as MainActivity).checkOnLatinLetters(dl)
            MainActivity.appPreferences.edit().putString(dlKey, dl).apply()

            (activity as MainActivity).onAnotherScreen(ResultFragment())
        } else {
            (activity as MainActivity).showToast(R.string.invalid_input_error)
        }
    }

    private fun isValid(text: String): Boolean {
        val filteredText = text.replace("\\s".toRegex(), "")

        return (Pattern.compile(dlPattern.pattern).matcher(filteredText).find())
    }
}