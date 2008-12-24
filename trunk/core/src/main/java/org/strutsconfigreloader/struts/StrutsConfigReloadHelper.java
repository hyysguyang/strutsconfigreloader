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
 * History: 2007-4-1 20:25:05 Created by guyang
 */
package org.strutsconfigreloader.struts;

import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.struts.Globals;
import org.strutsconfigreloader.resource.ResourceListener;

/**
 * 
 * History: 2007-4-1 20:25:05 Created by guyang
 * 
 * @author <a href="mailto:hyysguyang@gmail.com ">guyang</a>
 */
public class StrutsConfigReloadHelper implements ResourceListener {

	private HttpServlet actionServlet = null;

	/**
	 * @param actionServlet
	 * @param resourceReloader TODO
	 */
	public StrutsConfigReloadHelper(HttpServlet actionServlet) {
		this.actionServlet = actionServlet;
	}

	public void afterReload() {
		getServlet().destroy();

		try {
			getServlet().init();
		}
		catch (ServletException e) {
			throw new RuntimeException(e);
		}

		removeAllRequestProcessor();
	}

	@SuppressWarnings("unchecked")
	protected void removeAllRequestProcessor() {
		Enumeration attributeEnum = getServletContext().getAttributeNames();
		while (attributeEnum.hasMoreElements()) {
			String attributeName = (String) attributeEnum.nextElement();
			if (attributeName.startsWith(Globals.REQUEST_PROCESSOR_KEY)) {
				getServletContext().removeAttribute(attributeName);
			}
		}
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
