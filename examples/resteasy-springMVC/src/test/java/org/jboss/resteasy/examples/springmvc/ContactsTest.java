package org.jboss.resteasy.examples.springmvc;

import java.io.File;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.resteasy.client.ClientURI;
import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.resteasy.plugins.providers.RegisterBuiltin;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.jboss.resteasy.util.HttpHeaderNames;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.importer.ZipImporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(Arquillian.class)
@RunAsClient
public class ContactsTest
{

    private static final String DEPLOYMENT = "resteasy-springMVC";
    private static final String URL = "http://localhost:8080/" + DEPLOYMENT;

    @Deployment
    public static WebArchive getDeployment() {
        WebArchive archive = ShrinkWrap.create(ZipImporter.class, DEPLOYMENT + ".war").importFrom(new File("target/" + DEPLOYMENT + ".war"))
         .as(WebArchive.class);
        return archive;
    }


   @Path(ContactsResource.CONTACTS_URL)
   public static interface ContactProxy
   {
      @Path("data")
      @POST
      @Consumes(MediaType.APPLICATION_XML)
      Response createContact(Contact contact);

      @GET
      @Produces(MediaType.APPLICATION_XML)
      Contact getContact(@ClientURI String uri);

      @GET
      String getString(@ClientURI String uri);
   }

   private ContactProxy proxy;

   @Before
   public void setup() throws Exception
   {

      RegisterBuiltin.register(ResteasyProviderFactory.getInstance());
      proxy = ProxyFactory.create(ContactProxy.class, URL);
   }

   @Test
   public void testData()
   {
      Response response = proxy.createContact(new Contact("Solomon", "Duskis"));
      String duskisUri = (String) response.getMetadata().getFirst(
            HttpHeaderNames.LOCATION);
      System.out.println(duskisUri);
      Assert.assertTrue(duskisUri.endsWith(ContactsResource.CONTACTS_URL
            + "/data/Duskis"));
      Assert
            .assertEquals("Solomon", proxy.getContact(duskisUri).getFirstName());
      proxy.createContact(new Contact("Bill", "Burkie"));
      System.out.println(proxy.getString("/" + DEPLOYMENT + ContactsResource.CONTACTS_URL
            + "/data"));
   }

   @Ignore
   @Test
   public void readHTML()
   {
      System.out.println(proxy.getString(ContactsResource.CONTACTS_URL));
   }
}
