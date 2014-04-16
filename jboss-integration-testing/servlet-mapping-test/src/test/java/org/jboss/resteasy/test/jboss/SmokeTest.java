package org.jboss.resteasy.test.jboss;

import java.io.File;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.resteasy.plugins.providers.RegisterBuiltin;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.jboss.resteasy.util.HttpResponseCodes;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.importer.ZipImporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;


/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
@RunWith(Arquillian.class)
@RunAsClient
public class SmokeTest
{

    private static final String DEPLOYMENT = "servlet-mapping-test";

    private static final String URL = "http://localhost:8080/" + /*DEPLOYMENT*/ "resteasy"; //see jboss-web.xml

    @Deployment
    public static WebArchive getDeployment() throws Exception {
        String name = DEPLOYMENT + ".war";
        WebArchive archive = ShrinkWrap.create(ZipImporter.class, name)
                .importFrom(new File("target/" + DEPLOYMENT + ".war")).as(WebArchive.class);
        return archive;
    }

   @Test
   public void testNoDefaultsResource() throws Exception
   {
      HttpClient client = new HttpClient();

      {
         GetMethod method = new GetMethod(URL + "/rest/basic");
         int status = client.executeMethod(method);
         Assert.assertEquals(HttpResponseCodes.SC_OK, status);
         Assert.assertEquals("basic", method.getResponseBodyAsString());
         method.releaseConnection();
      }
   }

   @Test
   public void testFormParam()
   {
      RegisterBuiltin.register(ResteasyProviderFactory.getInstance());
      final FormResource client = ProxyFactory.create(FormResource.class, URL + "/rest");
      final String result = client.postForm("value");
      Assert.assertEquals(result, "value");
   }

   @Path("/")
   public static interface FormResource
   {
      @POST
      @Path("formtestit")
      public String postForm(@FormParam("value") String value);
   }
}
