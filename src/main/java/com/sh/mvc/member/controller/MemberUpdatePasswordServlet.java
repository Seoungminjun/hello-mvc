package com.sh.mvc.member.controller;

import com.sh.mvc.common.HelloMvcUtils;
import com.sh.mvc.member.model.entity.Member;
import com.sh.mvc.member.model.service.MemberService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@WebServlet("/member/memberUpdatePassword")
public class MemberUpdatePasswordServlet extends HttpServlet {
    private MemberService memberService = new MemberService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/member/memberUpdatePassword.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 1. 사용자 입력값 처리
        HttpSession session = req.getSession();
        Member loginMember = (Member) session.getAttribute("loginMember");
        String id = loginMember.getId();
        String oldPassowrd = HelloMvcUtils.getEncryptedPassword(req.getParameter("oldPassword"), id);
        String newPassowrd = HelloMvcUtils.getEncryptedPassword(req.getParameter("newPassword"), id);

        // 2. 업무로직
        String location = req.getContextPath();

        if(oldPassowrd.equals(loginMember.getPassword())) {
            loginMember.setPassword(newPassowrd);
            int result = memberService.updateMemberPassword(loginMember);
            session.setAttribute("msg", "비밀번호가 변경되었습니다. 😎");

            // 3. 리다이렉트 - 문자열로 복합대입 연산자 사용 +=
            location += "/member/memberDetail";
        }
        else {
            session.setAttribute("msg", "기존 비밀번호가 일치하지 않아 비밀번호 변경에 실패했습니다. 😎");
            // 3. 리다이렉트
            location += "/member/memberUpdatePassword";
        }

        resp.sendRedirect(location);
    }
}
