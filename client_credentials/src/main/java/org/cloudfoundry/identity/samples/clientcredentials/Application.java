package org.cloudfoundry.identity.samples.clientcredentials;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.token.grant.client.ClientCredentialsResourceDetails;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

import static org.springframework.http.HttpMethod.GET;

@Configuration
@EnableAutoConfiguration
@ComponentScan
@Controller
public class Application {

    public static void main(String[] args) {
        if ("true".equals(System.getenv("SKIP_SSL_VALIDATION"))) {
            SSLValidationDisabler.disableSSLValidation();
            System.out.println(">>>>>>>>>>>disabled sso validation");
        }
        SpringApplication.run(Application.class, args);
    }

    @Autowired
    private ObjectMapper objectMapper;

    @Value("${ssoServiceUrl:example.com}")
    private String ssoServiceUrl;

    @Autowired
    @Qualifier("clientCredentialsRestTemplate")
    private OAuth2RestTemplate clientCredentialsRestTemplate;


    @Value("${resourceServerUrl:http://sso-resource.cfapps.pez.pivotal.io}")
    private String resourceServer;

    @RequestMapping("/")
    public String index(HttpServletRequest request, Model model) {
        return "index";
    }

    @RequestMapping("/client_credentials")
    public String clientCredentials(Model model) throws Exception {
        if (ssoServiceUrl.equals("example.com")) {
            return "configure_warning";
        }

        ResponseEntity<String> response = clientCredentialsRestTemplate.exchange("{resourceServer}/ping",
                GET, null, new ParameterizedTypeReference<String>() {
                }, resourceServer);


        model.addAttribute("token", toPrettyJsonString(getToken()));
        model.addAttribute("resourceData", response.getBody());

        return "client_credentials";
    }

    private String toPrettyJsonString(Object object) throws Exception {
        return objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(object);
    }

    @Configuration
    @EnableConfigurationProperties
    @EnableOAuth2Client
    public static class Config {
        @Bean
        @ConfigurationProperties(prefix = "security.oauth2.client")
        ClientCredentialsResourceDetails clientCredentialsResourceDetails() {
            return new ClientCredentialsResourceDetails();
        }

        @Bean
        OAuth2RestTemplate clientCredentialsRestTemplate() {
            OAuth2RestTemplate restTemplate = new OAuth2RestTemplate(clientCredentialsResourceDetails());
            return restTemplate;
        }
    }

    private Map<String, ?> getToken() throws Exception {
        OAuth2AccessToken accessToken = clientCredentialsRestTemplate.getAccessToken();
        if (accessToken != null) {
            String tokenBase64 = accessToken.getValue().split("\\.")[1];
            return objectMapper.readValue(Base64.decodeBase64(tokenBase64), new TypeReference<Map<String, ?>>() {
            });
        }
        return null;
    }
}
