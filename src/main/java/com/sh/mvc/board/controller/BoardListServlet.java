package com.sh.mvc.board.controller;

import com.sh.mvc.board.model.service.BoardService;
import com.sh.mvc.board.model.vo.BoardVo;
import com.sh.mvc.common.HelloMvcUtils;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@WebServlet("/board/boardList")
public class BoardListServlet extends HttpServlet {
    private BoardService boardService = new BoardService();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 1. content
        // page
        // limit
        int page = 1;
        int limit = 10;
        try {
            page = Integer.parseInt(req.getParameter("page"));
        } catch (NumberFormatException ignore){};
        Map<String, Object> param = new HashMap<>();
        param.put("page", page);
        param.put("limit", limit);

        // content영역 값 가져오기
        List<BoardVo> boards = boardService.findAll(param);
        req.setAttribute("boards", boards);
        System.out.println(boards);

        // 페이지바
        int totalCount = boardService.getTotalCount();
        String url = req.getRequestURI();
        String pagebar = HelloMvcUtils.getPagebar(page, limit, totalCount, url);
        req.setAttribute("pagebar", pagebar);

        req.getRequestDispatcher("/WEB-INF/views/board/boardList.jsp").forward(req, resp);
    }
}