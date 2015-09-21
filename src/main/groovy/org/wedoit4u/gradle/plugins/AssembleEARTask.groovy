package org.wedoit4u.gradle.plugins

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

/**
 * @author Naga
 *
 */


class AssembleEARTask extends DefaultTask {
    ConfigObject assembleEARConfig

    def deleteEar(earName) {
        println ("Deleting $earName")
        new ByteArrayOutputStream().withStream {
            project.exec {
                commandLine 'rm'
                args = ["-rf", earName]
            }
        }
    }

    @TaskAction
    def assembleEAR() {
        def deployConfig = project.extensions.config.config.deploy
        def securityFolder = "$deployConfig.assembleDir/$project.env/$assembleEARConfig.earname/atg_bootstrap.war/WEB-INF/ATG-INF/home/security"
        deleteEar("$deployConfig.assembleDir/$project.env/$assembleEARConfig.earname")
        project.exec {
            commandLine "./runAssembler"
            args = [
                "$deployConfig.assembleDir/$project.env/$assembleEARConfig.earname",
                "-m $assembleEARConfig.modules"
            ]
            workingDir "$project.atgHome/bin"
        }
        project.mkdir(securityFolder)

        project.copy {
            into "$securityFolder"
            from "$project.atgHome/security"
        }
    }
}
