package models.entities;

import javax.persistence.*;

@Entity
@Table(name = "badminton_comments")
@NamedQueries(value =
        {
                @NamedQuery(name = "CommentEntity.getAll",
                        query = "SELECT oe FROM CommentEntity oe")
        })
public class CommentEntity {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Integer id;

        @Column(name = "court_id")
        private Integer court;

        @Column(name = "user_id")
        private Integer user;

        @Column(name = "content")
        private String content;

        @Column(name = "profanity")
        private boolean profanity;

        public boolean getProfanity() {
                return profanity;
        }

        public void setProfanity(boolean prof) {
                this.profanity = prof;
        }


        public Integer getId() {
                return id;
        }

        public void setId(Integer id) {
                this.id = id;
        }

        public Integer getUser() {
                return user;
        }

        public void setUser(Integer user) {
                this.user = user;
        }

        public Integer getCourt() {
                return court;
        }

        public void setCourt(Integer court) {
                this.court = court;
        }

        public String getContent() {
                return content;
        }

        public void setContent(String content) {
                this.content = content;
        }


}
