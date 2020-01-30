package com.example.coinflipper

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import java.util.*

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in [CoinWidgetConfigureActivity]
 */


class CoinWidget : AppWidgetProvider() {
    private val timer = Timer()
    private var isFlipping = false

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
        }
    }

    override fun onEnabled(context: Context?) {
        super.onEnabled(context)
        timer.cancel()
        isFlipping = false
    }

    override fun onDisabled(context: Context?) {
        super.onDisabled(context)
        timer.cancel()
        isFlipping = false
    }

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        // When the user deletes the widget, delete the preference associated with it.
        for (appWidgetId in appWidgetIds) {
            deleteTitlePref(context, appWidgetId)
            timer.cancel()
            isFlipping = false
        }
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        intent?.let { receivedIntent ->
            if (receivedIntent.action == COIN_FLIPPED) {
                val appWidgetId = receivedIntent.getIntExtra(APP_WIDGET_ID, -1)
                context?.let {
                    if (appWidgetId > -1)
                        flipCoin(it, appWidgetId)
                }
            }
        }
    }

    private fun flipCoin(context: Context, appWidgetId: Int) {
        Log.d("FLIP_COIN", "isFlipping: $isFlipping, id: $appWidgetId")
        if (isFlipping) {
            isFlipping = false
            timer.cancel()
        } else {
            isFlipping = true
            timer.scheduleAtFixedRate(object : TimerTask() {
                override fun run() {
                    val appWidgetManager = AppWidgetManager.getInstance(context)
                    val views = RemoteViews(context.packageName, R.layout.coin_widget)
                    views.showNext(R.id.flipper)
                    appWidgetManager.updateAppWidget(appWidgetId, views)
                }
            }, 0, 100)
        }
    }

    companion object {
        private const val COIN_FLIPPED = "com.example.coinflipper.action.COIN_FLIPPED"
        private const val APP_WIDGET_ID = "app_widget_id"

        fun updateAppWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {
            val views = RemoteViews(context.packageName, R.layout.coin_widget)
            views.setOnClickPendingIntent(R.id.flipper, getPendingIntent(context, COIN_FLIPPED, appWidgetId))
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }

        private fun getPendingIntent(context: Context, action: String, appWidgetId: Int): PendingIntent {
            val intent = Intent(context, CoinWidget::class.java).apply {
                setAction(action)
                putExtra(APP_WIDGET_ID, appWidgetId)
            }
            return PendingIntent.getBroadcast(context, appWidgetId, intent, 0)
        }
    }
}
