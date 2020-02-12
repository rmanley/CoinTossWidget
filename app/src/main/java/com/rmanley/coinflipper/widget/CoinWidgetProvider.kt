package com.rmanley.coinflipper.widget

import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.widget.RemoteViews
import android.widget.Toast
import com.rmanley.coinflipper.R
import com.rmanley.coinflipper.storage.CoinWidgetSharedPreferences
import com.rmanley.coinflipper.util.CoinFlipper
import com.rmanley.coinflipper.util.CoinSpritesProvider
import kotlinx.coroutines.*

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in [CoinWidgetConfigureActivity]
 */


class CoinWidgetProvider : AppWidgetProvider() {
    private var isFlipping = false

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateCoinWidget(
                context,
                appWidgetManager,
                appWidgetId
            )
        }
        super.onUpdate(context, appWidgetManager, appWidgetIds)
    }

    override fun onDeleted(context: Context?, appWidgetIds: IntArray?) {
        context?.let {
            val coinWidgetStorage = CoinWidgetSharedPreferences.createInstance(it)
            appWidgetIds?.forEach { id ->
                coinWidgetStorage.deleteCoinColors(id)
            }
        }
        isFlipping = false
        super.onDeleted(context, appWidgetIds)
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        super.onReceive(context, intent)
        intent?.let { receivedIntent ->
            if (receivedIntent.action == COIN_FLIPPED) {
                val appWidgetId = receivedIntent.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID,
                    AppWidgetManager.INVALID_APPWIDGET_ID)
                context?.let {
                    if (appWidgetId != AppWidgetManager.INVALID_APPWIDGET_ID && !isFlipping) {
                        isFlipping = true
                        GlobalScope.launch {
                            flipCoin(it, appWidgetId)
                        }
                    }
                }
            }
        }
    }

    // todo: refactor
    private suspend fun flipCoin(context: Context, appWidgetId: Int) {
        withContext(Dispatchers.Main) {
            val coinFlipResult = coinFlipper?.getCoinFlipResult()
            coinFlipResult?.let { result ->
                repeat(result.timesFlipped) {
                    val appWidgetManager = AppWidgetManager.getInstance(context)
                    val views = RemoteViews(
                        context.packageName,
                        R.layout.coin_widget
                    )
                    views.showNext(R.id.flipper)
                    appWidgetManager.updateAppWidget(appWidgetId, views)
                    delay(25)
                }
                isFlipping = false
                val side = if (result.isHeads) "Heads!" else "Tails!"
                Toast.makeText(context, side, Toast.LENGTH_SHORT).show()
            }
        }
    }

    companion object {
        private const val COIN_FLIPPED = "com.example.coinflipper.action.COIN_FLIPPED"
        private lateinit var coinFlipper: CoinFlipper

        fun updateCoinWidget(
            context: Context,
            appWidgetManager: AppWidgetManager,
            appWidgetId: Int
        ) {
            val views = RemoteViews(context.packageName, R.layout.coin_widget)
            val intent = Intent(context, CoinWidgetSpritesService::class.java).apply {
                putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
                data = Uri.parse(toUri(Intent.URI_INTENT_SCHEME))
            }
            views.setRemoteAdapter(R.id.flipper, intent)
            views.setPendingIntentTemplate(R.id.flipper, getPendingIntent(context, appWidgetId))
            val coinSprites = CoinSpritesProvider.createInstance(context!!).getCoinSprites(appWidgetId)
            coinFlipper = CoinFlipper(coinSprites)
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
