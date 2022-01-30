package com.reg;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;


@SpringBootApplication
@RestController
@RefreshScope
public class RegisterApplication {
    @Autowired
    @Lazy
    private RestTemplate template;
    @Value("${ipQuery}")
    private String ipQueryUrl;
    @Value("${validChecking}")
    private String validationUrl;

    @GetMapping ("/register")
    public String userRegister(@RequestParam(required = true) String name,
                               @RequestParam(required = true) String pwd,
                               @RequestParam(required = true) String ip)  {
        if (name.isEmpty() || pwd.isEmpty() || ip.isEmpty()) {
            return new RegisterReturn("input is invalid").toJson();
        }

        if (!template.getForObject(validationUrl, Boolean.class, pwd)) {
            return new RegisterReturn("pwd is invalid").toJson();
        }

        IPQuery ipQuery = template.getForObject(ipQueryUrl+"/"+ip, IPQuery.class);
        String country = ipQuery.getCountry();
        //this "Canada" can be setup in config server as well
        if (!country.equals("Canada")) {
            return new RegisterReturn("user is not eligible to register").toJson();
        }

        return new RegisterReturn(name, country).toJson();

    }

    public static void main(String[] args) {
        SpringApplication.run(RegisterApplication.class, args);
    }

    @Bean
    public RestTemplate template() {
        return new RestTemplate();
    }
}
