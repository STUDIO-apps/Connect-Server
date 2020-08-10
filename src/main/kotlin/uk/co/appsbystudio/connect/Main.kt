package uk.co.appsbystudio.connect

import com.jfoenix.controls.JFXButton
import com.jfoenix.controls.JFXTextField
import javafx.application.Application
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.Label
import javafx.scene.image.ImageView
import javafx.scene.input.MouseEvent
import javafx.scene.layout.AnchorPane
import javafx.scene.paint.Color
import javafx.stage.Stage
import javafx.stage.StageStyle
import java.net.URL
import java.util.*
import kotlin.collections.ArrayList

fun main(args: Array<String>) {
    Application.launch(Main::class.java, *args)
}

class Main : Application(), MainView, Initializable {

    private var presenter: MainPresenter? = null

    //Mouse offset
    private var xOffset: Double = 0.0
    private var yOffset: Double = 0.0

    //Toolbar
    var selectedId: String? = "toolbar_dash_button"
    var selectedImage: ImageView? = null
    @FXML
    var toolbar_dash_button: ImageView? = null
    @FXML
    var toolbar_contact_button: ImageView? = null
    @FXML
    var toolbar_messages_button: ImageView? = null
    @FXML
    var toolbar_settings_button: ImageView? = null

    //Main view pane
    @FXML
    var view_dashboard: AnchorPane? = null
    @FXML
    var view_contacts: AnchorPane? = null

    //Dashboard
    @FXML
    var field_port: JFXTextField? = null
    @FXML
    var server_status_label: Label? = null
    @FXML
    var button_server: JFXButton? = null
    @FXML
    var client_status_label: Label? = null

    init {
        presenter = MainPresenterImpl(this)
    }

    override fun start(primaryStage: Stage?) {
        val root = FXMLLoader.load<Parent>(javaClass.classLoader.getResource("layout_main.fxml"))

        val scene = Scene(root, Color.TRANSPARENT)

        primaryStage?.apply {
            setScene(scene)
            title = "Connect"
            initStyle(StageStyle.TRANSPARENT)
            show()
        }

        root.setOnMousePressed {
            xOffset = it.sceneX
            yOffset = it.sceneY
        }

        root.setOnMouseDragged {
            primaryStage?.apply {
                x = it.screenX - xOffset
                y = it.screenY - yOffset
            }
        }
    }

    override fun initialize(location: URL?, resources: ResourceBundle?) {
        println(field_port?.text)
        field_port?.text?.toInt()?.let { presenter?.serverInit(it) }
    }

    /* TOOLBAR */

    @FXML
    fun entered(event: MouseEvent) {
        presenter?.setImageOpacity(event.target as ImageView, 1.0)
    }

    @FXML
    fun exited(event: MouseEvent) {
        if ((event.target as ImageView).id != selectedId) presenter?.setImageOpacity(event.target as ImageView, 0.8)
    }

    @FXML
    fun selected(event: MouseEvent) {
        if (selectedImage == null) selectedImage = toolbar_dash_button
        if (selectedImage != event.target as ImageView) presenter?.setImageOpacity(selectedImage, 0.8)
        selectedImage = event.target as ImageView
        selectedId = selectedImage?.id

        when (selectedId) {
            "toolbar_dash_button" -> {
                view_dashboard?.isVisible = true
                view_contacts?.isVisible = false
            }
            "toolbar_contact_button" -> {
                view_dashboard?.isVisible = false
                view_contacts?.isVisible = true
            }
        }
    }

    /* Socket server */

    fun setPort() {
        presenter?.serverInit(field_port?.text?.toInt()!!)
    }

    fun startMainService() {
        presenter?.startServer()
    }

    override fun stop() {
        super.stop()
        presenter?.stopServer()
    }

    override fun serverStarted() {
        button_server?.text = "Stop server"
        server_status_label?.text = "Server is running"
    }

    override fun serverClosed() {
        button_server?.text = "Start server"
        server_status_label?.text = "Server is not running"
    }

    override fun connectionReceived(address: String) {
        client_status_label?.text = address
    }

    override fun connectionClosed() {
        client_status_label?.text = "Disconnected"
    }

    override fun receivedMessage(message: String) {
        println(message)
    }
}