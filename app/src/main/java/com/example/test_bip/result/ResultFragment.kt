package com.example.test_bip.result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.test_bip.MainActivity
import com.example.test_bip.R
import com.example.test_bip.data.CommonData
import com.example.test_bip.data.CommonData.Companion.dlKey
import com.example.test_bip.data.CommonData.Companion.srpKey
import com.example.test_bip.data.CommonData.Companion.srpRegionKey
import com.example.test_bip.data.CommonData.Companion.vrcKey
import com.example.test_bip.databinding.ResultLayoutBinding

class ResultFragment: Fragment() {

    private lateinit var binding: ResultLayoutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = ResultLayoutBinding.inflate(layoutInflater)
        setContent()
        return binding.root
    }

    private fun setContent() {
        val srp = MainActivity.appPreferences.getString(srpKey, null)
        val srpRegion = MainActivity.appPreferences.getString(srpRegionKey, null)
        val vrc = MainActivity.appPreferences.getString(vrcKey, null)
        val dl = MainActivity.appPreferences.getString(dlKey, null)

        if (!srp.isNullOrEmpty() && !srpRegion.isNullOrEmpty()) {
            binding.srpResult.text = "ГРЗ: $srp $srpRegion"
            binding.srpRegionResult.text = CommonData.regionCodes[srpRegion]
        } else {
            binding.srpResult.text = "ГРЗ: " + context?.resources?.getString(R.string.not_filled_in)
        }

        binding.vrcResult.text = "СТС: " + (vrc?: context?.resources?.getString(R.string.not_filled_in))
        binding.dlResult.text = "ВУ: " + (dl?: context?.resources?.getString(R.string.not_filled_in))
    }
}