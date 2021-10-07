package org.dailymenu.services.rest;

import org.dailymenu.parser.ParserProvider;
import org.dailymenu.services.parser.ParserService;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;
import java.util.stream.Collectors;

@Path("/restaurant")
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

    @POST
    @Path("/supported")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces("application/json")
    public List<String> getRestaurants(List<String> placeIds) {
        System.out.println("Checking " + String.join(", ", placeIds));

        return placeIds.stream().filter(id -> parserService.hasProviderFor(id)).collect(Collectors.toList());
    }
}
