package se.root.ordersystem.service;

import main.se.root.ordersystem.exception.RepositoryException;
import main.se.root.ordersystem.exception.ServiceException;
import main.se.root.ordersystem.model.Team;
import main.se.root.ordersystem.model.User;
import main.se.root.ordersystem.repository.interfaces.TeamRepository;
import main.se.root.ordersystem.repository.interfaces.UserRepository;
import main.se.root.ordersystem.service.TeamService;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNotNull;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public final class TeamServiceTest {

    private Team testTeam;
    private User testUser;
    private String teamId;
    private String userId;
    private static ArrayList<User> usersInTeam;
    private static ArrayList<Team> teamsInDb;


    @BeforeClass
    public static void initialize() {
        usersInTeam = new ArrayList<>();
        teamsInDb = new ArrayList<>();
    }

    @Before
    public void setUp() {
        this.teamId = "4";
        this.userId = "2";
        this.testTeam = Team.teamBuilder("Test Team").setId(teamId).build();
        this.testUser = User.userBuilder("testName", "testName", "testName")
                .setId(userId).build();
        setAmountOfUsersInTeam(5);
        setAmountOfTeamsInDb(5);
    }

    private void setAmountOfUsersInTeam(int amount) {
        usersInTeam.clear();
        for (int i = 0; i < amount; i++) {
            usersInTeam.add(User.userBuilder("testName", "testName", "testName")
                    .setId(String.valueOf(i)).setTeamId(teamId).build());
        }
    }

    private void setAmountOfTeamsInDb(int amount) {
        teamsInDb.clear();
        for (int i = 0; i < amount; i++) {
            teamsInDb.add(Team.teamBuilder("Test Team").setId(teamId).build());
        }
    }

    @Mock
    TeamRepository teamRepository;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    TeamService teamService;

    @Test
    public void canGetUsersFromTeam() throws ServiceException, RepositoryException {
        when(teamRepository.getUsersFromTeam(teamId)).thenReturn(usersInTeam);
        List<User> usersFromTeam = teamService.getUsersFromTeam(teamId);
        verify(teamRepository).getUsersFromTeam(teamId);
        assertNotNull(usersFromTeam);
        assertEquals(usersInTeam.size(), usersFromTeam.size());
        assertThat(usersInTeam, is(usersFromTeam));
    }

    @Test
    public void canCreateTeam() throws ServiceException, RepositoryException {
        teamService.createTeam(testTeam);
        verify(teamRepository).create(testTeam);
    }

    @Test
    public void createTeamReturnsObjectWithId() throws ServiceException, RepositoryException {
        Long generatedId = 5L;
        when(teamRepository.create(testTeam)).thenReturn(generatedId);
        Team createdTeam = teamService.createTeam(testTeam);
        assertEquals("The team should have a auto generated id from the db when it gets created",
                String.valueOf(generatedId), createdTeam.getId());
    }

    @Test
    public void canUpdateTeam() throws ServiceException, RepositoryException {
        when(teamRepository.read(teamId)).thenReturn(testTeam);
        teamService.updateTeam(testTeam);
        verify(teamRepository).update(testTeam);
    }

    @Test
    public void canInactivateTeam() throws ServiceException, RepositoryException {
        teamService.inactivateTeam(teamId);
        verify(teamRepository).changeStatus(false, teamId);
    }

    @Test
    public void canActivateTeam() throws ServiceException, RepositoryException {
        teamService.activateTeam(teamId);
        verify(teamRepository).changeStatus(true, teamId);
    }

    @Test
    public void canGetAllTeams() throws ServiceException, RepositoryException {
        when(teamRepository.getAll()).thenReturn(teamsInDb);
        List<Team> allTeamsFromDb = teamService.getAllTeams();
        verify(teamRepository).getAll();
        assertEquals(teamsInDb.size(), allTeamsFromDb.size());
        assertThat(teamsInDb, is(allTeamsFromDb));
    }

    @Test
    public void canAddUserToExistingTeam() throws ServiceException, RepositoryException {
        when(userRepository.read(userId)).thenReturn(testUser);
        when(teamRepository.getAll()).thenReturn(teamsInDb);
        when(teamRepository.getUsersFromTeam(teamId)).thenReturn(usersInTeam);
        teamService.addUserToTeam(userId);
        verify(teamRepository).addUserToTeam(userId, teamId);
    }

    @Test
    public void addsUserToNewTeamIfAllTeamsContainsTenUsers() throws ServiceException, RepositoryException {
        Long createdTeamsId = 5L;
        int maxAmountOfUsersInTeam = 10;
        setAmountOfUsersInTeam(maxAmountOfUsersInTeam);
        when(userRepository.read(userId)).thenReturn(testUser);
        when(teamRepository.getAll()).thenReturn(teamsInDb);
        when(teamRepository.getUsersFromTeam(teamId)).thenReturn(usersInTeam);
        when(teamRepository.create(Team.teamBuilder("Team " + (teamsInDb.size() + 1)).build())).thenReturn(createdTeamsId);

        teamService.addUserToTeam(userId);
        verify(teamRepository).create(Team.teamBuilder("Team " + (teamsInDb.size() + 1)).build());
        verify(teamRepository).addUserToTeam(userId, String.valueOf(createdTeamsId));
    }

    @Test(expected = ServiceException.class)
    public void canNotAddInactiveUserToTeam() throws ServiceException, RepositoryException {
        this.testUser = User.userBuilder("testName", "testName", "testName")
                .setId(userId).setActive(false).build();
        when(userRepository.read(userId)).thenReturn(testUser);
        teamService.addUserToTeam(testUser.getId());
    }
}