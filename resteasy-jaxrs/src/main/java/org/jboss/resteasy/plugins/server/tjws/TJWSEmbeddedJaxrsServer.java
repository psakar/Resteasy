package org.jboss.resteasy.plugins.server.tjws;

import java.util.Hashtable;

import Acme.Serve.Serve;
import org.jboss.resteasy.core.Dispatcher;
import org.jboss.resteasy.plugins.server.embedded.EmbeddedJaxrsServer;
import org.jboss.resteasy.plugins.server.embedded.SecurityDomain;
import org.jboss.resteasy.spi.Registry;
import org.jboss.resteasy.spi.ResteasyDeployment;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
public class TJWSEmbeddedJaxrsServer extends TJWSServletServer implements EmbeddedJaxrsServer
{
   protected ResteasyDeployment deployment = new ResteasyDeployment();
   protected TJWSServletDispatcher servlet = new TJWSServletDispatcher();

   protected String rootResourcePath = "";
   protected Hashtable<String,String> initParameters;
   protected Hashtable<String,String> contextParameters;

   public void setRootResourcePath(String rootResourcePath)
   {
      this.rootResourcePath = rootResourcePath;
   }

   public TJWSEmbeddedJaxrsServer()
   {
   }

   public ResteasyDeployment getDeployment()
   {
      return deployment;
   }

   public void setDeployment(ResteasyDeployment deployment)
   {
      this.deployment = deployment;
   }
   
   public Hashtable<String,String> getInitParameters()
   {
      return initParameters;
   }
   
   public void setInitParameters(Hashtable<String,String> initParameters)
   {
      this.initParameters = initParameters;
   }
   
   public Hashtable<String,String> getContextParameters()
   {
      return contextParameters;
   }
   
   public void setContextParameters(Hashtable<String,String> contextParameters)
   {
      this.contextParameters = contextParameters;
   }

   @Override
   public void start()
   {
      server.setInitParams(getContextParameters());
      deployment.setServletContext(server);
      deployment.start();
      server.setAttribute(ResteasyProviderFactory.class.getName(), deployment.getProviderFactory());
      server.setAttribute(Dispatcher.class.getName(), deployment.getDispatcher());
      server.setAttribute(Registry.class.getName(), deployment.getRegistry());
      addServlet(rootResourcePath, servlet);
      servlet.setContextPath(rootResourcePath);
      super.start();
   }

   public void setSecurityDomain(SecurityDomain sc)
   {
      servlet.setSecurityDomain(sc);
   }

   public String getProperty(String key)
   {
      return props.getProperty(key);
   }

   public String getPort()
   {
      return getProperty(Serve.ARG_PORT);
   }
}
