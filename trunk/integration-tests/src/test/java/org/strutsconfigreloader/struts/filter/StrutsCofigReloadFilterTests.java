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
import org.apache.cactus.WebRequest;
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
public class StrutsCofigReloadFilterTests extends FilterTestCase {

    private IMocksControl control = null;

    private FilterChain fc = null;

    private StrutsConfigReloadFilter filter = null;

    private File file = null;

    private long userModifyTime = 0L;

    private String webroot = System.getProperty("cactus.jetty.resourceDir");

    /*
     * (non-Javadoc)
     *
     * @see junit.framework.TestCase#setUp()
     */
    @Override
    protected void setUp() throws Exception {
        super.setUp();
        this.file = new File(this.webroot + "/WEB-INF/struts-config-user.xml");
        this.userModifyTime = this.file.lastModified();

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

        String testConfigUserFilePath = this.webroot + "/WEB-INF/test-config-user.xml";
        renameFile(this.file, new File(testConfigUserFilePath));
        renameFile(new File(this.webroot + "/WEB-INF/user_config.txt"), this.file);

        this.file.setLastModified(this.userModifyTime);
        new File(testConfigUserFilePath).setLastModified(this.userModifyTime);

        this.control = null;
        this.fc = null;
        this.filter = null;
        this.file = null;
    }

    /**
     * @param renameFile
     */
    private void renameFile(File renameFile, File destFile) {
        if (!destFile.exists() && renameFile.exists()) {
            renameFile.renameTo(destFile);
        }
    }

    /**
     * Test method for
     * {@link StrutsConfigReloadFilter#init(javax.servlet.FilterConfig)}.
     *
     * @throws ServletException
     */
    public void testInit() throws ServletException {

        StrutsConfigReloadFilterStub filter = new StrutsConfigReloadFilterStub();
        filter.init(this.config);

        StrutsResourcesReloaderStub rd = (StrutsResourcesReloaderStub) filter.getResourceReloader();

        List<ResourceListener> listeners = rd.getListeners();

        assertEquals(1, listeners.size());
        assertFalse(rd.hasModified());

    }

    /**
     * Test method for
     * {@link StrutsConfigReloadFilter#init(javax.servlet.FilterConfig)}.
     *
     * @throws ServletException
     */
    public void testDoFilterWhenModifyConfigFile() throws Exception {
        this.filter.init(this.config);
        this.filter.doFilter(this.request, this.response, this.fc);

        StrutsResourcesReloader rd = assertFirstFilter();

        this.file.setLastModified(this.userModifyTime + 5000);

        assertTrue(rd.hasModified());

        this.filter.doFilter(this.request, this.response, this.fc);

        assertFalse(rd.hasModified());

        this.control.verify();

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
     * Verify that we can simulate HTTP parameters in the HTTP request.
     *
     * @param theRequest the request object that serves to initialize the HTTP
     *            connection to the server redirector.
     */
    public void beginDoFilterWhenReloadConfigFile(WebRequest theRequest) {
        theRequest.setURL("localhost:8080", "/test", "/action", "/test.do", "");
    }

    /**
     * Test method for
     * {@link StrutsConfigReloadFilter#init(javax.servlet.FilterConfig)}.
     *
     * @throws ServletException
     */
    public void testDoFilterWhenReloadConfigFile() throws Exception {

        this.filter.init(this.config);
        this.filter.doFilter(this.request, this.response, this.fc);
        StrutsResourcesReloader rd = assertFirstFilter();

        this.file.renameTo(new File(this.webroot + "/WEB-INF/user_config.txt"));

        new File(this.webroot + "/WEB-INF/test-config-user.xml").renameTo(this.file);

        this.file.setLastModified(this.userModifyTime + 5000);

        assertTrue(rd.hasModified());

        this.filter.doFilter(this.request, this.response, this.fc);

        assertFalse(rd.hasModified());

        this.control.verify();

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
