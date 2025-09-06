//quiz_study_detail: (lessonId,quizStudyId)
//Gọi danh sách thông qua await authApis().get(endpoints.quiz_study_detail(lessonId,quizStudyId)) 
////Quiz Study hien danh sách giống bài kiểm tra á (Nhưng mà khác ở chỗ không tính thời gian, và khi người dùng điền đáp án / hoặc chọn đáp án thì sẽ hiện kế quả đúng sai lun) thông qua phương thức await authApis().put(endpoints.quiz_history(quizhistoryId))
//trả ra danh sách 
//     "code": 1000,
// "result": {
//     "id": 35,
//     "lessonId": {
//         "id": 20,
//         "title": "Hôm nay thứ mấy",
//         "description": "Bài học ddddddddddddddddddddddddddddddddddddddddd òo SS",
//         "image": "http://res.cloudinary.com/dnc5sycvb/image/upload/v1754486199/avatars/bkgwgc7sl7habcoqqt4x.jpg",
//         "visibility": "PUBLIC",
//         "isCommentLocked": false,
//         "createdAt": "2025-08-06T13:16:35.000+00:00",
//         "updateAt": "2025-08-06T13:16:35.000+00:00"
//     },
//     "userId": {
//         "accountId": 26
//     },
//     "results": {
//         "CORRECT": 0,
//         "INCORRECT": 0,
//         "UNRESULT": 4
//     },
//     "quizhistorySet": [
//         {
//             "id": 88,
//             "flashcardId": {
//                 "id": 37,
//                 "word": "Word",
//                 "definition": "Glacier",
//                 "image": null,
//                 "createdAt": "2025-08-06T13:19:34.000+00:00",
//                 "updateAt": "2025-08-06T13:19:34.000+00:00"
//             },
//             "explanation": null,
//             "questionType": "TEXT_AUDIO",
//             "answerType": "MULTIPLE_CHOICE",
//             "result": "UNRESULT",
//             "userAnswer": null,
//             "quizanswerSet": [
//                 {
//                     "id": 129,
//                     "definition": "A slow-moving mass of ice",
//                     "position": 1,
//                     "createdAt": "2025-08-08T14:30:34.000+00:00",
//                     "updateAt": "2025-08-08T14:30:34.000+00:00",
//                     "historyId": null
//                 },
//                 {
//                     "id": 128,
//                     "definition": "Glacier",
//                     "position": 0,
//                     "createdAt": "2025-08-08T14:30:34.000+00:00",
//                     "updateAt": "2025-08-08T14:30:34.000+00:00",
//                     "historyId": null
//                 },
//                 {
//                     "id": 130,
//                     "definition": "Tornado",
//                     "position": 2,
//                     "createdAt": "2025-08-08T14:30:34.000+00:00",
//                     "updateAt": "2025-08-08T14:30:34.000+00:00",
//                     "historyId": null
//                 },
//                 {
//                     "id": 131,
//                     "definition": "https:",
//                     "position": 3,
//                     "createdAt": "2025-08-08T14:30:34.000+00:00",
//                     "updateAt": "2025-08-08T14:30:34.000+00:00",
//                     "historyId": null
//                 }
//             ],
//             "createdAt": "2025-08-08T14:30:34.000+00:00",
//             "updateAt": "2025-08-08T14:30:34.000+00:00"
//         },
//         {
//             "id": 87,
//             "flashcardId": {
//                 "id": 40,
//                 "word": "Word",
//                 "definition": "Tornado",
//                 "image": null,
//                 "createdAt": "2025-08-06T13:19:34.000+00:00",
//                 "updateAt": "2025-08-06T13:19:34.000+00:00"
//             },
//             "explanation": null,
//             "questionType": "TEXT_AUDIO",
//             "answerType": "TEXT_INPUT",
//             "result": "UNRESULT",
//             "userAnswer": null,
//             "quizanswerSet": [],
//             "createdAt": "2025-08-08T14:30:34.000+00:00",
//             "updateAt": "2025-08-08T14:30:34.000+00:00"
//         },
//                        "id": 85,
//     "id": 86,
//     "flashcardId": {
//         "id": 39,
//         "word": "Image URL",
//         "definition": "",
//         "image": null,
//         "createdAt": "2025-08-06T13:19:34.000+00:00",
//         "updateAt": "2025-08-06T13:19:34.000+00:00"
//     },
//     "explanation": null,
//     "questionType": "TEXT_AUDIO",
//     "answerType": "TRUE_FALSE",
//     "result": "UNRESULT",
//     "userAnswer": null,
//     "quizanswerSet": [
//         {
//             "id": 127,
//             "definition": "",
//             "position": 0,
//             "createdAt": "2025-08-08T14:30:34.000+00:00",
//             "updateAt": "2025-08-08T14:30:34.000+00:00",
//             "historyId": null
//         }
//     ],
//     "createdAt": "2025-08-08T14:30:34.000+00:00",
//     "updateAt": "2025-08-08T14:30:34.000+00:00"
// }
//Đánh số thứ tự từng quizhistoryset
// MỖI FORM QUIZHISTORY SET BO GÓC NỀN ĐEN CHỮ TRẮNG TA CÓ
// Phần 1: Hiện từng câu hỏi gồm câu hỏi word VÀ AUDIO ( NẾU QUESTION TYPE CHỈ CÓ AUDIO THÌ HIỆN AUDIO)
// Phần 2: Hiện lựa chọn trả lời 
// Nếu là MULTIPLECHOCE THÌ HIỆN DEFINITION 4 LỰA CHỌN CHO NGƯỜI DÙNG CLICK ( CÁC VỊ TRÍ LÀ CỘT 2 DÒNG THEO POSITION CỦA TỪNG CÁI QUIZANSWERSET)
// ->>>> khi người dùng nhấn để truyền qua const res = await authApis().put(endpoints.quiz_history(quizhistoryId)) thì truyền id của answerset vd
//// {
//   "userAnswer": "129" //Truyền id của quiz-answer đối với multiple choice
// }// KẾT QUẢ CỦA RES TRẢ VỀ 
// {
//     "code": 1000,
//     "message": "Updated successfully !!",
//     "result": {
//         "id": 88,
//         "flashcardId": {
//             "id": 37,
//             "word": "Word",
//             "definition": "Glacier",
//             "image": null,
//             "createdAt": "2025-08-06T13:19:34.000+00:00",
//             "updateAt": "2025-08-06T13:19:34.000+00:00"
//         },
//         "explanation": null,
//         "questionType": "TEXT_AUDIO",
//         "answerType": "MULTIPLE_CHOICE",
//         "result": "INCORRECT",
//         "userAnswer": "127",
//         "quizanswerSet": [
//             {
//                 "id": 128,
//                 "definition": "Glacier",
//                 "position": 0,
//                 "createdAt": "2025-08-08T14:30:34.000+00:00",
//                 "updateAt": "2025-08-08T14:30:34.000+00:00",
//                 "historyId": {
//                     "id": 88,
//                     "explanation": null,
//                     "questionType": "TEXT_AUDIO",
//                     "answerType": "MULTIPLE_CHOICE",
//                     "result": "INCORRECT",
//                     "userAnswer": "127",
//                     "createdAt": "2025-08-08T14:30:34.000+00:00",
//                     "updateAt": "2025-08-08T14:47:30.874+00:00"
//                 }
//             },
//             {
//                 "id": 129,
//                 "definition": "A slow-moving mass of ice",
//                 "position": 1,
//                 "createdAt": "2025-08-08T14:30:34.000+00:00",
//                 "updateAt": "2025-08-08T14:30:34.000+00:00",
//                 "historyId": {
//                     "id": 88,
//                     "explanation": null,
//                     "questionType": "TEXT_AUDIO",
//                     "answerType": "MULTIPLE_CHOICE",
//                     "result": "INCORRECT",
//                     "userAnswer": "127",
//                     "createdAt": "2025-08-08T14:30:34.000+00:00",
//                     "updateAt": "2025-08-08T14:47:30.874+00:00"
//                 }
//             },
//             {
//                 "id": 130,
//                 "definition": "Tornado",
//                 "position": 2,
//                 "createdAt": "2025-08-08T14:30:34.000+00:00",
//                 "updateAt": "2025-08-08T14:30:34.000+00:00",
//                 "historyId": {
//                     "id": 88,
//                     "explanation": null,
//                     "questionType": "TEXT_AUDIO",
//                     "answerType": "MULTIPLE_CHOICE",
//                     "result": "INCORRECT",
//                     "userAnswer": "127",
//                     "createdAt": "2025-08-08T14:30:34.000+00:00",
//                     "updateAt": "2025-08-08T14:47:30.874+00:00"
//                 }
//             },
//             {
//                 "id": 131,
//                 "definition": "https:",
//                 "position": 3,
//                 "createdAt": "2025-08-08T14:30:34.000+00:00",
//                 "updateAt": "2025-08-08T14:30:34.000+00:00",
//                 "historyId": {
//                     "id": 88,
//                     "explanation": null,
//                     "questionType": "TEXT_AUDIO",
//                     "answerType": "MULTIPLE_CHOICE",
//                     "result": "INCORRECT",
//                     "userAnswer": "127",
//                     "createdAt": "2025-08-08T14:30:34.000+00:00",
//                     "updateAt": "2025-08-08T14:47:30.874+00:00"
//                 }
//             }
//         ],
//         "createdAt": "2025-08-08T14:30:34.000+00:00",
//         "updateAt": "2025-08-08T14:47:30.872+00:00"
//     }
// }

//Kết quả trả về khi người dùng nhấn nè -> hiện lun nếu đúng thì chúc mừng ý là chỗ họ chọn (button -> màu xanh) còn lại màu đỏ, 
// nếu sai hiện thông báo bạn sai rồi bằng tiếng anh. tương tự đáp án nào đúng

// Nếu TRUE_FLASE thì hiển thị định nghĩa của anwsershet 
// Ở dưới hiện 2 nút 2 bên nằm giữa TRUE  và False cho người dùng nhấn nút
// khi nhấn nút truyền vầy nè 
//// {
//   "userAnswer": "true" // truyền đúng true / false =))) 
// }
// Thì kết quả res trả về cũng cho ta biết correct hay incorrect,
// NẾU ĐÚNG -> Hiện thôn báo thành công  vànút đã chọn màu xanh lá pastel ,  nút còn lại màu đỏ
// NẾU SAI -> Đảo ngược lại =)))))
// Nếu TEXT -> đơn giản
// Hiện input nền đen chữ trắng cho ngươi dùng nhập , kết bên là submit
// Khi submit -> cũngg tương tự truyền api dưới dạng 
// 
// {
//   "userAnswer": "Quả nho" // đối với text thì gõ text như bình thường
// }
// Xong cũng trả về kết quả correct hoặc incorrect, nếu đúng thì hiện "GREAT!" , sai thì hiện definition của word lun 
// NGƯỜI DÙNG CŨNG CÓ THỂ SKIPP BỎ QUA =))))
// SAU KHI THỰC HIỆN XONG ĐẾN FLASHCARD CUỐI HIỆN NÚT FINISH VÀ THỐNG KÊ KẾT QUẢ THÔNG QUA GỌI LẠI  authApis().get(endpoints.quiz_study_detail(lessonId,quizStudyId))  
// MÌNH CÓ THỂ DÙNG FILE <QuizstudyResult /> để trình bày, biểu đồ , điểm số người dùng 
// TRONG QUIZ STUDY RESULT 
// PHẦN 1: THÔNG TIN KẾT QUẢ (BIỂU ĐỒ CHART.JS HÌNH TRÒN BÊN TRÁI, BÊN PHẢI LÀ SÓ TỪ CORRECT, ...)
// Phần 2: NÚT LÀM LẠI VÀ NÚT THOÁT CĂN GIỮA 
//

import "./QuizStudyDo.css";
import MySpinner from "../../layouts/MySpinner";
import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { authApis, endpoints } from "../../../configs/Apis";
import DetectLanguage from "../../../configs/DetectLanguage"
import { FaArrowLeft } from "react-icons/fa";
import QuizStudyResult from "./QuizStudyResult";
import { FaVolumeUp, FaChevronLeft, FaChevronRight } from "react-icons/fa";

const QuizStudyDo = () => {
    const { lessonId, quizStudyId } = useParams();
    const navigate = useNavigate();
    const [showResult, setShowResult] = useState(false);
    const [loading, setLoading] = useState(true);
    const [quizStudy, setQuizStudy] = useState(null);
    const [index, setIndex] = useState(0);
    const [finished, setFinished] = useState(false);

    useEffect(() => {
        const loadQuizStudy = async () => {
            try {
                const res = await authApis().get(
                    endpoints.quiz_study_detail(lessonId, quizStudyId)
                );
                setQuizStudy(res.data.result);
            } catch (err) {
                console.error(err);
            } finally {
                setLoading(false);
            }
        };
        loadQuizStudy();
    }, [lessonId, quizStudyId]);

    const updateAnswer = async (quizhistoryId, payload) => {
        try {
            const res = await authApis().put(
                endpoints.quiz_history(quizhistoryId),
                payload
            );
            const updated = quizStudy.quizhistorySet.map((q) =>
                q.id === res.data.result.id ? res.data.result : q
            );
            setQuizStudy({ ...quizStudy, quizhistorySet: updated });
        } catch (err) {
            console.error(err);
        }
    };

    const skip = () => {
        setShowResult(false);
        if (index + 1 >= quizStudy.quizhistorySet.length) setFinished(true);
        else setIndex(index + 1);
    };

    const handleAnswer = async (quizhistoryId, payload) => {
        await updateAnswer(quizhistoryId, payload);
        //Sau khi update xong, cần tìm lại "current" từ updated quizStudy
        const newCurrent = quizStudy.quizhistorySet.find(q => q.id === quizhistoryId);
        if (newCurrent && newCurrent.result !== "UNRESULT") {
            setShowResult(true);
        }
    };

    useEffect(() => {
        setShowResult(false); //reset mỗi lần qua câu mới
    }, [index]);



    if (loading) return <MySpinner />;
    if (!quizStudy) return <p>Không tìm thấy dữ liệu.</p>;

    if (finished) {
        navigate(`/lesson/${lessonId}/quiz-study/${quizStudyId}/result`);
        return null; //tránh render dư
    }
    const current = quizStudy.quizhistorySet[index];

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

    return (
        <div className="quiz-container">
            <button className="back-btn" onClick={() => navigate(-1)}>
                <FaArrowLeft /> Back
            </button>
            <div className="quiz-box">
                <h5>
                    Question {index + 1} / {quizStudy.quizhistorySet.length}
                </h5>

                <div className="question-box">
                    <p><strong>Word:</strong></p>
                    {current.questionType === "TEXT_AUDIO" && (
                        <>
                            <h4 className="word-study">{current.flashcardId.word}</h4>
                        </>
                    )}
                    <button
                        className="audio-player"
                        onClick={(e) => {
                            e.stopPropagation();
                            handleSpeak(current.flashcardId.word);
                        }}
                    >
                        <FaVolumeUp />
                    </button>
                </div>

                <div className="answer-box">
                    <p><strong>Definition:</strong> </p>

                    {current.answerType === "MULTIPLE_CHOICE" && (
                        <div className="choices-grid">
                            {current.quizanswerSet
                                .slice() //tạo bản copy tránh mutate mảng gốc
                                .sort((a, b) => a.position - b.position) //sort theo position tăng dần
                                .map((a) => {
                                    const isCorrect = a.historyId?.result === "CORRECT";
                                    const isIncorrect = a.historyId?.result === "INCORRECT";
                                    const isUserAnswer = current.userAnswer == a.id;
                                    return (
                                        <button
                                            key={a.id}
                                            className={`answer-btn ${isCorrect ? "correct" : isUserAnswer && isIncorrect ? "incorrect" : ""
                                                }`}
                                            onClick={() =>
                                                handleAnswer(current.id, {
                                                    userAnswer: `${a.id}`,
                                                })
                                            }
                                        >
                                            {a.definition}
                                        </button>
                                    );
                                })}
                        </div>
                    )}

                    {current.answerType === "TRUE_FALSE" && (
                        <div className="true-false-box-wrapper">
                            <h3 className="true-false-definition">{current.quizanswerSet[0]?.definition}</h3>


                           <div className="true-false-box">
        <button
            className={`answer-btn ${
                showResult && current.userAnswer === "true"
                    ? current.result === "CORRECT" 
                        ? "correct" 
                        : "incorrect"
                    : ""
            }`}
            onClick={() => {
                if (!showResult) {
                    handleAnswer(current.id, { userAnswer: "true" });
                }
            }}
        >
            TRUE
        </button>

        <button
            className={`answer-btn ${
                showResult && current.userAnswer === "false"
                    ? current.result === "CORRECT" 
                        ? "correct" 
                        : "incorrect"
                    : ""
            }`}
            onClick={() => {
                if (!showResult) {
                    handleAnswer(current.id, { userAnswer: "false" });
                }
            }}
        >
            FALSE
        </button>
    </div>
                        </div>
                    )}


                    {current.answerType === "TEXT_INPUT" && (
                        <TextInputForm
                            question={current}
                            handleAnswer={handleAnswer}
                        />
                    )}
                </div>
                {showResult && current.result !== "UNRESULT" && (
                    <div className="result-message">
                        {current.result === "CORRECT" ? (
                            <p className="correct-msg">GREAT!  {current.flashcardId.definition}</p>
                        ) : (
                            <p className="incorrect-msg">
                                Incorrect! Definition: {current.flashcardId.definition}
                            </p>
                        )}
                    </div>
                )}

                <div className="skip-box">
                    <button onClick={skip} className="skip-btn">
                        Skip
                    </button>
                </div>
            </div>
        </div>
    );
};

const TextInputForm = ({ question, handleAnswer }) => {
    const [text, setText] = useState("");

    const submit = () => {
        if (!text.trim()) return;
        handleAnswer(question.id, { userAnswer: text.trim() });
        setText("");
    };

    return (
        <div className="text-input-box">
            <input
                type="text"
                value={text}
                onChange={(e) => setText(e.target.value)}
                className="text-input"
                placeholder="Type your answer..."
            />
            <button onClick={submit} className="submit-btn">
                Submit
            </button>
        </div>
    );
};


export default QuizStudyDo;
