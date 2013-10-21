# unzip maven local repo as

unzip $MAVEN_REPO_AS_ZIP
mv jboss-eap-6.2.0.Beta1-maven-repository maven-repo-as

mvn org.apache.maven.plugins:maven-install-plugin:2.5:install-file -DgroupId=tv.bodil -DartifactId=maven-testlol-plugin -Dversion=1.5.0 -Dpackaging=jar  -Dfile=maven-repo-extra/maven-testlol-plugin-1.5.0.jar -DlocalRepositoryPath=maven-repo-local
mvn org.apache.maven.plugins:maven-install-plugin:2.5:install-file -DgroupId=tv.bodil -DartifactId=maven-testlol-plugin -Dversion=1.5.0 -Dpackaging=pom  -Dfile=maven-repo-extra/maven-testlol-plugin-1.5.0.pom -DlocalRepositoryPath=maven-repo-local

mvn org.apache.maven.plugins:maven-install-plugin:2.5:install-file -DgroupId=org.jboss.resteasy -DartifactId=resteasy-guice -Dversion=2.3.7.Final-redhat-2 -Dpackaging=jar -Dfile=maven-repo-extra/resteasy-guice-2.3.7.Final-redhat-2.jar -DlocalRepositoryPath=maven-repo-local
mvn org.apache.maven.plugins:maven-install-plugin:2.5:install-file -DgroupId=org.jboss.resteasy -DartifactId=resteasy-guice -Dversion=2.3.7.Final-redhat-2 -Dpackaging=pom -Dfile=maven-repo-extra/resteasy-guice-2.3.7.Final-redhat-2.pom -DlocalRepositoryPath=maven-repo-local

mvn org.apache.maven.plugins:maven-install-plugin:2.5:install-file -DgroupId=org.jboss.resteasy -DartifactId=resteasy-fastinfoset-provider  -Dversion=2.3.7.Final-redhat-2 -Dpackaging=jar -Dfile=maven-repo-extra/resteasy-fastinfoset-provider-2.3.7.Final-redhat-2.jar -DlocalRepositoryPath=maven-repo-local
mvn org.apache.maven.plugins:maven-install-plugin:2.5:install-file -DgroupId=org.jboss.resteasy -DartifactId=resteasy-fastinfoset-provider  -Dversion=2.3.7.Final-redhat-2 -Dpackaging=pom -Dfile=maven-repo-extra/resteasy-fastinfoset-provider-2.3.7.Final-redhat-2.pom -DlocalRepositoryPath=maven-repo-local

mvn org.apache.maven.plugins:maven-install-plugin:2.5:install-file -DgroupId=com.google.inject -DartifactId=guice -Dversion=3.0-redhat-2 -Dpackaging=jar -Dfile=maven-repo-extra/guice-3.0-redhat-2.jar  -DlocalRepositoryPath=maven-repo-local
mvn org.apache.maven.plugins:maven-install-plugin:2.5:install-file -DgroupId=com.google.inject -DartifactId=guice -Dversion=3.0-redhat-2 -Dpackaging=pom -Dfile=maven-repo-extra/guice-3.0-redhat-2.pom  -DlocalRepositoryPath=maven-repo-local

mvn org.apache.maven.plugins:maven-install-plugin:2.5:install-file -DgroupId=com.google.inject -DartifactId=guice-parent -Dversion=3.0-redhat-2 -Dpackaging=pom -Dfile=maven-repo-extra/guice-parent-3.0-redhat-2.pom  -DlocalRepositoryPath=maven-repo-local
mvn org.apache.maven.plugins:maven-install-plugin:2.5:install-file -DgroupId=com.google -DartifactId=google -Dversion=5-redhat-2 -Dpackaging=pom -Dfile=maven-repo-extra/google-5-redhat-2.pom  -DlocalRepositoryPath=maven-repo-local

mvn org.apache.maven.plugins:maven-install-plugin:2.5:install-file -DgroupId=org.jboss.resteasy -DartifactId=resteasy-jdk-http  -Dversion=2.3.7.Final-redhat-2 -Dpackaging=jar -Dfile=maven-repo-extra/resteasy-jdk-http-2.3.7.Final-redhat-2.jar  -DlocalRepositoryPath=maven-repo-local
mvn org.apache.maven.plugins:maven-install-plugin:2.5:install-file -DgroupId=org.jboss.resteasy -DartifactId=resteasy-jdk-http  -Dversion=2.3.7.Final-redhat-2 -Dpackaging=pom -Dfile=maven-repo-extra/resteasy-jdk-http-2.3.7.Final-redhat-2.pom  -DlocalRepositoryPath=maven-repo-local

mvn org.apache.maven.plugins:maven-install-plugin:2.5:install-file -DgroupId=org.jboss.resteasy -DartifactId=resteasy-netty  -Dversion=2.3.7.Final-redhat-2 -Dpackaging=jar -Dfile=maven-repo-extra/resteasy-netty-2.3.7.Final-redhat-2.jar -DlocalRepositoryPath=maven-repo-local
mvn org.apache.maven.plugins:maven-install-plugin:2.5:install-file -DgroupId=org.jboss.resteasy -DartifactId=resteasy-netty  -Dversion=2.3.7.Final-redhat-2 -Dpackaging=pom -Dfile=maven-repo-extra/resteasy-netty-2.3.7.Final-redhat-2.pom -DlocalRepositoryPath=maven-repo-local
