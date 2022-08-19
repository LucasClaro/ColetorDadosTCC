package br.senac.tcc

data class Registro(
    var setor: String,
    var sala: String,
    var conexoes: List<Conexao>
)

data class Conexao(
    var SSID: String,
    var BSSID: String,
    var RSSI: String
)