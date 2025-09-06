import React from "react";
import { FaTrash } from "react-icons/fa";
import { useNavigate, useParams } from "react-router-dom";
import moment from "moment";
import { Pie } from "react-chartjs-2";
import { authApis, endpoints } from "../../../configs/Apis";
import "chart.js/auto";
import "./QuizStudySet.css";

const QuizStudySet = React.forwardRef(({ quizstudy_father, onDelete }, ref) => {
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
    nav(`/lesson/${lessonId}/quiz-study/${quizstudy_father.id}`);
  };

  const correct = quizstudy_father.results?.CORRECT || 0;
  const incorrect = quizstudy_father.results?.INCORRECT || 0;
  const unresult = quizstudy_father.results?.UNRESULT || 0;

  const chartData = {
    labels: ["Correct", "Incorrect", "Unresult"],
    datasets: [
      {
        data: [correct, incorrect, unresult],
        backgroundColor: ["#4caf50", "#f44336", "#ff9800"],
        borderWidth: 1,
      },
    ],
  };

  return (
    <div className="quizstudy-card" ref={ref} >
      {/* Header - Phần 1 */}
      <div className="quizstudy-header">
        <div className="created-date">Created: {moment(quizstudy_father.createdAt).format("YYYY-MM-DD HH:mm")}</div>
        <button className="quizstudy-delete-btn" onClick={handleDeleteClick}>
          <FaTrash />
        </button>
      </div>

      {/* Body - Phần 2 */}
      <div className="quizstudy-body">
        <div className="quizstudy-chart">
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

        <div className="quizstudy-stats">
          <h3 className="quizstudy-title">Quiz Study #{quizstudy_father.id}</h3>
          {/* <p>Type: {quizstudy_father.questionType}</p>
          <p>Answers: {quizstudy_father.answerTypes?.join(", ")}</p>
          <p>Mode: {quizstudy_father.studyMode}</p> */}
          <p>
            Results: <br />
            Correct: {correct} <br />
            Incorrect: {incorrect} <br />
            Unresult: {unresult}
          </p>
        </div>
      </div>

      {/* Footer - Phần 3 */}
      <div className="quizstudy-footer">
        <button className="quizstudy-do-btn" onClick={handleDoQuiz}>
          Do Quiz
        </button>
      </div>
    </div>
  );
});

export default QuizStudySet;
