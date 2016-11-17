package io.pivotal.poc.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

@Component
@Path("/ping")
@Produces(MediaType.APPLICATION_JSON)
public class Endpoint {

    @GET
    @PreAuthorize("#oauth2.hasScope('sso-resource.read')")
    public String message() {
        return "Hello its now " + System.currentTimeMillis();
    }
}
