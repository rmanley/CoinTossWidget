package com.rmanley.coinflipper.widget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.rmanley.coinflipper.util.CoinSpritesProvider
import com.rmanley.coinflipper.R

class CoinWidgetSpritesService : RemoteViewsService()
{
    private var appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID

    override fun onGetViewFactory(intent: Intent?): RemoteViewsFactory = CoinWidgetRemoteViewsFactory(
        applicationContext,
        intent,
        CoinSpritesProvider.createInstance(applicationContext)
    )

    inner class CoinWidgetRemoteViewsFactory(
        private val context: Context,
        private val intent: Intent?,
        private val coinSpritesProvider: CoinSpritesProvider
    ) : RemoteViewsFactory {

        private lateinit var coinSpriteIds: IntArray

        // todo: better implement error logging/handling
        override fun onCreate() {
            intent?.let {
                appWidgetId = it.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)
                if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
                    Log.e("ERROR", "App widget id is invalid.")
                    return
                }
                coinSpriteIds = coinSpritesProvider.getCoinSprites(appWidgetId)
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
            setOnClickFillInIntent(R.id.coin_sprite, Intent())
        }

        override fun getCount() = coinSpriteIds.size

        override fun getViewTypeCount() = 1

        override fun onDestroy() {
        }
    }
}