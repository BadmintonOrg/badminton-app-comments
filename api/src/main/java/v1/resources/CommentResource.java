package v1.resources;

import com.kumuluz.ee.cors.annotations.CrossOrigin;
import lib.Comment;
import org.eclipse.microprofile.openapi.annotations.Operation;
import org.eclipse.microprofile.openapi.annotations.enums.SchemaType;
import org.eclipse.microprofile.openapi.annotations.headers.Header;
import org.eclipse.microprofile.openapi.annotations.media.Content;
import org.eclipse.microprofile.openapi.annotations.media.Schema;
import org.eclipse.microprofile.openapi.annotations.parameters.Parameter;
import org.eclipse.microprofile.openapi.annotations.parameters.RequestBody;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponse;
import org.eclipse.microprofile.openapi.annotations.responses.APIResponses;
import services.beans.CommentBean;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.logging.Logger;

@ApplicationScoped
@Path("/comments")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
@CrossOrigin(supportedMethods = "GET, POST, HEAD, DELETE, OPTIONS")
public class CommentResource {
    private Logger log = Logger.getLogger(CommentResource.class.getName());

    @Inject
    private CommentBean commentBean;

    @Context
    protected UriInfo uriInfo;

    @Operation(description = "Get all comments in a list", summary = "Get all comments")
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "List of comments",
                    content = @Content(schema = @Schema(implementation = Comment.class, type = SchemaType.ARRAY)),
                    headers = {@Header(name = "X-Total-Count", description = "Number of objects in list")}
            )})
    @GET
    public Response getComments() {

        List<Comment> comments = commentBean.getComments(uriInfo);

        return Response.status(Response.Status.OK).entity(comments).build();
    }

    @Operation(description = "Get data for a comments.", summary = "Get data for a comment")
    @APIResponses({
            @APIResponse(responseCode = "200",
                    description = "Comment",
                    content = @Content(
                            schema = @Schema(implementation = Comment.class))
            )})
    @GET
    @Path("/{commentId}")
    public Response getComment(@Parameter(description = "Comment ID.", required = true)
                                   @PathParam("commentId") Integer courtId) {

        Comment comm = commentBean.getComment(courtId);

        if (comm == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.OK).entity(comm).build();
    }

    @Operation(description = "Add comment.", summary = "Add comment")
    @APIResponses({
            @APIResponse(responseCode = "201",
                    description = "Comment successfully added."
            ),
            @APIResponse(responseCode = "405", description = "Validation error .")
    })
    @POST
    public Response createComment(@RequestBody(
            description = "DTO object with comment data.",
            required = true, content = @Content(
            schema = @Schema(implementation = Comment.class))) Comment comm) {

        //check for profanity with external api
        if (comm.getCourt() == null || comm.getCourt() == null || comm.getContent() == null ) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        else {
            comm = commentBean.createComemnt(comm);
        }

        return Response.status(Response.Status.CREATED).entity(comm).build();

    }

    @Operation(description = "Delete comment.", summary = "Delete comment")
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Comment successfully deleted."
            ),
            @APIResponse(
                    responseCode = "404",
                    description = "Not found."
            )
    })
    @DELETE
    @Path("{commentId}")
    public Response deleteComment(@Parameter(description = "Comment ID.", required = true)
                                      @PathParam("commentId") Integer commentId){

        boolean deleted = commentBean.deleteComment(commentId);

        if (deleted) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @Operation(description = "Update data for a comment.", summary = "Update comment")
    @APIResponses({
            @APIResponse(
                    responseCode = "200",
                    description = "Comment successfully updated."
            )
    })
    @PUT
    @Path("{commentId}")
    public Response putComment(@Parameter(description = "Comment ID.", required = true)
                                   @PathParam("commentId") Integer commentId,
                               @RequestBody(
                                       description = "DTO object with comment data.",
                                       required = true, content = @Content(
                                       schema = @Schema(implementation = Comment.class))) Comment comm){

        comm = commentBean.putComment(commentId, comm);

        if (comm == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.NOT_MODIFIED).build();

    }
}

