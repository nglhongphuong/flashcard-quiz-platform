import { Container, Row, Col, Alert, Button, Form, Image } from "react-bootstrap";
import { useContext, useState } from "react";
import Apis, { authApis, endpoints } from "../../configs/Apis";
import MySpinner from "../layouts/MySpinner";
import { useNavigate } from "react-router-dom";
import cookie from "react-cookies";
import { MyDispatcherContext } from "../../configs/MyContexts";
import imageZotri from '../../Img/ZotriVerseImg.png'
import './Login.css';


const Login = () => {
    const info = [
        { title: "Email", field: "username", type: "text" },
        { title: "Password", field: "password", type: "password" }
    ];
    const [user, setUser] = useState({});
    const [msg, setMsg] = useState();
    const [loading, setLoading] = useState(false);
    const nav = useNavigate();
    const dispatch = useContext(MyDispatcherContext);

    const setState = (value, field) => {
        setUser({ ...user, [field]: value });
    };

    const login = async (e) => {
        e.preventDefault();
        setMsg(null);

        try {
            setLoading(true);

            let res = await Apis.post(endpoints.login, { ...user });
            console.log("Login response:", res.data);
            cookie.save("token", res.data.result.token);
            let u = await authApis().get(endpoints.account_info);
            console.info("Current user:", u.data);
            dispatch({
                type: "login",
                payload: u.data
            });
            console.log("Đăng nhập thành công, chuyển trang");
            nav("/");

        } catch (err) {
        console.error(err);

        if (err.response && err.response.data && err.response.data.message) {
          setMsg(err.response.data.message); // In message từ backend
        } else {
          setMsg("Login failed. Please try again.");
        }
        } finally {
            setLoading(false);
        }
    };
    return (
        <Container className="login-container">
            <Row className="justify-content-center align-items-center">
                <Col md={6} className="d-none d-md-block">
                    <Image src={imageZotri}
                        style={{
                            width: '400px',
                            height: '400px',
                            borderRadius: '5%'
                        }}
                        fluid />
                    <p className="register-image-caption text-center mt-3 text-light">
                        ZotriVerse!! Learn your way, have fun all day
                    </p>

                </Col>

                <Col md={6}>
                    <h1 className="login-title">Welcome to ZotriVerse</h1>
                    <div className="login-box">
                        <h1>Login</h1>

                        {msg && <Alert variant="danger">{msg}</Alert>}

                        <Form onSubmit={login}>
                            {info.map(i => (
                                <Form.Control
                                    value={user[i.field] || ""}
                                    onChange={e => setState(e.target.value, i.field)}
                                    className="login-input"
                                    key={i.field}
                                    type={i.type}
                                    placeholder={i.title}
                                    required
                                />
                            ))}

                            {loading ? <MySpinner /> : <Button type="submit" className="login-submit-btn">Submit</Button>}
                        </Form>

                        <div className="login-register-btn">
                            <Button variant="outline-light" onClick={() => nav("/register")}>Register</Button>
                        </div>
                    </div>
                </Col>
            </Row>
        </Container>

    );
};

export default Login;