import "./AdminLesson.css";
import { Container, Row, Col, Button, Form, Table } from "react-bootstrap";
import { useContext, useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { MyUserContext } from "../../../configs/MyContexts";
import { authApis, endpoints } from "../../../configs/Apis";
import { FaEdit, FaEye, FaTrash, FaCheckCircle, FaTimesCircle } from "react-icons/fa";

const AdminLesson = () => {
    const user = useContext(MyUserContext);
    const nav = useNavigate();
    const [loading, setLoading] = useState(false);
    const [page, setPage] = useState(1);
    const [hasMore, setHasMore] = useState(true);
    const [lessons, setLessons] = useState([]);
    const [filters, setFilters] = useState({
        search: "",
        startDate: "",
        endDate: "",
        order: "desc",
        userId: "",
    });

    const loadLessons = async () => {
        setLoading(true);
        try {
            const params = new URLSearchParams();
            params.append("page", page);
            if (filters.search) params.append("search", filters.search);
            if (filters.startDate) params.append("startDate", filters.startDate);
            if (filters.endDate) params.append("endDate", filters.endDate);
            if (filters.userId) params.append("userId", filters.userId);
            params.append("order", filters.order);

            const res = await authApis().get(`${endpoints.lesson}?${params.toString()}`);
            const newLessons = res.data;

            if (newLessons.length < 5) setHasMore(false);
            setLessons((prev) => (page === 1 ? newLessons : [...prev, ...newLessons]));
        } catch (err) {
            console.error("Error fetching lessons", err);
        } finally {
            setLoading(false);
        }
    };

    const handleFilter = () => {
        setPage(1);
        setHasMore(true);
        loadLessons();
    };

    useEffect(() => {
        loadLessons();
        // eslint-disable-next-line react-hooks/exhaustive-deps
    }, [page]);

    const deleteLesson = async (id) => {
        if (!window.confirm("Are you sure you want to delete this lesson?")) return;
        try {
            await authApis().delete(endpoints.lesson_update(id));
            setLessons((prev) => prev.filter((u) => u.id !== id));
        } catch (err) {
            console.error(err);
        }
    };

    

    if (loading && lessons.length === 0) return <p className="text-light p-3">Loading...</p>;

    return (
        <Container className="mt-3">
            <h2 className="text-light mb-3">Lesson Management</h2>

            {/* Filters */}
            <Row className="mb-3">
                <Col md={3}>
                    <Form.Control
                        type="text"
                        placeholder="Search by title..."
                        value={filters.search}
                        onChange={(e) => setFilters({ ...filters, search: e.target.value })}
                    />
                </Col>
                <Col md={2}>
                    <Form.Control
                        type="date"
                        value={filters.startDate}
                        onChange={(e) => setFilters({ ...filters, startDate: e.target.value })}
                    />
                </Col>
                <Col md={2}>
                    <Form.Control
                        type="date"
                        value={filters.endDate}
                        onChange={(e) => setFilters({ ...filters, endDate: e.target.value })}
                    />
                </Col>
                <Col md={2}>
                    <Form.Control
                        type="number"
                        placeholder="Author ID"
                        value={filters.userId}
                        onChange={(e) => setFilters({ ...filters, userId: e.target.value })}
                    />
                </Col>
                <Col md={2}>
                    <Form.Select
                        value={filters.order}
                        onChange={(e) => setFilters({ ...filters, order: e.target.value })}
                    >
                        <option value="desc">Newest</option>
                        <option value="asc">Oldest</option>
                    </Form.Select>
                </Col>
                <Col md={1}>
                    <Button variant="primary" onClick={handleFilter}>
                        Filter
                    </Button>
                </Col>
            </Row>

            {/* Table */}
            <Table striped bordered hover variant="dark" responsive>
                <thead>
                    <tr>
                        <th>ID</th>
                        <th>Image</th>
                        <th>Title</th>
                        <th>Description</th>
                        <th>Author ID</th>
                        <th>Visibility</th>
                        <th>Created At</th>
                        <th>Updated At</th>
                        <th>Actions</th>
                    </tr>
                </thead>
                <tbody>
                    {lessons.map((lesson) => (
                        <tr key={lesson.id}>
                            <td>{lesson.id}</td>
                            <td>
                                <img
                                    src={lesson.image}
                                    alt={lesson.title}
                                    style={{ width: "60px", height: "40px", objectFit: "cover" }}
                                />
                            </td>
                            <td>{lesson.title}</td>
                            <td>{lesson.description}</td>
                            <td>{lesson.userInfo?.id}</td>
                            <td>
                                {lesson.visibility === "PUBLIC" ? (
                                    <FaCheckCircle color="lightgreen" />
                                ) : (
                                    <FaTimesCircle color="red" />
                                )}
                            </td>

                            <td>{new Date(lesson.createdAt).toLocaleString()}</td>
                            <td>{new Date(lesson.updateAt).toLocaleString()}</td>
                            <td>
                                <FaEye
                                    style={{
                                        cursor: "pointer",
                                        color: "dodgerblue",
                                        marginRight: "10px",
                                    }}
                                    onClick={() => nav(`/admin/lessons/${lesson.id}`)}
                                />

                                <FaTrash
                                    style={{ cursor: "pointer", color: "red" }}
                                    onClick={() => deleteLesson(lesson.id)}
                                />
                            </td>
                        </tr>
                    ))}
                </tbody>
            </Table>

            {/* Load more */}
            {hasMore && (
                <div className="text-center">
                    <Button
                        variant="secondary"
                        disabled={loading}
                        onClick={() => setPage((prev) => prev + 1)}
                    >
                        {loading ? "Loading..." : "Load more"}
                    </Button>
                </div>
            )}
        </Container>
    );
};

export default AdminLesson;
