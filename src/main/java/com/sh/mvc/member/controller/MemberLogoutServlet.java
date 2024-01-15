package com.sh.mvc.member.controller;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * 서버측 session객체의 무효화처리 invalidate
 *  - 속성으로 저장된 데이터도 모두 폐기된다.
 *  -
 */
@WebServlet("/member/memberLogout")
public class MemberLogoutServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // created 매개변수
        // - true(기본값) 세션이 있으면 세션반환, 없으면 새로 생성해서 반환
        // - false 세션이 있으면 세션반환, 없으면 null을 반환(새로 생성하지 말라는 뜻)
        HttpSession session = req.getSession(false); // 새로 생성 여부
        if(session != null){
            session.invalidate();
        }
        // 인덱스페이지로 이동 (url변경) + 뒤에 "/" 하나씩 붙여주자
        // /mvc/ = /mvc/index.jsp
        // /mvc 일 경우, contextPath 이후로 경로를 새로 생성하기 때문에 리다이렉트가 다시 일어남
        resp.sendRedirect(req.getContextPath() + "/");
    }
}