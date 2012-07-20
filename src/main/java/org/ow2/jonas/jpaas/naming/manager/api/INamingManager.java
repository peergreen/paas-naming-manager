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

package org.ow2.jonas.jpaas.naming.manager.api;

/**
 * Interface for the Naming Manager.
 * @author David Richard
 */
public interface INamingManager {

    /**
     * Get a new name for a Container
     * @param paasConfigurationName the name of the PaaS Configuration
     * @return a name
     */
    public String getNewContainerName(String paasConfigurationName) throws NamingManagerException;

    /**
     * Get a new name for a Router
     * @param paasConfigurationName the name of the PaaS Configuration
     * @return a name
     */
    public String getNewRouterName(String paasConfigurationName) throws NamingManagerException;

    /**
     * Get a new name for an Agent
     * @param iaasComputeName the name of the IaasCompute
     * @return a name
     */
    public String getNewAgentName(String iaasComputeName) throws NamingManagerException;

    /**
     * Get a new name for a Compute
     * @param iaasConfigurationName the name of the IaaS Configuration
     * @return a name
     */
    public String getNewComputeName(String iaasConfigurationName) throws NamingManagerException;

    /**
     * Get the default Vhost name
     * @param appInstanceName the Application Instance name
     * @return the default name
     */
    public String getDefaultVhostName(String appInstanceName);

    /**
     * Get the default Connector name
     * @param routerName the Router name
     * @param containerName The Container name
     * @return the default name
     */
    public String getDefaultConnectorName(String routerName, String containerName);

    /**
     * Get the default Worker name
     * @param routerName the Router name
     * @param containerName The Container name
     * @return the default name
     */
    public String getDefaultWorkerName(String routerName, String containerName);

    /**
     * Get the default Lb name
     * @param appInstanceName the Application Instance name
     * @return the default name
     */
    public String getDefaultLbName(String appInstanceName);

    /**
     * Get the default Datasource name
     * @param datasourceConf the Datasource configuration name
     * @return the default name
     */
    public String getDefaultDatasourceName(String datasourceConf);

}
