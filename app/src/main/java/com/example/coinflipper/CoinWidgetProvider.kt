package com.example.coinflipper

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.RemoteViews
import android.widget.Toast
import kotlinx.coroutines.*

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in [CoinWidgetConfigureActivity]
 */


class CoinWidgetProvider : AppWidgetProvider() {
    private var isFlipping = false
    private lateinit var job: Job

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
                val appWidgetId = receivedIntent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID)
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
        const val EXTRA_COIN_SPRITES_IDS = "coin_sprites"

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

            views.setPendingIntentTemplate(R.id.flipper, getPendingIntent(context, appWidgetId))
            appWidgetManager.updateAppWidget(appWidgetId, views)
        }

        private fun getPendingIntent(context: Context, appWidgetId: Int): PendingIntent {
            val intent = Intent(context, CoinWidgetProvider::class.java).apply {
                action = COIN_FLIPPED
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
                data = Uri.parse(toUri(Intent.URI_INTENT_SCHEME))
            }
            return PendingIntent.getBroadcast(context, appWidgetId, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        }
    }
}
