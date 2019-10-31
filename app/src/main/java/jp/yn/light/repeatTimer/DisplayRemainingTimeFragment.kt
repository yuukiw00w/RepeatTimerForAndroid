package jp.yn.light.repeatTimer

import android.app.AlarmManager
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import jp.yn.light.repeatTimer.databinding.FragmentDisplayRemainingTimeBinding

class DisplayRemainingTimeFragment : Fragment(),
    DisplayRemainingTimeFragmentActionCreator.Listener {
    private lateinit var binding: FragmentDisplayRemainingTimeBinding
    private val viewModel by lazy {
        val store = DisplayRemainingTimeFragmentStore()
        val factory = DisplayRemainingTimeFragmentViewModelFactory(this, store) { this }
        ViewModelProviders.of(this, factory).get(DisplayRemainingTimeFragmentViewModel::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel.onCreate(savedInstanceState)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        viewModel.attach(context)
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
        val hourPicker = binding.hourPicker
        val minutePicker = binding.minutePicker
        val secondPicker = binding.secondPicker
        hourPicker.maxValue = 99
        hourPicker.minValue = 0
        hourPicker.setOnValueChangedListener { _, _, hour ->
            viewModel.changeTime(hour, minutePicker.value, secondPicker.value)
        }

        minutePicker.maxValue = 59
        minutePicker.minValue = 0
        minutePicker.setOnValueChangedListener { _, _, minute ->
            viewModel.changeTime(hourPicker.value, minute, secondPicker.value)
        }

        secondPicker.maxValue = 59
        secondPicker.minValue = 0
        secondPicker.setOnValueChangedListener { _, _, second ->
            viewModel.changeTime(hourPicker.value, minutePicker.value, second)
        }
        viewModel.arcDrawParameter.observe(this, Observer {
            binding.arcView.draw(it)
        })
        return binding.root
    }

    override fun generateAlarm() {
        val context = context ?: return
        AlarmService.startService(context)
    }

    override fun cancelAlarm() {
        val context = context ?: return
        AlarmService.stopService(context)
    }

    override fun onStart() {
        super.onStart()
        viewModel.onStart()
    }

    override fun onStop() {
        super.onStop()
        viewModel.onStop()
    }

    override fun setAlarmClock(nextAlarmTime: Long) {
        val activity = activity
        check(activity != null)
        val alarmManager = activity.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        check(alarmManager is AlarmManager)
        val pendingIntent = AlarmBroadcastReceiver.makePendingIntent(activity)
        alarmManager.setAlarmClock(AlarmManager.AlarmClockInfo(nextAlarmTime, null), pendingIntent)
    }

    override fun resetAlarmClock() {
        val activity = activity
        check(activity != null)
        val alarmManager = activity.getSystemService(Context.ALARM_SERVICE) as? AlarmManager
        check(alarmManager is AlarmManager)
        val pendingIntent = AlarmBroadcastReceiver.makePendingIntent(activity)
        pendingIntent.cancel()
        alarmManager.cancel(pendingIntent)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        viewModel.save(outState)
    }
}