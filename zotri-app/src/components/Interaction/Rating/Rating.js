import "./Rating.css";
import React, { useState } from "react";
import { authApis, endpoints } from "../../../configs/Apis";
import { FaStar } from "react-icons/fa";

export default function Rating({ lessonId, onClose, onRated  }) {
    const [star, setStar] = useState(null);
    const [loading, setLoading] = useState(false);

    const handleSubmit = async () => {
        if (!star) return;
        try {
            setLoading(true);
            await authApis().post(endpoints.rating(lessonId), { star: String(star) });
            if (onRated) {
                onRated(star); // tell parent about the new rating
            }

            onClose();
        } catch (err) {
            console.error("Error rating:", err);
        } finally {
            setLoading(false);
        }
    };

    return (
        <div className="rating-overlay">
            <div className="rating-modal">
                <h3 className="rating-title">Rate this lesson</h3>
                <div className="rating-stars">
                    {[1, 2, 3, 4, 5].map((s) => (
                        <FaStar
                            key={s}
                            size={32}
                            className={`rating-star ${s <= star ? "active" : ""}`}
                            onClick={() => setStar(s)}
                        />
                    ))}
                </div>
                <div className="rating-actions">
                    <button
                        className="rating-btn rating-submit"
                        onClick={handleSubmit}
                        disabled={loading}
                    >
                        Submit
                    </button>
                    <button className="rating-btn rating-cancel" onClick={onClose}>
                        Cancel
                    </button>
                </div>
            </div>
        </div>
    );
}
