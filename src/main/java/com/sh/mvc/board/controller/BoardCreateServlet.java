package com.sh.mvc.board.controller;

import com.sh.mvc.board.model.entity.Attachment;
import com.sh.mvc.board.model.service.BoardService;
import com.sh.mvc.board.model.vo.BoardVo;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;

@WebServlet("/board/boardCreate")
public class BoardCreateServlet extends HttpServlet {

    private BoardService boardService = new BoardService();
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/board/boardCreate.jsp").forward(req,resp);
    }

    /**
     *  파일업로드 처리
     *  1. commons-io, commons-fileupload 의존 추가
     *  2. form[method=post][enctype=multipart/form-data] 설정
     *  3. DiskFileItemFactory / ServletFileUpload 요청처리
     *   - 저장경로
     *   - 파일최대크기
     *
     *
     *
     * @param req
     * @param resp
     * @throws ServletException
     * @throws IOException
     */

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 1. 사용자입력값처리 및 파일업로드
        File repository = new File("C:\\Users\\cksgm\\Dropbox\\workspaces_ming\\web_server_workspace\\hello-mvc\\src\\main\\webapp\\upload\\board");
        int sizeThreshold = 10 * 1024 * 1024; // 10mb (1mb = 1024kb , 1kb = 1024b)

        DiskFileItemFactory factory = new DiskFileItemFactory();
        factory.setRepository(repository);
        factory.setSizeThreshold(sizeThreshold);

        BoardVo board = new BoardVo();

        // ServletFileUpload 실제요청을 핸들링할 객체
        ServletFileUpload servletFileUpload = new ServletFileUpload(factory);
        try {
            // 전송된 값을 하나의 FileItem으로 관리
            List<FileItem> fileItemList = servletFileUpload.parseRequest(req);

            for (FileItem item: fileItemList) {

                String name = item.getFieldName();
                if(item.isFormField()){
                    // 일반 텍스트필드 : Board객체에 설정
                    String value = item.getString("utf-8");
                    System.out.println(name + "=" + value);
                    //  Board 객체에 설정자 로직 구현
                    board.setValue(name, value);
                }
                else{
                    // 파일 : 서버컴퓨터에 저장, 파일정보를 Attachment객체로 만들어서 db에 저장
                    if(item.getSize() > 0){
                        String originalFilename = item.getName(); // 업로드 파일명
                        System.out.println("파일 :" + originalFilename);
                        System.out.println("크기 :" + item.getSize() + "byte");

                        int dotIndex = originalFilename.lastIndexOf(".");
                        String ext = dotIndex > -1 ? originalFilename.substring(dotIndex) : "";

                        UUID uuid = UUID.randomUUID(); // 고유한 문자열 토큰 발급
                        String renamedFilename = uuid + ext;  // 저장된 파일명 (파일덮어쓰기, 인코딩이슈 방지)
                        System.out.println("새 파일명 : " + renamedFilename);

                        // 서버 컴퓨터 파일 저장
                        File upFile = new File(repository, renamedFilename);
                        item.write(upFile); // throw Exception

                        // Attachment 객체 생성
                        Attachment attach = new Attachment();
                        attach.setOriginalFilename(originalFilename);
                        attach.setRenamedFilename(renamedFilename);
                        board.addAttachment(attach);
                    }
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        System.out.println(board); // board객체, attach객체들

        // 2. 업무로직
        int result = boardService.insertBoard(board);
        req.getSession().setAttribute("msg", "게시글을 성공적으로 등록했습니다.😁");

        // 3. redirect 목록페이지
        resp.sendRedirect(req.getContextPath() + "/board/boardList");
    }
}