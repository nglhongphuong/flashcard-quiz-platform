import { useNavigate } from "react-router-dom";
import "./AdminHome.css";

const AdminHome = () => {
  const navigate = useNavigate();

  return (
    <div className="admin-dashboard-container">
      <h1>Admin Dashboard</h1>
      <p>Welcome to the admin control panel. Choose an option below to manage the platform.</p>

      <div className="admin-dashboard-buttons">
        <button
          className="admin-btn"
          onClick={() => navigate("/admin/users")}
        >
          Manage Users
        </button>

        <button
          className="admin-btn"
          onClick={() => navigate("/admin/lessons")}
        >
          Manage Lessons
        </button>

        <button
          className="admin-btn"
          onClick={() => navigate("/admin/statistics")}
        >
          View Statistics
        </button>

        <button
          className="admin-btn"
          onClick={() => navigate("/admin/home")}
        >
          Dashboard Home
        </button>
      </div>
    </div>
  );
};

export default AdminHome;
