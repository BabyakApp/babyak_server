package com.babyak.babyak.service;

import com.babyak.babyak.DTO.chat.CheckResponse;
import com.babyak.babyak.DTO.user.IdAndNicknameDTO;
import com.babyak.babyak.mongo.ChatroomRepository;
import com.babyak.babyak.domain.noshow.Noshow;
import com.babyak.babyak.domain.noshow.NoshowRepository;
import com.babyak.babyak.domain.user.User;
import com.babyak.babyak.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NoshowService {
    private final NoshowRepository noshowRepository;
    private final UserRepository userRepository;
    private final ChatroomRepository chatroomRepository;

    // NoshowController/reportUser
    public CheckResponse reportUser(Integer postId, Integer userId){
        if (!noshowRepository.existsByPostIdAndUserId(postId, userId)){
            Noshow noshow = new Noshow(postId, userId, 0, false);
            noshowRepository.save(noshow);
        }

        Noshow noshow = noshowRepository.findByPostIdAndUserId(postId, userId);
        noshow.increaseNum();
        Integer participateUser = getParticipateUserNumber(postId);
        int reportNumber = noshow.getReportNumber()/2;

        if(participateUser/3 <= reportNumber && !noshow.isReported()){
            noshow.setReported(true);
            User user = userRepository.findByUserId(userId);
            user.setNoShow(user.getNoShow()+1);
            userRepository.save(user);
        }

        noshowRepository.save(noshow);
        if(noshow.isReported()){
            return new CheckResponse(true, "노쇼 신고가 확인되어 해당 유저에게 패널티가 부과되었습니다.");
        }
        return new CheckResponse(false, "노쇼 신고 확인 중 입니다. 다른 참여자의 투표에 따라 패널티 부과 여부가 결정됩니다.");
    }

    private Integer getParticipateUserNumber(Integer postId) {
        Integer chatroomUser = chatroomRepository.findByIdx(postId.longValue()).getCurrentNumber();
        Integer noshowUser = noshowRepository.findByPostId(postId).size();

        return chatroomUser - noshowUser;
    }

    // NoshowController/findUserList
    public List<IdAndNicknameDTO> findUserList(Integer postId){
        // chatroom db에 접근해서 postId에 해당하는 유저 아이디 리스트 반환
        List<Integer> userList = findUserListByPostId(postId);
        // 아이디에 맞는 닉네임 찾아서 List<아이디, 닉네임> 형식으로 반환.
        List<IdAndNicknameDTO> userIdAndNicknameList = new ArrayList<>();
        for (Integer integer : userList) {
            IdAndNicknameDTO idAndNicknameDTO = new IdAndNicknameDTO();
            idAndNicknameDTO.setUserId(integer);

            String nickname = userRepository.findByUserId(integer).getNickname();
            idAndNicknameDTO.setNickname(nickname);

            userIdAndNicknameList.add(idAndNicknameDTO);
        }
        return userIdAndNicknameList;
    }

    private List<Integer> findUserListByPostId(Integer postId) {
        return chatroomRepository.findByIdx(postId.longValue()).getUserList();
    }
}
