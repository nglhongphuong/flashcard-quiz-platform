import { Container, Row, Col, Alert, Button, Form, Image, Toast } from "react-bootstrap";
import { useRef, useState } from "react";
import Apis, { endpoints } from "../../configs/Apis";
import MySpinner from "../layouts/MySpinner";
import { useNavigate } from "react-router-dom";
import './Register.css';
import imageRegister from '../../Img/register.png'

const Register = () => {
  const fields = [
    { title: "Email", field: "username", type: "username" },
    { title: "Password", field: "password", type: "password" },
    { title: "Confirm Password", field: "confirm", type: "password" },
      { title: "Display Name", field: "name", type: "text" },
    { title: "Birthday", field: "birthday", type: "date" },
    { title: "Gender", field: "gender", type: "select", options: ["MALE", "FEMALE"] }
  ];


  const [user, setUser] = useState({ role: "USER", active: false });
  const avatar = useRef();
  const coverPhoto = useRef();
  const [msg, setMsg] = useState();
  const [loading, setLoading] = useState(false);
  const nav = useNavigate();

  const setState = (value, field) => {
    setUser({ ...user, [field]: value });
  };

  const register = async (e) => {
    e.preventDefault();
    if (user.password !== user.confirm) {
      setMsg("Mật khẩu KHÔNG khớp");
    } else {
      let form = new FormData();
      for (let key in user) {
        if (key !== "confirm") {
          form.append(key, user[key]);
        }
      }
      form.append("avatar", avatar.current.files[0]);

      try {
        setLoading(true);


        const res = await Apis.post(endpoints.register, form, {
          headers: { "Content-Type": "multipart/form-data" }
        });

        console.log("Thông tin user vừa tạo:", res.data);

        nav("/");
      } catch (err) {
        console.error(err);

        if (err.response && err.response.data && err.response.data.message) {
          setMsg(err.response.data.message); // In message từ backend
        } else {
          setMsg("Registeration failed. Please try again.");
        }
      } finally {
        setLoading(false);
      }

      
    }
  };

  return (
    <Container className="register-container">
      <Row className="justify-content-center align-items-center">

        {/* Bên trái: Ảnh minh họa */}
        <Col md={6} className="d-none d-md-block register-image-section">
          <Image src={imageRegister}
            style={{
              width: '400px',
              height: '400px',
              borderRadius: '5%'
            }}
            fluid />
          <h2 className="register-headline">Join ZotriVerse and Learn with Flashcards the Easy Way!</h2>
          <p className="register-subtext">Fun, fast, and effective — your study journey starts here.</p>

        </Col>

        {/* Bên phải: Form đăng ký */}
        <Col md={5}>
          <div className="register-form-wrapper">
            <h1 className="register-title">Register Account</h1>

            {msg && <Alert variant="danger">{msg}</Alert>}

            <Form onSubmit={register} className="register-form">
              {fields.map(f =>
                f.type === "select" ? (
                  <Form.Select
                    key={f.field}
                    value={user[f.field] || ""}
                    onChange={e => setState(e.target.value, f.field)}
                    required
                  >
                    <option value="">-- {f.title.toLowerCase()} --</option>
                    {f.options.map(opt => (
                      <option key={opt} value={opt}>
                        {opt === "MALE" ? "Male" : "Female"}
                      </option>
                    ))}
                  </Form.Select>
                ) : (
                  <Form.Control
                    key={f.field}
                    value={user[f.field] || ""}
                    onChange={e => setState(e.target.value, f.field)}
                    type={f.type}
                    placeholder={f.title}
                    required
                  />
                )
              )}

              <Form.Label>Avatar</Form.Label>
              <Form.Control ref={avatar} type="file" className="mb-3" required />

              {loading ? (
                <MySpinner />
              ) : (
                <Button type="submit" variant="success" className="register-button">
                  Submit
                </Button>
              )}
            </Form>
          </div>
        </Col>
      </Row>
    </Container>
  );

};

export default Register;