package org.jboss.resteasy.test.jboss;

import java.io.File;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.UsernamePasswordCredentials;
import org.apache.commons.httpclient.auth.AuthScope;
import org.apache.commons.httpclient.methods.GetMethod;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.resteasy.util.HttpResponseCodes;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.importer.ZipImporter;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
@RunAsClient
public class SecurityTest {

    private static final String DEPLOYMENT = "basic-integration-test";
    private static final String URL = "http://localhost:8080/" + DEPLOYMENT;

    @Deployment
    public static WebArchive getDeployment() {
        WebArchive archive = ShrinkWrap.create(ZipImporter.class, DEPLOYMENT + ".war")
                .importFrom(new File("target/" + DEPLOYMENT + ".war")).as(WebArchive.class);
        return archive;
    }

    @Test
    public void testSecurity() throws Exception {
        HttpClient client = new HttpClient();

        client.getState().setCredentials(
        //new AuthScope(null, 8080, "Test"),
                new AuthScope(AuthScope.ANY), new UsernamePasswordCredentials("bill", "password"));
        {
            GetMethod method = new GetMethod(URL + "/security");
            method.setDoAuthentication(true);
            int status = client.executeMethod(method);
            Assert.assertEquals(HttpResponseCodes.SC_OK, status);
            Assert.assertEquals("Wild", method.getResponseBodyAsString());
            method.releaseConnection();
        }
    }

    @Test
    public void testSecurityFailure() throws Exception {
        HttpClient client = new HttpClient();

        {
            GetMethod method = new GetMethod(URL + "/security");
            int status = client.executeMethod(method);
            Assert.assertEquals(401, status);
            method.releaseConnection();
        }
    }

}
