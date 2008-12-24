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
 * History: 2007-4-1 19:46:23 Created by guyang
 */
package org.strutsconfigreloader.resource;

import java.io.File;

import junit.framework.TestCase;

import org.strutsconfigreloader.resource.impl.FileReloadableResource;

/**
 * 
 * History: 2007-4-1 19:46:23Created by guyang
 * 
 * @author <a href="mailto:hyysguyang@gmail.com ">guyang</a>
 */
public class MultiResourcesReloadableTest extends TestCase {

	private String resourceBaseDir = "/org/strutsconfigreloader/resource/resources/";

	private MultiResourcesReloadable rm = null;

	private String filePath = null;

	/**
	 * @param arg0
	 */
	public MultiResourcesReloadableTest(String arg0) {
		super(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();

		rm = new AbstractMultiResourcesReloadable() {
		};

		createTestResources();
	}

	/**
     * 
     */
	private void createTestResources() {
		String fileClasspath = resourceBaseDir + "test_file_isModifyed_nochange.txt";
		filePath = getClass().getResource(fileClasspath).getFile();
		fileClasspath = resourceBaseDir + "test_file_isModifyed_1.txt";
		String filePath2 = getClass().getResource(fileClasspath).getFile();

		ResourceReloadable resource = new FileReloadableResource(filePath);
		rm.add(resource);
		resource = new FileReloadableResource(filePath2);
		rm.add(resource);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testHasModifyWhenNoModify() {
		assertFalse(rm.hasModified());

	}

	public void testHasModify() {

		modifyFile();

		assertTrue(rm.hasModified());
	}

	/**
     * 
     */
	private void modifyFile() {
		File file = new File(filePath);
		long lastModified = file.lastModified();
		file.setLastModified(lastModified + 1000);
	}

	public void testReload() {
		modifyFile();
		rm.reload();
		assertFalse(rm.hasModified());
	}

}
