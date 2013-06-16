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

This project is deployed in Tomcat7 running on OpenShift PAAS at [http://randomname-viveks.rhcloud.com/repo/randomName](http://randomname-viveks.rhcloud.com/repo/randomName)

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

After build and depoyment, app can be accessed at 

    [http://test-<namespace>.rhcloud.com/repo/randomName](http://test-<namespace>.rhcloud.com/repo/randomName)

Reference for README.md

    [https://github.com/ramr/openshift-tomcat7-websockets/blob/master/README.md](https://github.com/ramr/openshift-tomcat7-websockets/blob/master/README.md)

