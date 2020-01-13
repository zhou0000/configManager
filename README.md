## configManager

* spring cloud config : 统一配置中心

* 意义：

    在分布式环境里，很多服务都要做集群部署，那就意味着这些服务都要提供一样的服务，所以他们的配置文件必须相同，我们提取一个配置中心，让他们从一个中心处拉取配置文件，这样就能保证所有的配置都相同，修改配置时，只需要修改一个地方即可，如果项目上线之后，修改配置的话需要重启服务，如果使用同一配置自动刷新，就不需要重启项目
    
* spring cloud config 就是提供配置中心的作用

* spring cloud config 架构：客户端 和 服务端

![同一配置中心架构图](https://github.com/zhou0000/configManager/blob/zhou/imgs/spring-cloud-config.png)

* server 服务端：用来提供配置文件

* client 客户端：从server中拉取配置文件

### spring cloud config server :

    1,引入依赖
    
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-config-server</artifactId>
        </dependency>
        
    2,在启动类上添加注解@EnableConfigServer
    3，修改配置文件 配置远程git信息
        spring:
          cloud:
            config:
              server:
                git:
                  uri: https://github.com/zhou0000/config-repo
                  username: zhou0000
                  password: Zhou@0086
                  basedir: F:/Demo/springboot-valid/basedir #configServer会从远端git拉取一份到本地，自定义本地git仓库
                  #search-paths: /config-demo #子目录
        远程git的配置文件如果发生改变，config-server服务端也会自动改变
    4，访问：
         {name}-{profiles}.yml
         {label}/{name}-{profiles}.properties
         {label}/{name}-{profiles}.json
         {name}-{profiles}.yml（更多看日志）
        其中：name：git上的配置文件名 如果文件名是config.yml 那么name的值就是config，如果文件名是config-dev，那么name的值还是config
              profiles：环境，开发环境dev 测试环境text 就是git配置文件名中“中划线”后面的值
              label：远程git的分支，默认是master
         注意：如果在config-demo目录下有两个配置文件分别是：config.yml 和config-dev.yml
               如果访问config-dev.yml的配置文件，他会把config.yml的文件和config-dev.yml的文件进行合并输出
               如果有config-text.yml配置文件，访问此文件也是会将config.yml的文件进行合并输出，
               所以config.yml文件一般放通用的配置信息
### Spring cloud config client ：
 
    1，引入依赖
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-config-client</artifactId>
        </dependency>
    2，启动类上不需要添加注解
    3，修改配置
        首先将application.yml文件名改为bootstroap.yml (bootstrap启动优先级高于application.yml)
        server:
          port: 10088
        spring:
          application:
            name: config-client
          cloud:
            config:
              profile: dev
              name: config
              discovery:
                enabled: true
                service-id: CONFIG-SERVER
        eureka:
          client:
            service-url:
              defaultZone: http://127.0.0.1:10086/eureka
  以上：就搭建好了配置中心的服务端和客户端，此时修改git里的配置文件，服务端会自动刷新，而客户端不会刷新
## 使用spring cloud bus 自动更新配置

* 理论：

![分析](https://github.com/zhou0000/configManager/blob/zhou/imgs/spring-cloud-bus.png)

* 修改config-server端
    
      1，添加依赖
        <dependency>
            <groupId>org.springframework.cloud</groupId>
            <artifactId>spring-cloud-starter-bus-amqp</artifactId>
        </dependency>
      2，添加配置
          rabbitmq:
            host: 192.168.117.128
            port: 5672
            username: zhou01
            password: zhou01
            virtual-host: /leyou
        management: #暴露触发消息总线的地址
          endpoints:
            web:
              exposure:
                include: "bus-refresh"

* 修改config-client 端

       1，添加依赖
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-starter-bus-amqp</artifactId>
            </dependency>
       2，在远端git添加配置   
            spring:
              rabbitmq: # MQ配置
                host: 192.168.117.128
                port: 5672
                username: zhou01
                password: zhou01
                virtual-host: /leyou
      在远端git修改配置，配置中心服务端自动刷新，而客户端访问：http://localhost:10087/actuator/bus-refresh post 请求才能自动刷新，不够智能
     
## 使用webhook实现客户端自动刷新

* 修改config-server服务端（客户端不需要做任何修改）

      1，添加依赖
            <dependency>
                <groupId>org.springframework.cloud</groupId>
                <artifactId>spring-cloud-config-monitor</artifactId>
            </dependency>
      2，修改配置
          rabbitmq:
            host: 192.168.117.128
            port: 5672
            username: zhou01
            password: zhou01
            virtual-host: /leyou
        management: #暴露触发消息总线的地址
          endpoints:
            web:
              exposure:
                #include: "bus-refresh"
                include: "*"
       3，修改服务端端口为80，也可以不用修改，如果不修改需要配nginx来监听80
       4，在git根目录setting 添加webhooks，使用内网穿透工具natapp生成一个域名，最终Payload URL路径：http://7ugkdy.natappfree.cc/monitor?path=*   Content type：选择：application/x-www-form-urlencoded
      配置好后，修改git的配置文件，会自动触发配置的webhook请求，实现配置中心客户端自动刷新


    
       
    
       
