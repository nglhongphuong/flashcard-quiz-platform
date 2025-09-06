import { useContext, useEffect, useState } from "react";
import { Button, Card, Col, Container, Form, Image, Alert, Row } from "react-bootstrap";
import { FaEdit, FaSave } from "react-icons/fa";
import Apis, { authApis, endpoints } from "../../configs/Apis";
import { MyDispatcherContext, MyUserContext } from "../../configs/MyContexts";
import "./Profile.css";
import 'bootstrap-icons/font/bootstrap-icons.css';
import MySpinner from "../layouts/MySpinner";

const Profile = () => {
    const user = useContext(MyUserContext);
    const dispatch = useContext(MyDispatcherContext);
    const [editing, setEditing] = useState(false);
    const [avatarFile, setAvatarFile] = useState(null);
    const [msg, setMsg] = useState();
    const [msgVariant, setMsgVariant] = useState("danger");
    const [loading, setLoading] = useState(false);
    const [profile, setProfile] = useState({
        username: "",
        password: "",
        name: "",
        birthday: "",
        gender: "MALE",
        role: "",
    });
    useEffect(() => {
        if (user && user.result) {
            setProfile({
                username: user.result.username || "",
                password: "",
                name: user.result.name || "",
                birthday: user.result.birthday || "",
                gender: user.result.gender || "MALE",
                role: user.result.role || "",
            });
        }
    }, [user]);
    const handleChange = (e) => {
        const { name, value } = e.target;
        setProfile({ ...profile, [name]: value });
    };
    //ẩn thông báo 4s
    useEffect(() => {
        if (msg) {
            const timer = setTimeout(() => {
                setMsg(null);
            }, 4000);
            return () => clearTimeout(timer);
        }
    }, [msg]);

    const handleSave = async () => {
        try {
            const formData = new FormData();
            if (profile.password && profile.password.trim() !== "") {
                formData.append("password", profile.password);
            }
            formData.append("name", profile.name);
            formData.append("birthday", profile.birthday);
            formData.append("gender", profile.gender);
            if (avatarFile) formData.append("avatar", avatarFile);
            const res = await authApis().put(endpoints.account, formData);
            const userRes = await authApis().get(endpoints.account_info);
            dispatch({
                type: "login",
                payload: userRes.data
            });

            console.log("res: ", res);
            setEditing(false);
            setMsg("Upload Successfully !");
            setMsgVariant("success");
        } catch (err) {
            console.error(err);
            if (err.response?.data?.message) {
                setMsg(err.response.data.message);
            } else {
                setMsg("Update failed. Please try again.");
            }
            setMsgVariant("danger");
        }
        finally {
            setLoading(false);
        }

    };
    // return (
    //     <Container className="mt-5">
    //         <Card className="shadow p-4">
    //             {msg && <Alert variant={msgVariant}>{msg}</Alert>}

    //             <Row className="align-items-center">
    //                 <Col md={4} className="text-center">
    //                     <div className="avatar-container">
    //                         <Image
    //                             src={user?.result?.avatar || "https://i.pinimg.com/1200x/e8/d7/d0/e8d7d05f392d9c2cf0285ce928fb9f4a.jpg"}
    //                             roundedCircle
    //                             width={150}
    //                             height={150}
    //                             className="avatar-img"
    //                         />
    //                         {editing && (
    //                             <>
    //                                 <label htmlFor="avatar-upload" className="avatar-edit-icon">
    //                                     <i className="bi bi-camera-fill"></i>
    //                                 </label>
    //                                 <input
    //                                     id="avatar-upload"
    //                                     className="d-none"
    //                                     type="file"
    //                                     accept="image/*"
    //                                     onChange={(e) => setAvatarFile(e.target.files[0])}
    //                                 />
    //                             </>
    //                         )}

    //                     </div>
    //                     <h2 className="fw-bold mt-3">{profile.name || "Your Name"}</h2>
    //                 </Col>
    //                 <Col md={8}>
    //                     <Form>
    // <Form.Group className="mb-2">
    //     <Form.Label>Email</Form.Label>
    //     <Form.Control
    //         type="email"
    //         name="username"
    //         value={profile.username}
    //         disabled
    //         onChange={handleChange}
    //     />
    // </Form.Group>
    // <Form.Group className="mb-2">
    //     <Form.Label>Password</Form.Label>
    //     <Form.Control
    //         type="password"
    //         name="password"
    //         value={profile.password}
    //         disabled={!editing}
    //         onChange={handleChange}
    //     />
    // </Form.Group>
    // <Form.Group className="mb-2">
    //     <Form.Label>Display Name</Form.Label>
    //     <Form.Control
    //         type="text"
    //         name="name"
    //         value={profile.name}
    //         disabled={!editing}
    //         onChange={handleChange}
    //     />
    // </Form.Group>
    // <Form.Group className="mb-2">
    //     <Form.Label>Birthday</Form.Label>
    //     <Form.Control
    //         type="date"
    //         name="birthday"
    //         value={profile.birthday}
    //         disabled={!editing}
    //         onChange={handleChange}
    //     />
    // </Form.Group>
    // <Form.Group className="mb-2">
    //     <Form.Label>Gender</Form.Label>
    //     <Form.Select
    //         name="gender"
    //         value={profile.gender}
    //         disabled={!editing}
    //         onChange={handleChange}
    //     >
    //         <option value="MALE">Male</option>
    //         <option value="FEMALE">Female</option>
    //     </Form.Select>
    // </Form.Group>
    // <Form.Group className="mb-3">
    //     <Form.Label>Role</Form.Label>
    //     <Form.Control
    //         type="text"
    //         name="role"
    //         value={profile.role}
    //         disabled
    //     />
    // </Form.Group>
    //                         {!editing ? (
    //                             <Button variant="warning" onClick={() => setEditing(true)}>
    //                                 <FaEdit className="me-2" /> Edit
    //                             </Button>
    //                         ) : (
    //                             <Button variant="success" onClick={handleSave}>
    //                                 <FaSave className="me-2" /> Save
    //                             </Button>
    //                         )}
    //                     </Form>
    //                 </Col>
    //             </Row>
    //         </Card>
    //     </Container>
    // );

    return (
        loading ? (
            <Container className="mt-5 text-center">
                <MySpinner />
            </Container>
        ) : (
            <Container className="mt-5">
                <Card className="shadow p-4">
                    {msg && <Alert variant={msgVariant}>{msg}</Alert>}

                    <Row className="align-items-center">
                        <Col md={4} className="text-center">
                            <div className="avatar-container">
                                <Image
                                    src={user?.result?.avatar || "https://i.pinimg.com/1200x/e8/d7/d0/e8d7d05f392d9c2cf0285ce928fb9f4a.jpg"}
                                    roundedCircle
                                    width={150}
                                    height={150}
                                    className="avatar-img"
                                />
                                {editing && (
                                    <>
                                        <label htmlFor="avatar-upload" className="avatar-edit-icon">
                                            <i className="bi bi-camera-fill"></i>
                                        </label>
                                        <input
                                            id="avatar-upload"
                                            className="d-none"
                                            type="file"
                                            accept="image/*"
                                            onChange={(e) => setAvatarFile(e.target.files[0])}
                                        />
                                    </>
                                )}
                            </div>
                            <h2 className="fw-bold mt-3">{profile.name || "Your Name"}</h2>
                        </Col>
                        <Col md={8}>
                            <Form>
                                <Form.Group className="mb-2">
                                    <Form.Label>Email</Form.Label>
                                    <Form.Control
                                        type="email"
                                        name="username"
                                        value={profile.username}
                                        disabled
                                        onChange={handleChange}
                                    />
                                </Form.Group>
                                <Form.Group className="mb-2">
                                    <Form.Label>Password</Form.Label>
                                    <Form.Control
                                        type="password"
                                        name="password"
                                        value={profile.password}
                                        disabled={!editing}
                                        onChange={handleChange}
                                    />
                                </Form.Group>
                                <Form.Group className="mb-2">
                                    <Form.Label>Display Name</Form.Label>
                                    <Form.Control
                                        type="text"
                                        name="name"
                                        value={profile.name}
                                        disabled={!editing}
                                        onChange={handleChange}
                                    />
                                </Form.Group>
                                <Form.Group className="mb-2">
                                    <Form.Label>Birthday</Form.Label>
                                    <Form.Control
                                        type="date"
                                        name="birthday"
                                        value={profile.birthday}
                                        disabled={!editing}
                                        onChange={handleChange}
                                    />
                                </Form.Group>
                                <Form.Group className="mb-2">
                                    <Form.Label>Gender</Form.Label>
                                    <Form.Select
                                        name="gender"
                                        value={profile.gender}
                                        disabled={!editing}
                                        onChange={handleChange}
                                    >
                                        <option value="MALE">Male</option>
                                        <option value="FEMALE">Female</option>
                                    </Form.Select>
                                </Form.Group>
                                <Form.Group className="mb-3">
                                    <Form.Label>Role</Form.Label>
                                    <Form.Control
                                        type="text"
                                        name="role"
                                        value={profile.role}
                                        disabled
                                    />
                                </Form.Group>
                                {!editing ? (
                                    <Button variant="warning" onClick={() => setEditing(true)}>
                                        <FaEdit className="me-2" /> Edit
                                    </Button>
                                ) : (
                                    <Button variant="success" onClick={() => {
                                        setLoading(true);
                                        handleSave();
                                    }}>
                                        <FaSave className="me-2" /> Save
                                    </Button>
                                )}
                            </Form>
                        </Col>
                    </Row>
                </Card>
            </Container>
        )
    );

};

export default Profile;
