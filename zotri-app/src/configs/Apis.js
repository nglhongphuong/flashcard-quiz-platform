import axios from "axios";
import cookie from 'react-cookies'

// const BASE_URL = 'http://localhost:8080/zotri';
const BASE_URL = 'https://flashcard-quiz-platform.onrender.com/zotri';

const HF_BASE_URL = "https://nglhongphuong-flashcard-generate.hf.space";


export const endpoints = {
    login: '/auth/login',
    account: '/account',
    register: '/account/create',
    account_info: '/account/info',
    lesson: '/lesson/',
    lesson_update: (lessonId) => `/lesson/${lessonId}`,
    rating: (lessonId) => `/lesson/${lessonId}/rating`,
    schedule :(lessonId) => `/lesson/${lessonId}/schedule`,
    lesson_detail: (lessonId) => `/lesson/${lessonId}/flashcards`,
    flashcard_manual: (lessonId) => `/lesson/${lessonId}/flashcards/manual-json`,
    flashcard_upload: (lessonId) => `/lesson/${lessonId}/flashcards/upload`,
    // flashcard_formdata : (lessonId) => `/lesson/${lessonId}/flashcards/manual`,


    lesson_create: '/lesson/create',
    flashcard: (flashcardId) => `/flashcard/${flashcardId}`,
    flashcard_flashcard_study: (flashcardId) => `/flashcard/${flashcardId}/flashcard-study`,//update trang  thai status
    flashcard_study: (lessonId) => `/lesson/${lessonId}/flashcard-study`,
    flashcard_study_status: (lessonId) => `/lesson/${lessonId}/flashcard-study/status`,
    flashcard_study_stats: (lessonId) => `/lesson/${lessonId}/flashcard-study/stats`,
    flashcard_study_reset: (lessonId) => `/lesson/${lessonId}/flashcard-study/reset`,//reset ve not_learned
    quiz_study: (lessonId) =>  `/lesson/${lessonId}/quiz-study`, //PUT_GET danh sach quiz_study thuoc lessonId cua user
    quiz_study_detail: (lessonId,quizStudyId) =>  `/lesson/${lessonId}/quiz-study/${quizStudyId}`,//DEL_GET Chi tiet quiz study 
    quiz_history: (quizHistoryId) => `/quiz-history/${quizHistoryId}`, //put delete quiz history
    
    quiz_history_bulk: '/quiz-history-bulk',

    test_study: (lessonId) => `/lesson/${lessonId}/test-study`,
    test_study_detail:  (lessonId,quizStudyId) =>  `/lesson/${lessonId}/quiz-study/${quizStudyId}`,
    test_study_reset: (quizStudyId) => `/quiz-study/${quizStudyId}/reset`,
    
    comment:(commentId) => `/comment/${commentId}`,
    view_history : `/view-history/info`,
    admin_user: `/admin/accounts`,
    admin_user_action: (accountId) => `/admin/accounts/${accountId}`,
    admin_stats: (year) => `/admin/stats/${year}`,
    admin_lesson_stats: (lessonId, year) => `/admin/lesson/${lessonId}/stats/${year}`,
    admin_lesson: (lessonId) => `/admin/lesson/${lessonId}`,
    admin_flashcard: (lessonId) => `lesson/${lessonId}/flashcards/manual`, // truyen params {word, definition, image neu co}


    flashcard_ai_generate: `${HF_BASE_URL}/generate-flashcards`,
    flashcard_ai_generate_by_topic: `${HF_BASE_URL}/generate-flashcards-by-topic`,


}


export const authApis = () => {
    return axios.create({
        baseURL: BASE_URL,
        headers: {
            'Authorization': `Bearer ${cookie.load('token')}`
        }
    })
}

export default axios.create({
    baseURL: BASE_URL
});