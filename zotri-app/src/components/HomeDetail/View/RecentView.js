import { useEffect, useState } from "react";
import { ClipLoader } from "react-spinners";
import LessonCard from "../../LessonDetail/LessonCard";
import { authApis, endpoints } from "../../../configs/Apis";
import { Link, useNavigate } from "react-router-dom";
import "./RecentView.css";

const RecentView = () => {
    const [lessons, setLessons] = useState([]);
    const [loading, setLoading] = useState(true);
      const nav = useNavigate();

    useEffect(() => {
        const loadAllRecentLessons = async () => {
            try {
                let page = 1;
                let allLessons = [];
                let hasMore = true;

                while (hasMore) {
                    const res = await authApis().get(`${endpoints.view_history}?page=${page}`);
                    const result = res.data?.result || [];

                    if (result.length > 0) {
                        allLessons = [...allLessons, ...result];
                        page++;
                    } else {
                        hasMore = false;
                    }
                }
                const sorted = [...allLessons].sort((a, b) => {
                    const dateA = a.viewhistoryResponse?.updateAt ? new Date(a.viewhistoryResponse.updateAt) : new Date(0);
                    const dateB = b.viewhistoryResponse?.updateAt ? new Date(b.viewhistoryResponse.updateAt) : new Date(0);
                    return dateB - dateA;
                });

                setLessons(sorted.slice(0, 4));
            } catch (err) {
                console.error("Error load view history", err);
            } finally {
                setLoading(false);
            }
        };

        loadAllRecentLessons();
    }, []);

    return (
        <div className="recent-view-section">
            <div className="recent-title" style={{ display: "flex", justifyContent: "space-between", alignItems: "center", marginBottom: "12px" }}>
                <h2 className="section-title">Your Recently Studied Lessons</h2>
                <button
                    className="recent-view-btn"
                    onClick={() => nav("/view-history/set")}
                    style={{
                        backgroundColor: "#6cc9cf",  // pastel mint
                        color: "#000",
                        borderRadius: "8px",
                        padding: "6px 16px",
                        border: "none",
                        cursor: "pointer",
                        fontWeight: "600",
                        boxShadow: "0 2px 5px rgba(0,0,0,0.1)",
                        transition: "background-color 0.3s ease"
                    }}
                    onMouseEnter={e => e.currentTarget.style.backgroundColor = "#5bb0b9"}
                    onMouseLeave={e => e.currentTarget.style.backgroundColor = "#6cc9cf"}
                >
                    View Details
                </button>
            </div>


            {loading ? (
                <div className="loading-container">
                    <ClipLoader color="#6cc9cf" size={50} />
                </div>
            ) : lessons.length === 0 ? (
                <p className="empty-message">You haven't studied any lessons recently..</p>
            ) : (
                <div className="recent-lesson-grid two-rows">
                    {lessons.map((lesson) => (
                        <LessonCard key={lesson.id} lesson={lesson} />
                    ))}
                </div>
            )}
        </div>
    );
};

export default RecentView;
