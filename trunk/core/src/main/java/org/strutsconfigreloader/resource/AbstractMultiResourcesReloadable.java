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
 * History: 2007-4-2 13:47:32 Created by guyang
 */
package org.strutsconfigreloader.resource;

import java.util.ArrayList;
import java.util.List;

/**
 * Support reloadable abstract resource object <br>
 * Include some common template process function
 * 
 * History: 2007-4-2 13:47:32Created by guyang
 * @author <a href="mailto:hyysguyang@gmail.com ">guyang</a>
 */
public abstract class AbstractMultiResourcesReloadable extends AbstractResourceReloadable implements
		MultiResourcesReloadable {

	protected List<ResourceReloadable> resources = new ArrayList<ResourceReloadable>();

	public void add(ResourceReloadable resource) {
		resources.add(resource);
	}

	public void remove(ResourceReloadable resource) {
		resources.remove(resource);
	}

	public boolean hasModified() {
		for (ResourceReloadable resource : resources) {
			if (resource.hasModified()) {
				return true;
			}
		}
		return false;
	}

	public void doReload() {
		for (ResourceReloadable resource : resources) {
			resource.reload();
		}
	}

}