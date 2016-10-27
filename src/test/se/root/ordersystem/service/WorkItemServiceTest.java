package se.root.ordersystem.service;

import main.se.root.ordersystem.exception.RepositoryException;
import main.se.root.ordersystem.exception.ServiceException;
import main.se.root.ordersystem.model.WorkItem;
import main.se.root.ordersystem.model.WorkItemStatus;
import main.se.root.ordersystem.repository.interfaces.WorkItemRepository;
import main.se.root.ordersystem.service.WorkItemService;
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
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public final class WorkItemServiceTest {

    private String workItemId;
    private String teamId;
    private WorkItem testWorkItem;
    private static List<WorkItem> workItemsInDb;

    @Mock
    private WorkItemRepository workItemRepository;

    @InjectMocks
    private WorkItemService workItemService;

    @BeforeClass
    public static void initialize(){
        workItemsInDb = new ArrayList<>();
    }

    @Before
    public void setUp() {
        this.workItemId = "2";
        this.teamId = "4";
        this.testWorkItem = WorkItem.workItemBuilder("test").setId(workItemId).build();
        setAmountOfWorkItemsInList(5);
    }
    
    private void setAmountOfWorkItemsInList(int amount) {
        workItemsInDb.clear();
        for (int i = 0; i < amount; i++) {
            workItemsInDb.add(WorkItem.workItemBuilder("test").setId(String.valueOf(i))
                    .setStatus(WorkItemStatus.DONE).build());
        }
    }

    @Test
    public void canCreateWorkItem() throws ServiceException, RepositoryException {
        workItemService.createWorkItem(testWorkItem);
        verify(workItemRepository).create(testWorkItem);
    }

    @Test
    public void createWorkItemReturnsObjectWithId() throws ServiceException, RepositoryException {
        Long generatedId = 5L;
        when(workItemRepository.create(testWorkItem)).thenReturn(generatedId);
        WorkItem createdWorkItem = workItemService.createWorkItem(testWorkItem);
        assertEquals("The work item should have a auto generated id from the db when it gets created"
                , String.valueOf(generatedId), createdWorkItem.getId());
    }

    @Test
    public void canUpdateWorkItem() throws ServiceException, RepositoryException {
        when(workItemRepository.read(workItemId)).thenReturn(testWorkItem);
        workItemService.updateWorkItem(testWorkItem);
        verify(workItemRepository).update(testWorkItem);
    }

    @Test
    public void canGetWorkItemById() throws ServiceException, RepositoryException {
        when(workItemRepository.read(workItemId)).thenReturn(testWorkItem);
        WorkItem createdWorkItem = workItemService.getWorkItem(workItemId);
        verify(workItemRepository).read(workItemId);
        assertEquals(testWorkItem, createdWorkItem);
    }

    @Test
    public void canInactivateWorkItem() throws ServiceException, RepositoryException {
        workItemService.inactivateWorkItem(workItemId);
        verify(workItemRepository).changeStatus(false, workItemId);
    }

    @Test
    public void canActivateWorkItem() throws ServiceException, RepositoryException {
        workItemService.activateWorkItem(workItemId);
        verify(workItemRepository).changeStatus(true, workItemId);
    }

    @Test
    public void canGetAllWorkItems() throws ServiceException, RepositoryException {
        when(workItemRepository.getAll()).thenReturn(workItemsInDb);
        List<WorkItem> workItemsFromDb = workItemService.getAllWorkItems();
        verify(workItemRepository).getAll();
        assertEquals(workItemsInDb.size(), workItemsFromDb.size());
        assertThat(workItemsInDb, is(workItemsFromDb));
    }

    @Test
    public void canGetAllWorkItemsByStatus() throws ServiceException, RepositoryException {
        when(workItemRepository.getWorkItemByStatus(WorkItemStatus.UNSTARTED)).thenReturn(workItemsInDb);
        List<WorkItem> workItemsFromDb = workItemService.getWorkItemByStatus(WorkItemStatus.UNSTARTED);
        verify(workItemRepository).getWorkItemByStatus(WorkItemStatus.UNSTARTED);
        assertEquals(workItemsInDb.size(), workItemsFromDb.size());
        assertThat(workItemsInDb, is(workItemsFromDb));
    }

    @Test
    public void canGetAllWorkItemsByTeam() throws ServiceException, RepositoryException {
        when(workItemRepository.getAllWorkItemsByTeam(teamId)).thenReturn(workItemsInDb);
        List<WorkItem> workItemsFromDb = workItemService.getAllWorkItemsByTeam(teamId);
        verify(workItemRepository).getAllWorkItemsByTeam(teamId);
        assertEquals(workItemsInDb.size(), workItemsFromDb.size());
        assertThat(workItemsInDb, is(workItemsFromDb));
    }
}
