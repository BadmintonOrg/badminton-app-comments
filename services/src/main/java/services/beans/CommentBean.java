package services.beans;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kumuluz.ee.discovery.annotations.DiscoverService;
import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;
import lib.Comment;
import lib.User;
import models.converters.CommentConverter;
import models.entities.CommentEntity;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import services.clients.ProfanityFilterApi;
import services.config.RestProperties;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.UriInfo;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RequestScoped
public class CommentBean {
    private Logger log = Logger.getLogger(CommentBean.class.getName());

    @Inject
    private EntityManager em;

    @Inject
    private RestProperties restProperties;

    private HttpClient httpClient;
    private ObjectMapper objectMapper;

    @Inject
    @DiscoverService(value = "badmiton-app-users-service", version = "1.0.0", environment = "dev")
    private Optional<WebTarget> target;

    @PostConstruct
    private void init() {
        httpClient = HttpClientBuilder.create().build();
        objectMapper = new ObjectMapper();
    }

    @Inject
    @RestClient
    private ProfanityFilterApi profanityFilterApi;

    public List<Comment> getComments() {

        TypedQuery<CommentEntity> query = em.createNamedQuery(
                "CommentEntity.getAll", CommentEntity.class);

        List<CommentEntity> resultList = query.getResultList();

        return resultList.stream().map(CommentConverter::toDto).collect(Collectors.toList());

    }

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

        log.log(Level.INFO,String.valueOf(restProperties.getUserDiscovery()));
        if(target.isPresent()&&restProperties.getUserDiscovery()){
            User usr = getUserFromService(comm.getUser());
            if(usr!=null)
                comm.setUserObj(usr);
        }

        return comm;
    }

    public User getUserFromService(Integer id){
        WebTarget service = target.get().path("v1/users");
        log.log(Level.INFO,String.valueOf(service.getUri()));
        try {
            HttpGet request = new HttpGet(String.valueOf("http://20.121.249.5:8080/v1/users") + "?filter=id:EQ:" + id);
            HttpResponse response = httpClient.execute(request);
            int status = response.getStatusLine().getStatusCode();
            if (status >= 200 && status < 300) {
                HttpEntity entity = response.getEntity();
                if (entity != null)
                    return getOneUser(EntityUtils.toString(entity));
            } else {
                String msg = "Remote server '"  + "' is responded with status " + status + ".";
                log.log(Level.SEVERE,msg);
                return null;
            }
        } catch (IOException e) {
            log.log(Level.SEVERE,e.getMessage());

            return null;
        }
        return null;
    }

    public User getOneUser(String json){
        try {
            ArrayList<User> ar = objectMapper.readValue(json,
                    objectMapper.getTypeFactory().constructCollectionType(List.class, User.class));
            if(ar.size()>0)
                return ar.get(0);
        } catch (JsonProcessingException e) {
            log.log(Level.SEVERE,e.getMessage());
            return null;
        }
        return null;
    }

    public User getOrgFallback(Integer id) {
        return null;
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

        CompletionStage<String> stringCompletionStage =
                profanityFilterApi.checkProfanity(commentEntity.getContent());

        stringCompletionStage.whenComplete((s, throwable) -> {
            //check if returned true or false
            boolean contains_prof = Boolean.parseBoolean(s);
            if(contains_prof){
                commentEntity.setProfanity(true);
                putComment(commentEntity.getId(),CommentConverter.toDto(commentEntity));
            }
        });
        stringCompletionStage.exceptionally(throwable -> {
            log.severe(throwable.getMessage());
            return throwable.getMessage();
        });

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
