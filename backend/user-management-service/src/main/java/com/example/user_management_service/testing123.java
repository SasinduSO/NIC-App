package com.example.user_management_service;

public class testing123 {
    public static void main(String[] args) {
        String pw1 = "$2a$10$CFHZCjf7XhGxY.gBpuiHY.fnQcd7cBE1jbFxCFe3gq1TI7wPyEQNG";
        String pw2 = "$2a$10$rTG.RtARbUxdFlc4lGAIuO2YC0HMb3WTlTMlKVEGbSa1rkTNWV2Jq";

        // Compare the lengths of the hashed passwords
        if (pw1.length() == pw2.length()) {
            System.out.println("Hashed passwords have same lengths, hence they are not matching");
        } 
        
        if (pw1.equals(pw2)) {
            System.out.println("Matching passwords");
        } else {
            System.out.println("NOT Matching passwords");
        }
    }
}
