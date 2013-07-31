Random Name Generator
=====================

This is a random name generator written in java. Algorithm is mentioned in this [blogpost](http://www.skorks.com/2009/07/how-to-write-a-name-generator-in-ruby/) by [skorks](https://github.com/skorks).

Build
-----
This project uses Gradle build. 

To generate a jar file use

    gradle jar
    
To generate [Jersey](https://jersey.java.net/) based REST service use

    gradle war

Deployment
----------

To deploy this project in OpenShift, create a new account at [http://openshift.redhat.com](http://openshift.redhat.com)

Install rhc gem

    sudo gem install rhc
    gem update rhc
    rhc setup

Cretae namespace

    rhc domain create -n <namespace>

Create test app

    rhc app-create test jbossews-2.0

Add github repo

    cd test
    git remote add upstream -m master git@github.com:mail2vks/random-name-generator.git
    git pull -s recursive -X theirs upstream master 

Push changes to OpenShift

    git push

Jenkins configuration for Execute shell for Gradle build

    # Build/update libs and run user pre_build and build
    #gear build

    # Set a writable Gradle Home Dir
    echo SETTING GRADLE HOME
    export GRADLE_USER_HOME=$OPENSHIFT_DATA_DIR/gradle
 
    # Mark gradlew as executable
    chmod +x gradlew
    ./gradlew build

    # Run tests here

    # Deploy new build

    # Stop app
    $GIT_SSH $upstream_ssh 'gear stop --conditional'

    # Push content back to application
    rsync ~/.m2/ $upstream_ssh:~/.m2/
    rsync $WORKSPACE/build/libs/. $upstream_ssh:'${OPENSHIFT_REPO_DIR}webapps/'
    rsync $WORKSPACE/.openshift/ $upstream_ssh:'${OPENSHIFT_REPO_DIR}.openshift/'

After build and depoyment, app can be accessed at 

    http://test-<namespace>.rhcloud.com/repo/randomName

Reference for README.md

> [https://github.com/ramr/openshift-tomcat7-websockets/blob/master/README.md](https://github.com/ramr/openshift-tomcat7-websockets/blob/master/README.md)

