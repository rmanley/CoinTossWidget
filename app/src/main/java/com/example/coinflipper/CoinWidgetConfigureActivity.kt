package com.example.coinflipper

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.example.coinflipper.CoinWidget.Companion.updateAppWidget
import kotlinx.android.synthetic.main.coin_widget_configure.*

/**
 * The configuration screen for the [CoinWidget] AppWidget.
 */
class CoinWidgetConfigureActivity : Activity() {

    private var appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID

    public override fun onCreate(icicle: Bundle?) {
        super.onCreate(icicle)

        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED)

        setContentView(R.layout.coin_widget_configure)
        initStateFromIntent()
        initUI()

    }

    private fun initStateFromIntent() {
        appWidgetId = intent?.extras?.getInt(
            AppWidgetManager.EXTRA_APPWIDGET_ID,
            AppWidgetManager.INVALID_APPWIDGET_ID
        ) ?: AppWidgetManager.INVALID_APPWIDGET_ID

        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish()
        }
    }

    private fun initUI() {
        val coinImages = arrayOf(R.drawable.gold1, R.drawable.silver1, R.drawable.copper1)
        Log.d("SPINNER", coinImages.joinToString(prefix = "[", postfix = "]"))
        heads_spinner.adapter = ImageSpinnerAdapter(this, coinImages)
        tails_spinner.adapter = ImageSpinnerAdapter(this, coinImages)
        add_button.setOnClickListener { createWidget() }
    }

    private fun createWidget() {
        // It is the responsibility of the configuration activity to update the app widget
        val appWidgetManager = AppWidgetManager.getInstance(this)
        // todo: set widget based on selections
        Log.d("SPINNER", "Heads: ${heads_spinner.selectedItem}")
        Log.d("SPINNER", "Tails: ${tails_spinner.selectedItem}")
        updateAppWidget(this, appWidgetManager, appWidgetId)

        // Make sure we pass back the original appWidgetId
        val resultValue = Intent()
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        setResult(RESULT_OK, resultValue)
        finish()
    }
}