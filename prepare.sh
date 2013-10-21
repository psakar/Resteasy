# variable to be set before running the script
# EAP_VERSION - eg 6.2.0.ER6
# BUILD_DIR - eg "."
# MAVEN_REPO_AS_ZIP - eg ${BUILD_DIR}/jboss-eap-${EAP_VERSION}-maven-repository.zip   
#

# param #1 maven repos as zip file 
# param #2 maven repos as folder
prepare_maven_repo_as() {
	if [ ! -f $1 ]; then
		echo "Maven repo as zip file $1 not found"
		exit 1
	fi
	local MAVEN_REPO_TMP=${BUILD_DIR}/maven-repo-tmp
	if [ -d ${MAVEN_REPO_TMP} ]; then
	  rm -rf ${MAVEN_REPO_TMP}
	fi
	mkdir ${MAVEN_REPO_TMP}

	unzip -q $1 -d ${MAVEN_REPO_TMP}
	local MAVEN_REPO_FOLDER=$(ls ${MAVEN_REPO_TMP})
	mv maven-repo-tmp/$MAVEN_REPO_FOLDER ${MAVEN_REPO_LOCAL}
}

# param #1 maven repos local folder
prepare_maven_repo_local() {
	if [ -d ${MAVEN_REPO_LOCAL} ]; then
	  rm -rf ${MAVEN_REPO_LOCAL}
	fi
	mkdir ${MAVEN_REPO_LOCAL}
}

# param #1 folder with extra artifacts not present in maven-repo-as
# param #2 folder with local maven repo
install_into_maven_repo_local_artifacts_not_available_in_maven_repo_as() {
	local MAVEN_REPO_EXTRA=$1
	local MAVEN_REPO_LOCAL=$2
	pushd ${MAVEN_REPO_EXTRA}
	mvn org.apache.maven.plugins:maven-install-plugin:2.5:install-file -DgroupId=tv.bodil -DartifactId=maven-testlol-plugin -Dversion=1.5.0 -Dpackaging=jar  -Dfile=${MAVEN_REPO_EXTRA}/maven-testlol-plugin-1.5.0.jar -DlocalRepositoryPath=${MAVEN_REPO_LOCAL}
	mvn org.apache.maven.plugins:maven-install-plugin:2.5:install-file -DgroupId=tv.bodil -DartifactId=maven-testlol-plugin -Dversion=1.5.0 -Dpackaging=pom  -Dfile=${MAVEN_REPO_EXTRA}/maven-testlol-plugin-1.5.0.pom -DlocalRepositoryPath=${MAVEN_REPO_LOCAL}

	mvn org.apache.maven.plugins:maven-install-plugin:2.5:install-file -DgroupId=org.jboss.resteasy -DartifactId=resteasy-guice -Dversion=2.3.7.Final-redhat-2 -Dpackaging=jar -Dfile=${MAVEN_REPO_EXTRA}/resteasy-guice-2.3.7.Final-redhat-2.jar -DlocalRepositoryPath=${MAVEN_REPO_LOCAL}
	mvn org.apache.maven.plugins:maven-install-plugin:2.5:install-file -DgroupId=org.jboss.resteasy -DartifactId=resteasy-guice -Dversion=2.3.7.Final-redhat-2 -Dpackaging=pom -Dfile=${MAVEN_REPO_EXTRA}/resteasy-guice-2.3.7.Final-redhat-2.pom -DlocalRepositoryPath=${MAVEN_REPO_LOCAL}

	mvn org.apache.maven.plugins:maven-install-plugin:2.5:install-file -DgroupId=org.jboss.resteasy -DartifactId=resteasy-fastinfoset-provider  -Dversion=2.3.7.Final-redhat-2 -Dpackaging=jar -Dfile=${MAVEN_REPO_EXTRA}/resteasy-fastinfoset-provider-2.3.7.Final-redhat-2.jar -DlocalRepositoryPath=${MAVEN_REPO_LOCAL}
	mvn org.apache.maven.plugins:maven-install-plugin:2.5:install-file -DgroupId=org.jboss.resteasy -DartifactId=resteasy-fastinfoset-provider  -Dversion=2.3.7.Final-redhat-2 -Dpackaging=pom -Dfile=${MAVEN_REPO_EXTRA}/resteasy-fastinfoset-provider-2.3.7.Final-redhat-2.pom -DlocalRepositoryPath=${MAVEN_REPO_LOCAL}

	mvn org.apache.maven.plugins:maven-install-plugin:2.5:install-file -DgroupId=com.google.inject -DartifactId=guice -Dversion=3.0-redhat-2 -Dpackaging=jar -Dfile=${MAVEN_REPO_EXTRA}/guice-3.0-redhat-2.jar  -DlocalRepositoryPath=${MAVEN_REPO_LOCAL}
	mvn org.apache.maven.plugins:maven-install-plugin:2.5:install-file -DgroupId=com.google.inject -DartifactId=guice -Dversion=3.0-redhat-2 -Dpackaging=pom -Dfile=${MAVEN_REPO_EXTRA}/guice-3.0-redhat-2.pom  -DlocalRepositoryPath=${MAVEN_REPO_LOCAL}

	mvn org.apache.maven.plugins:maven-install-plugin:2.5:install-file -DgroupId=com.google.inject -DartifactId=guice-parent -Dversion=3.0-redhat-2 -Dpackaging=pom -Dfile=${MAVEN_REPO_EXTRA}/guice-parent-3.0-redhat-2.pom  -DlocalRepositoryPath=${MAVEN_REPO_LOCAL}
	mvn org.apache.maven.plugins:maven-install-plugin:2.5:install-file -DgroupId=com.google -DartifactId=google -Dversion=5-redhat-2 -Dpackaging=pom -Dfile=${MAVEN_REPO_EXTRA}/google-5-redhat-2.pom  -DlocalRepositoryPath=${MAVEN_REPO_LOCAL}

	mvn org.apache.maven.plugins:maven-install-plugin:2.5:install-file -DgroupId=org.jboss.resteasy -DartifactId=resteasy-jdk-http  -Dversion=2.3.7.Final-redhat-2 -Dpackaging=jar -Dfile=${MAVEN_REPO_EXTRA}/resteasy-jdk-http-2.3.7.Final-redhat-2.jar  -DlocalRepositoryPath=${MAVEN_REPO_LOCAL}
	mvn org.apache.maven.plugins:maven-install-plugin:2.5:install-file -DgroupId=org.jboss.resteasy -DartifactId=resteasy-jdk-http  -Dversion=2.3.7.Final-redhat-2 -Dpackaging=pom -Dfile=${MAVEN_REPO_EXTRA}/resteasy-jdk-http-2.3.7.Final-redhat-2.pom  -DlocalRepositoryPath=${MAVEN_REPO_LOCAL}

	mvn org.apache.maven.plugins:maven-install-plugin:2.5:install-file -DgroupId=org.jboss.resteasy -DartifactId=resteasy-netty  -Dversion=2.3.7.Final-redhat-2 -Dpackaging=jar -Dfile=${MAVEN_REPO_EXTRA}/resteasy-netty-2.3.7.Final-redhat-2.jar -DlocalRepositoryPath=${MAVEN_REPO_LOCAL}
	mvn org.apache.maven.plugins:maven-install-plugin:2.5:install-file -DgroupId=org.jboss.resteasy -DartifactId=resteasy-netty  -Dversion=2.3.7.Final-redhat-2 -Dpackaging=pom -Dfile=${MAVEN_REPO_EXTRA}/resteasy-netty-2.3.7.Final-redhat-2.pom -DlocalRepositoryPath=${MAVEN_REPO_LOCAL}
	popd
}

export MAVEN_REPO_AS=${BUILD_DIR}/maven-repo-as
export MAVEN_REPO_LOCAL=${BUILD_DIR}/maven-repo-local

prepare_maven_repo_as ${MAVEN_REPO_AS_ZIP} ${MAVEN_REPO_AS}
prepare_maven_repo_local ${MAVEN_REPO_LOCAL}
install_into_maven_repo_local_artifacts_not_available_in_maven_repo_as ${BUILD_DIR}/maven-repo-extra ${MAVEN_REPO_LOCAL}
