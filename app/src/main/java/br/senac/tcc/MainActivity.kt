package br.senac.tcc

import android.R
import android.content.BroadcastReceiver
import android.content.ContentValues.TAG
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.net.wifi.ScanResult
import android.net.wifi.WifiManager
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import br.senac.tcc.databinding.ActivityMainBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.util.*


class MainActivity : AppCompatActivity() {

    private lateinit var b: ActivityMainBinding
    private lateinit var database: DatabaseReference

    lateinit var wifiManager: WifiManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityMainBinding.inflate(layoutInflater)
        setContentView(b.root)

        setupFirebase()

        Log.v(TAG, "11111111111111")
        wifiManager = applicationContext.getSystemService(Context.WIFI_SERVICE) as WifiManager
        Log.v(TAG, "2222222222222")
        //verifierPermission()

        setupAdapters()

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

    private val wifiScanReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {

            if (intent.action == WifiManager.SCAN_RESULTS_AVAILABLE_ACTION) {
                Log.v(TAG, "444444444444444444444")
                val success = intent.getBooleanExtra(WifiManager.EXTRA_RESULTS_UPDATED, false)
                if (success) {
                    Log.v(TAG, "666666666")
                    val wifiScanList: List<ScanResult> = wifiManager.scanResults
                    Log.v(TAG, wifiScanList.toString())

                    val conexoes = mutableListOf<Conexao>()

                    for (wifiScan in wifiScanList) {
                        val conexao = Conexao(SSID = wifiScan.SSID, BSSID = wifiScan.BSSID.lowercase(Locale.getDefault()), RSSI = wifiScan.level.toString())
                        conexoes += conexao
//                    val nome = wifiScan.SSID
//                    val cod = wifiScan.BSSID.lowercase(Locale.getDefault())
//                    val rssi = wifiScan.level
//                    Log.v(TAG, "SSID: " + nome + ", BSSID: " + cod + "=> " + rssi + "dBm")
//                    texto += "SSID: " + nome + ", BSSID: " + cod + "=> " + rssi + "dBm\n"
                    }
                    val registro = Registro(setor = b.spinnerSetor.selectedItem.toString(), sala = b.spinnerSala.selectedItem.toString(), conexoes)

                    insert(registro)

                    b.btnTestar.isEnabled = true
                    b.spinnerSetor.setSelection(0)
                }
                else {
                    Log.v(TAG, "0000")
                }
            }

        }
    }

    fun insert(registro: Registro) {
        AlertDialog.Builder(this)
            .setTitle("Inserir registro em: " + registro.setor + " - " + registro.sala + "?")
            .setPositiveButton("Inserir") { _, _ ->
                val newNode = database.push()
                newNode.setValue(registro)

                b.textView.text = "Registro em ${registro.setor} - ${registro.sala} salvo no banco."
            }
            .setNegativeButton("Cancelar", null)
            .create()
            .show()
    }

    private fun teste() {
        b.btnTestar.isEnabled = false
        wifiManager.startScan()
    }

    private fun setupFirebase() {
        database = FirebaseDatabase.getInstance().reference
    }

    private fun setupAdapters() {
        val adapter = ArrayAdapter(applicationContext, R.layout.simple_spinner_item, listOf("Lixo", "A11", "A12", "B11", "C11", "C12", "D11",
            "NASA", "E11", "A21", "A22", "B21", "C21", "C22", "D21", "E21", "F21",
            "G11", "G12", "H11", "I11", "I12", "J11", "K11",
            "G21", "G22", "H21", "I21", "I22", "J21", "K21"
        ))
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        b.spinnerSetor.adapter = adapter

        b.spinnerSetor.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                val adapterSala = when(b.spinnerSetor.selectedItem) {
                    "A11" -> ArrayAdapter(applicationContext, R.layout.simple_spinner_item, listOf("A101", "A102", "A103", "A104", "A105", "A106", "A107", "A109", "A111", "A113"))
                    "A12" -> ArrayAdapter(applicationContext, R.layout.simple_spinner_item, listOf("A114", "A115", "A116", "A117", "A118", "A119", "A120", "A121", "A123", "A125"))
                    "B11" -> ArrayAdapter(applicationContext, R.layout.simple_spinner_item, listOf("B126", "B127", "B128", "B129", "B130", "B228", "B230"))
                    "C11" -> ArrayAdapter(applicationContext, R.layout.simple_spinner_item, listOf("C131", "C132", "C133", "C134", "C135", "C136", "C137", "C138"))
                    "C12" -> ArrayAdapter(applicationContext, R.layout.simple_spinner_item, listOf("C139", "C141", "C143", "C145", "C146", "C147", "C148", "C149", "C150"))
                    "D11" -> ArrayAdapter(applicationContext, R.layout.simple_spinner_item, listOf("D152", "D153", "D154", "D155", "D156", "D157", "D158", "D159", "D160"))
                    "NASA" -> ArrayAdapter(applicationContext, R.layout.simple_spinner_item, listOf("A110"))
                    "E11" -> ArrayAdapter(applicationContext, R.layout.simple_spinner_item, listOf("E166", "E167", "E168", "E169"))
                    "A21" -> ArrayAdapter(applicationContext, R.layout.simple_spinner_item, listOf("A201", "A202", "A203", "A204", "A205", "A207", "A209", "A211", "A212", "A213"))
                    "A22" -> ArrayAdapter(applicationContext, R.layout.simple_spinner_item, listOf("A214", "A215", "A216", "A217", "A219", "A221", "A222", "A223", "A224", "A225", "A226"))
                    "B21" -> ArrayAdapter(applicationContext, R.layout.simple_spinner_item, listOf("B228", "B230"))
                    "C21" -> ArrayAdapter(applicationContext, R.layout.simple_spinner_item, listOf("C232", "C233", "C234", "C235", "C236", "C237", "C238", "C239", "C241", "C242", "C243"))
                    "C22" -> ArrayAdapter(applicationContext, R.layout.simple_spinner_item, listOf("C244", "C245", "C246", "C247", "C249", "C251", "C253", "C254", "C255", "C256", "C257"))
                    "D21" -> ArrayAdapter(applicationContext, R.layout.simple_spinner_item, listOf("D258", "D259", "D260", "D261", "D262", "D263", "D264", "D265", "D267"))
                    "E21" -> ArrayAdapter(applicationContext, R.layout.simple_spinner_item, listOf("E285", "E286", "E287"))
                    "F21" -> ArrayAdapter(applicationContext, R.layout.simple_spinner_item, listOf("F272", "F273", "F274", "F275", "F276", "F277", "F278", "F279", "F280", "F281", "F282", "F284"))
                    "G11" -> ArrayAdapter(applicationContext, R.layout.simple_spinner_item, listOf("G301", "G303", "G305", "G306", "G307", "G308"))
                    "G12" -> ArrayAdapter(applicationContext, R.layout.simple_spinner_item, listOf("G309", "G311", "G313", "G314", "G315", "G316", "G317"))
                    "H11" -> ArrayAdapter(applicationContext, R.layout.simple_spinner_item, listOf("H319", "H321", "H323", "H324", "H324"))
                    "I11" -> ArrayAdapter(applicationContext, R.layout.simple_spinner_item, listOf("I327", "I328", "I329", "I330", "I331", "I333", "I335", "I336"))
                    "I12" -> ArrayAdapter(applicationContext, R.layout.simple_spinner_item, listOf("I337", "I338", "I339", "I340", "I341", "I342", "I343", "I345"))
                    "J11" -> ArrayAdapter(applicationContext, R.layout.simple_spinner_item, listOf("Corredor"))
                    "K11" -> ArrayAdapter(applicationContext, R.layout.simple_spinner_item, listOf("K352", "K354", "K356", "K357", "K358", "K359", "K361", "K363"))
                    "G21" -> ArrayAdapter(applicationContext, R.layout.simple_spinner_item, listOf("G401", "G403", "G405", "G407", "G408", "G409", "G410", "G411", "G413"))
                    "G22" -> ArrayAdapter(applicationContext, R.layout.simple_spinner_item, listOf("G414", "G415", "G416", "G417", "G418", "G419", "G421", "G423", "G425"))
                    "H21" -> ArrayAdapter(applicationContext, R.layout.simple_spinner_item, listOf("H426", "H427", "H428", "H429", "H430", "H431", "H433", "H435", "H437"))
                    "I21" -> ArrayAdapter(applicationContext, R.layout.simple_spinner_item, listOf("I439", "I440", "I441", "I442", "I443", "I444", "I445", "I447", "I449"))
                    "I22" -> ArrayAdapter(applicationContext, R.layout.simple_spinner_item, listOf("I451", "I452", "I453", "I454", "I455", "I463", "I465", "I467", "I469"))
                    "J21" -> ArrayAdapter(applicationContext, R.layout.simple_spinner_item, listOf("J471", "J473", "J475", "J477", "J479", "J481"))
                    "K21" -> ArrayAdapter(applicationContext, R.layout.simple_spinner_item, listOf("K478", "K480", "K482", "K483", "K485", "K487"))
                    else -> ArrayAdapter(applicationContext, R.layout.simple_spinner_item, listOf("Lixo"))
                }

                adapterSala.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                b.spinnerSala.adapter = adapterSala
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }
    }

}