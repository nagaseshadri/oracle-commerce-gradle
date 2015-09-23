package org.wedoit4u.gradle.plugins

import groovy.text.GStringTemplateEngine

/**
 * 
 * @author Naga
 *
 */
class Utils {

    static createCIMFileFromTemplate(templateFileName, bindings, cimFile) {
        def templateFile = new File(templateFileName)
        createCIMFileFromTemplate(templateFile, bindings, cimFile)
    }

    static createCIMFileFromTemplateFile(templateFile, bindings, cimFile) {
        def engine = new GStringTemplateEngine()
        def template = engine.createTemplate(templateFile).make(bindings)
        println template.toString()
        cimFile.write template.toString()
    }

    static createCIMFileFromTemplateURL(templateURL, bindings, cimFile) {
        def engine = new GStringTemplateEngine()
        def template = engine.createTemplate(templateURL).make(bindings)
        println template.toString()
        cimFile.write template.toString()
    }

    static String readFromJARFile(String filename)
    throws IOException {
        InputStream is = this.getClass().getResourceAsStream(filename);
        InputStreamReader isr = new InputStreamReader(is);
        BufferedReader br = new BufferedReader(isr);
        StringBuffer sb = new StringBuffer();
        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line);
        }
        br.close();
        isr.close();
        is.close();
        return sb.toString();
    }
}
