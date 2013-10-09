package org.ow2.jpaas.naming.manager.mocks;

import org.apache.felix.ipojo.*;
import org.apache.felix.ipojo.annotations.*;
import org.ow2.jonas.jpaas.catalog.api.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.ow2.util.log.Log;
import org.ow2.util.log.LogFactory;


@Component(immediate=true)
@Instantiate
@Provides
public class CatalogMock implements IIaasCatalogFacade, IPaasCatalogFacade {

    private List<IaasConfiguration> iaasConfigurations;
    private List<PaasConfiguration> paasConfigurations;

    private static Log logger = LogFactory.getLog(CatalogMock.class);


    @Validate
    public void initialize() {

        logger.info("Started");

        iaasConfigurations = new ArrayList<IaasConfiguration>();

        IaasConfiguration iaasConf1 = new IaasConfiguration(
                "mycompute",
                "COMPUTE",
                "DUMMYIAAS",
                true,
                false,
                "src/test/resources/dummyiaas.xml",
                "COMPUTEIAAS-",
                new HashMap<String,String>());
        iaasConfigurations.add(iaasConf1);


        paasConfigurations = new ArrayList<PaasConfiguration>();

        PaasConfiguration paasConf1 = new PaasConfiguration(
                "mycontainer",
                "CONTAINER",
                "DUMMYPAAS",
                true,
                "src/test/resources/dummypaas.xml",
                null,
                "CONTAINERPAAS-",
                new HashMap<String,String>(),
                1);
        paasConfigurations.add(paasConf1);

        PaasConfiguration paasConf2 = new PaasConfiguration(
                "myrouter",
                "ROUTER",
                "ROUTERPAAS",
                true,
                "src/test/resources/dummypaas.xml",
                null,
                "ROUTERPAAS-",
                new HashMap<String,String>(),
                1);
        paasConfigurations.add(paasConf2);




    }
    @Override
    public List<IaasConfiguration> getIaasConfigurationList() {
        logger.info("iaasConfigurations.size()="+iaasConfigurations.size());

        return    iaasConfigurations;
    }

    @Override
    public String getDefaultIaasConfigurationName() throws IaasCatalogException {
        for (IaasConfiguration conf:getIaasConfigurationList()) {
            if (conf.isDefault()) {
                return conf.getName();
            }

        }
        throw new IaasCatalogException("Default IaaS configuration not found");

    }

    @Override
    public IaasConfiguration getIaasConfiguration(String name) throws IaasCatalogException {
        for (IaasConfiguration conf:getIaasConfigurationList()) {
            if (conf.getName().equals(name)) {
                return conf;
            }
        }
        throw new IaasCatalogException("IaaS configuration " + name + "not found");

    }

    @Override
    public List<PaasConfiguration> getPaasConfigurationList() {
        logger.info("paasConfigurations.size()="+paasConfigurations.size());

        return    paasConfigurations;
    }

    @Override
    public List<PaasConfiguration> getPaasConfigurationList(String s) throws PaasCatalogException {
        logger.info("paasConfigurations.size()="+paasConfigurations.size());

        return    paasConfigurations;
    }

    @Override
    public PaasConfiguration getPaasConfiguration(String s) throws PaasCatalogException {
        for (PaasConfiguration conf:getPaasConfigurationList()) {
            if (conf.getName().equals(s)) {
                return conf;
            }
        }
        throw new PaasCatalogException("PaaS configuration " + s + "not found");
    }

    @Override
    public String getDefaultPaasConfigurationName() throws PaasCatalogException {
        for (PaasConfiguration conf:getPaasConfigurationList()) {
            if (conf.isDefault()) {
                return conf.getName();
            }

        }
        throw new PaasCatalogException("Default PaaS configuration not found");    }
}
