package se.root.ordersystem.service;

import main.se.root.ordersystem.exception.RepositoryException;
import main.se.root.ordersystem.exception.ServiceException;
import main.se.root.ordersystem.model.Issue;
import main.se.root.ordersystem.model.WorkItem;
import main.se.root.ordersystem.model.WorkItemStatus;
import main.se.root.ordersystem.repository.interfaces.IssueRepository;
import main.se.root.ordersystem.repository.interfaces.WorkItemRepository;
import main.se.root.ordersystem.service.IssueService;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static junit.framework.Assert.assertEquals;

@RunWith(MockitoJUnitRunner.class)
public final class IssueServiceTest {

    private String workItemId;
    private String issueId;
    private WorkItem testWorkItem;
    private Issue testIssue;
    private Long generatedIssueId;
    private static List<WorkItem> workItemsInDb;

    @Mock
    private IssueRepository issueRepository;

    @Mock
    private WorkItemRepository workItemRepository;

    @InjectMocks
    private IssueService issueService;

    @BeforeClass
    public static void initialize(){
        workItemsInDb = new ArrayList<>();
    }

    @Before
    public void setUp(){
        this.workItemId = "1";
        this.issueId = "2";
        this.generatedIssueId = 5L;
        this.testWorkItem = WorkItem.workItemBuilder("test").setId(workItemId)
                .setStatus(WorkItemStatus.DONE).build();
        this.testIssue = Issue.issueBuilder("test").setId(issueId).build();
        setAmountOfWorkItemsInList(10);
    }

    private void setAmountOfWorkItemsInList(int amount) {
        workItemsInDb.clear();
        for (int i = 0; i < amount; i++) {
            workItemsInDb.add(WorkItem.workItemBuilder("test").setId(String.valueOf(i))
                    .setStatus(WorkItemStatus.DONE).build());
        }
    }

    @Test
    public void canCreateIssue() throws ServiceException, RepositoryException {
        when(workItemRepository.read(workItemId)).thenReturn(testWorkItem);
        issueService.addIssueToWorkItem(testIssue, workItemId);
        verify(issueRepository).create(testIssue);
    }

    @Test
    public void addIssueToWorkItemReturnsObjectWithId() throws ServiceException, RepositoryException {
        when(workItemRepository.read(workItemId)).thenReturn(testWorkItem);
        when(issueRepository.create(testIssue)).thenReturn(generatedIssueId);
        Issue createdIssue = issueService.addIssueToWorkItem(testIssue, workItemId);
        assertEquals("The issue should have a auto generated id from the db when it gets created",
                String.valueOf(generatedIssueId), createdIssue.getId());
    }

    @Test
    public void canAddIssueToWorkItemWithStatusDone() throws ServiceException, RepositoryException {
        when(workItemRepository.read(workItemId)).thenReturn(testWorkItem);
        when(issueRepository.create(testIssue)).thenReturn(generatedIssueId);
        issueService.addIssueToWorkItem(testIssue, workItemId);
        Issue createdIssue = Issue.issueBuilder(testIssue.getTitle())
                .setDescription(testIssue.getDescription())
                .setId(String.valueOf(generatedIssueId))
                .build();
        verify(issueRepository).AddIssueToWorkItem(createdIssue, workItemId);
    }

    @Test
    public void workItemStatusChangeToUnStartedWhenIssueIsAdded() throws ServiceException, RepositoryException {
        when(workItemRepository.read(workItemId)).thenReturn(testWorkItem);
        issueService.addIssueToWorkItem(testIssue, workItemId);
        verify(workItemRepository).changeWorkItemStatus(workItemId, WorkItemStatus.UNSTARTED);
    }

    @Test(expected = ServiceException.class)
    public void canNotAddIssueToWorkItemWithStatusStarted() throws ServiceException, RepositoryException {
        this.testWorkItem = WorkItem.workItemBuilder("test").setId(workItemId)
                .setStatus(WorkItemStatus.STARTED).build();
        when(workItemRepository.read(workItemId)).thenReturn(testWorkItem);
        issueService.addIssueToWorkItem(testIssue, workItemId);
    }

    @Test(expected = ServiceException.class)
    public void canNotAddIssueToWorkItemWithStatusUnStarted() throws ServiceException, RepositoryException {
        this.testWorkItem = WorkItem.workItemBuilder("test").setId(workItemId)
                .setStatus(WorkItemStatus.UNSTARTED).build();
        when(workItemRepository.read(workItemId)).thenReturn(testWorkItem);
        issueService.addIssueToWorkItem(testIssue, workItemId);
    }

    @Test
    public void CanAddIssueToWorkItemWithStatusDone() throws ServiceException, RepositoryException {
        this.testWorkItem = WorkItem.workItemBuilder("test").setId(workItemId)
                .setStatus(WorkItemStatus.DONE).build();
        this.testIssue = Issue.issueBuilder("test").setId(String.valueOf(generatedIssueId)).build();
        when(workItemRepository.read(workItemId)).thenReturn(testWorkItem);
        when(issueRepository.create(testIssue)).thenReturn(generatedIssueId);

        issueService.addIssueToWorkItem(testIssue, workItemId);
        verify(issueRepository).AddIssueToWorkItem(testIssue, workItemId);
    }

    @Test
    public void canUpdateIssue() throws ServiceException, RepositoryException {
        when(issueRepository.read(issueId)).thenReturn(testIssue);
        issueService.updateIssue(testIssue);
        verify(issueRepository).update(testIssue);
    }

    @Test
    public void canGetAllWorkItemsWithAnIssue() throws ServiceException, RepositoryException {
        when(issueRepository.getAllWorkItemsWithIssue()).thenReturn(workItemsInDb);
        List<WorkItem> allWorkItemsFromDb = issueService.getAllWorkItemsWithIssue();
        verify(issueRepository).getAllWorkItemsWithIssue();
        assertEquals(workItemsInDb.size(), allWorkItemsFromDb.size());
    }
}
