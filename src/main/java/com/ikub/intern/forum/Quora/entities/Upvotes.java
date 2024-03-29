package com.ikub.intern.forum.Quora.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
public abstract class Upvotes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "id_user", referencedColumnName = "id")
    private UserEntity user;

    @Column
    private boolean active;

    @Column
    private LocalDateTime upvoteDate;

    public Upvotes(UserEntity user, boolean active, LocalDateTime upvoteDate) {
        this.user = user;
        this.active = active;
        this.upvoteDate = upvoteDate;
    }
}
