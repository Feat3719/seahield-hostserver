package com.seahield.hostserver.service;

import java.util.HashMap;
import java.util.Random;

import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.seahield.hostserver.exception.ErrorException;
import com.seahield.hostserver.repository.MessageCertificationRepository;

import lombok.RequiredArgsConstructor;
import net.nurigo.sdk.NurigoApp;
import net.nurigo.sdk.message.exception.NurigoMessageNotReceivedException;
import net.nurigo.sdk.message.model.Message;
import net.nurigo.sdk.message.request.SingleMessageSendingRequest;
import net.nurigo.sdk.message.response.SingleMessageSentResponse;
import net.nurigo.sdk.message.service.DefaultMessageService;

@Service
@RequiredArgsConstructor
public class SmsService {

    private DefaultMessageService messageService;
    private final MessageCertificationRepository messageCertificationRepository;

    @Value("${coolsms.apikey}")
    private String apiKey;

    @Value("${coolsms.apisecret}")
    private String apiSecret;

    @Value("${coolsms.fromnumber}")
    private String fromNumber;

    private String makeRandomNumber() {
        Random rand = new Random();
        String randomNum = "";
        for (int i = 0; i < 4; i++) {
            String random = Integer.toString(rand.nextInt(10));
            randomNum += random;
        }

        return randomNum;
    }

    // 인증번호 전송하기
    public SingleMessageSentResponse sendSms(String phone) {
        this.messageService = NurigoApp.INSTANCE.initialize(apiKey, apiSecret, "https://api.coolsms.co.kr");
        Message message = new Message();
        String randomNum = makeRandomNumber();
        message.setFrom(fromNumber);
        message.setTo(phone);
        message.setText("[Seahield] 회원가입 인증 번호입니다.\n" + randomNum);

        try {
            SingleMessageSentResponse result = this.messageService.sendOne(new SingleMessageSendingRequest(message));
            messageCertificationRepository.createSmsCertification(phone, randomNum);
            return result;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw new ErrorException("FAIL TO SEND SMS");
        }
    }

    // 인증번호 확인
    public boolean checkSms(String phone, String code) {
        String savedCertificationNumber = messageCertificationRepository.getSmsCertification(phone);
        if (savedCertificationNumber != null && savedCertificationNumber.equals(code)) {
            // 인증번호가 일치하면 Redis에서 삭제
            messageCertificationRepository.deleteSmsCertification(phone);
            return true;
        }
        return false;
    }

}
