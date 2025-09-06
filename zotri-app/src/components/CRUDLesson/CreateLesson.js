import React, { useContext, useState } from "react";
import { useNavigate } from "react-router-dom";
import { MyUserContext } from "../../configs/MyContexts";
import { authApis, endpoints } from "../../configs/Apis";
import MySpinner from "../layouts/MySpinner";
import { FaUpload } from "react-icons/fa";
import "./CreateLesson.css";


const CreateLesson = () => {
    const user = useContext(MyUserContext);
    const nav = useNavigate();

    const [title, setTitle] = useState("");
    const [description, setDescription] = useState("");
    const [visibility, setVisibility] = useState("PUBLIC");
    const [isCommentLocked, setIsCommentLocked] = useState(false);
    const [image, setImage] = useState(null);
    const [loading, setLoading] = useState(false);
    const [error, setError] = useState(null);

    const handleImageChange = (e) => {
        setImage(e.target.files[0]);
    };

    const handleSubmit = async (e) => {
        e.preventDefault();

        if (!title.trim()) {
            setError("Title is required");
            return;
        }

        let form = new FormData();
        form.append("title", title);
        form.append("description", description);
        form.append("visibility", visibility);
        form.append("isCommentLocked", isCommentLocked);
        if (image) form.append("image", image);

        try {
            setLoading(true);
            let res = await authApis().post(endpoints.lesson_create, form);
            if (res.data.code === 1000) {
                const lessonId = res.data.result.id;
                nav(`/lesson/${lessonId}/flashcard`);
            } else {
                setError(res.data.message || "Unknown error occurred");
            }
        } catch (err) {
            setError(err.response?.data?.message || "Server error");
        } finally {
            setLoading(false);
        }
    };

    if (!user) return <p>You must be logged in to create a lesson.</p>;

    return (
        <div className="create-lesson-form">
            <h2>Create New Lesson</h2>
            {loading && <MySpinner />}
            {error && <p className="text-red-500">{error}</p>}

            <form onSubmit={handleSubmit}>
                <div>
                    <label>Title</label>
                    <input
                        type="text"
                        value={title}
                        onChange={(e) => setTitle(e.target.value)}
                        placeholder="Enter lesson title"
                        required
                    />
                </div>

                <div className="flex space-x-4">
                    <div>
                        <label>Visibility</label>
                        <select value={visibility} onChange={(e) => setVisibility(e.target.value)}>
                            <option value="PUBLIC">Public</option>
                            <option value="PRIVATE">Private</option>
                        </select>
                    </div>

                    <div>
                        <label>Comment</label>
                        <select value={isCommentLocked} onChange={(e) => setIsCommentLocked(e.target.value === "true")}>
                            <option value={false}>Enable</option>
                            <option value={true}>Disable</option>
                        </select>
                    </div>
                </div>

                <div className="w-full mb-4">
                    <label className="block mb-1 font-medium text-white">Description</label>
                    <textarea
                        value={description}
                        onChange={(e) => setDescription(e.target.value)}
                        placeholder="Enter description"
                        rows={4}
                        className="w-full p-3 rounded bg-[#1a1a1a] text-white border border-gray-600 focus:outline-none focus:ring-2 focus:ring-green-500 resize-y min-h-[100px]"
                    />
                </div>



                <div>
                    <label htmlFor="image-upload" className="upload-label">
                        <FaUpload /> Upload Image
                    </label>
                    <input
                        type="file"
                        id="image-upload"
                        style={{ display: "none" }}
                        onChange={handleImageChange}
                    />
                    {image && <p>{image.name}</p>}
                </div>

                <button type="submit" className="btn btn-primary mt-4">
                    Next
                </button>
            </form>
        </div>
    );
};

export default CreateLesson;
