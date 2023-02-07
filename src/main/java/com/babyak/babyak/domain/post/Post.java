package com.babyak.babyak.domain.post;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Post {
    @Id
    @Column(name = "post_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer postId;

    @NotNull
    @Column(name = "title")
    private String title;

    @CreatedDate
    @Column(updatable = false, nullable = false)
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    private LocalDateTime createdDate;

    // 이부분 없애면 오류가...ㅠㅠ 없애고 생성일만 자동으로 나타날 수 있게 하는 방법을 더 찾아볼게요...!
    @LastModifiedDate
    @Column(updatable = false, nullable = false)
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    private LocalDateTime modifiedDate;

    @NotNull
    @Column(name = "meeting_date")
    private @JsonFormat(pattern = "yyyy-MM-dd")Date meetingDate;

    @NotNull
    @Column(name = "meeting_time")
    private Integer meetingTime;

    @NotNull
    @Column(name = "meeting_site")
    private String meetingSite;

    @NotNull
    @Column(name = "max_people")
    private Integer maxPeople;

    @NotNull
    @Column(name = "preferred_food")
    private String preferredFood;

    @Column(name = "introduce")
    private String introduce;

    @NotNull
    @Column(name = "user_id")
    private Integer userId;
}
