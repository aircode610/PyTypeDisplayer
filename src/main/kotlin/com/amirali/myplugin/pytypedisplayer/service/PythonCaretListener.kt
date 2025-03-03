package com.amirali.myplugin.pytypedisplayer.service

// Local
import com.amirali.myplugin.pytypedisplayer.widget.PythonTypeWidget

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.Service.Level
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.editor.event.CaretEvent
import com.intellij.openapi.editor.event.CaretListener
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.FileEditorManagerEvent
import com.intellij.openapi.fileEditor.FileEditorManagerListener
import com.intellij.openapi.project.Project
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiDocumentManager
import com.jetbrains.python.psi.PyFile

@Service(Level.PROJECT)
class PythonCaretListener(private val project: Project) : CaretListener {

    private var currentEditor: Editor? = null

    init {
        // Subscribe to file editor events to track editor changes
        project.messageBus.connect().subscribe(
            FileEditorManagerListener.FILE_EDITOR_MANAGER,
            object : FileEditorManagerListener {
                override fun fileOpened(source: FileEditorManager, file: VirtualFile) {
                    registerToNewEditor(source.selectedTextEditor)
                }

                override fun selectionChanged(event: FileEditorManagerEvent) {
                    registerToNewEditor(event.manager.selectedTextEditor)
                }
            }
        )

        // Register to the currently active editor if any
        registerToNewEditor(FileEditorManager.getInstance(project).selectedTextEditor)
    }

    private fun registerToNewEditor(editor: Editor?) {
        // Remove listener from previous editor
        currentEditor?.caretModel?.removeCaretListener(this)

        // Register to new editor if it exists
        editor?.caretModel?.addCaretListener(this)
        currentEditor = editor

        // Process initial caret position
        if (editor != null) {
            processCaretPosition(editor)
        }
    }

    override fun caretPositionChanged(event: CaretEvent) {
        processCaretPosition(event.editor)
    }

    private fun processCaretPosition(editor: Editor) {
        // Get current PSI element
        val offset = editor.caretModel.offset
        val psiFile = PsiDocumentManager.getInstance(project).getPsiFile(editor.document)

        if (psiFile !is PyFile) {
            // If not a Python file, clear the widget
            PythonTypeWidget.updateMessage(project, "")
            return
        }

        val element = psiFile.findElementAt(offset) ?: return

        // Get element type information
        val elementService = PythonElementServiceImpl.getInstance(project)
        val typeInfo = elementService.getVariableTypeInfo(element, project)

        if (typeInfo != null) {
            PythonTypeWidget.updateMessage(project, "Variable: ${typeInfo.variableName}, Type: ${typeInfo.typeName ?: "unknown"}")
        } else {
            // Clear the widget when not on a variable
            PythonTypeWidget.updateMessage(project, "")
        }
    }

    override fun caretAdded(event: CaretEvent) {
        // Not needed, but required by the interface (TODO: can be implemented for other versions)
    }

    override fun caretRemoved(event: CaretEvent) {
        // Not needed, but required by the interface (TODO: can be implemented for other versions)
    }

}