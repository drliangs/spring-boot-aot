package com.drlang.aot.scopes;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;

import java.util.UUID;

@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
public class RequestContext {
    //    class RequestContext{
    private String uuid = UUID.randomUUID().toString();

    public RequestContext() {
    }

    public String getUuid() {
        return uuid;
    }
//    }
}
