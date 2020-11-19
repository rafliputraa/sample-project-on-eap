package com.example.sampleeapdemo.route;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestParamType;
import org.springframework.stereotype.Component;

@Component
public class SampleRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {

        restConfiguration()
                .port("{{conf.port}}")
                .component("jetty")
                .contextPath("/v1")
                .host("{{server.address}}")
        ;

        rest()
                .consumes("application/json")
                .produces("application/json")
                .get("/print").id("print user's name")
                .param().name("firstName")
                .name("lastName").name("company")
                .type(RestParamType.body).endParam()
                .to("direct:getSampleData")
        ;

        from("direct:getSampleData")
                .unmarshal().json(JsonLibrary.Jackson)
                .setBody(simple("Hello World! from EAP \n ${body[firstName]} \n ${body[lastName]} \n ${body[company][name]} \n ${body[company][sector]}"))
        ;
    }
}
