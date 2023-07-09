package com.onetwoclass.onetwoclass.domain.form.review;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Builder
public class AddReviewForm {

  @NotNull(message = "리뷰 대상의 스케쥴 아이디를 기재해주세요.")
  private Long scheduleId;

  @NotNull(message = "별점을 기재해주세요.")
  @Min(value = 1, message = "별점은 1 이상 5 이하입니다.")
  @Max(value = 5, message = "별점은 1 이상 5 이하입니다.")
  private Integer star;

  @NotNull(message = "내용을 기재해주세요.")
  @Length(min = 5, message = "최소 5글자를 기재해주세요.")
  private String text;

}
