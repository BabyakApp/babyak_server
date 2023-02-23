package com.babyak.babyak.service;

import com.babyak.babyak.DTO.noshow.NoshowPK;
import com.babyak.babyak.domain.noshow.Noshow;
import com.babyak.babyak.domain.noshow.NoshowRepository;
import com.babyak.babyak.domain.user.User;
import com.babyak.babyak.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NoshowService {
    private final NoshowRepository noshowRepository;
    private final UserRepository userRepository;

    public void reportUser(Integer postId, Integer userId){
        if (!noshowRepository.existsByPostIdAndUserId(postId, userId)){
            Noshow noshow = new Noshow(postId, userId);
            noshowRepository.save(noshow);

            User user = userRepository.findByUserId(userId);
            user.setNoShow(user.getNoShow()+1);
            userRepository.save(user);
        }
    }

    public void findUserList(Integer postId){
        // chatroom db에 접근해서 postId에 해당하는 유저 아이디 리스트 반환
        // 아이디에 맞는 닉네임 찾아서 List<아이디, 닉네임> 형식으로 반환.
    }
}
