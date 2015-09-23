# Gradle Plugin to build Oracle Commerce (ATG + Endeca) Projects
[![Build Status](https://travis-ci.org/nagaseshadri/oracle-commerce-gradle.svg?branch=master)](https://travis-ci.org/nagaseshadri/oracle-commerce-gradle)

created by Nagarajan Seshadri email a dot s dot nagarajan at gmail dot com
[Github homepage](https://github.com/nagaseshadri/oracle-commerce-gradle)

## Complete examples
- ATG Module that uses the plugin - example [test-atg-module](https://github.com/nagaseshadri/test-atg-module)

## Supported Tasks
- [Generates ATG Manifest](#GenerateATGManifest)
- [Assemble Application EAR](#AssembleEAR)
- [Drop Schema Tables](#DropSchemaTables)
- [Create Schema Tables](#CreateSchemaTables)

# Generate ATG Manifest

Generates the ATG Manifest for the Root and all sub modules

# Assemble EAR

The plugin generates assemble**Application**EAR tasks based on the applications defined inside deploy. So for the example snippet in **config.groovy** it would generate 4 tasks - assembleStorefrontEAR, assembleManagementEAR, assembleLockEAR, assembleSsoEAR

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

# Create Schema Tables

