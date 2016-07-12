# Service Discovery with Zookeeper and Spring Framework

## Description
This is an example for service discovery with zookeeper (curator) and spring. There are NO dependencies on spring-cloud*/spring-boot. If you want to use spring-boot, please take a look at spring-cloud-zookeeper.

## (Very) brief overview
There are two war-s - *hello-service* and *name-service*. *hello-service* can only say "Hello" and *name-service* knows how to say a name. Therefore *hello-service* needs to find an instance of *name-service* via service discovery in order to output a full sentence.

## How to install and run
1. Run "mvn clean package" to build all the projects
2. Deploy hello-service.war and name-service.war onto a servlet container (e.g. tomcat, jetty, etc.)
3. Start a zookeeper server
4. Start the servlet container
5. Try to access hello-service like this: "http://localhost:8080/name-service/?name=World&client=restTemplate"
6. If it says "Hello, World" everything is working fine. Otherwise you'll have to troubleshoot.

## Configuration options
There are a few configuration options you may want to tweak if you don't have everything deployed on local servers with default settings.

**zookeeper connect string** - in spring.xml in both hello-service and name-service, edit the "connectString" property for the "curatorFramework" bean

**server port** - in spring.xml in both hello-service and name-service, edit the "port" property for the "serviceInstance" bean

**anything else** - you are on your own, but it's not really that hard - it's a very simple barebones spring webmvc application

## Other notes
There are 2 rest clients available for the hello-service - RestTemplate and Feign. You can test both by switching the "client" request parameter in the url:
* hello-service/?name=World&client=restTemplate - will use RestTemplate
* hello-service/?name=World&client=feign - will use Feign

You may notice spring-feign-zookeeper has a bunch of code directly copied from spring-cloud-netflix-core. There is one thing I like there and that's the Spring MVC annotations integration. I could have added a dependency to spring-cloud-netflix-core but it drags a bunch of stuff with itself, including spring-boot. And I am not the biggest fan of spring-boot yet.
