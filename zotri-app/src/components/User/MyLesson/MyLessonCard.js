import { useEffect, useState } from "react"; 
import { useNavigate } from "react-router-dom";
import "./MyLessonCard.css";
import moment from "moment";
import "moment/locale/vi";
import { FaStar, FaStarHalfAlt, FaRegStar, FaTrash } from "react-icons/fa";
import { BsBarChart } from "react-icons/bs";
import Apis, { authApis, endpoints } from "../../../configs/Apis";
import React from 'react';
moment.locale("vi");

const LessonCard = React.forwardRef(({ lesson_father, onDelete }, ref) => {
  const [rating, setRating] = useState(null);
  const [lesson, setLesson] = useState(lesson_father);
  const navigate = useNavigate();

  const handleClick = () => {
    navigate(`${lesson.id}`);
  };

  const handleDelete = async (e) => {
    e.stopPropagation(); // ngăn không cho click nút xóa kích hoạt handleClick điều hướng
    if (window.confirm(`Are you sure you want to delete lesson "${lesson.title}"?`)) {
      try {
        onDelete?.(lesson.id);
        await authApis().delete(endpoints.lesson_update(lesson.id));
      } catch (err) {
        console.error("Failed to delete lesson:", err);
        alert("Failed to delete lesson.");
      }
    }
  };

  useEffect(() => {
    const fetchRating = async () => {
      try {
        const res = await Apis.get(endpoints.rating(lesson.id));
        setRating(res.data.result);
      } catch (err) {
        console.error("Lỗi khi lấy rating:", err);
      }
    };

    fetchRating();
  }, [lesson.id]);

  return (
    <div className="lesson-card" ref={ref} onClick={handleClick} style={{ position: "relative" }}>
      {/* Nút xóa góc trên phải */}
      <button
        className="lesson-delete-btn"
        onClick={handleDelete}
        title="Delete Lesson"
        aria-label="Delete lesson"
      >
        <FaTrash />
      </button>

      <div className="lesson-header-card">
        <div className="avatar">
          {lesson.userInfo.avatar ? (
            <img src={lesson.userInfo.avatar} alt="avatar" />
          ) : (
            <div className="avatar-placeholder">
              {lesson.userInfo.name?.charAt(0).toUpperCase()}
            </div>
          )}
        </div>
        <div className="info">
          <span className="name">{lesson.userInfo.name}</span>
          <span className="time">
            {moment(lesson.createdAt).fromNow()}
          </span>
        </div>
      </div>

      {lesson.image && (
        <div className="lesson-image">
          <img src={lesson.image} alt="lesson" />
        </div>
      )}
      <div className="lesson-content">
        <div className="title-rating">
          <h3 className="lesson-title">{lesson.title}</h3>
          {rating && (
            <div className="lesson-rating">
              {[1, 2, 3, 4, 5].map((i) => {
                if (rating.averageRating >= i) {
                  return <FaStar key={i} className="star" />;
                } else if (rating.averageRating >= i - 0.5) {
                  return <FaStarHalfAlt key={i} className="star" />;
                } else {
                  return <FaRegStar key={i} className="star" />;
                }
              })}
              <span className="score">/5</span>
              <BsBarChart style={{ margin: "0 4px" }} />
              <span>Studied by {rating.totalUser}</span>
            </div>
          )}
        </div>

        <p>{lesson.description}</p>
      </div>
    </div>
  );
});

export default LessonCard;
