package uk.co.appsbystudio.connect

interface MainView {

    fun serverStarted()

    fun serverClosed()

    fun connectionReceived(address: String)

    fun connectionClosed()

    fun receivedMessage(message: String)

}