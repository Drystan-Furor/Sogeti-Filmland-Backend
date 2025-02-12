# Config Spring Boot
- https://www.baeldung.com/spring-boot-https-self-signed-certificate
-
Set VM arguments: `-Xms1G -Xmx1G -server -XX:+UseParallelGC`

We can use the following command to generate our PKCS12 keystore format:
config/dco.p12
  ```shell
  keytool -genkeypair -alias filmland -keyalg RSA -keysize 2048 -storetype PKCS12 -keystore filmland.p12 -validity 3650
  ```

config/application.yaml
  ```yaml
# https://docs.spring.io/spring-boot/docs/current/reference/html/common-application-properties.html
spring:
  # ====================================================================================================================
  # = DATA SOURCE
  # ====================================================================================================================
  datasource:
    url: jdbc:mariadb://192.168.99.11:3306/filmland
    driver-class-name: org.mariadb.jdbc.Driver
    username: filmland
    password: filmland

server:
  # ====================================================================================================================
  # = SERVER - HTTPS
  # ====================================================================================================================
  port: 8443
  ssl:
    key-store: config/filmland.p12
    key-store-password: filmland
    keyStoreType: PKCS12
    keyAlias: filmland
  ```

```shell
java -cp ~/.m2/repository/com/h2database/h2/2.3.232/h2-2.3.232.jar org.h2.tools.Server -tcp -tcpAllowOthers -tcpPort 9092 -ifNotExists

```

```shell
java -cp ~/.m2/repository/com/h2database/h2/2.3.232/h2-2.3.232.jar org.h2.tools.Server -tcp -tcpAllowOthers -tcpPort 9092 -ifNotExists

```