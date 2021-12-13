package v1.resources;

import lib.Comment;
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
public class CommentResource {
    private Logger log = Logger.getLogger(CommentResource.class.getName());

    @Inject
    private CommentBean commentBean;

    @Context
    protected UriInfo uriInfo;

    @GET
    public Response getComments() {

        List<Comment> comments = commentBean.getComments(uriInfo);

        return Response.status(Response.Status.OK).entity(comments).build();
    }

    @GET
    @Path("/{commentId}")
    public Response getComment(@PathParam("commentId") Integer courtId) {

        Comment comm = commentBean.getComment(courtId);

        if (comm == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.OK).entity(comm).build();
    }

    @POST
    public Response createComment(Comment comm) {

        //check for profanity with external api
        if (comm.getCourt() == null || comm.getCourt() == null || comm.getContent() == null ) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        else {
            comm = commentBean.createComemnt(comm);
        }

        return Response.status(Response.Status.CREATED).entity(comm).build();

    }

    @DELETE
    @Path("{commentId}")
    public Response deleteComment(@PathParam("commentId") Integer commentId){

        boolean deleted = commentBean.deleteComment(commentId);

        if (deleted) {
            return Response.status(Response.Status.NO_CONTENT).build();
        }
        else {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
    }

    @PUT
    @Path("{commentId}")
    public Response putComment(@PathParam("commentId") Integer commentId,
                             Comment comm){

        comm = commentBean.putComment(commentId, comm);

        if (comm == null) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        return Response.status(Response.Status.NOT_MODIFIED).build();

    }
}

