    //NẾU USER ĐÃ ĐĂNG NHẬP
    // Gồm 3 phần (NỀN ĐEN CHỮ TRẮNG PASTEL DÙNG ICON-REACT-ICON NO EMOJI, BO GÓC , CHỦ YẾU LÀ CÓ THÌ XANH BIỂN PASTEL) CHIA RA DANH SÁCH TỪNG THẺ DỄ QUẢN LÝ NẰM  TRONG FOLDER DETAILHOME
    //PHẦN 1: HEIGHT 200PX, GIỚI THIỆU WEB HỌC FLASHCARD TIỆN LỢI  , CONTENT ĐỒ ĐÓ,
    //  PHẦN 2: 600PX 2 đang học gần đây gọi authapis().get   view_history : `/view-history/info`
    // kq trả về danh sách lesson id của người dùng {
    // "code": 1000,
    // "result": [
    //     {
    //         "id": 3,
    //         "title": "Lesson 3",
    //         "description": "Description for lesson 3",
    //         "image": null,
    //         "visibility": "PRIVATE",
    //         "isCommentLocked": false,
    //         "createdAt": "2025-07-14T10:25:10.000+00:00",
    //         "updateAt": "2025-07-14T10:25:10.000+00:00",
    //         "flashcardSet": [
    //             {
    //                 "id": 3,
    //                 "word": "Word 3",
    //                 "definition": "Definition 3",
    //                 "image": null,
    //                 "createdAt": "2025-07-14T10:25:10.000+00:00",
    //                 "updateAt": "2025-07-14T10:25:10.000+00:00"
    
    //         ],
    //         "userInfo": {
    //             "id": 3,
    //             "username": "user3",
    //             "name": "User 3",
    //             "role": "USER",
    //             "avatar": "http://res.cloudinary.com/dnc5sycvb/image/upload/v1754455186/avatars/iozktbptqzj8vsf8ms4x.jpg"
    //         },
    //         "viewhistoryResponse": {
    //             "viewhistoryPK": {
    //                 "userId": 26,
    //                 "lessonId": 3
    //             },
    //             "study": true,
    //             "createdAt": "2025-08-10T06:40:07.000+00:00",
    //             "updateAt": "2025-08-10T06:40:07.000+00:00",
    //             "user": {
    //                 "accountId": 26
    //             }
    //         },
    //         "lessonschedules": []
    //     },
    //     {
    //         "id": 20,
    //         "title": "Hôm nay thứ mấy",
    //         "description": "Bài học ddddddddddddddddddddddddddddddddddddddddd òo SS",
    //         "image": "http://res.cloudinary.com/dnc5sycvb/image/upload/v1754486199/avatars/bkgwgc7sl7habcoqqt4x.jpg",
    //         "visibility": "PUBLIC",
    //         "isCommentLocked": false,
    //         "createdAt": "2025-08-06T13:16:35.000+00:00",
    //         "updateAt": "2025-08-06T13:16:35.000+00:00",
    //         "flashcardSet": [
    //             {
    //                 "id": 34,
    //                 "word": "White Cat",
    //                 "definition": "Mèo màu trắng",
    //                 "image": "http://res.cloudinary.com/dnc5sycvb/image/upload/v1754624342/flashcards/mtnntsac000aebjuqubs.jpg",
    //                 "createdAt": "2025-08-06T13:19:34.000+00:00",
    //                 "updateAt": "2025-08-08T03:39:02.000+00:00"
    //             },

    //         ],
    //         "userInfo": {
    //             "id": 26,
    //             "username": "nguyenluhongphuong@gmail.com",
    //             "name": "ok",
    //             "role": "USER",
    //             "avatar": "http://res.cloudinary.com/dnc5sycvb/image/upload/v1754578892/avatars/fumyjgzyyw0ksreljgg6.jpg"
    //         },
    //         "viewhistoryResponse": {
    //             "viewhistoryPK": {
    //                 "userId": 26,
    //                 "lessonId": 20
    //             },
    //             "study": true,
    //             "createdAt": "2025-08-08T03:26:00.000+00:00",
    //             "updateAt": "2025-08-10T04:32:51.000+00:00",
    //             "user": {
    //                 "accountId": 26
    //             }
    //         },
    //         "lessonschedules": []
    //     },
    //PHẦN 2:
    // PHẦN 2.1 TIÊU ĐỀ  DANH SÁCH LESSON GẦN ĐÂY BẠN ĐÃ HỌC (DỰA TRÊN NGÀY UPDATE LẤY 4 PHẦN TỬ ĐẦU TIÊN)
    // PHẦN 2.2: HÀNG  BỐ CỤC 1 HÀNG 4 CỘT NGAY NGẮN (CỦA 4 PHẦN TỬ GẦN ĐÂY NHẤT), BO GÓC THAM KHẢO GIAO DIỆN 1 CARD TỪ FLASHACRD
    // import { useEffect, useState } from "react";
    // import { useNavigate } from "react-router-dom";
    // import "./LessonCard.css";
    // import moment from "moment";
    // import "moment/locale/vi";
    // import { FaStar, FaStarHalfAlt, FaRegStar } from "react-icons/fa";
    // import { BsBarChart } from "react-icons/bs";
    // import Apis, { endpoints } from "../../configs/Apis";
    // import React from 'react';
    // moment.locale("vi");
    
    // const LessonCard = React.forwardRef(({ lesson }, ref) => {
    //   const [rating, setRating] = useState(null);
    //   const navigate = useNavigate();
    //   const handleClick = () => {
    //     navigate(`/lesson/${lesson.id}`);
    //   };
    
    
    //   useEffect(() => {
    //     const fetchRating = async () => {
    //       try {
    //         const res = await Apis.get(endpoints.rating(lesson.id));
    //         setRating(res.data.result);
    //       } catch (err) {
    //         console.error("Lỗi khi lấy rating:", err);
    //       }
    //     };
    
    //     fetchRating();
    //   }, [lesson.id]);
    
    //   return (
    //     <div className="lesson-card" ref={ref} onClick={handleClick}>
    //       <div className="lesson-header-card">
    //         <div className="avatar">
    //           {lesson.userInfo.avatar ? (
    //             <img src={lesson.userInfo.avatar} alt="avatar" />
    //           ) : (
    //             <div className="avatar-placeholder">
    //               {lesson.userInfo.name?.charAt(0).toUpperCase()}
    //             </div>
    //           )}
    //         </div>
    //         <div className="info">
    //           <span className="name">{lesson.userInfo.name}</span>
    //           <span className="time">
    //             {moment(lesson.createdAt).fromNow()}
    //           </span>
    //         </div>
    //       </div>
    
    //       {lesson.image && (
    //         <div className="lesson-image">
    //           <img src={lesson.image} alt="lesson" />
    //         </div>
    //       )}
    //       <div className="lesson-content">
    //         <div className="title-rating">
    //           <h3 className="lesson-title">{lesson.title}</h3>
    //           {rating && (
    //             <div className="lesson-rating">
    //               {[1, 2, 3, 4, 5].map((i) => {
    //                 if (rating.averageRating >= i) {
    //                   return <FaStar key={i} className="star" />;
    //                 } else if (rating.averageRating >= i - 0.5) {
    //                   return <FaStarHalfAlt key={i} className="star" />;
    //                 } else {
    //                   return <FaRegStar key={i} className="star" />;
    //                 }
    //               })}
    //               <span className="score">/5</span>
    //               <BsBarChart style={{ margin: "0 4px" }} />
    //               <span>Studied by {rating.totalUser}</span>
    //             </div>
    //           )}
    //         </div>
    
    //         <p>{lesson.description}</p>
    //       </div>
    
    //     </div>
    //   );
    // });
    
    // export default LessonCard;
    //PHẦN 2.3:
    // DANH SÁCH CÁC LESSON TỪ TRUY VẤN TRẢ VỀ CÓ PHÂN TRANG Á NÊN PHẢI LOAD PAGE =))) , MY SPONNER CÁC KIỂU , CHIỀU CAO 500PX, CÓ SCROLL TRƯỢT NHƯ COMMENT

    // PHẦN 3 :
import HomeIntro from "./HomeDetail/HomeIntro";
import PartContent from "./HomeDetail/PartContent";
import RecentView from "./HomeDetail/View/RecentView";
import BookMark from "./HomeDetail/Bookmark/Bookmark";
import { useContext } from "react";
import { MyUserContext } from "../configs/MyContexts";

import React from "react";
import "./Home.css";

const Home = () => {
      const user = useContext(MyUserContext);
  
  if (!user) {
    return (
      <div className="home-container">
        <HomeIntro />
      </div>
    );
  }
if (user.result.role === "ADMIN") {
  return (
    <div className="home-container" style={{ padding: "20px", color: "#f0f0f0" }}>
      <h1>Welcome Admin</h1>
      <p>
        Thank you for logging in as an administrator. Here, you have full access to manage lessons, users, 
        and monitor system statistics to ensure everything runs smoothly. Your role is crucial for maintaining 
        the quality and security of the platform. Please use the admin dashboard to efficiently oversee all activities.
      </p>
      <p>
        If you need any assistance, don't hesitate to reach out to the support team. We appreciate your 
        dedication and commitment to making this learning platform the best it can be.
      </p>
    </div>
  );
}


return (
    <div className="home-container">
      <PartContent />
      <RecentView />
      {/* <BookMark />  */}
    </div>
  );
};

export default Home;
