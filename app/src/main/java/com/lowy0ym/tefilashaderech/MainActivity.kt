package com.lowy0ym.tefilashaderech

import android.Manifest
import android.app.Activity
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.TextView
import android.graphics.Typeface
import android.graphics.Color

class MainActivity : Activity() {
    private val channelId = "tefilas_haderech_reminders"
    private val prayer = "יְהִי רָצוֹן מִלְּפָנֶיךָ יְיָ אֱלֹהֵינוּ וֵאלֹהֵי אֲבוֹתֵינוּ, שֶׁתּוֹלִיכֵנוּ לְשָׁלוֹם וְתַצְעִידֵנוּ לְשָׁלוֹם וְתַדְרִיכֵנוּ לְשָׁלוֹם, וְתַגִּיעֵנוּ לִמְחוֹז חֶפְצֵנוּ לְחַיִּים וּלְשִׂמְחָה וּלְשָׁלוֹם. וְתַצִּילֵנוּ מִכַּף כָּל אוֹיֵב וְאוֹרֵב וְלִסְטִים וְחַיּוֹת רָעוֹת בַּדֶּרֶךְ, וּמִכָּל מִינֵי פֻּרְעָנִיּוֹת הַמִּתְרַגְּשׁוֹת לָבוֹא לָעוֹלָם. וְתִשְׁלַח בְּרָכָה בְּכָל מַעֲשֵׂה יָדֵינוּ, וְתִתְּנֵנוּ לְחֵן וּלְחֶסֶד וּלְרַחֲמִים בְּעֵינֶיךָ וּבְעֵינֵי כָל רוֹאֵינוּ, וְתִגְמְלֵנוּ חֲסָדִים טוֹבִים. בָּרוּךְ אַתָּה יְיָ, שׁוֹמֵעַ תְּפִלָּה."

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        createChannel()
        val root = LinearLayout(this).apply { orientation = LinearLayout.VERTICAL; setPadding(24, 28, 24, 24); setBackgroundColor(Color.rgb(250,248,243)) }
        val title = TextView(this).apply { text = "תפילת הדרך"; textSize = 30f; gravity = Gravity.CENTER; setTextColor(Color.rgb(35,55,48)); typeface = Typeface.DEFAULT_BOLD }
        val subtitle = TextView(this).apply { text = "נוסח אשכנז"; textSize = 16f; gravity = Gravity.CENTER; setPadding(0,8,0,18); setTextColor(Color.DKGRAY) }
        val prayerView = TextView(this).apply { text = prayer; textSize = 27f; gravity = Gravity.CENTER; textDirection = TextView.TEXT_DIRECTION_RTL; setTextColor(Color.rgb(30,30,30)); setPadding(8,12,8,20); setLineSpacing(12f,1.12f) }
        val scroll = ScrollView(this).apply { addView(prayerView) }
        val bigger = Button(this).apply { text = "הגדל את האותיות"; setOnClickListener { prayerView.textSize = (prayerView.textSize / resources.displayMetrics.scaledDensity + 2f).coerceAtMost(42f) } }
        val smaller = Button(this).apply { text = "הקטן את האותיות"; setOnClickListener { prayerView.textSize = (prayerView.textSize / resources.displayMetrics.scaledDensity - 2f).coerceAtLeast(18f) } }
        val reminder = Button(this).apply { text = "הצג לי תזכורת בעוד 5 דקות"; setOnClickListener { sendReminder() } }
        root.addView(title); root.addView(subtitle); root.addView(scroll, LinearLayout.LayoutParams(-1,0,1f)); root.addView(bigger); root.addView(smaller); root.addView(reminder)
        setContentView(root)
        if (Build.VERSION.SDK_INT >= 33 && checkSelfPermission(Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) requestPermissions(arrayOf(Manifest.permission.POST_NOTIFICATIONS), 100)
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= 26) {
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(NotificationChannel(channelId, "Tefilas Haderech", NotificationManager.IMPORTANCE_HIGH))
        }
    }

    private fun sendReminder() {
        val intent = Intent(this, MainActivity::class.java).apply { flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TOP }
        val pending = PendingIntent.getActivity(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)
        val notification = if (Build.VERSION.SDK_INT >= 26) android.app.Notification.Builder(this, channelId) else android.app.Notification.Builder(this)
        notification.setSmallIcon(android.R.drawable.ic_dialog_info).setContentTitle("Tefilas Haderech").setContentText("Open the app to say the prayer.").setContentIntent(pending).setAutoCancel(true)
        val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(1, notification.build())
    }
}
