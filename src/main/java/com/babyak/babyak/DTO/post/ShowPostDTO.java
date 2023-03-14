package com.babyak.babyak.DTO.post;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ShowPostDTO {
    private Integer postId;
    private String title;
    private @JsonFormat(pattern = "yyyy-MM-dd")
    Date meetingDate;
    private Integer meetingTime;
    private String meetingSite;
    private Integer maxPeople;
    private String preferredFood;
    private String introduce;
    private String hostName;
    private String hostDept;
    private Integer hostStudentId; // 학번 앞에 두자리만
    private Integer hostUserId;
    private Integer currentUser;
}
