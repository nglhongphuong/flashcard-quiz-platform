import React from "react";
import { useNavigate } from "react-router-dom";
import "./PartContent.css";

const PartContent = () => {
    const navigate = useNavigate();

    const parts = [
        {
            title: "Explore the Library",
            description:
                "Browse a diverse collection of study sets, created by learners worldwide. Find what interests you and expand your knowledge.",
            btnText: "Go to Library",
            path: "/library",
            color: "#ffd6a5" // pastel orange
        },
        {
            title: "Interactive Flashcards",
            description:
                "Master your lessons faster with smart, customizable flashcards designed to help you retain knowledge effectively.",
            btnText: "Start New Lesson",
            path: "/create-lesson-home",
            color: "#6cc9cfff" // pastel mint
        },
        {
            title: "Your Lessons",
            description:
                "Access all the lessons you’ve created or enrolled in. Continue where you left off and keep your learning momentum going.",
            btnText: "View My Lessons",
            path: "/progress",
            color: "#ffb3c6" // pastel pink
        }
    ];

    return (
        <div className="part-content-container">
            {/* Title section */}
            <div className="welcome-section">
                <h1 className="welcome-title">Welcome to ZotriVerse</h1>
                <p className="welcome-subtitle">
                    Your all-in-one learning space — explore diverse topics, create interactive flashcards.
                </p>
            </div>

            {/* 3 parts */}
            <div className="part-grid">
                {parts.map((part, index) => (
                    <div
                        key={index}
                        className="part-card"
                        style={{ backgroundColor: part.color }}
                    >
                        <h2>{part.title}</h2>
                        <p>{part.description}</p>
                        <button
                            onClick={() => navigate(part.path)}
                            className="part-btn"
                        >
                            {part.btnText}
                        </button>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default PartContent;
