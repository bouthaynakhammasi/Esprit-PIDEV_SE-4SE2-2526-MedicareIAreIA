package com.aziz.demosec.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class SmsService {

    @Value("${sms.enabled:false}")
    private boolean smsEnabled;

    @Value("${twilio.account-sid:}")
    private String accountSid;

    @Value("${twilio.auth-token:}")
    private String authToken;

    @Value("${twilio.from-number:}")
    private String fromNumber;

    public void sendSms(String toPhone, String message) {
        String formattedPhone = formatPhoneNumber(toPhone);

        if (!smsEnabled) {
            log.info("[SMS MOCK] To: {} | Message: {}", formattedPhone, message);
            return;
        }

        try {
            Twilio.init(accountSid, authToken);
            Message.creator(
                    new PhoneNumber(formattedPhone),
                    new PhoneNumber(fromNumber),
                    message
            ).create();
            log.info("[SMS] Sent successfully to {}", maskPhone(formattedPhone));
        } catch (Exception e) {
            log.error("[SMS ERROR] Failed to send SMS to {}: {}", maskPhone(formattedPhone), e.getMessage());
        }
    }

    private String formatPhoneNumber(String phone) {
        if (phone == null || phone.isBlank()) return "";
        // Remove all spaces and dashes
        String cleaned = phone.replaceAll("[\\s-]", "");
        
        // If it's exactly 8 digits (typical local Tunisian format), prepend +216
        if (cleaned.matches("^\\d{8}$")) {
            return "+216" + cleaned;
        }
        
        // Ensure it starts with '+'
        if (!cleaned.startsWith("+")) {
            return "+" + cleaned;
        }
        
        return cleaned;
    }

    private String maskPhone(String phone) {
        if (phone == null || phone.length() <= 4) return phone;
        return phone.substring(0, 4) + "****" + phone.substring(phone.length() - 2);
    }
}
