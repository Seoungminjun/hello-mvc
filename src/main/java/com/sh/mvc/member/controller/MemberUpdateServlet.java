package com.sh.mvc.member.controller;

import com.sh.mvc.member.model.entity.Gender;
import com.sh.mvc.member.model.entity.Member;
import com.sh.mvc.member.model.service.MemberService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;

@WebServlet("/member/memberUpdate")
public class MemberUpdateServlet extends HttpServlet {
    private MemberService memberService = new MemberService();
    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 1. 인코딩 설정
//        req.setCharacterEncoding("utf-8");

        // 2. 사용자 입력값 가져오기
//        String id = req.getParameter("id");
        Member loginMember = (Member) req.getSession().getAttribute("loginMember");
        String id = loginMember.getId();
        String newName = req.getParameter("name");
        String _newBirthday = req.getParameter("birthday");
        String newEmail = req.getParameter("email");
        String newPhone = req.getParameter("phone");
        String _newGender = req.getParameter("gender");
        String[] _newHobby = req.getParameterValues("hobby");

        // input:date는 text계열이라 작성하지 않아도 ""이 전송
        LocalDate newBirthday = _newBirthday != null && !"".equals(_newBirthday) ?
                LocalDate.parse(_newBirthday, DateTimeFormatter.ISO_DATE) :
                null;
        Gender newGender = _newGender != null ? Gender.valueOf(_newGender) : null;
        List<String> newHobby = _newHobby != null ? Arrays.asList(_newHobby) : null;
        
        // 업데이트하지 않을 데이터는 null 또는 0 대입
        Member member = new Member(id, null, newName, null, newGender, newBirthday, newEmail, newPhone, newHobby, 0, null);

        // 3. 업무로직
        int result = memberService.updateMember(member);
        // db 정보가 성공적으로 수정되었다면, 해당 내용으로 session의 속성 loginMember도 업데이트
        Member memberUpdated = memberService.findById(id);
        req.getSession().setAttribute("loginMember", memberUpdated);

        // 4. 리다이렉트 mvc/member/memberDetail
        resp.sendRedirect(req.getContextPath() + "/member/memberDetail");
    }
}