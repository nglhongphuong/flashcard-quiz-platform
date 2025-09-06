import React, { useEffect, useState, useRef, useCallback } from "react";
import moment from "moment";
import Apis, { authApis, endpoints } from "../../configs/Apis";
import { FiMoreVertical } from "react-icons/fi"; // icon 3 chấm
import { FaTrash, FaEdit } from "react-icons/fa";
import "./Comment.css";

const Comment = ({ lessonId, comments, setComments, user, onRequireLogin }) => {
  const [newComment, setNewComment] = useState("");
  const [editingId, setEditingId] = useState(null);
  const [editContent, setEditContent] = useState("");
  const [menuOpenId, setMenuOpenId] = useState(null);
  const [sortOrder, setSortOrder] = useState("desc");
  const [comment, setComment] = useState([]);
  const [page, setPage] = useState(1);
  const [hasMore, setHasMore] = useState(true); // còn dữ liệu để load không


  const loadComment = async (pageNumber, order = sortOrder) => {
    try {
      const res = await Apis.get(
        `${endpoints.lesson}${lessonId}/comment?page=${pageNumber}&sort=${order}`
      );
      if (res.data?.code === 1000) {
        if (res.data.result.length > 0) {
          setComment((prev) => [...prev, ...res.data.result]);
        } else {
          setHasMore(false);
        }
      }
    } catch (err) {
      console.error("Error loading comments:", err);
    }
  };

  const handleScroll = (e) => {
    const { scrollTop, scrollHeight, clientHeight } = e.target;
    if (scrollTop + clientHeight >= scrollHeight - 10 && hasMore) {
      setPage((prev) => prev + 1);
    }
  };





  // Push bình luận mới
  const handlePostComment = async () => {
    if (!user) return onRequireLogin();
    if (!newComment.trim()) return;

    try {
      const res = await authApis().post(
        `${endpoints.lesson}${lessonId}/comment`,
        { content: newComment.trim() }
      );
      if (res.data?.code === 1000) {
        setNewComment("");
        setPage(1);
        setComment([]);
        setHasMore(true);
        loadComment(1, sortOrder);
      }
    } catch (err) {
      console.error("Error posting comment:", err);
    }
  };


  // Xóa comment
  const handleDeleteComment = async (commentId) => {
    try {
      await authApis().delete(endpoints.comment(commentId));
      setComment((prev) => prev.filter((c) => c.id !== commentId));
    } catch (err) {
      console.error("Error deleting comment:", err);
    }
  };

  // Chỉnh sửa comment
  const handleUpdateComment = async (commentId) => {
    if (!editContent.trim()) return;
    try {
      const res = await authApis().put(endpoints.comment(commentId), {
        content: editContent.trim(),
      });
      if (res.data?.code === 1000) {
        setComment((prev) =>
          prev.map((c) =>
            c.id === commentId ? { ...c, content: editContent } : c
          )
        );
        setEditingId(null);
      }
    } catch (err) {
      console.error("Error updating comment:", err);
    }
  };

  // Load lần đầu và khi sortOrder thay đổi
  useEffect(() => {
    setPage(1);
    setComment([]); // reset
    setHasMore(true);
    loadComment(1, sortOrder);
  }, [sortOrder]);

  // Scroll load thêm
  useEffect(() => {
    if (page > 1) {
      loadComment(page, sortOrder);
    }
  }, [page]);


  return (
    <div className="comment-wrapper">
      {/* Input comment */}
      <div className="comment-input-row">
        <input
          type="text"
          placeholder="Comment..."
          value={newComment}
          onChange={(e) => setNewComment(e.target.value)}
          className="comment-input"
        />
        <button onClick={handlePostComment} className="comment-submit">
          Gửi
        </button>
      </div>

      {/* Sort */}
      <div style={{ marginBottom: 10 }}>
        <select
          value={sortOrder}
          onChange={(e) => setSortOrder(e.target.value)}
        >
          <option value="desc">Newest</option>
          <option value="asc">Oldest</option>
        </select>
      </div>

      {/* List comments */}
      <div className="comment-list" onScroll={handleScroll}>
        {comment
          .slice()
          .map((cmt) => {
            const isAuthor = user && user.id === cmt.userInfo.id;
            const isLessonOwner = user && user.id === cmt.lessonId?.userId; // cần backend trả userId của lesson

            return (
              <div key={cmt.id} className="comment-item">
                {/* Avatar */}
                <div className="comment-avatar">
                  {cmt.userInfo.avatar ? (
                    <img src={cmt.userInfo.avatar} alt="avatar" />
                  ) : (
                    <div className="avatar-placeholder">
                      {cmt.userInfo.name?.charAt(0).toUpperCase()}
                    </div>
                  )}
                </div>

                {/* Nội dung */}
                <div className="comment-content-box" style={{ position: "relative", flex: 1 }}>
                  <div style={{ display: "flex", justifyContent: "space-between" }}>
                    <div className="comment-author">{cmt.userInfo.name}</div>
                    {(isAuthor || isLessonOwner) && (
                      <FiMoreVertical
                        style={{ cursor: "pointer" }}
                        onClick={() =>
                          setMenuOpenId(menuOpenId === cmt.id ? null : cmt.id)
                        }
                      />
                    )}
                  </div>

                  {editingId === cmt.id ? (
                    <>
                      <textarea
                        value={editContent}
                        onChange={(e) => setEditContent(e.target.value)}
                        style={{
                          width: "100%",
                          background: "#3a3a3a",
                          color: "white",
                          borderRadius: 6,
                          padding: 6,
                        }}
                      />
                      <div style={{ marginTop: 6, display: "flex", gap: 8 }}>
                        <button
                          onClick={() => handleUpdateComment(cmt.id)}
                          className="comment-submit"
                        >
                          Submit
                        </button>
                        <button
                          onClick={() => setEditingId(null)}
                          className="comment-submit"
                        >
                          Cancel
                        </button>
                      </div>
                    </>
                  ) : (
                    <div className="comment-content">{cmt.content}</div>
                  )}

                  <div className="comment-time">
                    {moment(cmt.createdAt).fromNow()}
                  </div>

                  {menuOpenId === cmt.id && (
                    <div
                      style={{
                        position: "absolute",
                        top: 25,
                        right: 0,
                        background: "#444",
                        borderRadius: 6,
                        padding: "6px 0",
                        display: "flex",
                        flexDirection: "column",
                        minWidth: 100,
                        zIndex: 100,
                      }}
                    >
                      {/* Kiểm tra nếu là chủ comment */}
                      {user?.result?.id === cmt.userInfo.id && (
                        <>
                          <button
                            onClick={() => {
                              setEditingId(cmt.id);
                              setEditContent(cmt.content);
                              setMenuOpenId(null);
                            }}
                            style={{
                              background: "none",
                              border: "none",
                              color: "white",
                              padding: "6px 12px",
                              textAlign: "left",
                              cursor: "pointer",
                              display: "flex",
                              alignItems: "center",
                              gap: 6,
                            }}
                          >
                            <FaEdit /> Edit
                          </button>
                          <button
                            onClick={() => handleDeleteComment(cmt.id)}
                            style={{
                              background: "none",
                              border: "none",
                              color: "#ff6666",
                              padding: "6px 12px",
                              textAlign: "left",
                              cursor: "pointer",
                              display: "flex",
                              alignItems: "center",
                              gap: 6,
                            }}
                          >
                            <FaTrash /> Delete
                          </button>
                        </>
                      )}
                    </div>
                  )}

                </div>
              </div>
            );
          })}
      </div>
    </div>
  );
};

export default Comment;
