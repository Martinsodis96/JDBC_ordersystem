package main.se.root.ordersystem.repository.interfaces;

import java.util.List;

import main.se.root.ordersystem.helpers.ResultMapper;
import main.se.root.ordersystem.exception.RepositoryException;


/**
 * The Interface CRUDRepository.
 * @author Root Group
 * @version 1.0.
 *
 * @param <T> the generic type
 */
public interface CRUDRepository<T> {

	/**
	 * Creates the.
	 *
	 * @param t the t
	 * @return the long
	 * @throws RepositoryException the repository exception
	 */
	long create(T t) throws RepositoryException;

	/**
	 * Update.
	 *
	 * @param t the t
	 * @throws RepositoryException the repository exception
	 */
	void update(T t) throws RepositoryException;

	/**
	 * Read.
	 *
	 * @param tablename the tablename
	 * @param id the id
	 * @param resultMapper the result mapper
	 * @return the t
	 * @throws RepositoryException the repository exception
	 */
	T read(String tablename, String id, ResultMapper<T> resultMapper) throws RepositoryException;

	/**
	 * Change status.
	 *
	 * @param tablename the tablename
	 * @param isActive the is active
	 * @param id the id
	 * @throws RepositoryException the repository exception
	 */
	void changeStatus(String tablename, boolean isActive, String id) throws RepositoryException;

	/**
	 * Gets the all.
	 *
	 * @param tablename the tablename
	 * @param resultMapper the result mapper
	 * @return the all
	 * @throws RepositoryException the repository exception
	 */
	List<T> getAll(String tablename, ResultMapper<T> resultMapper) throws RepositoryException;
}
