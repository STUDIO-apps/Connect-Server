import java.io.DataInputStream
import java.io.IOException
import java.io.OutputStream
import java.io.PrintStream
import java.net.ServerSocket
import java.net.Socket

class Server : Runnable {

    private var connection: Socket? = null
    private var inputStream: DataInputStream? = null

    override fun run() {
        try {
            val serverSocket = ServerSocket(8080, 10)
            while (true) {
                connection = serverSocket.accept()
                println("Connection received from: " + connection?.inetAddress?.hostName)

                val buffer = ByteArray(1024)

                if (connection != null) {
                    val socketServerReplyThread = SocketServerReplyThread(connection!!)
                    socketServerReplyThread.run()

                    inputStream = DataInputStream(connection?.getInputStream())
                }

                while (inputStream?.read(buffer) != -1) {
                    val len = inputStream?.readInt()
                    val data = len?.let { ByteArray(it) }
                    inputStream?.readFully(data)
                    val message = data?.let { String(it, Charsets.UTF_8) }
                    println(message)
                }
                println("Socket has closed!")
                System.exit(0)
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
            } catch (e: IOException) {
                e.printStackTrace()
            }

        }
    }

}