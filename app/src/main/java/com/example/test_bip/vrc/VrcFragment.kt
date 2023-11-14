package com.example.test_bip.vrc

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.test_bip.MainActivity
import com.example.test_bip.R
import com.example.test_bip.data.CommonData
import com.example.test_bip.data.CommonData.Companion.lettersMap
import com.example.test_bip.data.CommonData.Companion.vrcKey
import com.example.test_bip.databinding.VrcLayoutBinding
import com.example.test_bip.dl.DlFragment
import com.google.code.regexp.Pattern

class VrcFragment: Fragment() { // Vehicle registration certificate (СТС)

    private lateinit var binding: VrcLayoutBinding

    private val vrcPattern: Regex = "^(${CommonData.digits}{2}((${CommonData.vrcAllowedCyrillic}|${CommonData.allowedLatin}){2}|${CommonData.digits}{2})${CommonData.digits}{6})$".toRegex()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = VrcLayoutBinding.inflate(layoutInflater)
        setListeners()
        return binding.root
    }

    private fun setListeners() {
        binding.skipButton.setOnClickListener {
            (activity as MainActivity).showDialogWindow(DlFragment())
        }

        binding.continueButton.setOnClickListener {
            validate()
        }
    }

    private fun validate() {
        var vrc = binding.vrcInput.text.toString().uppercase()

        if (vrc.isEmpty()) {
            (activity as MainActivity).showToast(R.string.empty_input_error)
            return
        }

        if (isValid(vrc)) {
            vrc = (activity as MainActivity).checkOnLatinLetters(vrc)
            MainActivity.appPreferences.edit().putString(vrcKey, vrc).apply()

            (activity as MainActivity).onAnotherScreen(DlFragment())
        } else {
            (activity as MainActivity).showToast(R.string.invalid_input_error)
        }
    }

    private fun isValid(text: String): Boolean {
        val filteredText = text.replace("\\s".toRegex(), "")

        return (Pattern.compile(vrcPattern.pattern).matcher(filteredText).find())
    }
}