# Gradle Plugin to build Oracle Commerce (ATG + Endeca) Projects

[![Build Status](https://travis-ci.org/nagaseshadri/oracle-commerce-gradle.svg?branch=master)](https://travis-ci.org/nagaseshadri/oracle-commerce-gradle)

Created by **Naga**rajan Seshadri  
Email <a.s.nagarajan@gmail.com>  
[Github homepage](https://github.com/nagaseshadri/oracle-commerce-gradle)

## Complete examples

- ATG Module that uses the plugin - example [test-atg-module](https://github.com/nagaseshadri/test-atg-module)
- Refer to the build.gradle, settings.gradle and gradle.properies in the root folder
- Refer to build.gradle inside all submodules

## Supported Environments

- Oracle Linux 6.6

## Features

- Automatically unlinks and links the workspace to a module inside ATG 11.1, name of the module is the rootProject.name as defined in settings.gradle in the root project folder
- Loads default configuration from ocpconfig.groovy present in the plugin, all configs can be overridden by a custom config file whose path (**configFilePath**) can be mentioned in the plugin extension configuration as below

```
config {
    atgRequired = 'DCS'
    configFilePath = 'buildtools/gradle/config.groovy'
}
```

## Supported Tasks

- [Copy Dependencies To Lib](#CopyDependenciesToLibs)
- [Generates ATG Manifest](#GenerateATGManifest)
- [Assemble Application EAR](#AssembleEAR)
- [Drop Schema Tables](#DropSchemaTables)
- [Create Schema Tables](#CreateSchemaTables)

# Copy Dependencies To Libs

Copies any third party dependencies to the build/libs directory based on copyToLib configuration. All dependencies in copyToLib configuration are added automatically to compile configuration so you dont need to specify the dependencies twice. Specify all runtime/compile time dependencies only in copyToLib

# Generate ATG Manifest

Generates the ATG Manifest for the Root and all sub modules. ATG-Required is updated based on the extension property atgRequired, ATG-J2EE and ATG-EAR-Module are added based on presence of EAR inside the project/subprojects. ATG-Class-Path and ATG-Client-Class-Path are updated based on copyToLib configuration. 

# Assemble EAR

The plugin generates multiple **assemble*Application*EAR** tasks based on the applications defined inside deploy. So for the example snippet (as below) in **config.groovy** it would generate 4 tasks - **assembleStorefrontEAR**, **assembleManagementEAR**, **assembleLockEAR**, **assembleSsoEAR**

```
deploy {
    assembleDir = '/u01/oracle/product/atg/ATG11.1/home/cimEars'
    dynamoRoot = '/u01/oracle/product/atg/ATG11.1/'
    applications {
        storefront {
            earname = 'testlive.ear'
            modules = 'DafEar.Admin TestATGModule'
        }
        management {
            earname = 'testca.ear'
            modules = 'DCS-UI.Versioned BIZUI PubPortlet DafEar.Admin SiteAdmin.Versioned DCS-UI DPS.Search.Index DCS.Search.Order.Index DAF.Endeca.Index.Versioned DCS.Endeca.Index.Versioned DCS-UI.SiteAdmin.Versioned'
        }
        lock {
            earname = 'testlock.ear'
            modules = 'DafEar.Admin'
        }
        sso {
            earname = 'testsso.ear'
            modules = 'DPS.InternalUsers SSO'
        }
    }
}
```

# Drop Schema Tables

The plugin adds a drop**Schema**Tables task for every schema defined inside db.  So for the example snippet (as below) in **config.groovy** it would generate 2 tasks - **dropCoreSchemaTables**, **dropCaSchemaTables**. It uses a CIM template to generate a cim batch script, which is then executed to drop the schema tables bsaed on dbinit configurations

```
db {
    host = 'localhost'
    port = 1521
    name = 'XE'
    jdbcdriverpath = '/Software/ojdbc6.jar'
    driverclassname = 'oracle.jdbc.OracleDriver'
    schemas {
        core {
            username = 'atg_core'
            password = 'atg_core'
            jndi = 'ATGCoreDS'
            datasourceid = 'nonswitchingCore'
        }
        ca {
            username = 'atg_ca'
            password = 'atg_ca'
            jndi = 'ATGCaDS'
            datasourceid = 'management'
        }
    }
}
```

# Create Schema Tables

The plugin adds a create**Schema**Tables task for every schema defined inside db.  So for the example snippet (as below) in **config.groovy** it would generate 2 tasks - **createCoreSchemaTables**, **createCaSchemaTables**. It uses a CIM template to generate a cim batch script, which is then executed to create the schema tables bsaed on dbinit configurations

```
db {
    host = 'localhost'
    port = 1521
    name = 'XE'
    jdbcdriverpath = '/Software/ojdbc6.jar'
    driverclassname = 'oracle.jdbc.OracleDriver'
    schemas {
        core {
            username = 'atg_core'
            password = 'atg_core'
            jndi = 'ATGCoreDS'
            datasourceid = 'nonswitchingCore'
        }
        ca {
            username = 'atg_ca'
            password = 'atg_ca'
            jndi = 'ATGCaDS'
            datasourceid = 'management'
        }
    }
}
```
