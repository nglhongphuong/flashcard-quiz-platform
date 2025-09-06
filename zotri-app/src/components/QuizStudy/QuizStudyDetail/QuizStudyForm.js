import React, { useState, useEffect } from "react";
import { authApis, endpoints } from "../../../configs/Apis";
import MySpinner from "../../layouts/MySpinner";
import { useNavigate } from "react-router-dom";
import "./QuizStudyForm.css";


const QuizStudyForm = ({ lessonId, onClose, onCreated }) => {
  const [questionType, setQuestionType] = useState("TEXT_AUDIO");
  const [answerTypes, setAnswerTypes] = useState([]);
  const [studyMode, setStudyMode] = useState("RANDOM");
  const [numberOfFlashcards, setNumberOfFlashcards] = useState(3);
  const [flashcardIds, setFlashcardIds] = useState([]);
  const [availableFlashcards, setAvailableFlashcards] = useState([]);
  const [loading, setLoading] = useState(false);
  const [msg, setMsg] = useState(null);

  const nav = useNavigate();

  useEffect(() => {
    const loadFlashcards = async () => {
      if (studyMode !== "CUSTOM") return;
      try {
        setLoading(true);
        let res = await authApis().get(endpoints.lesson_detail(lessonId));
        setAvailableFlashcards(res.data.result.flashcardSet || []);
      } catch (err) {
        setMsg("Error Loading Flashcards!");
        console.error(err);
      } finally {
        setLoading(false);
      }
    };

    loadFlashcards();
  }, [studyMode, lessonId]);

  const toggleAnswerType = (type) => {
    setAnswerTypes((prev) =>
      prev.includes(type) ? prev.filter((t) => t !== type) : [...prev, type]
    );
  };

  const toggleFlashcard = (id) => {
    setFlashcardIds((prev) =>
      prev.includes(id) ? prev.filter((fid) => fid !== id) : [...prev, id]
    );
  };

  const handleSubmit = async () => {
    const payload = {
      questionType,
      answerTypes,
      studyMode,
    };
    if (studyMode === "RANDOM") payload.numberOfFlashcards = Number(numberOfFlashcards);
    if (studyMode === "CUSTOM") payload.flashcardIds = flashcardIds;

    try {
      setLoading(true);
      const res = await authApis().post(endpoints.quiz_study(lessonId), payload);
      setMsg("Create Successfully !!");
      console.log("res : ", res);
      //Gọi lại api chi tiết của quiz-study vừa tạo để có result lun 
      const haha = await authApis().get(
        endpoints.quiz_study_detail(lessonId, res.data.result.id));
      onCreated?.(haha.data.result);
      onClose?.();
      nav(`/lesson/${lessonId}/quiz-study/${res.data.result.id}`);
    } catch (err) {
      if (err.response && err.response.data && err.response.data.message) {
        setMsg(err.response.data.message); // In message từ backend
      } else {
        setMsg("Create failed. Please try again.");
      }
    } finally {
      setLoading(false);
    }
  };

  if (loading) return <MySpinner />;

  return (
    <div className="quiz-form-container">
      <h2>Create New Quiz Study</h2>
      {msg && <p>{msg}</p>}

      <label>Question Type:</label>
      <select value={questionType} onChange={(e) => setQuestionType(e.target.value)}>
        <option value="TEXT_AUDIO">Text and Audio</option>
        <option value="AUDIO">Only Audio</option>
      </select>

      <label>Answer Type:</label>
      <div className="checkbox-group">
        {["TEXT_INPUT", "TRUE_FALSE", "MULTIPLE_CHOICE"].map((type) => (
          <label key={type}>
            <input
              type="checkbox"
              checked={answerTypes.includes(type)}
              onChange={() => toggleAnswerType(type)}
            />
            {type.replaceAll("_", " ")}
          </label>
        ))}
      </div>

      <label>Study Mode:</label>
      <select value={studyMode} onChange={(e) => setStudyMode(e.target.value)}>
        <option value="RANDOM">Random</option>
        <option value="CUSTOM">Custom</option>
        <option value="REMEMBERED">Remembered</option>
        <option value="NOT_REMEMBERED">Not Remembered</option>
        <option value="NOT_LEARNED">Not Learned</option>
      </select>

      {studyMode === "RANDOM" && (
        <>
          <label>Number of flashcards: </label>
          <input
            type="number"
            min={1}
            value={numberOfFlashcards}
            onChange={(e) => setNumberOfFlashcards(e.target.value)}
          />
        </>
      )}

      {studyMode === "CUSTOM" && (
        <div className="custom-flashcards">
          <p>Select flashcards to study</p>
          {availableFlashcards.map((fc) => (
            <label key={fc.id} className="flashcard-checkbox">
              <input
                type="checkbox"
                checked={flashcardIds.includes(fc.id)}
                onChange={() => toggleFlashcard(fc.id)}
              />
              <img src={fc.image} alt={fc.word} />
              <strong>{fc.word}</strong>: {fc.definition}
            </label>
          ))}
        </div>
      )}

      <div style={{ marginTop: 20 }}>
        <button onClick={handleSubmit}>Submit</button>
        <button onClick={onClose}>Cancel</button>
      </div>
    </div>
  );
};

export default QuizStudyForm;