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
            Task genATGManifestTask = prj.tasks.create("generateATGManifest", GenerateManifestTask.class);
            genATGManifestTask.dependsOn('build', 'copyDependenciesToLibs')
            genATGManifestTask.setDescription("Generate ATG Manifest file for the Modules/Submodules")
            genATGManifestTask.setGroup("ATG")
        }
        loadConfiguration(project)
        project.extensions.config.config.deploy.applications.each { key, value ->
            //println value
            Task assemblerAppEARTask = project.tasks.create("assemble"+key.capitalize()+"EAR", AssembleEARTask.class)
            assemblerAppEARTask.setGroup("ATG")
            assemblerAppEARTask.setDescription("Assembles the " +key.capitalize()+ " application")
            assemblerAppEARTask.dependsOn(project.allprojects.generateATGManifest)
            assemblerAppEARTask.assembleEARConfig = value
            //println assemblerAppEARTask.assembleEARConfig
        }
    }

    // Load Configuration Sample taken from - http://mrhaki.blogspot.nl/2009/11/gradle-goodness-using-properties-for.html
    def loadConfiguration(project) {
        def environment = project.hasProperty('env') ? project.env : 'dev'
        println "Environment is set to $environment"
        def configFile = new File("$project.projectDir/$project.extensions.config.configFilePath")
        //println project.extensions.config.configFilePath
        //println configFile
        //println 'this.getClass().getResource("/ocpconfig.groovy") ==> '+this.getClass().getResource("/ocpconfig.groovy")
        def pluginConfigObj = new ConfigSlurper(environment).parse(this.getClass().getResource("/ocpconfig.groovy"))
        //println "pluginConfigObj ==> "+pluginConfigObj.common.scriptsfolder
        def configObj = new ConfigSlurper(environment).parse(configFile.toURL())
        //println "configObj ==> "+ configObj.common.scriptsfolder
        def mergedConfigObj = pluginConfigObj.merge(configObj)
        //println "mergedConfigObj ==> "+ mergedConfigObj.common.scriptsfolder
        project.extensions.config.config = mergedConfigObj
    }
}

class OCPluginConfig {
    String atgRequired = ""
    String buildDirName = 'build'
    String classesDirName = 'classes'
    String configDirName = 'config'
    String liveconfigDirName = 'liveconfig'
    String j2eeappsDirName = 'j2ee-apps'
    String metainfDirName = 'META-INF'
    String manifestFileName = 'MANIFEST.MF'
    String configFilePath = 'buildtools/gradle/config.groovy'
    Manifest atgManifest
    ConfigObject config
}