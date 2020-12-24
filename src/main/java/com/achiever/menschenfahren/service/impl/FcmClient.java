package com.achiever.menschenfahren.service.impl;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.springframework.stereotype.Service;

import com.achiever.menschenfahren.config.FcmSettings;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class FcmClient {

    public FcmClient(FcmSettings settings) {
        Path p = Paths.get(settings.getServiceAccountFile());
        try (InputStream serviceAccount = Files.newInputStream(p)) {
            FirebaseOptions options = new FirebaseOptions.Builder().setCredentials(GoogleCredentials.fromStream(serviceAccount)).build();

            FirebaseApp.initializeApp(options);
        } catch (IOException e) {
            log.error("init fcm", e);
        }
    }

}
