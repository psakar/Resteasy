package org.jboss.resteasy.examples.jaxbjson;

import static org.junit.Assert.*;

import java.io.File;


import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.importer.ZipImporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
@RunAsClient
public class JaxbJsonTest {

	private static final String DEPLOYMENT = "jaxb-json";
	private static final String URL = "http://localhost:8080/" + DEPLOYMENT;;
	private static final String RESPONSE_MAPPED = "{\"listing\":{\"books\":[{\"@title\":\"EJB 3.0\",\"author\":\"Bill Burke\",\"ISBN\":596529260},{\"@title\":\"RESTful Web Services\",\"author\":\"Leonard Richardson\",\"ISBN\":596529260}]}}";
	private static final String RESPONSE_BADGER = "{\"listing\":{\"books\":[{\"@title\":\"EJB 3.0\",\"author\":{\"$\":\"Bill Burke\"},\"ISBN\":{\"$\":\"596529260\"}},{\"@title\":\"RESTful Web Services\",\"author\":{\"$\":\"Leonard Richardson\"},\"ISBN\":{\"$\":\"596529260\"}}]}}";

	@Deployment
	   public static WebArchive getDeployment() {
		   WebArchive archive = ShrinkWrap.create(ZipImporter.class, DEPLOYMENT + ".war").importFrom(new File("target/" + DEPLOYMENT + ".war"))
	        .as(WebArchive.class);
		   return archive;
	   }

	@Test
	public void mappedJsonTest() throws Exception {
		HttpClient client = new HttpClient();
		GetMethod get = new GetMethod(URL + "/resteasy/library/books/mapped.html");
		client.executeMethod(get);
		Assert.assertEquals(get.getResponseBodyAsString(), RESPONSE_MAPPED);
	}

	@Test
	public void badgerJsonTest() throws Exception {
		HttpClient client = new HttpClient();
		GetMethod get = new GetMethod(URL + "/resteasy/library/books/badger.html");
		client.executeMethod(get);
		Assert.assertEquals(get.getResponseBodyAsString(), RESPONSE_BADGER);
	}

}
