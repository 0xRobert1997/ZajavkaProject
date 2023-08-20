package code.medconnect.webMvc;

import code.medconnect.api.controller.DoctorController;
import code.medconnect.api.dto.DoctorDTO;
import code.medconnect.api.dto.PatientDTO;
import code.medconnect.api.dto.VisitDTO;
import code.medconnect.api.dto.mapper.DoctorMapper;
import code.medconnect.api.dto.mapper.PatientMapper;
import code.medconnect.business.DoctorService;
import code.medconnect.business.PatientService;
import code.medconnect.business.VisitService;
import code.medconnect.domain.Doctor;
import code.medconnect.domain.Patient;
import code.medconnect.security.AppUser;
import code.medconnect.security.AppUserService;
import code.medconnect.util.DomainFixtures;
import code.medconnect.util.DtoFixtures;
import lombok.AllArgsConstructor;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = DoctorController.class)
@AutoConfigureMockMvc(addFilters = false)
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class DoctorControllerWebMvcTest {

    private MockMvc mockMvc;
    @MockBean
    VisitService visitService;
    @MockBean
    DoctorService doctorService;
    @MockBean
    AppUserService appUserService;
    @MockBean
    PatientService patientService;
    @MockBean
    PatientMapper patientMapper;
    @MockBean
    DoctorMapper doctorMapper;

    @Test
    void doctorPageTest() throws Exception {
        Doctor doctor = DomainFixtures.someDoctor1().withDoctorId(1);
        DoctorDTO doctorDTO = DtoFixtures.someDoctorDTO1().withDoctorId(1);
        String username = "Doctor1";
        String userEmail = doctor.getEmail();
        AppUser appUser = AppUser.builder().email(userEmail).build();
        Map<VisitDTO, PatientDTO> visitsWithPatients = new HashMap<>();

        Mockito.when(appUserService.findByUsername(username)).thenReturn(appUser);
        Mockito.when(doctorService.findByEmail(Mockito.anyString())).thenReturn(doctor);
        Mockito.when(doctorMapper.map(Mockito.any(Doctor.class))).thenReturn(doctorDTO);
        Mockito.when(doctorService.getDoctorsVisitsWithPatients(Mockito.anyInt())).thenReturn(visitsWithPatients);

        // when then
        mockMvc.perform(get("/doctor").principal(new UsernamePasswordAuthenticationToken(username, "test")))
                .andExpect(status().isOk())
                .andExpect(view().name("doctor-portal"))
                .andExpect(model().attribute("doctorDTO", doctorDTO))
                .andExpect(model().attribute("visits", visitsWithPatients));

        Mockito.verify(appUserService).findByUsername(username);
        Mockito.verify(doctorService).findByEmail(userEmail);
        Mockito.verify(doctorMapper).map(Mockito.any(Doctor.class));
        Mockito.verify(doctorService).getDoctorsVisitsWithPatients(Mockito.anyInt());
    }

    @Test
    void addAvailabilityTest() throws Exception {
        //given
        Doctor doctor = DomainFixtures.someDoctor1();
        String doctorEmail = doctor.getEmail();

        //when
        Mockito.when(doctorService.findByEmail(doctorEmail)).thenReturn(doctor);
        //then
        mockMvc.perform(post("/doctor/add-availability")
                .param("doctorEmail", doctorEmail)
                .param("date", "2023-01-01")
                .param("startTime", "12:15")
                .param("endTime", "12:30"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/doctor"));
    }

    @Test
    void addNoteToVisitTest() throws Exception{
        // given
        Integer visitId = 1;
        String noteContent = "Some note content";

        // when then
        mockMvc.perform(post("/doctor/add-note")
                        .param("visitId", visitId.toString())
                        .param("noteContent", noteContent))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/doctor"));
        Mockito.verify(visitService).addNoteToVisit(visitId, noteContent);
    }

    @Test
    void checkPatientTest() throws Exception {
        // given
        Patient patient = DomainFixtures.somePatient();
        String patientPesel = patient.getPesel();
        PatientDTO patientDTO = DtoFixtures.somePatientDTO();
        patientDTO.setPesel(patientPesel);

        Mockito.when(patientService.findPatientWithDiseases(patientPesel)).thenReturn(patient);
        Mockito.when(patientMapper.map(patient)).thenReturn(patientDTO);

        // wen then
        mockMvc.perform(post("/doctor/check-patient")
                        .param("patientPesel", patientPesel))
                .andExpect(status().isOk())
                .andExpect(view().name("check-patient"))
                .andExpect(model().attribute("patient", patientDTO));
        Mockito.verify(patientService).findPatientWithDiseases(patientPesel);
        Mockito.verify(patientMapper).map(patient);

    }
}
