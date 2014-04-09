To run locally

1. Extract EAP 5 distribution and set variable AS_DISTRIBUTION and JBOSS_HOME
eg.
	unzip /home/development/artifacts/JBEAP-5.3.0.ER3/jboss-eap-noauth-5.3.0-ER3.zip
	export AS_DISTRIBUTION=$(pwd)/jboss-eap-5.3
	export JBOSS_HOME=$(pwd)/jboss-eap-5.3/jboss-as

IMPORTANT - noauth variant of EAP sever has to be used !

2. Install resteasy artifacts into local maven repository
Select which maven repository will be used for testing - eap 5 artifacts are not available from public or RedHat maven repositories, we have to install them into local maven repository

IMPORTANT - please note that artifacts are installed without poms, so maven will not be able automatically resolve dependencies !

To use your standard maven repository set MAVEN_REPO_LOCAL 
	export MAVEN_REPO_LOCAL=~/.m2/repository

If you do not set MAVEN_REPO_LOCAL variable, artifacts will be installed into new folder maven-repo-local


3. Run tests
mvn -Dmaven.repo.local=REPO_SELECTED_IN_SECOND_STEP clean verify

Note:
-Dmaven.repo.local=REPO_SELECTED_IN_SECOND_STEP can be ommited if you selected your standard maven repository
