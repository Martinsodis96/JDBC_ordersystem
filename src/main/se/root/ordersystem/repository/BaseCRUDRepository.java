package main.se.root.ordersystem.repository;

import static main.se.root.ordersystem.helpers.DBInfo.url;

import java.sql.SQLException;
import java.util.List;

import main.se.root.ordersystem.helpers.ResultMapper;
import main.se.root.ordersystem.helpers.SQL;
import main.se.root.ordersystem.exception.RepositoryException;
import main.se.root.ordersystem.repository.interfaces.CRUDRepository;

public abstract class BaseCRUDRepository<T> implements CRUDRepository<T> {

    @Override
    public abstract long create(T t) throws RepositoryException;

    @Override
    public abstract void update(T t) throws RepositoryException;

    @Override
    public T read(String tablename, String id, ResultMapper<T> resultMapper) throws RepositoryException {
        try {
            return new SQL(url).query("SELECT * FROM " + tablename + " WHERE id = ?").parameter(id)
                    .single(resultMapper);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RepositoryException("could not read data from '" + tablename + "' with id: " + id, e);
        }
    }

    @Override
    public void changeStatus(String tablename, boolean isActive, String id) throws RepositoryException {
        try {
            new SQL(url).query("UPDATE " + tablename + " SET is_active= ? WHERE id = ?").parameter(isActive)
                    .parameter(id).update();
        } catch (SQLException e) {
            throw new RepositoryException("Could not change active status on data from '" + tablename + "' with id: " + id, e);
        }
    }

    @Override
    public List<T> getAll(String tablename, ResultMapper<T> resultMapper) throws RepositoryException {
        try {
            return new SQL(url).query("SELECT * FROM " + tablename).many(resultMapper);
        } catch (SQLException e) {
            throw new RepositoryException("could not get all the data from table: " + tablename, e);
        }
    }
}
