package com.tonynowater.qr_scanner_to_sms

import android.os.Build
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService
import androidx.annotation.RequiresApi

@RequiresApi(api = Build.VERSION_CODES.N)
class QRSmsScannerTileService : TileService() {
    override fun onClick() {
        super.onClick()
        val launchIntent = packageManager.getLaunchIntentForPackage(packageName)
        if (launchIntent != null) {
            startActivityAndCollapse(launchIntent)
        }
    }

    override fun onTileAdded() {
        super.onTileAdded()
        val tile = qsTile
        tile.state = Tile.STATE_INACTIVE
        tile.updateTile()
    }

    override fun onTileRemoved() {
        super.onTileRemoved()
    }

    override fun onStartListening() {
        super.onStartListening()
    }

    override fun onStopListening() {
        super.onStopListening()
    }
}