package org.ow2.jpaas.naming.manager.tests;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.ops4j.pax.exam.Configuration;
import org.ops4j.pax.exam.Option;
import org.ops4j.pax.exam.junit.PaxExam;
import org.ops4j.pax.exam.spi.reactors.ExamReactorStrategy;
import org.ops4j.pax.exam.spi.reactors.PerClass;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.ow2.jonas.jpaas.catalog.api.IIaasCatalogFacade;
import org.ow2.jonas.jpaas.catalog.api.IaasCatalogException;
import org.ow2.jonas.jpaas.catalog.api.IaasConfiguration;
import org.ow2.jonas.jpaas.naming.manager.api.INamingManager;
import org.ow2.jonas.jpaas.naming.manager.api.NamingManagerException;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;

import static org.junit.Assert.*;
import static org.ops4j.pax.exam.CoreOptions.*;

@RunWith(PaxExam.class)
@ExamReactorStrategy(PerClass.class)
public class NamingManagerTest {

    public static final String PROJECT_VERSION = "1.0.0-M1-SNAPSHOT";


    @Inject
    BundleContext context;

    @Inject
    private INamingManager namingManager;

    @Inject
    private IIaasCatalogFacade catalog;

    @Configuration
    public Option[] config() {

        // Reduce log level.
        Logger root = (Logger) LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);
        root.setLevel(Level.INFO);

        return options(systemProperty("org.ops4j.pax.logging.DefaultServiceLog.level").value("WARN"),
                mavenBundle("com.peergreen.paas", "paas-system-representation-vo").version(PROJECT_VERSION),
                mavenBundle("com.peergreen.paas", "paas-system-representation-api").version(PROJECT_VERSION),
                mavenBundle("com.peergreen.paas", "paas-catalog-api").version(PROJECT_VERSION),
                mavenBundle("com.peergreen.paas", "paas-naming-manager-external-mocks").version(PROJECT_VERSION),
                mavenBundle("com.peergreen.paas", "paas-naming-manager-ejb").version(PROJECT_VERSION),
                junitBundles());
    }

    @Test
    public void checkInjectContext() {
        assertNotNull(context);
    }

    @Test
    public void checkInjectNamingManager() {
        assertNotNull(namingManager);
    }

    @Test
    public void checkBundleNamingManager() {
        Boolean found = false;
        Boolean active = false;
        Bundle[] bundles = context.getBundles();
        for (Bundle bundle : bundles) {
            if (bundle != null) {
                if (bundle.getSymbolicName().equals("com.peergreen.paas.naming-manager-ejb")) {
                    found = true;
                    if (bundle.getState() == Bundle.ACTIVE) {
                        active = true;
                    }
                }
            }
        }
        assertTrue(found);
        assertTrue(active);
    }

    @Test
    public void getNewContainerName() {
        String name = null;

        try {
            name = namingManager.getNewContainerName("mycontainer");
        } catch (NamingManagerException e) {
            fail(e.getMessage());
        }

        System.out.println("name=" + name);
        assertNotNull(name);
        assertTrue(name.startsWith("CONTAINERPAAS-"));

    }

    @Test
    public void getNewRouterName() {
        String name = null;

        try {
            name = namingManager.getNewRouterName("myrouter");
        } catch (NamingManagerException e) {
            fail(e.getMessage());
        }

        System.out.println("name=" + name);
        assertNotNull(name);
        assertTrue(name.startsWith("ROUTERPAAS-"));

    }

    @Test
    public void getNewAgentName() {
        String name = null;

        try {
            name = namingManager.getNewAgentName("mycompute");
        } catch (NamingManagerException e) {
            fail(e.getMessage());
        }

        System.out.println("name=" + name);
        assertNotNull(name);
        assertTrue(name.startsWith("agent-mycompute-"));

    }

    @Test
    public void getNewComputeName() {
        String name = null;

        try {
            name = namingManager.getNewComputeName("mycompute");
        } catch (NamingManagerException e) {
            fail(e.getMessage());
        }

        System.out.println("name=" + name);
        assertNotNull(name);
        assertTrue(name.startsWith("COMPUTEIAAS-"));

    }

    @Test
    public void getDefaultVhostName() {
        String name = null;
        name = namingManager.getDefaultVhostName("myapp");
        System.out.println("name=" + name);
        assertNotNull(name);
        assertTrue(name.endsWith(".jpaas.org"));
    }

    @Test
    public void getDefaultConnectorName() {
        String name = null;
        name = namingManager.getDefaultConnectorName("myrouter", "mycontainer");
        System.out.println("name=" + name);
        assertNotNull(name);
        assertTrue(name.startsWith("connector-"));
    }

    @Test
    public void getDefaultWorkerName() {
        String name = null;
        name = namingManager.getDefaultWorkerName("myrouter", "mycontainer");
        System.out.println("name=" + name);
        assertNotNull(name);
        assertTrue(name.startsWith("wk-"));
    }

    @Test
    public void getDefaultLbName() {
        String name = null;
        name = namingManager.getDefaultLbName("myapp");
        System.out.println("name=" + name);
        assertNotNull(name);
        assertTrue(name.startsWith("lb-"));
    }


    @Test
    public void getDefaultDatasourceName() {
        String name = null;
        name = namingManager.getDefaultDatasourceName("myds");
        System.out.println("name=" + name);
        assertNotNull(name);
        assertTrue(name.startsWith("datasource-"));
    }

}
