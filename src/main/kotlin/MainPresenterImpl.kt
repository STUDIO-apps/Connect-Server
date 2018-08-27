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
            thread.start()
            server.isRunning = true
        } else {
            server.closeConnection()
            thread.interrupt()
            view.serverClosed()
        }
    }

    override fun stopServer() {
        server.closeConnection()
    }
}