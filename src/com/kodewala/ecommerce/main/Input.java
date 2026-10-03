//package com.kodewala.ecommerce.main;
//
//import java.util.Scanner;
//
//public class Input {
//	
////	private static final Scanner sc = new Scanner(System.in);
//	
//	public static int readInt(String prompt) {
//        while (true) {
//            System.out.print(prompt);
//            try {
//                int val = Integer.parseInt(sc.nextLine().trim());
//                return val;
//            } catch (NumberFormatException e) {
//                System.out.println("  Please enter a valid integer.");
//            }
//        }
//    }
//	
//	public static double readDouble(String prompt) {
//        while (true) {
//            System.out.print(prompt);
//            try {
//                double val = Double.parseDouble(sc.nextLine().trim());
//                return val;
//            } catch (NumberFormatException e) {
//                System.out.println("  Please enter a valid number.");
//            }
//        }
//    }
//
//    public static String readLine(String text) {
//    	while(true) {
//    		System.out.print(text);
//    		try {
//    			String input = sc.nextLine().trim();
//    			if(input == null || input.isEmpty()) throw new IllegalArgumentException();
//    			return input;
//			} catch (IllegalArgumentException e) {
//				System.out.println("Please Enter a" + text);
//			}
//    	} 
//    }
//    
//    sc.close;
//
//}
