# Bookstore
> A Bookstore website built in SpringBoot

[![NPM Version][npm-image]][npm-url]
[![Build Status][travis-image]][travis-url]
[![Downloads Stats][npm-downloads]][npm-url]

A [website](ec2-54-157-39-180.compute-1.amazonaws.com) deployed in AWS.

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
