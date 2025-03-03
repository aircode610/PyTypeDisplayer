package com.amirali.myplugin.pytypedisplayer.widget

import com.amirali.myplugin.pytypedisplayer.service.PythonTypeWidgetUpdater
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.StatusBar
import com.intellij.openapi.wm.StatusBarWidget
import com.intellij.util.Consumer
import java.awt.Component
import java.awt.event.MouseEvent

class PythonTypeWidget(private val project: Project) : StatusBarWidget, StatusBarWidget.TextPresentation {

    companion object {
        const val ID = "PythonTypeWidget"
        private var currentMessage = ""

        fun updateMessage(project: Project, message: String) {
            currentMessage = message

            // Request the status bar to update our widget
            project.getService(PythonTypeWidgetUpdater::class.java)?.updateWidget()
        }
    }

    override fun ID(): String = ID

    override fun getPresentation(): StatusBarWidget.WidgetPresentation = this

    override fun install(statusBar: StatusBar) {
        // Widget is being installed to the status bar
    }

    override fun dispose() {
        // Clean up resources if needed
    }

    // TextPresentation methods
    override fun getText(): String = currentMessage

    override fun getTooltipText(): String = "Python Variable Type Information"

    override fun getAlignment(): Float = Component.LEFT_ALIGNMENT

    override fun getClickConsumer(): Consumer<MouseEvent>? = null
}