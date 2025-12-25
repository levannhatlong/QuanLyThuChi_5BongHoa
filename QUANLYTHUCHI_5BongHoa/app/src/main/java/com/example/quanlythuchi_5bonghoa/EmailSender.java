package com.example.quanlythuchi_5bonghoa;

import java.util.Properties;
import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class EmailSender {
    private static final String SENDER_EMAIL = "myhaho29@gmail.com";
    private static final String SENDER_PASSWORD = "upxdbfssppzkftbp"; // Thay bằng App Password 16 ký tự
    
    public interface EmailCallback {
        void onSuccess();
        void onError(String error);
    }
    
    public static void sendOTP(String recipientEmail, String otpCode, EmailCallback callback) {
        new Thread(() -> {
            try {
                Properties props = new Properties();
                props.put("mail.smtp.host", "smtp.gmail.com");
                props.put("mail.smtp.port", "587");
                props.put("mail.smtp.auth", "true");
                props.put("mail.smtp.starttls.enable", "true");
                props.put("mail.smtp.ssl.protocols", "TLSv1.2");
                
                Session session = Session.getInstance(props, new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(SENDER_EMAIL, SENDER_PASSWORD);
                    }
                });
                
                MimeMessage message = new MimeMessage(session);
                message.setFrom(new InternetAddress(SENDER_EMAIL, "Quản Lý Thu Chi"));
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(recipientEmail));
                message.setSubject("Mã OTP khôi phục mật khẩu - Quản Lý Thu Chi");
                
                String htmlContent = 
                    "<div style='font-family: Arial, sans-serif; padding: 20px; max-width: 400px;'>" +
                    "<h2 style='color: #333;'>Khôi phục mật khẩu</h2>" +
                    "<p>Mã OTP của bạn là:</p>" +
                    "<h1 style='color: #4CAF50; font-size: 32px; letter-spacing: 5px; background: #f5f5f5; padding: 15px; text-align: center;'>" + otpCode + "</h1>" +
                    "<p style='color: #666; font-size: 13px;'>Mã có hiệu lực trong 5 phút.</p>" +
                    "<p style='color: #999; font-size: 12px;'>Nếu bạn không yêu cầu, hãy bỏ qua email này.</p>" +
                    "<hr style='border: none; border-top: 1px solid #eee;'>" +
                    "<p style='color: #999; font-size: 11px;'>Quản Lý Thu Chi - 5 Bông Hoa</p>" +
                    "</div>";
                
                message.setContent(htmlContent, "text/html; charset=utf-8");
                
                Transport.send(message);
                callback.onSuccess();
                
            } catch (Exception e) {
                e.printStackTrace();
                callback.onError(e.getMessage());
            }
        }).start();
    }
    
    public static String generateOTP() {
        int otp = (int) (Math.random() * 900000) + 100000; // 6 số
        return String.valueOf(otp);
    }
}
