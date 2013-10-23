Testsuite for Resteasy 2.3 branch
=================================
Testsuite was created from upstream Resteasy tag 2.3.7 (https://github.com/resteasy/Resteasy/tree/2.3.7)
Following chages were done:
1) deleted folders src/main
2) cleaned and changed pom.xml
   deleted src/main replaced with dependency
   created profile productized and upstream for testing either upstream of productized dependencies
   created parent aggregator pom for arquillian and war-test submodules
3) removed forked moded and fixed test to do cleanup (stop tjws server, close spring application context)
4) fixed eclipse warnings
5) commented out modules without tests
6) created new version of maven-testlol-plugin-1.5.0 
   replacement for original tv.bodil:maven-testlol-plugin:1.2.2 used in test-jsapi-servlet-war failing with NullPointerException because of required change of testlol/src/main/resources/tv/bodil/testlol/js/env.rhino.js
   see http://envjs.lighthouseapp.com/projects/21590/tickets/178-__trim__-function-not-available-from-envjssetcookie-method
   see maven-repo-extra


Running testsuite
=================

To test with jetty 8
mvn -s settings-jenkins.xml -Dproductized clean verify -Djetty8 -Djboss.home 2>&1 | tee log.txt

To test with existing EAP (not running)
mvn -s settings-jenkins.xml -Dproductized clean verify -Djboss730 -Djboss.home=/home/development/jbossqe/JBEAP-6.2.0.ER6/build/jboss-eap-6.2 2>&1 | tee log.txt

To test with EAP locally installed
mvn -s settings-jenkins.xml -Dproductized clean verify -Djboss730 2>&1 | tee log.txt

To run single module use -pl option
eg. mvn -s settings.xml -Dproductized -pl :RESTEASY-736-jetty clean verify -Djboss730 2>&1 | tee log.txt

