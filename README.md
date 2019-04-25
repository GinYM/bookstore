# Bookstore
> A Bookstore website built in SpringBoot

A [website](http://ec2-18-212-36-55.compute-1.amazonaws.com) deployed in AWS.

Adminportal can be access in this [url](http://ec2-18-212-36-55.compute-1.amazonaws.com/admin) Username:admin, Password:admin

## Installation

To build microservice admin,

```sh
cd admin
mvn clean package
mvn jib:dockerBuild
npm install my-crazy-module --save
```

The docker-compose files are also provided to setup nginx in your servers

```sh
cd app
docker-compose up -d
cd ../app-admin
docker-compose up -d
```
