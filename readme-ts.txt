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
   see https://github.com/psakar/maven-testlol-plugin/tree/1.5



Running testsuite
=================
1. Prerequisities
   jdk 1.6 with unlimited cryptography policy
   maven 3
   
2. Setup environment
run in shell following commands (set proper values)
 export EAP_VERSION=6.2.0.ER6
 export BUILD_DIR=$(pwd)
 export MAVEN_REPO_AS_ZIP=${BUILD_DIR}/jboss-eap-${EAP_VERSION}-maven-repository.zip
 export RESTEASY_VERSION=2.3.7.Final-redhat-2

 export AS_ZIP=${BUILD_DIR}/jboss-eap-${EAP_VERSION}.zip #if set AS is unzipped
 export AS_HOME=$BUILD_DIR/jboss-eap-$(echo $EAP_VERSION | awk -F"." '{ print $1 "." $2 }')
 export APPEND_JAVA_OPTS_TO_AS_CONFIG=""
 export AS_NETWORK_CONFIG="-Djava.net.preferIPv4Stack=true"
 export SHOW_INFO=1

3. install maven-testlol-plugin, unzip AS and AS maven repo
 run prepare.sh script
 or 
 install maven-testlol-plugin yourself and setup AS_HOME used for running testsuite in step to proper value

4. run testsuite
 mvn -s settings-jenkins.xml -Dproductized -Peap-productization-maven-repository -Dversions-test.org.jboss.resteasy=${RESTEASY_VERSION} -Djboss.home=$AS_HOME -Djboss730 clean verify

profile -Peap-productization-maven-repository is required only first time to download artifacts not available in maven repo as


To test with jetty 8
mvn -s settings-jenkins.xml -Dproductized clean verify -Djetty8 -Djboss.home 2>&1 | tee log.txt

To test with existing EAP (not running)
mvn -s settings.xml -Dproductized clean verify -Djboss730 -Djboss.home=/home/development/jbossqe/JBEAP-6.2.0.ER6/build/jboss-eap-6.2 2>&1 | tee log.txt

To test with EAP downloaded and locally installed by maven
mvn -s settings.xml -Dproductized clean verify -Djboss730 2>&1 | tee log.txt

To run single module use -pl option
eg. mvn -s settings.xml -Dproductized -pl :RESTEASY-736-jetty clean verify -Djboss730 2>&1 | tee log.txt

To run with different version of restesay use -Dversions.
eg. mvn -s settings.xml -Dproductized -pl :RESTEASY-736-jetty clean verify -Djboss730 2>&1 | tee log.txt

