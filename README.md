# Spree

Tiny utilities for SPRing 3 (thREE).

## JndiPropertyPlaceholderConfigure

Usage is as follows.

context.xml

```xml
<Context>
  <Environment name="datasource.password" value="root$password"/>
</Context>
```

applicationContext.xml

```xml
<bean id="propertyConfigure" class="com.yo1000.spree.beans.factory.config.JndiPropertyPlaceholderConfigure">
  <property name="locations" value="classpath:config.properties"/>
  <property name="jndiOverride" value="true"/>
</bean>
<bean id="dataSource" class="org.springframework.jdbc.datasource.DriverManagerDataSource">
  <property name="driverClassName" value="org.postgresql.Driver"/>
  <property name="url" value="jdbc:postgresql://host:port/database"/>
  <property name="username" value="root"/>
  <property name="password" value="${datasource.password}"/>
</bean>
```
