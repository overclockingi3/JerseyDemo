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
    public String getMessage() {
        return "Hello world!";
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


    @GET
    @Consumes(MediaType.TEXT_PLAIN)//注解指定解压方式接收数据类型
    @Path("/hello")
    public Response postJson(@QueryParam("name") String name) {
        return Response.status(200).entity("hello "+name).build();
    }

}
