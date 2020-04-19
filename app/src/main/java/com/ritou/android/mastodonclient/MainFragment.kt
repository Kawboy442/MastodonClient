package com.ritou.android.mastodonclient

import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import com.ritou.android.mastodonclient.databinding.FragmentMainBinding

class MainFragment: Fragment(R.layout.fragment_main) {

    private var binding: FragmentMainBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = DataBindingUtil.bind(view)
        binding?.button?.text = "Hello Fragment"
    }

    override fun onDestroyView() {
        super.onDestroyView()

        binding?.unbind()
    }
}