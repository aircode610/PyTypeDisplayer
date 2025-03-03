package com.amirali.myplugin.pytypedisplayer.service

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement

// Service interface
interface PythonElementService {
    fun getElementAtCaret(editor: Editor, project: Project): PsiElement?
    fun getVariableTypeInfo(element: PsiElement, project: Project): PyTypeInfo?
}

// Data class to hold type information
data class PyTypeInfo(
    val variableName: String,
    val typeName: String?,
    val isInferred: Boolean = true,
    val isAnnotated: Boolean = false
)