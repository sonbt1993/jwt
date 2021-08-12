package com.example.demo.entity;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "comment")
@Table(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String content;
    private LocalDateTime lastUpdate;
    @PrePersist //Trước khi lưu khi khởi tạo record
    public void prePersist() {
        lastUpdate = LocalDateTime.now();
    }
    @PreUpdate //Khi cập nhật record
    public void preUpdate() {
        lastUpdate = LocalDateTime.now();
    }

    public Comment(String content) {
        this.content = content;
    }

    @ManyToOne(fetch = FetchType.LAZY)
    @NotNull
    private User commenter; //Mỗi comment phải do một commenter viết

    @ManyToOne(fetch = FetchType.LAZY)
    private Post post; //Mỗi comment phải gắn vào một post


}
