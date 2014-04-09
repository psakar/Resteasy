package org.jboss.resteasy.examples.guice.hello;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.importer.ZipImporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.net.URL;

@RunWith(Arquillian.class)
@RunAsClient
public class HelloResourceIntegrationTest
{

	private static final String DEPLOYMENT = "guice-hello";
	private static final String URL = "http://localhost:8080/" + DEPLOYMENT;

	@Deployment
	   public static WebArchive getDeployment() {
		   WebArchive archive = ShrinkWrap.create(ZipImporter.class, DEPLOYMENT + ".war").importFrom(new File("target/" + DEPLOYMENT + ".war"))
	        .as(WebArchive.class);
		   return archive;
	   }

   @Test
   public void test() throws Exception
   {
      final URL url = new URL(URL + "/hello/world");
      final BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
      try {
         Assert.assertEquals("Hello world", reader.readLine());
         Assert.assertNull(reader.readLine());
      } finally {
         reader.close();
      }
   }
}
