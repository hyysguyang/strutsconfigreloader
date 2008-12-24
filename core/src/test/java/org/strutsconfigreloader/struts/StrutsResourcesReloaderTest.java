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

import java.io.File;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import junit.framework.TestCase;

import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.strutsconfigreloader.common.io.FileUtils;
import org.strutsconfigreloader.resource.MultiResourcesReloadable;
import org.strutsconfigreloader.resource.ResourceListener;
import org.strutsconfigreloader.resource.ResourceReloadable;

/**
 * 
 * History: 2007-4-12 11:42:11Created by guyang
 * @author <a href="mailto:hyysguyang@gmail.com ">guyang</a>
 */
public class StrutsResourcesReloaderTest extends TestCase {

	String resDir = "org/strutsconfigreloader/struts/resources";

	IMocksControl control = null;

	StrutsResourcesReloaderStub loader = null;

	StrutsConfigFileHelper scfh = null;

	private File file1 = null;

	private long file1ModifyTime = 0L;

	private File file2 = null;

	private long file2ModifyTime = 0L;

	/**
	 * @param name
	 */
	public StrutsResourcesReloaderTest(String name) {
		super(name);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	@Override
	protected void setUp() throws Exception {
		super.setUp();

		this.control = EasyMock.createControl();
		this.scfh = this.control.createMock(StrutsConfigFileHelper.class);

		this.file1 = new File(FileUtils.getFullPath(this.resDir + "/test_file_1.txt"));
		this.file1ModifyTime = this.file1.lastModified();

		this.file2 = new File(FileUtils.getFullPath(this.resDir + "/test_file_2.txt"));
		this.file2ModifyTime = this.file2.lastModified();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#tearDown()
	 */
	@Override
	protected void tearDown() throws Exception {
		super.tearDown();

		this.control.verify();
		this.control.reset();

		this.file1.setLastModified(this.file1ModifyTime);
		this.file2.setLastModified(this.file2ModifyTime);
	}

	/**
	 * Test method for
	 * {@link org.strutsconfigreloader.struts.filter.StrutsCofigReloadHelper#StrutsCofigReloadHelper(javax.servlet.http.HttpServlet, org.strutsconfigreloader.struts.filter.StrutsConfigFileHelper, MultiResourcesReloadable)}
	 * .
	 */
	public void testNoConfigFile() {
		Set<String> configs = new TreeSet<String>();
		String file = FileUtils.getFullPath(this.resDir + "/test_file_1.txt");
		configs.add(file);
		file = FileUtils.getFullPath(this.resDir + "/test_file_2.txt");
		configs.add(file);

		EasyMock.expect(this.scfh.getConfigFiles()).andReturn(new TreeSet<String>());
		this.control.replay();

		this.loader = new StrutsResourcesReloaderStub(this.scfh);

		assertEquals("Error counter of resources.", 0, this.loader.getResources().size());
		assertEquals("Error counter of listeners.", 0, this.loader.getListeners().size());
		assertFalse("The resources should be modified state.", this.loader.hasModified());
	}

	/**
	 * Test method for
	 * {@link org.strutsconfigreloader.struts.filter.StrutsCofigReloadHelper#StrutsCofigReloadHelper(javax.servlet.http.HttpServlet, org.strutsconfigreloader.struts.filter.StrutsConfigFileHelper, MultiResourcesReloadable)}
	 * .
	 */
	public void testHaveConfigFile() {
		Set<String> configs = new TreeSet<String>();
		String file = FileUtils.getFullPath(this.resDir + "/test_file_1.txt");
		configs.add(file);
		file = FileUtils.getFullPath(this.resDir + "/test_file_2.txt");
		configs.add(file);

		EasyMock.expect(this.scfh.getConfigFiles()).andReturn(configs);
		this.control.replay();

		this.loader = new StrutsResourcesReloaderStub(this.scfh);

		assertEquals("Error counter of resources.", 2, this.loader.getResources().size());
		assertEquals("Error counter of listeners.", 0, this.loader.getListeners().size());
		assertFalse("The resources should not be modified state.", this.loader.hasModified());
	}

	/**
	 * Test method for
	 * {@link org.strutsconfigreloader.struts.filter.StrutsCofigReloadHelper#StrutsCofigReloadHelper(javax.servlet.http.HttpServlet, org.strutsconfigreloader.struts.filter.StrutsConfigFileHelper, MultiResourcesReloadable)}
	 * .
	 */
	public void testHasModifConfigFile() {
		Set<String> configs = new TreeSet<String>();
		configs.add(this.file1.getPath());
		configs.add(this.file2.getPath());
		String file = FileUtils.getFullPath(this.resDir + "/test_file_3.txt");
		configs.add(file);

		EasyMock.expect(this.scfh.getConfigFiles()).andReturn(configs);
		this.control.replay();

		this.loader = new StrutsResourcesReloaderStub(this.scfh);
		StrutsCofigReloadHelperStub listener = new StrutsCofigReloadHelperStub();
		this.loader.addListener(listener);

		assertEquals(1, this.loader.getListeners().size());
		assertSame("Error listener", listener, this.loader.getListeners().get(0));
		assertFalse("The resources should not be modified state.", this.loader.hasModified());

		this.file1.setLastModified(this.file1ModifyTime + 5000);
		this.file2.setLastModified(this.file2ModifyTime + 5000);

		assertTrue("The resources should be modified state.", this.loader.hasModified());

		this.loader.reload();

		assertFalse("The resources should not be modified state after reload.", this.loader.hasModified());

		assertEquals("The listener should be called olny one times", 1, listener.callConter);

	}

	class StrutsCofigReloadHelperStub implements ResourceListener {
		int callConter = 0;

		public void afterReload() {
			this.callConter++;
		}
	}

	private class StrutsResourcesReloaderStub extends StrutsResourcesReloader {
		public StrutsResourcesReloaderStub(StrutsConfigFileHelper scfh) {
			super(scfh);
		}

		public List<ResourceListener> getListeners() {
			return this.listeners;
		}

		public List<ResourceReloadable> getResources() {
			return this.resources;
		}
	}

}
