import "./TestStudy.css";
import MySpinner from "../layouts/MySpinner";
import React, { useEffect, useState, useRef, useCallback } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { FaArrowLeft, FaPlus } from "react-icons/fa";
import { authApis, endpoints } from "../../configs/Apis";
import TestStudyForm from "./TestStudyDetails/TestStudyForm";
import TestStudySet from "./TestStudyDetails/TestStudySet";

const TestStudy = () => {
    const { lessonId } = useParams();
    const nav = useNavigate();
    const observer = useRef();
    const [showForm, setShowForm] = useState(false);
    const [quizList, setQuizList] = useState([]);
    const [loading, setLoading] = useState(false);
    const [page, setPage] = useState(1);
    const [hasMore, setHasMore] = useState(true);

    const [filters, setFilters] = useState({
        startDate: null,
        endDate: null,
        order: "desc",
    });

    const lastTestQuizRef = useCallback(
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

    useEffect(() => {
        const loadQuizList = async () => {
            setLoading(true);
            try {
                const params = new URLSearchParams();
                params.append("page", page);
                params.append("order", filters.order);
                if (filters.startDate) params.append("startDate", filters.startDate);
                if (filters.endDate) params.append("endDate", filters.endDate);

                const res = await authApis().get(
                    `${endpoints.test_study(lessonId)}?${params.toString()}`
                );

                console.log("res data: " , res.data.result);

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
                    Create Test Study
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
                <TestStudyForm
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
                            <div ref={lastTestQuizRef} key={quizstudy.id}>
                                <TestStudySet quizstudy_father={quizstudy} onDelete={handleDelete} />
                            </div>
                        );
                    }
                    return (
                        <TestStudySet key={quizstudy.id} quizstudy_father={quizstudy} onDelete={handleDelete} />
                    );
                })}
            </div>

            {loading && <MySpinner />}
        </div>
    );

};

export default TestStudy;