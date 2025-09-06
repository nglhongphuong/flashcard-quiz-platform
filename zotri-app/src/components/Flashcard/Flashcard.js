import React, { useState } from "react";
import { FaVolumeUp, FaChevronLeft, FaChevronRight } from "react-icons/fa";
import "./Flashcard.css";
import DetectLanguage from "../../configs/DetectLanguage";

const Flashcard = ({ flashcards = [], user, onRequireLogin }) => {
  const [currentIndex, setCurrentIndex] = useState(0);
  const [flipped, setFlipped] = useState(false);

  if (!flashcards || flashcards.length === 0) {
    return <div className="no-flashcard">No flashcards available</div>;
  }

  const current = flashcards[currentIndex];

  const handleFlip = () => setFlipped(!flipped);

  const handlePrev = () => {
    setCurrentIndex((prev) => (prev === 0 ? flashcards.length - 1 : prev - 1));
    setFlipped(false);
  };

  const handleNext = () => {
    setCurrentIndex((prev) => (prev === flashcards.length - 1 ? 0 : prev + 1));
    setFlipped(false);
  };

const handleSpeak = () => {
  const utterance = new SpeechSynthesisUtterance(current.word);
  const voices = window.speechSynthesis.getVoices();
  const lang = DetectLanguage(current.word);

  const selectedVoice = voices.find(voice => voice.lang.startsWith(lang));

  if (selectedVoice) {
    utterance.voice = selectedVoice;
  }

  speechSynthesis.speak(utterance);
};

  const getInitial = (word) => word?.charAt(0).toUpperCase() || "?";

  return (
    <div className="flashcard-container">
      <div className="flashcard" onClick={handleFlip}>
        <div className={`card ${flipped ? "flipped" : ""}`}>
          <div className="front">
            <div className="card-content">
              <div className="word-image-row">
                {current.image ? (
                  <img src={current.image} alt="flashcard" className="flashcard-image" />
                ) : (
                  <div className="flashcard-image">
                    {getInitial(current.word)}
                  </div>
                )}
                <h2 className="flashcard-word">{current.word}</h2>
              </div>
            </div>
          </div>
          <div className="back">
            <div className="card-content">
              <h2>{current.definition}</h2>
            </div>
          </div>
        </div>

        <button
          className="audio-btn"
          onClick={(e) => {
            e.stopPropagation();
            handleSpeak();
          }}
        >
          <FaVolumeUp />
        </button>
      </div>

      <div className="card-controls">
        <button onClick={handlePrev}>
          <FaChevronLeft />
        </button>
        <span className="counter">
          {currentIndex + 1} / {flashcards.length}
        </span>
        <button onClick={handleNext}>
          <FaChevronRight />
        </button>
      </div>
    </div>
  );
};

export default Flashcard;
