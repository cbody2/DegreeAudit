package edu.xula.www;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = new User(900200697);
        testUser.setMajor("Data Science");
        testUser.setTranscript(new ArrayList<>());
    }

    @AfterEach
    void tearDown() {
        testUser = null;
    }

    @Test
    void oldTranscriptTest() {
        String current = "Spring 2023";
        String transcript = "Fall 2022";
        assertTrue(Main.transcriptNeedsUpdate(current,transcript));
    }

    @Test
    void currentTranscriptTest() {
        String current = "Spring 2023";
        String transcript = "Spring 2023";
        assertFalse(Main.transcriptNeedsUpdate(current,transcript));
    }

    @Test
    void incorrectSemesterTest() {
        String current = "Spring 2023";
        String transcript = "Spring";
        assertFalse(Main.transcriptNeedsUpdate(current,transcript));
    }

    @Test
    void fallSemesterFileTest() {
        String filename = "900212345_transcript_20220925.txt";
        assertEquals("Fall 2022", Main.getSemesterFromFilename(filename));
    }

    @Test
    void springSemesterFileTest() {
        String filename = "900212345_transcript_20230325.txt";
        assertEquals("Spring 2023", Main.getSemesterFromFilename(filename));
    }

    @Test
    void getSemesterIncorrectFilenameTest() {
        String filename = "transcript_20230325.txt";
        assertNull(Main.getSemesterFromFilename(filename));
    }

    @Test
    void getLatestTranscriptSemesterTest() {
        List<String> transcripts = new ArrayList<>(Arrays.asList("900212345_transcript_20211001.txt","900212345_transcript_20221001.txt"));
        assertEquals("Fall 2022", Main.getLatestTranscriptSemester(transcripts, 900212345));
    }

    @Test
    void getLatestTranscriptTest() {
        List<String> transcripts = new ArrayList<>(Arrays.asList("900212345_transcript_20211001.txt",
                "900132365_transcript_20221001.txt", "900212345_transcript_20221001.txt"));
        assertEquals("900212345_transcript_20221001.txt", Main.getLatestTranscript(transcripts, 900212345));
    }

    @Test
    void getFallSemesterTest() {
        assertEquals("Fall 2022", Main.getSemester(8, 2022));
    }

    @Test
    void getSpringSemesterTest() {
        assertEquals("Spring 2023", Main.getSemester(2, 2023));
    }

    @Test
    void getSummerSemesterTest() {
        assertEquals("Summer 2021", Main.getSemester(6, 2021));
    }

    @Test
    void getTranscriptFilenamesTest() {
        List<String> transcripts = Main.getTranscriptFilenames();
        assertNotEquals(0, transcripts.size());
    }

    @Test
    void noTranscriptTest() {
        List<String> transcripts = new ArrayList<>(Arrays.asList("900212345_transcript_20211001.txt","900212345_transcript_20221001.txt"));
        assertFalse(Main.hasTranscript(transcripts, 123456789));
    }

    @Test
    void hasTranscriptTest() {
        List<String> transcripts = new ArrayList<>(Arrays.asList("900212345_transcript_20211001.txt","900212345_transcript_20221001.txt"));
        assertTrue(Main.hasTranscript(transcripts, 900212345));
    }

    @Test
    void catalogYearTest(){
//        assertInstanceOf(ClassRequirements.class, Main.catalogYear(testUser));
    }

    @Test
    void majorSelectTest(){
        assertEquals("Data Science", testUser.getMajor());
    }

    @Test
    void userInputTest() {
        assertEquals(User.class, testUser.getClass());
    }

    @Test
    void createUserTranscriptTest(){
        assertInstanceOf(ArrayList.class, testUser.getTranscript());
        List<String> transcripts = new ArrayList<>(Arrays.asList("900736152_transcript_20230325","900212345_transcript_20211001.txt","900212345_transcript_20221001.txt"));
        assertThrows(EmptyStackException.class, () -> Main.createUserTranscript(transcripts, testUser));
        testUser.setUserIdentification(900212345);
        assertThrows(FileNotFoundException.class, () -> Main.createUserTranscript(transcripts, testUser));
        testUser.setUserIdentification(900736152);
        assertInstanceOf(ArrayList.class, Main.createUserTranscript(transcripts, testUser));
    }

}