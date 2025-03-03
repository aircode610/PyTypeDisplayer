package com.amirali.myplugin.pytypedisplayer.service

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.jetbrains.python.psi.*

class PythonElementServiceImpl : PythonElementService {

    override fun getElementAtCaret(editor: Editor, project: Project): PsiElement? {
        val caretOffset = editor.caretModel.offset
        val psiFile = PsiDocumentManager.getInstance(project).getPsiFile(editor.document) ?: return null

        // Make sure we're in a Python file
        if (psiFile !is PyFile) return null

        // Return the element at caret position
        return psiFile.findElementAt(caretOffset)
    }

    override fun getPythonElementInfo(element: PsiElement): PyElementInfo? {
        // Basic element info
        val elementType = element.node.elementType.toString()
        val elementClass = element.javaClass.simpleName
        val elementText = element.text.let {
            if (it.length > 50) it.substring(0, 50) + "..." else it
        }

        // Find meaningful parent element
        val meaningfulParent = findMeaningfulParent(element)
        val parentType = meaningfulParent?.javaClass?.simpleName

        return PyElementInfo(
            elementType = elementType,
            elementClass = elementClass,
            elementText = elementText,
            parentType = parentType
        )
    }

    // Helper method to find a more meaningful parent element
    private fun findMeaningfulParent(element: PsiElement): PyElement? {
        return PsiTreeUtil.getParentOfType(
            element,
            PyFunction::class.java,
            PyClass::class.java,
            PyStatement::class.java,
            PyExpression::class.java,
            PyImportStatement::class.java,
            PyImportElement::class.java
        )
    }
}