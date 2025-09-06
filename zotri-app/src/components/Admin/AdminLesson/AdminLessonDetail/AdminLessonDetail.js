import "./AdminLessonDetail.css";
import { useContext, useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { authApis, endpoints } from "../../../../configs/Apis";
import { FaEdit, FaEye, FaTrash, FaCheckCircle, FaTimesCircle } from "react-icons/fa";
import MySpinner from "../../../layouts/MySpinner";

const AdminLessonDetail = () => {
    const { id } = useParams();
    const nav = useNavigate();
    const [lesson, setLesson] = useState(null);
    const [loading, setLoading] = useState(true);
    const [saving, setSaving] = useState(false);

    const [newFlashcard, setNewFlashcard] = useState({ word: "", definition: "", image: null });
    const [editingId, setEditingId] = useState(null);
    const [editingFlashcard, setEditingFlashcard] = useState({ word: "", definition: "", image: "" });
    const [showCreateForm, setShowCreateForm] = useState(false);

     const [editingLesson, setEditingLesson] = useState(false);
    const [lessonForm, setLessonForm] = useState({
        title: "",
        description: "",
        image: null,
        visibility: "PUBLIC",
        isCommentLocked: false
    });

    const handleUpdateLesson = async () => {
        try {
            setSaving(true);
            const formData = new FormData();
            formData.append("title", lessonForm.title);
            formData.append("description", lessonForm.description);
            if (lessonForm.image) formData.append("image", lessonForm.image);
            formData.append("visibility", lessonForm.visibility);
            formData.append("isCommentLocked", lessonForm.isCommentLocked);

            const res = await authApis().put(
                endpoints.lesson_update(id),
                formData,
                { headers: { "Content-Type": "multipart/form-data" } }
            );

            setLesson(res.data);
            setEditingLesson(false);
        } catch (err) {
            console.error("Error updating lesson:", err);
        } finally {
            setSaving(false);
        }
    };

    
    useEffect(() => {
        if (lesson) {
            setLessonForm({
                title: info.title,
                description: info.description,
                image: null, // giữ null để không update ảnh nếu user không đổi
                visibility: info.visibility,
                isCommentLocked: info.isCommentLocked
            });
        }
    }, [lesson]);

    useEffect(() => {
        const loadLessonDetail = async () => {
            try {
                const res = await authApis().get(endpoints.admin_lesson(id));
                setLesson(res.data);
            } catch (err) {
                console.error("Error loading lesson detail:", err);
            } finally {
                setLoading(false);
            }
        };
        loadLessonDetail();
    }, [id]);

    if (loading) return <MySpinner />;
    if (!lesson) return <p className="lesson-admin-detail-error">Lesson not found.</p>;

    const info = lesson.result;

    const handleCreateFlashcard = async () => {
        try {
            setSaving(true);
            const formData = new FormData();
            formData.append("word", newFlashcard.word);
            formData.append("definition", newFlashcard.definition);
            if (newFlashcard.image) formData.append("image", newFlashcard.image);

            const res = await authApis().post(
                endpoints.admin_flashcard(id),
                formData,
                { headers: { "Content-Type": "multipart/form-data" } }
            );

            setLesson(prev => ({
                ...prev,
                result: {
                    ...prev.result,
                    flashcardSet: [...prev.result.flashcardSet, res.data.result]
                }
            }));
            setNewFlashcard({ word: "", definition: "", image: null });
            setShowCreateForm(false);
        } catch (err) {
            console.error("Error creating flashcard:", err);
        } finally {
            setSaving(false);
        }
    };

    const handleDeleteFlashcard = async (flashcardId) => {
        if (!window.confirm("Are you sure you want to delete this flashcard?")) return;
        try {
            setSaving(true);
            await authApis().delete(endpoints.flashcard(flashcardId));
            setLesson(prev => ({
                ...prev,
                result: {
                    ...prev.result,
                    flashcardSet: prev.result.flashcardSet.filter(fc => fc.id !== flashcardId)
                }
            }));
        } catch (err) {
            console.error("Error deleting flashcard:", err);
        } finally {
            setSaving(false);
        }
    };

    const startEdit = (fc) => {
        setEditingId(fc.id);
        setEditingFlashcard({ word: fc.word, definition: fc.definition, image: fc.image || "" });
    };

    const handleUpdateFlashcard = async (flashcardId) => {
        try {
            setSaving(true);
            const formData = new FormData();
            formData.append("word", editingFlashcard.word);
            formData.append("definition", editingFlashcard.definition);
            if (editingFlashcard.image instanceof File) {
                formData.append("image", editingFlashcard.image);
            }
            const res = await authApis().put(
                endpoints.flashcard(flashcardId),
                formData,
                { headers: { "Content-Type": "multipart/form-data" } }
            );

            setLesson(prev => ({
                ...prev,
                result: {
                    ...prev.result,
                    flashcardSet: prev.result.flashcardSet.map(fc =>
                        fc.id === flashcardId ? res.data.result : fc
                    )
                }
            }));
            setEditingId(null);
        } catch (err) {
            console.error("Error updating flashcard:", err);
        } finally {
            setSaving(false);
        }
    };

   


    return (
        <div className="lesson-admin-detail-container">
            {saving && <MySpinner />}

            <h1 className="lesson-admin-detail-title">Lesson Detail</h1>
            <div className="lesson-admin-detail-actions">
                <button
                    className="lesson-admin-detail-button"
                    onClick={() => nav(`/admin/lessons/${id}/stats`)}
                >
                    View Statistics
                </button>
            </div>

            <div className="lesson-admin-detail-grid">
                <div className="lesson-admin-detail-card">
                    <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
                        <h2>Lesson Information #{lesson.result.id}</h2>
                        <button className="lesson-admin-detail-button" onClick={() => setEditingLesson(!editingLesson)}>
                           Edit
                        </button>
                    </div>

                    {editingLesson ? (
                        <div className="lesson-admin-detail-form">
                            <input
                                type="text"
                                value={lessonForm.title}
                                onChange={(e) => setLessonForm({ ...lessonForm, title: e.target.value })}
                                placeholder="Title"
                            />
                            <textarea
                                value={lessonForm.description}
                                onChange={(e) => setLessonForm({ ...lessonForm, description: e.target.value })}
                                placeholder="Description"
                            />
                            <input
                                type="file"
                                accept="image/*"
                                onChange={(e) => setLessonForm({ ...lessonForm, image: e.target.files[0] })}
                            />
                            <select
                                value={lessonForm.visibility}
                                onChange={(e) => setLessonForm({ ...lessonForm, visibility: e.target.value })}
                            >
                                <option value="PUBLIC">PUBLIC</option>
                                <option value="PRIVATE">PRIVATE</option>
                            </select>
                            <label>
                                <input
                                    type="checkbox"
                                    checked={lessonForm.isCommentLocked}
                                    onChange={(e) => setLessonForm({ ...lessonForm, isCommentLocked: e.target.checked })}
                                />
                                Lock Comments
                            </label>

                            <div className="lesson-admin-detail-form-buttons">
                                <button className="btn-save" onClick={handleUpdateLesson}>Save</button>
                                <button className="btn-cancel" onClick={() => setEditingLesson(false)}>Cancel</button>
                            </div>
                        </div>
                    ) : (
                        <>
                            <img src={info.image} alt={info.title} className="lesson-admin-detail-image" />
                            <p><strong>Title:</strong> {info.title}</p>
                            <p><strong>Description:</strong> {info.description}</p>
                            <p><strong>Visibility:</strong> {info.visibility}</p>
                            <p><strong>Comment Locked:</strong> {info.isCommentLocked ? "Yes" : "No"}</p>
                            <p><strong>Created At:</strong> {new Date(info.createdAt).toLocaleString()}</p>
                            <p><strong>Updated At:</strong> {new Date(info.updateAt).toLocaleString()}</p>
                        </>
                    )}
                </div>


                <div className="lesson-admin-detail-card">
                    <h2>Author Information</h2>
                    <img
                        src={info.userInfo?.avatar}
                        alt={info.userInfo?.name}
                        className="lesson-admin-detail-avatar"
                    />
                    <p><strong>ID:</strong> {info.userInfo?.id}</p>
                    <p><strong>Name:</strong> {info.userInfo?.name}</p>
                    <p><strong>Username:</strong> {info.userInfo?.username}</p>
                    <p><strong>Role:</strong> {info.userInfo?.role}</p>
                </div>
            </div>

            <div className="lesson-admin-detail-card">
                <div style={{ display: "flex", justifyContent: "space-between", alignItems: "center" }}>
                    <h2>Flashcards</h2>
                    <button className="btn btn-success" onClick={() => setShowCreateForm(!showCreateForm)}>
                        Add Flashcard
                    </button>
                </div>

                {showCreateForm && (
                    <div className="lesson-admin-detail-form">
                        <input
                            type="text"
                            placeholder="Word"
                            value={newFlashcard.word}
                            onChange={(e) => setNewFlashcard({ ...newFlashcard, word: e.target.value })}
                        />
                        <input
                            type="text"
                            placeholder="Definition"
                            value={newFlashcard.definition}
                            onChange={(e) => setNewFlashcard({ ...newFlashcard, definition: e.target.value })}
                        />
                        <input
                            type="file"
                            accept="image/*"
                            onChange={(e) =>
                                setNewFlashcard({
                                    ...newFlashcard,
                                    image: e.target.files[0]
                                })
                            }
                        />

                        <div className="lesson-admin-detail-form-buttons">
                            <button className="btn-save" onClick={handleCreateFlashcard}>
                                Save
                            </button>
                            <button className="btn-cancel" onClick={() => setShowCreateForm(false)}>
                                Cancel
                            </button>
                        </div>
                    </div>
                )}

                {info.flashcardSet?.length > 0 ? (
                    <table className="lesson-admin-detail-table">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Word</th>
                                <th>Definition</th>
                                <th>Image</th>
                                <th>Created</th>
                                <th>Updated</th>
                                <th>Actions</th>
                            </tr>
                        </thead>
                        <tbody>
                            {info.flashcardSet.map(fc => (
                                <tr key={fc.id}>
                                    <td>{fc.id}</td>
                                    <td>
                                        {editingId === fc.id ? (
                                            <input
                                                value={editingFlashcard.word}
                                                onChange={(e) => setEditingFlashcard({ ...editingFlashcard, word: e.target.value })}
                                            />
                                        ) : fc.word}
                                    </td>
                                    <td>
                                        {editingId === fc.id ? (
                                            <input
                                                value={editingFlashcard.definition}
                                                onChange={(e) => setEditingFlashcard({ ...editingFlashcard, definition: e.target.value })}
                                            />
                                        ) : fc.definition}
                                    </td>
                                    <td>
                                        {editingId === fc.id ? (
                                            <input
                                                type="file"
                                                accept="image/*"
                                                onChange={(e) =>
                                                    setEditingFlashcard({
                                                        ...editingFlashcard,
                                                        image: e.target.files[0]
                                                    })
                                                }
                                            />
                                        ) : fc.image ? (
                                            <img src={fc.image} alt={fc.word} style={{ width: "50px" }} />
                                        ) : "—"}
                                    </td>
                                    <td>{new Date(fc.createdAt).toLocaleString()}</td>
                                    <td>{new Date(fc.updateAt).toLocaleString()}</td>
                                    <td>
                                        {editingId === fc.id ? (
                                            <>
                                                <FaCheckCircle
                                                    style={{ color: "green", cursor: "pointer", marginRight: "8px" }}
                                                    onClick={() => handleUpdateFlashcard(fc.id)}
                                                />
                                                <FaTimesCircle
                                                    style={{ color: "red", cursor: "pointer" }}
                                                    onClick={() => setEditingId(null)}
                                                />
                                            </>
                                        ) : (
                                            <>
                                                <FaEdit
                                                    style={{ color: "dodgerblue", cursor: "pointer", marginRight: "8px" }}
                                                    onClick={() => startEdit(fc)}
                                                />
                                                <FaTrash
                                                    style={{ color: "red", cursor: "pointer" }}
                                                    onClick={() => handleDeleteFlashcard(fc.id)}
                                                />
                                            </>
                                        )}
                                    </td>
                                </tr>
                            ))}
                        </tbody>
                    </table>
                ) : (
                    <p>No flashcards available.</p>
                )}
            </div>
        </div>
    );
};

export default AdminLessonDetail;
