package com.onetwoclass.onetwoclass.domain.entity;

import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Chatting {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Lob
  private String text;

  @ManyToOne
  @JoinColumn(name = "customer_id")
  private Member customer;

  @ManyToOne
  @JoinColumn(name = "seller_id")
  private Member seller;

  private LocalDateTime registeredAt;

}
