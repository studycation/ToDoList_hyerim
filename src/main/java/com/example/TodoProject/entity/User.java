package com.example.TodoProject.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users") // H2 예약어 충돌 방지용
@Getter @Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true) // 이메일을 username으로 사용
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nickname;
}
