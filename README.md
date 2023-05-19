# interlok-swagger-codegen-generators

[![GitHub tag](https://img.shields.io/github/tag/adaptris/interlok-swagger-codegen-generators.svg)](https://github.com/adaptris/interlok-swagger-codegen-generators/tags)
[![license](https://img.shields.io/github/license/adaptris/interlok-swagger-codegen-generators.svg)](https://github.com/adaptris/interlok-swagger-codegen-generators/blob/develop/LICENSE)
[![Actions Status](https://github.com/adaptris/interlok-swagger-codegen-generators/actions/workflows/gradle-publish.yml/badge.svg)](https://github.com/adaptris/interlok-swagger-codegen-generators/actions)
[![CircleCI](https://circleci.com/gh/adaptris/interlok-swagger-codegen-generators.svg?style=svg)](https://circleci.com/gh/adaptris/interlok-swagger-codegen-generators) [![codecov](https://codecov.io/gh/adaptris/interlok-swagger-codegen-generators/branch/develop/graph/badge.svg)](https://codecov.io/gh/adaptris/interlok-swagger-codegen-generators)
[![CodeQL](https://github.com/adaptris/interlok-swagger-codegen-generators/workflows/CodeQL/badge.svg)](https://github.com/adaptris/interlok-swagger-codegen-generators/security/code-scanning)
[![Known Vulnerabilities](https://snyk.io/test/github/adaptris/interlok-swagger-codegen-generators/badge.svg?targetFile=build.gradle)](https://snyk.io/test/github/adaptris/interlok-swagger-codegen-generators?targetFile=build.gradle)
[![Closed PRs](https://img.shields.io/github/issues-pr-closed/adaptris/interlok-swagger-codegen-generators)](https://github.com/adaptris/interlok-swagger-codegen-generators/pulls?q=is%3Apr+is%3Aclosed)

## Note
This is a light copy of https://github.com/swagger-api/swagger-codegen-generators.

All codegen codes and associated templates related to specific languages (java, js, c++ ...) haven been removed.
Some dependency also have been removed to make the project lighter.

**InterlokAdapterCodegen** and **InterlokChannelCodegen** and their moustache templates haven been added to the project so we can use them in the interlok ui.
Their goal is to convert a swagger configuration (simple rest services) to an adapter configuration supporting those http rest services.

## Overview
**Swagger Codegen Generators** project is a set of classes and templates ([Handlebars](https://jknack.github.io/handlebars.java)) used by [Swagger Codegen 3.0.0 project](https://github.com/swagger-api/swagger-codegen/tree/3.0.0) in its code generation process for a specific language or language framework. The main differents with **Swagger Codegen 2.x.x** are:

- **Handlebars as template engine:** with Handelbars feature is possible to create more logic-less templates.
- **OAS 3 support:** generator classes work with OpenAPI Specification V3.

More details about these and more differences are referenced at [https://github.com/swagger-api/swagger-codegen/releases/tag/v3.0.0](https://github.com/swagger-api/swagger-codegen/releases/tag/v3.0.0)

### Prerequisites
You need the following installed and available in your $PATH:

* Java 8 (http://java.oracle.com)
* Apache maven 3.0.4 or greater (http://maven.apache.org/)

## License information on Generated Code

The Swagger Codegen project is intended as a benefit for users of the Swagger / Open API Specification.  The project itself has the [License](#license) as specified.  In addition, please understand the following points:

* The templates included with this project are subject to the [License](#license).
* Generated code is intentionally _not_ subject to the parent project license

When code is generated from this project, it shall be considered **AS IS** and owned by the user of the software.  There are no warranties--expressed or implied--for generated code.  You can do what you wish with it, and once generated, the code is your responsibility and subject to the licensing terms that you deem appropriate.
