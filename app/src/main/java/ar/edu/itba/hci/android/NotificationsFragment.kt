package ar.edu.itba.hci.android

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import ar.edu.itba.hci.android.databinding.FragmentNotificationsBinding
import androidx.navigation.fragment.findNavController
import ar.edu.itba.hci.android.ui.profile.ProfileFragmentDirections
import java.util.*

class NotificationsFragment : Fragment(), CompoundButton.OnCheckedChangeListener {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)

        val sharedPref = requireActivity().getPreferences(Context.MODE_PRIVATE)

        createNotificationChannel()
        binding.saveNotif.setOnClickListener { scheduleNotification() }
        binding.switchNotif.setOnCheckedChangeListener(this)
        binding.switchNotif.isChecked = sharedPref.getBoolean(getString(R.string.notif_enabled_key), false)
        return binding.root
    }

    override fun onCheckedChanged(p0: CompoundButton?, isChecked: Boolean) {
        val sharedPref = activity?.getPreferences(Context.MODE_PRIVATE) ?: return
        if (!isChecked) {
            val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.cancel(createPendingIntent())

            with (sharedPref.edit()) {
                putBoolean(getString(R.string.notif_enabled_key), false)
                commit()
            }

            Toast.makeText(context, getString(R.string.notif_disable), Toast.LENGTH_SHORT).show()
        } else {
            with (sharedPref.edit()) {
                putBoolean(getString(R.string.notif_enabled_key), true)
                commit()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val backButton = binding.back

        backButton.setOnClickListener {
            // Get the possibles actions to translate through with this fragment
            val action = NotificationsFragmentDirections.actionNotificationsToNavigationProfile()
            findNavController().navigate(action)
        }
    }

    private fun createPendingIntent(): PendingIntent? {
        val intent = Intent(context, Notification::class.java)
        intent.putExtra(titleExtra, getString(R.string.notifTitle))
        intent.putExtra(messageExtra, getString(R.string.notifDesc))

        val pendingIntent = PendingIntent.getBroadcast(
            context,
            notificationID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        return pendingIntent
    }

    private fun scheduleNotification() {
        if (!binding.switchNotif.isChecked) {
            Toast.makeText(context, getString(R.string.notif_enable), Toast.LENGTH_SHORT).show()
            return
        }

        val alarmManager = context?.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val calendar = GregorianCalendar.getInstance().apply {
            if (get(Calendar.MINUTE) >= binding.timePicker.minute && get(Calendar.HOUR_OF_DAY) >= binding.timePicker.hour) {
                add(Calendar.DAY_OF_MONTH, 1)
            }

            set(Calendar.HOUR_OF_DAY, binding.timePicker.hour)
            set(Calendar.MINUTE, binding.timePicker.minute)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }

        alarmManager.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            createPendingIntent()
        )

        Toast.makeText(context, getString(R.string.reminder_set), Toast.LENGTH_SHORT).show()
    }

    private fun createNotificationChannel() {
        val name = "Reminder Channel"
        val desc = "Exercise reminders"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelID, name, importance)
        channel.description = desc
        val notificationManager = context?.getSystemService(AppCompatActivity.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

}