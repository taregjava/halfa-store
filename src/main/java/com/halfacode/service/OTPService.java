package com.halfacode.service;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class OTPService {

    public String generateAndSendOTP(String phoneNumber) throws NumberParseException {
        int otp = generateRandomOTP(6);
        boolean otpSent = sendOTPToPhoneNumber(phoneNumber, otp);

        if (otpSent) {
            return Integer.toString(otp);
        } else {
            throw new RuntimeException("OTP sending failed");
        }
    }

    private int generateRandomOTP(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException("OTP length must be positive");
        }

        // Define the upper and lower bounds for random number generation
        int min = (int) Math.pow(10, length - 1);
        int max = (int) Math.pow(10, length) - 1;

        // Generate a random number within the specified range
        return min + new Random().nextInt(max - min + 1);
    }

    private boolean sendOTPToPhoneNumber(String phoneNumber, int otp) throws NumberParseException {
        // Get an instance of PhoneNumberUtil
        PhoneNumberUtil phoneNumberUtil = PhoneNumberUtil.getInstance();

        // Parse the provided phone number with the appropriate country code
        Phonenumber.PhoneNumber formattedNumber = phoneNumberUtil.parse(phoneNumber, "US");

        // Validate the parsed phone number
        if (!phoneNumberUtil.isValidNumber(formattedNumber)) {
            throw new IllegalArgumentException("Invalid phone number");
        }

        // Convert the PhoneNumber object back to a formatted string
        String formattedPhoneNumber = phoneNumberUtil.format(formattedNumber, PhoneNumberUtil.PhoneNumberFormat.E164);

        // Implement logic to send the OTP to the formatted phone number
        boolean otpSent = sendOtpUsingSmsGateway(formattedPhoneNumber, otp);

        // Return true if OTP is successfully sent
        return otpSent;
    }

    private boolean sendOtpUsingSmsGateway(String formattedPhoneNumber, int otp) {
        // Implement your logic to send the OTP via an SMS gateway
        // This could involve making an API request to the SMS gateway provider
        // Return true if OTP is successfully sent
        return true; // Replace with actual logic
    }

    /*private boolean sendOtpUsingSmsGateway(String formattedPhoneNumber, int otp) {
    try {
        // Set up the HTTP client to communicate with the SMS gateway API
        HttpClient httpClient = HttpClient.newHttpClient();

        // Construct the request URL and payload based on the SMS gateway's API documentation
        String apiEndpoint = "https://sms-gateway-api.com/send-otp";
        String apiKey = "your-api-key";
        String message = "Your OTP code is: " + otp;

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(apiEndpoint))
            .header("Authorization", "Bearer " + apiKey)
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString("{\"to\": \"" + formattedPhoneNumber + "\", \"message\": \"" + message + "\"}"))
            .build();

        // Send the HTTP request
        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());

        // Process the response and return true if the OTP was successfully sent
        if (response.statusCode() == 200) {
            return true;
        } else {
            System.err.println("Failed to send OTP: " + response.body());
            return false;
        }
    } catch (IOException | InterruptedException e) {
        e.printStackTrace();
        return false;
    }
}*/
}