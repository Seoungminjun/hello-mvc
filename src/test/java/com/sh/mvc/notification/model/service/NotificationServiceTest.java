package com.sh.mvc.notification.model.service;

import com.sh.mvc.notification.model.entity.Notification;
import com.sh.mvc.notification.model.entity.Type;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class NotificationServiceTest {
    private NotificationService notificationService;

    @BeforeEach
    void setUp() {
        this.notificationService = new NotificationService();
    }

    @DisplayName("한행의 알림 데이터를 기록한다.")
    @ParameterizedTest
    @CsvSource({
                "qwerty,NEW_COMMENT,마약시작하면바로끝 게시글에 댓글이 달렸습니다.",
                "abcde,NEW_COMMENT,안녕히계세요~ 여러분 게시글에 댓글이 달렸습니다"})
    void test1(String memberID, Type type, String content){
        // given
        assertThat(memberID).isNotNull();
        assertThat(type).isNotNull();
        assertThat(content).isNotNull();
        // when
        Notification noti = new Notification();
        noti.setMemberId(memberID);
        noti.setType(type);
        noti.setContent(content);
        int result = notificationService.insertNotification(noti);
        // then
        assertThat(result).isGreaterThan(0);
    }
    @DisplayName("특정회원의 확인안한 알림내역을 조회한다.")
    @ParameterizedTest
    @ValueSource(strings = {"qwerty", "abcde"})
    void test2(String memberId){
        // given
        assertThat(memberId).isNotNull();
        // when
        List<Notification> notifications = notificationService.findByMemberId(memberId);
        System.out.println(notifications);
        //then
        assertThat(notifications)
                .isNotNull()
                .allSatisfy((noti) -> {
                    assertThat(noti.getId()).isNotNull().isNotZero();
                    assertThat(noti.getMemberId()).isNotNull().isEqualTo(memberId);
                    assertThat(noti.getType()).isNotNull();
                    assertThat(noti.getContent()).isNotNull();
                    assertThat(noti.isChecked()).isFalse(); // 확인 안한 알림
                    assertThat(noti.getRegDate()).isNotNull();

                });
    }


}
