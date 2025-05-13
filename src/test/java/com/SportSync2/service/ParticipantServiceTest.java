package com.SportSync2.service;

import com.SportSync2.dto.ParticipantDTO;
import com.SportSync2.dto.ResponseDTO;
import com.SportSync2.dto.TeamMemberDTO;
import com.SportSync2.entity.*;
import com.SportSync2.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ParticipantServiceTest {

    @Mock private UserRepository userRepository;
    @Mock private SubEventRepository subEventRepository;
    @Mock private ParticipantRepository participantRepository;
    @Mock private TeamRepository teamRepository;
    @Mock private TeamMemberRepository teamMemberRepository;

    @InjectMocks private ParticipantService participantService;

    private User user;
    private User host;
    private Event event;
    private SubEvent subEvent;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        user = User.builder().id(1L).email("user@test.com").password("pass").verified(true).build();
        host = User.builder().id(2L).email("host@test.com").password("pass").verified(true).build();

        event = Event.builder().id(10L).host(host).build();
        subEvent = SubEvent.builder()
                .id(100L)
                .name("Futsal")
                .event(event)
                .teamBased(false)
                .maxParticipants(10)
                .startTime(java.time.LocalDateTime.now())
                .endTime(java.time.LocalDateTime.now().plusHours(2))
                .build();
    }

    @Test
    void testRegisterIndividualParticipant_Success() {
        ParticipantDTO dto = ParticipantDTO.builder()
                .userId(user.getId())
                .subEventId(subEvent.getId())
                .build();

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(subEventRepository.findById(subEvent.getId())).thenReturn(Optional.of(subEvent));
        when(participantRepository.existsByUserAndSubEvent(user, subEvent)).thenReturn(false);

        ResponseDTO response = participantService.registerParticipant(dto);

        assertTrue(response.isSuccess());
        assertEquals("Successfully registered as an individual participant!", response.getMessage());
        verify(participantRepository, times(1)).save(any(Participant.class));
    }

    @Test
    void testRegisterTeamParticipant_Success() {
        subEvent.setTeamBased(true);

        List<TeamMemberDTO> members = List.of(
                TeamMemberDTO.builder().name("Teammate 1").email("mate1@example.com").build(),
                TeamMemberDTO.builder().name("Teammate 2").email("mate2@example.com").build()
        );

        ParticipantDTO dto = ParticipantDTO.builder()
                .userId(user.getId())
                .subEventId(subEvent.getId())
                .teamName("Dream Team")
                .teamMembers(members)
                .build();

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(subEventRepository.findById(subEvent.getId())).thenReturn(Optional.of(subEvent));
        when(participantRepository.existsByUserAndSubEvent(user, subEvent)).thenReturn(false);
        when(teamRepository.save(any(Team.class))).thenAnswer(i -> i.getArgument(0)); // return saved team

        ResponseDTO response = participantService.registerParticipant(dto);

        assertTrue(response.isSuccess());
        assertEquals("Team registered successfully!", response.getMessage());
        verify(teamRepository, times(1)).save(any(Team.class));
        verify(teamMemberRepository, times(2)).save(any(TeamMember.class));
        verify(participantRepository, times(1)).save(any(Participant.class));
    }

    @Test
    void testRegisterFails_InvalidUserOrEvent() {
        ParticipantDTO dto = ParticipantDTO.builder().userId(1L).subEventId(100L).build();

        when(userRepository.findById(1L)).thenReturn(Optional.empty());
        when(subEventRepository.findById(100L)).thenReturn(Optional.empty());

        ResponseDTO response = participantService.registerParticipant(dto);

        assertFalse(response.isSuccess());
        assertEquals("Invalid user or sub-event", response.getMessage());
    }

    @Test
    void testRegisterFails_AlreadyRegistered() {
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(subEventRepository.findById(subEvent.getId())).thenReturn(Optional.of(subEvent));
        when(participantRepository.existsByUserAndSubEvent(user, subEvent)).thenReturn(true);

        ParticipantDTO dto = ParticipantDTO.builder().userId(user.getId()).subEventId(subEvent.getId()).build();
        ResponseDTO response = participantService.registerParticipant(dto);

        assertFalse(response.isSuccess());
        assertEquals("User is already registered for this event", response.getMessage());
    }

    @Test
    void testRegisterFails_HostTryingToRegister() {
        subEvent.getEvent().setHost(user); // make user the host
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(subEventRepository.findById(subEvent.getId())).thenReturn(Optional.of(subEvent));

        ParticipantDTO dto = ParticipantDTO.builder().userId(user.getId()).subEventId(subEvent.getId()).build();
        ResponseDTO response = participantService.registerParticipant(dto);

        assertFalse(response.isSuccess());
        assertEquals("Hosts cannot register for their own event.", response.getMessage());
    }

    @Test
    void testRegisterFails_TeamEvent_MissingTeamInfo() {
        subEvent.setTeamBased(true);
        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(subEventRepository.findById(subEvent.getId())).thenReturn(Optional.of(subEvent));
        when(participantRepository.existsByUserAndSubEvent(user, subEvent)).thenReturn(false);

        ParticipantDTO dto = ParticipantDTO.builder().userId(user.getId()).subEventId(subEvent.getId()).build();
        ResponseDTO response = participantService.registerParticipant(dto);

        assertFalse(response.isSuccess());
        assertEquals("Team name and members are required for team-based events", response.getMessage());
    }

    @Test
    void testRegisterFails_TeamEvent_MemberMissingFields() {
        subEvent.setTeamBased(true);

        List<TeamMemberDTO> members = List.of(
                TeamMemberDTO.builder().name(null).email("mate1@example.com").build()
        );

        when(userRepository.findById(user.getId())).thenReturn(Optional.of(user));
        when(subEventRepository.findById(subEvent.getId())).thenReturn(Optional.of(subEvent));
        when(participantRepository.existsByUserAndSubEvent(user, subEvent)).thenReturn(false);

        ParticipantDTO dto = ParticipantDTO.builder()
                .userId(user.getId())
                .subEventId(subEvent.getId())
                .teamName("Team")
                .teamMembers(members)
                .build();

        ResponseDTO response = participantService.registerParticipant(dto);

        assertFalse(response.isSuccess());
        assertEquals("Each team member must have a name and email.", response.getMessage());
    }
}
