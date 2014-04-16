package org.jboss.resteasy.test.smoke;

import java.util.HashSet;
import java.util.Set;

import javax.ws.rs.core.Application;

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
public class MyApplicationConfig extends Application
{
   private final Set<Class<?>> classes = new HashSet<Class<?>>();
   private final Set<Object> singletons = new HashSet<Object>();

   public MyApplicationConfig()
   {
      classes.add(AppConfig.MyResource.class);
      singletons.add(new AppConfig.QuotedTextWriter());
   }

   @Override
   public Set<Class<?>> getClasses()
   {
      return classes;
   }

   @Override
   public Set<Object> getSingletons()
   {
      return singletons;
   }

}
