package com.zotriverse.demo.service;

import com.zotriverse.demo.dto.request.QuizHistoryBulkUpdateRequest;
import com.zotriverse.demo.dto.request.QuizHistoryUpdateRequest;
import com.zotriverse.demo.dto.request.QuizstudyRequest;
import com.zotriverse.demo.dto.request.TeststudyRequest;
import com.zotriverse.demo.dto.response.*;
import com.zotriverse.demo.enums.Result;
import com.zotriverse.demo.enums.StudyMode;
import com.zotriverse.demo.exception.AppException;
import com.zotriverse.demo.exception.ErrorCode;
import com.zotriverse.demo.mapper.QuizhistoryMapper;
import com.zotriverse.demo.mapper.QuizstudyMapper;
import com.zotriverse.demo.pojo.*;
import com.zotriverse.demo.repository.*;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
@Slf4j
public class QuizstudyService {
    @Autowired
    private QuizstudyRepository quizstudyRepository;

    @Autowired
    private FlashCardRepository flashcardRepository;

    @Autowired
    private FlashcardStudyRepository flashcardStudyRepository;

    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuizhistoryRepository quizhistoryRepository;

    @Autowired
    private QuizstudyMapper quizstudyMapper;

    @Autowired
    private QuizhistoryMapper quizhistoryMapper;

    @Autowired
    private QuizanswerRepository quizanswerRepository;

    @Value("${MIN_FLASHCARD_RANDOM}")
    private int MIN_FLASHCARD_RANDOM;

    @PersistenceContext
    private EntityManager entityManager;

    @Autowired
    private TeststudyRepository teststudyRepository;

    @Value("${PAGE_SIZE}")
    private int PAGE_SIZE;

    private List<Integer> getFlashcardsByStudyMode(
            StudyMode studyMode,
            int lessonId,
            QuizstudyRequest request,
            int userId
    ) {
        // B1: Lấy tất cả flashcard thuộc lesson và user
        List<Flashcard> flashcards = flashcardRepository.findByLessonIdAndUserId(lessonId, userId);
        List<Integer> flashcardIds = flashcards.stream()
                .map(Flashcard::getId)
                .collect(Collectors.toList());

        switch (studyMode) {
            case CUSTOM:
                List<Integer> customIds = request.getFlashcardIds();
                return flashcards.stream()
                        .filter(f -> customIds.contains(f.getId()))
                        .map(Flashcard::getId)
                        .collect(Collectors.toList());

            case RANDOM:
                int total = flashcards.size();
                Integer required = request.getNumberOfFlashcards();
                int min = MIN_FLASHCARD_RANDOM; // có thể dùng @Value cũng được
                System.out.println("require: " + required);

                if (required == null || required < min || required > total) {
                    throw new AppException(ErrorCode.INVALID_NUM_QUIZ);
                }

                Collections.shuffle(flashcards);
                return flashcards.stream()
                        .limit(required)
                        .map(Flashcard::getId)
                        .collect(Collectors.toList());

            case NOT_REMEMBERED:
            case NOT_LEARNED:
            case REMEMBERED:
                String status = studyMode.name(); // ví dụ: "NOT_REMEMBERED"
                List<FlashcardStudy> studies = flashcardStudyRepository
                        .findByUserIdAndStatusAndFlashcardIds(userId, status, flashcardIds);

                return studies.stream()
                        .map(fcs -> fcs.getFlashcardStudyPK().getFlashcardId())
                        .collect(Collectors.toList());

            default:
                throw new IllegalArgumentException("Chế độ học không hợp lệ: " + studyMode);
        }
    }
    private void assignAnswerTypesToHistories(
            List<Quizhistory> histories,
            List<String> answerTypes
    ) {
        int n = histories.size();
        int m = answerTypes.size();

        if (m == 1) {
            // Gán cùng một answerType cho tất cả
            String type = answerTypes.get(0);
            histories.forEach(q -> q.setAnswerType(type));
        } else {
            // Rải đều các answerType
            for (int i = 0; i < n; i++) {
                String type = answerTypes.get(i % m);
                histories.get(i).setAnswerType(type);
            }
            Collections.shuffle(histories); // Trộn để tránh nhóm cùng loại
        }
    }

    private void generateQuizAnswers(List<Quizhistory> histories, int lessonId) {
        // Lấy tất cả flashcard của lesson 1 lần
        List<Flashcard> allFlashcards = flashcardRepository.findByLessonId(lessonId);
        for (Quizhistory history : histories) {
            Flashcard correctFlashcard = history.getFlashcardId();
            String correctDef = correctFlashcard.getDefinition();
            switch (history.getAnswerType()) {
                case "MULTIPLE_CHOICE" -> {
                    // Lấy các flashcard khác  correctFlashcard
                    List<String> wrongDefs = allFlashcards.stream()
                            .filter(f -> f.getId() != correctFlashcard.getId())
                            .map(Flashcard::getDefinition)
                            .distinct()
                            .collect(Collectors.toList());
                    Collections.shuffle(wrongDefs);
                    List<String> choices = new ArrayList<>();
                    choices.add(correctDef);
                    choices.addAll(wrongDefs.subList(0, 3)); //3 sai
                    Collections.shuffle(choices);

                    for (int i = 0; i < 4; i++) {
                        Quizanswer qa = Quizanswer.builder()
                                .definition(choices.get(i))
                                .position(i)
                                .historyId(history)
                                .build();
                        quizanswerRepository.save(qa);
                    }
                }
                case "TRUE_FALSE" -> {
                    boolean isCorrect = ThreadLocalRandom.current().nextBoolean();
                    String defToUse = isCorrect
                            ? correctDef
                            : allFlashcards.stream()
                            .filter(f -> f.getId() != correctFlashcard.getId())
                            .map(Flashcard::getDefinition)
                            .filter(def -> !def.equals(correctDef))
                            .findAny()
                            .orElse(correctDef); // fallback if not found
                    Quizanswer qa = Quizanswer.builder()
                            .definition(defToUse)
                            .position(0) // true/false chỉ 1 đáp án
                            .historyId(history)
                            .build();
                    quizanswerRepository.save(qa);
                }
                // TEXT_INPUT hoặc FILL_IN_THE_BLANK thì không có Quizanswer (người dùng tự gõ)
            }
        }
    }
    @Transactional
    public QuizstudyResponse createQuizStudy(int userId, int lessonId, QuizstudyRequest request) {
        // 1. Validate answerTypes
        List<String> answerTypes = request.getAnswerTypes();
        if (answerTypes == null || answerTypes.isEmpty()) {
            throw new AppException(ErrorCode.INVALID_ANSWER_TYPE);
        }
        String modeString = request.getStudyMode();
        StudyMode studyMode;

        try {
            studyMode = StudyMode.valueOf(modeString.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new AppException(ErrorCode. INVALID_STUDY_MODE);
        }

        // 3. Lấy danh sách flashcard theo chế độ học
        List<Integer> selectedFlashcardIds = getFlashcardsByStudyMode(studyMode ,lessonId, request, userId);

        if (selectedFlashcardIds.isEmpty()) {
            throw new AppException(ErrorCode.CANNOT_FOUND_FLASHCARD_STUDY);
        }
        //Tạo QuizStudy từ lessonId và userId
        Quizstudy quizstudy = Quizstudy.builder()
                .lessonId(lessonRepository.findById(lessonId)
                        .orElseThrow(() -> new AppException(ErrorCode.CANNOT_FOUND)))
                .userId(userRepository.findById(userId)
                        .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_EXISTED)))
                .build();

        quizstudyRepository.save(quizstudy);
       // 4. Tạo danh sách Quizhistory từ danh sách flashcard đã chọn
        List<Quizhistory> quizhistories = selectedFlashcardIds.stream()
                .map(flashcardId -> Quizhistory.builder()
                        .quizId(quizstudy)
                        .flashcardId(flashcardRepository.findById(flashcardId)
                                .orElseThrow(() -> new AppException(ErrorCode.CANNOT_FOUND))) // nếu flashcardId bị sai
                        .questionType(request.getQuestionType()) // từ người dùng chọn ở request
                        .result(Result.UNRESULT.getDisplayName())
                        .build())
                .collect(Collectors.toList());

        // tuy nhiên , khi Quizhistory có danh sách flashcard đã chọn
        // Tiến hành gán answer type tuong ung
        //Nếu đầu vào answertypes từ request người dùng truyền là 1 thì đơn giản
        //Nhưng nếu truyền 2, 3 vd như có cả text, true/false, multiple choice thì phải random rải đều answer type lun TT^TT
        // sau khi gán quizhistory gán flashcard và answertype tương ứng mới tính tiếp vụ lưu TT^TT
        //tao 1 ham rieng gan cho de quan ly xong ms luu repository
        assignAnswerTypesToHistories(quizhistories, answerTypes);
        quizhistoryRepository.saveAll(quizhistories);
        generateQuizAnswers(quizhistories, lessonId);

        quizstudy.setQuizhistorySet(new HashSet<>(quizhistories));

        quizstudyRepository.save(quizstudy);

        return quizstudyMapper.toQuizstudyReponse(quizstudy);

//        // 4. Phân phối answerType đều cho flashcardId
//        Map<Integer, String> flashcardToAnswerType = assignAnswerTypesEvenly(selectedFlashcardIds, answerTypes);
//
//        // 5. Tạo QuizStudy entity → lưu DB
//        Quizstudy quizstudy = saveQuizStudy(userId, lessonId);
//
//        // 6. Sinh danh sách QuizHistory từ flashcard + answerType
//        List<Quizhistory> histories = createQuizHistories(flashcardToAnswerType, request.getQuestionType(), quizstudy);
//
//        // 7. Lưu vào DB
//        quizhistoryRepository.saveAll(histories);
    }

//    public QuizstudyDetailResponse getQuizstudyDetail(int quizStudyId, int userId, int lessonId) {
//        // 1. Truy xuất Quizstudy theo id, userId và lessonId
//        Quizstudy quizstudy = quizstudyRepository.findByIdUserLesson(quizStudyId, userId, lessonId)
//                .orElseThrow(() -> new AppException(ErrorCode.CANNOT_FOUND));
//
//        // 2. Lấy danh sách quizhistory liên quan
//        Set<QuizhistoryResponse> quizhistoryResponses = quizstudy.getQuizhistorySet().stream()
//                .map(history -> {
//                    // Lấy các Quizanswer
//                    Set<QuizanswerResponse> answerResponses = history.getQuizanswerSet() != null
//                            ? history.getQuizanswerSet().stream()
//                            .map(answer -> QuizanswerResponse.builder()
//                                    .id(answer.getId())
//                                    .definition(answer.getDefinition())
//                                    .position(answer.getPosition())
//                                    .createdAt(answer.getCreatedAt())
//                                    .updateAt(answer.getUpdateAt())
//                                    .build())
//                            .collect(Collectors.toSet())
//                            : null;
//
//                    // Trả về DTO quizhistory
//                    return QuizhistoryResponse.builder()
//                            .id(history.getId())
//                            .explanation(history.getExplanation())
//                            .questionType(history.getQuestionType())
//                            .answerType(history.getAnswerType())
//                            .result(history.getResult())
//                            .userAnswer(history.getUserAnswer())
//                            .createdAt(history.getCreatedAt())
//                            .updateAt(history.getUpdateAt())
//                            .quizanswerSet(answerResponses)
//                            .flashcardId(history.getFlashcardId())
//                            .build();
//                })
//                .collect(Collectors.toSet());
//
//        // 3. Trả về DTO QuizstudyDetailResponse
//        return QuizstudyDetailResponse.builder()
//                .id(quizstudy.getId())
//                .createdAt(quizstudy.getCreatedAt())
//                .updateAt(quizstudy.getUpdateAt())
//                .lessonId(quizstudy.getLessonId())
//                .userId(quizstudy.getUserId())
//                .quizhistorySet(quizhistoryResponses)
//                .build();
//    }
//

    public QuizstudyDetailResponse getQuizstudyDetail(int quizStudyId, int userId, int lessonId) {
        Quizstudy quizstudy = quizstudyRepository.findByIdUserLesson(quizStudyId, userId, lessonId)
                .orElseThrow(() -> new AppException(ErrorCode.CANNOT_FOUND));

        Set<Quizhistory> histories = quizstudy.getQuizhistorySet();

        // Tính số lượng từng loại kết quả
        Map<String, Integer> resultCounts = new HashMap<>();
        resultCounts.put(Result.CORRECT.getDisplayName(), 0);
        resultCounts.put(Result.INCORRECT.getDisplayName(), 0);
        resultCounts.put(Result.UNRESULT.getDisplayName(), 0);

        for (Quizhistory h : histories) {
            String result = h.getResult();
            if (resultCounts.containsKey(result)) {
                resultCounts.computeIfPresent(result, (k, v) -> v + 1);
            }
        }

        Set<QuizhistoryResponse> quizhistoryResponses = histories.stream()
                .map(history -> {
                    Set<QuizanswerResponse> answerResponses = history.getQuizanswerSet() != null
                            ? history.getQuizanswerSet().stream()
                            .map(answer -> QuizanswerResponse.builder()
                                    .id(answer.getId())
                                    .definition(answer.getDefinition())
                                    .position(answer.getPosition())
                                    .createdAt(answer.getCreatedAt())
                                    .updateAt(answer.getUpdateAt())
                                    .build())
                            .collect(Collectors.toSet())
                            : null;

                    return QuizhistoryResponse.builder()
                            .id(history.getId())
                            .explanation(history.getExplanation())
                            .questionType(history.getQuestionType())
                            .answerType(history.getAnswerType())
                            .result(history.getResult())
                            .userAnswer(history.getUserAnswer())
                            .createdAt(history.getCreatedAt())
                            .updateAt(history.getUpdateAt())
                            .quizanswerSet(answerResponses)
                            .flashcardId(history.getFlashcardId())
                            .build();
                })
                .collect(Collectors.toSet());

        return QuizstudyDetailResponse.builder()
                .id(quizstudy.getId())
                .createdAt(quizstudy.getCreatedAt())
                .updateAt(quizstudy.getUpdateAt())
                .lessonId(quizstudy.getLessonId())
                .userId(quizstudy.getUserId())
                .quizhistorySet(quizhistoryResponses)
                .results(resultCounts)
                .teststudy(quizstudy.getTeststudy())
                .build();
    }


    public List<QuizstudyInfoResponse> getQuizStudiesByLessonId(int userId, int lessonId) {
        List<Quizstudy> quizStudies = quizstudyRepository.findAllByLessonIdAndUserId(lessonId, userId);

        return quizStudies.stream().map(quizstudy -> {
            Map<String, Integer> resultCounts = new HashMap<>();
            resultCounts.put(Result.CORRECT.getDisplayName(), 0);
            resultCounts.put(Result.INCORRECT.getDisplayName(), 0);
            resultCounts.put(Result.UNRESULT.getDisplayName(), 0);

            for (Quizhistory history : quizstudy.getQuizhistorySet()) {
                String result = history.getResult(); // "CORRECT", "UNRESULT", ...
                resultCounts.computeIfPresent(result, (k, v) -> v + 1);
            }

            return QuizstudyInfoResponse.builder()
                    .id(quizstudy.getId())
                    .createdAt(quizstudy.getCreatedAt())
                    .updateAt(quizstudy.getUpdateAt())
                    .lessonId(quizstudy.getLessonId())
                    .userId(quizstudy.getUserId().getAccountId())
                    .results(resultCounts)
                    .build();
        }).collect(Collectors.toList());
    }

    public List<QuizstudyInfoResponse> getQuizStudiesByLessonId(Map<String, String> params) {
        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Quizstudy> query = builder.createQuery(Quizstudy.class);
        Root<Quizstudy> root = query.from(Quizstudy.class);
        query.select(root);
        List<Predicate> predicates = new ArrayList<>();
        // Lọc theo lessonId
        if (params.containsKey("lessonId")) {
            try {
                int lessonId = Integer.parseInt(params.get("lessonId"));
                predicates.add(builder.equal(root.get("lessonId").get("id"), lessonId));
            } catch (NumberFormatException e) {
                throw new AppException(ErrorCode.INVALID_INPUT);
            }
        }
        // Lọc theo userId
        if (params.containsKey("userId")) {
            try {
                int userId = Integer.parseInt(params.get("userId"));
                predicates.add(builder.equal(root.get("userId").get("accountId"), userId));
            } catch (NumberFormatException e) {
                throw new AppException(ErrorCode.USER_NOT_EXISTED);
            }
        }
        // Lọc theo khoảng thời gian createdAt
        String startDate = params.get("startDate");
        String endDate = params.get("endDate");
        if (startDate != null && !startDate.isBlank()) {
            LocalDateTime start = LocalDate.parse(startDate).atStartOfDay();
            predicates.add(builder.greaterThanOrEqualTo(root.get("createdAt"), start));
        }
        if (endDate != null && !endDate.isBlank()) {
            LocalDateTime end = LocalDate.parse(endDate).atTime(LocalTime.MAX);
            predicates.add(builder.lessThanOrEqualTo(root.get("createdAt"), end));
        }

        if (params.containsKey("teststudy")) {
            Join<Quizstudy, Teststudy> teststudyJoin = root.join("teststudy", JoinType.LEFT);
            String teststudyFlag = params.get("teststudy");
            if ("true".equalsIgnoreCase(teststudyFlag)) {
                predicates.add(builder.isNotNull(teststudyJoin));
            } else if ("false".equalsIgnoreCase(teststudyFlag)) {
                predicates.add(builder.isNull(teststudyJoin));
            }
        }


        // Sắp xếp
        String order = params.getOrDefault("order", "desc");
        if ("asc".equalsIgnoreCase(order)) {
            query.orderBy(builder.asc(root.get("createdAt")));
        } else {
            query.orderBy(builder.desc(root.get("createdAt")));
        }
        query.where(predicates.toArray(new Predicate[0]));
        TypedQuery<Quizstudy> typedQuery = entityManager.createQuery(query);
        // Phân trang
        int page = Integer.parseInt(params.getOrDefault("page", "1"));
        int size =  PAGE_SIZE; // PAGE_SIZE tùy biến
        typedQuery.setFirstResult((page - 1) * size);
        typedQuery.setMaxResults(size);
        List<Quizstudy> results = typedQuery.getResultList();
        return results.stream().map(quizstudy -> {
            Map<String, Integer> resultCounts = new HashMap<>();
            resultCounts.put(Result.CORRECT.getDisplayName(), 0);
            resultCounts.put(Result.INCORRECT.getDisplayName(), 0);
            resultCounts.put(Result.UNRESULT.getDisplayName(), 0);

            for (Quizhistory history : quizstudy.getQuizhistorySet()) {
                String result = history.getResult();
                resultCounts.computeIfPresent(result, (k, v) -> v + 1);
            }
            return QuizstudyInfoResponse.builder()
                    .id(quizstudy.getId())
                    .createdAt(quizstudy.getCreatedAt())
                    .updateAt(quizstudy.getUpdateAt())
                    .lessonId(quizstudy.getLessonId())
                    .userId(quizstudy.getUserId().getAccountId())
                    .results(resultCounts)
                    .teststudy(quizstudy.getTeststudy())
                    .build();
        }).collect(Collectors.toList());
    }

    @Transactional
    public void deleteByUserAndLesson(int quizstudyId, int userId, int lessonId) {
        Quizstudy quizstudy = quizstudyRepository.findByIdUserLesson(quizstudyId, userId, lessonId)
                .orElseThrow(() -> new AppException(ErrorCode.CANNOT_FOUND));
        quizstudyRepository.delete(quizstudy);
    }

    private String evaluateResult(Quizhistory history, String userAnswer) {
        Flashcard flashcard = history.getFlashcardId();
        String correctDefinition = flashcard.getDefinition().trim();
        String answerType = history.getAnswerType();

        switch (answerType) {
            case "TEXT_INPUT" -> {
                return userAnswer.trim().equalsIgnoreCase(correctDefinition) ?
                        Result.CORRECT.getDisplayName() : Result.INCORRECT.getDisplayName();
            }

            case "MULTIPLE_CHOICE" -> {
                try {
                    int quizAnswerId = Integer.parseInt(userAnswer);
                    Quizanswer selectedAnswer = quizanswerRepository.findById(quizAnswerId)
                            .orElseThrow(() -> new AppException(ErrorCode.CANNOT_FOUND));
                    return selectedAnswer.getDefinition().trim().equalsIgnoreCase(correctDefinition) ?
                            Result.CORRECT.getDisplayName() : Result.INCORRECT.getDisplayName();
                } catch (NumberFormatException e) {
                    throw new AppException(ErrorCode.INVALID_INPUT);
                }
            }

            case "TRUE_FALSE" -> {
                boolean isTrueSelected = Boolean.parseBoolean(userAnswer);
                Quizanswer answerInDb = history.getQuizanswerSet().stream().findFirst()
                        .orElseThrow(() -> new AppException(ErrorCode.CANNOT_FOUND));
                boolean actualIsTrue = answerInDb.getDefinition().trim().equalsIgnoreCase(correctDefinition);
                return (isTrueSelected == actualIsTrue) ?
                        Result.CORRECT.getDisplayName() : Result.INCORRECT.getDisplayName();
            }

            default -> throw new AppException(ErrorCode.INVALID_INPUT);
        }
    }
    @Transactional
    public QuizhistoryResponse updateQuizHistory(int historyId, QuizHistoryUpdateRequest request) {
        Quizhistory history = quizhistoryRepository.findById(historyId)
                .orElseThrow(() -> new AppException(ErrorCode.CANNOT_FOUND));

        String result = evaluateResult(history, request.getUserAnswer());

        history.setUserAnswer(request.getUserAnswer());
        history.setResult(result);
        history.setUpdateAt(new Date());

        quizhistoryRepository.save(history);
        return quizhistoryMapper.toQuizhistoryResponse(history);
    }

    ////Truowngf hop lam bai kiem tra truyen danh sach user tra loi cau hoi tuong ung voi tung quiz_study so voi flashcard Id

    @Transactional
    public void updateQuizHistoryBulk(List<QuizHistoryBulkUpdateRequest> requests) {
        for (QuizHistoryBulkUpdateRequest req : requests) {
            Quizhistory history = quizhistoryRepository.findById(req.getQuizhistoryId())
                    .orElseThrow(() -> new AppException(ErrorCode.CANNOT_FOUND));

            String result = evaluateResult(history, req.getUserAnswer());

            history.setUserAnswer(req.getUserAnswer());
            history.setResult(result);
            history.setUpdateAt(new Date());

            quizhistoryRepository.save(history);
        }
    }




    //========================================= TEST STUDY =================================================================================================
    @Transactional
    public QuizstudyResponse createTeststudy(int userId, int lessonId, TeststudyRequest request) {
        // 1. Gọi lại hàm đã có để tạo Quizstudy
        QuizstudyResponse quizstudyResponse = createQuizStudy(userId, lessonId, request.getQuizstudy());

        quizstudyRepository.flush();
        // 2. Tìm lại quizstudy vừa tạo để thiết lập quan hệ 2 chiều
        Quizstudy quizstudy = quizstudyRepository.findById(quizstudyResponse.getId())
                .orElseThrow(() -> new AppException(ErrorCode.CANNOT_FOUND));

        System.out.println("HAHAHHAHAHHAHA QUIZ STUDY : " + quizstudy.getId());

        // 3. Tạo teststudy & liên kết với quizstudy
        Teststudy teststudy = Teststudy.builder()
//                //.quizId(quizstudy.getId())
                .quizstudy(quizstudy)
                .min(request.getMin())
                .build();

        teststudyRepository.save(teststudy);

//        // 4. Gán teststudy vào quizstudy (2 chiều)
          quizstudy.setTeststudy(teststudy);
          quizstudyRepository.save(quizstudy); // cập nhật

        // 5. Trả về response (nếu có mapper)
        return quizstudyMapper.toQuizstudyReponse(quizstudy);
    }


}
