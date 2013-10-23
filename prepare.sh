# variable to be set before running the script
# EAP_VERSION - eg 6.2.0.ER6
# BUILD_DIR - eg "."
# MAVEN_REPO_AS_ZIP - eg ${BUILD_DIR}/jboss-eap-${EAP_VERSION}-maven-repository.zip
# RESTEASY_VERSION - eg. 2.3.7.Final-redhat-2
# TEST_GUICE - if value is 1 then install resteasy-guice related artifacts which are not avaiable in AS repo
#   GUICE_VERSION
#   GOOGLE_VERSION
# TEST_UNSUPPORTED - if value is 1 then install artifacts resteasy-fastinfoset-provider, resteasy-jdk-http, resteasy-netty which are not avaiable in AS repo
#

# param #1 maven repos as zip file
# param #2 maven repos as folder
prepare_maven_repo_as() {
	if [ ! -f $1 ]; then
		echo "Maven repo as zip file $1 not found"
		exit 1
	fi
	if [ -d $2 ]; then
		rm -rf $2
	fi
	local MAVEN_REPO_TMP=${BUILD_DIR}/maven-repo-tmp
	if [ -d ${MAVEN_REPO_TMP} ]; then
	  rm -rf ${MAVEN_REPO_TMP}
	fi
	mkdir ${MAVEN_REPO_TMP}

	unzip -q $1 -d ${MAVEN_REPO_TMP}
	local MAVEN_REPO_FOLDER=$(ls ${MAVEN_REPO_TMP})
	mv ${MAVEN_REPO_TMP}/$MAVEN_REPO_FOLDER $2
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
# param #3 resteasy version
# param #4 TEST_GUICE - install resteasy-guice related artifacts which are not avaiable in AS repo
# param #5 GUICE_VERSION
# param #6 GOOGLE_VERSION
# param #7 TEST_UNSUPPORTED install artifacts resteasy-fastinfoset-provider, resteasy-jdk-http, resteasy-netty which are not avaiable in AS repo

install_into_maven_repo_local_artifacts_not_available_in_maven_repo_as() {
	local MAVEN_REPO_EXTRA=$1
	local MAVEN_REPO_LOCAL=$2
	local RESTEASY_VERSION=$3
	local NO_GUICE=$4
	local GUICE_VERSION=$5
	local GOOGLE_VERSION=$6
	local NO_UNSUPPORTED=$7

	pushd ${MAVEN_REPO_EXTRA}
	mvn org.apache.maven.plugins:maven-install-plugin:2.5:install-file -DgroupId=tv.bodil -DartifactId=maven-testlol-plugin -Dversion=1.5.0 -Dpackaging=jar  -Dfile=${MAVEN_REPO_EXTRA}/maven-testlol-plugin-1.5.0.jar -DlocalRepositoryPath=${MAVEN_REPO_LOCAL}
	mvn org.apache.maven.plugins:maven-install-plugin:2.5:install-file -DgroupId=tv.bodil -DartifactId=maven-testlol-plugin -Dversion=1.5.0 -Dpackaging=pom  -Dfile=${MAVEN_REPO_EXTRA}/maven-testlol-plugin-1.5.0.pom -DlocalRepositoryPath=${MAVEN_REPO_LOCAL}


	if [ "${TEST_GUICE}" == "1" ]; then

  	mvn org.apache.maven.plugins:maven-install-plugin:2.5:install-file -DgroupId=org.jboss.resteasy -DartifactId=resteasy-guice -Dversion=${RESTEASY_VERSION} -Dpackaging=jar -Dfile=${MAVEN_REPO_EXTRA}/resteasy-guice-${RESTEASY_VERSION}.jar -DlocalRepositoryPath=${MAVEN_REPO_LOCAL}
	  mvn org.apache.maven.plugins:maven-install-plugin:2.5:install-file -DgroupId=org.jboss.resteasy -DartifactId=resteasy-guice -Dversion=${RESTEASY_VERSION} -Dpackaging=pom -Dfile=${MAVEN_REPO_EXTRA}/resteasy-guice-${RESTEASY_VERSION}.pom -DlocalRepositoryPath=${MAVEN_REPO_LOCAL}

		mvn org.apache.maven.plugins:maven-install-plugin:2.5:install-file -DgroupId=com.google.inject -DartifactId=guice -Dversion=${GUICE_VERSION} -Dpackaging=jar -Dfile=${MAVEN_REPO_EXTRA}/guice-${GUICE_VERSION}.jar  -DlocalRepositoryPath=${MAVEN_REPO_LOCAL}
		mvn org.apache.maven.plugins:maven-install-plugin:2.5:install-file -DgroupId=com.google.inject -DartifactId=guice -Dversion=${GUICE_VERSION} -Dpackaging=pom -Dfile=${MAVEN_REPO_EXTRA}/guice-${GUICE_VERSION}.pom  -DlocalRepositoryPath=${MAVEN_REPO_LOCAL}

		mvn org.apache.maven.plugins:maven-install-plugin:2.5:install-file -DgroupId=com.google.inject -DartifactId=guice-parent -Dversion=${GUICE_VERSION} -Dpackaging=pom -Dfile=${MAVEN_REPO_EXTRA}/guice-parent-${GUICE_VERSION}.pom  -DlocalRepositoryPath=${MAVEN_REPO_LOCAL}
		mvn org.apache.maven.plugins:maven-install-plugin:2.5:install-file -DgroupId=com.google -DartifactId=google -Dversion=${GOOGLE_VERSION} -Dpackaging=pom -Dfile=${MAVEN_REPO_EXTRA}/google-${GOOGLE_VERSION}.pom  -DlocalRepositoryPath=${MAVEN_REPO_LOCAL}
	fi

	if [ "${TEST_UNSUPPORTED}" == "1" ]; then
		mvn org.apache.maven.plugins:maven-install-plugin:2.5:install-file -DgroupId=org.jboss.resteasy -DartifactId=resteasy-fastinfoset-provider  -Dversion=${RESTEASY_VERSION} -Dpackaging=jar -Dfile=${MAVEN_REPO_EXTRA}/resteasy-fastinfoset-provider-${RESTEASY_VERSION}.jar -DlocalRepositoryPath=${MAVEN_REPO_LOCAL}
		mvn org.apache.maven.plugins:maven-install-plugin:2.5:install-file -DgroupId=org.jboss.resteasy -DartifactId=resteasy-fastinfoset-provider  -Dversion=${RESTEASY_VERSION} -Dpackaging=pom -Dfile=${MAVEN_REPO_EXTRA}/resteasy-fastinfoset-provider-${RESTEASY_VERSION}.pom -DlocalRepositoryPath=${MAVEN_REPO_LOCAL}

		mvn org.apache.maven.plugins:maven-install-plugin:2.5:install-file -DgroupId=org.jboss.resteasy -DartifactId=resteasy-jdk-http  -Dversion=${RESTEASY_VERSION} -Dpackaging=jar -Dfile=${MAVEN_REPO_EXTRA}/resteasy-jdk-http-${RESTEASY_VERSION}.jar  -DlocalRepositoryPath=${MAVEN_REPO_LOCAL}
		mvn org.apache.maven.plugins:maven-install-plugin:2.5:install-file -DgroupId=org.jboss.resteasy -DartifactId=resteasy-jdk-http  -Dversion=${RESTEASY_VERSION} -Dpackaging=pom -Dfile=${MAVEN_REPO_EXTRA}/resteasy-jdk-http-${RESTEASY_VERSION}.pom  -DlocalRepositoryPath=${MAVEN_REPO_LOCAL}

		mvn org.apache.maven.plugins:maven-install-plugin:2.5:install-file -DgroupId=org.jboss.resteasy -DartifactId=resteasy-netty  -Dversion=${RESTEASY_VERSION} -Dpackaging=jar -Dfile=${MAVEN_REPO_EXTRA}/resteasy-netty-${RESTEASY_VERSION}.jar -DlocalRepositoryPath=${MAVEN_REPO_LOCAL}
		mvn org.apache.maven.plugins:maven-install-plugin:2.5:install-file -DgroupId=org.jboss.resteasy -DartifactId=resteasy-netty  -Dversion=${RESTEASY_VERSION} -Dpackaging=pom -Dfile=${MAVEN_REPO_EXTRA}/resteasy-netty-${RESTEASY_VERSION}.pom -DlocalRepositoryPath=${MAVEN_REPO_LOCAL}
	fi

	popd
}

export MAVEN_REPO_AS=${BUILD_DIR}/maven-repo-as
export MAVEN_REPO_LOCAL=${BUILD_DIR}/maven-repo-local

prepare_maven_repo_as ${MAVEN_REPO_AS_ZIP} ${MAVEN_REPO_AS}
if [ ! "${MAVEN_REPO_LOCAL_PRELOADED}" == "1" ]; then
	prepare_maven_repo_local ${MAVEN_REPO_LOCAL}
	install_into_maven_repo_local_artifacts_not_available_in_maven_repo_as ${BUILD_DIR}/maven-repo-extra ${MAVEN_REPO_LOCAL} ${RESTEASY_VERSION} ${TEST_GUICE} ${GUICE_VERSION} ${GOOGLE_VERSION} ${TEST_UNSUPPORTED}
fi
