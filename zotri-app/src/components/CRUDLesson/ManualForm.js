import React, { useState } from "react";
import { authApis, endpoints } from "../../configs/Apis";
import MySpinner from "../layouts/MySpinner";
import "./ManualForm.css";

const ManualForm = ({ lessonId, onBack }) => {
  const [rawTextInput, setRawTextInput] = useState("");
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const [wordDelimiter, setWordDelimiter] = useState("comma");
  const [cardDelimiter, setCardDelimiter] = useState("newline");
  const [customWordDelimiter, setCustomWordDelimiter] = useState("");
  const [customCardDelimiter, setCustomCardDelimiter] = useState("");
  const [msg, setMsg] = useState("");

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError("");
    setMsg(""); 

    if (!rawTextInput.trim()) {
      setError("Please enter some text.");
      return;
    }

    const resolvedWordDelimiter =
      wordDelimiter === "custom"
        ? customWordDelimiter
        : wordDelimiter === "comma"
          ? ","
          : wordDelimiter === "tab"
            ? "\t"
            : wordDelimiter;

    const resolvedCardDelimiter =
      cardDelimiter === "custom"
        ? customCardDelimiter
        : cardDelimiter === "newline"
          ? "\\n"
          : cardDelimiter === "semicolon"
            ? ";"
            : cardDelimiter;

    const lines =
      cardDelimiter === "newline"
        ? rawTextInput.trim().split("\n")
        : rawTextInput.trim().split(resolvedCardDelimiter);

    // const formattedText = lines
    // .map((line) => {
    //   const parts = line
    //     .split(resolvedWordDelimiter)
    //     .map((p) => p.trim().replace(/\r/g, ""));
    //   return parts.join("/");
    // })
    // .join("\n");

    const formattedText = lines
      .map((line) => {
        const parts = line
          .split(resolvedWordDelimiter)
          .map((p) => p.trim().replace(/\r/g, ""));

        // Chỉ giữ 3 phần: word, definition, image (nếu có)
        const word = parts[0] || "";
        const definition = parts[1] || "";
        const image = parts[2] || "";

        return [word, definition, image].filter(Boolean).join("/");
      })
      .join("\n");



    const payload = {
      wordDelimiter: "/",
      cardDelimiter: "\\n",
      rawText: formattedText,
    };

    console.log("hahaahha : ", payload);

    try {
      setLoading(true);
      const res = await authApis().post(endpoints.flashcard_manual(lessonId), payload);
            if (res.data.code === 1000) {
        setMsg("Upload Successfully!");
        setRawTextInput("");
        setWordDelimiter("comma");
        setCardDelimiter("newline");
        setCustomWordDelimiter("");
        setCustomCardDelimiter("");
      } else {
        setError(res.data.message || "Import failed.");
      }
    } catch (err) {
      setError(err.response?.data?.message || "Server error");
    } finally {
      setLoading(false);
    }
  };

const previewCards = rawTextInput
  .trim()
  .split(
    cardDelimiter === "newline"
      ? "\n"
      : cardDelimiter === "semicolon"
      ? ";"
      : customCardDelimiter
  )
  .map((line, idx) => {
    const parts = line.split(
      wordDelimiter === "comma"
        ? ","
        : wordDelimiter === "tab"
        ? "\t"
        : customWordDelimiter
    ).map(p => p.trim()); // Bỏ khoảng trắng đầu đuôi từng phần

    const word = parts[0] || "";
    const definition = parts[1] || "";
    const image = parts[2] || "";

    if (!word || !definition) return null;

    return { word, definition, image, id: idx };
  })
  .filter(Boolean);


  return (
    <div className="manual-form-container">
      <h2 className="manual-form-title">Create Flashcards</h2>

      {error && <p className="manual-form-error">{error}</p>}
        {msg && <p className="manual-form-success">{msg}</p>}
      {loading && <MySpinner />}

      <div className="manual-form-wrapper">
        <form className="manual-form" onSubmit={handleSubmit}>
          <div className="delimiter-section">
            <div>
              <label className="section-label">Between word and definition</label>
              <div className="options-manual">
                <label><input type="radio" value="comma" checked={wordDelimiter === "comma"} onChange={() => setWordDelimiter("comma")} /> Comma</label>
                <label><input type="radio" value="tab" checked={wordDelimiter === "tab"} onChange={() => setWordDelimiter("tab")} /> Tab</label>
                <label><input type="radio" value="custom" checked={wordDelimiter === "custom"} onChange={() => setWordDelimiter("custom")} /> Custom</label>
                {wordDelimiter === "custom" && (
                  <input className="custom-input" type="text" value={customWordDelimiter} onChange={(e) => setCustomWordDelimiter(e.target.value)} />
                )}
              </div>
            </div>
            <div>
              <label className="section-label">Between lines</label>
              <div className="options-manual">
                <label><input type="radio" value="newline" checked={cardDelimiter === "newline"} onChange={() => setCardDelimiter("newline")} /> New line</label>
                <label><input type="radio" value="semicolon" checked={cardDelimiter === "semicolon"} onChange={() => setCardDelimiter("semicolon")} /> Semicolon</label>
                <label><input type="radio" value="custom" checked={cardDelimiter === "custom"} onChange={() => setCardDelimiter("custom")} /> Custom</label>
                {cardDelimiter === "custom" && (
                  <input className="custom-input" type="text" value={customCardDelimiter} onChange={(e) => setCustomCardDelimiter(e.target.value)} />
                )}
              </div>
            </div>
          </div>

          <textarea
            value={rawTextInput}
            onChange={(e) => setRawTextInput(e.target.value)}
            placeholder="Format:\nHello/Xin chào\nWorld/Thế giới"
            rows={8}
            className="textarea"
          />



          <div className="buttons">
            <button type="button" className="btn btn-back" onClick={onBack}>Back</button>
            <button type="submit" className="btn btn-submit">Import</button>
          </div>
        </form>

        <div className="manual-form-note">
          <h3 className="note-title">Instructions:</h3>
          <ul className="note-list">
            <li>Select how to separate the word and definition (e.g. comma, tab...)</li>
            <li>Select how to separate each card (e.g. new line, semicolon...)</li>
            <li>Paste your word list into the input box below.</li>
            <li>The system will convert it into standard format automatically.</li>
            <li>Example: <code>Apple, Quả táo</code> → <code>Apple/Quả táo</code></li>
            <li>Optional: Add image URL (e.g. <code>Cat,Con mèo,https://img.url</code>)</li>
          </ul>
        </div>
      </div>

      {previewCards.length > 0 && (
        <div className="preview-list">
          <h3 className="preview-title">Preview:</h3>
          {previewCards.map(card => (
            <div className="preview-card" key={card.id}>
              {card.image && (
                <img
                  src={card.image}
                  alt={card.word}
                  className="preview-image"
                  onError={(e) => {
                    e.target.onerror = null;
                    e.target.src = "https://via.placeholder.com/100?text=No+Image";
                  }}
                />
              )}

              <div className="preview-content">
                <h4 className="preview-word">{card.word}</h4>
                <p className="preview-definition">{card.definition}</p>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
};

export default ManualForm;