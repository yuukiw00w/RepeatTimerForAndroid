package jp.yn.light.repeattimer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import jp.yn.light.repeattimer.databinding.FragmentDisplayRemainingTimeBinding

class DisplayRemainingTimeFragment : Fragment() {
    private lateinit var binding: FragmentDisplayRemainingTimeBinding
    private val viewModel by lazy {
        val store = DisplayRemainingTimeFragmentStore()
        val vibratorAccessor = VibratorAccessor(context)
        val factory = DisplayRemainingTimeFragmentViewModelFactory(vibratorAccessor, store) { this }
        ViewModelProviders.of(this, factory).get(DisplayRemainingTimeFragmentViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.arcDrawParameter.observe(this, Observer {
            binding.arcView.draw(it)
        })
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_display_remaining_time,
            container,
            false
        )
        binding.lifecycleOwner = this
        binding.viewModel = viewModel
        return binding.root
    }
}