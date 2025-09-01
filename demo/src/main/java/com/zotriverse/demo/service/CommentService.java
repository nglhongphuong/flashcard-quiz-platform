package com.zotriverse.demo.service;

import com.zotriverse.demo.dto.request.CommentRequest;
import com.zotriverse.demo.dto.response.CommentResponse;
import com.zotriverse.demo.exception.AppException;
import com.zotriverse.demo.exception.ErrorCode;
import com.zotriverse.demo.mapper.CommentMapper;
import com.zotriverse.demo.pojo.Account;
import com.zotriverse.demo.pojo.Comment;
import com.zotriverse.demo.pojo.Lesson;
import com.zotriverse.demo.repository.AccountRepository;
import com.zotriverse.demo.repository.CommentRepository;
import com.zotriverse.demo.repository.LessonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import java.util.Map;
import java.util.stream.Collectors;

@Service
@Slf4j //log cua lomboo inject 1 logger
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Value("${PAGE_SIZE}")
    private int PAGE_SIZE;

    public CommentResponse createComment(int lessonId, int userId, CommentRequest commentRequest){
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new AppException(ErrorCode.CANNOT_FOUND));
        Account account = accountRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED));
        if(commentRequest.getContent().isEmpty() || commentRequest.getContent().isBlank()){
            throw  new AppException(ErrorCode.INVALID_INPUT);
        }
        if(lesson.getIsCommentLocked()){
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }

        Comment comment = Comment.builder()
                .content(commentRequest.getContent())
                .userId(account.getUser())
                .lessonId(lesson)
                .build();

        this.commentRepository.save(comment);

        return commentMapper.toCommentResponse(comment);


    }


    public CommentResponse updateComment(int commentId, CommentRequest commentRequest) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new AppException(ErrorCode.CANNOT_FOUND));

        if (commentRequest.getContent() == null || commentRequest.getContent().isBlank()) {
            throw new AppException(ErrorCode.INVALID_INPUT);
        }
        comment.setContent(commentRequest.getContent());
        commentRepository.save(comment);
        return commentMapper.toCommentResponse(comment);
    }

    public void deleteComment(int commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new AppException(ErrorCode.CANNOT_FOUND));
        commentRepository.delete(comment);
    }
    public List<CommentResponse> getComments(int lessonId, Map<String, String> params) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new AppException(ErrorCode.CANNOT_FOUND));

        int page = params.containsKey("page") ? Integer.parseInt(params.get("page")) : 1;
        if (page <= 0) {
            return new ArrayList<>(); // 👈 trả về danh sách rỗng nếu page <= 0
        }

        String sort = params.getOrDefault("sort", "desc");

        Sort sorting = sort.equalsIgnoreCase("asc") ?
                Sort.by("createdAt").ascending() :
                Sort.by("createdAt").descending();

        Pageable pageable = PageRequest.of(page - 1, PAGE_SIZE, sorting); // 👈 subtract 1

        Page<Comment> commentPage = commentRepository.findByLessonId(lesson, pageable);

        return commentPage.getContent()
                .stream()
                .map(commentMapper::toCommentResponse)
                .collect(Collectors.toList());
    }




}
