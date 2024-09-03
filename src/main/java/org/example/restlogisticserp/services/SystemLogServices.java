package org.example.restlogisticserp.services;

import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.example.restlogisticserp.database.SystemLogDBUtils;
import org.example.restlogisticserp.models.SystemLog;

import java.util.List;


@Path("/systemlogs")
public class SystemLogServices {

    @POST
    @Path("/create")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createSystemLog(SystemLog systemLog) {
        SystemLogDBUtils.createSystemLog(systemLog.getSeverity(), systemLog.getSource(), systemLog.getIpAddress(), systemLog.getMessage(), systemLog.getUserId());
        return Response.ok(systemLog).build();
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAllSystemLogs() {
        List<SystemLog> logs = SystemLogDBUtils.fetchAllSystemLogs();
        return Response.ok(logs).build();
    }


}
