package edu.xula.www;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.*;
import java.util.List;


public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome to..");
        StartScreen();

        User inputUser = UserInput();

        List<String> transcripts = getTranscriptFilenames();
        if(!hasTranscript(transcripts, inputUser.getUserIdentification())) {
            System.out.println("Please upload a transcript");
        } else {
            String currentSemester =
                    getSemester(LocalDate.now().getMonthValue(), LocalDate.now().getYear());
            String latestTranscriptSemester =
                    getLatestTranscriptSemester(transcripts,inputUser.getUserIdentification());
            System.out.println("Latest Transcript: " + latestTranscriptSemester);
            if (transcriptNeedsUpdate(currentSemester, latestTranscriptSemester))
                System.out.println("Please upload updated transcript");
        }

//        catalogYear();

    }

    public static boolean transcriptNeedsUpdate(String current, String transcript) {
        String[] currentParts = current.split(" ");
        String[] transcriptParts = transcript.split(" ");
        if (transcriptParts.length != 2 || current == null || transcript == null)
            return false;
        if (Integer.parseInt(currentParts[1]) > Integer.parseInt(transcriptParts[1]))
            return true;
        else {
            String currentSemester = currentParts[0];
            String transcriptSemester = transcriptParts[0];
            if (currentSemester.equalsIgnoreCase("Spring"))
                return false;
            else if (currentSemester.equalsIgnoreCase("Summer"))
                return transcriptSemester.equalsIgnoreCase("Spring");
            else
                return transcriptSemester.equalsIgnoreCase(currentSemester);
        }
    }

    public static String getSemesterFromFilename(String filename) {
        String[] parts = filename.split("_");
        if (parts.length != 3 || filename == null)
            return null;
        char[] dateCharacters = parts[2].toCharArray();
        String year = new StringBuilder().append(dateCharacters[0]).append(dateCharacters[1])
                .append(dateCharacters[2]).append(dateCharacters[3]).toString();
        String month = new StringBuilder().append(dateCharacters[4]).append(dateCharacters[5]).toString();
        return getSemester(Integer.parseInt(month), Integer.parseInt(year));
    }

    public static String getLatestTranscriptSemester(List<String> transcripts, int id) {
        return getSemesterFromFilename(getLatestTranscript(transcripts, id));
    }

    public static String getLatestTranscript(List<String> transcripts, int id) {
        Stack<String> userTranscripts = new Stack<>();
        for (int i = 0; i < transcripts.size(); i++) {
            String current = transcripts.get(i);
            String[] parts = current.split("_");
            if (Integer.parseInt(parts[0]) == id)
                userTranscripts.push(current);
        }
        return userTranscripts.pop();
    }

    public static String getSemester(int month, int year) {
        String semester = "";
        if (month >= 1 && month <= 5)
            semester = "Spring";
        else if (month >= 6 && month <= 7)
            semester = "Summer";
        else if (month >= 8 && month <= 12)
            semester = "Fall";
        return semester + " " + year;
    }

    public static List<String> getTranscriptFilenames() {
        List<String> transcripts = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Paths.get("src/main/Transcripts"))) {
            for (Path path : stream) {
                if (!Files.isDirectory(path)) {
                    transcripts.add(path.getFileName()
                            .toString());
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return transcripts;
    }

    public static boolean hasTranscript(List<String> transcripts, int userIdentification) {
        for (String filename : transcripts) {
            String[] parts = filename.split("_");
            if (Integer.parseInt(parts[0]) == userIdentification)
                return true;
        }
        return false;
    }

    public static void StartScreen(){
        /**Prints out an interesting start-up screen to the console. */

        int width = 200;
        int height = 30;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = image.getGraphics();

        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics2D.drawString("D E G R E E  A U D I T", 15, 24);

        for (int y = 0; y < height; y++) {
            StringBuilder stringBuilder = new StringBuilder();
            for (int x = 0; x < width; x++) {
                stringBuilder.append(image.getRGB(x, y) == -16777216 ? "*" : " ");
            }

            if (stringBuilder.toString().trim().isEmpty()) {
                continue;
            }
            System.out.println(stringBuilder);
        }
    }

    public static User UserInput(){
        /**Returns/Checks a students 900 number for identification.*/
        Scanner userInput = new Scanner(System.in);
        System.out.println("Please input your XULA 900 number:");
        String userIdentification = userInput.nextLine();

        if (userIdentification.length() != 9){
            System.out.println("Invalid 900 number length. Please try again.\n");
            return UserInput();
        }

        try {
            Integer.parseInt(userIdentification);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid 900 number. Please try again.\n");
            return UserInput();
        }

        return new User(Integer.parseInt(userIdentification));

    }
}