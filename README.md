# JerseyDemo
IDEA+Jersey+Tomcat Build RESTful API

部分内容参考自：https://github.com/waylau/Jersey-2.x-User-Guide/blob/master/Chapter%204.%20Application%20Deployment%20and%20Runtime%20Environments%20%E5%BA%94%E7%94%A8%E9%83%A8%E7%BD%B2%E5%92%8C%E8%BF%90%E8%A1%8C%E6%97%B6%E7%8E%AF%E5%A2%83/4.7.%20Servlet-based%20Deployment%20%E5%9F%BA%E4%BA%8E%20Servlet%20%E7%9A%84%E9%83%A8%E7%BD%B2.md

maven库：http://mvnrepository.com/
# 环境：
IDEA Ultimate 2017.2.2 + JDK 1.8 + Jersey 2 + Maven + Tomcat 9.0.0.M22
# 首先创建项目：
选中Web Application(3.1)框架，勾选Create web.xml，IDE会在创建项目时创建web.xml配置文件。
![创建项目](http://upload-images.jianshu.io/upload_images/5544786-ad988eed04ed37c6.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
创建项目后右键选中项目，添加框架支持，选中Maven，随即生成pom.xml文件。这里会弹出Event对话框，会有提示你Enable Auto-Import，这里我们Enable一下。
![添加框架](http://upload-images.jianshu.io/upload_images/5544786-0197ce7e12874553.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
然后开始通过maven添加依赖jar包，引用Jersey，pom.xml配置如下：
```
    <groupId>com.overc_i3.jerseydemo</groupId>
    <artifactId>JerseyDemo</artifactId>
    <version>1.0-SNAPSHOT</version>
    <properties>
        <!--maven默认的java源值、目标值版本是1.5，修改成1.8-->
        <maven.compiler.source>1.8</maven.compiler.source>
        <maven.compiler.target>1.8</maven.compiler.target>
        <jersey.version>2.25</jersey.version>
    </properties>

    <dependencies>
        <dependency>
            <!--jersey服务端包，将 Jersey 当做 Servlet-->
            <groupId>org.glassfish.jersey.containers</groupId>
            <artifactId>jersey-container-servlet</artifactId>
            <version>${jersey.version}</version>
        </dependency>
        <dependency>
            <!--服务端核心包-->
            <groupId>org.glassfish.jersey.core</groupId>
            <artifactId>jersey-server</artifactId>
            <version>${jersey.version}</version>
        </dependency>
        <dependency>
            <!--客户端包，封装了一些客户端操作的类-->
            <groupId>org.glassfish.jersey.core</groupId>
            <artifactId>jersey-client</artifactId>
            <version>${jersey.version}</version>
        </dependency>
        <dependency>
            <groupId>org.glassfish.jersey.media</groupId>
            <artifactId>jersey-media-json-jackson</artifactId>
            <version>${jersey.version}</version>
        </dependency>
    </dependencies>
```
接下来是配置web.xml，项目里的路径是web/WEB-INF/web.xml，这里我们将 Jersey 当做 Servlet：
```
    <servlet>
        <!--Servlet配置-->
        <servlet-name>jerseyServlet</servlet-name>
        <servlet-class>org.glassfish.jersey.servlet.ServletContainer</servlet-class>

        <init-param>
            <!--包名-->
            <param-name>jersey.config.server.provider.packages</param-name>
            <param-value>com.overc_i3.jerseydemo</param-value>
        </init-param>
        <load-on-startup>1</load-on-startup>
    </servlet>

    <servlet-mapping>
        <servlet-name>jerseyServlet</servlet-name>
        <url-pattern>/*</url-pattern>
    </servlet-mapping>
```
也可将 Jersey 当做 Servlet Filter，如下配置：
```
<filter>
        <filter-name>MyApplication</filter-name>
        <filter-class>org.glassfish.jersey.servlet.ServletContainer</filter-class>
        <init-param>
            ...
        </init-param>
    </filter>
    ...
    <filter-mapping>
        <filter-name>MyApplication</filter-name>
        <url-pattern>/myApp/*</url-pattern>
    </filter-mapping>
```
<init-param> 元素内容将取决于你如何决定资源配置不同的 Jersey 资源。

最后一步配置，在输出中把jar包全部加进去
![在输出中加入库文件](http://upload-images.jianshu.io/upload_images/5544786-7d6174e219a06413.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
配置完成，开始写上API代码，首先在src\main\java中根据之前配置的包建好对应的包，我这里配置的是com\overc_i3\jerseydemo，这里使用Json做交互，先建model：
```
package com.overc_i3.model;

import java.util.List;

public class ReqJson {
    private String id;
    private List<ListBean> list;

    public List<ListBean> getList() {
        return list;
    }

    public void setList(List<ListBean> list) {
        this.list = list;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public static class ListBean {

        private String maxname;

        public String getMaxname() {
            return maxname;
        }

        public void setMaxname(String maxname) {
            this.maxname = maxname;
        }
    }
}
```
创建TestApi类：
```
package com.overc_i3.jerseydemo;

import com.overc_i3.model.ReqJson;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

@Path("/")
public class TestApi {

    //GET注解设置接受请求类型为GET
    @GET
    // 与Produces对应的是@Consumes
    @Produces(MediaType.TEXT_PLAIN)
    //用作测试服务启动情况
    public String getMessage() {
        return "Hello world!";
    }

    @GET
    @Consumes(MediaType.TEXT_PLAIN)//注解指定解压方式接收数据类型
    @Path("/hello")
    public Response postJson(@QueryParam("name") String name) {
        return Response.status(200).entity("hello "+name).build();
    }


    @POST
    @Consumes(MediaType.APPLICATION_JSON)//注解指定解压方式接收数据类型
    @Produces(MediaType.APPLICATION_JSON)//注解指定压缩方式返回数据类型
    @Path("/postJson2")
    public Response postJson2(ReqJson msg) {
        ReqJson reqJson = new ReqJson();
        reqJson.setId(msg.getId());
        List<ReqJson.ListBean> listBeanList = new ArrayList<>();
        for (int i = 0; i < msg.getList().size(); i++) {
            ReqJson.ListBean listBean = new ReqJson.ListBean();
            listBean.setMaxname(msg.getList().get(i).getMaxname());
            listBeanList.add(listBean);
        }
        reqJson.setList(listBeanList);

        return Response.status(200).entity(reqJson).build();
    }
}
```
现在配置Tomcat运行一下：
点击Run >Edit Configurations… > “+” > Tomcat Server > Local，加入Tomcat，选择Deployment tab, 点击 “+”, 选择唯一的Artifact，点击"OK"即可。
![Tomcat配置](http://upload-images.jianshu.io/upload_images/5544786-8ab2830d40561b98.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
配置好之后直接运行，在浏览器访问http://localhost:8080
![默认地址](http://upload-images.jianshu.io/upload_images/5544786-b1bc6be9d13f5470.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

![Get hello](http://upload-images.jianshu.io/upload_images/5544786-7925e8a1de983dac.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)

到这里RESTful API已经正常运行，接下来再用Fiddler调用API（注意设置请求格式Content-Type: application/json）：
![调用API](http://upload-images.jianshu.io/upload_images/5544786-7e8cf93ccbce4d10.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
![查看调API结果](http://upload-images.jianshu.io/upload_images/5544786-ec795559a09c7053.png?imageMogr2/auto-orient/strip%7CimageView2/2/w/1240)
到这里RESTful API已经搭建完成，下面是部署的一些其它“姿势”：
>#### 自定义 Application 子类
>如果你的继承 [Application](http://jax-rs->spec.java.net/nonav/2.0/apidocs/javax/ws/rs/core/Application.html) 类来提供有关根资源类的列表（getresources()）和单身（getsingletons()），即你的 JAX-RS 应用模型，然后你需要注册一个 javax.ws.rs.Application [原名] 名称的 Servlet 或 Servlet 过滤器作为 web 应用程序的初始化参数，在 web.xml 中进行部署描述：
配置 Jersey 容器 Servlet 或者 过滤器来自定义 Application 子类
>```
><init-param>
>    <param-name>javax.ws.rs.Application</param-name>
>    <param-value>org.foo.MyApplication</param-value>
></init-param>
>```
>Jersey 将考虑所有 Application 实现的 getClasses() 和 getSingletons() 方法的返回。
注意：JAX-RS 规范定义的配置名称确实是 javax.ws.rs.Application 而不是 javax.ws.rs.core.Application

>#### Jersey 扫描包
>如果配置属性无需设置，要部署应用程序只包括存储在特定的包的资源和提供者，那么你可以指示 Jersey 自动扫描这些包，这样就能自动注册找到的任何资源和提供者：
配置 Jersey 的 Servlet 或者 Filter 来扫描包
>```
><init-param>
>    <param-name>jersey.config.server.provider.packages</param-name>
>    <param-value>
>        org.foo.myresources,org.bar.otherresources
>    </param-value>
></init-param>
><init-param>
>    <param-name>jersey.config.server.provider.scanning.recursive</param-name>
>    <param-value>false</param-value>
></init-param>
>```
>Jersey 将会自动发现被选中的资源和提供者。你可以通过设置 >jersey.config.server.provider.scanning.recursive 属性来决定 Jersey 是否扫描子包。默认值是 true , 即启用递归扫描子包。
