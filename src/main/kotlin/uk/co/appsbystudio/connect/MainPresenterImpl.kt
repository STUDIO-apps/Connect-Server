package uk.co.appsbystudio.connect

import javafx.scene.image.ImageView

class MainPresenterImpl(val view: MainView) : MainPresenter {

    private lateinit var server: MainServer
    private lateinit var thread: Thread

    override fun setImageOpacity(imageView: ImageView?, opacity: Double) {
        imageView?.opacity = opacity
    }

    override fun serverInit(port: Int) {
        server = MainServer(port, view)
        thread = Thread(server)
    }

    override fun startServer() {
        if (!server.isRunning) {
            server.isRunning = true
            thread.start()
        } else {
            server.isRunning = false
            server.closeConnection()
            //thread.join()
            view.serverClosed()
        }
    }

    override fun stopServer() {
        server.closeConnection();
    }
}