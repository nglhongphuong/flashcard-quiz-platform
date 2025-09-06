import "./TestStudyForm.css";
import React, { useState } from "react";
import { authApis, endpoints } from "../../../configs/Apis";
import MySpinner from "../../layouts/MySpinner";
import { useNavigate } from "react-router-dom";

const TestStudyForm = ({ lessonId, onClose }) => {
  const [loading, setLoading] = useState(false);
  const [min, setMin] = useState(5);
  const [questionType, setQuestionType] = useState("TEXT_AUDIO");
  const [answerTypes, setAnswerTypes] = useState(["MULTIPLE_CHOICE"]);
  const [numberOfFlashcards, setNumberOfFlashcards] = useState(3);
  const [msg, setMsg] = useState(null);
  const nav = useNavigate();

  const toggleAnswerType = (type) => {
    setAnswerTypes((prev) =>
      prev.includes(type) ? prev.filter((t) => t !== type) : [...prev, type]
    );
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setMsg(null);
    try {
      const payload = {
        min: parseInt(min),
        quizstudy: {
          questionType,
          answerTypes,
          studyMode: "RANDOM",
          numberOfFlashcards: parseInt(numberOfFlashcards),
        },
      };

      const res = await authApis().post(endpoints.test_study(lessonId), payload);
      const quizStudyId = res.data?.result?.id;
      nav(`/lesson/${lessonId}/test-study/${quizStudyId}`);
    } catch (err) {
      console.error(err);
      setMsg("Failed to create test. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="teststudy-form-overlay">
      <div className="teststudy-form">
        <h2>Create Test Study</h2>
        {msg && <div className="error-msg">{msg}</div>}
        {loading ? (
          <MySpinner />
        ) : (
          <form onSubmit={handleSubmit}>
            <label>
              Time limit (minutes):
              <input
                type="number"
                value={min}
                min={1}
                onChange={(e) => setMin(e.target.value)}
              />
            </label>

            <label>
              Question type:
              <select
                value={questionType}
                onChange={(e) => setQuestionType(e.target.value)}
              >
                <option value="TEXT_AUDIO">TEXT_AUDIO</option>
                <option value="AUDIO">AUDIO</option>
              </select>
            </label>

            <fieldset>
              <legend>Answer types:</legend>
              {["MULTIPLE_CHOICE", "TRUE_FALSE", "TEXT_INPUT"].map((type) => (
                <label key={type} className="checkbox-label">
                  <input
                    type="checkbox"
                    checked={answerTypes.includes(type)}
                    onChange={() => toggleAnswerType(type)}
                  />
                  {type}
                </label>
              ))}
            </fieldset>

            <label>
              Number of flashcards:
              <input
                type="number"
                value={numberOfFlashcards}
                min={1}
                onChange={(e) => setNumberOfFlashcards(e.target.value)}
              />
            </label>

            <div className="form-buttons">
              <button type="submit" className="create-btn">Create Test</button>
              <button type="button" className="cancel-btn" onClick={onClose}>
                Cancel
              </button>
            </div>
          </form>
        )}
      </div>
    </div>
  );
};

export default TestStudyForm;
