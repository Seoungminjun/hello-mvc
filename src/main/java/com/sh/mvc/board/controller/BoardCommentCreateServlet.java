package com.sh.mvc.board.controller;

import com.sh.mvc.board.model.entity.BoardComment;
import com.sh.mvc.board.model.service.BoardService;
import com.sh.mvc.notification.model.service.NotificationService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/board/boardCommentCreate")
public class BoardCommentCreateServlet extends HttpServlet {
    private BoardService boardService =new BoardService();
    private NotificationService notificationService = new NotificationService();

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 1. 사용자 입력값 처리
        long boardId = Long.parseLong(req.getParameter("boardId"));
        String memberId = req.getParameter("memberId");
        String content = req.getParameter("content");
        int commentLevel = Integer.parseInt(req.getParameter("commentLevel"));
        Long parentCommentId = null;
        try {
            parentCommentId = Long.parseLong(req.getParameter("parentCommentId"));
        } catch (NumberFormatException ignore) {}

        BoardComment comment = new BoardComment();
        comment.setBoardId(boardId);
        comment.setMemberId(memberId);
        comment.setContent(content);
        comment.setCommentLevel(commentLevel);
        comment.setParentCommentId(parentCommentId);
        System.out.println(comment);

        // 2. 업무로직
        int result = boardService.insertBoardComment(comment);

        req.getSession().setAttribute("msg", "댓글을 정상등록 했습니다.♥️♥️♥️");

        // 실시간 알림처리
        result = notificationService.notify(comment);

        // 3. redirect
        resp.sendRedirect(req.getContextPath() + "/board/boardDetail?id=" + boardId);
    }
}