package br.senac.tcc

import android.content.BroadcastReceiver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import br.senac.tcc.databinding.ActivityMainBinding
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var b: ActivityMainBinding

    lateinit var wifiManager: WifiManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)

        wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager

        b.btnTestar.setOnClickListener { teste() }
    }

    override fun onStart() {
        super.onStart()
        val intentFilter = IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
        registerReceiver(wifiScanReceiver, intentFilter)
    }
    override fun onStop() {
        super.onStop()
        unregisterReceiver(wifiScanReceiver)
    }

    private val wifiScanReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            val wifiScanList: List<ScanResult> = wifiManager.getScanResults()
            for (wifiScan in wifiScanList) {
                val name = wifiScan.BSSID.lowercase(Locale.getDefault())
                val rssi = wifiScan.level
                Log.v(TAG, "wifi: " + name + " => " + rssi + "dBm")
            }
        }
    }

    private fun teste() {
        wifiManager.startScan()
        //val ssid = info.ssid
        //val rssi = info.rssi
    }
}