/**
 * JPaaS
 * Copyright (C) 2012 Bull S.A.S.
 * Contact: jasmine@ow2.org
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307
 * USA
 *
 * --------------------------------------------------------------------------
 * $Id$
 * --------------------------------------------------------------------------
 */

package org.ow2.jonas.jpaas.naming.manager.bean;

import org.ow2.easybeans.osgi.annotation.OSGiResource;
import org.ow2.jonas.jpaas.catalog.api.IIaasCatalogFacade;
import org.ow2.jonas.jpaas.catalog.api.IPaasCatalogFacade;
import org.ow2.jonas.jpaas.catalog.api.IaasCatalogException;
import org.ow2.jonas.jpaas.catalog.api.IaasConfiguration;
import org.ow2.jonas.jpaas.catalog.api.PaasCatalogException;
import org.ow2.jonas.jpaas.catalog.api.PaasConfiguration;
import org.ow2.jonas.jpaas.naming.manager.api.INamingManager;
import org.ow2.jonas.jpaas.naming.manager.api.NamingManagerException;
import org.ow2.jonas.jpaas.naming.manager.entity.AgentSequence;
import org.ow2.jonas.jpaas.naming.manager.entity.ComputeSequence;
import org.ow2.jonas.jpaas.naming.manager.entity.ContainerSequence;
import org.ow2.jonas.jpaas.naming.manager.entity.RouterSequence;
import org.ow2.jonas.jpaas.sr.facade.api.ISrIaasComputeFacade;
import org.ow2.jonas.jpaas.sr.facade.vo.IaasComputeVO;

import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Startup;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 * Naming Manager Bean.
 * @author David Richard
 */
@Stateless(mappedName = "NamingManagerBean")
@Remote(INamingManager.class)
@Local(INamingManager.class)
public class NamingManagerBean implements INamingManager {

    @OSGiResource
    IPaasCatalogFacade iPaasCatalogFacade;

    @OSGiResource
    IIaasCatalogFacade iIaasCatalogFacade;

    @OSGiResource
    ISrIaasComputeFacade iSrIaasComputeFacade;

    @PersistenceContext
    EntityManager entityManager;


    /**
     * Get a new name for a Container
     *
     * @param paasConfigurationName the name of the PaaS Configuration
     * @return a name
     * @throws NamingManagerException
     */
    @Override
    public String getNewContainerName(String paasConfigurationName) throws NamingManagerException {
        try {
            PaasConfiguration paasConfiguration = iPaasCatalogFacade.getPaasConfiguration(paasConfigurationName);
            String prefix = paasConfiguration.getPrefixResourceName();
            String sequence = getContainerNextSequence(paasConfigurationName);
            return prefix + "-" + sequence;
        } catch (PaasCatalogException e) {
            throw new NamingManagerException("Error to find the PaaS Configuration named " +
                    paasConfigurationName + ".", e.getCause());
        }
    }

    /**
     * Get a new name for a Router
     *
     * @param paasConfigurationName the name of the PaaS Configuration
     * @return a name
     * @throws NamingManagerException
     */
    @Override
    public String getNewRouterName(String paasConfigurationName) throws NamingManagerException {
        try {
            PaasConfiguration paasConfiguration = iPaasCatalogFacade.getPaasConfiguration(paasConfigurationName);
            String prefix = paasConfiguration.getPrefixResourceName();
            String sequence = getRouterNextSequence(paasConfigurationName);
            return prefix + "-" + sequence;
        } catch (PaasCatalogException e) {
            throw new NamingManagerException("Error to find the PaaS Configuration named " +
                    paasConfigurationName + ".", e.getCause());
        }
    }

    /**
     * Get a new name for an Agent
     *
     * @param iaasComputeName the name of the IaasCompute
     * @return a name
     * @throws NamingManagerException
     */
    @Override
    public String getNewAgentName(String iaasComputeName) throws NamingManagerException {
        List<IaasComputeVO> iaasComputeList = iSrIaasComputeFacade.findIaasComputes();
        boolean isComputeExist = false;
        for (IaasComputeVO tmp : iaasComputeList) {
            if (tmp.getName().equals(iaasComputeName)) {
                isComputeExist = true;
                break;
            }
        }
        if (isComputeExist) {
            String sequence = getAgentNextSequence(iaasComputeName);
            return "agent-" + iaasComputeName + "-" + sequence;
        } else {
            throw new NamingManagerException("The IaaS Compute named " + iaasComputeName + " does not exist.");
        }
    }

    /**
     * Get a new name for a Compute
     *
     * @param iaasConfigurationName the name of the IaaS Configuration
     * @return a name
     */
    @Override
    public String getNewComputeName(String iaasConfigurationName) throws NamingManagerException {
        try {
            IaasConfiguration iaasConfiguration = iIaasCatalogFacade.getIaasConfiguration(iaasConfigurationName);
            String prefix = iaasConfiguration.getPrefixResourceName();
            String sequence = getComputeNextSequence(iaasConfigurationName);
            return prefix + "-" + sequence;
        } catch (IaasCatalogException e) {
            throw new NamingManagerException("Error to find the IaaS Configuration named " +
                    iaasConfigurationName + ".", e.getCause());
        }
    }

    /**
     * Get the default Vhost name
     *
     * @param appInstanceName the Application Instance name
     * @return the default name
     */
    @Override
    public String getDefaultVhostName(String appInstanceName) {
        return appInstanceName + ".jpaas.org";
    }

    /**
     * Get the default Connector name
     *
     * @param routerName    the Router name
     * @param containerName The Container name
     * @return the default name
     */
    @Override
    public String getDefaultConnectorName(String routerName, String containerName) {
        return "connector-" + routerName;
    }

    /**
     * Get the default Worker name
     *
     * @param routerName    the Router name
     * @param containerName The Container name
     * @return the default name
     */
    @Override
    public String getDefaultWorkerName(String routerName, String containerName) {
        return "wk-" + containerName;
    }

    /**
     * Get the default Lb name
     *
     * @param appInstanceName the Application Instance name
     * @return the default name
     */
    @Override
    public String getDefaultLbName(String appInstanceName) {
        return "lb-" + appInstanceName;
    }

    /**
     * Get the default Datasource name
     *
     * @param datasourceConf the Datasource configuration name
     * @return the default name
     */
    @Override
    public String getDefaultDatasourceName(String datasourceConf) {
        return "datasource-" + datasourceConf;
    }


    /**
     * Get the next sequence for a Container
     * @param paasConfigurationName
     * @return the next sequence
     */
    private String getContainerNextSequence(String paasConfigurationName) {
        ContainerSequence containerSequence = entityManager.find(ContainerSequence.class, paasConfigurationName);
        if (containerSequence == null) {
            containerSequence = new ContainerSequence();
            containerSequence.setPaasConfigurationName(paasConfigurationName);
            containerSequence.setLastSequence(0L);
            entityManager.persist(containerSequence);
        }
        long lastSequence = containerSequence.getLastSequence();
        long nextSequence = lastSequence + 1;
        containerSequence.setLastSequence(nextSequence);
        entityManager.merge(containerSequence);
        return String.valueOf(nextSequence);
    }

    /**
     * Get the next sequence for a Router
     * @param paasConfigurationName
     * @return the next sequence
     */
    private String getRouterNextSequence(String paasConfigurationName) {
        RouterSequence routerSequence = entityManager.find(RouterSequence.class, paasConfigurationName);
        if (routerSequence == null) {
            routerSequence = new RouterSequence();
            routerSequence.setPaasConfigurationName(paasConfigurationName);
            routerSequence.setLastSequence(0L);
            entityManager.persist(routerSequence);
        }
        long lastSequence = routerSequence.getLastSequence();
        long nextSequence = lastSequence + 1;
        routerSequence.setLastSequence(nextSequence);
        entityManager.merge(routerSequence);
        return String.valueOf(nextSequence);
    }

    /**
     * Get the next sequence for a Agent
     * @param vmComputeName
     * @return the next sequence
     */
    private String getAgentNextSequence(String vmComputeName) {
        AgentSequence agentSequence = entityManager.find(AgentSequence.class, vmComputeName);
        if (agentSequence == null) {
            agentSequence = new AgentSequence();
            agentSequence.setVmComputeName(vmComputeName);
            agentSequence.setLastSequence(0L);
            entityManager.persist(agentSequence);
        }
        long lastSequence = agentSequence.getLastSequence();
        long nextSequence = lastSequence + 1;
        agentSequence.setLastSequence(nextSequence);
        entityManager.merge(agentSequence);
        return String.valueOf(nextSequence);
    }
    
    /**
     * Get the next sequence for a Compute
     * @param iaasConfigurationName
     * @return the next sequence
     */
    private String getComputeNextSequence(String iaasConfigurationName) {
        ComputeSequence computeSequence = entityManager.find(ComputeSequence.class, iaasConfigurationName);
        if (computeSequence == null) {
            computeSequence = new ComputeSequence();
            computeSequence.setIaasConfigurationName(iaasConfigurationName);
            computeSequence.setLastSequence(0L);
            entityManager.persist(computeSequence);
        }
        long lastSequence = computeSequence.getLastSequence();
        long nextSequence = lastSequence + 1;
        computeSequence.setLastSequence(nextSequence);
        entityManager.merge(computeSequence);
        return String.valueOf(nextSequence);
    }


}
