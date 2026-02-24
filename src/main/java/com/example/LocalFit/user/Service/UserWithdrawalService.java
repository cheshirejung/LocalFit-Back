//package com.example.LocalFit.user.Service;
//
//import com.example.LocalFit.auth.AuthProvider;
//import com.example.LocalFit.auth.oauth2.service.OAuth2UserService;
//import com.example.LocalFit.auth.service.RedisTokenService;
//import com.example.LocalFit.global.CookieUtil;
//import com.example.LocalFit.global.exception.CustomErrorCode;
//import com.example.LocalFit.global.exception.CustomException;
//import com.example.LocalFit.user.dto.DeleteUserReqDto;
//import com.example.LocalFit.user.entity.User;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import java.util.concurrent.CompletableFuture;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class UserWithdrawalService {
//    private final RedisTokenService redisTokenService;
//    private final OAuth2UserService oAuth2UserService;
//    private final CookieUtil cookieUtil;
//    private final PasswordEncoder passwordEncoder;
//
//    @Transactional
//    public void processUserWithdrawal(User user, DeleteUserReqDto deleteUserReqDto,
//                                      HttpServletRequest request, HttpServletResponse response) {
//        try {
//            log.info("사용자 탈퇴 프로세스 시작: {}", user.getEmail());
//
//            // 1. 인증 관련 처리
//            handleAuthenticationCleanup(user, deleteUserReqDto, request, response);
//
//            // 2. 연관 데이터 삭제
//            deleteUserRelatedData(user);
//
//            log.info("사용자 탈퇴 프로세스 완료: {}", user.getEmail());
//        } catch (Exception e) {
//            log.error("사용자 탈퇴 처리 중 오류 발생: {}", user.getEmail(), e);
//            throw new CustomException(CustomErrorCode.USER_DELETION_FAILED);
//        }
//    }
//
//    private void handleAuthenticationCleanup(User user, DeleteUserReqDto deleteUserReqDto,
//                                             HttpServletRequest request, HttpServletResponse response) {
//        if (user.getProvider() == AuthProvider.LOCAL) {
//            validateLocalUserDeletion(user, deleteUserReqDto);
//        } else {
//            oauth2UserService.processOAuth2UserDeletion(user, request, response);
//        }
//
//        redisTokenService.deleteRefreshToken(user.getEmail());
//        cookieUtil.deleteCookie(response, "accessToken");
//        cookieUtil.deleteCookie(response, "refreshToken");
//        SecurityContextHolder.clearContext();
//    }
//
//    private void validateLocalUserDeletion(User user, DeleteUserReqDto deleteUserReqDto) {
//        if (deleteUserReqDto == null || deleteUserReqDto.getPassword() == null) {
//            throw new CustomException(CustomErrorCode.PASSWORD_REQUIRED);
//        }
//        if (!passwordEncoder.matches(deleteUserReqDto.getPassword(), user.getPassword())) {
//            throw new CustomException(CustomErrorCode.INVALID_PASSWORD);
//        }
//    }
//
//    private void deleteUserRelatedData(User user) {
//        CompletableFuture<Void> chatRoomsFuture = CompletableFuture
//                .runAsync(() -> chatRoomService.deleteUserChatRooms(user.getId()));
//
//        CompletableFuture<Void> meetingsFuture = CompletableFuture
//                .runAsync(() -> meetingService.deleteUserMeetings(user.getId()));
//
//        CompletableFuture<Void> feedsFuture = CompletableFuture
//                .runAsync(() -> feedService.deleteUserFeeds(user.getId()));
//
//        // 비동기 작업들이 모두 완료될 때까지 대기
//        CompletableFuture.allOf(chatRoomsFuture, meetingsFuture, feedsFuture).join();
//    }
//}