package com.overc_i3.jerseydemo;

import com.overc_i3.model.RequestJson;
import com.overc_i3.model.ResponseJson;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;


@Path("/")
public class TestApi {

    //GET注解设置接受请求类型为GET
    @GET
    // 与Produces对应的是@Consumes
    @Produces(MediaType.TEXT_PLAIN)
    public String getMessage() {
        return "Hello world!";
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)//注解指定解压方式接收数据类型
    @Produces(MediaType.APPLICATION_JSON)//注解指定压缩方式返回数据类型
    @Path("/postJersey")
    public  Response postJersey(RequestJson requestJson) {
        ResponseJson responseJson = new ResponseJson();
        responseJson.setResult(0);
        responseJson.setData("hello" + requestJson.getName());
        return Response.status(200).entity(responseJson).build();
    }

    @GET
    @Consumes(MediaType.TEXT_PLAIN)//注解指定解压方式接收数据类型
    @Path("/hello")
    public Response postJson(@QueryParam("name") String name) {
        return Response.status(200).entity("hello "+name).build();
    }

}
