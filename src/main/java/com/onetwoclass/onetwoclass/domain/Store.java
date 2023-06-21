package com.onetwoclass.onetwoclass.domain;

import com.onetwoclass.onetwoclass.domain.constants.Category;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Store {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String storeName;

  @Lob
  private String explains;

  @Enumerated(EnumType.STRING)
  private Category category;

  @OneToOne
  @JoinColumn(name = "seller_id")
  private User seller;


}
