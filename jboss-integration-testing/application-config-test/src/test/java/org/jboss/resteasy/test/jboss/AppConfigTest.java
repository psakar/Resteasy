package org.jboss.resteasy.test.jboss;

import java.io.File;
import java.io.IOException;

import org.apache.commons.httpclient.HttpClient;
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

/**
 * @author <a href="mailto:bill@burkecentral.com">Bill Burke</a>
 * @version $Revision: 1 $
 */
@RunWith(Arquillian.class)
@RunAsClient
public class AppConfigTest
{

    private static final String DEPLOYMENT = "application-config-test";
    private static final String URL = "http://localhost:8080/" + DEPLOYMENT;

    @Deployment
    public static WebArchive getDeployment() {
        WebArchive archive = ShrinkWrap.create(ZipImporter.class, DEPLOYMENT + ".war").importFrom(new File("target/" + DEPLOYMENT + ".war"))
         .as(WebArchive.class);
        return archive;
    }

    @Test
    public void testIt() {
        HttpClient client = new HttpClient();
        GetMethod method = new GetMethod(URL + "/my");
        try {
            method.addRequestHeader("Accept", "text/quoted");
            int status = client.executeMethod(method);
            Assert.assertEquals(status, HttpResponseCodes.SC_OK);
            Assert.assertEquals("\"hello\"", method.getResponseBodyAsString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
