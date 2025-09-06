import React, { useEffect, useState, useContext } from "react";
import { useParams, useNavigate } from "react-router-dom";
import moment from "moment";
import "moment/locale/vi";
import { FaQuestionCircle, FaPencilAlt, FaClone, FaStar, FaStarHalfAlt, FaRegStar, FaBookmark } from "react-icons/fa";
import { BsBarChart } from "react-icons/bs";
import Apis, { endpoints } from "../../configs/Apis";
import { MyUserContext, MyDispatcherContext } from "../../configs/MyContexts";
import Flashcard from "../Flashcard/Flashcard";
import Comment from "../Comment/Comment"
import "./LessonDetail.css";
import Schedule from "../Interaction/Schedule/Schedule";
import Rating from "../Interaction/Rating/Rating";

moment.locale("en");

const LessonDetail = () => {
  const { lessonId } = useParams();
  const [lesson, setLesson] = useState(null);
  const [rating, setRating] = useState(null);
  const [comments, setComments] = useState([]);
  const user = useContext(MyUserContext);
  const navigate = useNavigate();

  const [showRating, setShowRating] = useState(false);
  const [showSchedule, setShowSchedule] = useState(false);

  useEffect(() => {
    const fetchData = async () => {
      try {
        const lessonRes = await Apis.get(endpoints.lesson_detail(lessonId));
        setLesson(lessonRes.data);

        const ratingRes = await Apis.get(endpoints.rating(lessonId));
        setRating(ratingRes.data.result);

        const commentRes = await Apis.get(`${endpoints.lesson}${lessonId}/comment`);
        setComments(commentRes.data.result);
      } catch (err) {
        console.error("Lỗi khi tải dữ liệu chi tiết bài học:", err);
      }
    };
    fetchData();
  }, [lessonId]);

  const handleRequireLogin = () => {
    navigate("/login");
  };
  const handleToFlashcard = () => {
    navigate(`/lesson/${lessonId}/flashcard-study`);
  };

  const handleToQuiz = () => {
    navigate(`/lesson/${lessonId}/quiz-study`);
  };

  const handleToTest = () => {
    navigate(`/lesson/${lessonId}/test-study`);
  };

  if (!lesson) return <div>Đang tải dữ liệu...</div>;

  return (
    <div className="lesson-detail-container">
      <div className="lesson-header">

        <div className="header-content">
          <div className="header-img">
            <img
              src={lesson.result.image || "https://reactjs.org/logo-og.png"}
              alt="lesson"
              className="image-lesson"
            />
          </div>

          <div className="header-info">
            <div className="header-top">
              <div className="header-left">
                <h3>{lesson.result.title}</h3>
                <div className="rating-info">
                  {[1, 2, 3, 4, 5].map((i) => {
                    if (rating?.averageRating >= i)
                      return <FaStar key={i} className="star" />;
                    else if (rating?.averageRating >= i - 0.5)
                      return <FaStarHalfAlt key={i} className="star" />;
                    else return <FaRegStar key={i} className="star" />;
                  })}
                  <span className="score">/5</span>
                  <BsBarChart style={{ margin: "0 4px" }} />
                  <span className="study-count">Studied by {rating?.totalUser || 0}</span>
                </div>
              </div>


              {/* <div className="author">
                {lesson.result.userInfo.avatar ? (
                  <img className="avatar-author" src={lesson.result.userInfo.avatar} alt="avatar" />
                ) : (
                  <div className="avatar-placeholder">
                    {lesson.result.userInfo.name?.charAt(0).toUpperCase()}
                  </div>
                )}
                <span className="name">{lesson.result.userInfo.name}</span>
                <span className="time">
                  {moment(lesson.result.createdAt).fromNow()}
                </span>
              </div> */}
              <div className="author">
                {lesson.result.userInfo.avatar ? (
                  <img
                    className="avatar-author"
                    src={lesson.result.userInfo.avatar}
                    alt="avatar"
                    onClick={() => navigate(`/info/${lesson.result.userInfo.id}`)}
                    style={{ cursor: "pointer" }}
                  />
                ) : (
                  <div
                    className="avatar-placeholder"
                    onClick={() => navigate(`/info/${lesson.result.userInfo.id}`)}
                    style={{ cursor: "pointer" }}
                  >
                    {lesson.result.userInfo.name?.charAt(0).toUpperCase()}
                  </div>
                )}
                <span
                  className="name"
                  onClick={() => navigate(`/info/${lesson.result.userInfo.id}`)}
                  style={{ cursor: "pointer", marginLeft: "8px" }}
                >
                  {lesson.result.userInfo.name}
                </span>
                <span className="time">
                  {moment(lesson.result.createdAt).fromNow()}
                </span>
              </div>


              <button className="bookmark-btn">
                <FaBookmark />
              </button>
            </div>
            <div className="header-bottom">
              <button
                onClick={() => setShowRating(true)}
                style={{
                  backgroundColor: "#a8e6cf", // xanh mint pastel
                  color: "#000", // chữ đen cho dễ đọc
                  padding: "8px 16px",
                  border: "none",
                  borderRadius: "6px",
                  cursor: "pointer",
                  fontWeight: "500",
                  marginRight: "8px"
                }}
              >
                Rate lesson
              </button>

              <button
                onClick={() => setShowSchedule(true)}
                style={{
                  backgroundColor: "#ffd3b6", // hồng cam pastel
                  color: "#000",
                  padding: "8px 16px",
                  border: "none",
                  borderRadius: "6px",
                  cursor: "pointer",
                  fontWeight: "500"
                }}
              >
                Schedule lesson
              </button>

              {showRating && (
                <Rating
                  lessonId={lessonId}
                  onClose={() => setShowRating(false)}
                  onRated={(newStar) => {
                    setRating(prev => ({
                      ...prev,
                      totalUser: prev?.totalUser ? prev.totalUser + 1 : 1,
                      averageRating:
                        prev?.totalUser
                          ? ((prev.averageRating * prev.totalUser) + newStar) / (prev.totalUser + 1)
                          : newStar
                    }));
                  }}
                />
              )}

              {showSchedule && <Schedule lessonId={lessonId} onClose={() => setShowSchedule(false)} />}
            </div>
          </div>
        </div>
      </div>
      <div className="function-buttons">
        <button onClick={() => user ? handleToQuiz() : handleRequireLogin()}>
          <FaQuestionCircle style={{ marginRight: "8px" }} />
          Quiz Study
        </button>
        <button onClick={() => user ? handleToTest() : handleRequireLogin()}>
          <FaPencilAlt style={{ marginRight: "8px" }} />
          Test Study
        </button>
        <button onClick={() => user ? handleToFlashcard() : handleRequireLogin()}>
          <FaClone style={{ marginRight: "8px" }} />
          Flashcards
        </button>
      </div>

      <div className="flashcard-section">
        <Flashcard flashcards={lesson.result.flashcardSet} user={user} onRequireLogin={handleRequireLogin} />
      </div>
      <div className="comment-section">
        <Comment
          lessonId={lessonId}
          comments={comments}
          setComments={setComments}
          user={user}
          onRequireLogin={handleRequireLogin}
        />
      </div>



    </div>
  );

};

export default LessonDetail;
