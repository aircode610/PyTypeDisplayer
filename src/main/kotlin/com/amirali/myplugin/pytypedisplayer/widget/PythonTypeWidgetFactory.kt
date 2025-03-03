package com.amirali.myplugin.pytypedisplayer.widget

import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.StatusBar
import com.intellij.openapi.wm.StatusBarWidget
import com.intellij.openapi.wm.StatusBarWidgetFactory

class PythonTypeWidgetFactory : StatusBarWidgetFactory {

    override fun getId(): String = PythonTypeWidget.ID

    override fun getDisplayName(): String = "Python Type Information"

    override fun isAvailable(project: Project): Boolean = true

    override fun createWidget(project: Project): StatusBarWidget {
        return PythonTypeWidget(project)
    }

    override fun disposeWidget(widget: StatusBarWidget) {
        widget.dispose()
    }

    override fun canBeEnabledOn(statusBar: StatusBar): Boolean = true
}