
// File: ocpconfig.groovy

// http://mrhaki.blogspot.nl/2009/11/gradle-goodness-using-properties-for.html

db {
    host = 'localhost'
    port = 1521
    name = 'XE'
    jdbcdriverpath = '/Software/ojdbc6.jar'
    driverClassName = 'oracle.jdbc.OracleDriver'
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

cim {
    core {
        productidlist = 'endeca,publishing,siteadmin,REST,wcs_extensions,endeca_reader,commerce'
        addonidlist = 'endeca,endeca_reader,sso,merch,preview,ssoIntegration,nonswitchingdatasource,dcsui_siteadmin_versioned,REST,publishing_management,publishing_nonswitchingdatasource,publishing_externalPreviewServer'
    }
    ca {
        productidlist = 'endeca,publishing,siteadmin,REST,wcs_extensions,endeca_reader,commerce'
        addonidlist = 'endeca,endeca_reader,sso,merch,preview,ssoIntegration,nonswitchingdatasource,dcsui_siteadmin_versioned,REST,publishing_management,publishing_nonswitchingdatasource,publishing_externalPreviewServer'
    }
}

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

environments {
    local {
        deploy {
            assembleDir = '/opt/oracle/product/atg/ATG11.1/home/cimEars'
            dynamoRoot = '/opt/oracle/product/atg/ATG11.1/'
        }
    }

    dev {
    }

    test {
    }

    preprod {
    }

    prod {
    }
}
