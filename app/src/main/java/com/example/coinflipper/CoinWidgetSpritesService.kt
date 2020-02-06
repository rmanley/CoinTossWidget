package com.example.coinflipper

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.example.coinflipper.CoinWidget.Companion.EXTRA_COIN_SPRITES_IDS

class CoinWidgetSpritesService : RemoteViewsService()
{
    private var appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID

    override fun onGetViewFactory(intent: Intent?): RemoteViewsFactory = CoinWidgetRemoteViewsFactory(applicationContext, intent)

    inner class CoinWidgetRemoteViewsFactory(
        private val context: Context,
        private val intent: Intent?
    ) : RemoteViewsFactory {

        private lateinit var coinSpriteIds: IntArray

        override fun onCreate() {
            Log.d("SERVICE", "Initializing service...: $intent")
            intent?.let {
                appWidgetId = it.getIntExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID)
                coinSpriteIds = it.getIntArrayExtra(EXTRA_COIN_SPRITES_IDS) ?: run {
                    Log.e("ERROR", "Error: Sprites are null.")
                    intArrayOf()
                }
            }
        }

        override fun getLoadingView() = RemoteViews(context.packageName, R.layout.coin_widget_sprite)

        override fun getItemId(position: Int): Long = coinSpriteIds[position].toLong()

        override fun onDataSetChanged() {
        }

        override fun hasStableIds() = true

        override fun getViewAt(position: Int) = RemoteViews(context.packageName, R.layout.coin_widget_sprite).apply {
            Log.d("SERVICE", "Setting image: ${coinSpriteIds[position]}")
            setImageViewResource(R.id.coin_sprite, coinSpriteIds[position])
        }

        override fun getCount() = coinSpriteIds.size

        override fun getViewTypeCount() = 1

        override fun onDestroy() {
        }
    }
}