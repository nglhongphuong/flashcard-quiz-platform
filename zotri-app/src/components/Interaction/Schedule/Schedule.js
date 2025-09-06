import "./Schedule.css";
import React, { useState } from "react";
import { authApis, endpoints } from "../../../configs/Apis";

export default function Schedule({ lessonId, onClose }) {
  const [scheduledTime, setScheduledTime] = useState("");
  const [notice, setNotice] = useState("");
  const [loading, setLoading] = useState(false);
  const [successMsg, setSuccessMsg] = useState("");

  const handleSubmit = async () => {
    if (!scheduledTime) return;
    try {
      setLoading(true);
      await authApis().post(endpoints.schedule(lessonId), {
        scheduledTime,
        notice
      });
      setSuccessMsg("Schedule created successfully!");
      setTimeout(() => {
        setSuccessMsg("");
        onClose();
      }, 2000); // đóng sau 2 giây
    } catch (err) {
      console.error("Error scheduling:", err);
      if (err.response && err.response.data) {
        const msg = err.response.data.message || err.response.data.error;

        if (msg && msg.includes("Duplicate")) {
          setSuccessMsg("You already scheduled this lesson!");
        } else {
          setSuccessMsg("Failed to create schedule. Please try again.");
        }
      } else {
        setSuccessMsg("Network error. Please try again.");
      }

    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="schedule-overlay">
      <div className="schedule-modal">
        <h3>Schedule lesson</h3>

        {successMsg && <div className="schedule-success">{successMsg}</div>}

        <input
          type="datetime-local"
          value={scheduledTime}
          onChange={(e) => setScheduledTime(e.target.value)}
          min={(() => {
            const tomorrow = new Date();
            tomorrow.setDate(tomorrow.getDate() + 1);
            return tomorrow.toISOString().slice(0, 16);
          })()}
        />

        <textarea
          placeholder="Notes"
          value={notice}
          onChange={(e) => setNotice(e.target.value)}
        />
        <div className="schedule-actions">
          <button
            onClick={handleSubmit}
            disabled={loading}
            className="pastel-btn pastel-green"
          >
            {loading ? "Saving..." : "Submit"}
          </button>
          <button onClick={onClose} className="pastel-btn pastel-red">
            Cancel
          </button>
        </div>
      </div>
    </div>
  );
}
