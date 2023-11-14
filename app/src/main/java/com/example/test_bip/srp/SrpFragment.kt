package com.example.test_bip.srp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.test_bip.MainActivity
import com.example.test_bip.R
import com.example.test_bip.data.CommonData
import com.example.test_bip.data.CommonData.Companion.srpKey
import com.example.test_bip.data.CommonData.Companion.srpRegionKey
import com.example.test_bip.databinding.SrpLayoutBinding
import com.example.test_bip.dl.DlFragment
import com.example.test_bip.vrc.VrcFragment
import com.google.code.regexp.Pattern

class SrpFragment: Fragment() { // State registration plate (ГРЗ)

    private lateinit var binding: SrpLayoutBinding

    private val group1Type1A: Regex = "^((${CommonData.srpAllowedCyrillic}|${CommonData.allowedLatin})${CommonData.digits}{3}(${CommonData.srpAllowedCyrillic}c|${CommonData.allowedLatin}){2})$".toRegex()
    private val group1Type1B: Regex = "^((${CommonData.srpAllowedCyrillic}|${CommonData.allowedLatin}){2}${CommonData.digits}{3})$".toRegex()
    private val group1Type2and3and4: Regex = "^((${CommonData.srpAllowedCyrillic}|${CommonData.allowedLatin}){2}${CommonData.digits}{4})$".toRegex()
    private val group2Type5and7and8: Regex = "^(${CommonData.digits}{4}(${CommonData.srpAllowedCyrillic}|${CommonData.allowedLatin}){2})$".toRegex()
    private val group2Type6: Regex = "^((${CommonData.srpAllowedCyrillic}|${CommonData.allowedLatin}){2}${CommonData.digits}{4})$".toRegex()
    private val group5Type20and22: Regex = "^((${CommonData.srpAllowedCyrillic}|${CommonData.allowedLatin})${CommonData.digits}{4})$".toRegex()
    private val group5Type21: Regex = "^(${CommonData.digits}{3}(${CommonData.srpAllowedCyrillic}|${CommonData.allowedLatin}))$".toRegex()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = SrpLayoutBinding.inflate(layoutInflater)
        setListeners()
        return binding.root
    }

    private fun setListeners() {
        binding.skipButton.setOnClickListener {
            val dlFragment = DlFragment()
            (activity as MainActivity).showDialogWindow(dlFragment)
        }

        binding.continueButton.setOnClickListener {
            validate()
        }
    }

    private fun validate() {
        var number = binding.srpNumberInput.text.toString().uppercase()
        val region = binding.srpRegionInput.text.toString()

        if (number.isEmpty() || region.isEmpty()) {
            (activity as MainActivity).showToast(R.string.empty_input_error)
            return
        }

        if (isValid(number) && CommonData.regionCodes.containsKey(region)) {
            number = (activity as MainActivity).checkOnLatinLetters(number)
            MainActivity.appPreferences.edit().putString(srpKey, number).apply()
            MainActivity.appPreferences.edit().putString(srpRegionKey, region).apply()

            (activity as MainActivity).onAnotherScreen(VrcFragment())
        } else {
            (activity as MainActivity).showToast(R.string.invalid_input_error)
        }
    }

    private fun isValid(text: String): Boolean {
        val filteredText = text.replace("\\s".toRegex(), "")

        if (Pattern.compile(group1Type1A.pattern).matcher(filteredText).find()) return true

        if (Pattern.compile(group1Type1B.pattern).matcher(filteredText).find()) return true

        if (Pattern.compile(group1Type2and3and4.pattern).matcher(filteredText).find()) return true

        if (Pattern.compile(group2Type5and7and8.pattern).matcher(filteredText).find()) return true

        if (Pattern.compile(group2Type6.pattern).matcher(filteredText).find()) return true

        if (Pattern.compile(group5Type20and22.pattern).matcher(filteredText).find()) return true

        if (Pattern.compile(group5Type21.pattern).matcher(filteredText).find()) return true

        return false
    }
}