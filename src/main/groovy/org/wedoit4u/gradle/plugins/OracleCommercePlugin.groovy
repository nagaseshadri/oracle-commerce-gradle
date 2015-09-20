package org.wedoit4u.gradle.plugins

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.api.java.archives.Manifest

class OracleCommercePlugin implements Plugin<Project> {
    void apply(Project project) {
        project.getAllprojects().each { prj ->
            prj.getExtensions().create("config", OCPluginConfig.class)
            prj.getConfigurations().create('copyToLib')
            Task copyDepsToLibsTask = prj.tasks.create("copyDependenciesToLibs", CopyDependenciesToLibsTask.class);
            copyDepsToLibsTask.setDescription("Copy all dependencies to the build lib directory")
            copyDepsToLibsTask.setGroup("ATG")
            Task genATGManifest = prj.tasks.create("generateATGManifest", GenerateManifestTask.class);
            genATGManifest.dependsOn('build', 'copyDependenciesToLibs')
            genATGManifest.setDescription("Generate ATG Manifest file for the Modules/Submodules")
            genATGManifest.setGroup("ATG")
        }
    }
}

class OCPluginConfig {
    String atgRequired = ""
    String buildDirName = 'build'
    String classesDirName = 'classes'
    String configDirName = 'config'
    String j2eeappsDirName = 'j2ee-apps'
    String metainfDirName = 'META-INF'
    String manifestFileName = 'MANIFEST.MF'
    Manifest atgManifest
    ConfigObject config
}

