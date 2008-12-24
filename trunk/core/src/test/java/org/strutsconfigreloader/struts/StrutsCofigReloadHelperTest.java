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
 * History: 2007-4-12 11:42:11 Created by guyang
 */
package org.strutsconfigreloader.struts;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import junit.framework.TestCase;

import org.easymock.classextension.EasyMock;
import org.easymock.classextension.IMocksControl;

/**
 * 
 * History: 2007-4-12 11:42:11Created by guyang
 * @author <a href="mailto:hyysguyang@gmail.com ">guyang</a>
 */
public class StrutsCofigReloadHelperTest extends TestCase {

	IMocksControl classControl = null;

	StrutsConfigReloadHelperStub scrh = null;

	HttpServlet servlet = null;

	/**
	 * @param name
	 */
	public StrutsCofigReloadHelperTest(String name) {
		super(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();
		classControl = EasyMock.createControl();
		servlet = classControl.createMock(HttpServlet.class);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();

		classControl.verify();
		classControl = null;
	}

	public void testAAA() throws ServletException {

		servlet.destroy();
		servlet.init();
		classControl.replay();
		scrh = new StrutsConfigReloadHelperStub(servlet);
		scrh.afterReload();

		assertTrue("Should remove the request processor.", scrh.haveRemoveAllRequestProcessor);
	}

	public void testStrutsCofigReloadHelperNoConfigFile() throws ServletException {

		// servlet.destroy();
		// servlet.init();
		//        
		classControl.replay();
		// scrh=new StrutsConfigReloadHelperStub(servlet);
		// try {
		// scrh.onResourceChange();
		// fail("Should throw RuntimeException");
		// } catch (RuntimeException e) {
		// assertTrue(true);
		// }

	}

	class StrutsConfigReloadHelperStub extends StrutsConfigReloadHelper {

		boolean haveRemoveAllRequestProcessor = false;

		public StrutsConfigReloadHelperStub(HttpServlet actionServlet) {
			super(actionServlet);
		}

		protected void removeAllRequestProcessor() {
			haveRemoveAllRequestProcessor = true;
		}

	}

}
