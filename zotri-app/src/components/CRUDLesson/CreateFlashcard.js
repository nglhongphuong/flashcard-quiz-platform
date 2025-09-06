import React, { useRef, useState } from "react";
import axios from "axios";
import ManualForm from "./ManualForm";
import { useParams, useNavigate } from "react-router-dom";
import MySpinner from "../layouts/MySpinner";
import { authApis, endpoints } from "../../configs/Apis";
import "./CreateFlashcard.css";

const CreateFlashcard = () => {
    const { lessonId } = useParams();
    const navigate = useNavigate();
    const [success, setSuccess] = useState("");


    const fileInputRef = useRef(null);
    const [manualMode, setManualMode] = useState(false);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");

    const handleBackToLesson = () => {
        navigate(`/user/lesson/${lessonId}`);
    };

    const handleFileChange = async (e) => {
        const file = e.target.files[0];
        setError("");

        if (!file) return;

        const validTypes = [
            "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
            "text/csv",
        ];
        if (!validTypes.includes(file.type)) {
            setError("Only .xlsx or .csv files are supported.");
            return;
        }

        const formData = new FormData();
        formData.append("file", file);

        try {
            setLoading(true);
            const res = await authApis().post(endpoints.flashcard_upload(lessonId), formData);

            if (res.data.code === 1000) {
                setSuccess("Import File CSV/EXCEL successful!");
                setError("");
                setTimeout(() => setSuccess(""), 5000);
            } else {
                setError(res.data.message || "Import failed.");
                setSuccess("");
            }
        } catch (err) {
            setError(err.response?.data?.message || "Server error");
        } finally {
            setLoading(false);
        }
    };

    if (manualMode) {
        return <ManualForm lessonId={lessonId} onBack={() => setManualMode(false)} />;
    }






    return (
        <div className="flashcard-container-create">
            <button className="back-button" onClick={handleBackToLesson}>
                ← Back to Lesson
            </button>
            {success && <p className="success">{success}</p>}

            <h1 className="title">Create Flashcards</h1>

            {error && <p className="error">{error}</p>}
            {loading && <MySpinner />}

            <div className="button-group">

                <button
                    className="action-button pastel-gemini"
                    onClick={() => navigate(`/lesson/${lessonId}/create-flashcard-gemini`)}
                >
                    Generate with Gemini
                </button>

                <button className="action-button" onClick={() => setManualMode(true)}>
                    Import Manually
                </button>

                <button className="action-button" onClick={() => fileInputRef.current.click()}>
                    Upload File CSV / Excel
                </button>

                <input
                    ref={fileInputRef}
                    type="file"
                    accept=".xlsx,.csv"
                    style={{ display: "none" }}
                    onChange={handleFileChange}
                />
            </div>

            <div className="note-section">
                <p className="note-title">Import file notes:</p>
                <ul className="note-list">
                    <li>Only Excel (.xlsx) or CSV (.csv) files are supported.</li>
                    <li>The file must have at least 3 columns in order: <strong>Word | Definition | Image</strong></li>
                    <li>Column names are optional, but the order must be exact.</li>
                    <li>The Image column should contain image URLs or be left blank.</li>
                </ul>
            </div>
        </div>
    );
};

export default CreateFlashcard;
