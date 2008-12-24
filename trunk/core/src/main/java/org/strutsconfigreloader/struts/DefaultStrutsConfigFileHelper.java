/**
 * Copyright 2004-2006 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * History: 2007-4-12 ����10:46:10 Created by guyang
 */
package org.strutsconfigreloader.struts;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServlet;

import org.apache.struts.Globals;
import org.apache.struts.config.MessageResourcesConfig;
import org.apache.struts.config.ModuleConfig;
import org.apache.struts.config.PlugInConfig;
import org.strutsconfigreloader.common.io.FileUtils;

/**
 * Struts config default implement helper.
 * 
 * History: 2007-4-12 ����10:46:10 Created by guyang
 * 
 * @author <a href="mailto:hyysguyang@gmail.com ">guyang</a>
 */
@SuppressWarnings("unchecked")
public class DefaultStrutsConfigFileHelper implements StrutsConfigFileHelper {

	private static final String STRUTS_CONFIG_FILE = "/WEB-INF/struts-config.xml";

	/**
	 * struts action
	 */
	private HttpServlet actionServlet = null;

	/**
	 * @param actionServlet
	 */
	public DefaultStrutsConfigFileHelper(HttpServlet actionServlet) {
		this.actionServlet = actionServlet;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.strutsconfigreloader.struts.filter.StrutsConfigFileHelper#
	 * getReloadableConfigFiles()
	 */
	public Set<String> getConfigFiles() {
		Set<String> configs = new TreeSet<String>();

		configs.addAll(this.getStrutsConfigFiles());
		configs.addAll(this.getMessageResourcesPath());
		configs.addAll(this.getPluginConfigFilePath());
		return configs;
	}

	protected Set<String> getStrutsConfigFiles() {
		Set<String> configs = new TreeSet<String>();
		Enumeration<String> paramEnum = getServlet().getInitParameterNames();
		while (paramEnum.hasMoreElements()) {
			String paramKey = paramEnum.nextElement();
			if (paramKey.startsWith("config")) {
				configs.addAll(getModuleStrtutsConfigFile(paramKey));
			}
		}

		return configs;
	}

	/**
	 * @return
	 */
	protected Set<String> getMessageResourcesPath() {

		List<String> attributeNameList = getStartsWithAttributeNames(Globals.MODULE_KEY);

		Set<String> resourcesPath = new TreeSet<String>();
		for (String attributeName : attributeNameList) {
			resourcesPath.addAll(getModuleMessageResourcesPath(attributeName));
		}

		return resourcesPath;
	}

	/**
	 * @param attributeName
	 * @return TODO
	 */
	private Set<String> getModuleMessageResourcesPath(String attributeName) {
		Set<String> resourcesPath = new TreeSet<String>();
		ModuleConfig config = getModuleConfig(attributeName);
		MessageResourcesConfig[] mrc = config.findMessageResourcesConfigs();
		for (MessageResourcesConfig config2 : mrc) {
			String classFileName = config2.getParameter().replace('.', '/') + ".properties";
			String resourceFile = FileUtils.getFullPath(classFileName);
			resourcesPath.add(resourceFile);
		}
		return resourcesPath;
	}

	/**
	 * @param attributeName
	 * @return
	 */
	private ModuleConfig getModuleConfig(String attributeName) {
		return (ModuleConfig) (getServletContext().getAttribute(attributeName));
	}

	private Set<String> getModuleStrtutsConfigFile(String paramKey) {
		Set<String> configs = new TreeSet<String>();
		String paramValue = STRUTS_CONFIG_FILE;
		paramValue = getServlet().getInitParameter(paramKey);
		String[] configFilePaths = paramValue.split(",");
		for (String configFilePath : configFilePaths) {
			configs.add(getWebRootPath() + configFilePath);
		}
		return configs;
	}

	/**
	 * @return
	 */
	protected Set<String> getPluginConfigFilePath() {
		List<String> attributeNameList = getStartsWithAttributeNames(Globals.MODULE_KEY);

		Set<String> resourcesPath = new TreeSet<String>();
		for (String attributeName : attributeNameList) {
			resourcesPath.addAll(getModulePlginConfigFiles(attributeName));
		}

		return resourcesPath;
	}

	/**
	 * @param attributeNamePrefix TODO
	 * @return
	 */
	private List<String> getStartsWithAttributeNames(String attributeNamePrefix) {
		List<String> attributeNameList = new ArrayList<String>();
		Enumeration<String> attributeEnum = getServletContext().getAttributeNames();
		while (attributeEnum.hasMoreElements()) {
			String attributeName = attributeEnum.nextElement();
			if (attributeName.startsWith(attributeNamePrefix)) {
				attributeNameList.add(attributeName);
			}
		}
		return attributeNameList;
	}

	/**
	 * @param attributeName
	 * @return TODO
	 */
	private Set<String> getModulePlginConfigFiles(String attributeName) {

		Set<String> resourcesPath = new TreeSet<String>();

		ModuleConfig config = getModuleConfig(attributeName);
		PlugInConfig plugInConfigs[] = config.findPlugInConfigs();
		for (PlugInConfig config2 : plugInConfigs) {
			Set<Map.Entry> set = config2.getProperties().entrySet();
			for (Map.Entry entry : set) {
				if (entry.getValue() instanceof String) {
					String value = (String) entry.getValue();

					String[] cofingValues = value.split(",");
					for (String configFile : cofingValues) {
						if (isPluginConfigFilePath(configFile)) {
							resourcesPath.add(getWebRootPath() + configFile);
						}
					}

				}

			}

		}
		return resourcesPath;
	}

	/**
	 * @return
	 */
	private String getWebRootPath() {
		return getServletContext().getRealPath("/");
	}

	/**
	 * @param configFile
	 * @return
	 */
	private boolean isPluginConfigFilePath(String configFile) {
		return configFile.indexOf("/") >= 0;
	}

	/**
	 * @return
	 */
	private ServletContext getServletContext() {
		return getServlet().getServletContext();
	}

	/**
	 * @return
	 */
	private HttpServlet getServlet() {
		return actionServlet;
	}

}
