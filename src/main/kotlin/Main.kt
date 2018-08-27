import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.paint.Color
import javafx.stage.Stage

fun main(args: Array<String>) {
    Application.launch(*args)
}

class Application : Application() {

    override fun start(primaryStage: Stage?) {
        val root = FXMLLoader.load<Parent>(javaClass.getResource("layout_main.fxml"))

        val scene = Scene(root, 1920.0, 1080.0, Color.BLUE)

        primaryStage?.apply {
            setScene(scene)
            title = "Connect"
            show()
        }
    }

}