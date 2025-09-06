import { useNavigate } from "react-router-dom";
import "./HomeIntro.css";

export default function HomeIntro() {
  const navigate = useNavigate();

  return (
    <div className="home-intro">
      <div className="home-intro-card">
        <h1 className="home-intro-title">ZotriVerse</h1>
        <p className="home-intro-text">
          Unlock a universe of learning with interactive flashcards, diverse study topics, 
          and creative tools that make every lesson engaging. 
          Track your progress, challenge yourself, and explore new ideas at your own pace.
        </p>
        <div className="home-intro-buttons">
          <button
            className="home-intro-btn"
            onClick={() => navigate("/login")}
          >
            Sign In
          </button>
          <button
            className="home-intro-btn secondary"
            onClick={() => navigate("/library")}
          >
            Explore Library
          </button>
        </div>
      </div>
    </div>
  );
}
