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
 * History: 2007-4-1 18:20:08 Created by guyang
 */
package org.strutsconfigreloader.resource;

import java.io.File;

import junit.framework.TestCase;

import org.strutsconfigreloader.resource.impl.FileReloadableResource;

/**
 * 
 * History: 2007-4-1 18:20:08Created by guyang
 * @author <a href="mailto:hyysguyang@gmail.com ">guyang</a>
 */
public class RenameReloadableResourceTest extends TestCase {

	private String resourceBaseDir = "/org/strutsconfigreloader/resource/resources/";

	private String filePath = "/test_file_isModifyed_rename_file.txt";

	private ResourceReloadable resource = null;

	private MultiResourcesReloadable rm = null;

	/**
	 * @param arg0
	 */
	public RenameReloadableResourceTest(String arg0) {
		super(arg0);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();

		String filePath = getResourceBaseDir() + "/test_file_isModifyed_rename_file.txt";
		resource = new FileReloadableResource(filePath);

		rm = new AbstractMultiResourcesReloadable() {
		};
		rm.add(new FileReloadableResource(filePath));

		filePath = getResourceBaseDir() + "/test_file_isModifyed_nochange.txt";
		rm.add(new FileReloadableResource(filePath));

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();

		File file = new File(getResourceBaseDir() + filePath);
		File renameFile = new File(getResourceBaseDir() + "/test_rename.txt");

		if (!file.exists() && renameFile.exists()) {
			renameFile.renameTo(file);
		}
	}

	public void testHasModifyedWhenDeleteFile() {

		File file = new File(getResourceBaseDir() + filePath);
		file.renameTo(new File(getResourceBaseDir() + "/test_rename.txt"));

		assertTrue(resource.hasModified());
		assertTrue(rm.hasModified());
	}

	/**
	 * @return
	 */
	private String getResourceBaseDir() {
		return getClass().getResource(resourceBaseDir).getFile();
	}

	// public void testReload() {
	//        
	// IMocksControl control=EasyMock.createNiceControl();
	// control.createMock(ResourceListener.class);
	//        
	// ResourceListener rl1=control.createMock(ResourceListener.class);
	// ResourceListener rl2=control.createMock(ResourceListener.class);
	//        
	// rl1.onResourceChange();
	// rl2.onResourceChange();
	// control.replay();
	//        
	// resource.addListener(rl1);
	// resource.addListener(rl2);
	//        
	//        
	// File file=new File(filePath);
	// long lastModified=file.lastModified();
	// file.setLastModified(lastModified+1000);
	//        
	// resource.reload();
	//        
	// assertEquals(lastModified+1000, file.lastModified());
	// assertFalse(resource.hasModified());
	//        
	// control.verify();
	// }

}
