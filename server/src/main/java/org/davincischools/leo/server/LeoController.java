package org.davincischools.leo.server;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class LeoController {
    @GetMapping("/")
    public String index() {
        return "Leo Works! This will look up and return resources from the React browser client.";
    }
}
