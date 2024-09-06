package org.example.restlogisticserp;

import jakarta.ws.rs.*;



@Path("/test")
public class TestResource {
    @GET
    @Produces("text/plain")
    public String test() {
        return "Test endpoint working!";
    }
}
