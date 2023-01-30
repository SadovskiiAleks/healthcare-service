package ru.netology.patient;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import ru.netology.patient.entity.BloodPressure;
import ru.netology.patient.entity.HealthInfo;
import ru.netology.patient.entity.PatientInfo;
import ru.netology.patient.repository.PatientInfoFileRepository;
import ru.netology.patient.repository.PatientInfoRepository;
import ru.netology.patient.service.alert.SendAlertService;
import ru.netology.patient.service.alert.SendAlertServiceImpl;
import ru.netology.patient.service.medical.MedicalServiceImpl;

import java.math.BigDecimal;
import java.time.LocalDate;

public class MedicalServiceImplTests {

    // 1
    @Test
    public void checkBloodPressureTest() {
        // arrange:
        PatientInfoRepository patientInfoRepository = Mockito.mock(PatientInfoRepository.class);
        Mockito.when(patientInfoRepository.getById(Mockito.any(String.class)))
                .thenReturn(new PatientInfo("1","Иван", "Петров",
                        LocalDate.of(1980, 11, 26),
                        new HealthInfo(new BigDecimal("36.65"), new BloodPressure(120, 80))));

        SendAlertService alertService = Mockito.mock(SendAlertService.class);
        Mockito.doNothing().when(alertService).send("Mockito.any()");

        BloodPressure currentPressure = new BloodPressure(50, 300);

        // act:
        MedicalServiceImpl medicalService = new MedicalServiceImpl(patientInfoRepository,alertService);
        medicalService.checkBloodPressure("1", currentPressure);

        // assert:
        Mockito.verify(alertService, Mockito.times(1)).send(Mockito.any());
    }

    // 2
    @Test
    public void checkTemperatureTest() {
        // arrange:
        PatientInfoRepository patientInfoRepository = Mockito.mock(PatientInfoRepository.class);
        Mockito.when(patientInfoRepository.getById(Mockito.any(String.class)))
                .thenReturn(new PatientInfo("1","Иван", "Петров",
                        LocalDate.of(1980, 11, 26),
                        new HealthInfo(new BigDecimal("36.65"), new BloodPressure(120, 80))));

        SendAlertService alertService = Mockito.mock(SendAlertService.class);
        Mockito.doNothing().when(alertService).send(Mockito.any());

        BigDecimal currentTemperature = new BigDecimal("35");

        // act:
        MedicalServiceImpl medicalService = new MedicalServiceImpl(patientInfoRepository,alertService);
        medicalService.checkTemperature("1", currentTemperature);

        // assert:
        Mockito.verify(alertService, Mockito.times(1)).send(Mockito.any());
    }

    // 3
    @Test
    public void checkTemperatureTest2() {
        // arrange:
        PatientInfoRepository patientInfoRepository = Mockito.mock(PatientInfoRepository.class);
        Mockito.when(patientInfoRepository.getById(Mockito.any(String.class)))
                .thenReturn(new PatientInfo("1","Иван", "Петров",
                        LocalDate.of(1980, 11, 26),
                        new HealthInfo(new BigDecimal("39"), new BloodPressure(120, 80))));

        SendAlertService alertService = Mockito.mock(SendAlertService.class);
        Mockito.doNothing().when(alertService).send(Mockito.any());

        BigDecimal currentTemperature = new BigDecimal("36.6");

        // act:
        MedicalServiceImpl medicalService = new MedicalServiceImpl(patientInfoRepository,alertService);
        medicalService.checkTemperature("1", currentTemperature);

        // assert:
        Mockito.verify(alertService, Mockito.times(1)).send("Warning, patient with id: 1, need help");

    }
}
