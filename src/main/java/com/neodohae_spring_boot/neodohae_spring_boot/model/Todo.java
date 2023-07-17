package com.neodohae_spring_boot.neodohae_spring_boot.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Entity
@Table(name = "Todos")
public class Todo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String title;

    private String description;

    @Column(name = "startTime", nullable = false)
    private LocalDateTime startTime;

    @Column(name = "endTime", nullable = false)
    private LocalDateTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", columnDefinition = "ENUM('TODO','DOING','DONE')")
    private Status status = Status.TODO;

    @Column(name = "repeatGroupId")
    private Integer repeatGroupId;

    @Column(name = "repeatEndTime")
    private LocalDateTime repeatEndTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "repeatType", columnDefinition = "ENUM('NONE','DAILY','WEEKLY','MONTHLY','YEARLY')")
    private RepeatType repeatType = RepeatType.NONE;

    @CreatedDate
    @Column(name = "createdAt", updatable = false, nullable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column(name = "updatedAt", nullable = false)
    private LocalDateTime updatedAt;

    // multiple todos to one user
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId", nullable = false) // TODO: nullable = false로 EDIT한거 말하기
    private User user;

    @OneToMany(mappedBy = "todo", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private Set<TodoUserMap> todoUserMaps;

    public enum Status {
        TODO,
        DOING,
        DONE
    }

    public enum RepeatType {
        NONE,
        DAILY,
        WEEKLY,
        MONTHLY,
        YEARLY
    }

}

