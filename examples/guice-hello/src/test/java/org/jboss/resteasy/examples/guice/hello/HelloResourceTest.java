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

public class HelloResourceTest
{

	@Test
	public void testHello() {
		final String message = "greeting";
		final HelloResource helloResource = new HelloResource(new Greeter()
		{
			public String greet(final String name)
			{
				return message;
			}
		});
		Assert.assertEquals(message, helloResource.hello(null));
	}
}
