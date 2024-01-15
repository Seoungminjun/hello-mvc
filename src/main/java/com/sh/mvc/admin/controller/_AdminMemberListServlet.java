package com.sh.mvc.admin.controller;

import com.sh.mvc.common.HelloMvcUtils;
import com.sh.mvc.member.model.entity.Member;
import com.sh.mvc.member.model.service.MemberService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * <pre>
 * 페이징
 * 1. content ⭐쿼리를 어떻게 날리느냐에 대한 문제
 *  - page 현재페이지
 *  - limit 한페이지당 표시할 개체수
 *
 * 2. pagebar
 *  - page 현재페이지
 *  - limit 한페이지당 표시할 개체수
 *  - totalCount 전체 개체수
 *  - totalPage 전체 페이지수
 *  - pagebarSize 페이지바의 숫자개수
 *  - pageNo 페이지 증감변수
 *  - pagebarStart | pagebarEnd 페이지 증감변수의 범위
 *  - url 요청 url
 *
 */
//@WebServlet("/admin/memberList")
public class _AdminMemberListServlet extends HttpServlet {
    private MemberService memberService = new MemberService();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 1. 사용자입력값 가져오기
        // -> 가장 처음에 목록 접근시에는 page의 값이 없음 -> 따라서 기본값을 배정한다.
        int page = 1; // 기본값
        int limit = 10;
        try {
            // -> 더불어 가장 최초 접근 시에는 getParameter에서 예외가 발생
            // 그래서 try~catch 절에 넣어서 사용한다.
            page = Integer.parseInt(req.getParameter("page"));
        } catch(NumberFormatException ignore){}
        Map<String, Object> param = Map.of("page", page, "limit", limit);
        System.out.println(param);

        // 2. 업무로직
        // DB에서 데이터 가져오기
        // a. content영역 : 전체조회 쿼리 + RowBounds | Top-n분석 쿼리
        List<Member> members = memberService.findAll(param);
        req.setAttribute("members", members);

        // 페이지바 작업
        // b. pagebar 영역
        int totalCount = memberService.getTotalCount();
        String url = req.getRequestURI();
        String pagebar = HelloMvcUtils.getPagebar(page, limit, totalCount, url);
        req.setAttribute("pagebar", pagebar);

        // 3. view단 처리
        req.getRequestDispatcher("/WEB-INF/views/admin/memberList.jsp").forward(req, resp);
    }
}