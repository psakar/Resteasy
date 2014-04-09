package org.jboss.resteasy.examples.guice.hello;

import org.junit.Assert;
import org.junit.Test;

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
