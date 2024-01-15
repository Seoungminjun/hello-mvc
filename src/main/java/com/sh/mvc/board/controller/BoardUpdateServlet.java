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

@WebServlet("/board/boardUpdate")
public class BoardUpdateServlet extends HttpServlet {
    private BoardService boardService = new BoardService();

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 1. 사용자 입력값 처리
        long id = Long.parseLong(req.getParameter("id"));
        System.out.println(id);
        // 2. 업무로직
        BoardVo board = boardService.findById(id);
        System.out.println(board);
        req.setAttribute("board", board);
        // 3. forword 처리
        req.getRequestDispatcher("/WEB-INF/views/board/boardUpdate.jsp").forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        // 1. 사용자 입력값 처리
        File repository = new File("C:\\Users\\cksgm\\Dropbox\\workspaces_ming\\web_server_workspace\\hello-mvc\\src\\main\\webapp\\upload\\board");
        int sizeThreshold = 10 * 1024 * 1024;

        // DiskFileItemFactory - ServletFileUpload
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
                    // 일반 텍스트 필드 : Board객체에 설정
                    String value = item.getString("utf-8");
                    System.out.println(name + "=" + value);
                    // Board 객체에 설정자 로직 구현
                    board.setValue(name, value);

                }
                else {
                    // db에 저장
                    if(item.getSize() > 0){
                        String originalFilename = item.getName();

                        int dotIndex = originalFilename.lastIndexOf(".");
                        String ext = dotIndex > -1 ? originalFilename.substring(dotIndex) : "";

                        UUID uuid = UUID.randomUUID();
                        String renamedFilename = uuid + ext;

                        // 서버 컴퓨터 파일 저장
                        File upFile = new File(repository, renamedFilename);
                        item.write(upFile);

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
        System.out.println(board);


        // 2. 업무로직
        int result = boardService.updateBoard(board);
        req.getSession().setAttribute("msg", "게시글이 성공적으로 수정됐습니다.🤢");

        // 3. redirect
        resp.sendRedirect(req.getContextPath() + "/board/boardDetail?id=" + board.getId());
    }
}





















