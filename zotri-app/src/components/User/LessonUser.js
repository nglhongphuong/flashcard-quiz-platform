import React, { useEffect, useState, useContext } from "react";
import { useParams, useNavigate } from "react-router-dom";
import moment from "moment";
import "moment/locale/vi";
import { FaEdit, FaQuestionCircle, FaPencilAlt, FaClone, FaStar, FaStarHalfAlt, FaRegStar, FaBookmark } from "react-icons/fa";
import { BsBarChart } from "react-icons/bs";
import Apis, { authApis, endpoints } from "../../configs/Apis";
import { MyUserContext, MyDispatcherContext } from "../../configs/MyContexts";
import Flashcard from "../Flashcard/Flashcard";
import Comment from "../Comment/Comment"
import "./LessonUser.css";
import FlashcardSet from "./FlashcardSet";
import Schedule from "../Interaction/Schedule/Schedule";
import Rating from "../Interaction/Rating/Rating";
import MySpinner from "../layouts/MySpinner";

moment.locale("en");

const LessonUser = () => {
  const { lessonId } = useParams();
  const [lesson, setLesson] = useState(null);
  const [rating, setRating] = useState(null);
  const [comments, setComments] = useState([]);
  const user = useContext(MyUserContext);
  const navigate = useNavigate();

  const [showRating, setShowRating] = useState(false);
  const [showSchedule, setShowSchedule] = useState(false);

  ///EDIT -----------------------------
  const [isEditing, setIsEditing] = useState(false);
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [visibility, setVisibility] = useState("public");
  const [isCommentLocked, setIsCommentLocked] = useState(false);
  const [image, setImage] = useState(null);
  const [loading, setLoading] = useState(false);
  const [successMsg, setSuccessMsg] = useState("");
  const [error, setError] = useState("");
  const [imagePreview, setImagePreview] = useState("");



  //-----------------------------------

  useEffect(() => {
    const fetchData = async () => {
      try {
        const lessonRes = await Apis.get(endpoints.lesson_detail(lessonId));
        setLesson(lessonRes.data);

        //edit-----------------
        const result = lessonRes.data.result;
        setTitle(result.title);
        setDescription(result.description);
        setVisibility(result.visibility);
        setIsCommentLocked(result.isCommentLocked);

        //-------------------------

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

  useEffect(() => {
    if (successMsg) {
      const timer = setTimeout(() => {
        setSuccessMsg("");
      }, 4000); // 5 giây

      return () => clearTimeout(timer); // cleanup nếu component unmount
    }
  }, [successMsg]);


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




  const handleUpdateLesson = async () => {
    try {
      setLoading(true);
      const form = new FormData();
      form.append("title", title);
      form.append("description", description);
      form.append("visibility", visibility);
      form.append("isCommentLocked", isCommentLocked);
      if (image) form.append("image", image);
      const res = await authApis().put(endpoints.lesson_update(lessonId), form);
      if (res.data.code === 1000) {
        const updatedLesson = res.data.result;
        setLesson(prev => ({
          ...prev,
          result: updatedLesson,
        }));
        setSuccessMsg("Lesson updated successfully!");
        setIsEditing(false);
      } else {
        setError(res.data.message || "Unknown error occurred");
      }
    } catch (err) {
      setError("Update failed!");
    } finally {
      setLoading(false);
    }
  };

  const handleBackToMyLesson = () => {
    navigate(`/user/lesson/`);
  };



  if (!lesson) return <div>Đang tải dữ liệu...</div>;

  return (
    <div className="lesson-detail-container">
      <button className="back-button-lesson-user" onClick={handleBackToMyLesson}>
        Back to My Lesson
      </button>
      {isEditing ? (
        <div className="edit-form">
          {loading && <MySpinner />}
          {successMsg && <div className="success-msg">{successMsg}</div>}
          {error && <div className="error-msg">{error}</div>}

          <input
            type="text"
            value={title}
            onChange={(e) => setTitle(e.target.value)}
            placeholder="Lesson Title"
          />
          <textarea
            value={description}
            onChange={(e) => setDescription(e.target.value)}
            placeholder="Lesson Description"
          />
          <select value={visibility} onChange={(e) => setVisibility(e.target.value)}>
            <option value="public">Public</option>
            <option value="private">Private</option>
          </select>
          <label>
            <input
              type="checkbox"
              checked={isCommentLocked}
              onChange={(e) => setIsCommentLocked(e.target.checked)}
            />
            Lock Comments
          </label>
          <input
            type="file"
            accept="image/*"
            onChange={(e) => {
              const file = e.target.files[0];
              setImage(file);
              if (file) {
                const reader = new FileReader();
                reader.onloadend = () => {
                  setImagePreview(reader.result);
                };
                reader.readAsDataURL(file);
              }
            }}
          />
          {imagePreview && (
            <div className="image-preview">
              <img src={imagePreview} alt="Preview" style={{ maxWidth: "200px", marginTop: "10px" }} />
            </div>
          )}

          <div
            style={{
              display: "flex",
              justifyContent: "center",
              gap: "12px",
              marginTop: "10px",
            }}
          >
            <button
              onClick={handleUpdateLesson}
              style={{
                backgroundColor: "#a8e6cf", // xanh pastel
                color: "#000",
                padding: "8px 20px",
                border: "none",
                borderRadius: "6px",
                cursor: "pointer",
                fontWeight: "500",
              }}
            >
              Save
            </button>

            <button
              onClick={() => setIsEditing(false)}
              style={{
                backgroundColor: "#ff8b94", // đỏ pastel
                color: "#000",
                padding: "8px 20px",
                border: "none",
                borderRadius: "6px",
                cursor: "pointer",
                fontWeight: "500",
              }}
            >
              Cancel
            </button>
          </div>


        </div>
      ) : (
        <div className="lesson-header-lesson-user">
          {user && user.result.id === lesson.result.userInfo.id && (
            <button
              className="edit-icon-button"
              onClick={() => setIsEditing(true)}
              title="Edit Lesson"
            >
              <FaEdit />
            </button>
          )}
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

                <div className="author">
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
                      // update state immediately so UI updates
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
      )}

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

        <button
          onClick={() => navigate(`/lesson/${lessonId}/flashcard`)}
          className="flashcard-button-lesson-user"
        >
          <FaPencilAlt className="icon" />
          Create Flashcard
        </button>

        <div className="comment-section">
          <FlashcardSet flashcards_father={lesson.result.flashcardSet} />
        </div>

        {
          /*
          Hiện danh sách flashcardset gọi flashcardset.js qua
          Ảnh flashcard đường link nếu ko có hiện chữ cái đầu hoi =))), 
          Chữ định nghĩa ở trên , definition ở dưới, ảnh và (word-definition) nằm cùng 1 hàng và space betwwen, tương ứng mỗi dòng sẽ có 
          icon 3 chấm khi người dùng rê chuột sổ danh sách mini (NHư facebook á) hiện danh sách sửa, xóa flashcard Id
          */
        }
      </div>


    </div>
  );

};

export default LessonUser;
