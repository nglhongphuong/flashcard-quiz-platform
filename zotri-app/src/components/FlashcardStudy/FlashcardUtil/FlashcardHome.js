import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { authApis, endpoints } from "../../../configs/Apis";
import { FaVolumeUp, FaChevronLeft, FaChevronRight } from "react-icons/fa";
import DetectLanguage from "../../../configs/DetectLanguage";
import "./FlashcardHome.css";

const FlashcardHome = () => {
    const { lessonId, status } = useParams();
    const [flashcards, setFlashcards] = useState([]);
    const [currentIndex, setCurrentIndex] = useState(0);
    const [flipped, setFlipped] = useState(false);
    const nav = useNavigate();

    useEffect(() => {
        const loadFlashcards = async () => {
            try {
                if (status == "ALL") {
                    const res = await authApis().get(endpoints.flashcard_study_status(lessonId))
                    setFlashcards(res.data.result.flashcardSet);
                }
                else {
                    const form = new FormData();
                    form.append("status", status);
                    const res = await authApis().get(endpoints.flashcard_study_status(lessonId), {
                        params: { status },
                    });
                    setFlashcards(res.data.result.flashcardSet);
                }
            } catch (err) {
                console.error("Error loading flashcards", err);
            }
        };

        loadFlashcards();
    }, [lessonId, status]);

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

        const selectedVoice = voices.find((voice) => voice.lang.startsWith(lang));
        if (selectedVoice) utterance.voice = selectedVoice;

        speechSynthesis.speak(utterance);
    };

    const handleStatusUpdate = async (status) => {
        try {
            const form = new FormData();
            form.append("status", status);
            await authApis().put(endpoints.flashcard_flashcard_study(current.id), form);
        } catch (err) {
            console.error("Failed to update status", err);
        }
        handleNext();
    };

    const getInitial = (word) => word?.charAt(0).toUpperCase() || "?";

    const handleFinish = () => {
        nav(`/lesson/${lessonId}/flashcard-study`);
    };

 
    return (
        <div className="flashcard-container-flashcardhome">
            <div className="flashcard" onClick={handleFlip}>
                <div className={`card ${flipped ? "flipped" : ""}`}>
                    <div className="front">
                        <div className="card-content">
                            <div className="word-image-row">
                                {current.image ? (
                                    <img
                                        src={current.image}
                                        alt="flashcard"
                                        className="flashcard-image"
                                    />
                                ) : (
                                    <div className="flashcard-image">{getInitial(current.word)}</div>
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
                <button onClick={handlePrev}><FaChevronLeft /></button>
                <span className="counter">{currentIndex + 1} / {flashcards.length}</span>
                <button onClick={handleNext}><FaChevronRight /></button>
            </div>

            <div className="action-buttons-flashcardhome">
                <button className="notremember-btn" onClick={() => handleStatusUpdate("NOT_REMEMBERED")}>Not Remembered</button>
                <button className="remember-btn" onClick={() => handleStatusUpdate("REMEMBERED")}>Remembered</button>
                <button className="finish-btn" onClick={handleFinish}>Finish</button>
            </div>
        </div>
    );
};

export default FlashcardHome;
