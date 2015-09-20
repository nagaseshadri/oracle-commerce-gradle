/**
 * 
 */
package org.wedoit4u.gradle.plugins

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

/**
 * @author Naga
 *
 */


class CopyDependenciesToLibsTask extends DefaultTask {

    @TaskAction
    def copyDependenciesToLibs() {
        project.copy {
            into project.libsDir
            from project.configurations.copyToLib
        }
    }
}
