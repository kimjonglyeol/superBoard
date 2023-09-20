package com.Super.Board.post.controller;

import com.Super.Board.post.dto.PostDTO;
import com.Super.Board.post.service.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts")//공통 주소
public class PostController {

    private final PostService postService;
    
    
    //검색
    @GetMapping("/search")
    public ResponseEntity<List<PostDTO>> getPostsByPostWriter(@RequestParam(name = "author") String author) {
        System.out.println("author = " + author);

        List<PostDTO> postsByPostWriter = postService.getPostsByPostWriter(author);

        // 결과가 비어 있는 경우 404 Not Found 응답을 반환
        if (postsByPostWriter.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        // 결과가 있는 경우 200 OK 응답을 반환
        return new ResponseEntity<>(postsByPostWriter, HttpStatus.OK);
    }
    
    
    //수정

    // 게시글 수정
    //post_id? or postId
    @PutMapping("/{post_id}")
    public ResponseEntity<PostDTO> updatePost(@PathVariable Long postId, @RequestBody PostDTO updatedPostDTO) {
        // 게시글 ID를 사용하여 해당 게시글을 업데이트
        System.out.println("postId = " + postId);
     
        
        String author = "user@email.com";

        // 기존 작성 글의 이메일
        PostDTO exitPostByEmail = postService.getPostById(postId);

        // 로그인한 유저와 기존 작성 글의 이메일 비교
        if (author.equals(exitPostByEmail.getAuthor())) {
            PostDTO updatedPost = postService.updatePost(postId, updatedPostDTO);

            if (updatedPost != null) {
                return ResponseEntity.ok(updatedPost); // 수정된 게시글을 반환
            } else {
                return ResponseEntity.notFound().build(); // 게시글을 찾을 수 없는 경우
            }
        } else {
            // 조회한 게시글이 없는 경우
            return ResponseEntity.notFound().build();
        }

    }

}
