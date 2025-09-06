import { useState, useRef } from "react";
import { Button, Card, Form, Alert } from "react-bootstrap";
import { useNavigate, useParams } from "react-router-dom";
import axios from "axios";
import { endpoints, authApis } from "../../configs/Apis";
import MySpinner from "../layouts/MySpinner";
import { FaMagic, FaTrash, FaEdit, FaSave } from "react-icons/fa";


const FlashcardGemini = () => {
    const { lessonId } = useParams();
    const [flashcards, setFlashcards] = useState([]);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState("");
    const [success, setSuccess] = useState("");
    const [numQuestion, setNumQuestion] = useState(5); // mặc định 5 câu
    const fileInputRef = useRef(null);
    const navigate = useNavigate();

    const handleFileChange = async (e) => {
        const file = e.target.files[0];
        if (!file) return;

        const validTypes = [
            "application/pdf",
            "application/msword",
            "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
            "text/plain",
        ];

        if (!validTypes.includes(file.type)) {
            setError("Only PDF, Word (.doc/.docx), or Text (.txt) files are supported.");
            return;
        }

        const formData = new FormData();
        formData.append("file", file);
        formData.append("num_question", numQuestion);

        try {
            setLoading(true);
            setError("");
            const res = await axios.post(endpoints.flashcard_ai_generate, formData, {
                headers: { "Content-Type": "multipart/form-data" },
            });

            if (res.data?.flashcards) {
                setFlashcards(res.data.flashcards);
                setSuccess("AI successfully generated flashcards!");
            } else {
                setError("Failed to generate flashcards.");
            }
        } catch (err) {
            setError(err.response?.data?.message || "Server error");
        } finally {
            setLoading(false);
        }
    };

    const handleDelete = (index) => {
        setFlashcards(flashcards.filter((_, i) => i !== index));
    };

    const handleChange = (index, field, value) => {
        const updated = [...flashcards];
        updated[index][field] = value;
        setFlashcards(updated);
    };

    const handleSubmit = async () => {
        try {
            setLoading(true);

            // Gom word/definition thành rawText theo format
            const formattedText = flashcards
                .map(card => `${card.word}/${card.definition}`)
                .join("\n");

            const payload = {
                wordDelimiter: "/",
                cardDelimiter: "\\n",
                rawText: formattedText,
            };

            console.log("payload >>> ", payload);

            const res = await authApis().post(endpoints.flashcard_manual(lessonId), payload);

            if (res.data.code === 1000) {
                navigate(`/user/lesson/${lessonId}`);
            } else {
                setError(res.data.message || "Import failed.");
            }
        } catch (err) {
            setError(err.response?.data?.message || "Server error");
        } finally {
            setLoading(false);
        }
    };


    return (
        <div className="flashcard-gemini-container">
            {loading && (
                <div className="flashcard-gemini-overlay">
                    <div className="flashcard-gemini-spinner">
                        <MySpinner />
                        <p className="flashcard-gemini-loading-text">Generating Flashcards...</p>
                    </div>
                </div>
            )}

            <Button
                variant="secondary"
                className="mt-3"
                onClick={() => navigate(`/lesson/${lessonId}`)}
            >
                Back to Lesson
            </Button>
            <h2 className="flashcard-gemini-title">
                < FaMagic className="mr-2 text-pink-500" /> Gemini Flashcard Generator
            </h2>
            <p className="flashcard-gemini-subtitle">
                Powered by <strong>Gemini 2.5 (preview)</strong> <br />
                Experimental version with some limitations — but still good enough to generate cool flashcards for you!
            </p>


            {error && <Alert variant="danger">{error}</Alert>}
            {success && <Alert variant="success">{success}</Alert>}

            <Form.Group className="mb-3">
                <Form.Label>Number of Questions</Form.Label>
                <Form.Control
                    type="number"
                    value={numQuestion}
                    onChange={(e) => setNumQuestion(e.target.value)}
                    min="1"
                    max="50"
                />
            </Form.Group>

            <input
                type="file"
                ref={fileInputRef}
                style={{ display: "none" }}
                onChange={handleFileChange}
                accept=".pdf,.doc,.docx,.txt"
            />

            <Button
                className="flashcard-gemini-upload"
                onClick={() => fileInputRef.current.click()}
                disabled={loading}
            >
                {loading ? <MySpinner /> : "Upload File"}
            </Button>

            <div className="flashcard-gemini-list mt-4">
                {flashcards.map((card, index) => (
                    <Card key={index} className="mb-3 p-3 rounded-lg shadow">
                        <Form.Control
                            className="mb-2"
                            value={card.word}
                            onChange={(e) => handleChange(index, "word", e.target.value)}
                        />
                        <Form.Control
                            as="textarea"
                            rows={3}
                            value={card.definition}
                            onChange={(e) => handleChange(index, "definition", e.target.value)}
                        />
                        <div className="mt-2 d-flex justify-content-between">
                            <Button
                                variant="danger"
                                size="sm"
                                onClick={() => handleDelete(index)}
                            >
                                <FaTrash /> Delete
                            </Button>
                        </div>
                    </Card>
                ))}
            </div>

            {flashcards.length > 0 && (
                <Button
                    className="flashcard-gemini-submit mt-3"
                    onClick={handleSubmit}
                    disabled={loading}
                >
                    Submit All
                </Button>
            )}

        </div>
    );
};

export default FlashcardGemini;
