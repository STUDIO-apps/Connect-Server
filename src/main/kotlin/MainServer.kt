import javafx.application.Platform
import java.io.DataInputStream
import java.io.IOException
import java.io.OutputStream
import java.io.PrintStream
import java.net.ServerSocket
import java.net.Socket

class MainServer(private val port: Int, val view: MainView) : Runnable {

    var isRunning: Boolean = false

    private var connection: Socket? = null
    private var inputStream: DataInputStream? = null

    override fun run() {
        try {
            Platform.runLater {
                view.serverStarted()
            }
            val serverSocket = ServerSocket(port, 10)
            while (true) {
                connection = serverSocket.accept()
                Platform.runLater {
                    view.connectionReceived("""Connection received from: ${connection?.inetAddress?.hostName}""")
                }
                println("Connection received from: " + connection?.inetAddress?.hostName)

                val buffer = ByteArray(1024)

                val socketServerReplyThread = SocketServerReplyThread(connection!!)
                socketServerReplyThread.run()

                inputStream = DataInputStream(connection?.getInputStream())

                while (inputStream?.read(buffer) != -1) {
                    val string = String(buffer, 0, inputStream!!.read(buffer))

                    Platform.runLater {
                        view.receivedMessage(string)
                    }
                    println(string)
                }

                Platform.runLater {
                    view.connectionClosed()
                }
                println("Socket has closed!")
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private inner class SocketServerReplyThread internal constructor(private val hostSocket: Socket) : Thread() {

        override fun run() {
            val outputStream: OutputStream
            val connectMessage = "You have connected to the server!"

            try {
                outputStream = hostSocket.getOutputStream()
                val printStream = PrintStream(outputStream)
                printStream.print(connectMessage)
            } catch (e: IOException) {
                e.printStackTrace()
            }


        }
    }

    fun closeConnection() {
        if (connection != null) {
            try {
                inputStream?.close()
                connection?.close()
                isRunning = false
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }

}