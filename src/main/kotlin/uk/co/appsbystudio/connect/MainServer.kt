package uk.co.appsbystudio.connect

import javafx.application.Platform
import uk.co.appsbystudio.connect.data.models.ContactsModel
import java.io.*
import java.net.ServerSocket
import java.net.Socket

class MainServer(private val port: Int, private val view: MainView) : Runnable {

    var isRunning: Boolean = false

    private var connection: Socket? = null
    private var input: ObjectInputStream? = null

    override fun run() {
        try {
            Platform.runLater {
                view.serverStarted()
            }
            val serverSocket = ServerSocket(port, 10)
            while (isRunning) {
                connection = serverSocket.accept()
                Platform.runLater {
                    view.connectionReceived("""Connection received from: ${connection?.inetAddress?.hostName}""")
                }
                println("Connection received from: " + connection?.inetAddress?.hostName)

                val socketServerReplyThread = SocketServerReplyThread(connection)
                socketServerReplyThread.run()

                Platform.runLater {
                    view.connectionClosed()
                }

                println("Socket has closed!")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private inner class SocketServerReplyThread internal constructor(private val hostSocket: Socket?) : Thread() {

        override fun run() {
            val outputStream: OutputStream?
            val connectMessage = "You have connected to the server!"

            try {
                outputStream = hostSocket?.getOutputStream()
                val printStream = PrintStream(outputStream)
                printStream.print(connectMessage)
            } catch (e: IOException) {
                e.printStackTrace()
            }


        }
    }

    fun getData() {
        input = ObjectInputStream(connection?.getInputStream())

        while (isRunning) {
            val contact: ContactsModel = (input?.readObject() as ArrayList<ContactsModel>)[0]
            println(contact.name)
        }
    }

    fun closeConnection() {
        if (connection != null) {
            try {
                //inputStream?.close()
                connection?.close()
                isRunning = false
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }

}