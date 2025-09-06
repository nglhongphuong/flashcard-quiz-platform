import "./RecentViewSet.css";
import { useEffect, useState, useRef, useCallback } from "react";
import { ClipLoader } from "react-spinners";
import LessonCard from "../../LessonDetail/LessonCard";
import { authApis, endpoints } from "../../../configs/Apis";
import { useNavigate } from "react-router-dom";

const PAGE_SIZE = 5; // giả định backend trả mỗi trang 5 phần tử

const RecentViewSet = () => {
  const [lessons, setLessons] = useState([]);
  const [loading, setLoading] = useState(false);  // ban đầu false, set true khi load
  const [page, setPage] = useState(1);
  const [hasMore, setHasMore] = useState(true);
  const navigate = useNavigate();

  const observer = useRef();
  const lastLessonRef = useCallback(
    (node) => {
      if (loading) return; // nếu đang load thì không làm gì
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

  useEffect(() => {
    const loadRecentLessons = async () => {
      setLoading(true);
      try {
        const params = new URLSearchParams();
        params.append("page", page);
        const res = await authApis().get(`${endpoints.view_history}?${params.toString()}`);
        const newLessons = res.data?.result || [];

        // Nếu ít hơn PAGE_SIZE thì không còn trang nữa
        if (newLessons.length < PAGE_SIZE) setHasMore(false);

        // Cộng dồn lessons, page 1 thì reset lại luôn
        setLessons((prev) => (page === 1 ? newLessons : [...prev, ...newLessons]));
      } catch (err) {
        console.error("Lỗi khi tải danh sách lesson gần đây:", err);
      } finally {
        setLoading(false);
      }
    };

    loadRecentLessons();
  }, [page]);

  return (
    <div className="recent-viewset-section">
      <div className="recent-viewset-header">
        <h2 className="section-title">Your Recently Studied Lessons</h2>
        <button className="back-button-flashcard-study" onClick={() => navigate("/")}>
          Back to Home
        </button>
      </div>

      {lessons.length === 0 && !loading ? (
        <p className="empty-message">You haven't studied any lessons recently.</p>
      ) : (
        <div className="recent-lesson-grid-two-columns">
          {lessons.map((lesson, index) => {
            if (index === lessons.length - 1) {
              return <LessonCard key={lesson.id} lesson={lesson} ref={lastLessonRef} />;
            }
            return <LessonCard key={lesson.id} lesson={lesson} />;
          })}
        </div>
      )}

      {loading && (
        <div className="loading-container" style={{ marginTop: 20 }}>
          <ClipLoader color="#6cc9cf" size={50} />
        </div>
      )}
    </div>
  );
};

export default RecentViewSet;
