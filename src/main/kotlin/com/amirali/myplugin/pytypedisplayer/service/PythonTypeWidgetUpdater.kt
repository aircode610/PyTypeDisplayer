package com.amirali.myplugin.pytypedisplayer.service

// Local
import com.amirali.myplugin.pytypedisplayer.widget.PythonTypeWidget

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.Service.Level
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.WindowManager

@Service(Level.PROJECT)
class PythonTypeWidgetUpdater(private val project: Project) {

    fun updateWidget() {
        val statusBar = WindowManager.getInstance().getStatusBar(project) ?: return
        statusBar.updateWidget(PythonTypeWidget.ID)
    }

}