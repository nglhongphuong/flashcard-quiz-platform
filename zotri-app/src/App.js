import { BrowserRouter, Routes, Route } from "react-router-dom";
import Footer from "./components/layouts/Footer";
import 'bootstrap/dist/css/bootstrap.min.css';
import { Container } from 'react-bootstrap';
import { MyDispatcherContext, MyUserContext } from "./configs/MyContexts";
import { useReducer, useEffect, useState } from "react";
import MyUserReducer from "./reducers/MyUserReducer"
import cookie from 'react-cookies';
import Apis, { endpoints } from "./configs/Apis";
import MySpinner from "./components/layouts/MySpinner";

//Pages
import Home from "./components/Home";
import Register from "./components/Auth/Register";
import Login from "./components/Auth/Login";
import Profile from "./components/User/Profile";
import Library from "./components/Library/Library";
import LessonDetail from "./components/LessonDetail/LessonDetail";
import CreateLesson from "./components/CRUDLesson/CreateLesson";
import CreateFlashcard from "./components/CRUDLesson/CreateFlashcard";
import LessonUser from "./components/User/LessonUser";
import AdminUser from "./components/Admin/AdminUser/AdminUser";
import AdminHome from "./components/Admin/AdminHome/AdminHome";
import AdminLesson from "./components/Admin/AdminLesson/AdminLesson";
import AdminStats from "./components/Admin/AdminStats/AdminStats";
//Layout
import Layout from "./components/layouts/Layout";
import MyLesson from "./components/User/MyLesson/MyLesson";
import FlashcardStudy from "./components/FlashcardStudy/FlashcardStudy";
import QuizStudy from "./components/QuizStudy/QuizStudy";
import FlashcardHome from "./components/FlashcardStudy/FlashcardUtil/FlashcardHome";
import QuizStudyDo from "./components/QuizStudy/QuizStudyDo/QuizStudyDo";
import QuizStudyResult from "./components/QuizStudy/QuizStudyDo/QuizStudyResult";
import TestStudy from "./components/TestStudy/TestStudy";
import TestStudyDo from "./components/TestStudy/TestStudyDo/TestStudyDo";
import TestStudyResult from "./components/TestStudy/TestStudyDo/TestStudyResult";
import RecentViewSet from "./components/HomeDetail/View/RecentViewSet";
import AdminUserEdit from "./components/Admin/AdminUser/AdminUserEdit";
import AdminLessonDetail from "./components/Admin/AdminLesson/AdminLessonDetail/AdminLessonDetail";
import AdminLessonStats from "./components/Admin/AdminLesson/AdminLessonDetail/AdminLessonStats";
import CreateFlashcardGemini from "./components/CRUDLesson/CreateFlashcardGemini";
import CreateLessonHome from "./components/CreateLessonHome/CreateLessonHome";
import CreateLessonGemini from "./components/CRUDLesson/CreateLessonGemini";
import UsernameInfo from "./components/UsernameInfo/UsernameInfo";

function App() {
  const [user, dispatch] = useReducer(MyUserReducer, null);
  const [checkingAuth, setCheckingAuth] = useState(true);

  useEffect(() => {
    const checkUser = async () => {
      try {
        const token = cookie.load("token");
        if (token) {
          const res = await Apis.get(endpoints.account_info, {
            headers: { Authorization: `Bearer ${token}` },
          });
          dispatch({ type: "login", payload: res.data });
        }
      } catch (error) {
        console.error("Lỗi xác thực:", error);
      } finally {
        setCheckingAuth(false);
      }
    };
    checkUser();
  }, []);

  if (checkingAuth) return <MySpinner />;

  return (
    <MyUserContext.Provider value={user}>
      <MyDispatcherContext.Provider value={dispatch}>

        <BrowserRouter>
          <Layout>
            <Routes>
              <Route path="/" element={<Home />} />
              <Route path="/library" element={<Library />} />
              <Route path="/lesson/:lessonId" element={<LessonDetail />} />

              {!user && (
                <>
                  <Route path="/login" element={<Login />} />
                  <Route path="/register" element={<Register />} />
                </>
              )}
              {user && (
                <>
                  <Route path="/info/:userId" element={<UsernameInfo />} />
                  <Route path="/profile" element={<Profile />} />
                  <Route path="/create-lesson" element={<CreateLesson />} />
                  <Route path="/lesson/:lessonId/flashcard" element={<CreateFlashcard />} />
                  <Route path="/user/lesson/:lessonId" element={<LessonUser />} />
                  <Route path="/user/lesson/" element={<MyLesson />} />
                  <Route path="/lesson/:lessonId/flashcard-study" element={<FlashcardStudy />} />
                  <Route path="/lesson/:lessonId/quiz-study" element={<QuizStudy />} />
                  <Route path="/flashcard-study/:lessonId/:status" element={<FlashcardHome />} />
                  <Route path="/lesson/:lessonId/quiz-study/:quizStudyId" element={<QuizStudyDo />} />
                  <Route path="/lesson/:lessonId/quiz-study/:quizStudyId/result" element={<QuizStudyResult />} />

                  <Route path="/lesson/:lessonId/test-study" element={<TestStudy />} />
                  <Route path="/lesson/:lessonId/test-study/:quizStudyId" element={<TestStudyDo />} />
                  <Route path="/lesson/:lessonId/test-study/:quizStudyId/result" element={<TestStudyResult />} />
                  <Route path="/view-history/set" element={<RecentViewSet />} />

                  <Route path="/lesson/:lessonId/create-flashcard-gemini" element={<CreateFlashcardGemini />} />

                  <Route path="/create-lesson-home" element={<CreateLessonHome />} />
                  <Route path="/gemini-topic" element={<CreateLessonGemini/>} />

                  {user.result.role === "ADMIN" && (
                    <>
                      <Route path="/admin/home" element={<AdminHome />} />
                      <Route path="/admin/users" element={<AdminUser />} />
                      <Route path="/admin/account/:accountId" element={< AdminUserEdit />} />
                      <Route path="/admin/statistics" element={<AdminStats />} />
                      <Route path="/admin/lessons" element={<AdminLesson />} />
                      <Route path="/admin/lessons/:id" element={<AdminLessonDetail />} />
                      <Route path="/admin/lessons/:id/stats" element={ <AdminLessonStats />} />

                    </>
                  )}
                </>
              )}
            </Routes>

          </Layout>
        </BrowserRouter>

      </MyDispatcherContext.Provider>
    </MyUserContext.Provider>
  );
}

export default App;