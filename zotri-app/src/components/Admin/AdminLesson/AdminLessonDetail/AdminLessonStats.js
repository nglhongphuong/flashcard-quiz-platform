// AdminLessonStats.js
import "./AdminLessonStats.css";
import React, { useEffect, useRef, useState } from "react";
import { useParams } from "react-router-dom";
import { authApis, endpoints } from "../../../../configs/Apis";
import MySpinner from "../../../layouts/MySpinner";
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
import { FaFileExcel, FaFileWord, FaTimes, FaListUl } from "react-icons/fa";
import * as XLSX from "xlsx";
import { Document, Packer, Paragraph, TextRun } from "docx";
import { saveAs } from "file-saver";

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

const monthsLabels = Array.from({ length: 12 }, (_, i) => `Month ${i + 1}`);

const AdminLessonStats = () => {
  const { id: lessonId } = useParams();
  const [year, setYear] = useState(new Date().getFullYear());
  const [raw, setRaw] = useState(null);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);

  const [showQuizDetails, setShowQuizDetails] = useState(false);
  const [showTestDetails, setShowTestDetails] = useState(false);
  const [detailList, setDetailList] = useState([]); // items to show in modal
  const [detailTitle, setDetailTitle] = useState("");

  const quizChartRef = useRef(null);
  const testChartRef = useRef(null);
  const quizChartInst = useRef(null);
  const testChartInst = useRef(null);

  useEffect(() => {
    let mounted = true;
    const load = async () => {
      setLoading(true);
      try {
        const res = await authApis().get(endpoints.admin_lesson_stats(lessonId, year));
        if (!mounted) return;
        setRaw(res.data);
      } catch (err) {
        console.error("Error loading lesson stats:", err);
        setRaw(null);
      } finally {
        setLoading(false);
      }
    };
    load();
    return () => {
      mounted = false;
    };
  }, [lessonId, year]);

  // build quiz bar
  useEffect(() => {
    if (!raw || !quizChartRef.current) return;
    const data = monthsLabels.map((_, idx) => raw.months[idx + 1]?.quizStudyCount || 0);
    if (quizChartInst.current) quizChartInst.current.destroy();
    quizChartInst.current = new Chart(quizChartRef.current, {
      type: "bar",
      data: {
        labels: monthsLabels,
        datasets: [
          {
            label: "QuizStudy Count",
            data,
            backgroundColor: "rgba(54,162,235,0.7)",
          },
        ],
      },
      options: {
        responsive: true,
        plugins: { legend: { labels: { color: "white" } } },
        scales: {
          x: { ticks: { color: "white" }, grid: { color: "#333" } },
          y: { ticks: { color: "white" }, grid: { color: "#333" }, beginAtZero: true },
        },
      },
    });
  }, [raw]);

  // build test line
  useEffect(() => {
    if (!raw || !testChartRef.current) return;
    const data = monthsLabels.map((_, idx) => raw?.months?.[idx + 1]?.quizStudyCount || 0);

    if (testChartInst.current) testChartInst.current.destroy();
    testChartInst.current = new Chart(testChartRef.current, {
      type: "line",
      data: {
        labels: monthsLabels,
        datasets: [
          {
            label: "TestStudy Count",
            data,
            borderColor: "rgba(255,206,86,1)",
            backgroundColor: "rgba(255,206,86,0.2)",
            fill: true,
            tension: 0.3,
            pointRadius: 4,
            pointHoverRadius: 6,
          },
        ],
      },
      options: {
        responsive: true,
        plugins: { legend: { labels: { color: "white" } } },
        scales: {
          x: { ticks: { color: "white" }, grid: { color: "#333" } },
          y: { ticks: { color: "white" }, grid: { color: "#333" }, beginAtZero: true },
        },
      },
    });
  }, [raw]);

  const uniqueUsersCount = (arr) => {
    if (!arr) return 0;
    const s = new Set(arr.map((a) => a.userId));
    return s.size;
  };

  const openQuizDetail = (month) => {
    const items = raw.months[month]?.quizStudies || [];
    setDetailList(items);
    setDetailTitle(`QuizStudy - Month ${month}`);
    setShowQuizDetails(true);
  };

  const openTestDetail = (month) => {
    const items = raw.months[month]?.testStudies || [];
    setDetailList(items);
    setDetailTitle(`TestStudy - Month ${month}`);
    setShowTestDetails(true);
  };

  const closeDetail = () => {
    setShowQuizDetails(false);
    setShowTestDetails(false);
    setDetailList([]);
  };

  const exportExcel = () => {
    if (!raw) return alert("No data");
    const wb = XLSX.utils.book_new();

    // Quiz sheet
    const quizRows = [["Month", "QuizStudy ID", "User ID", "User Name", "Created At"]];
    for (let m = 1; m <= 12; m++) {
      const arr = raw.months[m]?.quizStudies || [];
      if (!arr.length) continue;
      quizRows.push([`Month ${m}`, "", "", "", ""]);
      arr.forEach((q) =>
        quizRows.push([
          "",
          q.id,
          q.userId,
          q.userInfo?.name || "",
          new Date(q.createdAt).toLocaleString(),
        ])
      );
    }
    XLSX.utils.book_append_sheet(wb, XLSX.utils.aoa_to_sheet(quizRows), "QuizStudies");

    // Test sheet
    const testRows = [["Month", "Quiz ID", "Min", "User ID", "User Name", "Created At"]];
    for (let m = 1; m <= 12; m++) {
      const arr = raw.months[m]?.testStudies || [];
      if (!arr.length) continue;
      testRows.push([`Month ${m}`, "", "", "", "", ""]);
      arr.forEach((t) =>
        testRows.push([
          "",
          t.quizId,
          t.min,
          t.userInfo?.id || "",
          t.userInfo?.name || "",
          new Date(t.createdAt).toLocaleString(),
        ])
      );
    }
    XLSX.utils.book_append_sheet(wb, XLSX.utils.aoa_to_sheet(testRows), "TestStudies");

    const wbout = XLSX.write(wb, { bookType: "xlsx", type: "array" });
    saveAs(new Blob([wbout], { type: "application/octet-stream" }), `Lesson_${lessonId}_Stats_${year}.xlsx`);
  };

  const exportWord = async () => {
    if (!raw) return alert("No data");
    const children = [
      new Paragraph({
        children: [new TextRun({ text: `Lesson ${lessonId} - Stats ${year}`, bold: true, size: 28, color: "000000" })],
        spacing: { after: 300 },
      }),
    ];

    for (let m = 1; m <= 12; m++) {
      const q = raw.months[m]?.quizStudies || [];
      const t = raw.months[m]?.testStudies || [];
      children.push(new Paragraph({ children: [new TextRun({ text: `Month ${m}`, bold: true })], spacing: { before: 200, after: 100 } }));

      children.push(new Paragraph({ children: [new TextRun({ text: "QuizStudies:", bold: true })] }));
      if (q.length === 0) children.push(new Paragraph("No quiz studies"));
      else q.forEach((it) => children.push(new Paragraph(`ID: ${it.id} | User: ${it.userInfo?.name || it.userId} | Created: ${new Date(it.createdAt).toLocaleString()}`)));

      children.push(new Paragraph({ children: [new TextRun({ text: "TestStudies:", bold: true })], spacing: { before: 100 } }));
      if (t.length === 0) children.push(new Paragraph("No test studies"));
      else t.forEach((it) => children.push(new Paragraph(`QuizId: ${it.quizId} | Min: ${it.min} | User: ${it.userInfo?.name || it.userId} | Created: ${new Date(it.createdAt).toLocaleString()}`)));
    }

    const doc = new Document({ sections: [{ children }] });
    const blob = await Packer.toBlob(doc);
    saveAs(blob, `Lesson_${lessonId}_Stats_${year}.docx`);
  };

 if (loading || !raw?.months) return <MySpinner />;


  return (
    <div className="lesson-stats-root">
      <h1 className="lesson-stats-title">Lesson Statistics (Detailed)</h1>

      {/* controls */}
      <div className="lesson-stats-controls">
        <div className="lesson-stats-filter">
          Year:
          <select value={year} onChange={(e) => setYear(+e.target.value)}>
            {Array.from({ length: 5 }).map((_, i) => {
              const y = new Date().getFullYear() - i;
              return (
                <option key={y} value={y}>
                  {y}
                </option>
              );
            })}
          </select>
        </div>

        <div className="lesson-stats-actions">
          <button className="btn pastel" onClick={exportExcel}>
            <FaFileExcel /> Export Excel
          </button>
          <button className="btn pastel" onClick={exportWord}>
            <FaFileWord /> Export Word
          </button>
        </div>
      </div>

      {/* QuizStudy section */}
      <section className="lesson-stats-section">
        <h2 className="section-title">QuizStudy Statistics</h2>
        <div className="section-body">
          <div className="section-left">
            <div className="stat-card">
              <div className="stat-item">
                <div className="stat-label">Total QuizStudies (This Year)</div>
                <div className="stat-value">
                  {Object.values(raw.months).reduce((s, m) => s + (m?.quizStudyCount || 0), 0)}
                </div>
              </div>

              <div className="stat-item">
                <div className="stat-label">Unique Participants</div>
                <div className="stat-value">
                  {Object.values(raw.months).reduce((acc, m) => {
                    const arr = m?.quizStudies || [];
                    arr.forEach((it) => acc.add(it.userId));
                    return acc;
                  }, new Set()).size}
                </div>
              </div>

              <div className="stat-item">
                <button
                  className="btn pastel"
                  onClick={() => {
                    // open detail for whole year: merge all quizStudies
                    const merged = [];
                    for (let m = 1; m <= 12; m++) {
                      (raw.months[m]?.quizStudies || []).forEach((it) => merged.push({ ...it, month: m }));
                    }
                    setDetailList(merged);
                    setDetailTitle("QuizStudy - All Months");
                    setShowQuizDetails(true);
                  }}
                >
                  <FaListUl /> View details
                </button>
              </div>
            </div>
          </div>

          <div className="section-right">
            <canvas ref={quizChartRef} />
            <div className="per-month-actions">
              {Array.from({ length: 12 }).map((_, i) => {
                const m = i + 1;
                return (
                  <button key={m} className="month-btn" onClick={() => openQuizDetail(m)}>
                    Month {m}
                  </button>
                );
              })}
            </div>
          </div>
        </div>
      </section>

      {/* TestStudy section */}
      <section className="lesson-stats-section">
        <h2 className="section-title">TestStudy Statistics</h2>
        <div className="section-body">
          <div className="section-left">
            <div className="stat-card">
              <div className="stat-item">
                <div className="stat-label">Total TestStudies (This Year)</div>
                <div className="stat-value">
                  {Object.values(raw.months).reduce((s, m) => s + (m?.testStudyCount || 0), 0)}
                </div>
              </div>

              <div className="stat-item">
                <div className="stat-label">Unique Participants</div>
                <div className="stat-value">
                  {Object.values(raw.months).reduce((acc, m) => {
                    const arr = m?.testStudies || [];
                    arr.forEach((it) => acc.add(it.userInfo?.id || it.quizId)); // best-effort
                    return acc;
                  }, new Set()).size}
                </div>
              </div>

              <div className="stat-item">
                <button
                  className="btn pastel"
                  onClick={() => {
                    const merged = [];
                    for (let m = 1; m <= 12; m++) {
                      (raw.months[m]?.testStudies || []).forEach((it) => merged.push({ ...it, month: m }));
                    }
                    setDetailList(merged);
                    setDetailTitle("TestStudy - All Months");
                    setShowTestDetails(true);
                  }}
                >
                  <FaListUl /> View details
                </button>
              </div>
            </div>
          </div>

          <div className="section-right">
            <canvas ref={testChartRef} />
            <div className="per-month-actions">
              {Array.from({ length: 12 }).map((_, i) => {
                const m = i + 1;
                return (
                  <button key={m} className="month-btn" onClick={() => openTestDetail(m)}>
                    Month {m}
                  </button>
                );
              })}
            </div>
          </div>
        </div>
      </section>

      {/* Detail modal */}
      {(showQuizDetails || showTestDetails) && (
        <div className="lesson-stats-modal">
          <div className="lesson-stats-modal-card">
            <button className="modal-close" onClick={closeDetail}>
              <FaTimes />
            </button>
            <h3 className="modal-title">{detailTitle}</h3>
            <div className="modal-body">
              <div className="modal-list">
                {detailList.length === 0 ? (
                  <p>No records</p>
                ) : (
                  <table className="modal-table">
                    <thead>
                      <tr>
                        <th>ID</th>
                        <th>User</th>
                        <th>Created At</th>
                      </tr>
                    </thead>
                    <tbody>
                      {detailList.map((it) => (
                        <tr key={it.id ?? it.quizId}>
                          <td>{it.id ?? it.quizId}</td>
                          <td className="user-cell">
                            <img className="user-avatar" src={it.userInfo?.avatar} alt={it.userInfo?.name} />
                            <div>
                              <div className="user-name">{it.userInfo?.name || it.userId}</div>
                              <div className="user-username">{it.userInfo?.username || ""}</div>
                            </div>
                          </td>
                          <td>{new Date(it.createdAt).toLocaleString()}</td>
                        </tr>
                      ))}
                    </tbody>
                  </table>
                )}
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  );
};

export default AdminLessonStats;
