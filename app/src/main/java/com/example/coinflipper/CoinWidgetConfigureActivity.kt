package com.example.coinflipper

import android.app.Activity
import android.appwidget.AppWidgetManager
import android.content.Intent
import android.os.Bundle
import com.example.coinflipper.CoinWidgetProvider.Companion.updateCoinWidget
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
        heads_spinner.adapter = CoinSpinnerAdapter(this, getSpinnerCoins())
        tails_spinner.adapter = CoinSpinnerAdapter(this, getSpinnerCoins())
        add_button.setOnClickListener { createWidget() }
    }

    private fun createWidget() {
        val appWidgetManager = AppWidgetManager.getInstance(this)

        val headsCoin = heads_spinner.selectedItem as Coin
        val tailsCoin = tails_spinner.selectedItem as Coin
        val spriteIds = getResourceIdsForSelection(headsCoin.coinType, tailsCoin.coinType)

        updateCoinWidget(this, appWidgetManager, appWidgetId, spriteIds)

        val resultValue = Intent()
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        setResult(RESULT_OK, resultValue)
        finish()
    }

    private fun getSpinnerCoins() = arrayOf(
        Coin(R.drawable.gold1, CoinType.Gold),
        Coin(R.drawable.silver1, CoinType.Silver),
        Coin(R.drawable.copper1, CoinType.Copper)
    )

    private fun getResourceIdsForSelection(headsCoinType: CoinType, tailsCoinType: CoinType) =
        CoinSpritesBuilder(resources)
            .setHeadsSprites(headsCoinType)
            .setTailsSprites(tailsCoinType)
            .build()
}