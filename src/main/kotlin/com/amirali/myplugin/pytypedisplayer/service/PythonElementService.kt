package com.amirali.myplugin.pytypedisplayer.service

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement

// Service interface
interface PythonElementService {
    fun getElementAtCaret(editor: Editor, project: Project): PsiElement?
    fun getPythonElementInfo(element: PsiElement): PyElementInfo?
    fun getVariableTypeInfo(element: PsiElement, project: Project): PyTypeInfo?
}

// Data class to hold element information
data class PyElementInfo(
    val elementType: String,
    val elementClass: String,
    val elementText: String,
    val parentType: String? = null
)

data class PyTypeInfo(
    val variableName: String,
    val typeName: String?,
    val isInferred: Boolean = true,
    val isAnnotated: Boolean = false
)