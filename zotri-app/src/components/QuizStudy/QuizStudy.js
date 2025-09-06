// import "./QuizStudy.css";

// import React, { useEffect, useState, useContext } from "react";
// import { useParams, useNavigate } from "react-router-dom";
// import { authApis, endpoints } from "../../configs/Apis";
// import { MyUserContext } from "../../configs/MyContexts";
// import MySpinner from "../layouts/MySpinner";


// const QuizStudy= () => {
//     const { lessonId } = useParams();
//     const user = useContext(MyUserContext);
//     const [loading, setLoading] = useState(false);
//     const [stats, setStats] = useState(null);
//     const nav = useNavigate();

//     useEffect(() => {
//         const loadQuizstudy = async () => {
//             setLoading(true);
//             try {
//                 // //Tao flashcard-study truoc tra ve 100 ok
//                 // await authApis.post(endpoints.flashcard_study(lessonId));
//                 // const res = await authApis.post(endpoints.flashcard_study_status(lessonId));

//                 // const flashcardset = res.data;
//             } catch (err) {
//                 console.error("Error fetching flashcard study", err);
//             } finally {
//                 setLoading(false);
//             }
//         };
//         loadQuizstudy();
//     },);


// //1) QuizStudy FORMATE NỀN ĐEN CHỮ TRẮNG, MÀU PASTEL (XANH , VÀNG, ĐỎ, XANH BIỂN, TÍM, HỒNG PASTEL DỄ NHÌN HIỆN ĐẠI)
// //GỒM 3 PHẦN
// // PHẦN 1: NÚT TẠO QUIZ STUDY MỚI , khi nhấn nút này sẽ hiện form tạo quiz study theo yêu cầu người dùng gọi component <QuizStudyForm/>
// // Khi nhấn submit truyền dưới dạng json khi gửi lên const res = await auth().post(endpoints.quiz_study(lessonId)) truyền dưới dạng file json 
// // public class QuizstudyRequest {
// //     private String questionType; // "AUDIO", "TEXT_AUDIO"
// //     private List<String> answerTypes;    // "MULTIPLE_CHOICE", "TRUE_FALSE", "TEXT_INPUT", "FILL_IN_THE_BLANK"
// //     private String studyMode; // ENUM: NOT_REMEMBERED, NOT_LEARNED, REMEMBERED, RANDOM, CUSTOM
// //     // Áp dụng nếu chế độ học là RANDOM Thì hiện form
// //     private Integer numberOfFlashcards;
// //     // Áp dụng nếu chế độ học là CUSTOM
// //     private List<Integer> flashcardIds;
// // }
// //vd 
// //Xảy ra khi học flashcard =))))) còn ko là nó ko biết đâu mà học chấn động note
// // {
// //   "questionType": "TEXT_AUDIO",
// //   "answerTypes": ["MULTIPLE_CHOICE", "TRUE_FALSE", "TEXT_INPUT"],
// //   "studyMode": "RANDOM",
// //   "numberOfFlashcards": 3
// // }
// //Coi lại chỉ định -> chọn số thứ tự.
// // {
// //   "questionType": "AUDIO",
// //   "answerTypes": ["TRUE_FALSE", "FILL_IN_THE_BLANK"],
// //   "studyMode": "CUSTOM",
// //   "flashcardIds": [12, 45, 78]
// // }

// // {
// //   "questionType": "TEXT_AUDIO",
// //   "answerTypes": ["MULTIPLE_CHOICE"],
// //   "studyMode": "REMEMBERED"
// // }

// //PHẦN 2: THANH TRUY VẤN LỌC DANH SÁCH QUIZSTUDY THUỘC LESSONID CỦA NGƯỜI DÙNG ID 
// // TA SẼ GỌI API ĐỂ Trả về danh sách các quiz_study tạo của người dùng trên 1 bài học  await authApis().get(endpoints.quiz_study(lessonId)); 
// //   {ket quả của await authApis().get(endpoints.quiz_study(lessonId)); 
// //     "code": 1000,
// //     "result": [
// //         {
// //             "id": 28,
// //             "createdAt": "2025-08-08T06:55:05.000+00:00",
// //             "updateAt": "2025-08-08T06:55:05.000+00:00",
// //             "lessonId": {
// //                 "id": 20,
// //                 "title": "Hôm nay thứ mấy",
// //                 "description": "Bài học ddddddddddddddddddddddddddddddddddddddddd òo SS",
// //                 "image": "http://res.cloudinary.com/dnc5sycvb/image/upload/v1754486199/avatars/bkgwgc7sl7habcoqqt4x.jpg",
// //                 "visibility": "PUBLIC",
// //                 "isCommentLocked": false,
// //                 "createdAt": "2025-08-06T13:16:35.000+00:00",
// //                 "updateAt": "2025-08-06T13:16:35.000+00:00"
// //             },
// //             "userId": 30,
// //             "results": {
// //                 "CORRECT": 0,
// //                 "INCORRECT": 0,
// //                 "UNRESULT": 2
// //             },
// //             "teststudy": null
// //         },
// //         {
// //             "id": 27,
// //             "createdAt": "2025-08-08T06:53:36.000+00:00",
// //             "updateAt": "2025-08-08T06:53:36.000+00:00",
// //             "lessonId": {
// //                 "id": 20,
// //                 "title": "Hôm nay thứ mấy",
// //                 "description": "Bài học ddddddddddddddddddddddddddddddddddddddddd òo SS",
// //                 "image": "http://res.cloudinary.com/dnc5sycvb/image/upload/v1754486199/avatars/bkgwgc7sl7habcoqqt4x.jpg",
// //                 "visibility": "PUBLIC",
// //                 "isCommentLocked": false,
// //                 "createdAt": "2025-08-06T13:16:35.000+00:00",
// //                 "updateAt": "2025-08-06T13:16:35.000+00:00"
// //             },
// //             "userId": 30,
// //             "results": {
// //                 "CORRECT": 0,
// //                 "INCORRECT": 0,
// //                 "UNRESULT": 3
// //             },
// //             "teststudy": null
// //         }
// //     ]
// // }
// //  có thể truy vấn, filter lọc  theo  Tên param	Kiểu giá trị	Chức năng của  
// // lessonId	int (string parse)	Lọc theo ID của bài học (lesson.id)
// // userId	int (string parse)	Lọc theo ID của người dùng (user.accountId)
// // startDate	String (ISO date)	Lọc từ ngày bắt đầu (createdAt >= startDate)
// // endDate	String (ISO date)	Lọc đến ngày kết thúc (createdAt <= endDate)
// // teststudy	String ("true" hoặc "false")	Lọc theo có hay không có teststudy (null hay not null)
// // order	String ("asc" hoặc "desc")	Sắp xếp theo createdAt tăng dần hoặc giảm dần
// // page	int (string parse)	Phân trang: xác định trang hiện tại
// // Tương ứng với mỗi "id" - đây là id của quiz-study , người dùng có thể truy cập trên đó thực hiện làm bài kiểm tra
// // khi nhấn id bất kỳ sẽ chuyển qua trang QuizStudyDetail/QuizStudyDo.js
// //
// //(CÓ PHÂN TRANG Á, NÊN LÚC LOAD XUỐNG TỰ ĐỘNG + THÊM TRANG ) BIẾT RĂNG DƯỚI BACKEND XỬ LÝ KIỂU 
// // public List<QuizstudyInfoResponse> getQuizStudiesByLessonId(Map<String, String> params) {
//     //     } PAGE_SIZE = 5
//     //     // Sắp xếp
//     //     String order = params.getOrDefault("order", "desc");
//     //     if ("asc".equalsIgnoreCase(order)) {
//     //         query.orderBy(builder.asc(root.get("createdAt")));
//     //     } else {
//     //         query.orderBy(builder.desc(root.get("createdAt")));
//     //     }
//     //     query.where(predicates.toArray(new Predicate[0]));
//     //     TypedQuery<Quizstudy> typedQuery = entityManager.createQuery(query);
//     //     // Phân trang
//     //     int page = Integer.parseInt(params.getOrDefault("page", "1"));
//     //     int size =  PAGE_SIZE; // PAGE_SIZE tùy biến
//     //     typedQuery.setFirstResult((page - 1) * size);
//     //     typedQuery.setMaxResults(size);
//     //     List<Quizstudy> results = typedQuery.getResultList();
//     //     return results.stream().map(quizstudy -> {
//     //         Map<String, Integer> resultCounts = new HashMap<>();
//     //         resultCounts.put(Result.CORRECT.getDisplayName(), 0);
//     //         resultCounts.put(Result.INCORRECT.getDisplayName(), 0);
//     //         resultCounts.put(Result.UNRESULT.getDisplayName(), 0);

//     //         for (Quizhistory history : quizstudy.getQuizhistorySet()) {
//     //             String result = history.getResult();
//     //             resultCounts.computeIfPresent(result, (k, v) -> v + 1);
//     //         }
//     //         return QuizstudyInfoResponse.builder()
//     //                 .id(quizstudy.getId())
//     //                 .createdAt(quizstudy.getCreatedAt())
//     //                 .updateAt(quizstudy.getUpdateAt())
//     //                 .lessonId(quizstudy.getLessonId())
//     //                 .userId(quizstudy.getUserId().getAccountId())
//     //                 .results(resultCounts)
//     //                 .teststudy(quizstudy.getTeststudy())
//     //                 .build();
//     //     }).collect(Collectors.toList());
//     // }
// //PHẦN 3: DANH SÁCH FORM QUIZSTUDY ĐƯỢC HIỂN THỊ (xử dụng < QuizStudySet quizstudy_father = {quizstudy}/> để hiển  thị dễ quản lý), mỗi form sẽ có nút xóa - nằm góc phải dùng icon react-icon, trong form 
// //Form QuizStudySet sẽ bao gồm 3 phần
// // phần 1: Hiển thị ngày tạo - dùng momemt (env) - created_date góc phải ở cạnh form là icon xóa -> khi nhấn nút này gọi await authApis().deletet (endpoints.quiz_study_detail(lessonId, quizStudyId))  để xóa và cập nhập lại setQuizStudy update state,
// // phần 2: Gồm bên trái là sơ đồ kết quả (hiện dưới dạng biểu đồ chi phần trănm câu đúng sai tương đẹp bằng js chart biểu đồ tròn donut đi cho đẹp từ result của correct, uncorrect, unresult ), bên phải là thống kê số chi tiết
// // phần 3: nút làm bài thì chuyển qua  QuizStudyDetail/QuizStudyDo.js nhớ lấy quizstudyId và lesson id để khi qua QuizStudyDo.js ta gọi await authApis().get(endpoints.quiz_study_detail(lessonId, quizStudyId)) để lấy danh  sách câu các câu hỏi và trả lời các kiểu 
// //KẾT QUẢ TRẢ VỀ KHI GỌI 
// // {
// //     "code": 1000,
// //     "result": {
// //         "id": 28,
// //         "lessonId": {
// //             "id": 20,
// //             "title": "Hôm nay thứ mấy",
// //             "description": "Bài học ddddddddddddddddddddddddddddddddddddddddd òo SS",
// //             "image": "http://res.cloudinary.com/dnc5sycvb/image/upload/v1754486199/avatars/bkgwgc7sl7habcoqqt4x.jpg",
// //             "visibility": "PUBLIC",
// //             "isCommentLocked": false,
// //             "createdAt": "2025-08-06T13:16:35.000+00:00",
// //             "updateAt": "2025-08-06T13:16:35.000+00:00"
// //         },
// //         "userId": {
// //             "accountId": 30
// //         },
// //         "results": {
// //             "CORRECT": 0,
// //             "INCORRECT": 0,
// //             "UNRESULT": 2
// //         },
// //         "quizhistorySet": [
// //             {
// //                 "id": 68,
// //                 "flashcardId": {
// //                     "id": 34,
// //                     "word": "White Cat",
// //                     "definition": "Mèo màu trắng",
// //                     "image": "http://res.cloudinary.com/dnc5sycvb/image/upload/v1754624342/flashcards/mtnntsac000aebjuqubs.jpg",
// //                     "createdAt": "2025-08-06T13:19:34.000+00:00",
// //                     "updateAt": "2025-08-08T03:39:02.000+00:00"
// //                 },
// //                 "explanation": null,
// //                 "questionType": "TEXT_AUDIO",
// //                 "answerType": "MULTIPLE_CHOICE",
// //                 "result": "UNRESULT",
// //                 "userAnswer": null,
// //                 "quizanswerSet": [
// //                     {
// //                         "id": 95,
// //                         "definition": "Tornado",
// //                         "position": 0,
// //                         "createdAt": "2025-08-08T06:55:05.000+00:00",
// //                         "updateAt": "2025-08-08T06:55:05.000+00:00",
// //                         "historyId": null
// //                     },
// //                     {
// //                         "id": 97,
// //                         "definition": "A mountain that erupts with lava",
// //                         "position": 2,
// //                         "createdAt": "2025-08-08T06:55:05.000+00:00",
// //                         "updateAt": "2025-08-08T06:55:05.000+00:00",
// //                         "historyId": null
// //                     },
// //                     {
// //                         "id": 98,
// //                         "definition": "A violent rotating column of air",
// //                         "position": 3,
// //                         "createdAt": "2025-08-08T06:55:05.000+00:00",
// //                         "updateAt": "2025-08-08T06:55:05.000+00:00",
// //                         "historyId": null
// //                     },
// //                     {
// //                         "id": 96,
// //                         "definition": "Mèo màu trắng",
// //                         "position": 1,
// //                         "createdAt": "2025-08-08T06:55:05.000+00:00",
// //                         "updateAt": "2025-08-08T06:55:05.000+00:00",
// //                         "historyId": null
// //                     }
// //                 ],
// //                 "createdAt": "2025-08-08T06:55:05.000+00:00",
// //                 "updateAt": "2025-08-08T06:55:05.000+00:00"
// //             },
// // 
// }

// export default QuizStudy;
import QuizStudyForm from "./QuizStudyDetail/QuizStudyForm";
import QuizStudySet from "./QuizStudySet/QuizStudySet";
import MySpinner from "../layouts/MySpinner";
import React, { useEffect, useState, useRef, useCallback } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { FaArrowLeft, FaPlus } from "react-icons/fa";
import { authApis, endpoints } from "../../configs/Apis";
import "./QuizStudy.css";

const QuizStudy = () => {
  const { lessonId } = useParams();
  const nav = useNavigate();

  const [showForm, setShowForm] = useState(false);
  const [quizList, setQuizList] = useState([]);
  const [loading, setLoading] = useState(false);
  const [page, setPage] = useState(1);
  const [hasMore, setHasMore] = useState(true);

  const [filters, setFilters] = useState({
    startDate: null,
    endDate: null,
    teststudy: "all",
    order: "desc",
  });

  const observer = useRef();

  const lastQuizStudyRef = useCallback(
    (node) => {
      if (loading) return;
      if (observer.current) observer.current.disconnect();
      observer.current = new IntersectionObserver(
        (entries) => {
          if (entries[0].isIntersecting && hasMore) {
            setPage((prev) => prev + 1);
          }
        },
        {
          threshold: 0.1,
          rootMargin: "100px",
        }
      );
      if (node) observer.current.observe(node);
    },
    [loading, hasMore]
  );

  // Load data mỗi khi page, filters, lessonId thay đổi
  useEffect(() => {
    const loadQuizList = async () => {
      setLoading(true);
      try {
        const params = new URLSearchParams();
        params.append("page", page);
        params.append("order", filters.order);
        if (filters.teststudy !== "all") params.append("teststudy", filters.teststudy);
        if (filters.startDate) params.append("startDate", filters.startDate);
        if (filters.endDate) params.append("endDate", filters.endDate);

        const res = await authApis().get(
          `${endpoints.quiz_study(lessonId)}?${params.toString()}`
        );

        if (res.data.code === 1000) {
          const newData = res.data.result || [];

          setQuizList((prev) => {
            if (page === 1) return newData; // trang 1 thì reset list
            // tránh trùng
            const ids = new Set(prev.map((q) => q.id));
            return [...prev, ...newData.filter((item) => !ids.has(item.id))];
          });

          setHasMore(newData.length > 0);
        }
      } catch (error) {
        console.error(error);
      } finally {
        setLoading(false);
      }
    };

    loadQuizList();
  }, [page, filters, lessonId]);

  // Reset lại list và page khi filter hoặc lessonId thay đổi
  useEffect(() => {
    setQuizList([]);
    setPage(1);
    setHasMore(true);
  }, [filters, lessonId]);

  const handleBackToMyLesson = () => {
    nav(`/lesson/${lessonId}`);
  };
  const handleDelete = async (quizId) => {
    try {
      setQuizList(prev => prev.filter(q => q.id !== quizId)); // chỉ xóa UI
    } catch (err) {
      console.error(err);
    }
  };


  return (
    <div className="quiz-study-page" >
      {/* Header */}
      <div className="quiz-header-row">
        <button className="back-button-quiz-study" onClick={handleBackToMyLesson}>
          <FaArrowLeft style={{ marginRight: 8 }} />
          Back to Lesson
        </button>

        <button className="quiz-create-btn" onClick={() => setShowForm(true)}>
          <FaPlus style={{ marginRight: 6 }} />
          Create Quiz Study
        </button>
      </div>

      {/* Filters */}
      <div className="quiz-filter-bar">
        <input
          type="date"
          value={filters.startDate || ""}
          onChange={(e) => setFilters((prev) => ({ ...prev, startDate: e.target.value }))}
          placeholder="Start Date"
        />
        <input
          type="date"
          value={filters.endDate || ""}
          onChange={(e) => setFilters((prev) => ({ ...prev, endDate: e.target.value }))}
          placeholder="End Date"
        />
        <select
          value={filters.order}
          onChange={(e) => setFilters((prev) => ({ ...prev, order: e.target.value }))}
        >
          <option value="desc">Newest</option>
          <option value="asc">Oldest</option>
        </select>
      </div>

      {showForm && (
        <QuizStudyForm
          lessonId={lessonId}
          onClose={() => setShowForm(false)}
          onCreated={(newQuiz) => {
            setQuizList(prev => [newQuiz, ...prev]); // thêm quiz mới lên đầu
          }}
        />

      )}

      {/* Danh sách QuizStudy */}
      <div className="quiz-list">
        {quizList.map((quizstudy, index) => {
          if (index === quizList.length - 1) {
            // phần tử cuối cùng gán ref để detect scroll
            return (
              <div ref={lastQuizStudyRef} key={quizstudy.id}>
                <QuizStudySet quizstudy_father={quizstudy} onDelete={handleDelete} />
              </div>
            );
          }
          return (
            <QuizStudySet key={quizstudy.id} quizstudy_father={quizstudy} onDelete={handleDelete} />
          );
        })}
      </div>

      {loading && <MySpinner />}
    </div>
  );
};

export default QuizStudy;
