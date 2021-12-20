package graphql;

import com.kumuluz.ee.graphql.annotations.GraphQLClass;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLMutation;
import io.leangen.graphql.annotations.GraphQLQuery;
import jdk.jfr.Description;
import jdk.jfr.Name;
import lib.Comment;
import services.beans.CommentBean;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@GraphQLClass
@ApplicationScoped
public class CommentMutations {
    @Inject
    private CommentBean commentBean;

    @GraphQLMutation
    public Comment addComment(@GraphQLArgument(name = "comment") Comment comm) {
        commentBean.createComemnt(comm);
        return comm;
    }

    @GraphQLMutation
    public DeleteResponse deleteComment(@GraphQLArgument(name = "id") Integer id) {
        return new DeleteResponse(commentBean.deleteComment(id));
    }

    @GraphQLMutation
    public Comment putComment(@GraphQLArgument(name = "id") Integer id,@GraphQLArgument(name = "comment") Comment comm) {
        comm = commentBean.putComment(id, comm);
        return comm;
    }
}
