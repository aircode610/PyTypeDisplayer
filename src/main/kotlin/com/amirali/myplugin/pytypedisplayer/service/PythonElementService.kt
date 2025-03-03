package com.amirali.myplugin.pytypedisplayer.service

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.jetbrains.python.psi.PyElement

// Service interface
interface PythonElementService {
    fun getElementAtCaret(editor: Editor, project: Project): PsiElement?
    fun getPythonElementInfo(element: PsiElement): PyElementInfo?
}

// Data class to hold element information
data class PyElementInfo(
    val elementType: String,
    val elementClass: String,
    val elementText: String,
    val parentType: String? = null
)