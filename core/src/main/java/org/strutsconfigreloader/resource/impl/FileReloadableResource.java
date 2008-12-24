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
 * History: 2007-4-1 18:27:59 Created by guyang
 */
package org.strutsconfigreloader.resource.impl;

import java.io.File;

import org.strutsconfigreloader.resource.AbstractResourceReloadable;
import org.strutsconfigreloader.resource.ResourceReloadable;

/**
 * 
 * 
 * History: 2007-4-1 18:27:59Created by guyang
 * @author <a href="mailto:hyysguyang@gmail.com ">guyang</a>
 */
public class FileReloadableResource extends AbstractResourceReloadable implements ResourceReloadable {

	private File file = null;

	private long readResourceTime = 0L;

	public FileReloadableResource(String fileFullPath) {
		file = new File(fileFullPath);
		readResourceTime = file.lastModified();
	}

	public boolean hasModified() {
		return !file.exists() || (file.lastModified() > readResourceTime);
	}

	@Override
	protected void doReload() {
		readResourceTime = file.lastModified();
	}

}
