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
 * History: 2007-4-2 22:16:55 Created by guyang
 */
package org.strutsconfigreloader.struts.filter;

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.cactus.FilterTestCase;
import org.easymock.EasyMock;
import org.easymock.IMocksControl;
import org.strutsconfigreloader.resource.ResourceListener;
import org.strutsconfigreloader.resource.ResourceReloadable;
import org.strutsconfigreloader.struts.DefaultStrutsConfigFileHelper;
import org.strutsconfigreloader.struts.StrutsConfigFileHelper;
import org.strutsconfigreloader.struts.StrutsResourcesReloader;


/**
 *
 * History: 2007-4-2 22:16:55Created by guyang
 *
 * @author <a href="mailto:hyysguyang@gmail.com ">guyang</a>
 */
public class StrutsMessageResourceReloadFilterTests extends FilterTestCase {

    private IMocksControl control = null;

    private FilterChain fc = null;

    private StrutsConfigReloadFilter filter = null;

    private File messageResourcesFile = null;

    private long resourcesModifyTime = 0L;

    private String webroot = System.getProperty("cactus.jetty.resourceDir");

    /*
     * (non-Javadoc)
     *
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.filter = new StrutsConfigReloadFilterStub();
        this.fc = createMockFilterChain();
    }

    /*
     * (non-Javadoc)
     *
     * @see junit.framework.TestCase#tearDown()
     */
    @Override
    protected void tearDown() throws Exception {
        super.tearDown();

        this.messageResourcesFile.setLastModified(this.resourcesModifyTime);

        this.control = null;
        this.fc = null;
        this.filter = null;
        this.messageResourcesFile = null;
    }


    /**
     * Test method for
     * {@link StrutsConfigReloadFilter#init(javax.servlet.FilterConfig)}.
     *
     * @throws ServletException
     */
    public void testDoFilterWhenMessageResourcesHasModified() throws Exception {
        this.messageResourcesFile = new File(this.webroot + "/WEB-INF/classes/MessageResources.properties");
        this.resourcesModifyTime = this.messageResourcesFile.lastModified();


        executeTestBody();

    }

    /**
     * Test method for
     * {@link StrutsConfigReloadFilter#init(javax.servlet.FilterConfig)}.
     *
     * @throws ServletException
     */
    public void testDoFilterWhenLongPathMessageResourcesHasModified() throws Exception {

        this.messageResourcesFile = new File(this.webroot + "/WEB-INF/classes/user/MessageResources.properties");
        this.resourcesModifyTime = this.messageResourcesFile.lastModified();

        executeTestBody();

    }


    /**
     * Test method for
     * {@link StrutsConfigReloadFilter#init(javax.servlet.FilterConfig)}.
     *
     * @throws ServletException
     */
    public void testDoFilterValidatorPluginConfigHasModified() throws Exception {

        this.messageResourcesFile = new File(this.webroot + "/WEB-INF/test-validation.xml");
        this.resourcesModifyTime = this.messageResourcesFile.lastModified();

        executeTestBody();

    }

    /**
     * @throws ServletException
     * @throws IOException
     */
    private void executeTestBody() throws ServletException, IOException {
        this.filter.init(this.config);
        this.filter.doFilter(this.request, this.response, this.fc);

        StrutsResourcesReloader rd = assertFirstFilter();


        this.messageResourcesFile.setLastModified(this.resourcesModifyTime + 5000);

        assertTrue(rd.hasModified());

        this.filter.doFilter(this.request, this.response, this.fc);

        assertFalse(rd.hasModified());

        this.control.verify();
    }


    /**
     * Test method for
     * {@link StrutsConfigReloadFilter#init(javax.servlet.FilterConfig)}.
     *
     * @throws ServletException
     */
    public void testDoFilterTilesPluginConfigHasModified() throws Exception {

        this.messageResourcesFile = new File(this.webroot + "/WEB-INF/test-tiles-defs.xml");
        this.resourcesModifyTime = this.messageResourcesFile.lastModified();

        executeTestBody();

    }


    /**
     * @return
     */
    private StrutsResourcesReloader assertFirstFilter() {
        StrutsResourcesReloaderStub rd = (StrutsResourcesReloaderStub) this.filter.getResourceReloader();
        List<ResourceListener> listeners = rd.getListeners();
        assertFalse(rd.hasModified());
        assertEquals(1, listeners.size());
        return rd;
    }

    /**
     * @return
     * @throws IOException
     * @throws ServletException
     */
    private FilterChain createMockFilterChain() throws IOException, ServletException {
        this.control = EasyMock.createNiceControl();
        FilterChain fc = this.control.createMock(FilterChain.class);
        fc.doFilter(this.request, this.response);
        this.control.replay();
        return fc;
    }


    private class StrutsConfigReloadFilterStub extends StrutsConfigReloadFilter {

	    @Override
	    protected StrutsResourcesReloader newResourcesReloader(HttpServlet actionServlet) {
	        return new StrutsResourcesReloaderStub(new DefaultStrutsConfigFileHelper(actionServlet));
	    }

	}

    private class StrutsResourcesReloaderStub extends StrutsResourcesReloader{
	    public StrutsResourcesReloaderStub(StrutsConfigFileHelper scfh) {
	        super(scfh);
	    }
	    public List<ResourceListener> getListeners(){
	        return this.listeners;
	    }
	    public List<ResourceReloadable> getResources() {
	        return this.resources;
	    }
	}

}
