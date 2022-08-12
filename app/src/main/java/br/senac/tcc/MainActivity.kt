package br.senac.tcc

import android.content.Context
import android.net.wifi.WifiManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import br.senac.tcc.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var b: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)

        b.btnTestar.setOnClickListener { teste() }
    }

    private fun teste() {
        val wifiManager = this.applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        val info = wifiManager.is5GHzBandSupported
        println(info)
        //val ssid = info.ssid
        //val rssi = info.rssi
    }
}