package com.onetwoclass.onetwoclass.domain.entity;

import com.onetwoclass.onetwoclass.domain.dto.DayClassDto;
import com.onetwoclass.onetwoclass.domain.form.dayclass.UpdateDayClassForm;
import java.time.LocalDateTime;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@EntityListeners(AuditingEntityListener.class)
public class DayClass {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String dayClassName;

  @Lob
  private String explains;

  private Integer price;

  @CreatedDate
  private LocalDateTime registeredAt;

  @LastModifiedDate
  private LocalDateTime modifiedAt;

  @ManyToOne
  @JoinColumn(name = "store_id")
  private Store store;

  public void updateDayClass(UpdateDayClassForm updateDayClassForm) {

    if (updateDayClassForm.getToChangeDayClassName() != null) {
      this.dayClassName = updateDayClassForm.getToChangeDayClassName();
    }

    if (updateDayClassForm.getToChangePrice() != null) {
      this.price = updateDayClassForm.getToChangePrice();
    }

    if (updateDayClassForm.getToChangeExplains() != null) {
      this.explains = updateDayClassForm.getToChangeExplains();
    }

  }

  public static DayClassDto toDayClassDto(DayClass dayClass) {
    return DayClassDto.builder()
        .dayClassName(dayClass.dayClassName)
        .explains(dayClass.explains)
        .price(dayClass.price)
        .registeredAt(dayClass.registeredAt)
        .modifiedAt(dayClass.modifiedAt)
        .build();
  }

}
