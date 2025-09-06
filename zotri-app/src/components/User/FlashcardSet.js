import React, { useEffect, useState, useContext } from "react";
import {
  FaEllipsisV,
  FaVolumeUp,
  FaTrashAlt,
  FaEdit,
  FaSave,
  FaTimes,
} from "react-icons/fa";
import DetectLanguage from "../../configs/DetectLanguage";
import { MyUserContext } from "../../configs/MyContexts";
import { authApis, endpoints } from "../../configs/Apis";
import "./FlashcardSet.css";
import MySpinner from "../layouts/MySpinner";

const FlashcardSet = ({ flashcards_father }) => {
  const [flashcards, setFlashcards] = useState(flashcards_father);
  const user = useContext(MyUserContext);
  const [openMenuId, setOpenMenuId] = useState(null);
  const [editCardId, setEditCardId] = useState(null);
  const [editForm, setEditForm] = useState({});
  const [imagePreview, setImagePreview] = useState(null);
  const [msg, setMsg] = useState("");
  const [loadingId, setLoadingId] = useState(null);
  const [loading, setLoading] = useState(false);

  const handleToggleMenu = (id) => {
    setOpenMenuId((prev) => (prev === id ? null : id));
  };

  const handleSpeak = (text) => {
    const utterance = new SpeechSynthesisUtterance(text);
    const voices = window.speechSynthesis.getVoices();
    const lang = DetectLanguage(text);

    const selectedVoice = voices.find((voice) =>
      voice.lang.toLowerCase().startsWith(lang.toLowerCase())
    );

    if (selectedVoice) {
      utterance.voice = selectedVoice;
    }

    speechSynthesis.speak(utterance);
  };

  const handleEdit = (card) => {
    setEditCardId(card.id);
    setOpenMenuId(null);
    setEditForm({ word: card.word, definition: card.definition, image: null });
    setImagePreview(card.image);
    setMsg("");
  };

  const handleDelete = async (cardId) => {
    try {
      setLoading(true);
      setLoadingId(cardId);
      await authApis().delete(endpoints.flashcard(cardId));
      setMsg("Deleted Successfully !");
       // Xoá khỏi state flashcards
      setFlashcards(prev => prev.filter(card => card.id !== cardId));

    } catch (err) {
      console.error(err);
      setMsg("Deleted Fail!");
    } finally {
      setLoadingId(null);
      setLoading(false);
    }
  };

  const handleFileChange = (e) => {
    const file = e.target.files[0];
    if (file) {
      setEditForm((prev) => ({ ...prev, image: file }));
      setImagePreview(URL.createObjectURL(file));
    }
  };

  const handleInputChange = (e) => {
    const { name, value } = e.target;
    setEditForm((prev) => ({ ...prev, [name]: value }));
  };

  const handleCancelEdit = () => {
    setEditCardId(null);
    setEditForm({});
    setImagePreview(null);
    setMsg("");
  };

  const handleSave = async (cardId) => {
    try {
      const form = new FormData();
      if (editForm.word) form.append("word", editForm.word);
      if (editForm.definition) form.append("definition", editForm.definition);
      if (editForm.image instanceof File) {
        form.append("image", editForm.image);
      }

      setLoadingId(cardId);
      setLoading(true);
      const res = await authApis().put(endpoints.flashcard(cardId), form, {
        headers: { "Content-Type": "multipart/form-data" },
      });

      const updatedCard = res.data.result;

      setFlashcards(prev =>
        prev.map(card => card.id === updatedCard.id ? updatedCard : card)
      );
       setMsg("Update Successfully !!");
      setEditCardId(null);
      setEditForm({});
      setImagePreview(null);

    } catch (err) {
      console.error(err);
      setMsg("Update Fail !!");
    } finally {
      setLoadingId(null);
      setLoading(false);
    }
  };
  useEffect(() => {
  if (msg) {
    const timer = setTimeout(() => {
      setMsg("");
    }, 5000);

    return () => clearTimeout(timer); // clear nếu msg thay đổi trước 5s
  }
}, [msg]);


  return (
    <div className="flashcardset-container">
      {msg && <p className="msg">{msg}</p>}
      {loading && <MySpinner />}
      {flashcards.map((card) => (
        <div key={card.id} className="flashcardset-card">
          {editCardId === card.id ? (
            <div className="flashcardset-edit-form">
              <input
                type="text"
                name="word"
                value={editForm.word}
                onChange={handleInputChange}
                placeholder="Word"
                className="flashcardset-input"
              />
              <input
                type="text"
                name="definition"
                value={editForm.definition}
                onChange={handleInputChange}
                placeholder="Definition"
                className="flashcardset-input"
              />
              <input
                type="file"
                onChange={handleFileChange}
                className="custom-file-input"
              />

              {imagePreview && (
                <img
                  src={imagePreview}
                  alt="preview"
                  className="flashcardset-image-preview"
                />
              )}
              <div className="flashcardset-edit-buttons">
                <button className="btn-save" onClick={() => handleSave(card.id)}>
                  <FaSave className="icon" /> Lưu
                </button>

                <button className="btn-cancel" onClick={handleCancelEdit}>
                  <FaTimes className="icon" /> Hủy
                </button>
              </div>

            </div>
          ) : (
            <div className="flashcardset-content-row">
              {card.image ? (
                <img
                  src={card.image}
                  alt={card.word}
                  className="flashcardset-image"
                />
              ) : (
                <div className="flashcardset-placeholder">
                  {card.word?.charAt(0)?.toUpperCase() || "?"}
                </div>
              )}

              {/* Word + definition */}
              <div className="flashcardset-word-def">
                <div className="flashcardset-word">
                  {card.word}
                  <FaVolumeUp
                    className="flashcardset-audio-icon"
                    onClick={() => handleSpeak(card.word)}
                  />
                </div>
                <div className="flashcardset-def">
                  {card.definition}
                  <FaVolumeUp
                    className="flashcardset-audio-icon"
                    onClick={() => handleSpeak(card.definition)}
                  />
                </div>
              </div>

              {/* Menu */}
              <div
                className="flashcardset-menu-icon"
                onClick={() => handleToggleMenu(card.id)}
              >
                <FaEllipsisV />
                {openMenuId === card.id && (
                  <div className="flashcardset-dropdown">
                    <div
                      className="flashcardset-dropdown-item"
                      onClick={() => handleEdit(card)}
                    >
                      <FaEdit />
                    </div>
                    <div
                      className="flashcardset-dropdown-item"
                      onClick={() => handleDelete(card.id)}
                    >
                      {loadingId === card.id ? <MySpinner /> : <><FaTrashAlt /> </>}
                    </div>
                  </div>
                )}
              </div>
            </div>
          )}
        </div>
      ))}
    </div>
  );
};

export default FlashcardSet;
