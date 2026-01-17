# springboot-apikey-starter
Spring Security with API Key

### Reference Documentation
For further reference, please consider the following sections:

* [Spring Web](https://docs.spring.io/spring-boot/docs/4.0.1/reference/htmlsingle/index.html#web)
* [Spring Security](https://docs.spring.io/spring-boot/docs/4.0.1/reference/htmlsingle/index.html#web.security)
* [Spring Boot Actuator](https://docs.spring.io/spring-boot/docs/4.0.1/reference/htmlsingle/index.html#actuator)

### Maven Parent overrides

Due to Maven's design, elements are inherited from the parent POM to the project POM.
While most of the inheritance is fine, it also inherits unwanted elements like `<license>` and `<developers>` from the parent.
To prevent this, the project POM contains empty overrides for these elements.
If you manually switch to a different parent and actually want the inheritance, you need to remove those overrides.
