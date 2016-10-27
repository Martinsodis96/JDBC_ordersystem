package main.se.root.ordersystem.repository;

import java.sql.SQLException;
import java.util.List;

import main.se.root.ordersystem.model.Issue;
import main.se.root.ordersystem.model.WorkItem;
import main.se.root.ordersystem.exception.RepositoryException;
import main.se.root.ordersystem.repository.interfaces.IssueRepository;
import main.se.root.ordersystem.helpers.*;

import static main.se.root.ordersystem.helpers.DBInfo.*;
import static main.se.root.ordersystem.helpers.Mapper.ISSUE_MAPPER;
import static main.se.root.ordersystem.helpers.Mapper.WORK_ITEM_MAPPER;

public final class MySQLIssueRepository extends BaseCRUDRepository<Issue> implements IssueRepository {

    @Override
    public long create(Issue issue) throws RepositoryException {
        try {
            return new SQL(url).query("INSERT INTO issue(title, description) VALUES(?, ?)").parameter(issue.getTitle())
                    .parameter(issue.getDescription()).insert();
        } catch (SQLException e) {
            throw new RepositoryException("Could not insert issue with title: " + issue.getTitle(), e);
        }
    }

    public Issue read(String id) throws RepositoryException {
        return super.read("issue", id, Mapper.ISSUE_MAPPER);
    }

    @Override
    public void update(Issue issue) throws RepositoryException {
        try {
            new SQL(url).query("UPDATE issue SET title= ? ,description=? WHERE id = ?").parameter(issue.getTitle())
                    .parameter(issue.getDescription()).parameter(issue.getId()).update();
        } catch (SQLException e) {
            throw new RepositoryException("Could not update issue with title: " + issue.getTitle(), e);
        }
    }

    public void changeStatus(boolean isActive, String id) throws RepositoryException {
        super.changeStatus("issue", isActive, id);
    }

    public List<Issue> getAll() throws RepositoryException {
        return super.getAll("issue", ISSUE_MAPPER);
    }

    @Override
    public void AddIssueToWorkItem(Issue issue, String workItemId) throws RepositoryException {
        try {
            new SQL(url).query("UPDATE workitem SET issue_id = ? WHERE id = ?").parameter(issue.getId()).parameter(workItemId)
                    .insert();

        } catch (SQLException e) {
            throw new RepositoryException("Could not add issue with title '" + issue.getTitle() + "' to work item with id: " + workItemId, e);
        }
    }

    @Override
    public List<WorkItem> getAllWorkItemsWithIssue() throws RepositoryException {
        try {
            return new SQL(url)
                    .query("SELECT * FROM workitem LEFT JOIN issue ON issue.id = workitem.issue_id WHERE issue.is_active = 1")
                    .many(WORK_ITEM_MAPPER);
        } catch (SQLException e) {
            throw new RepositoryException("Could not get all work items with an issue", e);
        }
    }
}
