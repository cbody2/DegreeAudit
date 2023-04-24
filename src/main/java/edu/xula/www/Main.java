package edu.xula.www;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        System.out.println("Welcome to..");
        StartScreen();

        User inputUser = UserInput();
    }

    public static void StartScreen(){

        int width = 200;
        int height = 30;

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics graphics = image.getGraphics();

        Graphics2D graphics2D = (Graphics2D) graphics;
        graphics2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        graphics2D.drawString("DEGREE AUDIT", 15, 24);

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