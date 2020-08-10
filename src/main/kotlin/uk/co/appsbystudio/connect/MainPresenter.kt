package uk.co.appsbystudio.connect

import javafx.scene.image.ImageView

interface MainPresenter {

    //Toolbar
    fun setImageOpacity(imageView: ImageView?, opacity: Double)

    //Server
    fun serverInit(port: Int = 8080)
    fun startServer()

    fun stopServer()

}