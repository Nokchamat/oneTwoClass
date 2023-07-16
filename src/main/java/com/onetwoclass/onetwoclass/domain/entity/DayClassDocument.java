package com.onetwoclass.onetwoclass.domain.entity;

import static org.springframework.data.elasticsearch.annotations.DateFormat.date_hour_minute_second_millis;
import static org.springframework.data.elasticsearch.annotations.DateFormat.epoch_millis;

import com.onetwoclass.onetwoclass.domain.dto.DayClassDto;
import com.onetwoclass.onetwoclass.domain.form.dayclass.UpdateDayClassForm;
import java.time.LocalDateTime;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.Mapping;
import org.springframework.data.elasticsearch.annotations.MultiField;
import org.springframework.data.elasticsearch.annotations.Setting;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Document(indexName = "dayclass")
@Mapping(mappingPath = "/elastic/mappings.json")
@Setting(settingPath = "/elastic/settings.json")
public class DayClassDocument {

  @Id
  @Field(type = FieldType.Keyword)
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private String id;

  @Field(type = FieldType.Text)
  private String dayClassName;

  @Lob
  @Field(type = FieldType.Keyword)
  private String explains;

  @Field(type = FieldType.Keyword)
  private Integer price;

  @Field(type = FieldType.Keyword)
  private Long storeId;

  @Field(type = FieldType.Date, format = {date_hour_minute_second_millis, epoch_millis})
  private LocalDateTime registeredAt;

  @Field(type = FieldType.Date, format = {date_hour_minute_second_millis, epoch_millis})
  private LocalDateTime modifiedAt;

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

    this.modifiedAt = LocalDateTime.now();

  }

  public static DayClassDto toDayClassDto(DayClassDocument dayClassDocument) {
    return DayClassDto.builder()
        .dayClassId(dayClassDocument.getId())
        .dayClassName(dayClassDocument.getDayClassName())
        .explains(dayClassDocument.getExplains())
        .price(dayClassDocument.getPrice())
        .registeredAt(dayClassDocument.getRegisteredAt())
        .modifiedAt(dayClassDocument.getModifiedAt())
        .build();
  }

}
