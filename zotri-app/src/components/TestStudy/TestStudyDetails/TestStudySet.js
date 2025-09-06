import "./TestStudySet.css";
import React from "react";
import { FaTrash } from "react-icons/fa";
import { useNavigate, useParams } from "react-router-dom";
import moment from "moment";
import { Pie } from "react-chartjs-2";
import { authApis, endpoints } from "../../../configs/Apis";
import "chart.js/auto";

const TestStudySet = React.forwardRef(({ quizstudy_father, onDelete }, ref) => {
    const nav = useNavigate();
    const { lessonId } = useParams();

    const handleDeleteClick = async () => {
        try {
            await authApis().delete(endpoints.quiz_study_detail(lessonId, quizstudy_father.id));
            onDelete(quizstudy_father.id);
            console.log("Deleted Successfully !!");
        } catch (error) {
            console.error("Error deleting quiz study:", error);
        }
    };
    const handleDoQuiz = () => {
        nav(`/lesson/${lessonId}/test-study/${quizstudy_father.id}`);
    };
    const correct = quizstudy_father.results?.CORRECT || 0;
    const incorrect = quizstudy_father.results?.INCORRECT || 0;
    const unresult = quizstudy_father.results?.UNRESULT || 0;

    const chartData = {
        labels: ["Correct", "Incorrect", "Unresult"],
        datasets: [
            {
                data: [correct, incorrect, unresult],
                backgroundColor: ["#ebe68bff", "#cb6a63ff", "#68c6c6ff"],
                borderWidth: 1,
            },
        ],
    };

    return (
        <div className="test-study-card" ref={ref} >
            {/* Header - Phần 1 */}
            <div className="test-study-header">
                <div className="created-date">Created: {moment(quizstudy_father.createdAt).format("YYYY-MM-DD HH:mm")}</div>
                <button className="test-study-delete-btn" onClick={handleDeleteClick}>
                    <FaTrash />
                </button>
            </div>

            {/* Body - Phần 2 */}
            <div className="test-study-body">
                <div className="test-study-chart">
                    <Pie data={chartData}
                        options={{
                            animation: {
                                duration: 0,          // tắt animation
                            },
                            hover: {
                                animationDuration: 0, // tắt animation khi hover
                            },
                            maintainAspectRatio: false,
                            responsive: true,
                        }} />
                </div>

                <div className="test-study-stats">
                    <h3 className="test-study-title">Test Study #{quizstudy_father.id}</h3>
                    <p>
                        Results: <br />
                        Correct: {correct} <br />
                        Incorrect: {incorrect} <br />
                        Unanswered: {unresult} <br />
                        Time limit: {quizstudy_father.teststudy?.min || 0} minutes
                    </p>
                </div>
            </div>

            {/* Footer - Phần 3 */}
            <div className="test-study-footer">
                <button className="test-study-do-btn" onClick={handleDoQuiz}>
                    Do Test
                </button>
            </div>
        </div>
    );
});

export default TestStudySet;