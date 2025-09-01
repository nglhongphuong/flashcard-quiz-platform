package com.zotriverse.demo.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.zotriverse.demo.dto.request.FlashcardRequest;
import com.zotriverse.demo.dto.response.FlashcardResponse;
import com.zotriverse.demo.exception.AppException;
import com.zotriverse.demo.exception.ErrorCode;
import com.zotriverse.demo.mapper.FlashcardMapper;
import com.zotriverse.demo.pojo.Flashcard;
import com.zotriverse.demo.pojo.Lesson;
import com.zotriverse.demo.repository.FlashCardRepository;
import com.zotriverse.demo.repository.LessonRepository;
import lombok.extern.slf4j.Slf4j;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
@Slf4j
public class FlashcardService {
    @Autowired
    private FlashCardRepository flashCardRepository;
    @Autowired
    private LessonRepository lessonRepository;

    @Autowired
    private FlashcardMapper flashcardMapper;

    @Autowired
    private Cloudinary cloudinary;


    //create - owner user
    public List<FlashcardRequest> parseFlashcardFile(MultipartFile file) {
        String filename = file.getOriginalFilename();
        if (filename == null) {
            throw new AppException(ErrorCode.INVALID_FILE);
        }

        try {
            if (filename.endsWith(".csv")) {
                return parseCSV(file);
            } else if (filename.endsWith(".xlsx")) {
                return parseExcel(file);
            } else {
                throw new AppException(ErrorCode.INVALID_FILE);
            }
        } catch (IOException e) {
            throw new AppException(ErrorCode.FILE_PROCESSING_ERROR);
        }
    }

    private List<FlashcardRequest> parseCSV(MultipartFile file) throws IOException {
        List<FlashcardRequest> list = new ArrayList<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            boolean isFirstLine = true;
            while ((line = reader.readLine()) != null) {
                if (isFirstLine) { // bỏ header
                    isFirstLine = false;
                    continue;
                }
                String[] parts = line.split(",", -1); // giữ cả phần rỗng
                if (parts.length >= 2) {
                    String word = parts[0].trim();
                    String def = parts[1].trim();
                    String img = parts.length >= 3 ? parts[2].trim() : null;
                    list.add(new FlashcardRequest(word, def, img));
                }
            }
        }
        return list;
    }

    private List<FlashcardRequest> parseExcel(MultipartFile file) throws IOException {
        List<FlashcardRequest> list = new ArrayList<>();
        try (InputStream is = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(is)) {

            Sheet sheet = workbook.getSheetAt(0);
            boolean isFirstRow = true;
            for (Row row : sheet) {
                if (isFirstRow) { // bỏ header
                    isFirstRow = false;
                    continue;
                }
                String word = getCellValue(row.getCell(0));
                String def = getCellValue(row.getCell(1));
                String img = row.getCell(2) != null ? getCellValue(row.getCell(2)) : null;

                if (word != null && def != null) {
                    list.add(new FlashcardRequest(word, def, img));
                }
            }
        }
        return list;
    }

    private String getCellValue(Cell cell) {
        if (cell == null) return null;
        if (cell.getCellType() == CellType.STRING) {
            return cell.getStringCellValue().trim();
        } else if (cell.getCellType() == CellType.NUMERIC) {
            return String.valueOf(cell.getNumericCellValue());
        } else {
            return null;
        }
    }

    public List<FlashcardResponse> createFlashcardSet(int lessonId, List<FlashcardRequest> requests) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new AppException(ErrorCode.CANNOT_FOUND));
        List<Flashcard> flashcards = requests.stream()
                .map(req -> Flashcard.builder()
                        .word(req.getWord())
                        .definition(req.getDefinition())
                        .image(req.getImage())
                        .lessonId(lesson)
                        .build())
                .toList();
        List<Flashcard> results = flashCardRepository.saveAll(flashcards);

        // Chuyển đổi sang FlashcardResponse
        return results.stream()
                .map(flashcardMapper::toFlashcardResponse)
                .toList();
    }

    //    public List<FlashcardRequest> parseFromRawText(String rawText, String cardDelimiter, String wordDelimiter) {
//        if (rawText == null || cardDelimiter == null || wordDelimiter == null) {
//            throw new AppException(ErrorCode.INVALID_INPUT);
//        }
//
//        // Tránh lỗi nếu delimiter là "\n" kiểu chuỗi JSON
//        if (cardDelimiter.equals("\\n")) cardDelimiter = "\n";
//        if (cardDelimiter.equals("\\r\\n")) cardDelimiter = "\r\n";
//
//        List<FlashcardRequest> flashcards = new ArrayList<>();
//        String[] cards = rawText.split(Pattern.quote(cardDelimiter));
//
//        for (String card : cards) {
//            String[] parts = card.split(Pattern.quote(wordDelimiter), -1);
//            if (parts.length >= 2) {
//                flashcards.add(new FlashcardRequest(
//                        parts[0].trim(),
//                        parts[1].trim(),
//                        parts[2].trim()//image
//                ));
//            }
//        }
//        return flashcards;
//    }
    public List<FlashcardRequest> parseFromRawText(String rawText, String cardDelimiter, String wordDelimiter) {
        if (rawText == null || cardDelimiter == null || wordDelimiter == null) {
            throw new AppException(ErrorCode.INVALID_INPUT);
        }

        if (cardDelimiter.equals("\\n")) cardDelimiter = "\n";
        if (cardDelimiter.equals("\\r\\n")) cardDelimiter = "\r\n";

        List<FlashcardRequest> flashcards = new ArrayList<>();
        String[] cards = rawText.split(Pattern.quote(cardDelimiter));

        for (String card : cards) {
            String[] parts = card.split(Pattern.quote(wordDelimiter), 3);

            if (parts.length >= 2) {
                String word = parts[0].trim();
                String definition = parts[1].trim();
                String image = parts.length == 3 ? parts[2].trim() : null;

                flashcards.add(new FlashcardRequest(word, definition, image));
            }
        }

        return flashcards;
    }

    public FlashcardResponse createFlashcard(int lessonId, Map<String, String> params, MultipartFile avatar) {
        Lesson lesson = lessonRepository.findById(lessonId)
                .orElseThrow(() -> new AppException(ErrorCode.CANNOT_FOUND));
        String word = params.get("word");
        String definition = params.get("definition");
        if (word == null || definition == null || word.isBlank() || definition.isBlank()) {
            throw new AppException(ErrorCode.INVALID_PARAMS);
        }
        String imageUrl = null;
        // Upload ảnh nếu có
        if (avatar != null && !avatar.isEmpty()) {
            try {
                File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + avatar.getOriginalFilename());
                try (FileOutputStream fos = new FileOutputStream(convFile)) {
                    fos.write(avatar.getBytes());
                }
                var pic = cloudinary.uploader().upload(convFile, ObjectUtils.asMap("folder", "/flashcards/"));
                imageUrl = pic.get("url").toString();
            } catch (IOException e) {
                throw new AppException(ErrorCode.INVALID_FILE);
            }
        }
        // Lưu flashcard
        Flashcard flashcard = Flashcard.builder()
                .word(word)
                .definition(definition)
                .image(imageUrl)
                .lessonId(lesson)
                .build();
        flashCardRepository.save(flashcard);
        return flashcardMapper.toFlashcardResponse(flashcard);
    }

    public FlashcardResponse updateFlashcard(int flashcardId, Map<String, String> params, MultipartFile avatar) {
        Flashcard flashcard = flashCardRepository.findById(flashcardId)
                .orElseThrow(() -> new AppException(ErrorCode.CANNOT_FOUND));

        // Cập nhật từng trường nếu có
        if (params.containsKey("word") && !params.get("word").isBlank()) {
            flashcard.setWord(params.get("word").trim());
        }

        if (params.containsKey("definition") && !params.get("definition").isBlank()) {
            flashcard.setDefinition(params.get("definition").trim());
        }

        // Cập nhật ảnh nếu có
        if (avatar != null && !avatar.isEmpty()) {
            String imageUrl = null;
            try {
                File convFile = new File(System.getProperty("java.io.tmpdir") + "/" + avatar.getOriginalFilename());
                try (FileOutputStream fos = new FileOutputStream(convFile)) {
                    fos.write(avatar.getBytes());
                }
                var pic = cloudinary.uploader().upload(convFile, ObjectUtils.asMap("folder", "/flashcards/"));
                imageUrl = pic.get("url").toString();
                flashcard.setImage(imageUrl);

            } catch (IOException e) {
                throw new AppException(ErrorCode.INVALID_FILE);
            }
        }

        flashCardRepository.save(flashcard);
        return flashcardMapper.toFlashcardResponse(flashcard);
    }

    public void deleteFlashcard(int flashcardId) {
        Flashcard flashcard = flashCardRepository.findById(flashcardId)
                .orElseThrow(() -> new AppException(ErrorCode.CANNOT_FOUND));
        flashCardRepository.delete(flashcard);
    }

    public FlashcardResponse getFlashcardById(int flashcardId) {
        Flashcard flashcard = this.flashCardRepository.findById(flashcardId).orElseThrow(() -> new AppException(ErrorCode.CANNOT_FOUND));
        return flashcardMapper.toFlashcardResponse(flashcard);
    }

}
