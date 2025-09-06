import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { authApis, endpoints } from "../../../configs/Apis";
import { Pie } from "react-chartjs-2";
import { Chart as ChartJS, ArcElement, Tooltip, Legend } from "chart.js";
import MySpinner from "../../layouts/MySpinner";
import "./QuizStudyResult.css";

ChartJS.register(ArcElement, Tooltip, Legend);

const QuizStudyResult = () => {
  const { lessonId, quizStudyId } = useParams();
  const navigate = useNavigate();
  const [loading, setLoading] = useState(true);
  const [result, setResult] = useState(null);

  useEffect(() => {
    const loadResult = async () => {
      try {
        const res = await authApis().get(
          endpoints.quiz_study_detail(lessonId, quizStudyId)
        );
        setResult(res.data.result);
      } catch (err) {
        console.error(err);
      } finally {
        setLoading(false);
      }
    };
    loadResult();
  }, [lessonId, quizStudyId]);

  if (loading) return <MySpinner />;
  if (!result) return <p>Không tìm thấy kết quả.</p>;

  const correctCount = result.quizhistorySet.filter(q => q.result === "CORRECT").length;
  const incorrectCount = result.quizhistorySet.filter(q => q.result === "INCORRECT").length;
  const unresultCount = result.quizhistorySet.filter(q => q.result === "UNRESULT").length;

  const chartData = {
    labels: ["Correct", "Incorrect", "Unanswered"],
    datasets: [
      {
        data: [correctCount, incorrectCount, unresultCount],
        backgroundColor: ["#4CAF50", "#F44336", "#9E9E9E"],
        hoverOffset: 10,
      },
    ],
  };

  return (
    <div className="quiz-result-container">
      <h2>Quiz Result</h2>
      <div className="quiz-result-content">
        {/* Biểu đồ bên trái */}
        <div className="chart-box">
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
        <div className="stats-box">
          <p><strong>Correct:</strong> {correctCount}</p>
          <p><strong>Incorrect:</strong> {incorrectCount}</p>
          <p><strong>Unanswered:</strong> {unresultCount}</p>
          <p><strong>Total:</strong> {result.quizhistorySet.length}</p>
        </div>
      </div>

      <div className="quiz-result-actions">
        <button onClick={() => navigate(`/lesson/${lessonId}/quiz-study/${quizStudyId}`)}>
          Restart
        </button>
        <button onClick={() => navigate(`/lesson/${lessonId}/quiz-study`)}>Exit</button>
      </div>
    </div>
  );
};

export default QuizStudyResult;
