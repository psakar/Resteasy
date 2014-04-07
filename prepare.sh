MAVEN_REPO_LOCAL=maven-repo-local
AS_DISTRIBUTION=/home/kanovotn/RESTEasy/EAP5/EAP_5.3.0.ER3/jboss-eap-5.3
RESTEASY_VERSION=1.2.1.GA_CP02_patch03

install_into_maven_repo_local() {
	local FILE=$1
	local GROUP_ID=$2
	local ARTIFACT_ID=$3
	local VERSION_ID=$4
	local PACKAGING=$5

	mvn org.apache.maven.plugins:maven-install-plugin:2.5:install-file -DgroupId=${GROUP_ID} -DartifactId=${ARTIFACT_ID} -Dversion=${VERSION_ID} -Dpackaging=${PACKAGING} -Dfile=${FILE} -DlocalRepositoryPath=${MAVEN_REPO_LOCAL}

}

# param #1 maven repos local folder
prepare_maven_repo_local() {
	if [ -d ${MAVEN_REPO_LOCAL} ]; then
		rm -rf ${MAVEN_REPO_LOCAL}
	fi
	mkdir ${MAVEN_REPO_LOCAL}
	install_into_maven_repo_local ${AS_DISTRIBUTION}/resteasy/lib/resteasy-jaxb-provider.jar org.jboss.resteasy resteasy-jaxb-provider ${RESTEASY_VERSION} jar
	install_into_maven_repo_local ${AS_DISTRIBUTION}/resteasy/lib/resteasy-jaxrs.jar org.jboss.resteasy resteasy-jaxrs ${RESTEASY_VERSION} jar
	install_into_maven_repo_local ${AS_DISTRIBUTION}/resteasy/lib/jaxrs-api.jar org.jboss.resteasy jaxrs-api ${RESTEASY_VERSION} jar
}


prepare_maven_repo_local
