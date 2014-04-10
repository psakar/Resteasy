package org.jboss.resteasy.test.jboss;

import java.io.File;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.GetMethod;
import org.apache.commons.httpclient.methods.PutMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.resteasy.util.HttpResponseCodes;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.importer.ZipImporter;
import org.jboss.shrinkwrap.api.spec.EnterpriseArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
@RunAsClient
public class SmokeTest {

    private static final String DEPLOYMENT = "ejb-integration";

    private static final String URL = "http://localhost:8080/";

    @Deployment
    public static EnterpriseArchive getDeployment() {
        EnterpriseArchive archive = ShrinkWrap.create(ZipImporter.class, DEPLOYMENT + ".ear")
                .importFrom(new File("target/" + DEPLOYMENT + ".ear")).as(EnterpriseArchive.class);
        //archive.as(ZipExporter.class).exportTo(new File("target/" + archive.getName()), true);
        return archive;
    }

    @Test
    public void testNoDefaultsResource() throws Exception {
        HttpClient client = new HttpClient();
        {
            GetMethod method = new GetMethod(URL + "ejb-integration-war/basic");
            int status = client.executeMethod(method);
            Assert.assertEquals(HttpResponseCodes.SC_OK, status);
            Assert.assertEquals("basic", method.getResponseBodyAsString());
            method.releaseConnection();
        }
        {
            PutMethod method = new PutMethod(URL + "ejb-integration-war/basic");
            method.setRequestEntity(new StringRequestEntity("basic", "text/plain", null));
            int status = client.executeMethod(method);
            Assert.assertEquals(204, status);
            method.releaseConnection();
        }
        {
            GetMethod method = new GetMethod(URL + "ejb-integration-war/queryParam");
            NameValuePair[] params = {new NameValuePair("param", "hello world")};
            method.setQueryString(params);
            int status = client.executeMethod(method);
            Assert.assertEquals(HttpResponseCodes.SC_OK, status);
            Assert.assertEquals("hello world", method.getResponseBodyAsString());
            method.releaseConnection();
        }
        {
            GetMethod method = new GetMethod(URL + "ejb-integration-war/uriParam/1234");
            int status = client.executeMethod(method);
            Assert.assertEquals(HttpResponseCodes.SC_OK, status);
            Assert.assertEquals("1234", method.getResponseBodyAsString());
            method.releaseConnection();
        }
    }

    @Test
    public void testLocatingResource() throws Exception {
        HttpClient client = new HttpClient();
        {
            GetMethod method = new GetMethod(URL + "ejb-integration-war/locating/basic");
            int status = client.executeMethod(method);
            Assert.assertEquals(HttpResponseCodes.SC_OK, status);
            Assert.assertEquals("basic", method.getResponseBodyAsString());
            method.releaseConnection();
        }
        {
            PutMethod method = new PutMethod(URL + "ejb-integration-war/locating/basic");
            method.setRequestEntity(new StringRequestEntity("basic", "text/plain", null));
            int status = client.executeMethod(method);
            Assert.assertEquals(204, status);
            method.releaseConnection();
        }
        {
            GetMethod method = new GetMethod(URL + "ejb-integration-war/locating/queryParam");
            NameValuePair[] params = {new NameValuePair("param", "hello world")};
            method.setQueryString(params);
            int status = client.executeMethod(method);
            Assert.assertEquals(HttpResponseCodes.SC_OK, status);
            Assert.assertEquals("hello world", method.getResponseBodyAsString());
            method.releaseConnection();
        }
        {
            GetMethod method = new GetMethod(URL + "ejb-integration-war/locating/uriParam/1234");
            int status = client.executeMethod(method);
            Assert.assertEquals(HttpResponseCodes.SC_OK, status);
            Assert.assertEquals("1234", method.getResponseBodyAsString());
            method.releaseConnection();
        }
    }

}
