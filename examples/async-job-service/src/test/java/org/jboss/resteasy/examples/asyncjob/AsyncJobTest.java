package org.jboss.resteasy.examples.asyncjob;

import java.io.File;

import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
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
public class AsyncJobTest
{
   private static final String DEPLOYMENT = "async-job";
   private static final String URL = "http://localhost:8080/" + DEPLOYMENT;

   @Deployment
   public static WebArchive getDeployment() {
	   WebArchive archive = ShrinkWrap.create(ZipImporter.class, DEPLOYMENT + ".war").importFrom(new File("target/" + DEPLOYMENT + ".war"))
        .as(WebArchive.class);
	   return archive;
   }

   @Test
   public void testOneway() throws Exception
   {
      HttpClient client = new HttpClient();
      {
         PutMethod method = new PutMethod(URL + "/resource?oneway=true");
         method.setRequestEntity(new StringRequestEntity("content", "text/plain", null));
         int status = client.executeMethod(method);
         Assert.assertEquals(202, status);
         Thread.sleep(1500);
         GetMethod get = new GetMethod(URL + "/resource");
         status = client.executeMethod(get);
         Assert.assertEquals(Integer.toString(1), get.getResponseBodyAsString());

         method.releaseConnection();
      }
   }

   @Test
   public void testAsynch() throws Exception
   {
      HttpClient client = new HttpClient();
      {
         PostMethod method = new PostMethod(URL + "/resource?asynch=true");
         method.setRequestEntity(new StringRequestEntity("content", "text/plain", null));
         int status = client.executeMethod(method);
         Assert.assertEquals(Response.Status.ACCEPTED.getStatusCode(), status);
         String jobUrl1 = method.getResponseHeader(HttpHeaders.LOCATION).getValue();

         GetMethod get = new GetMethod(jobUrl1);
         status = client.executeMethod(get);
         Assert.assertEquals(Response.Status.ACCEPTED.getStatusCode(), status);

         Thread.sleep(1500);
         status = client.executeMethod(get);
         Assert.assertEquals(Response.Status.OK.getStatusCode(), status);
         Assert.assertEquals(get.getResponseBodyAsString(), "content");

         method.releaseConnection();
      }
   }
}
