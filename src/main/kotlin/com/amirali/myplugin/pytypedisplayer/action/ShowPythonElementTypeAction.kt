package com.amirali.myplugin.pytypedisplayer.action

import com.amirali.myplugin.pytypedisplayer.service.PythonElementServiceImpl
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.components.service
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
        val element = elementService.getElementAtCaret(editor, project)

        if (element == null) {
            Messages.showInfoMessage(
                "No Python element found at caret position.",
                "Python Element Info"
            )
            return
        }

        // Get element info
        val elementInfo = elementService.getPythonElementInfo(element)

        if (elementInfo == null) {
            Messages.showInfoMessage(
                "Could not determine Python element type.",
                "Python Element Info"
            )
            return
        }

        // Display the information
        val message = """
            Element Type: ${elementInfo.elementType}
            Element Class: ${elementInfo.elementClass}
            Text: "${elementInfo.elementText}"
            
            Parent Element: ${elementInfo.parentType ?: "None"}
        """.trimIndent()

        Messages.showInfoMessage(
            message,
            "Python Element Info"
        )
    }
}