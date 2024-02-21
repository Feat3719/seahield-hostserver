package com.seahield.hostserver.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;

import com.seahield.hostserver.dto.EmailDto.Email;

import org.thymeleaf.TemplateEngine;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import java.util.Random;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final TemplateEngine templateEngine;
    private final AuthService authService;

    @Async("emailTaskExecutor")
    public void sendMail(Email email, String type) {
        String authNum = createCode();

        if (type.equals("password")) {
            authService.setTempPassword(email.getTo(), authNum);
        }

        MimeMessage mimeMessage = javaMailSender.createMimeMessage();

        try {
            createMimeMessageHelper(mimeMessage, email.getTo(), email.getSubject(), setContext(authNum, type));
            javaMailSender.send(mimeMessage);

            log.info("SUCCESS TO SEND EMAIL");

        } catch (MessagingException e) {
            log.info("FAIL TO SEND EMAIL");
            throw new RuntimeException(e);
        }
    }

    @Async("emailTaskExecutor")
    public void sendAuthEmail(String userEmail, Email email, String type) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            createMimeMessageHelper(mimeMessage, email.getTo(),
                    email.getSubject(), setContext(authService.findUserId(userEmail), type));
            javaMailSender.send(mimeMessage);
            log.info("SUCCESS TO SEND EMAIL");

        } catch (MessagingException e) {
            log.info("FAIL TO SEND EMAIL");
            throw new RuntimeException(e);
        }
    }

    private MimeMessageHelper createMimeMessageHelper(MimeMessage mimeMessage, String to, String subject, String text)
            throws MessagingException {
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, false, "UTF-8");
        mimeMessageHelper.setTo(to);
        mimeMessageHelper.setSubject(subject);
        mimeMessageHelper.setText(text, true);
        return mimeMessageHelper;
    }

    public String createCode() {
        Random random = new Random();
        StringBuilder key = new StringBuilder();

        for (int i = 0; i < 8; i++) {
            int index = random.nextInt(4);

            switch (index) {
                case 0:
                    key.append((char) (random.nextInt(26) + 97));
                    break;
                case 1:
                    key.append((char) (random.nextInt(26) + 65));
                    break;
                default:
                    key.append(random.nextInt(9));
            }
        }
        return key.toString();
    }

    public String setContext(String code, String type) {
        Context context = new Context();
        context.setVariable("code", code);
        return templateEngine.process(type, context);
    }
}