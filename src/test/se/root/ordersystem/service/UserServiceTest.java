package se.root.ordersystem.service;

import main.se.root.ordersystem.exception.RepositoryException;
import main.se.root.ordersystem.exception.ServiceException;
import main.se.root.ordersystem.model.User;
import main.se.root.ordersystem.model.WorkItem;
import main.se.root.ordersystem.model.WorkItemStatus;
import main.se.root.ordersystem.repository.interfaces.UserRepository;
import main.se.root.ordersystem.repository.interfaces.WorkItemRepository;
import main.se.root.ordersystem.service.UserService;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

@RunWith(MockitoJUnitRunner.class)
public final class UserServiceTest {

    private User testUser;
    private String userId;
    private String workItemId;
    private static List<User> allUsersInDb;
    private static List<WorkItem> allWorkItemsInUser;
    private int maxAmountOfWorkItems;

    @BeforeClass
    public static void initialize() {
        allUsersInDb = new ArrayList<>();
        allWorkItemsInUser = new ArrayList<>();
    }

    @Before
    public void setUp() {
        this.userId = "2";
        this.workItemId = "2";
        this.maxAmountOfWorkItems = 5;
        this.testUser = User.userBuilder("Over Ten Characters", "testName", "testName")
                .setId(userId).build();
        addUsersToList(10);
        addWorkItemsToUser(10);
    }

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Mock
    private UserRepository userRepository;

    @Mock
    private WorkItemRepository workItemRepository;

    @InjectMocks
    private UserService userService;

    public void addWorkItemsToUser(int amount) {
        allWorkItemsInUser.clear();
        for (int i = 0; i < amount; i++) {
            allWorkItemsInUser.add(WorkItem.workItemBuilder("test")
                    .setId(workItemId).build());
        }
    }

    private void addUsersToList(int amount) {
        allUsersInDb.clear();
        for (int i = 0; i < amount; i++) {
            allUsersInDb.add(User.userBuilder("testName", "testName", "testName")
                    .setId(String.valueOf(i)).build());
        }
    }

    @Test
    public void canCreateUser() throws ServiceException, RepositoryException {
        userService.createUser(testUser);
        verify(userRepository).create(testUser);
    }

    @Test
    public void createUserReturnsObjectWithId() throws ServiceException, RepositoryException {
        Long generatedId = 5L;
        when(userRepository.create(testUser)).thenReturn(generatedId);
        User createdUser = userService.createUser(testUser);
        assertEquals("The User have a auto generated id from the db when it gets created",
                String.valueOf(generatedId), createdUser.getId());
    }

    @Test
    public void canNotCreateUserWithUsernameShorterThanTenCharacters() throws ServiceException, RepositoryException {
        exception.expect(ServiceException.class);
        exception.expectMessage("Username too short, 10 characters required");
        this.testUser = User.userBuilder("test", "testName", "testName")
                .setId(userId).build();
        userService.createUser(testUser);
    }

    @Test
    public void canUpdateUser() throws ServiceException, RepositoryException {
        when(userRepository.read(userId)).thenReturn(testUser);
        userService.updateUser(testUser);
        verify(userRepository).update(testUser);
    }

    @Test
    public void canGetUserById() throws ServiceException, RepositoryException {
        when(userRepository.read(userId)).thenReturn(testUser);
        User userFromDb = userService.getUserById(userId);
        verify(userRepository).read(userId);
        assertEquals(testUser.getUsername(), userFromDb.getUsername());
        assertEquals(testUser.getFirstname(), userFromDb.getFirstname());
        assertEquals(testUser.getLastname(), userFromDb.getLastname());
        assertEquals(testUser.getId(), userFromDb.getId());
    }

    @Test
    public void canGetUserByUsernameOrName() throws ServiceException, RepositoryException {
        when(userRepository.getUsersBy(testUser.getUsername(), testUser.getFirstname(), testUser.getLastname())).thenReturn(allUsersInDb);
        List<User> usersFromDb = userService.getUsersBy(testUser.getUsername(), testUser.getFirstname(), testUser.getLastname());
        verify(userRepository).getUsersBy(testUser.getUsername(), testUser.getFirstname(), testUser.getLastname());
        assertEquals(allUsersInDb.size(), usersFromDb.size());
        assertThat(allUsersInDb, is(usersFromDb));
    }

    @Test
    public void canInactivateTeam() throws ServiceException, RepositoryException {
        userService.inactivateUser(userId);
        verify(userRepository).changeStatus(false, userId);
    }

    @Test
    public void workItemStatusChangeToUnStartedWhenUserGetInactive() throws ServiceException, RepositoryException {
        when(userRepository.getAllWorkItemsByUser(userId)).thenReturn(allWorkItemsInUser);
        userService.inactivateUser(userId);
        verify(workItemRepository, times(allWorkItemsInUser.size())).changeWorkItemStatus(workItemId, WorkItemStatus.UNSTARTED);
    }

    @Test
    public void canActivateTeam() throws ServiceException, RepositoryException {
        userService.activateUser(userId);
        verify(userRepository).changeStatus(true, userId);
    }

    @Test
    public void getAllUsers() throws ServiceException, RepositoryException {
        when(userRepository.getAll()).thenReturn(allUsersInDb);
        List<User> usersFromDb = userService.getAll();
        verify(userRepository).getAll();
        assertEquals(usersFromDb.size(), allUsersInDb.size());
        assertThat(allUsersInDb, is(usersFromDb));
    }

    @Test
    public void canGetAllWorkItemsFromUser() throws ServiceException, RepositoryException {
        when(userRepository.getAllWorkItemsByUser(userId)).thenReturn(allWorkItemsInUser);
        List<WorkItem> workItemsFromUser = userService.getAllWorkItemsByUser(userId);
        verify(userRepository).getAllWorkItemsByUser(userId);
        assertEquals(allWorkItemsInUser.size(), workItemsFromUser.size());
        assertThat(allWorkItemsInUser, is(workItemsFromUser));
    }

    @Test(expected = ServiceException.class)
    public void canNotAddWorkItemToUserIfUserIsInactive() throws ServiceException, RepositoryException {
        this.testUser = User.userBuilder("testName", "testName", "testName")
                .setActive(false).setId(userId).build();
        when(userRepository.read(userId)).thenReturn(testUser);
        userService.addWorkItemToUser(userId, workItemId);
    }

    @Test(expected = ServiceException.class)
    public void canNotAddWorkItemToUserWithMaxAmountOfWorkItems() throws ServiceException, RepositoryException {
        addWorkItemsToUser(maxAmountOfWorkItems);
        when(userRepository.read(userId)).thenReturn(testUser);
        when(userRepository.getAllWorkItemsByUser(userId)).thenReturn(allWorkItemsInUser);
        userService.addWorkItemToUser(userId, workItemId);
    }

    @Test
    public void canAddWorkItemToUser() throws ServiceException, RepositoryException {
        addWorkItemsToUser(maxAmountOfWorkItems - 1);
        when(userRepository.read(userId)).thenReturn(testUser);
        when(userRepository.getAllWorkItemsByUser(userId)).thenReturn(allWorkItemsInUser);
        userService.addWorkItemToUser(userId, workItemId);
        verify(userRepository).addWorkItemToUser(userId, workItemId);
    }
}
