package org.jboss.resteasy.examples.guice.hello;

import java.io.File;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.importer.ZipImporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Test;
import org.junit.Assert;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
@RunAsClient
public class DefaultGreeterTest
{
	private static final String DEPLOYMENT = "guice-hello-1.2.1.GA_CP02_patch03";
	//private static final String URL = "http://localhost:8080/" + DEPLOYMENT;

	@Deployment
	   public static WebArchive getDeployment() {
		   WebArchive archive = ShrinkWrap.create(ZipImporter.class, DEPLOYMENT + ".war").importFrom(new File("target/" + DEPLOYMENT + ".war"))
	        .as(WebArchive.class);
		   return archive;
	   }

   @Test
   public void testGreet()
   {
      final Greeter greeter = new DefaultGreeter();
      Assert.assertEquals("Hello foo", greeter.greet("foo"));
      Assert.assertEquals("Hello bar", greeter.greet("bar"));
   }
}
