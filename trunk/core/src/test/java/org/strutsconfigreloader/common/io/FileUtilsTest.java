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
 * History: 2007-2-3 05:52:55 Created by guyang
 */
package org.strutsconfigreloader.common.io;

import java.io.File;

import junit.framework.TestCase;

/**
 * 
 * History: 2007-2-3 05:52:55Created by guyang
 * @author <a href="mailto:hyysguyang@gmail.com ">guyang</a>
 */
public class FileUtilsTest extends TestCase {

	File resourcesFile = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	protected void setUp() throws Exception {
		super.setUp();

		String file = getClass().getResource("/org/strutsconfigreloader/common/io/resources").getPath();
		resourcesFile = new File(file + "/resources");
		resourcesFile.mkdir();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();

		FileUtils.deleteFileRecursive(resourcesFile);
	}

	public void testCreateSubFile() {
		assertTrue(FileUtils.createSubFile(resourcesFile, "createSubFile.txt"));
		assertTrue(new File(resourcesFile.getPath() + "/createSubFile.txt").exists());
		assertFalse(FileUtils.createSubFile(resourcesFile, "createSubFile.txt"));
	}

	public void testDeleteFileRecursive() {

		File tempFile = new File(resourcesFile.getPath() + "/temp_file");
		tempFile.mkdir();

		FileUtils.createSubFile(tempFile, "createSubFile.txt");

		FileUtils.deleteFileRecursive(tempFile);
		assertFalse(tempFile.exists());
	}

	public void testRenameFilesWhenNofile() {
		assertEquals(0, FileUtils.renameFiles(resourcesFile.getPath(), "txt", "cpp").length);
	}

	public void testRenameFilesWhenHaveSubDirButNofile() {
		String testFilePath = resourcesFile.getPath() + "/have_sub_folder";
		new File(testFilePath).mkdir();
		assertEquals(0, FileUtils.renameFiles(resourcesFile.getPath(), "txt", "cpp").length);
	}

	public void testRenameFilesWhenHaveOneSubFile() {

		String testFilePath = resourcesFile.getPath() + "/have_sub_folder";
		new File(testFilePath).mkdir();

		FileUtils.createSubFile(resourcesFile, "test.txt");

		assertEquals(0, FileUtils.renameFiles(resourcesFile.getPath(), "txt", "cpp").length);

		File testFile = new File(resourcesFile.getPath() + "/test.cpp");
		assertTrue(testFile.exists());

		testFile = new File(resourcesFile.getPath() + "/test.txt");
		assertFalse(testFile.exists());

	}

	public void testRenameFilesWhenHaveManyFile() {
		String file = resourcesFile.getPath();

		FileUtils.createSubFile(resourcesFile, "test.txt");
		FileUtils.createSubFile(resourcesFile, "another_test.txt");
		FileUtils.createSubFile(resourcesFile, "third_test.aaa");

		assertEquals(0, FileUtils.renameFiles(file, "txt", "cpp").length);

		File testFile = new File(file + "/test.cpp");
		assertTrue(testFile.exists());
		testFile = new File(file + "/test.txt");
		assertFalse(testFile.exists());

		testFile = new File(file + "/another_test.cpp");
		assertTrue(testFile.exists());
		testFile = new File(file + "/another_test.txt");
		assertFalse(testFile.exists());

		testFile = new File(file + "/third_test.cpp");
		assertFalse(testFile.exists());
		testFile = new File(file + "/third_test.aaa");
		assertTrue(testFile.exists());

	}

	public void testRenameFilesWhenHaveSubDirAndFile() {
		String testFilePath = resourcesFile.getPath() + "/have_sub_folder";
		File subdir = new File(testFilePath);
		subdir.mkdir();

		FileUtils.createSubFile(subdir, "sub_dir_test.txt");
		FileUtils.createSubFile(resourcesFile, "test.txt");

		assertEquals(0, FileUtils.renameFiles(resourcesFile.getPath(), "txt", "cpp").length);

		File testFile = new File(resourcesFile.getPath() + "/test.cpp");
		assertTrue(testFile.exists());
		testFile = new File(resourcesFile.getPath() + "/test.txt");
		assertFalse(testFile.exists());

		testFile = new File(subdir.getPath() + "/sub_dir_test.cpp");
		assertTrue(testFile.exists());
		testFile = new File(subdir.getPath() + "/sub_dir_test.txt");
		assertFalse(testFile.exists());
	}

	public void testGetFullPathNull() {
		assertNotNull(FileUtils.getFullPath(null));
	}

	public void testGetFullPathEmpty() {
		assertNotNull(FileUtils.getFullPath(""));
	}

	public void testGetFullPathNullImputShouldEqualEmptyInput() {
		assertTrue(FileUtils.getFullPath(null).equals(FileUtils.getFullPath("")));
	}

	public void testGetFullPathClassPath() {
		String classPath = "org/strutsconfigreloader/common/io/resources";
		String actualPath = FileUtils.getFullPath(classPath);

		assertTrue(actualPath.endsWith(classPath));
		assertTrue(new File(actualPath).exists());
	}

}
