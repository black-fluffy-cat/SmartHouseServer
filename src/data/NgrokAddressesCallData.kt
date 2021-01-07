package com.jj.smarthouseserver.data

data class NgrokAddressesCallData(val senderId: String?, val tunnelsList: List<NgrokAddress?>?)
data class NgrokAddress(val name: String?, val publicUrl: String?, val addr: String?)

/* Sample JSON
{
    "senderId": "PC",
    "tunnelsList": [
    {
        "name": "tcp_ssh",
        "publicUrl": "tcp://2.tcp.ngrok.io:17821",
        "addr": "localhost:22"
    },
    {
        "name": "http_server_1",
        "publicUrl": "https://f2302c567a1f.ngrok.io",
        "addr": "http://localhost:8080"
    },
    {
        "name": "http_server",
        "publicUrl": "https://364cc38b4b7d.ngrok.io",
        "addr": "http://localhost:5000"
    }
    ]
}
*/
