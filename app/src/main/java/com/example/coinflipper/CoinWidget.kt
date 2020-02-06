package com.example.coinflipper

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.RemoteViews
import kotlinx.coroutines.*

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in [CoinWidgetConfigureActivity]
 */


class CoinWidget : AppWidgetProvider() {
    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateCoinWidget(context, appWidgetManager, appWidgetId)
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds)
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        intent?.let { receivedIntent ->
            if (receivedIntent.action == COIN_FLIPPED) {
                val appWidgetId = receivedIntent.getIntExtra(APP_WIDGET_ID, -1)
                context?.let {
                    if (appWidgetId > -1 && !isFlipping) {
                        isFlipping = true
                        GlobalScope.launch(Dispatchers.Main) {
                            flipCoin(it, appWidgetId)
                        }
                    } else {
                        job.cancel()
                        isFlipping = false
                    }
                }
            }
        }
    }

    private suspend fun flipCoin(context: Context, appWidgetId: Int) {
        job = GlobalScope.launch(Dispatchers.Main) {
            while (job.isActive) {
                val appWidgetManager = AppWidgetManager.getInstance(context)
                val views = RemoteViews(context.packageName, R.layout.coin_widget)
                views.showNext(R.id.flipper)
                appWidgetManager.updateAppWidget(appWidgetId, views)
                delay(100)
            }
        }
    }

    companion object {
        private const val COIN_FLIPPED = "com.example.coinflipper.action.COIN_FLIPPED"
        private const val APP_WIDGET_ID = "app_widget_id"
        const val EXTRA_COIN_SPRITES_IDS = "coin_sprites"
        private var isFlipping = false
        private lateinit var job: Job

        fun updateCoinWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int,
            coinSpriteIds: IntArray? = null
        ) {
            val views = RemoteViews(context.packageName, R.layout.coin_widget)

            coinSpriteIds?.let { ids ->
                val intent = Intent(context, CoinWidgetSpritesService::class.java).apply {
                    putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
                    putExtra(EXTRA_COIN_SPRITES_IDS, ids)
                    data = Uri.parse(toUri(Intent.URI_INTENT_SCHEME))
                }
                views.setRemoteAdapter(R.id.flipper, intent)
            }

            // todo: Figure out why this line causes "Problem Loading Widget Error"
            //views.setOnClickPendingIntent(R.id.flipper, getPendingIntent(context, COIN_FLIPPED, appWidgetId))
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }

        private fun getPendingIntent(context: Context, action: String, appWidgetId: Int): PendingIntent {
            val intent = Intent(context, CoinWidget::class.java).apply {
                setAction(action)
                putExtra(APP_WIDGET_ID, appWidgetId)
                data = Uri.parse(toUri(Intent.URI_INTENT_SCHEME))
            }
            return PendingIntent.getBroadcast(context, appWidgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }
    }
}
