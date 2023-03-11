package com.babyak.babyak.controller;

import com.babyak.babyak.domain.user.User;
import com.babyak.babyak.domain.user.UserRepository;
import com.babyak.babyak.dto.token.RefreshTokenDTO;
import com.babyak.babyak.dto.user.AuthResponseDTO;
import com.babyak.babyak.dto.user.InfoUpdateRequestDTO;
import com.babyak.babyak.dto.user.SignUpRequestDTO;
import com.babyak.babyak.dto.token.TokenDTO;
import com.babyak.babyak.security.jwt.JwtTokenProvider;
import com.babyak.babyak.security.oauth2.PrincipalDetails;
import com.babyak.babyak.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;

    // 로그인 완료 (기존에 가입했던 사용자)
    @GetMapping("/auth/ok/{email}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<TokenDTO> authOk(@PathVariable String email) {
        User user = userRepository.findByEmail(email);
        TokenDTO resDTO = userService.reissueToken(user.getUserId(), user.getEmail());
        return ResponseEntity.ok(resDTO);
    }


    // 구글 로그인 후 Reject 결과 알려주기 (domain/blocked/withdraw 문제로 자격 없는 유저)
    @GetMapping("/auth/reject/{email}/{reason}")
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ResponseEntity<AuthResponseDTO> reject(@PathVariable String email, @PathVariable String reason) {
        AuthResponseDTO resDTO;

        if(reason.equals("blocked"))
            resDTO = new AuthResponseDTO(email, "blocked");

        else if(reason.equals("withdraw"))
            resDTO = new AuthResponseDTO(email, "withdraw");

        else
            resDTO = new AuthResponseDTO(email, "domain");

        return ResponseEntity.ok(resDTO);
    }

    // 구글 로그인 후 회원가입 페이지로 이동
    @GetMapping("/signup/{email}")
    @ResponseStatus(HttpStatus.OK)
    public ResponseEntity<AuthResponseDTO> signup(@PathVariable String email) {
        AuthResponseDTO resDTO = new AuthResponseDTO(email, "");
        return ResponseEntity.ok(resDTO);
    }

    // 닉네임 중복 확인
    @GetMapping("/signup/check/{nickname}")
    public ResponseEntity<Boolean> checkNickname(@PathVariable String nickname) {
        if(userService.availableNickname(nickname)) {
            return new ResponseEntity<Boolean>(true, HttpStatus.OK);
        }
        return new ResponseEntity<Boolean>(false, HttpStatus.valueOf(409));
    }

    // 회원가입
    @PostMapping("/signup")
    public ResponseEntity<TokenDTO> signup(@RequestBody @Valid SignUpRequestDTO reqDTO) {
        TokenDTO tokenDTO = userService.signup(reqDTO);
        return ResponseEntity.ok(tokenDTO);
    }

    // Authentication 회원 정보
    @GetMapping("/info")
    public ResponseEntity<User> user(@AuthenticationPrincipal PrincipalDetails principalDetails) {
        User user = principalDetails.getUser();

        System.out.println("========== PrincipalDetails User : " + user);
        return ResponseEntity.ok(principalDetails.getUser());
    }

    @GetMapping("/info/test")
    public ResponseEntity<User> test() {
        PrincipalDetails principalDetails = (PrincipalDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = principalDetails.getUser();

        return ResponseEntity.ok(user);
    }

    // 회원 정보 업데이트
    @PatchMapping("/info/update")
    public ResponseEntity<User> updateInfo(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                           @RequestBody InfoUpdateRequestDTO reqDTO) {
        User user = principalDetails.getUser();
        return ResponseEntity.ok(userService.updateInfo(user, reqDTO));

    }

    // 토큰 재발급
    @PostMapping("/token/refresh")
    public ResponseEntity<TokenDTO> refresh(@RequestBody @Valid RefreshTokenDTO reqDTO) {
        TokenDTO resDTO = userService.reissueToken(reqDTO.getRefreshToken());
        return ResponseEntity.ok(resDTO);
    }


    // 로그아웃
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@RequestBody @Valid TokenDTO tokenDTO) {
        return ResponseEntity.ok(userService.logout(tokenDTO));
    }


    // 회원 자진 탈퇴
    @PostMapping("/withdraw")
    public ResponseEntity<String> withdraw(@AuthenticationPrincipal PrincipalDetails principalDetails,
                                           @RequestBody @Valid TokenDTO tokenDTO) {
        User user = principalDetails.getUser();
        return ResponseEntity.ok(userService.withdraw(user, tokenDTO));
    }
}
