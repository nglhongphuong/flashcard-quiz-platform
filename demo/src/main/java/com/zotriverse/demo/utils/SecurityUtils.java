package com.zotriverse.demo.utils;


import com.zotriverse.demo.dto.response.QuizanswerResponse;
import com.zotriverse.demo.exception.AppException;
import com.zotriverse.demo.exception.ErrorCode;
import com.zotriverse.demo.pojo.Account;
import com.zotriverse.demo.pojo.Lesson;
import com.zotriverse.demo.repository.*;
import lombok.experimental.UtilityClass;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("securityUtils") // để gọi được từ @PreAuthorize
public class SecurityUtils {
    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private FlashCardRepository flashCardRepo;

    @Autowired
    private FlashcardStudyRepository flashcardStudyRepository;

    @Autowired
    private QuizhistoryRepository quizhistoryRepository;

    @Autowired
    private QuizanswerRepository quizanswerRepository;

    @Autowired
    private CommentRepository commentRepository;

    public boolean isOwnerOrAdmin(int lessonId, String username) {
        Optional<Account> accountOpt = accountRepository.findByUsername(username);
        Optional<Lesson> lessonOpt = lessonRepository.findById(lessonId);
        if (accountOpt.isEmpty() || lessonOpt.isEmpty()) {
            return false;
        }
        Account account = accountOpt.get();
        Lesson lesson = lessonOpt.get();
        boolean isOwner = lesson.getUserId() != null &&
                lesson.getUserId().getAccount() != null &&
                lesson.getUserId().getAccount() == account;
        boolean isAdmin = "ADMIN".equalsIgnoreCase(account.getRole());
        return isOwner || isAdmin;
    }

    public boolean isFlashcardOwnerOrAdmin(int flashcardId, String username) {
        Optional<Account> opt = accountRepository.findByUsername(username);
        if (opt.isEmpty()) return false;
        Account account = opt.get();
        if ("ADMIN".equalsIgnoreCase(account.getRole())) return true;
        return flashCardRepo.findById(flashcardId)
                .map(flashcard -> {
                    Lesson lesson = flashcard.getLessonId();
                    if (lesson == null || lesson.getUserId() == null) return false;
                    return lesson.getUserId().getAccount() == account;
                }).orElse(false);
    }

    //Chỗ này bổ sung, nếu tồn tại userId, quizhistoryId thì mới được cập nhập
    public boolean isQuizhistoryOwner(int quizhistoryId, String username) {
        Optional<Account> accountOpt = accountRepository.findByUsername(username);
        if (accountOpt.isEmpty()) return false;
        Account account = accountOpt.get();
        return quizhistoryRepository.findQuizhistoryById(quizhistoryId)
                .map(qh -> {
                    Integer ownerId = qh.getQuizId().getUserId().getAccountId();
                    return (ownerId != null && ownerId.equals(account.getId()))
                            || "ADMIN".equalsIgnoreCase(account.getRole());
                })
                .orElse(false);
    }
    // Bo sung tac gia quizanswer

    public boolean isQuizanswerOwner(int quizanswerId, String username) {
        Optional<Account> accountOpt = accountRepository.findByUsername(username);
        if (accountOpt.isEmpty()) return false;
        Account account = accountOpt.get();
        return quizanswerRepository.findById(quizanswerId)
                .map(answer -> {
                    if (answer.getHistoryId() == null ||
                            answer.getHistoryId().getQuizId() == null ||
                            answer.getHistoryId().getQuizId().getUserId() == null ||
                            answer.getHistoryId().getQuizId().getUserId().getAccount() == null) {
                        return false;
                    }
                    Integer ownerId = answer.getHistoryId().getQuizId().getUserId().getAccount().getId();
                    return ownerId.equals(account.getId()) || "ADMIN".equalsIgnoreCase(account.getRole());
                })
                .orElse(false);
    }

    public boolean isCommentOwnerOrAdmin(int commentId, String username) {
        Optional<Account> accountOpt = accountRepository.findByUsername(username);
        if (accountOpt.isEmpty()) return false;
        Account account = accountOpt.get();
        return commentRepository.findById(commentId)
                .map(comment -> {
                    Account owner = comment.getUserId().getAccount();
                    return (owner != null && owner.getId().equals(account.getId())) ||
                            "ADMIN".equalsIgnoreCase(account.getRole());
                }).orElse(false);
    }

    public boolean isCommentDeletableBy(String username, int commentId) {
        Optional<Account> accountOpt = accountRepository.findByUsername(username);
        if (accountOpt.isEmpty()) return false;
        Account account = accountOpt.get();
        return commentRepository.findById(commentId)
                .map(comment -> {
                    Account commentOwner = comment.getUserId().getAccount();
                    Lesson lesson = comment.getLessonId();
                    //tg comment
                    boolean isCommentOwner = commentOwner != null && commentOwner.getId().equals(account.getId());
                    //tg bài học
                    boolean isLessonOwner = lesson != null
                            && lesson.getUserId() != null
                            && lesson.getUserId().getAccount() != null
                            && lesson.getUserId().getAccount().getId().equals(account.getId());
                    boolean isAdmin = "ADMIN".equalsIgnoreCase(account.getRole());
                    return isCommentOwner || isLessonOwner || isAdmin;
                }).orElse(false);
    }


}
