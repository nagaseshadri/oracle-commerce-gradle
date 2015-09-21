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
        def configDir = new File("$project.projectDir/$project.extensions.config.configDirName")
        def liveconfigDir = new File("$project.projectDir/$project.extensions.config.liveconfigDirName")
        def metainfDir = new File("$project.projectDir/$project.extensions.config.metainfDirName")
        def j2eeappsDir = new File("$project.projectDir/$project.extensions.config.j2eeappsDirName")
        File manifestFile = new File("$metainfDir/$project.extensions.config.manifestFileName")

        // create a new manifest file
        project.extensions.config.atgManifest = new DefaultManifest(new IdentityFileResolver())

        // add the project name as full name of the product
        project.extensions.config.atgManifest.attributes.put("ATG-Product-Full", project.name)

        // add the configured atg required modules
        project.extensions.config.atgManifest.attributes.put('ATG-Required', project.extensions.config.atgRequired )

        // if the project has a config directory, add it to the manifest
        if(configDir.isDirectory()) {
            project.extensions.config.atgManifest.attributes.put("ATG-Config-Path", project.extensions.config.configDirName)
        }

        // if the project has a liveconfig directory, add it to the manifest
        if(liveconfigDir.isDirectory()) {
            project.extensions.config.atgManifest.attributes.put('ATG-LiveConfig-Path', project.extensions.config.liveconfigDirName )
        }

        // create the manifest directory
        if(!metainfDir.isDirectory()) {
            metainfDir.mkdirs()
        }

        // build and set the ATG class path and client class path based on the jars in the directory
        StringBuilder sb = new StringBuilder()
        if (project.libsDir.listFiles() != null) {
            project.libsDir.listFiles().sort().each { File file ->
                sb.append("$project.extensions.config.buildDirName/$project.libsDir.name/").append(file.name).append(" ")
            }
        }
        if(!sb.toString().trim().equals("")) {
            project.extensions.config.atgManifest.attributes.put("ATG-Class-Path", sb.toString())
            project.extensions.config.atgManifest.attributes.put("ATG-Client-Class-Path", sb.toString())
        }

        // set client update true for ACC to work
        project.extensions.config.atgManifest.attributes.put("ATG-Client-Update-File", "true")

        // adding EAR directory to manifest
        if(j2eeappsDir.isDirectory()) {
            j2eeappsDir.listFiles().each { File file ->
                project.extensions.config.atgManifest.attributes.put("ATG-EAR-Module", "$j2eeappsDir/$file.name")
                project.extensions.config.atgManifest.attributes.put("ATG-J2EE", "$j2eeappsDir/$file.name")
            }
        }

        project.extensions.config.atgManifest.writeTo (manifestFile.getAbsolutePath())
    }
}
