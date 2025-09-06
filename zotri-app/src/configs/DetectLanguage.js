
const DetectLanguage = (text) => {
  if (/[\u4e00-\u9fff]/.test(text)) return "zh";       // Chinese
  if (/[\u3040-\u30ff]/.test(text)) return "ja";       // Japanese
  if (/[\uac00-\ud7af]/.test(text)) return "ko";       // Korean
  if (/[\u0E00-\u0E7F]/.test(text)) return "th";       // Thai
  if (/[\u0590-\u05FF]/.test(text)) return "he";       // Hebrew
  if (/[\u0600-\u06FF]/.test(text)) return "ar";       // Arabic
  if (/[\u0400-\u04FF]/.test(text)) return "ru";       // Russian
  if (/[\u0900-\u097F]/.test(text)) return "hi";       // Hindi
  if (/[ăâđêôơưáàảãạấầẩẫậắằẳẵặéèẻẽẹếềểễệóòỏõọốồổỗộớờởỡợúùủũụứừửữựíìỉĩịýỳỷỹỵ]/i.test(text)) {
    return "vi";
  }
  if (/[a-zA-Z]/.test(text)) return "en";              // English fallback
  return "en";
};

export default DetectLanguage;