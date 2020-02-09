package com.rmanley.coinflipper.widget

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import com.rmanley.coinflipper.R
import com.rmanley.coinflipper.model.Coin
import com.rmanley.coinflipper.model.CoinColor
import com.rmanley.coinflipper.storage.CoinWidgetSharedPreferences
import com.rmanley.coinflipper.ui.CoinSpinnerAdapter
import com.rmanley.coinflipper.widget.CoinWidgetProvider.Companion.updateCoinWidget
import kotlinx.android.synthetic.main.coin_widget_configure.*

/**
 * The configuration screen for the [CoinWidgetProvider] AppWidget.
 */
class CoinWidgetConfigureActivity : Activity() {

    private var appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID

    public override fun onCreate(icicle: Bundle?) {
        super.onCreate(icicle)
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
        heads_spinner.adapter = CoinSpinnerAdapter(
            this,
            getSpinnerCoins()
        )
        tails_spinner.adapter = CoinSpinnerAdapter(
            this,
            getSpinnerCoins()
        )
        create_button.setOnClickListener { createWidget() }
    }

    private fun createWidget() {
        val coinWidgetStorage = CoinWidgetSharedPreferences.createInstance(this)
        val appWidgetManager = AppWidgetManager.getInstance(this)

        val headsCoin = heads_spinner.selectedItem as Coin
        val tailsCoin = tails_spinner.selectedItem as Coin
        coinWidgetStorage.saveHeadsColor(appWidgetId, headsCoin.coinColor)
        coinWidgetStorage.saveTailsColor(appWidgetId, tailsCoin.coinColor)

        updateCoinWidget(this, appWidgetManager, appWidgetId)

        val resultValue = Intent()
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        setResult(RESULT_OK, resultValue)
        finish()
    }

    private fun getSpinnerCoins() = arrayOf(
        Coin(R.drawable.gold1, CoinColor.Gold),
        Coin(R.drawable.silver1, CoinColor.Silver),
        Coin(R.drawable.copper1, CoinColor.Copper)
    )
}