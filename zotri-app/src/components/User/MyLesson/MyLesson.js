import { useEffect, useState, useContext , useRef, useCallback } from "react";
import Apis, { endpoints } from "../../../configs/Apis";
import MyLessonCard from "./MyLessonCard";
import MySpinner from "../../layouts/MySpinner";
import "./MyLesson.css";
import { FaSearch } from "react-icons/fa"
import { MyUserContext, MyDispatcherContext } from "../../../configs/MyContexts";

const MyLesson = () => {
    const user = useContext(MyUserContext);
  const [lessons, setLessons] = useState([]);
  const [loading, setLoading] = useState(false);
  const [page, setPage] = useState(1);
  const [hasMore, setHasMore] = useState(true);
  const [search, setSearch] = useState("");
  const [startDate, setStartDate] = useState("");
  const [endDate, setEndDate] = useState("");
  const [sortOrder, setSortOrder] = useState("desc");
  const observer = useRef();
  const lastLessonRef = useCallback(
    (node) => {
      if (loading) return;
      if (observer.current) observer.current.disconnect();
      observer.current = new IntersectionObserver(
        (entries) => {
          if (entries[0].isIntersecting && hasMore) {
            console.log("tăng page");
            setPage((prev) => prev + 1);
          }
        },
        {
          threshold: 0.1,        // chỉ cần 10% lọt vào là kích hoạt
          rootMargin: "100px",   // bắt sớm hơn 100px trước khi thật sự chạm
        }
      );
      if (node) observer.current.observe(node);
    },
    [loading, hasMore]
  );
  useEffect(() => {
    const loadLessons = async () => {
      setLoading(true);
      try {
        const params = new URLSearchParams();
        params.append("page", page);
        if (search) params.append("search", search); //Backend chưa xử lý "title"
        if (startDate) params.append("startDate", startDate);
        if (endDate) params.append("endDate", endDate);
        params.append("order", sortOrder);
        params.append("userId", user?.result?.id);  //đảm bảo có user mới append
        const res = await Apis.get(`${endpoints.lesson}?${params.toString()}`);
        const newLessons = res.data;
        if (newLessons.length < 5) setHasMore(false);
        setLessons((prev) => [...prev, ...newLessons]);
      } catch (err) {
        console.error("Error fetching lessons", err);
      } finally {
        setLoading(false);
      }
    };
    loadLessons();
  }, [page, search, startDate, endDate, sortOrder]);

  const handleDeleteLesson = async (deletedId) => {
  setLessons((prev) => prev.filter((l) => l.id !== deletedId));
    console.log("Đã cập nhập!!! deleted Id ", deletedId);
};

  return (
    <div className="library-wrapper">
      <div className="library-header">
        <h2>My Lesson Library</h2>
        <p className="library-description">
          Discover a variety of lessons shared by different authors across multiple social media platforms. Browse, search, and filter to find the content that inspires you.
        </p>
      </div>

      <div className="sidebar-filter">
        <div className="filter-row">
          <div className="search-box">
            <FaSearch className="search-icon" />
            <input
              type="text"
              placeholder="Title or description"
              value={search}
              onChange={(e) => {
                setLessons([]);
                setPage(1);
                setHasMore(true);
                setSearch(e.target.value);
              }}
            />
          </div>

          <input
            type="date"
            value={startDate}
            onChange={(e) => {
              setLessons([]);
              setPage(1);
              setHasMore(true);
              setStartDate(e.target.value);
            }}
          />

          <input
            type="date"
            value={endDate}
            onChange={(e) => {
              setLessons([]);
              setPage(1);
              setHasMore(true);
              setEndDate(e.target.value);
            }}
          />

          <select
            value={sortOrder}
            onChange={(e) => {
              setLessons([]);
              setPage(1);
              setHasMore(true);
              setSortOrder(e.target.value);
            }}
          >
            <option value="desc">Mới nhất</option>
            <option value="asc">Cũ nhất</option>
          </select>
        </div>
      </div>
      <div className="lesson-list">
        {lessons.map((lesson, index) => {
          if (index === lessons.length - 1) {
            return <MyLessonCard key={lesson.id} lesson_father={lesson} ref={lastLessonRef}    onDelete={handleDeleteLesson}  />;
          } else {
            return <MyLessonCard key={lesson.id} lesson_father={lesson}     onDelete={handleDeleteLesson} />;
          }
        })}
        {loading && <MySpinner />}
      </div>
    </div>
  );
};

export default MyLesson;
