Struts Config Reloader is developed to support the reloading of struts1 config files .Struts config reloading is not supported in struts1,but in develop stage struts config is alway modified,then we must restart web server (e.g. tomcat),this wastes a lot of time and very inefficient,especially when server restart is slowly,so this project is aimed to resolve the issue.This tools make your development more efficiency at develop stage of your struts1 project. Of course it's not required if you use struts2. Now this tools supports reloading struts-config.xml ,tiles config file and message resources properties file that defined in struts-config.xml .

# 1. How To Use #
To reload your struts config files is very easy with struts config reload,the only things you need to do is just add one filter to your web.xml,then you can reload your struts config files when those file modified.The filter configuration should look like:
```
   <!-- Struts Cofig Reload Filter start --> 
    <filter> 
      <filter-name>strutsCofigReloadFilter</filter-name> 
      <filter-class>
           org.strutsconfigreloader.struts.filter.StrutsConfigReloadFilter
      </filter-class> 
    </filter> 
    <filter-mapping> 
      <filter-name>strutsCofigReloadFilter</filter-name> 
      <url-pattern>*.do</url-pattern> 
    </filter-mapping> 
   <!-- Struts Cofig Reload Filter end -->
```

**N.B.** It's strongly recommended to remove this filter when you release your product to enhance the system performance.

# 2. How to build your actifact from sources #

We will use $SCR\_HOME$ to standard for the base directory of the porject,e.g,you check out from the svn.

It's assumes you had installed maven,you could download and install from maven official site,For more detail of maven please locate your browser to http://maven.apache.org and got it.

When you had installed maven,the only things is :

->cd $SCR\_HOME$
->mvn clean install

Then you should find the binary artifact from the output directory of module core.It's seems as $SCR$/croe/target/struts-config-reloader-1.1-SNAPSHOT.jar. Besides you also can find the same jar from you local maven repository.

# 3. Bug report #

Please submit issues to the bug tracer if you find any bug or any other requirements or enhancement,The issues address is http://code.google.com/p/strutsconfigreloader/issues/list. Any suggestion or effort would be very appreciate.


For more details please reame the [readme](http://rsc.googlecode.com/svn/trunk/readme)