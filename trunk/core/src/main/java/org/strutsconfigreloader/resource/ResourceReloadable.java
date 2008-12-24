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
 * History: 2007-4-1 18:27:35 Created by guyang
 */
package org.strutsconfigreloader.resource;

/**
 * Reloadable resouce interface </p> Note: implement
 * {@link MultiResourcesReloadable} for multiply resources reload.
 * 
 * History: 2007-4-1 18:27:35 Created by guyang
 * @author <a href="mailto:hyysguyang@gmail.com ">guyang</a>
 * {@link MultiResourcesReloadable}
 */
public interface ResourceReloadable {

	/**
	 * Detect whether or not the resource had been modified.
	 * 
	 * @return
	 */
	boolean hasModified();

	/**
	 * Reload the resource. <br>
	 *The caller should call the {@link #hasModified} to check if the resource
	 * required to reload.
	 * 
	 */
	void reload();

	/**
	 * Add listener to this resource. <br>
	 * It's a hook for the client to do something whe reload this resource.
	 * @param resourceListener
	 */
	void addListener(ResourceListener resourceListener);

}
