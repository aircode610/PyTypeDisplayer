package com.amirali.myplugin.pytypedisplayer.service

import com.intellij.openapi.components.Service
import com.intellij.openapi.components.Service.Level
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.jetbrains.python.psi.*
import com.jetbrains.python.psi.types.PyType
import com.jetbrains.python.psi.types.TypeEvalContext

@Service(Level.PROJECT)
class PythonElementServiceImpl : PythonElementService {

    override fun getElementAtCaret(editor: Editor, project: Project): PsiElement? {
        val caretOffset = editor.caretModel.offset
        val psiFile = PsiDocumentManager.getInstance(project).getPsiFile(editor.document) ?: return null

        // Make sure we're in a Python file
        if (psiFile !is PyFile) return null

        // Return the element at caret position
        return psiFile.findElementAt(caretOffset)
    }

    override fun getVariableTypeInfo(element: PsiElement, project: Project): PyTypeInfo? {
        // Start with the element at caret
        var currentElement = element

        // If we're directly on an identifier, it might be part of a reference or target expression
        if (currentElement.node.elementType.toString() == "Py:IDENTIFIER") {
            // Look for parent reference or target expression
            val parent = currentElement.parent
            if (parent is PyReferenceExpression || parent is PyTargetExpression) {
                currentElement = parent
            }
        }

        // Now handle the main element types that could represent variables
        when (currentElement) {
            is PyReferenceExpression -> {
                // For references (using a variable), get the inferred type
                return getTypeFromReference(currentElement, project)
            }

            is PyTargetExpression -> {
                // For targets (defining a variable), get the type from the assignment
                return getTypeFromTarget(currentElement, project)
            }

            else -> {
                // Try looking for a parent that might be a variable reference or definition
                val refExpr = PsiTreeUtil.getParentOfType(currentElement, PyReferenceExpression::class.java)
                if (refExpr != null) {
                    return getTypeFromReference(refExpr, project)
                }

                val targetExpr = PsiTreeUtil.getParentOfType(currentElement, PyTargetExpression::class.java)
                if (targetExpr != null) {
                    return getTypeFromTarget(targetExpr, project)
                }
            }
        }

        return null
    }

    private fun getTypeFromReference(reference: PyReferenceExpression, project: Project): PyTypeInfo {
        val variableName = reference.name ?: "unknown"

        // Create a type evaluation context
        val typeEvalContext = TypeEvalContext.codeAnalysis(project, reference.containingFile)

        // Get the inferred type
        val pyType = typeEvalContext.getType(reference)

        return PyTypeInfo(
            variableName = variableName,
            typeName = formatPyType(pyType),
        )
    }

    private fun getTypeFromTarget(target: PyTargetExpression, project: Project): PyTypeInfo {
        val variableName = target.name ?: "unknown"

        // Create a type evaluation context
        val typeEvalContext = TypeEvalContext.codeAnalysis(project, target.containingFile)

        // Get the inferred type
        val pyType = typeEvalContext.getType(target)

        return PyTypeInfo(
            variableName = variableName,
            typeName = formatPyType(pyType),
        )
    }

    private fun formatPyType(pyType: PyType?): String? {
        if (pyType == null) return "unknown"

        // Extract a clean type name from the PyType object
        return pyType.name?.let { name ->
            // Clean up type names like "builtins.str" to just "str"
            if (name.startsWith("builtins.")) {
                name.substring("builtins.".length)
            } else {
                name
            }
        } ?: "unknown"
    }

    companion object {
        // Convenience method for getting the service
        fun getInstance(project: Project): PythonElementService {
            return project.getService(PythonElementServiceImpl::class.java)
        }
    }

}