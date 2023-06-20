package com.onetwoclass.onetwoclass.domain;

import com.onetwoclass.onetwoclass.domain.constants.Role;
import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true)
  private String email;

  private String name;

  private String password;

  private String phone;

  @Enumerated(EnumType.STRING)
  private Role role;

  private LocalDateTime registeredAt;

  private LocalDateTime modifiedAt;

}
