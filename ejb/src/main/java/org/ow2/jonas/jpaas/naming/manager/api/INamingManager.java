/**
 * JPaaS
 * Copyright 2012 Bull S.A.S.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * $Id:$
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
