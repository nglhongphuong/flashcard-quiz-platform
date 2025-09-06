import { useNavigate } from "react-router-dom";
import { FaMagic, FaEdit } from "react-icons/fa";
import "./CreateLessonHome.css"; // import file css riêng

export default function CreateLessonHome() {
  const navigate = useNavigate();

  return (
    <div className="create-lesson-home-container">
      <h1 className="create-lesson-home-title">Create Lesson</h1>
      <p className="create-lesson-home-subtitle">
        Choose your style to start creating flashcards
      </p>
      <div className="create-lesson-home-grid">
        <div
          className="create-lesson-home-card gemini"
          onClick={() => navigate("/gemini-topic")}
        >
          <FaMagic className="create-lesson-home-icon gemini" />
          <h2 className="create-lesson-home-card-title">Create with Gemini</h2>
          <p className="create-lesson-home-card-text">
            Powered by Gemini 2.5 (preview)
            A test version with some restrictions — but already smart enough to help you create awesome flashcards
          </p>
          <button className="create-lesson-home-btn gemini">
            Start with Gemini
          </button>
        </div>
        <div
          className="create-lesson-home-card manual"
          onClick={() => navigate("/create-lesson")}
        >
          <FaEdit className="create-lesson-home-icon manual" />
          <h2 className="create-lesson-home-card-title">Create Manually</h2>
          <p className="create-lesson-home-card-text">
            Build your own flashcards step by step, customize every detail, and keep only what truly matters.
          </p>
          <button className="create-lesson-home-btn manual">
            Start Manually
          </button>
        </div>
      </div>
    </div>
  );
}
