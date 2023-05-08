package edu.xula.www;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.Console;
import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.time.LocalDate;
import java.util.*;
import java.util.List;


public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome to..");
        startScreen();

        User inputUser = userInput();

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

        inputUser.setTranscript(createUserTranscript(transcripts, inputUser));

        majorSelect(inputUser);

        ClassRequirements curriculum = catalogYear(inputUser);

        degreeAuditPrompt(curriculum, inputUser);

    }

    private static ArrayList<String> createUserTranscript(List<String> transcripts, User user) {
        /**Create a list of user taken classes.*/
        ArrayList<String> userClasses = new ArrayList<String>();
        try {
            File transcript = new File("src/main/Transcripts/"
                    + getLatestTranscript(transcripts, user.getUserIdentification()));
            Scanner transcriptReader = new Scanner(transcript);
            transcriptReader.nextLine();
            transcriptReader.nextLine();
            transcriptReader.nextLine();

            while (transcriptReader.hasNextLine()){
                String line = transcriptReader.nextLine();
                if (line.isEmpty())
                    break;
                String[] classes = line.split(" ");
                if (classes[3].equalsIgnoreCase("a")
                        || classes[3].equalsIgnoreCase("b")
                        || classes[3].equalsIgnoreCase("c"))
                    classes[3] = "C";
                String takenClasses = classes[0] + " " + classes[1] + " " + classes[2] + " " + classes[3];
                userClasses.add(takenClasses);

            }

            return userClasses;

        } catch (FileNotFoundException e) {
            System.out.println("Please upload a transcript to view your degree audit.");
            System.exit(0);
        }

        return new ArrayList<>();
    }

    private static void degreeAudit(ClassRequirements curriculum, User user){
        /**Output a user's degree audit based on completed classes on transcript.*/
        List<String> completedClasses = new ArrayList<>();
        Set<String[]> coreClasses = curriculum.getCore();
        Set<String[]> coreRemoved = new HashSet<>();
        Set<String> majorClasses = curriculum.getMajor();
        Set<String> gutterClasses = new HashSet<>();

        if(user.getTranscript().isEmpty())
            System.exit(0);

        for (String userClass : user.getTranscript()){
            if (majorClasses.contains(userClass)) {
                completedClasses.add(userClass);
                majorClasses.remove(userClass);
            } else if (!coreClasses.isEmpty()) {
                for (String[] classSet : coreClasses){
                    for (String subClass : classSet){
                        if (userClass.equalsIgnoreCase(subClass)){
                            completedClasses.add(userClass);
                            coreRemoved.add(classSet);
                        } else {
                            gutterClasses.add(userClass);
                        }
                    }
                }
            }
        }

        coreClasses.removeAll(coreRemoved);
        completedClasses.forEach(gutterClasses::remove);
        System.out.println("Here are your completed Classes:\n" + completedClasses);
        System.out.println("\nHere are your remaining Major Classes:");
        for (String major : majorClasses)
            System.out.println(major);
        System.out.println("\nHere are your Core Classes (Only take one from each list):");
        for (String[] requirement : coreClasses) {
            System.out.println(Arrays.toString(requirement));
        }
        System.out.println("\nFree Electives:\n" + gutterClasses);

    }

    public static void degreeAuditPrompt(ClassRequirements curriculum, User user) {
        /**Prompt a user to view their degree audit.*/
        System.out.println("\nWould you like to view your Degree Audit thus far?\nY/n?");
        Scanner inputAnswer = new Scanner(System.in);
        String userAnswer = inputAnswer.nextLine();

        if (userAnswer.equalsIgnoreCase("n")) {
            System.out.println("Thank you for using our Degree Audit system. Have a great day!");
            System.exit(0);
        } else if (userAnswer.equalsIgnoreCase("y")){
            degreeAudit(curriculum, user);
        } else {
            System.out.println("Invalid input. Please either type Y/n.");
            degreeAuditPrompt(curriculum, user);
        }
    }

    public static ClassRequirements catalogYear(User inputUser){
        /**Output curriculum requirements based on user's selected major.*/
        Scanner inputYear = new Scanner(System.in);
        System.out.println("Latest curriculum date based on " + inputUser.getMajor() + " major.\n");

        System.out.println("What year would you like to view the curriculum for? ");
        String userYear = inputYear.nextLine();

        if (userYear.length() != 4){
            System.out.println("Invalid year. Defaulting to current year: " + LocalDate.now().getYear());
            userYear = String.valueOf(LocalDate.now().getYear());
        }


        try {
            Integer.parseInt(userYear);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid year. Defaulting to current year: " + LocalDate.now().getYear());
            userYear = String.valueOf(LocalDate.now().getYear());
        }

        try {
            File curriculumFile = new File("src/main/Curriculums/"
                    + inputUser.getMajor().replace(" ", "_")
                    + "_" + String.valueOf(userYear) +".txt");
            Scanner myReader = new Scanner(curriculumFile);
            Set<String> majorRequirements = new HashSet<>();
            Set<String[]> coreRequirements = new HashSet<>();
            myReader.nextLine();
            myReader.nextLine();
            System.out.println(myReader.nextLine() + " Requirements:");

            while (myReader.hasNextLine()) {
                String line = myReader.nextLine();
                if (line.isEmpty())
                    break;
                String[] curriculum = line.split(" ");
                String requirement = curriculum[0] + " " + curriculum[1] + " " + curriculum[2] + " " + curriculum[3];
                majorRequirements.add(requirement);
                System.out.println(curriculum[0] + " " + curriculum[1] + " - Passing Grade: " + curriculum[3]);
            }

            curriculumFile = new File("src/main/Curriculums/Core_" + String.valueOf(userYear) + ".txt");
            myReader = new Scanner(curriculumFile);
            while (myReader.hasNextLine()){
                String line = myReader.nextLine();
                if (line.isEmpty())
                    break;
                String[] curriculum = line.strip().split(",");
                coreRequirements.add(curriculum);

            }


            System.out.println("\nCore Requirements (take one from each section):");
            for (String[] requirement : coreRequirements) {
                System.out.println(Arrays.toString(requirement));
            }

            return new ClassRequirements(majorRequirements, coreRequirements);
        } catch (FileNotFoundException e) {
            System.out.println("Error - Curriculum file not found.");
        }

        return new ClassRequirements();
    }

    public static void majorSelect(User inputUser){
        /**Set the user's major from a list of majors.*/
        Scanner userInput = new Scanner(System.in);
        System.out.println("Please select a major from the following list:");
        System.out.println("Computer Science\nComputer Information Systems\nData Science\nBioinformatics\nYour Selection: ");
        String userMajor = userInput.nextLine();

        if (userMajor.strip().equalsIgnoreCase("computer science")) {
            inputUser.setMajor("Computer Science");
        } else if (userMajor.strip().equalsIgnoreCase("data science")){
            inputUser.setMajor("Data Science");
        } else if (userMajor.strip().equalsIgnoreCase("bioinformatics")) {
            inputUser.setMajor("Bioinformatics");
        } else if (userMajor.strip().equalsIgnoreCase("computer information systems")){
            inputUser.setMajor("Computer Information Systems");
        } else{
            System.out.println("Incorrect major input.\n");
            majorSelect(inputUser);
        }
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

    public static void startScreen(){
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

    public static User userInput(){
        /**Returns/Checks a students 900 number for identification.*/
        Scanner userInput = new Scanner(System.in);
        System.out.println("Please input your XULA 900 number:");
        String userIdentification = userInput.nextLine();

        if (userIdentification.length() != 9){
            System.out.println("Invalid 900 number length. Please try again.\n");
            return userInput();
        }

        try {
            Integer.parseInt(userIdentification);
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid 900 number. Please try again.\n");
            return userInput();
        }

        return new User(Integer.parseInt(userIdentification));

    }

    public static File uploadFile(File sourceFile, String directory) throws IOException {
        String fileName = sourceFile.getName();
        Path targetPath = Path.of(directory, fileName);
        Files.copy(sourceFile.toPath(), targetPath, StandardCopyOption.REPLACE_EXISTING);
        return targetPath.toFile();
    }

}