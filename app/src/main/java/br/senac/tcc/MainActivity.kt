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

        Log.v(TAG, "11111111111111")
        wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        Log.v(TAG, "2222222222222")
        //verifierPermission()

        b.btnTestar.setOnClickListener { teste() }
    }

//    private fun verifierPermission() {
//        Log.v(TAG, "33333333333333333333")
//        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CHANGE_WIFI_MULTICAST_STATE)!= PackageManager.PERMISSION_GRANTED) {
//            /ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CHANGE_WIFI_MULTICAST_STATE), 1)
//        } else {
//            Log.v(TAG, "555555555555555555555")
//            permissaoConcedida = true
//            wifiManager.startScan()
//        }
//    }

//    override fun onRequestPermissionsResult(
//        requestCode: Int,
//        permissions: Array<out String>,
//        grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        Log.v(TAG, "444444444444444444444")
//
//        if (requestCode == 1) {
//            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                permissaoConcedida = true
//                wifiManager.startScan()
//            }
//            else if (!shouldShowRequestPermissionRationale(permissions[0])) {
//                Toast.makeText(this, "Not Allowed", Toast.LENGTH_LONG).show()
//            }
//            else {
//                permissaoConcedida = false
//                Toast.makeText(this, "Not Allowed", Toast.LENGTH_LONG).show()
//                setResult(RESULT_CANCELED)
//                finish()
//            }
//        }
//    }

    override fun onStart() {
        super.onStart()
        Log.v(TAG, "333333")
//        val intentFilter = IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
//        registerReceiver(wifiScanReceiver, intentFilter)
        val intentFilter = IntentFilter()
        intentFilter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)
        applicationContext.registerReceiver(wifiScanReceiver, intentFilter)
    }

    override fun onStop() {
        super.onStop()
        unregisterReceiver(wifiScanReceiver)
    }

    val wifiScanReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            Log.v(TAG, "444444444444444444444")
            val success = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false)
            if (success) {
                Log.v(TAG, "666666666")
                val wifiScanList: List<ScanResult> = wifiManager.scanResults
                Log.v(TAG, wifiScanList.toString())
                for (wifiScan in wifiScanList) {
                    val name = wifiScan.BSSID.lowercase(Locale.getDefault())
                    val rssi = wifiScan.level
                    Log.v(TAG, "wifi: " + name + " => " + rssi + "dBm")
                }
            }
            else {
                Log.v(TAG, "XXXXXX")
            }
        }
    }

    private fun teste() {
        //verifierPermission()
        //wifiManager.setWifiEnabled(false);
        wifiManager.startScan()
        Log.v(TAG, "555555555")
        //val ssid = info.ssid
        //val rssi = info.rssi
    }
}