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
 * History: 2007-4-1 19:56:02 Created by guyang
 */
package org.strutsconfigreloader.struts;

import java.util.Set;

import org.strutsconfigreloader.resource.AbstractMultiResourcesReloadable;
import org.strutsconfigreloader.resource.ResourceReloadable;
import org.strutsconfigreloader.resource.impl.FileReloadableResource;

/**
 * Default support multiply resource reload object
 * 
 * History: 2007-4-1 19:56:02Created by guyang
 * @author <a href="mailto:hyysguyang@gmail.com ">guyang</a>
 */
public class StrutsResourcesReloader extends AbstractMultiResourcesReloadable {

	private StrutsConfigFileHelper scfh = null;

	public StrutsResourcesReloader(StrutsConfigFileHelper scfh) {
		this.scfh = scfh;
		init();
	}

	protected void init() {
		Set<String> configs = this.scfh.getConfigFiles();
		for (String config : configs) {
			ResourceReloadable resource = new FileReloadableResource(config);
			this.add(resource);
		}
	}

}