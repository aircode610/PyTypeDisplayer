package com.amirali.myplugin.pytypedisplayer.listener

// Local
import com.amirali.myplugin.pytypedisplayer.service.PythonCaretListener

import com.intellij.openapi.project.Project
import com.intellij.openapi.startup.ProjectActivity

class PythonCaretListenerStarter : ProjectActivity {

    override suspend fun execute(project: Project) {
        // Initialize the caret listener
        project.getService(PythonCaretListener::class.java)
    }

}