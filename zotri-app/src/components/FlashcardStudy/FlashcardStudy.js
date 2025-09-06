//1) flashcardStudy hien thong ke tien do nguoi hoc tuc la thong ke , sau do hien 3 nut Hoc laij Remembered, not_remembered, not learned hoac tat ca , cho nguoi dung hoc.
// //ket qua tra ve cua   const res = await authApis.get(endpoints.flashcard_study_stats(lessonId));
// {
//     "code": 1000,
//     "result": {
//         "lessonId": 15,
//         "all": 11,
//         "not_REMEMBERED": 0,
//         "remembered": 0,
//         "not_LEARNED": 11
//     }
// BIEU COT THIET KE DEP MAT TEMPLATE NEN DEN CHU TRANG
// FORM GỒM 3 PHẦN
// PHẦN 1: TIÊU ĐỀ
// PHẦN 2: LA BANG THỐNG KÊ CỘT NGANG (MÀU PASTEL) TƯƠNG ỨNG VỚI MỖI CỘT NẰM NGANG NẾU SỐ LƯỢNG KHÔNG = 0 THÌ HIỆN NÚT TƯƠNG ỨNG LÀ NÚT NOT_LEARNED, NOT_REMEMBRED, REMBRED
// PHẦN 3: Ở DƯỚI BIỂU ĐỒ THỐNG KÊ CÓ THÊM 2 NÚT LÀ HỌC TẤT CẢ, HOẶC HỌC LẠI TỪ ĐẦU (TỨC LÀ RESET HẾT TẤT CẢ VỀ NOT_LEARNED )
// }
//NẾU NGƯỜI DÙNG CHỌN NÚT HỌC LẠI TỪ ĐẦU -> RESET GỌI API authApis.post(endpoints.flashcard_study_reset(lessonId)); XONG QUA KHI QUA TRANG FLASHCARDHOME.JS THÌ MẶC ĐỊNH LÀ ALL 

//2) Neu nguoi dung chon 1 nut bat ky trong not_learned, remembered, not_remembered, ALL -> qua flashcardUtil/FlashcardHome.js de hoc 
//
// truy vấn sẽ là
//  flashcard_study_status: (lessonId) => `/lesson/${lessonId}/flashcard-study/status`, =>      const res = await authApis.get(endpoints.flashcard_study_status(lessonId));
// truyen params tương ứng là status trước khi từ flashcardstudy.js qua flashcardHome.js là
// status Text NOT_LEARNED khi nhấn nút not_leanred
// status Text REMEMBERED khi nhấn nút remembered
// status Text NOT_REMEMBERED khi nhấn nút not_remembered
// để khi gọi      const res = await authApis.get(endpoints.flashcard_study_status(lessonId)); ở flashcardHome.js có truyền params   let form = new FormData();       form.append("status", status); để nó hiện danh sách trạng thái người dùng muốn học
// sau khi có danh sách đánh số thứ tự từng thẻ ý là nói chung giống bên quizlet á.
// nút skip, nút remembered , nút not_remembered -> nhấn xong thì qua thẻ khác nhưng mà khi nhấn thì cần cập nhập trạng thái thẻ
// nếu skip ko cần gọi authApi.put , nếu remembred hoặc not_rembered thì gọi  await authApis.get(endpoints.flashcard_flashcard_study(lessonId)) cũng truyền  có truyền params   let form = new FormData();       form.append("status", status)

//sAU KHI HỌC XONG QUAY LẠI TRANG FLASHCARDSTUDY.JS ĐỂ TỔNG HỢP KẾT QUẢ HỌC 

// FlashcardStudy.js
import "./FlashcardStudy.css";
import React, { useEffect, useState, useContext } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { authApis, endpoints } from "../../configs/Apis";
import { MyUserContext } from "../../configs/MyContexts";
import MySpinner from "../layouts/MySpinner";
import { Bar } from 'react-chartjs-2';
import {
    Chart as ChartJS,
    CategoryScale,
    LinearScale,
    BarElement,
    Title,
    Tooltip,
    Legend,
} from 'chart.js';

ChartJS.register(
    CategoryScale,
    LinearScale,
    BarElement,
    Title,
    Tooltip,
    Legend
);


const FlashcardStudy = () => {
    const { lessonId } = useParams();
    const user = useContext(MyUserContext);
    const [loading, setLoading] = useState(false);
    const [stats, setStats] = useState(null);
    const nav = useNavigate();

    useEffect(() => {
        const loadStats = async () => {
            setLoading(true);
            try {
                // let res = await authApis().post(endpoints.lesson_create, form);
                await authApis().post(endpoints.flashcard_study(lessonId));
                let res = await authApis().get(endpoints.flashcard_study_stats(lessonId));
                setStats(res.data.result);
            } catch (err) {
                console.error("Error fetching flashcard study", err);
            } finally {
                setLoading(false);
            }
        };
        loadStats();
    }, [lessonId]);

    const resetStudy = async () => {
        await authApis().put(endpoints.flashcard_study_reset(lessonId));
        nav(`/flashcard-study/${lessonId}/ALL`);
    };

    const handleStudyStatus = (status) => {
        nav(`/flashcard-study/${lessonId}/${status}`);
    };

    if (loading || !stats) return <MySpinner />;

    const data = {
        labels: ['NOT_LEARNED', 'NOT_REMEMBERED', 'REMEMBERED'],
        datasets: [
            {
                label: 'Flashcard Status',
                data: [stats.not_LEARNED, stats.not_REMEMBERED, stats.remembered],
                backgroundColor: ['#ffb6b9', '#fcd5ce', '#c8e6c9'],
                borderWidth: 1,
                borderRadius: 6,
            }
        ]
    };

    const options = {
        responsive: true,
        maintainAspectRatio: false,  // giúp chart fill container mà không bị co dãn lạ
        indexAxis: 'y',
        animation: {
            duration: 400,   // animation load mượt, không tắt hoàn toàn
        },
        hover: {
            mode: null,      // tắt mọi hiệu ứng hover scale hay highlight
            animationDuration: 0,
        },
        plugins: {
            legend: { display: false },
            tooltip: { enabled: true }
        },
        scales: {
            x: {
                beginAtZero: true,
                ticks: { color: 'white' },
                grid: { color: '#333' }
            },
            y: {
                ticks: { color: 'white' },
                grid: { display: false }
            }
        }
    };

    const handleBackToMyLesson = () => {
        nav(`/lesson/${lessonId}`);
    };


    return (

        <div className="flashcard-study-page">
            <button className="back-button-flashcard-study" onClick={handleBackToMyLesson}>
                Back to Lesson
            </button>
            <h1 className="study-title">Flashcard Progress</h1>

            <div className="chart-button-container">
                <div className="chart-section">
                    <Bar data={data} options={options}

                    />
                </div>

                <div className="status-buttons">
                    {stats.not_LEARNED > 0 && (
                        <button onClick={() => handleStudyStatus("NOT_LEARNED")}>Not Learned</button>
                    )}
                    {stats.not_REMEMBERED > 0 && (
                        <button onClick={() => handleStudyStatus("NOT_REMEMBERED")}>Not Remembered</button>
                    )}
                    {stats.remembered > 0 && (
                        <button onClick={() => handleStudyStatus("REMEMBERED")}>Remembered</button>
                    )}
                </div>
            </div>

            <div className="reset-section">
                <button className="all-btn" onClick={() => handleStudyStatus("ALL")}>Study All</button>
                <button className="reset-btn" onClick={resetStudy}>Restart</button>
            </div>
        </div>
    );
};

export default FlashcardStudy;
