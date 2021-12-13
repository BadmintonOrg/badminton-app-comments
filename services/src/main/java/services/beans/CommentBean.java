package services.beans;

import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import lib.Comment;
import models.converters.CommentConverter;
import models.entities.CommentEntity;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RequestScoped
public class CommentBean {
    private Logger log = Logger.getLogger(CommentBean.class.getName());

    @Inject
    private EntityManager em;

    public List<Comment> getComments(UriInfo uriInfo) {

        QueryParameters queryParameters = QueryParameters.query(uriInfo.getRequestUri().getQuery()).defaultOffset(0)
                .build();

        return JPAUtils.queryEntities(em, CommentEntity.class, queryParameters).stream()
                .map(CommentConverter::toDto).collect(Collectors.toList());
    }

    public Comment getComment(Integer id) {

        CommentEntity commentEntity = em.find(CommentEntity.class, id);

        if (commentEntity == null) {
            throw new NotFoundException();
        }

        Comment comm = CommentConverter.toDto(commentEntity);

        return comm;
    }

    public Comment createComemnt(Comment comm) {

        CommentEntity commentEntity = CommentConverter.toEntity(comm);

        try {
            beginTx();
            em.persist(commentEntity);
            commitTx();
        }
        catch (Exception e) {
            rollbackTx();
        }

        if (commentEntity.getId() == null) {
            throw new RuntimeException("Entity was not persisted");
        }

        return CommentConverter.toDto(commentEntity);
    }

    public boolean deleteComment(Integer id) {

        CommentEntity comm = em.find(CommentEntity.class, id);

        if (comm != null) {
            try {
                beginTx();
                em.remove(comm);
                commitTx();
            }
            catch (Exception e) {
                rollbackTx();
            }
        }
        else {
            return false;
        }

        return true;
    }

    public Comment putComment(Integer id, Comment comm) {

        CommentEntity c = em.find(CommentEntity.class, id);

        if (c == null) {
            return null;
        }

        CommentEntity updatedCommentEntity = CommentConverter.toEntity(comm);

        try {
            beginTx();
            updatedCommentEntity.setId(c.getId());
            updatedCommentEntity = em.merge(updatedCommentEntity);
            commitTx();
        }
        catch (Exception e) {
            rollbackTx();
        }

        return CommentConverter.toDto(updatedCommentEntity);
    }

    private void beginTx() {
        if (!em.getTransaction().isActive()) {
            em.getTransaction().begin();
        }
    }

    private void commitTx() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().commit();
        }
    }

    private void rollbackTx() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
    }
}
