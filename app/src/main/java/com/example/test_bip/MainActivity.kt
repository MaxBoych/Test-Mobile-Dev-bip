package com.example.test_bip

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.test_bip.data.CommonData
import com.example.test_bip.data.CommonData.Companion.srpKey
import com.example.test_bip.databinding.MainActivityLayoutBinding
import com.example.test_bip.dl.DlFragment
import com.example.test_bip.result.ResultFragment
import com.example.test_bip.srp.SrpFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityLayoutBinding

    companion object {
        lateinit var appPreferences: SharedPreferences
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityLayoutBinding.inflate(layoutInflater)
        val view = binding.root
        setupSharedPreferences()
        setContentView(view)
        checkStatus()
    }

    private fun setupSharedPreferences() {
        val appPreferencesFileName = "TestBipAppSharedPreferences"
        appPreferences = getSharedPreferences(appPreferencesFileName, Context.MODE_PRIVATE)
    }

    private fun checkStatus() {
        val srp = appPreferences.getString(srpKey, null)
        val vrc = appPreferences.getString(CommonData.vrcKey, null)
        val dl = appPreferences.getString(CommonData.dlKey, null)

        if (srp.isNullOrEmpty() || vrc.isNullOrEmpty() || dl.isNullOrEmpty()) {
            onAnotherScreen(SrpFragment())
        } else {
            onAnotherScreen(ResultFragment())
        }
    }

    fun showToast(message: Int) {
        runOnUiThread {
            Toast.makeText(
                this,
                resources.getString(message),
                Toast.LENGTH_LONG
            ).show()
        }
    }

    fun showDialogWindow(fragment: Fragment) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(R.string.skip_this_step_title)
        builder.setNegativeButton(R.string.stay_button) { _, _ -> }
        builder.setPositiveButton(R.string.skip_button) { _, _ ->
            if (fragment is DlFragment) {
                appPreferences.edit().remove(srpKey).apply()
            }
            onAnotherScreen(fragment)
        }
        builder.setCancelable(true)

        Handler(Looper.getMainLooper()).post {
            val dialog: AlertDialog = builder.create()
            dialog.show()
        }
    }

    fun onAnotherScreen(fragment: Fragment) {
        supportFragmentManager
            .beginTransaction()
            //.replace(R.id.main)
            .replace(R.id.main_layout, fragment, null)
            .commit()
    }

    fun checkOnLatinLetters(text: String): String {
        var filteredText = text
        for (letter in filteredText) {
            if (CommonData.lettersMap.containsKey(letter)) {
                filteredText = filteredText.replace(letter, CommonData.lettersMap[letter]!!)
            }
        }
        return filteredText
    }
}
