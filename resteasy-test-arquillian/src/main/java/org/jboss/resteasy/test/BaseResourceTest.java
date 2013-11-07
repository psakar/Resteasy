package org.jboss.resteasy.test;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import org.jboss.arquillian.container.test.api.Deployer;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.test.api.ArquillianResource;
import org.jboss.shrinkwrap.api.ArchivePath;
import org.jboss.shrinkwrap.api.Node;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.Asset;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
@RunAsClient
public abstract class BaseResourceTest
{

  private static final Logger logger = Logger.getLogger(BaseResourceTest.class.getName());

  public static final String DEPLOYMENT = "app";

  @ArquillianResource
  protected static Deployer deployer;
  // IT IS NOT THREADSAFE !!!!
  protected static WebArchive war;
  // IT IS NOT THREADSAFE !!!!
  protected static boolean deployed;
  // IT IS NOT THREADSAFE !!!!
  protected static boolean manualStart;
  // IT IS NOT THREADSAFE !!!!
  protected static Map<String,String> initParams = new Hashtable<String,String>();
  // IT IS NOT THREADSAFE !!!!
  protected static Map<String,String> contextParams = new Hashtable<String,String>();

//  protected static ResteasyDeployment deployment;
//  protected static Dispatcher dispatcher;

  @Deployment(name = DEPLOYMENT, managed = false, testable = false)
  public static WebArchive getDeployment() {
    createDeployment();
    //war.toString(true);
    return war;
  }

  public static WebArchive createDeployment() {
    war = ShrinkWrap.create(WebArchive.class,  DEPLOYMENT + ".war");
    war.addClass(TestApplication.class);
    return war;
  }

  @BeforeClass
  public static void beforeClass() throws Exception
  {

  }


  @Before
  public void before () throws Exception {
    info("before");
    if (!deployed && !manualStart)
      startContainer();
  }

  @AfterClass
  public static void afterClass() throws Exception
  {
    stopContainer();
  }

  public static void addPerRequestResource(Class<?> ... resources)
  {
    Class<?> resourceClass = resources[0];
    info("added request resource " + resourceClass.getName());
    TestApplication.classes.add(resourceClass);
    war.addClasses(resources);
  }
/*
  protected static void addClasspathResource(String name, String target) {
    war.addAsResource(name, target);
  }
*/
  protected static void addWebResource(File file) {
    assertTrue("Resource not found " + file.getAbsolutePath(), file.exists());
    info("added web resource " + file.getAbsolutePath());
    war.addAsWebResource(file);
  }

  @Provider
  @ApplicationPath("/")
  public static class TestApplication extends Application
  {
     // IT IS NOT THREADSAFE !!!!
     public static final Set<Class<?>> classes = new HashSet<Class<?>>();

     @Override
     public Set<Class<?>> getClasses()
     {
        return classes;
     }
  }


  public String readString(InputStream in) throws IOException
  {
     char[] buffer = new char[1024];
     StringBuilder builder = new StringBuilder();
     BufferedReader reader = new BufferedReader(new InputStreamReader(in));
     int wasRead = 0;
     do
     {
        wasRead = reader.read(buffer, 0, 1024);
        if (wasRead > 0)
        {
           builder.append(buffer, 0, wasRead);
        }
     }
     while (wasRead > -1);

     return builder.toString();
  }

  public static void registerProvider(Class<?> clazz) {
    info("register provider " + clazz.getName());
    war.addClass(clazz);
    TestApplication.classes.add(clazz);
  }

  public static void addExceptionMapper(Class<? extends ExceptionMapper<?>> clazz) {
    war.addClass(clazz);
    TestApplication.classes.add(clazz);
  }

  protected static void createContainer(Map<String,String> initParams, Map<String, String> contextParams) throws Exception {
    info("create container");
    BaseResourceTest.initParams = initParams;
    BaseResourceTest.contextParams = contextParams;
  }

  protected static void startContainer() throws Exception {
    info("start container - deploy " + DEPLOYMENT);
    if (contextParams != null && contextParams.size() > 0 && !war.contains("WEB-INF/web.xml")) {
      StringBuilder webXml = new StringBuilder();
      webXml.append("<web-app version=\"3.0\" xmlns=\"http://java.sun.com/xml/ns/javaee\" \n");
      webXml.append(" xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" \n");
      webXml.append( " xsi:schemaLocation=\"http://java.sun.com/xml/ns/javaee http://java.sun.com/xml/ns/javaee/web-app_3_0.xsd\"> \n");
      for (Map.Entry<String, String> entry : contextParams.entrySet()) {
        String paramName = entry.getKey();
        String paramValue = entry.getValue();
        info("Context param " + paramName + " value " + paramValue);
        webXml.append("<context-param>\n");
        webXml.append("<param-name>" + paramName + "</param-name>\n");
        webXml.append("<param-value>" + paramValue + "</param-value>\n");
        webXml.append("</context-param>\n");
      }
      webXml.append("</web-app>\n");
      Asset resource = new StringAsset(webXml.toString());
      war.addAsWebInfResource(resource, "web.xml");
    }

    deployer.deploy(DEPLOYMENT);
    deployed = true;
  }

  protected static void stopContainer() throws Exception {
    info("stop container - undeploy " + DEPLOYMENT);
    if (deployed)
      deployer.undeploy(DEPLOYMENT);
    deployed = false;
    if (war != null) {
      for (Map.Entry<ArchivePath, Node> asset : war.getContent().entrySet()) {
        war.delete(asset.getKey());
      }
      war.addClass(TestApplication.class);
    }
    TestApplication.classes.clear();
  }

  protected static void info (String message) {
    logger.info(message);
    System.err.println("BaseResourceTest - " + message);
  }
}