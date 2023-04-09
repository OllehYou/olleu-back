package com.example.olleuback.utils.service.email;

import com.example.olleuback.common.exception.OlleUException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Locale;
import java.util.Map;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring6.SpringTemplateEngine;

@Service
@RequiredArgsConstructor
public class EmailService {
    private final String MAIL_SUBJECT = "mail_subject";
    private final String MAIL_SENDER_AKA = "올래유";
    private final String MAIL_SENDER_MAIL = "olleu159@gmail.com";
    private final String PASSWORD_MAIL_TEMPLATE = "resetPasswordTemplate";
    private final String AUTH_CODE_MAIL_TEMPLATE = "emailCodeTemplate";
    private final JavaMailSender mailSender;
    private final SpringTemplateEngine htmlTemplateEngine;
    private final ObjectMapper objectMapper;

    public void sendRestPasswordMail(String email, String password) {
        EmailContextDto emailContextDto =
                EmailContextDto.ofCreateResetPasswordInfo(email, password);
        this.sendMail(emailContextDto, PASSWORD_MAIL_TEMPLATE);
    }

    public void sendMailAuthCode(String email, String authCode) {
        EmailContextDto emailContextDto =
                EmailContextDto.ofCreateAuthCodeInfo(email, authCode);
        this.sendMail(emailContextDto, AUTH_CODE_MAIL_TEMPLATE);
    }

    @Async
    public void sendMail(EmailContextDto contextInfo , String template) {
        try {
            Context context = new Context(Locale.KOREA);
            if (contextInfo != null) {
                context.setVariables(objectMapper.convertValue(contextInfo, Map.class));
            }

            String htmlTemplate = htmlTemplateEngine.process(template, context);

            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
            helper.setSubject(MAIL_SUBJECT);
            helper.setFrom(MAIL_SENDER_MAIL, MAIL_SENDER_AKA);
            helper.setTo(contextInfo.getReceiverEmail());
            helper.setText(htmlTemplate, true);

            mailSender.send(mimeMessage);

        } catch (MessagingException e) {
            throw new OlleUException(500, "Fail to send Email", HttpStatus.INTERNAL_SERVER_ERROR);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
