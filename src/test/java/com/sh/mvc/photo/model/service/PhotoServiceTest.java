package com.sh.mvc.photo.model.service;

import com.sh.mvc.photo.model.entity.Photo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;

class PhotoServiceTest {
    private PhotoService photoService;
    static final int LIMIT = 5; // 한페이지당 게시물수

    @BeforeEach
    void setUp() {
        this.photoService = new PhotoService();
    }
    @DisplayName("PhotoService는 null이 아니다.")
    @Test
    void test() {
        assertThat(photoService).isNotNull();
    }

    @DisplayName("전체 게시물수 조회")
    @Test
    void test1() {
        // given
        // when
        int totalCount = photoService.getTotalCount();
        // then
        assertThat(totalCount).isGreaterThanOrEqualTo(0);
    }

    @DisplayName("페이지별 게시물 조회")
    @ParameterizedTest
    @MethodSource("pageProvider")
    void test2(int page) {
        // given
        assertThat(page).isNotZero();
        // when
        Map<String, Object> param = Map.of("page",page,"limit",LIMIT);
        List<Photo> photos = photoService.findAll(param);
        // then
        assertThat(photos)
                .isNotNull()
                .isNotEmpty()
                .allSatisfy((photo) -> {
                    assertThat(photo.getId()).isNotNull().isNotZero();
                    assertThat(photo.getContent()).isNotNull();
                    assertThat(photo.getOriginalFilename()).isNotNull();
                    assertThat(photo.getRenamedFilename()).isNotNull();
                    assertThat(photo.getRegDate()).isNotNull();
                })
                .size().isLessThanOrEqualTo(LIMIT);

    }

    /**
     *  총게시물수 -> 전체페이지수 조회 -> Stream으로 변환
     * @return
     */
    public static Stream<Integer> pageProvider() {
        PhotoService photoService = new PhotoService();
        int totalCount = photoService.getTotalCount();
        int totalPage = (int)Math.ceil((double)totalCount / LIMIT);
        return IntStream.rangeClosed(1, totalPage).boxed();
    }
}