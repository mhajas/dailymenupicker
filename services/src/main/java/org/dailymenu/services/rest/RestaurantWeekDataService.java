package org.dailymenu.services.rest;

import org.dailymenu.parser.ParserProvider;
import org.dailymenu.services.parser.ParserService;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

@Path("/weekData")
public class RestaurantWeekDataService {


    @Inject
    private ParserService parserService;

    @GET
    @Path("{googleID}")
    @Produces("application/json")
    public Response getThisWeekMenu(@PathParam("googleID") String googleId) {

        ParserProvider parserProvider = parserService.getParserProvider(googleId);

        if (parserProvider == null) {
            return Response.status(404).build();
        }

        return Response.status(Response.Status.OK).entity(parserProvider.parse()).build();
    }
}
