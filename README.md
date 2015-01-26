# stackify-log-servlet

[![Build Status](https://travis-ci.org/stackify/stackify-log-servlet.png)](https://travis-ci.org/stackify/stackify-log-servlet)
[![Coverage Status](https://coveralls.io/repos/stackify/stackify-log-servlet/badge.png?branch=master)](https://coveralls.io/r/stackify/stackify-log-servlet?branch=master)

This project contains J2EE and JAX-RS servlet filters for capturing web request details that will be attached to log messages that you send to Stackify. 

Errors and Logs Overview:

http://docs.stackify.com/m/7787/l/189767

Sign Up for a Trial:

http://www.stackify.com/sign-up/

stackify-log-servlet needs to be used in conjunction with one of our java logging libraries.
- https://github.com/stackify/stackify-log-log4j12 (version 1.0.5 or greater)
- https://github.com/stackify/stackify-log-logback (version 1.0.5 or greater)

The web request details will be attached to exceptions that are sent to Stackify. Here are the details that we will capture:
- User
- User IP address
- Server name
- Request protocol
- Request method (GET, POST, PUT, DELETE)
- Request URL
- Referral URL
- HTTP headers
- HTTP cookies (names only)
- HTTP session attributes (names only)
- Query string parameters

Note: *We do not currently capture any details from the POST/PUT body.*

In addition to the web request details, we will also generate a transaction id in the filter. This transaction id will be added to all messages that are logged from that request thread. This gives you an easy way to identify all messages that were logged from the same request.

## J2EE Servlet Usage

Add the StackifyLogFilter servlet filter and mapping to the web-app element in your web.xml file.

```xml
<web-app>
...
    <filter>
        <filter-name>StackifyLogFilter</filter-name>
        <filter-class>com.stackify.log.servlet.StackifyLogFilter</filter-class>
    </filter>

    <filter-mapping>
        <filter-name>StackifyLogFilter</filter-name>
        <url-pattern>/*</url-pattern>
    </filter-mapping>
...
</web-app>
```

## JAX-RS Servlet Usage

Add the StackifyLogFilter servlet filter to the servlet element in your web.xml file.

```xml
<servlet>
    ...
    <init-param>
        <param-name>jersey.config.server.provider.classnames</param-name>
        <param-value>com.stackify.log.servlet.jaxrs.StackifyLogFilter</param-value>
    </init-param>
    ...
</servlet>
```

Jersey Note: *This will only work with Jersey >= version 2.4 because of the following bug:*
- https://java.net/jira/browse/JERSEY-1960.

## Installation

Add it as a maven dependency:
```xml
<dependency>
    <groupId>com.stackify</groupId>
    <artifactId>stackify-log-servlet</artifactId>
    <version>1.1.0</version>
</dependency>
```

## License

Copyright 2014 Stackify, LLC.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.

