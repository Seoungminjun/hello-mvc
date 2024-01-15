package com.sh.mvc.board.model.service;

import com.sh.mvc.board.model.entity.Board;
import com.sh.mvc.board.model.vo.BoardVo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class BoardServiceTest {
    private BoardService boardService;

    @BeforeEach
    public void beforeEach(){
        boardService = new BoardService();
    }

    @DisplayName("boardService 객체는 null이 아닙니다.")
    @Test
    public void test1(){
        assertThat(boardService).isNotNull();
    }

//    게시글 전체조회
    @DisplayName("게시글 전체 조회")
    @Test
    public void test2(){
        List<Board> boards = boardService.findAll();
        assertThat(boards).isNotNull();
        boards.forEach((board) -> {
            System.out.println(board);
            assertThat(board.getId()).isNotEqualTo(0);
            assertThat(board.getMemberId()).isNotNull();
        });
    }
//    게시글 페이징 조회
    @DisplayName("게시글은 10건씩 조회할 수 있습니다.")
    @ParameterizedTest
    @ValueSource(ints = {1, 2, 3, 4, 5, 6})
    public void test3(int page){
        assertThat(page).isGreaterThan(0);
        System.out.println(page);

        int limit = 10;
        Map<String, Object> param = new HashMap<>();
        param.put("page", page);
        param.put("limit", limit);
        List<BoardVo> boards = boardService.findAll(param);
        System.out.println(boards);
        assertThat(boards).isNotNull();
        assertThat(boards.size()).isLessThanOrEqualTo(limit);
    }
//    전체게시글수 조회
    @DisplayName("전체 게시글 수 조회할 수 있습니다.")
    @Test
    public void test4(){
        int totalCount = boardService.getTotalCount();
        System.out.println(totalCount);
        assertThat(totalCount).isNotNull();
        assertThat(totalCount).isGreaterThanOrEqualTo(0);
    }
//    게시글 한건 조회
    @DisplayName("게시글 한건을 조회할 수 있습니다.")
    @Test
    public void test5(){
        long id = 1;
        Board board = boardService.findById(id);
        System.out.println(board);
        assertThat(board).isNotNull();
        assertThat(board.getId()).isEqualTo(id);
        assertThat(board.getMemberId()).isNotNull();
    }
//    게시글 등록
    @Disabled
    @DisplayName("게시글을 등록할 수 있습니다.")
    @Test
    public void test6(){
        String title = "오늘은 금요일";
        String memberId = "abcde";
        String content = "금요일 조아";
        BoardVo board = new BoardVo();
        board.setTitle("제목");
        board.setContent("내용");

        int result = boardService.insertBoard(board);

        // test 내용
        assertThat(result).isEqualTo(1);
    }

//    게시글 수정
    @Disabled
    @DisplayName("게시글을 수정할 수 있습니다.")
    @Test
    public void test7(){
        // 제목, 내용 수정
        long id = 62;
        String newTitle = "내일은 토요일";
        String newContent = "토요일은 더 조아";

        BoardVo board = boardService.findById(id);
        board.setTitle(newTitle);
        board.setContent(newContent);
        int result = boardService.updateBoard(board);

        assertThat(result).isGreaterThan(0);

        Board updateBoard = boardService.findById(id);
        assertThat(updateBoard).isNotNull();
        assertThat(updateBoard.getId()).isEqualTo(id);
        assertThat(board.getMemberId()).isEqualTo(updateBoard.getMemberId());
        assertThat(updateBoard.getTitle()).isEqualTo(newTitle);
        assertThat(updateBoard.getContent()).isEqualTo(newContent);
    }

//    게시글 삭제
    @Disabled
    @DisplayName("게시글을 삭제할 수 있습니다.")
    @Test
    public void test8(){
        long id = 63;
        Board board = boardService.findById(id);
        assertThat(board).isNotNull();

        int result = boardService.deleteBoard(id);
        assertThat(result).isEqualTo(1);

        Board board2 = boardService.findById(id);
        assertThat(board2).isNull();
    }
}
