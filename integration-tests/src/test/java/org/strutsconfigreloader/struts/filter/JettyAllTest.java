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
 * History: 2007-4-2 22:16:55 Created by guyang
 */
package org.strutsconfigreloader.struts.filter;

import java.io.IOException;
import java.util.Properties;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.apache.cactus.extension.jetty.JettyTestSetup;

/**
 * 
 * History: 2007-4-2 22:16:55Created by guyang
 * 
 * @author <a href="mailto:hyysguyang@gmail.com ">guyang</a>
 */
public class JettyAllTest extends TestCase {

	/**
	 * @return a <code>JettyTestSetup</code> test suite that wraps all our tests
	 * so that Jetty will be started before the tests execute
	 * @throws IOException
	 */
	public static Test suite() {
		initSystemProperties();

		TestSuite suite = new TestSuite("Cactus unit tests executing in Jetty");
		// Functional tests
		suite.addTestSuite(StrutsCofigReloadFilterTests.class);
		suite.addTestSuite(StrutsMessageResourceReloadFilterTests.class);
		return new JettyTestSetup(suite);
	}

	private static void initSystemProperties() {
		Properties systemProperies = System.getProperties();
		if (!systemProperies.containsKey("cactus.contextURL")) {
			System.setProperty("cactus.contextURL", "http://localhost:8080/test");
		}
		if (!systemProperies.containsKey("cactus.jetty.resourceDir")) {
			System.setProperty("cactus.jetty.resourceDir", JettyAllTest.class.getResource("/cactus_resource/webapp")
					.getPath());
		}
		if (!systemProperies.containsKey("cactus.jetty.config")) {
			System.setProperty("cactus.jetty.config", JettyAllTest.class.getResource("/cactus_resource/conf/jetty.xml")
					.getPath());
		}
	}
}
