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

        project.extensions.config.config.db.schemas.each { key, value ->
            Task createSchemaTablesTask = project.tasks.create("create"+key.capitalize()+"SchemaTables", CreateSchemaTablesTask.class)
            createSchemaTablesTask.setGroup("Database")
            createSchemaTablesTask.setDescription("Creates the OOTB tables for "  + key.capitalize()  + " from scratch using CIM")
            createSchemaTablesTask.dependsOn(project.allprojects.generateATGManifest)
            createSchemaTablesTask.createSchemaTablesConfig = value
            createSchemaTablesTask.createSchemaTablesConfig.putAt("cimfilename", "create-"+key+"-db")
            createSchemaTablesTask.createSchemaTablesConfig.putAt("dbhost", project.extensions.config.config.db.host)
            createSchemaTablesTask.createSchemaTablesConfig.putAt("dbport", project.extensions.config.config.db.port)
            createSchemaTablesTask.createSchemaTablesConfig.putAt("dbname", project.extensions.config.config.db.name)
            createSchemaTablesTask.createSchemaTablesConfig.putAt("jdbcdriverpath", project.extensions.config.config.db.jdbcdriverpath)
            createSchemaTablesTask.createSchemaTablesConfig.putAt("driverclassname", project.extensions.config.config.db.driverclassname)
            createSchemaTablesTask.createSchemaTablesConfig.putAt("createtablestemplatefilepath", project.extensions.config.config.cim.createtablestemplatefilepath)
            createSchemaTablesTask.createSchemaTablesConfig.putAt("droptablestemplatefilepath", project.extensions.config.config.cim.droptablestemplatefilepath)
            createSchemaTablesTask.createSchemaTablesConfig.putAt("productidlist", project.extensions.config.config.cim.getAt(key).productidlist)
            createSchemaTablesTask.createSchemaTablesConfig.putAt("addonidlist", project.extensions.config.config.cim.getAt(key).addonidlist)
            println createSchemaTablesTask.createSchemaTablesConfig

            Task dropSchemaTablesTask = project.tasks.create("drop"+key.capitalize()+"SchemaTables", DropSchemaTablesTask.class)
            dropSchemaTablesTask.setGroup("Database")
            dropSchemaTablesTask.setDescription("Drops the OOTB tables for "  + key.capitalize()  + " from scratch using CIM")
            dropSchemaTablesTask.dependsOn(project.allprojects.generateATGManifest)
            dropSchemaTablesTask.dropSchemaTablesConfig = value
            dropSchemaTablesTask.dropSchemaTablesConfig.putAt("cimfilename", "drop-"+key+"-db")
            dropSchemaTablesTask.dropSchemaTablesConfig.putAt("dbhost", project.extensions.config.config.db.host)
            dropSchemaTablesTask.dropSchemaTablesConfig.putAt("dbport", project.extensions.config.config.db.port)
            dropSchemaTablesTask.dropSchemaTablesConfig.putAt("dbname", project.extensions.config.config.db.name)
            dropSchemaTablesTask.dropSchemaTablesConfig.putAt("jdbcdriverpath", project.extensions.config.config.db.jdbcdriverpath)
            dropSchemaTablesTask.dropSchemaTablesConfig.putAt("driverclassname", project.extensions.config.config.db.driverclassname)
            dropSchemaTablesTask.dropSchemaTablesConfig.putAt("createtablestemplatefilepath", project.extensions.config.config.cim.createtablestemplatefilepath)
            dropSchemaTablesTask.dropSchemaTablesConfig.putAt("droptablestemplatefilepath", project.extensions.config.config.cim.droptablestemplatefilepath)
            dropSchemaTablesTask.dropSchemaTablesConfig.putAt("productidlist", project.extensions.config.config.cim.getAt(key).productidlist)
            dropSchemaTablesTask.dropSchemaTablesConfig.putAt("addonidlist", project.extensions.config.config.cim.getAt(key).addonidlist)
            println dropSchemaTablesTask.dropSchemaTablesConfig
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