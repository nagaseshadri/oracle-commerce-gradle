/**
 * 
 */
package org.wedoit4u.gradle.plugins

import org.gradle.api.DefaultTask
import org.gradle.api.internal.file.IdentityFileResolver
import org.gradle.api.java.archives.internal.DefaultManifest
import org.gradle.api.tasks.TaskAction

/**
 * @author Naga
 *
 */


class GenerateManifestTask extends DefaultTask {

    @TaskAction
    def generateATGManifest() {
        File manifestFile = new File("$project.projectDir/$project.extensions.config.metainfDirName/$project.extensions.config.manifestFileName")
        project.extensions.config.atgManifest = new DefaultManifest(new IdentityFileResolver())
        project.extensions.config.atgManifest.attributes.put("ATG-Product-Full", project.name)
        project.extensions.config.atgManifest.attributes.put('ATG-Required', project.extensions.config.atgRequired )
        def configDir = new File(project.extensions.config.configDirName)
        def manifestDir = new File("$project.projectDir/$project.extensions.config.metainfDirName")

        if(configDir.isDirectory()) {
            project.extensions.config.atgManifest.attributes.put("ATG-Config-Path", project.extensions.config.configDirName)
        }
        if(!manifestDir.isDirectory()) {
            manifestDir.mkdirs()
        }

        StringBuilder sb = new StringBuilder()
        /*
         * If the build is running in local environment and the project has a build/classes/main folder
         * add it to the classpath :: This is a fix for remote debugging from Eclipse to be able to
         * do Hot Deployment, for some reason Hot Deploy doesnt work when the Classes are packed in a Jar.
         * Doing this only in local as we wouldnt need to remote debug other environments, remove this check if you
         * think otherwise. CAUTION: the classes are now in the folder and also in the project.jar file
         */
        if (project.libsDir.listFiles() != null) {
            println "inside libsDir listfiles null check"
            project.libsDir.listFiles().sort().each { File file ->
                println "$file.name"
                sb.append("$project.extensions.config.buildDirName/$project.libsDir.name/").append(file.name).append(" ")
            }
        }
        if(!sb.toString().trim().equals("")) {
            project.extensions.config.atgManifest.attributes.put("ATG-Class-Path", sb.toString())
            project.extensions.config.atgManifest.attributes.put("ATG-Client-Class-Path", sb.toString())
        }

        project.extensions.config.atgManifest.writeTo (manifestFile.getAbsolutePath())
    }
}
