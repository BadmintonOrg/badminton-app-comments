package models.converters;

import lib.Comment;
import models.entities.CommentEntity;

public class CommentConverter {

    public static Comment toDto(CommentEntity entity) {

        Comment dto = new Comment();
        dto.setContent(entity.getContent());
        dto.setCourt(entity.getCourt());
        dto.setUser(entity.getUser());
        dto.setId(entity.getId());
        dto.setProfanity(entity.getProfanity());

        return dto;

    }

    public static CommentEntity toEntity(Comment dto) {

        CommentEntity entity = new CommentEntity();
        entity.setId(dto.getId());
        entity.setContent(dto.getContent());
        entity.setCourt(dto.getCourt());
        entity.setUser(dto.getUser());
        entity.setProfanity(dto.getProfanity());
        return entity;

    }
}
