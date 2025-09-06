import "./UsernameInfo.css";
import React, { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import moment from "moment";
import "moment/locale/vi";
import Apis, { endpoints } from "../../configs/Apis";
import MySpinner from "../layouts/MySpinner";
import LessonCard from "../LessonDetail/LessonCard";

const UsernameInfo = () => {
  const { userId } = useParams();
  const [lessons, setLessons] = useState([]);
  const [loading, setLoading] = useState(false);

  const loadLessons = async () => {
    setLoading(true);
    try {
      const params = new URLSearchParams();
      params.append("userId", userId);
      const res = await Apis.get(`${endpoints.lesson}?${params.toString()}`);
      console.log("new lessons: ", res.data);
      setLessons(res.data);
    } catch (err) {
      console.error("Error fetching lessons", err);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadLessons();
  }, [userId]);

  if (loading) return <MySpinner text="Loading lessons..." />;

  if (!lessons || lessons.length === 0) {
    return <p style={{ textAlign: "center" }}>This user has no lessons yet.</p>;
  }

  // Lấy userInfo từ lesson đầu tiên
  const userInfo = lessons[0]?.userInfo;

  return (
    <div className="usernameinfo-container">
      {/* Cover + Thông tin người dùng */}
      {userInfo && (
        <div className="usernameinfo-header">
          <div className="usernameinfo-cover"></div>
          <div className="usernameinfo-info">
            <img
              src={userInfo.avatar}
              alt={userInfo.name}
              className="usernameinfo-avatar"
            />
            <h2 className="usernameinfo-name">{userInfo.name}</h2>
            <p className="usernameinfo-username">@{userInfo.username}</p>
            <div className="usernameinfo-details">
              {userInfo.gender && <p>Gender: {userInfo.gender}</p>}
              {userInfo.dateOfBirth && (
                <p>
                  Birthday: {moment(userInfo.dateOfBirth).format("LL")}
                </p>
              )}
              {userInfo.role && <p>Role: {userInfo.role}</p>}
            </div>
          </div>
        </div>
      )}

      {/* Danh sách bài học */}
      <div className="usernameinfo-lessons">
        <h3>Lessons</h3>
        {lessons
          .filter((l) => l.visibility !== "PRIVATE")
          .map((lesson) => (
            <LessonCard key={lesson.id} lesson={lesson} />
          ))}
      </div>
    </div>
  );
};

export default UsernameInfo;
