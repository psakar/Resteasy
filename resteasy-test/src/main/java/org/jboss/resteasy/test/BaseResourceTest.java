/**
 *
 */
package org.jboss.resteasy.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Hashtable;

import javax.ws.rs.ext.ExceptionMapper;

import org.jboss.resteasy.core.Dispatcher;
import org.jboss.resteasy.spi.Registry;
import org.jboss.resteasy.spi.ResteasyDeployment;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;

/**
 * @author <a href="mailto:ryan@damnhandy.com">Ryan J. McDonough</a>
 * @version $Revision:$
 */
public abstract class BaseResourceTest
{
   // IT IS NOT THREADSAFE !!!!
   protected static boolean manualStart;

   protected static Hashtable<String,String> initParams = new Hashtable<String,String>();
   protected static Hashtable<String,String> contextParams = new Hashtable<String,String>();
   protected static ResteasyDeployment deployment;
   protected static Dispatcher dispatcher;

   @Before
   public void before () throws Exception {

   }

   @BeforeClass
   public static void beforeClass() throws Exception
   {
      startContainer();
   }

   protected static void createContainer(Hashtable<String,String> initParams, Hashtable<String, String> contextParams) throws Exception {
     BaseResourceTest.initParams = initParams;
     BaseResourceTest.contextParams = contextParams;
     startContainer();
   }

   protected static void startContainer() throws Exception {
      if (deployment == null) {
         deployment = EmbeddedContainer.start(initParams, contextParams);
         dispatcher = deployment.getDispatcher();
      }
   }

   @AfterClass
   public static void afterClass() throws Exception
   {
      stopContainer();
   }

   protected static void stopContainer() throws Exception {
      if (deployment != null)
        EmbeddedContainer.stop();
      dispatcher = null;
      deployment = null;
   }

   public Registry getRegistry()
   {
      return deployment.getRegistry();
   }

   public ResteasyProviderFactory getProviderFactory()
   {
      return deployment.getProviderFactory();
   }

   public static void addPerRequestResource(Class<?> resource, Class<?> ... otherResources)
   {
      deployment.getRegistry().addPerRequestResource(resource);
   }

   protected void addClasspathResource(File file) {

   }

   public static void registerProvider(Class<?> clazz) {
     deployment.getProviderFactory().registerProvider(clazz);
   }

   public static void addExceptionMapper(Class<? extends ExceptionMapper<?>> clazz) {
     deployment.getProviderFactory().addExceptionMapper(clazz);
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
}