import React, { useState, useContext } from "react";
import { Link, useNavigate } from "react-router-dom";
import { FaUsers, FaHome,FaChartBar, FaPlusCircle, FaBars, FaUser, FaSignOutAlt, FaBook, FaSignInAlt } from "react-icons/fa";
import "./Sidebar.css";
import { MyUserContext, MyDispatcherContext } from "../../configs/MyContexts";
import { GiNotebook } from "react-icons/gi";


const Sidebar = () => {
    const [collapsed, setCollapsed] = useState(false);
    const user = useContext(MyUserContext);
    const dispatch = useContext(MyDispatcherContext);
    const nav = useNavigate();

    const handleLogout = () => {
        dispatch({ type: "logout" });
        nav("/");
    };

    return (
        <div className={`sidebar ${collapsed ? "collapsed" : ""}`}>
            <div className="top-section">
                <button className="toggle-btn" onClick={() => setCollapsed(!collapsed)}>
                    <FaBars />
                </button>
            </div>
            {user && (
                <Link to="/profile" className="nav-item">
                    {user?.result?.avatar ? (
                        <img
                            src={user.result.avatar}
                            alt="avatar"
                            className="avatar"
                            style={{
                                width: '40px',
                                height: '40px',
                                borderRadius: '50%',
                                objectFit: 'cover',
                            }}
                        />
                    ) : (
                        <FaUser className="icon" />
                    )}
                    {!collapsed && <span>{user?.result?.name || "Profile"}</span>}
                </Link>
            )}

            {(!user || user.result.role !== "ADMIN") && (
                <>
                    <Link to="/" className="nav-item">
                        <FaHome className="icon" />
                        {!collapsed && <span>Home</span>}
                    </Link>
                    <Link to="/library" className="nav-item">
                        <FaBook className="icon" />
                        {!collapsed && <span>Library</span>}
                    </Link>
                </>
            )}
            <div className="nav-links">
                {user ? (
                    user.result.role === "ADMIN" ? (
                        <>
                            {/* Menu dành cho Admin */}
                            <Link to="/admin/home" className="nav-item">
                                <GiNotebook className="icon" />
                                {!collapsed && <span>Dashboard</span>}
                            </Link>
                            <Link to="/admin/users" className="nav-item">
                                <FaUsers className="icon" />
                                {!collapsed && <span>Manage Users</span>}
                            </Link>
                            <Link to="/admin/lessons" className="nav-item">
                                <FaBook className="icon" />
                                {!collapsed && <span>Manage Lessons</span>}
                            </Link>
                            <Link to="/admin/statistics" className="nav-item">
                                <FaChartBar className="icon" />
                                {!collapsed && <span>Statistics</span>}
                            </Link>
                            <button className="nav-item" onClick={handleLogout}>
                                <FaSignOutAlt className="icon" />
                                {!collapsed && <span>Logout</span>}
                            </button>
                        </>
                    ) : (
                        <>
                            {/* Menu dành cho user bình thường */}
                            <Link to="/user/lesson/" className="nav-item">
                                <GiNotebook className="icon" />
                                {!collapsed && <span>My Lessons</span>}
                            </Link>
                            <Link to="/create-lesson-home" className="nav-item">
                                <FaPlusCircle className="icon" />
                                {!collapsed && <span>Create</span>}
                            </Link>
                            <button className="nav-item" onClick={handleLogout}>
                                <FaSignOutAlt className="icon" />
                                {!collapsed && <span>Logout</span>}
                            </button>
                        </>
                    )
                ) : (
                    <Link to="/login" className="nav-item">
                        <FaSignInAlt className="icon" />
                        {!collapsed && <span>Login</span>}
                    </Link>
                )}

            </div>
        </div>
    );
};

export default Sidebar;
