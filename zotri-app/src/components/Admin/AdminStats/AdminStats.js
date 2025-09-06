import { authApis, endpoints } from "../../../configs/Apis";
import "./AdminStats.css";

import React, { useEffect, useState, useRef } from "react";
import {
    Chart,
    BarController,
    BarElement,
    CategoryScale,
    LinearScale,
    PointElement,
    LineElement,
    LineController,
    Tooltip,
    Legend,
} from "chart.js";
import { saveAs } from "file-saver";
import * as XLSX from "xlsx";
import { Document, Packer, Paragraph, TextRun } from "docx";

Chart.register(
    BarController,
    BarElement,
    CategoryScale,
    LinearScale,
    PointElement,
    LineElement,
    LineController,
    Tooltip,
    Legend
);

const AllStats = () => {
    const currentYear = new Date().getFullYear();
    const [year, setYear] = useState(currentYear);
    const [rawData, setRawData] = useState(null);
    const userChartRef = useRef(null);
    const lessonChartRef = useRef(null);
    const userChartInstance = useRef(null);
    const lessonChartInstance = useRef(null);
    const [loading, setLoading] = useState(false);

    useEffect(() => {
        setLoading(true);
        authApis()
            .get(endpoints.admin_stats(year))
            .then((res) => setRawData(res.data))
            .catch(console.error)
            .finally(() => setLoading(false));
    }, [year]);
    useEffect(() => {
        if (!rawData || !userChartRef.current) return;
        const labels = Array.from({ length: 12 }, (_, i) => `Month ${i + 1}`);
        const userCounts = labels.map((_, idx) => {
            const month = idx + 1;
            return rawData.months[month]?.userCount || 0;
        });
        if (userChartInstance.current) {
            userChartInstance.current.destroy();
        }

        userChartInstance.current = new Chart(userChartRef.current, {
            type: "bar",
            data: {
                labels,
                datasets: [
                    {
                        label: "User Count",
                        data: userCounts,
                        backgroundColor: "rgba(54, 162, 235, 0.7)",
                    },
                ],
            },
            options: {
                responsive: true,
                scales: {
                    y: {
                        beginAtZero: true,
                        ticks: { color: "white" },
                        grid: { color: "#444" },
                    },
                    x: {
                        ticks: { color: "white" },
                        grid: { color: "#444" },
                    },
                },
                plugins: {
                    legend: { labels: { color: "white" } },
                    tooltip: {},
                },
            },
        });
    }, [rawData]);

    useEffect(() => {
        if (!rawData || !lessonChartRef.current) return;
        const labels = Array.from({ length: 12 }, (_, i) => `Month ${i + 1}`);
        const lessonCounts = labels.map((_, idx) => {
            const month = idx + 1;
            return rawData.months[month]?.lessonCount || 0;
        });
        if (lessonChartInstance.current) {
            lessonChartInstance.current.destroy();
        }

        lessonChartInstance.current = new Chart(lessonChartRef.current, {
            type: "line",
            data: {
                labels,
                datasets: [
                    {
                        label: "Lesson Count",
                        data: lessonCounts,
                        borderColor: "rgba(255, 206, 86, 1)",
                        backgroundColor: "rgba(255, 206, 86, 0.3)",
                        fill: true,
                        tension: 0.3,
                        pointRadius: 5,
                        pointHoverRadius: 7,
                    },
                ],
            },
            options: {
                responsive: true,
                scales: {
                    y: {
                        beginAtZero: true,
                        ticks: { color: "white" },
                        grid: { color: "#444" },
                    },
                    x: {
                        ticks: { color: "white" },
                        grid: { color: "#444" },
                    },
                },
                plugins: {
                    legend: { labels: { color: "white" } },
                    tooltip: {},
                },
            },
        });
    }, [rawData]);
    const exportExcel = () => {
        if (!rawData) return alert("Không có dữ liệu");
        const wb = XLSX.utils.book_new();
        const userSheetData = [
            ["Month", "User ID", "Username", "Name", "Gender", "Created At"],
        ];
        for (let month = 1; month <= 12; month++) {
            const users = rawData.months[month]?.users || [];
            if (users.length === 0) continue;
            userSheetData.push([`Month ${month}`, "", "", "", "", ""]);
            users.forEach((u) => {
                userSheetData.push([
                    "",
                    u.id,
                    u.username,
                    u.name,
                    u.gender,
                    new Date(u.createdAt).toLocaleString(),
                ]);
            });
        }
        const wsUsers = XLSX.utils.aoa_to_sheet(userSheetData);
        XLSX.utils.book_append_sheet(wb, wsUsers, "Users");
        const lessonSheetData = [
            ["Month", "Lesson ID", "Title", "User ID", "Created At"],
        ];
        for (let month = 1; month <= 12; month++) {
            const lessons = rawData.months[month]?.lessons || [];
            if (lessons.length === 0) continue;
            lessonSheetData.push([`Month ${month}`, "", "", "", ""]);
            lessons.forEach((l) => {
                lessonSheetData.push([
                    "",
                    l.id,
                    l.title,
                    l.userId,
                    new Date(l.createdAt).toLocaleString(),
                ]);
            });
        }
        const wsLessons = XLSX.utils.aoa_to_sheet(lessonSheetData);
        XLSX.utils.book_append_sheet(wb, wsLessons, "Lessons");
        const wbout = XLSX.write(wb, { bookType: "xlsx", type: "array" });
        saveAs(
            new Blob([wbout], { type: "application/octet-stream" }),
            `Stats_Details_${year}.xlsx`
        );
    };

    const exportWord = async () => {
        if (!rawData) return alert("No data... !");
        const children = [
            new Paragraph({
                children: [
                    new TextRun({
                        text: `Detail Stats in ${year}`,
                        bold: true,
                        size: 28,
                        color: "000000",
                    }),
                ],
                spacing: { after: 400 },
            }),
        ];

        for (let month = 1; month <= 12; month++) {
            const users = rawData.months[month]?.users || [];
            const lessons = rawData.months[month]?.lessons || [];
            children.push(
                new Paragraph({
                    children: [
                        new TextRun({
                            text: `Month ${month}`,
                            bold: true,
                            size: 24,
                            color: "000000",
                        }),
                    ],
                    spacing: { before: 300, after: 200 },
                })
            );
            children.push(
                new Paragraph({
                    children: [
                        new TextRun({ text: "Users:", bold: true, color: "000000" }),
                    ],
                })
            );
            if (users.length === 0) {
                children.push(new Paragraph("Donnot have any users!"));
            } else {
                users.forEach((u) => {
                    children.push(
                        new Paragraph(
                            `ID: ${u.id} | Username: ${u.username} | Name: ${u.name} | Gender: ${u.gender} | Created At: ${new Date(
                                u.createdAt
                            ).toLocaleString()}`
                        )
                    );
                });
            }

            children.push(
                new Paragraph({
                    children: [
                        new TextRun({ text: "Lessons:", bold: true, color: "000000" }),
                    ],
                    spacing: { before: 200, after: 100 },
                })
            );
            if (lessons.length === 0) {
                children.push(new Paragraph("Không có lesson nào"));
            } else {
                lessons.forEach((l) => {
                    children.push(
                        new Paragraph(
                            `ID: ${l.id} | Title: ${l.title} | User ID: ${l.userId} | Created At: ${new Date(
                                l.createdAt
                            ).toLocaleString()}`
                        )
                    );
                });
            }
        }
        const doc = new Document({
            sections: [
                {
                    properties: {},
                    children,
                },
            ],
        });
        const blob = await Packer.toBlob(doc);
        saveAs(blob, `Stats_Details_${year}.docx`);
    };

    return (
        <div className="all-stats-grid">
            {/* Bảng thống kê bên trái */}
            <div className="all-stats-table">
                <h3>Monthly Summary</h3>
                {rawData ? (
                    <table>
                        <thead>
                            <tr>
                                <th>Month</th>
                                <th>User Count</th>
                                <th>Lesson Count</th>
                            </tr>
                        </thead>
                        <tbody>
                            {Array.from({ length: 12 }).map((_, i) => {
                                const month = i + 1;
                                return (
                                    <tr key={month}>
                                        <td>{month}</td>
                                        <td>{rawData.months[month]?.userCount || 0}</td>
                                        <td>{rawData.months[month]?.lessonCount || 0}</td>
                                    </tr>
                                );
                            })}
                            {/* Hàng tổng cộng */}
                            <tr className="all-stats-total">
                                <td><b>Total</b></td>
                                <td>
                                    {Object.values(rawData.months).reduce(
                                        (sum, m) => sum + (m?.userCount || 0),
                                        0
                                    )}
                                </td>
                                <td>
                                    {Object.values(rawData.months).reduce(
                                        (sum, m) => sum + (m?.lessonCount || 0),
                                        0
                                    )}
                                </td>
                            </tr>
                        </tbody>
                    </table>
                ) : (
                    <p>No data</p>
                )}
            </div>

            {/* Biểu đồ bên phải */}
            <div className="all-stats-charts">
                <div className="all-stats-controls">
                    <label>
                        <div className="stats-all-select">
                            Year:{" "}
                            <select value={year} onChange={(e) => setYear(+e.target.value)}>
                                {Array.from({ length: 5 }).map((_, idx) => {
                                    const y = currentYear - idx;
                                    return (
                                        <option key={y} value={y}>
                                            {y}
                                        </option>
                                    );
                                })}
                            </select>
                        </div>
                    </label>

                    <button onClick={exportExcel}>Export Excel</button>
                    <button onClick={exportWord}>Export Word</button>
                </div>

                {loading && <p>Loading...</p>}


                <div className="all-stats-chart-container">
                    <h3>User Count</h3>
                    <canvas ref={userChartRef} />
                </div>

                <div className="all-stats-chart-container" style={{ marginTop: 40 }}>
                    <h3>Lesson Count</h3>
                    <canvas ref={lessonChartRef} />
                </div>
            </div>
        </div>

    );
};

export default AllStats;
