package com.rmanley.coinflipper.widget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.rmanley.coinflipper.R
import com.rmanley.coinflipper.storage.CoinSpritesRepository
import com.rmanley.coinflipper.util.CoinFlipper
import com.rmanley.coinflipper.widget.CoinWidgetProvider.Companion.COIN_FLIP_RESULT_IS_HEADS
import com.rmanley.coinflipper.widget.CoinWidgetProvider.Companion.COIN_FLIP_RESULT_TIMES

class CoinWidgetSpritesService : RemoteViewsService()
{
    private var appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID

    override fun onGetViewFactory(intent: Intent?): RemoteViewsFactory = CoinWidgetRemoteViewsFactory(
        applicationContext,
        intent,
        CoinSpritesRepository.createInstance(applicationContext)
    )

    inner class CoinWidgetRemoteViewsFactory(
        private val context: Context,
        private val intent: Intent?,
        private val coinSpritesRepository: CoinSpritesRepository
    ) : RemoteViewsFactory {

        private lateinit var coinSpriteIds: IntArray
        private lateinit var coinFlipper: CoinFlipper

        // todo: better implement error logging/handling
        override fun onCreate() {
            intent?.let {
                appWidgetId = it.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)
                if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
                    Log.e("ERROR", "App widget id is invalid.")
                    return
                }
                coinSpriteIds = coinSpritesRepository.getCoinSprites(appWidgetId)
                coinFlipper = CoinFlipper(coinSpriteIds)
            } ?: run {
                Log.e("ERROR", "Intent is null.")
                return
            }
        }

        override fun getLoadingView() = RemoteViews(context.packageName,
            R.layout.coin_widget_sprite
        )

        override fun getItemId(position: Int): Long = coinSpriteIds[position].toLong()

        override fun onDataSetChanged() {
        }

        override fun hasStableIds() = true

        override fun getViewAt(position: Int) = RemoteViews(context.packageName,
            R.layout.coin_widget_sprite
        ).apply {
            setImageViewResource(R.id.coin_sprite, coinSpriteIds[position])
            val coinFlipResult = coinFlipper.getCoinFlipResult()
            val coinFlipIntent = Intent().apply {
                putExtra(COIN_FLIP_RESULT_IS_HEADS, coinFlipResult.isHeads)
                putExtra(COIN_FLIP_RESULT_TIMES, coinFlipResult.timesFlipped)
            }
            setOnClickFillInIntent(R.id.coin_sprite, coinFlipIntent)
        }

        override fun getCount() = coinSpriteIds.size

        override fun getViewTypeCount() = 1

        override fun onDestroy() {
        }
    }
}