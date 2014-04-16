package org.jboss.resteasy.tests.scanning;

import static org.junit.Assert.*;

import java.io.File;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.resteasy.client.ClientRequest;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.plugins.providers.RegisterBuiltin;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.importer.ZipImporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * TEST  RESTEASY-263 RESTEASY-274

 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
@RunWith(Arquillian.class)
@RunAsClient
public class ScanTest
{
    private static final String DEPLOYMENT = "scanning-test";

    private static final String URL = "http://localhost:8080/" + DEPLOYMENT;

    @Deployment
    public static WebArchive getDeployment() throws Exception {
        String name = DEPLOYMENT + ".war";
        WebArchive archive = ShrinkWrap.create(ZipImporter.class, name)
                .importFrom(new File("target/" + DEPLOYMENT + ".war")).as(WebArchive.class);
        return archive;
    }

   @Test
   public void testAll() throws Exception
   {
      RegisterBuiltin.register(ResteasyProviderFactory.getInstance());
      ClientRequest request = new ClientRequest(URL + "/test/doit");
      ClientResponse<?> response = request.get();
      assertEquals(200, response.getStatus());
      assertEquals("hello world", response.getEntity(String.class));
   }
}
