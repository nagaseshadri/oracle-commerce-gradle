package org.wedoit4u.gradle.plugins

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

/**
 * @author Naga
 *
 */


class DropSchemaTablesTask extends DefaultTask {
    ConfigObject dropSchemaTablesConfig

    @TaskAction
    def dropSchemaTables() {
        File tempCIMFile = File.createTempFile(dropSchemaTablesConfig.cimfilename,".cim");
        def bindings = [
            "productidlist":"$dropSchemaTablesConfig.productidlist",
            "addonidlist":"$dropSchemaTablesConfig.addonidlist",
            "dbhost":"$dropSchemaTablesConfig.dbhost",
            "dbport":"$dropSchemaTablesConfig.dbport",
            "dbname":"$dropSchemaTablesConfig.dbname",
            "driverpath":"$dropSchemaTablesConfig.jdbcdriverpath",
            "schemas": [dropSchemaTablesConfig]]
        tempCIMFile.deleteOnExit()
        def templateFilePath = dropSchemaTablesConfig.droptablestemplatefilepath
        def templateFile = null
        if (templateFilePath.startsWith("classpath:")) {
            String dropTablesFileName = templateFilePath.replace("classpath:", "")
            println dropTablesFileName
            println 'this.getClass().getResource(dropTablesFileName) ==> '+this.getClass().getResource(dropTablesFileName)
            Utils.createCIMFileFromTemplateURL(this.getClass().getResource(dropTablesFileName), bindings, tempCIMFile)
        } else {
            templateFile = new File(templateFilePath)
            Utils.createCIMFileFromTemplateFile(templateFile, bindings, tempCIMFile)
        }

        project.exec {
            commandLine = ["./cim.sh"]
            args = [
                "-batch",
                tempCIMFile.getAbsolutePath()
            ]
            workingDir "$project.atgHome/bin"
        }
    }
}
