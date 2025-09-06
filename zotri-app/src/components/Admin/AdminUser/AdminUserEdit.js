import React, { useEffect, useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { Container, Row, Col, Form, Button, Spinner } from "react-bootstrap";
import { authApis, endpoints } from "../../../configs/Apis";
import "./AdminUserEdit.css";

const AdminUserEdit = () => {
    const { accountId } = useParams();
    const nav = useNavigate();

    const [user, setUser] = useState(null);
    const [loading, setLoading] = useState(true);
    const [saving, setSaving] = useState(false);
    const [error, setError] = useState(null);

    // State riêng cho edit
    const [image, setImage] = useState(null);
    const [isActive, setActive] = useState(false);
    const [gender, setGender] = useState("MALE");
    const [name, setName] = useState("");
    const [birthday, setBirthday] = useState("");
    const [role, setRole] = useState("USER");
    const [password, setPassword] = useState("");
    const [imagePreview, setImagePreview] = useState("");

    useEffect(() => {
        const fetchUser = async () => {
            try {
                setLoading(true);
                const params = new URLSearchParams();
                params.append("id", accountId);
                const res = await authApis().get(`${endpoints.admin_user}?${params.toString()}`);

                if (res.data && res.data.length > 0) {
                    const u = res.data[0];
                    setUser(u);

                    // Đồng bộ dữ liệu vào các state edit
                    setName(u.name || "");
                    setGender(u.gender || "MALE");
                    setRole(u.role || "USER");
                    setBirthday(u.birthday || "");
                    setActive(u.isActive || false);
                    setImage(u.avatar || "");
                    setImagePreview(u.avatar || "");
                } else {
                    setError("User not found");
                }
            } catch (err) {
                setError("Error loading user");
                console.error(err);
            } finally {
                setLoading(false);
            }
        };
        fetchUser();
    }, [accountId]);

    // Handle input change cho từng trường riêng biệt
    const handleNameChange = (e) => setName(e.target.value);
    const handleGenderChange = (e) => setGender(e.target.value);
    const handleRoleChange = (e) => setRole(e.target.value);
    const handleBirthdayChange = (e) => setBirthday(e.target.value);
    const handleIsActiveChange = (e) => setActive(e.target.checked);
    const handlePasswordChange = (e) => setPassword(e.target.value);
    const handleImageChange = (e) => {
        const file = e.target.files[0];
        if (file) {
            setImage(file);
            setImagePreview(URL.createObjectURL(file));
        }
    };

    // Submit form
    const handleSubmit = async (e) => {
        e.preventDefault();
        setSaving(true);
        setError(null);

        try {
            const form = new FormData();
            form.append("name", name);
            form.append("birthday", birthday);
            form.append("gender", gender);
            form.append("role", role);
            form.append("isActive", isActive);
            if (password) form.append("password", password);
            if (image) form.append("image", image);

            const res = await authApis().put(endpoints.admin_user_action(accountId), form);
            console.log("res:", res.data);
            alert("User updated successfully!");
            nav("/admin/users");
        } catch (err) {
            setError("Error updating user");
            console.error(err);
        } finally {
            setSaving(false);
        }
    };

    if (loading) return <Spinner animation="border" variant="light" />;

    if (error)
        return (
            <Container className="edit-account-container text-light p-3">
                <p>{error}</p>
                <Button variant="outline-light" onClick={() => nav("/admin/users")}>
                    Back to Users
                </Button>
            </Container>
        );

    return (
        <Container fluid className="edit-account-container p-3">
            <h2 className="text-light mb-4">Edit User #{accountId}</h2>
            <Form onSubmit={handleSubmit} className="text-light">

                <Row className="mb-3">
                    <Col md={6}>
                        <Form.Group controlId="formUsername">
                            <Form.Label>Username (email)</Form.Label>
                            <Form.Control
                                type="email"
                                name="username"
                                value={user?.username || ""}
                                disabled
                            />
                        </Form.Group>
                    </Col>

                    <Col md={6}>
                        <Form.Group controlId="formName">
                            <Form.Label>Name</Form.Label>
                            <Form.Control
                                type="text"
                                value={name}
                                onChange={handleNameChange}
                                required
                            />
                        </Form.Group>
                    </Col>
                </Row>

                <Row className="mb-3">
                    <Col md={4}>
                        <Form.Group controlId="formRole">
                            <Form.Label>Role</Form.Label>
                            <Form.Select value={role} onChange={handleRoleChange} required>
                                <option value="USER">User</option>
                                <option value="ADMIN">Admin</option>
                            </Form.Select>
                        </Form.Group>
                    </Col>

                    <Col md={4}>
                        <Form.Group controlId="formGender">
                            <Form.Label>Gender</Form.Label>
                            <Form.Select value={gender} onChange={handleGenderChange} required>
                                <option value="MALE">Male</option>
                                <option value="FEMALE">Female</option>
                                <option value="OTHER">Other</option>
                            </Form.Select>
                        </Form.Group>
                    </Col>

                    <Col md={4} className="d-flex align-items-center">
                        <Form.Group controlId="formIsActive" className="mb-0">
                            <Form.Check
                                type="checkbox"
                                label="Active"
                                checked={isActive}
                                onChange={handleIsActiveChange}
                                style={{ color: "white" }}
                            />
                        </Form.Group>
                    </Col>
                </Row>

                <Row className="mb-3">
                    <Col md={6}>
                        <Form.Group controlId="formBirthday">
                            <Form.Label>Birthday</Form.Label>
                            <Form.Control
                                type="date"
                                value={birthday}
                                onChange={handleBirthdayChange}
                            />
                        </Form.Group>
                    </Col>

                    <Col md={6}>
                        <Form.Group controlId="formPassword">
                            <Form.Label>Password (leave blank to keep current)</Form.Label>
                            <Form.Control
                                type="password"
                                value={password}
                                onChange={handlePasswordChange}
                                placeholder="New password"
                            />
                        </Form.Group>
                    </Col>
                </Row>

                <Row className="mb-3">
                    <Col md={6}>
                        <Form.Group controlId="formAvatar">
                            <Form.Label>Avatar</Form.Label>
                            <Form.Control
                                type="file"
                                accept="image/*"
                                onChange={handleImageChange}
                            />
                            {imagePreview && (
                                <img
                                    src={imagePreview}
                                    alt="avatar preview"
                                    style={{ maxWidth: "150px", marginTop: "10px", borderRadius: "5px" }}
                                />
                            )}
                        </Form.Group>
                    </Col>
                </Row>

                <div className="edit-account-buttons">
                    <Button type="submit" variant="outline-light" disabled={saving}>
                        {saving ? "Saving..." : "Save Changes"}
                    </Button>
                    <Button
                        variant="outline-secondary"
                        onClick={() => nav("/admin/users")}
                        disabled={saving}
                    >
                        Cancel
                    </Button>
                </div>


                {error && <p className="mt-3 text-danger">{error}</p>}
            </Form>
        </Container>
    );
};

export default AdminUserEdit;
