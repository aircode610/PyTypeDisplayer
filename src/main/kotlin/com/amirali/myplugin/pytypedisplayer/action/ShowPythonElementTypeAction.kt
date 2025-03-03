package com.amirali.myplugin.pytypedisplayer.action

import com.amirali.myplugin.pytypedisplayer.service.PythonElementServiceImpl
import com.amirali.myplugin.pytypedisplayer.widget.PythonTypeWidget
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.wm.StatusBar
import com.intellij.openapi.wm.impl.status.StatusBarUtil
import com.intellij.openapi.wm.WindowManager
import com.intellij.openapi.project.Project
import com.intellij.openapi.ui.Messages

class ShowPythonElementTypeAction : AnAction() {

    override fun update(e: AnActionEvent) {
        // Enable the action only if we're in an editor within a project
        val project = e.project
        val editor = e.getData(CommonDataKeys.EDITOR)
        e.presentation.isEnabledAndVisible = project != null && editor != null
    }

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val editor = e.getData(CommonDataKeys.EDITOR) ?: return

        // Get the service
        val elementService = PythonElementServiceImpl.getInstance(project)

        // Get the element at caret
        val element = elementService.getElementAtCaret(editor, project) ?: return

        if (element == null) {
            updateWidgetMessage(project, "No Python element found at caret position")
            return
        }

        // Get the element type
        val typeInfo = elementService.getVariableTypeInfo(element, project)

        if (typeInfo != null) {
            // Display type information in a message dialog
            updateWidgetMessage(project, "Variable: ${typeInfo.variableName}, Type: ${typeInfo.typeName ?: "unknown"}")
        } else {
            // If we couldn't get type information, say can't determine the type
            updateWidgetMessage(project, "Could not determine element information")
        }
    }

    private fun updateWidgetMessage(project: Project, message: String) {
        // Update our status bar widget with the message
        PythonTypeWidget.updateMessage(project, message)
    }
}