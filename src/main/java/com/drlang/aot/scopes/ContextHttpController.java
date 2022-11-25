package com.drlang.aot.scopes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@ResponseBody

public class ContextHttpController {
    @Autowired
    @Lazy
    public RequestContext requestContext;

    @GetMapping("/scopes/context")
    public String uuid() {
        return requestContext.getUuid();
    }
}
