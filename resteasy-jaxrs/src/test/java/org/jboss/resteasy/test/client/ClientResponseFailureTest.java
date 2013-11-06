package org.jboss.resteasy.test.client;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

import org.jboss.resteasy.client.ClientResponseFailure;
import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.resteasy.spi.NoLogWebApplicationException;
import org.jboss.resteasy.test.BaseResourceTest;
import org.jboss.resteasy.test.TestPortProvider;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * Test connection cleanup
 *
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
public class ClientResponseFailureTest extends BaseResourceTest
{

   public static class MyResourceImpl implements MyResource
   {
      @Override
      public String get()
      {
         return "hello world";
      }

      @Override
      public String error()
      {
         Response r = Response.status(404).type("text/plain").entity("there was an error").build();
         throw new NoLogWebApplicationException(r);
      }
   }

   @Path("/test")
   public static interface MyResource
   {
      @GET
      @Produces("text/plain")
      public String get();

      @GET
      @Path("error")
      @Produces("text/plain")
      String error();
   }

   @BeforeClass
   public static void setUp() throws Exception
   {
      addPerRequestResource(MyResourceImpl.class);
   }


   @SuppressWarnings("unchecked")
   @Test
   public void testStreamStillOpen() throws Exception
   {
      final MyResource proxy = ProxyFactory.create(MyResource.class, TestPortProvider.generateBaseUrl());
      boolean failed = true;
      try
      {
         proxy.error();
         failed = false;
      }
      catch (ClientResponseFailure e)
      {
         Assert.assertEquals(e.getResponse().getStatus(), 404);
         Assert.assertEquals((e.getResponse()).getEntity(String.class), "there was an error");
         e.getResponse().releaseConnection();
      }

      Assert.assertTrue(failed);
   }

   public static class Background extends Thread
   {

      private final MyResource proxy;

      boolean finished = false;

      public Background(MyResource proxy)
      {
         this.proxy = proxy;
      }

      @Override
      public void run()
      {
         boolean failed = true;
         try
         {
            proxy.error();
            failed = false;
         }
         catch (ClientResponseFailure e)
         {
            Assert.assertEquals(e.getResponse()
                    .getStatus(), 404);
            @SuppressWarnings("unchecked")
            String str = (String) e.getResponse().getEntity(String.class);
            Assert.assertEquals("there was an error", str);

//                Assert.assertEquals( e.getResponse()
//                    .getEntity( String.class ), "there was an error" );
            //e.getResponse().releaseConnection();
            //e.printStackTrace();
         }

         Assert.assertTrue(failed);
         finished = true;
      }
   }


}