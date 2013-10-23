package org.jboss.resteasy.resteasy736;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.annotations.Suspend;
import org.jboss.resteasy.spi.AsynchronousResponse;

/**
 *
 * @author <a href="ron.sigal@jboss.com">Ron Sigal</a>
 * @version $Revision: 1.1 $
 *
 * Copyright Aug 15, 2012
 */
@Path("/")
@Produces("text/plain")
public class TestResourceWithRunnable
{

   private final ExecutorService executor = Executors.newSingleThreadExecutor();

   @GET
   @Path("test")
   public void test(final @Suspend(5000) AsynchronousResponse response)
   {
      Runnable runnable = new Runnable()
      {
         @Override
         public void run()
         {
            try
            {
              System.out.println("TestResource test async thread started, timeout 50000, sleep 10000");
               Thread.sleep(10000);
               Response jaxrs = Response.ok("test").type(MediaType.TEXT_PLAIN).build();
               response.setResponse(jaxrs);
               System.out.println("TestResource test async thread finished");
            }
            catch (Exception e)
            {
               e.printStackTrace();
            }
         }
      };
      executor.submit(runnable);

   }

   @GET
   @Path("default")
   public void defalt(final @Suspend AsynchronousResponse response)
   {
      Runnable runnable = new Runnable()
      {
         @Override
         public void run()
         {
            try
            {
               System.out.println("TestResource default async thread started, timeout default, sleep 35000");
               Thread.sleep(35000); // Jetty async timeout defaults to 30000.
               Response jaxrs = Response.ok("test").type(MediaType.TEXT_PLAIN).build();
               response.setResponse(jaxrs);
               System.out.println("TestResource default async thread finished");
            }
            catch (Exception e)
            {
               e.printStackTrace();
            }
         }
      };
      executor.submit(runnable);
   }
}
