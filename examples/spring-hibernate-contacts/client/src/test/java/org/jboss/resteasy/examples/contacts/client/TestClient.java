package org.jboss.resteasy.examples.contacts.client;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.apache.tools.ant.util.FileUtils;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.resteasy.client.ClientResponse;
import org.jboss.resteasy.client.ProxyFactory;
import org.jboss.resteasy.examples.contacts.core.Contacts;
import org.jboss.resteasy.plugins.providers.RegisterBuiltin;
import org.jboss.resteasy.spi.ResteasyProviderFactory;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.StringAsset;
import org.jboss.shrinkwrap.api.exporter.ZipExporter;
import org.jboss.shrinkwrap.api.importer.ZipImporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
@RunAsClient
public class TestClient
{
   private static final String USER_EMAIL = "olivier@yahoo.com";

   private static final String DEPLOYMENT = "spring-hibernate-contacts-services";
   private static final String URL = "http://localhost:8080/" + DEPLOYMENT;

   @Deployment
   public static WebArchive getDeployment() throws Exception {
       String name = DEPLOYMENT + ".war";
    WebArchive archive = ShrinkWrap.create(ZipImporter.class, name).importFrom(new File("../services/target/" + DEPLOYMENT + ".war"))
        .as(WebArchive.class);
       archive.addAsResource(new StringAsset(createDbProperties()), "db.properties");
       archive.as(ZipExporter.class).exportTo(new File("target/" + name), true);
       return archive;
   }

    private static String createDbProperties() throws IOException, FileNotFoundException {
        File databaseDir = new File("../persistence/src/main/sql");
        File dbProperties = new File("src/test/resources/db.properties");
        String content = FileUtils.readFully(new FileReader(dbProperties));
        return content.replace("hsqldb/contact", databaseDir.getAbsolutePath() + "/" + "hsqldb/contact");
    }

   @Test
   public void testGetContacts()
   {
       RegisterBuiltin.register(ResteasyProviderFactory.getInstance());
       ContactClient client = ProxyFactory.create(ContactClient.class, URL);

      ClientResponse<Contacts> contacts = client.getContacts();
      Assert.assertEquals(3, contacts.getEntity().getContacts().size());
      Assert.assertEquals(USER_EMAIL, contacts.getEntity().getContacts()
            .iterator().next().getEmail());
   }
}
