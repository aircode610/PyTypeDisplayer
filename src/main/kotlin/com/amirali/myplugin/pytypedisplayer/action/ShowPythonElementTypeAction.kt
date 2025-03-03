package com.amirali.myplugin.pytypedisplayer.action

import com.amirali.myplugin.pytypedisplayer.service.PythonElementServiceImpl
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.project.Project
import com.intellij.openapi.wm.StatusBar
import com.intellij.openapi.wm.WindowManager
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
            Messages.showInfoMessage(
                "No Python element found at caret position.",
                "Python Element Info"
            )
            return
        }

        val typeInfo = elementService.getVariableTypeInfo(element, project)

        if (typeInfo != null) {
            // Display type information in a message dialog
            val annotationInfo = if (typeInfo.isAnnotated) "Explicitly annotated" else "Inferred by PyCharm"

            val message = """
                Variable: ${typeInfo.variableName}
                Type: ${typeInfo.typeName ?: "unknown"}
                Source: $annotationInfo
            """.trimIndent()

            Messages.showInfoMessage(
                message,
                "Python Type Info"
            )
        } else {
            // If we couldn't get type information, show basic element info as a fallback
            val elementInfo = elementService.getPythonElementInfo(element)

            if (elementInfo != null) {
                val message = """
                    Could not determine variable type.
                    Element Type: ${elementInfo.elementType}
                    Element Class: ${elementInfo.elementClass}
                    Text: "${elementInfo.elementText}"
                """.trimIndent()

                Messages.showInfoMessage(
                    message,
                    "Python Type Info"
                )
            } else {
                Messages.showInfoMessage(
                    "Could not determine element information.",
                    "Python Type Info"
                )
            }
        }

        // Get element info
//        val elementInfo = elementService.getPythonElementInfo(element)
//
//        if (elementInfo == null) {
//            Messages.showInfoMessage(
//                "Could not determine Python element type.",
//                "Python Element Info"
//            )
//            return
//        }

        // Display the information
//        val message = """
//            Element Type: ${elementInfo.elementType}
//            Element Class: ${elementInfo.elementClass}
//            Text: "${elementInfo.elementText}"
//
//            Parent Element: ${elementInfo.parentType ?: "None"}
//        """.trimIndent()
//
//        Messages.showInfoMessage(
//            message,
//            "Python Element Info"
//        )
    }
}