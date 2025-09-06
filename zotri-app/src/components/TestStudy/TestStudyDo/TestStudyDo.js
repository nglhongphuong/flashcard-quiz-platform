// TRƯỚC ĐÓ GỌI API RESET NẾU NGƯỜI DÙNG ĐÃ CÓ LỊCH SỬ LÀM BÀI TRƯỚC ĐÓ ĐI
// gọi api   test_study_reset: (quizStudyId) => `/quiz-study/${quizStudyId}/reset`, 
// GỌI API MỚI HIỆN DANH SÁCH CÁC CÂU HỎI 
//Kết quả trả về khi gọi  const res = await authApis().get(
//     endpoints.test_study_detail(lessonId, quizStudyId)
// );
//
// {
//     "code": 1000,
//     "result": {
//         "id": 52,
//         "lessonId": {
//             "id": 20,
//             "title": "Hôm nay thứ mấy",
//             "description": "Bài học ddddddddddddddddddddddddddddddddddddddddd òo SS",
//             "image": "http://res.cloudinary.com/dnc5sycvb/image/upload/v1754486199/avatars/bkgwgc7sl7habcoqqt4x.jpg",
//             "visibility": "PUBLIC",
//             "isCommentLocked": false,
//             "createdAt": "2025-08-06T13:16:35.000+00:00",
//             "updateAt": "2025-08-06T13:16:35.000+00:00"
//         },
//         "userId": {
//             "accountId": 26
//         },
//         "results": {
//             "CORRECT": 0,
//             "INCORRECT": 0,
//             "UNRESULT": 3
//         },
//         "quizhistorySet": [
//             {
//                 "id": 139,
//                 "flashcardId": {
//                     "id": 40,
//                     "word": "Word",
//                     "definition": "Tornado",
//                     "image": null,
//                     "createdAt": "2025-08-06T13:19:34.000+00:00",
//                     "updateAt": "2025-08-06T13:19:34.000+00:00"
//                 },
//                 "explanation": null,
//                 "questionType": "TEXT_AUDIO",
//                 "answerType": "TRUE_FALSE",
//                 "result": "UNRESULT",
//                 "userAnswer": null,
//                 "quizanswerSet": [
//                     {
//                         "id": 195,
//                         "definition": "Tornado",
//                         "position": 0,
//                         "createdAt": "2025-08-09T12:49:56.000+00:00",
//                         "updateAt": "2025-08-09T12:49:56.000+00:00",
//                         "historyId": null
//                     }
//                 ],
//                 "createdAt": "2025-08-09T12:49:56.000+00:00",
//                 "updateAt": "2025-08-09T12:49:56.000+00:00"
//             },
//             {
//                 "id": 140,
//                 "flashcardId": {
//                     "id": 41,
//                     "word": "Definition",
//                     "definition": "A violent rotating column of air",
//                     "image": null,
//                     "createdAt": "2025-08-06T13:19:34.000+00:00",
//                     "updateAt": "2025-08-06T13:19:34.000+00:00"
//                 },
//                 "explanation": null,
//                 "questionType": "TEXT_AUDIO",
//                 "answerType": "MULTIPLE_CHOICE",
//                 "result": "UNRESULT",
//                 "userAnswer": null,
//                 "quizanswerSet": [
//                     {
//                         "id": 196,
//                         "definition": "Tornado",
//                         "position": 0,
//                         "createdAt": "2025-08-09T12:49:56.000+00:00",
//                         "updateAt": "2025-08-09T12:49:56.000+00:00",
//                         "historyId": null
//                     },
//                     {
//                         "id": 198,
//                         "definition": "A violent rotating column of air",
//                         "position": 2,
//                         "createdAt": "2025-08-09T12:49:56.000+00:00",
//                         "updateAt": "2025-08-09T12:49:56.000+00:00",
//                         "historyId": null
//                     },
//                     {
//                         "id": 199,
//                         "definition": "Glacier",
//                         "position": 3,
//                         "createdAt": "2025-08-09T12:49:56.000+00:00",
//                         "updateAt": "2025-08-09T12:49:56.000+00:00",
//                         "historyId": null
//                     },
//                     {
//                         "id": 197,
//                         "definition": "A slow-moving mass of ice",
//                         "position": 1,
//                         "createdAt": "2025-08-09T12:49:56.000+00:00",
//                         "updateAt": "2025-08-09T12:49:56.000+00:00",
//                         "historyId": null
//                     }
//                 ],
//                 "createdAt": "2025-08-09T12:49:56.000+00:00",
//                 "updateAt": "2025-08-09T12:49:56.000+00:00"
//             },
//             {
//                 "id": 141,
//                 "flashcardId": {
//                     "id": 39,
//                     "word": "Image URL",
//                     "definition": "",
//                     "image": null,
//                     "createdAt": "2025-08-06T13:19:34.000+00:00",
//                     "updateAt": "2025-08-06T13:19:34.000+00:00"
//                 },
//                 "explanation": null,
//                 "questionType": "TEXT_AUDIO",
//                 "answerType": "TEXT_INPUT",
//                 "result": "UNRESULT",
//                 "userAnswer": null,
//                 "quizanswerSet": [],
//                 "createdAt": "2025-08-09T12:49:56.000+00:00",
//                 "updateAt": "2025-08-09T12:49:56.000+00:00"
//             }
//         ],
//         "teststudy": {
//             "quizId": 52,
//             "min": 5
//         },
//         "createdAt": "2025-08-09T12:49:56.000+00:00",
//         "updateAt": "2025-08-09T12:49:56.000+00:00"
//     }
// }
//HIỆN DANH SÁCH CÁC TEST FLASHCARD SET , WORD, ĐỊNH NGHĨA VÀ ĐÁP ÁN
// KHI NGƯỜI DÙNG CHỌN ĐÁP ÁN THÌ PHẦN ĐƯỢC CHỌN SẼ CÓ MÀU XANH PASTEL CÒN KO LÀ XÁM
// SẼ CÓ BẤM GIỜ THÒI GIANLAAYS TỪ MIN ĐẾM NGƯỢC
// KIỂU 2 PHẦN BÊN TRÁI LÀ DANH SÁCH FLASHCARDSET TEST TỪNG CÂU HỎI KIỂM TRA (ĐÁNH DẤU SỐ THỨ TỰ), BÊN PHẢI LÀ BỘ ĐẾM THỜI GIAN Ở TRÊNM DANH SÁCH THỨ TỰ CÂU HỎI LÀM ( NẾU ĐÃ CHỌN ĐÁP ÁN, HÌNH TRÒN THỨ TỰ ĐÓ HIỆN MÀU PASTEL)
// BÊN PHẢI GỒM PHẦN 1 BỘ ĐẾM TGIAN, PHẦN 2 DANH SÁCH CÁC STT LƯU CÂU HỎI, PHẦN 3 NÚT COMPLETE NẾU NGƯỜI DÙNG MUỐN COMPLETE THÌ NAVIGATE QUA TRANG  <Route path="/lesson/:lessonId/test-study/:quizStudyId/result" element={<QuizStudyResult />} />
// nếu bấm giờ kết thúc thì cũng thu về kết quả lun

//TRƯỜNG HỢP NHẤN FINISH HOẶC HẾT THỜI GIAN -> CHẠY HÀM XỬ LÝ HANDLE RESULT
// TRUYỀN PARAMS LÊN CHO SERVER DƯỚI DẠNG (Kiểu nhớ lưu lại mỗi lần người dùng nhấn nút, hoặc thay đổi khi họ thay đổi á, tại sợ có sự cố lưu lại kiểu zị)
// GỌI AUTHAPI.POST CÓ ENDPOINT    quiz_history_bulk: '/quiz-history-bulk',
// [
//   {
//     "quizhistoryId": 24, //CÂU HỎI TRẢ LỜI
//     "userAnswer": "true" // KIỂU TRẢ LỜI CỦA NGƯỜI DÙNG KHI NHẤN NÚT TRUE HOẶC FALSE , NẾU FALSE THÌ TRUYỀN "false"
//   },
//   {
//     "quizhistoryId": 26,
//     "userAnswer": "Con mèo màu trắng" // KIỂU TRẢ Lời DƯỚI DẠNG TEXT
//   },
//   {
//     "quizhistoryId": 25,
//     "userAnswer": "9" // Id của quizanswer //Truyền id của quiz-answer đối với multiple choice
//   }
// ]
// 

import "./TestStudyDo.css";
import MySpinner from "../../layouts/MySpinner";
import React, { useEffect, useState, useRef } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { authApis, endpoints } from "../../../configs/Apis";
import DetectLanguage from "../../../configs/DetectLanguage";
import { FaArrowLeft, FaVolumeUp } from "react-icons/fa";

const formatTime = (s) => `${Math.floor(s / 60)}:${String(s % 60).padStart(2, "0")}`;

const TestStudyDo = () => {
  const { lessonId, quizStudyId } = useParams();
  const navigate = useNavigate();

  const [loading, setLoading] = useState(true);
  const [quizStudy, setQuizStudy] = useState(null); // full result from API
  const [questions, setQuestions] = useState([]); // quizhistorySet sorted
  const [answers, setAnswers] = useState({}); // { [historyId]: userAnswer }
  const [timeLeft, setTimeLeft] = useState(0);
  const [currentIdx, setCurrentIdx] = useState(0);
  const timerRef = useRef(null);
  const savingRef = useRef(false);

  //reset (optional) + load data
  useEffect(() => {
    const load = async () => {
      setLoading(true);
      try {
        // optional reset endpoint (call if you want to force reset previous answers)
        try {
          await authApis().put(endpoints.test_study_reset(quizStudyId));
        } catch (e) {
          // ignore if reset not needed/failed
        }

        const res = await authApis().get(
          endpoints.test_study_detail(lessonId, quizStudyId)
        );
        const data = res.data.result;
        // ensure quizhistorySet ordered by createdAt or some consistent order
        const sorted = (data.quizhistorySet || []).slice().sort((a, b) =>
          new Date(a.createdAt) - new Date(b.createdAt)
        );
        setQuizStudy(data);
        setQuestions(sorted);
        // populate answers map from result (but UI will not highlight until user selects; we still store for resume)
        const map = {};
        sorted.forEach((q) => {
          if (q.userAnswer != null) map[q.id] = String(q.userAnswer);
        });
        setAnswers(map);
        setTimeLeft((data.teststudy?.min || 0) * 60);
      } catch (err) {
        console.error("Load test study failed", err);
      } finally {
        setLoading(false);
      }
    };

    load();
    // cleanup on unmount
    return () => clearInterval(timerRef.current);
  }, [lessonId, quizStudyId]);

  //timer
  useEffect(() => {
    if (loading) return;
    if (timeLeft <= 0) return;
    timerRef.current = setInterval(() => {
      setTimeLeft((t) => t - 1);
    }, 1000);
    return () => clearInterval(timerRef.current);
  }, [loading]);

  useEffect(() => {
    if (!loading && timeLeft <= 0 && quizStudy) {
      handleFinish();
    }
  }, [timeLeft, loading]);

  const saveSingleAnswer = async (historyId, userAnswer) => {
    try {
      await authApis().put(endpoints.quiz_history(historyId), { userAnswer });
    } catch (err) {
      console.error("Save single answer failed", err);
    }
  };

  const handleSpeak = (definition) => {
    const utterance = new SpeechSynthesisUtterance(definition);
    const voices = window.speechSynthesis.getVoices();
    const lang = DetectLanguage(definition);

    const selectedVoice = voices.find(voice => voice.lang.startsWith(lang));

    if (selectedVoice) {
      utterance.voice = selectedVoice;
    }

    speechSynthesis.speak(utterance);
  };

  const handleAnswer = (historyId, value) => {
    setAnswers((prev) => {
      const next = { ...prev, [historyId]: String(value) };
      return next;
    });
    saveSingleAnswer(historyId, String(value));
  };
  const goToIndex = (idx) => {
    if (idx < 0 || idx >= questions.length) return;
    setCurrentIdx(idx);
    window.scrollTo({ top: 0, behavior: "smooth" });
  };
  const handleFinish = async () => {
    if (savingRef.current) return;
    savingRef.current = true;
    const payload = Object.entries(answers).map(([quizhistoryId, userAnswer]) => ({
      quizhistoryId: Number(quizhistoryId),
      userAnswer,
    }));
    try {
      if (payload.length > 0) {
        await authApis().post(endpoints.quiz_history_bulk, payload);
      } else {
      }
    } catch (err) {
      console.error("Bulk save failed", err);
    } finally {
      savingRef.current = false;
      navigate(`/lesson/${lessonId}/test-study/${quizStudyId}/result`);
    }
  };

  const current = questions[currentIdx];

  if (loading) return <MySpinner />;
  if (!quizStudy) return <p className="ts-error">No test study found.</p>;

  return (
    <div className="ts-page">
      <div className="ts-left">
        {/* Header */}
        <div className="ts-topbar">
          <button className="ts-back" onClick={() => navigate(-1)}><FaArrowLeft /> Back</button>
        </div>

        {/* Cards list (all questions shown as stacked cards) */}
        <div className="ts-cards">
          {questions.map((q, idx) => {
            const isActive = idx === currentIdx;
            const userAns = answers[q.id];
            return (
              <div key={q.id} className={`ts-card ${isActive ? "active" : ""}`}>
                <div className="ts-card-header">
                  <div className="ts-card-index">Question {idx + 1}</div>
                  <div className="ts-card-date">{new Date(q.createdAt).toLocaleString()}</div>
                </div>
                <label className="ts-label">Word: </label>
                <div className="ts-card-body">
                  <div className="ts-question">
                    <h3 className="ts-card-word">{q.flashcardId.word}</h3>
                    <button
                      className="audio-player"
                      onClick={(e) => {
                        e.stopPropagation();
                        handleSpeak(q.flashcardId.word);
                      }}
                    >
                      <FaVolumeUp />
                    </button>
                  </div>


                  {/* question prompt area */}
                  {q.answerType === "TEXT_INPUT" && (
                    <>
                      <label className="ts-label">Enter the definition</label>
                      <input
                        className="ts-input"
                        placeholder="Enter..."
                        value={userAns || ""}
                        onChange={(e) => handleAnswer(q.id, e.target.value)}
                      />
                    </>
                  )}

                  {q.answerType === "TRUE_FALSE" && (
                    <div className="ts-choices true-false">
                      <>
                        <label className="ts-label">Definition</label>
                        <div className="ts-def-pill">{q.quizanswerSet[0]?.definition || ""}</div>

                        <div className="ts-choices">
                          <button
                            className={`ts-choice ${userAns === "true" ? "selected" : ""}`}
                            onClick={() => handleAnswer(q.id, "true")}
                          >
                            True
                          </button>
                          <button
                            className={`ts-choice ${userAns === "false" ? "selected" : ""}`}
                            onClick={() => handleAnswer(q.id, "false")}
                          >
                            False
                          </button>
                        </div>
                      </>
                    </div>
                  )}

                  {q.answerType === "MULTIPLE_CHOICE" && (
                    <>
                       <label className="ts-choice-haha">Choices</label>
                    <div className="ts-choices-grid multiple-choice">
                    
                        <div className="ts-choices-grid">
                          {q.quizanswerSet
                            .slice()
                            .sort((a, b) => a.position - b.position)
                            .map((opt) => (
                              <button
                                key={opt.id}
                                className={`ts-mc ${userAns === String(opt.id) ? "selected" : ""}`}
                                onClick={() => handleAnswer(q.id, String(opt.id))}
                              >
                                {opt.definition}
                              </button>
                            ))}
                        </div>
                    
                    </div>
                    </>
                  )}
                </div>
              </div>
            );
          })}
        </div>
      </div>

      <aside className="ts-right">
        <div className="ts-right-box">
          <div className="ts-right-header">
            <div className="ts-right-title">Questions</div>
            <div className="ts-timer">{formatTime(timeLeft)}</div>
          </div>

          <div className="ts-grid">
            {questions.map((q, idx) => (
              <button
                key={q.id}
                className={`ts-circle ${answers[q.id] ? "answered" : ""} ${idx === currentIdx ? "active" : ""}`}
                onClick={() => goToIndex(idx)}
              >
                {idx + 1}
              </button>
            ))}
          </div>

          <div className="ts-actions">
            <button className="ts-nav" onClick={() => goToIndex(Math.max(0, currentIdx - 1))}>Prev</button>
            <button className="ts-complete" onClick={handleFinish}>Complete</button>
            <button className="ts-nav" onClick={() => goToIndex(Math.min(questions.length - 1, currentIdx + 1))}>Next</button>
          </div>
        </div>
      </aside>
    </div>
  );
};

export default TestStudyDo;
