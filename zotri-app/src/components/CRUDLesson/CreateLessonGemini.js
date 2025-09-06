import { useState } from "react";
import { Button, Card, Form, Alert } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import { endpoints, authApis } from "../../configs/Apis";
import MySpinner from "../layouts/MySpinner";
import { FaMagic, FaTrash, FaEdit, FaSave, FaCheck, FaTimes } from "react-icons/fa";
import "./CreateLessonGemini.css";

const CreateLessonGemini = () => {
  const [topic, setTopic] = useState("");
  const [numQuestion, setNumQuestion] = useState(5);
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [flashcards, setFlashcards] = useState([]);
  const [image, setImage] = useState(null);

  const [editingCard, setEditingCard] = useState(null); // card index đang edit
  const [editingWord, setEditingWord] = useState("");
  const [editingDef, setEditingDef] = useState("");

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const navigate = useNavigate();

  const handleGenerate = async () => {
    try {
      setLoading(true);
      setError("");

      const formData = new FormData();
      formData.append("topic", topic);
      formData.append("num_question", numQuestion);

      let res = await authApis().post(endpoints.flashcard_ai_generate_by_topic, formData);
      const data = res.data;

      setTitle(data.title);
      setDescription(data.description);
      setFlashcards(data.flashcards);
    } catch (err) {
      setError(err.response?.data?.message || "AI generate failed");
    } finally {
      setLoading(false);
    }
  };

  const handleSave = async () => {
    try {
      setLoading(true);
      setError("");

      let form = new FormData();
      form.append("title", title);
      form.append("description", description);
      form.append("visibility", "PUBLIC");
      form.append("isCommentLocked", false);
      if (image) form.append("image", image);

      let res = await authApis().post(endpoints.lesson_create, form);

      if (res.data.code === 1000) {
        const lessonId = res.data.result.id;

        const formattedText = flashcards
          .map((card) => `${card.word}/${card.definition}`)
          .join("\n");

        const payload = {
          wordDelimiter: "/",
          cardDelimiter: "\\n",
          rawText: formattedText,
        };

        let res2 = await authApis().post(endpoints.flashcard_manual(lessonId), payload);

        if (res2.data.code === 1000) {
          navigate(`/user/lesson/${lessonId}`);
        } else {
          setError(res2.data.message || "Import flashcards failed");
        }
      } else {
        setError(res.data.message || "Lesson create failed");
      }
    } catch (err) {
      setError(err.response?.data?.message || "Server error");
    } finally {
      setLoading(false);
    }
  };

  const handleDeleteCard = (idx) => {
    setFlashcards(flashcards.filter((_, i) => i !== idx));
  };

  const handleEditCard = (idx) => {
    setEditingCard(idx);
    setEditingWord(flashcards[idx].word);
    setEditingDef(flashcards[idx].definition);
  };

  const handleSaveCardEdit = (idx) => {
    const updated = [...flashcards];
    updated[idx] = { word: editingWord, definition: editingDef };
    setFlashcards(updated);
    setEditingCard(null);
  };

  return (
    <div className="create-lesson-gemini">
      <Card className="p-4">
        <h2 className="create-lesson-gemini-title">
          <FaMagic className="me-2" /> Create Lesson with Gemini
        </h2>

        {error && <Alert variant="danger">{error}</Alert>}

        <Form>
          <Form.Group className="mb-3">
            <Form.Label>Topic</Form.Label>
            <Form.Control
              value={topic}
              onChange={(e) => setTopic(e.target.value)}
              placeholder="Enter topic (e.g., English vocabulary about nature)"
            />
          </Form.Group>

          <Form.Group className="mb-3">
            <Form.Label>Number of Flashcards</Form.Label>
            <Form.Control
              type="number"
              value={numQuestion}
              min={1}
              max={50}
              onChange={(e) => setNumQuestion(e.target.value)}
            />
          </Form.Group>

          <Button variant="dark" onClick={handleGenerate} disabled={loading}>
            {loading ? <MySpinner /> : <><FaMagic className="me-2" /> Generate Flashcards</>}
          </Button>
        </Form>

        {flashcards.length > 0 && (
          <div className="create-lesson-gemini-preview mt-4">
            <h4>
              <Form.Control
                value={title}
                onChange={(e) => setTitle(e.target.value)}
                placeholder="Lesson Title"
              />
            </h4>
            <Form.Control
              as="textarea"
              rows={2}
              className="mb-3"
              value={description}
              onChange={(e) => setDescription(e.target.value)}
              placeholder="Lesson Description"
            />

            <div className="create-lesson-gemini-flashcards">
              {flashcards.map((card, idx) => (
                <Card key={idx} className="p-3 mb-2 d-flex flex-row justify-content-between align-items-center">
                  {editingCard === idx ? (
                    <div className="flex-grow-1">
                      <Form.Control
                        className="mb-2"
                        value={editingWord}
                        onChange={(e) => setEditingWord(e.target.value)}
                        placeholder="Word"
                      />
                      <Form.Control
                        value={editingDef}
                        onChange={(e) => setEditingDef(e.target.value)}
                        placeholder="Definition"
                      />
                    </div>
                  ) : (
                    <div className="flex-grow-1">
                      <b>{card.word}</b> - {card.definition}
                    </div>
                  )}

                  <div className="ms-3">
                    {editingCard === idx ? (
                      <>
                        <Button
                          variant="success"
                          size="sm"
                          className="me-2"
                          onClick={() => handleSaveCardEdit(idx)}
                        >
                          <FaCheck />
                        </Button>
                        <Button
                          variant="secondary"
                          size="sm"
                          onClick={() => setEditingCard(null)}
                        >
                          <FaTimes />
                        </Button>
                      </>
                    ) : (
                      <>
                        <Button
                          variant="outline-primary"
                          size="sm"
                          className="me-2"
                          onClick={() => handleEditCard(idx)}
                        >
                          <FaEdit />
                        </Button>
                        <Button
                          variant="outline-danger"
                          size="sm"
                          onClick={() => handleDeleteCard(idx)}
                        >
                          <FaTrash />
                        </Button>
                      </>
                    )}
                  </div>
                </Card>
              ))}
            </div>

            <Button variant="success" onClick={handleSave} disabled={loading}>
              {loading ? <MySpinner /> : <><FaSave className="me-2" /> Save Lesson</>}
            </Button>
          </div>
        )}
      </Card>
    </div>
  );
};

export default CreateLessonGemini;
