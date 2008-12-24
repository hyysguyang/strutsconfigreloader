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
 * History: .0 2008-10-29 下午11:35:19 Created by Yang Gu
 */
package org.strutsconfigreloader.resource;

import java.util.ArrayList;
import java.util.List;

/**
 * The abstract single resource reloader. <br>
 * Witch centralize the common process here and give some extend point method
 * for the subclass
 * 
 * 
 * @author <a href="mailto:hyysguyang@gmail.com">Yang Gu</a>
 * @version 1.0 2008-10-29 下午11:35:19
 */
public abstract class AbstractResourceReloadable implements ResourceReloadable {

	protected List<ResourceListener> listeners = new ArrayList<ResourceListener>();

	public void addListener(ResourceListener listener) {
		listeners.add(listener);
	}

	protected void notifyListener() {
		for (ResourceListener listener : listeners) {
			listener.afterReload();
		}
	}

	public void reload() {
		doReload();
		notifyListener();
	}

	/**
	 * The actualy reload the resource action.the subclass thould override this
	 * method.
	 */
	protected abstract void doReload();

}