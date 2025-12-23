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
    
    // ============ CẤU HÌNH EMAIL GỬI OTP ============
    // Đây là email DÙNG ĐỂ GỬI OTP (không phải email nhận)
    // Bước 1: Đăng nhập Gmail này, vào Google Account > Security > Bật 2-Step Verification
    // Bước 2: Vào App passwords > Tạo password cho "Mail" > Copy 16 ký tự
    // Bước 3: Paste 16 ký tự vào SENDER_PASSWORD (không có dấu cách)
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
                    "<div style='font-family: Arial, sans-serif; max-width: 500px; margin: 0 auto; padding: 20px;'>" +
                    "<div style='background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); padding: 30px; border-radius: 10px 10px 0 0; text-align: center;'>" +
                    "<h1 style='color: white; margin: 0;'>🔐 Khôi phục mật khẩu</h1>" +
                    "</div>" +
                    "<div style='background: #f9f9f9; padding: 30px; border-radius: 0 0 10px 10px;'>" +
                    "<p style='color: #333; font-size: 16px;'>Xin chào,</p>" +
                    "<p style='color: #666; font-size: 14px;'>Bạn đã yêu cầu khôi phục mật khẩu cho tài khoản Quản Lý Thu Chi.</p>" +
                    "<div style='background: white; border: 2px dashed #667eea; border-radius: 10px; padding: 20px; text-align: center; margin: 20px 0;'>" +
                    "<p style='color: #666; margin: 0 0 10px 0; font-size: 14px;'>Mã OTP của bạn là:</p>" +
                    "<h2 style='color: #667eea; font-size: 36px; letter-spacing: 8px; margin: 0;'>" + otpCode + "</h2>" +
                    "</div>" +
                    "<p style='color: #999; font-size: 12px;'>⏰ Mã OTP có hiệu lực trong 5 phút.</p>" +
                    "<p style='color: #999; font-size: 12px;'>⚠️ Nếu bạn không yêu cầu khôi phục mật khẩu, vui lòng bỏ qua email này.</p>" +
                    "<hr style='border: none; border-top: 1px solid #eee; margin: 20px 0;'>" +
                    "<p style='color: #999; font-size: 11px; text-align: center;'>© 2024 Quản Lý Thu Chi - 5 Bông Hoa</p>" +
                    "</div>" +
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
