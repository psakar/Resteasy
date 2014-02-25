# variables to be set before running the script
#
# export EAP_VERSION=6.2.0.ER6
# export BUILD_DIR=$(pwd)
# export MAVEN_REPO_AS_ZIP=${BUILD_DIR}/jboss-eap-${EAP_VERSION}-maven-repository.zip
# export RESTEASY_VERSION=2.3.7.Final-redhat-2
#
# export AS_ZIP=${BUILD_DIR}/jboss-eap-${EAP_VERSION}.zip #if set AS is unzipped
#   export AS_HOME=$BUILD_DIR/jboss-eap-$(echo $EAP_VERSION | awk -F"." '{ print $1 "." $2 }')
#   export APPEND_JAVA_OPTS_TO_AS_CONFIG=""
#   export AS_NETWORK_CONFIG="-Djava.net.preferIPv4Stack=true"
# export SHOW_INFO=1

#show info message. message displayed only if SHOW_INFO variable is set to 1
#param #1 message to display
info() {
	if [ "$SHOW_INFO" == "1" ]; then
		echo $1
	fi;
}

assert_dir_exists() {
	if [ ! -d "$1" ]; then
		mkdir -p $1
	fi
}

#param #1 file to unzip
#param #2 directory to unzip into
unzip_file() {
	assert_dir_exists $2
	if [ "$UNZIP_WITH_JAR" == "1" ]; then 
		(cd $2; jar xf $(os_specific_path $1))
	else
		unzip -q $1 -d $2
	fi
}

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

	cp $1 ${BUILD_DIR}
	unzip -q ${BUILD_DIR}/$(basename $1) -d ${MAVEN_REPO_TMP}
	# unzip -q $1 -d ${MAVEN_REPO_TMP} # slow if zip is on network share
	local MAVEN_REPO_FOLDER=$(ls ${MAVEN_REPO_TMP})
	mv ${MAVEN_REPO_TMP}/$MAVEN_REPO_FOLDER $2
}

# param #1 folder with extra artifacts not present in maven-repo-as
# param #2 folder with local maven repo

install_into_maven_repo_local_extra_artifacts() {
	local MAVEN_REPO_EXTRA=$1
	local MAVEN_REPO_LOCAL=$2

	pushd ${MAVEN_REPO_EXTRA} > /dev/null
	mvn org.apache.maven.plugins:maven-install-plugin:2.5:install-file -DgroupId=tv.bodil -DartifactId=maven-testlol-plugin -Dversion=1.5.0 -Dpackaging=jar  -Dfile=${MAVEN_REPO_EXTRA}/maven-testlol-plugin-1.5.0.jar -DlocalRepositoryPath=${MAVEN_REPO_LOCAL}
	mvn org.apache.maven.plugins:maven-install-plugin:2.5:install-file -DgroupId=tv.bodil -DartifactId=maven-testlol-plugin -Dversion=1.5.0 -Dpackaging=pom  -Dfile=${MAVEN_REPO_EXTRA}/maven-testlol-plugin-1.5.0.pom -DlocalRepositoryPath=${MAVEN_REPO_LOCAL}

	popd > /dev/null
}

# param #1 maven repos local folder
prepare_maven_repo_local() {
	if [ -d ${MAVEN_REPO_LOCAL} ]; then
	  rm -rf ${MAVEN_REPO_LOCAL}
	fi
	mkdir ${MAVEN_REPO_LOCAL}
	install_into_maven_repo_local_extra_artifacts ${BUILD_DIR}/maven-repo-extra ${MAVEN_REPO_LOCAL}
}


# param  #1 string to replace
# param  #2 replacement
# param  #3 name of file
replace_in_file() {
	if [[ "$OSTYPE" == "solaris"* ]] || [[ "$OSTYPE" == "hpux"* ]] || [[ "$OSTYPE" == "cygwin" ]]; then
		local NAME=$(basename $3)
		local TEMP_FILE=$TMP_DIR/$NAME
		cp $3 $TEMP_FILE
		sed "s/$1/$2/g" $TEMP_FILE > $3
	else
		sed -i "s/$1/$2/g" $3
	fi
}

patch_as() {
	:
}

# param  #1 name of as zip
# param  #2 replacement
# param  #3 APPEND_JAVA_OPTS_TO_AS_CONFIG
# param  #4 AS_NETWORK_CONFIG
unzip_as() {
	local AS_DISTR=$1
	local AS_HOME=$2
	local APPEND_JAVA_OPTS_TO_AS_CONFIG=$3
	local AS_NETWORK_CONFIG=$4
	local AS_HOME_PARENT_DIR="$(dirname "$AS_HOME")"
	info  "Unzip AS $AS_VERSION into $AS_HOME_PARENT_DIR"
	rm -rf $AS_HOME
	unzip_file "$AS_DISTR" $AS_HOME_PARENT_DIR
	if [ "$OSTYPE" == "cygwin" ]; then
		chmod u+rwx $AS_HOME/bin/*.bat  #for windows
	fi
	if [ ! "$APPEND_JAVA_OPTS_TO_AS_CONFIG" == "" ]; then
		info "Append to AS standalone.conf JAVA_OPTS $APPEND_JAVA_OPTS_TO_AS_CONFIG"
		if [ "$OSTYPE" == "cygwin" ]; then
			echo "set JAVA_OPTS=$APPEND_JAVA_OPTS_TO_AS_CONFIG %JAVA_OPTS%" >> $AS_HOME/bin/standalone.conf.bat
		else
			echo JAVA_OPTS=\"$APPEND_JAVA_OPTS_TO_AS_CONFIG \$JAVA_OPTS\" >> $AS_HOME/bin/standalone.conf
		fi
	fi
	local AS_CONFIG_FILE="$AS_HOME/bin/standalone.conf"
	if [ "$OSTYPE" == "cygwin" ]; then
		AS_CONFIG_FILE="$AS_HOME/bin/standalone.conf.bat"
	fi
	# patch standalone.conf to replace preferIPv4Stack=true with value from $AS_NETWORK_CONFIG
	info "AS_NETWORK_CONFIG $AS_NETWORK_CONFIG"
	replace_in_file "-Djava.net.preferIPv4Stack=true" "$AS_NETWORK_CONFIG" $AS_CONFIG_FILE

	patch_as
}

export TMP_DIR=/tmp
export MAVEN_REPO_AS=${BUILD_DIR}/maven-repo-as
export MAVEN_REPO_LOCAL=${BUILD_DIR}/maven-repo-local

if [ ! "${MAVEN_REPO_AS_ZIP}" == "" ]; then
	prepare_maven_repo_as ${MAVEN_REPO_AS_ZIP} ${MAVEN_REPO_AS}
fi
if [ ! "$AS_ZIP" == "" ]; then
	unzip_as "$AS_ZIP" "$AS_HOME" "$APPEND_JAVA_OPTS_TO_AS_CONFIG" "$AS_NETWORK_CONFIG"
fi
if [ ! "${MAVEN_REPO_LOCAL_PRELOADED}" == "1" ]; then
	prepare_maven_repo_local ${MAVEN_REPO_LOCAL}
fi
