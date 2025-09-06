import React from "react";
import Sidebar from "./Sidebar";
import Body from "./Body";
import "./Layout.css";

const Layout = ({ children }) => {
    return (
        <div className="layout">
            <Sidebar />
            <Body>{children}</Body>
        </div>
    );
};

export default Layout;