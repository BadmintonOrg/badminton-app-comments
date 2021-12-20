package graphql;

import com.kumuluz.ee.graphql.annotations.GraphQLClass;
import com.kumuluz.ee.graphql.classes.Filter;
import com.kumuluz.ee.graphql.classes.Pagination;
import com.kumuluz.ee.graphql.classes.PaginationWrapper;
import com.kumuluz.ee.graphql.classes.Sort;
import com.kumuluz.ee.graphql.utils.GraphQLUtils;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLQuery;
import jdk.jfr.Description;
import jdk.jfr.Name;
import lib.Comment;
import services.beans.CommentBean;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@GraphQLClass
@ApplicationScoped
public class CommentQueries {

    @Inject
    private CommentBean commentBean;

    @GraphQLQuery
    public PaginationWrapper<Comment> getComments(@GraphQLArgument(name = "pagination") Pagination pagination,
                                                       @GraphQLArgument(name = "sort") Sort sort,
                                                       @GraphQLArgument(name = "filter") Filter filter) {

        return GraphQLUtils.process(commentBean.getComments(), pagination, sort, filter);
    }

    @GraphQLQuery
    public Comment getComment(@GraphQLArgument(name = "id") Integer id) {
        return commentBean.getComment(id);
    }


}
