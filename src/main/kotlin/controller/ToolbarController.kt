package controllers

import javafx.fxml.FXML
import javafx.scene.image.ImageView
import javafx.scene.input.MouseEvent
import javafx.scene.layout.AnchorPane

class ToolbarController {

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

    @FXML
    var view_dashboard: AnchorPane? = null
    @FXML
    var view_contacts: AnchorPane? = null

    @FXML
    fun entered(event: MouseEvent) {
        opacity(event.target as ImageView, 1.0)
    }

    @FXML
    fun exited(event: MouseEvent) {
        if ((event.target as ImageView).id != selectedId) opacity(event.target as ImageView, 0.8)
    }

    @FXML
    fun selected(event: MouseEvent) {
        if (selectedImage == null) selectedImage = toolbar_dash_button
        if (selectedImage != event.target as ImageView) opacity(selectedImage, 0.8)
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

    private fun opacity(imageView: ImageView?, opacity: Double) {
        imageView?.opacity = opacity
    }

    fun startMainService() {
        val thread = Thread(MainServer())
        thread.start
    }

}