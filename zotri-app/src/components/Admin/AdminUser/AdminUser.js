//goi authApis để lấy danh sách user 
// admin_user: `/admin/accounts`, // lấy danh sách 
// admin_user_action: (accountId) => `/admin/accounts/${accountId}`, // update, put, xóa lun

// Sẽ có 2 phần, 
// 1 là thanh tìm kiếm user name, truy vấn ns chung là 1 hàng filter,...
// 2 là danh sách thông tin user, cuối mỗi dòng sẽ có 2 button là put hoặc delete
// khi nhấn update thì chuyển qua link nav  /admin/account/id chỉnh sửa -> AdminUserEdit.js
// Khi nhấn xóa thì gọi  authApis.delete, xong update state
// public List<AccountResponse> getAccounts(Map<String, String> params) {
//     CriteriaBuilder builder = entityManager.getCriteriaBuilder();
//     CriteriaQuery<Account> query = builder.createQuery(Account.class);
//     Root<Account> root = query.from(Account.class);
//     query.select(root);

//     List<Predicate> predicates = new ArrayList<>();

//     // Filter theo id
//     if (params.containsKey("id")) {
//         try {
//             int id = Integer.parseInt(params.get("id"));
//             predicates.add(builder.equal(root.get("id"), id));
//         } catch (NumberFormatException ignored) {}
//     }

//     // Filter theo username
//     if (params.containsKey("username")) {
//         String username = params.get("username");
//         if (!username.isEmpty()) {
//             predicates.add(builder.like(root.get("username"), "%" + username + "%"));
//         }
//     }

//     // Filter theo role
//     if (params.containsKey("role")) {
//         String role = params.get("role");
//         if (!role.isEmpty()) {
//             predicates.add(builder.equal(root.get("role"), role));
//         }
//     }

//     // Filter theo isActive
//     if (params.containsKey("isActive")) {
//         String isActive = params.get("isActive");
//         if (!isActive.isEmpty()) {
//             predicates.add(builder.equal(root.get("isActive"), Boolean.parseBoolean(isActive)));
//         }
//     }

//     // Filter theo khoảng thời gian createdAt
//     DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

//     if (params.containsKey("startDate")) {
//         try {
//             LocalDateTime start = LocalDate.parse(params.get("startDate"), formatter).atStartOfDay();
//             predicates.add(builder.greaterThanOrEqualTo(root.get("createdAt"), start));
//         } catch (DateTimeParseException ignored) {}
//     }

//     if (params.containsKey("endDate")) {
//         try {
//             LocalDateTime end = LocalDate.parse(params.get("endDate"), formatter).atTime(LocalTime.MAX);
//             predicates.add(builder.lessThanOrEqualTo(root.get("createdAt"), end));
//         } catch (DateTimeParseException ignored) {}
//     }

//     query.where(predicates.toArray(new Predicate[0]));

//     // Order
//     String order = params.getOrDefault("order", "desc");
//     if (order.equalsIgnoreCase("asc")) {
//         query.orderBy(builder.asc(root.get("createdAt")));
//     } else {
//         query.orderBy(builder.desc(root.get("createdAt")));
//     }

//     TypedQuery<Account> q = entityManager.createQuery(query);

//     // Phân trang
//     int page = 1;
//     int pageSize = PAGE_SIZE;

//     if (params.containsKey("page")) {
//         page = Integer.parseInt(params.get("page"));
//     }

//     if (params.containsKey("size")) {
//         pageSize = Integer.parseInt(params.get("size"));
//     }

//     q.setFirstResult((page - 1) * pageSize);
//     q.setMaxResults(pageSize);

//     return q.getResultList().stream().map(accountMapper::toAccountResponse).toList();
// }
//KẾT QUẢ TRẢ VỀ TRUY VẤN GET ADMIN/ACCOUNT



// {
//     "id": 32,
//     "username": "hahah@gmail.com",
//     "password": "$2a$10$1XFT8b2uu0nwLqdzyv1UTOQSSIhL8BgHF7clYNL7Y0FVl2ceF30Ae",
//     "name": "Muhahaha 1",
//     "role": "USER",
//     "gender": "FEMALE",
//     "createdAt": "2025-08-03T08:55:59.000+00:00",
//     "updateAt": "2025-08-03T10:55:41.000+00:00",
//     "avatar": "http://res.cloudinary.com/dnc5sycvb/image/upload/v1754218541/avatars/sqy12ckaalkgawmcqftb.jpg",
//     "isActive": false,
//     "birthday": "2025-08-18"
// },
// {
//     "id": 31,
//     "username": "123333",
//     "password": "$2a$10$qY4iAJSU5jBFTEl2joFB8.kxxSf1snH8xbfz1GQUcz9.ZDpOlesS6",
//     "name": "eeeee",
//     "role": "USER",
//     "gender": "MALE",
//     "createdAt": "2025-08-03T05:57:50.000+00:00",
//     "updateAt": "2025-08-03T05:57:50.000+00:00",
//     "avatar": "http://res.cloudinary.com/dnc5sycvb/image/upload/v1754200670/avatars/xkfqhcil6bbooednpbht.jpg",
//     "isActive": false,
//     "birthday": "2025-08-25"
// },
// {
//     "id": 30,
//     "username": "nglhongphuong@gmail.com",
//     "password": "$2a$10$brwTBrx3SRo8jz1T02CfXuUypcTLu/w.PTDAUK3vOP0xgKVtMjddS",
//     "name": "tesst thu ",
//     "role": "USER",
//     "gender": "FEMALE",
//     "createdAt": "2025-08-01T13:40:20.000+00:00",
//     "updateAt": "2025-08-03T09:43:07.000+00:00",
//     "avatar": "http://res.cloudinary.com/dnc5sycvb/image/upload/v1754055622/avatars/xpdbu67toc58hxmwsuv8.jpg",
//     "isActive": false,
//     "birthday": "2004-07-23"
// },

import "./AdminUser.css";
import { Container, Row, Col, Button, Form, Table } from "react-bootstrap";
import { useContext, useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { MyUserContext } from "../../../configs/MyContexts";
import { authApis, endpoints } from "../../../configs/Apis";
import { FaEdit, FaTrash, FaCheckCircle, FaTimesCircle } from "react-icons/fa";
import { Modal } from "react-bootstrap";

const AdminUser = () => {
    const user = useContext(MyUserContext);
    const nav = useNavigate();
    const [users, setUsers] = useState([]);
    const [username, setUsername] = useState("");
    const [loading, setLoading] = useState(false);
    const [page, setPage] = useState(1);
    const [hasMore, setHasMore] = useState(true);

    const [filters, setFilters] = useState({
        username: null,
        startDate: null,
        endDate: null,
        order: "desc",
        isActive: null,
        role: null
    });

    const [showAddUser, setShowAddUser] = useState(false);
    const [newUser, setNewUser] = useState({
        username: "",
        password: "",
        confirm: "",
        name: "",
        birthday: "",
        gender: "MALE",
        role: "USER",
        isActive: "true", // mặc định active
        image: null
    });

    const fields = [
        { title: "Email", field: "username", type: "email" },
        { title: "Password", field: "password", type: "password" },
        { title: "Confirm Password", field: "confirm", type: "password" },
        { title: "Display Name", field: "name", type: "text" },
        { title: "Birthday", field: "birthday", type: "date" },
        { title: "Gender", field: "gender", type: "select", options: ["MALE", "FEMALE"] },
        { title: "Role", field: "role", type: "select", options: ["USER", "ADMIN"] },
        { title: "isActive", field: "isActive", type: "select", options: ["true", "false"] },
          { title: "Profile Image", field: "image", type: "file" }
    ];

    const handleAddUser = async (e) => {
        e.preventDefault();
        if (newUser.password !== newUser.confirm) {
            alert("Passwords do not match!");
            return;
        }
        try {

            const formData = new FormData();
            formData.append("username", newUser.username);
            formData.append("password", newUser.password);
            formData.append("name", newUser.name);
            formData.append("birthday", newUser.birthday);
            formData.append("gender", newUser.gender);
            formData.append("role", newUser.role);
            formData.append("isActive", newUser.isActive === "true"); // convert boolean
            if(newUser.image) formData.append("image", newUser.image);

            const res = await authApis().post(endpoints.register, formData);
            setUsers((prev) => [res.data, ...prev]);
            alert("User created successfully!");
            setShowAddUser(false);
            setPage(1);
            loadUsers();
        } catch (err) {
            console.error(err);
            alert("Error creating user");
        }
    };

    const loadUsers = async () => {
        try {
            setLoading(true);
            const params = new URLSearchParams();
            params.append("page", page);
            params.append("order", filters.order);
            if (filters.username) params.append("username", filters.username);
            if (filters.role) params.append("role", filters.role);
            if (filters.isActive !== null && filters.isActive !== undefined)
                params.append("isActive", filters.isActive);
            if (filters.startDate) params.append("startDate", filters.startDate);
            if (filters.endDate) params.append("endDate", filters.endDate);

            let res = await authApis().get(`${endpoints.admin_user}?${params.toString()}`);
            console.log("info: ", res.data);

            // Giả sử res.data là mảng user, còn nếu API trả về { data: [], total: x } thì bạn phải điều chỉnh
            if (page === 1) {
                setUsers(res.data);
            } else {
                setUsers(prev => [...prev, ...res.data]);
            }

            // Nếu số lượng trả về nhỏ hơn 1 trang, không còn dữ liệu nữa
            if (res.data.length === 0) {
                setHasMore(false);
            } else {
                setHasMore(true);
            }

        } catch (err) {
            console.error(err);
        } finally {
            setLoading(false);
        }
    };

    // loadUsers chạy khi filters hoặc page thay đổi
    useEffect(() => {
        loadUsers();
    }, [filters, page]);

    const searchHandler = (e) => {
        e.preventDefault();
        setFilters(prev => ({
            ...prev,
            username: username || null
        }));
        setPage(1); // reset page về 1 để load lại từ đầu
    };

    const deleteUser = async (id) => {
        if (!window.confirm("Are you sure you want to delete this user?")) return;
        try {
            await authApis().delete(endpoints.admin_user_action(id));
            setUsers(prev => prev.filter(u => u.id !== id));
        } catch (err) {
            console.error(err);
        }
    };

    if (loading && users.length === 0) return <p className="text-light p-3">Loading...</p>;

    return (
        <Container fluid className="admin-user-container">
            <Row className="mb-3">
                <Col>
                    <h2 className="text-light">User Management</h2>
                </Col>
                <Col md="auto">
                    <Button variant="success" onClick={() => setShowAddUser(true)}>
                        Add User
                    </Button>
                </Col>
            </Row>

            {/* Search bar */}
            <Row className="mb-3" style={{ gap: '8px' }}>
                <Col md="auto" style={{ flex: '1 1 200px' }}>
                    <Form.Control
                        type="text"
                        placeholder="Search by username..."
                        value={username}
                        onChange={(e) => setUsername(e.target.value)}
                    />
                </Col>

                <Col md="auto" style={{ minWidth: '150px' }}>
                    <Form.Select
                        value={filters.role || ""}
                        onChange={(e) =>
                            setFilters((prev) => ({
                                ...prev,
                                role: e.target.value || null,
                            }))
                        }
                    >
                        <option value="">All Roles</option>
                        <option value="admin">Admin</option>
                        <option value="user">User</option>
                        {/* Thêm role khác nếu có */}
                    </Form.Select>
                </Col>

                <Col md="auto" style={{ minWidth: '120px' }}>
                    <Form.Select
                        value={filters.isActive === null ? "" : filters.isActive ? "true" : "false"}
                        onChange={(e) => {
                            let val = e.target.value;
                            setFilters((prev) => ({
                                ...prev,
                                isActive: val === "" ? null : val === "true",
                            }));
                        }}
                    >
                        <option value="">All Status</option>
                        <option value="true">Active</option>
                        <option value="false">Inactive</option>
                    </Form.Select>
                </Col>

                <Col md="auto" style={{ minWidth: '150px' }}>
                    <Form.Control
                        type="date"
                        value={filters.startDate || ""}
                        onChange={(e) =>
                            setFilters((prev) => ({
                                ...prev,
                                startDate: e.target.value || null,
                            }))
                        }
                        placeholder="Start Date"
                    />
                </Col>

                <Col md="auto" style={{ minWidth: '150px' }}>
                    <Form.Control
                        type="date"
                        value={filters.endDate || ""}
                        onChange={(e) =>
                            setFilters((prev) => ({
                                ...prev,
                                endDate: e.target.value || null,
                            }))
                        }
                        placeholder="End Date"
                    />
                </Col>

                <Col md="auto" style={{ minWidth: '120px' }}>
                    <Form.Select
                        value={filters.order}
                        onChange={(e) =>
                            setFilters((prev) => ({
                                ...prev,
                                order: e.target.value,
                            }))
                        }
                    >
                        <option value="desc">Newest</option>
                        <option value="asc">Oldest</option>
                    </Form.Select>
                </Col>

                <Col md="auto">
                    <Button variant="outline-light" onClick={() => {
                        setFilters((prev) => ({ ...prev, username: username || null }));
                        setPage(1);
                    }}>
                        Search
                    </Button>
                </Col>
            </Row>

            {/* Modal thêm user */}
            <Modal
                show={showAddUser}
                onHide={() => setShowAddUser(false)}
                centered
                dialogClassName="admin-user-modal"
            >
                <Modal.Header closeButton className="admin-user-modal-header">
                    <Modal.Title>Add New User</Modal.Title>
                </Modal.Header>
                <Modal.Body className="admin-user-modal-body">
                    <Form onSubmit={handleAddUser}>
                        {fields.map((f) => (
                            <Form.Group className="mb-3" key={f.field}>
                                <Form.Label>{f.title}</Form.Label>
                                {f.type === "select" ? (
                                    <Form.Select
                                        value={newUser[f.field]}
                                        onChange={(e) => setNewUser({ ...newUser, [f.field]: e.target.value })}
                                    >
                                        {f.options.map((opt) => (
                                            <option key={opt} value={opt}>{opt}</option>
                                        ))}
                                    </Form.Select>
                                ) : (
                                    <Form.Control
                                        type={f.type}
                                        value={newUser[f.field]}
                                        onChange={(e) => setNewUser({ ...newUser, [f.field]: e.target.value })}
                                        required
                                    />
                                )}
                            </Form.Group>
                        ))}
                        <div className="text-end">
                            <Button variant="secondary" onClick={() => setShowAddUser(false)} className="me-2">
                                Cancel
                            </Button>
                            <Button variant="success" type="submit">
                                Save
                            </Button>
                        </div>
                    </Form>
                </Modal.Body>
            </Modal>



            {/* User Table */}
            <Row>
                <Col>
                    <Table striped bordered hover responsive variant="dark" className="align-middle">
                        <thead>
                            <tr>
                                <th>ID</th>
                                <th>Username</th>
                                <th>Created Date</th>
                                <th>Updated Date</th>
                                <th>Active</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody>
                            {users.length === 0 ? (
                                <tr>
                                    <td colSpan={6} className="text-center text-muted">No data available</td>
                                </tr>
                            ) : (
                                users.map((u) => (
                                    <tr key={u.id}>
                                        <td>{u.id}</td>
                                        <td>{u.username}</td>
                                        <td>{new Date(u.createdAt).toLocaleString()}</td>
                                        <td>{new Date(u.updateAt).toLocaleString()}</td>
                                        <td>
                                            {u.isActive
                                                ? <FaCheckCircle color="lightgreen" />
                                                : <FaTimesCircle color="red" />
                                            }
                                        </td>

                                        <td>
                                            <Button
                                                variant="outline-info"
                                                size="sm"
                                                className="me-2"
                                                onClick={() => nav(`/admin/account/${u.id}`)}
                                            >
                                                <FaEdit />
                                            </Button>
                                            <Button
                                                variant="outline-danger"
                                                size="sm"
                                                onClick={() => deleteUser(u.id)}
                                            >
                                                <FaTrash />
                                            </Button>
                                        </td>
                                    </tr>
                                ))
                            )}
                        </tbody>
                    </Table>

                    {/* Load More Button */}
                    {users.length > 0 && (
                        <div className="text-center mt-3">
                            <Button
                                variant="outline-light"
                                onClick={() => setPage((prev) => prev + 1)}
                            >
                                Load More
                            </Button>
                        </div>
                    )}
                </Col>
            </Row>
        </Container>
    );
};

export default AdminUser;
