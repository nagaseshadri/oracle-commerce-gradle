package org.wedoit4u.gradle.plugins

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

/**
 * @author Naga
 *
 */


class CreateSchemaTablesTask extends DefaultTask {
    ConfigObject createSchemaTablesConfig

    @TaskAction
    def createSchemaTables() {
        File tempCIMFile = File.createTempFile(createSchemaTablesConfig.cimfilename,".cim");
        def bindings = [
            "productidlist":"$createSchemaTablesConfig.productidlist",
            "addonidlist":"$createSchemaTablesConfig.addonidlist",
            "dbhost":"$createSchemaTablesConfig.dbhost",
            "dbport":"$createSchemaTablesConfig.dbport",
            "dbname":"$createSchemaTablesConfig.dbname",
            "driverpath":"$createSchemaTablesConfig.jdbcdriverpath",
            "schemas": [createSchemaTablesConfig]]
        tempCIMFile.deleteOnExit()
        def templateFilePath = createSchemaTablesConfig.createtablestemplatefilepath
        def templateFile = null
        if (templateFilePath.startsWith("classpath:")) {
            String createTablesFileName = templateFilePath.replace("classpath:", "")
            println createTablesFileName
            println 'this.getClass().getResource(createTablesFileName) ==> '+this.getClass().getResource(createTablesFileName)
            Utils.createCIMFileFromTemplateURL(this.getClass().getResource(createTablesFileName), bindings, tempCIMFile)
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
