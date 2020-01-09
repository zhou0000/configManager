# configManager
spring cloud config : 统一配置中心
意义：
    在分布式环境里，很多服务都要做集群部署，那就意味着这些服务都要提供一样的服务，所以他们的配置文件必须相同，
    我们提取一个配置中心，让他们从一个中心处拉取配置文件，这样就能保证所有的配置都相同，修改配置时，只需要修改一个地方即可
spring cloud config 就是提供配置中心的作用
spring cloud config 架构：客户端 和 服务端
  server 服务端：用来提供配置文件
  client 客户端：从server中拉取配置文件
spring cloud config server :
    1，引入依赖
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-config-server</artifactId>
        </dependency>
    2，在启动类上添加注解@EnableConfigServer
    3，修改配置文件 配置远程git信息
        spring:
          cloud:
            config:
              server:
                git:
                  uri: https://github.com/zhou0000/config-server.git
                  username: zhou0000
                  password: Zhou@0086
                  basedir: F:/Demo/springboot-valid/basedir #configServer会从远端git拉取一份到本地，自定义本地git仓库
                  #search-paths: /config yml目录
 
       
    
       
