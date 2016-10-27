package main.se.root.ordersystem.service;

import java.util.List;

import main.se.root.ordersystem.model.Team;
import main.se.root.ordersystem.model.User;
import main.se.root.ordersystem.exception.RepositoryException;
import main.se.root.ordersystem.exception.ServiceException;
import main.se.root.ordersystem.repository.interfaces.TeamRepository;
import main.se.root.ordersystem.repository.interfaces.UserRepository;

/**
 * The Class TeamService - A grouping of User Features: - Create a team -
 * Updating a team - Disable a team - Download all the teams - Adding a User to
 * a team.
 * - maximum of 10 users in a team - A User can only be part of one team at the
 * time
 *
 * @author Root Group
 * @version 1.0.
 */
public final class TeamService {

    private final TeamRepository teamRepository;
    private final UserRepository userRepository;

    public TeamService(TeamRepository teamRepository, UserRepository userRepository) {
        this.teamRepository = teamRepository;
        this.userRepository = userRepository;
    }

    /**
     * Gets the users from team.
     *
     * @param id the id
     * @return the users from team
     * @throws ServiceException the service exception
     */
    public List<User> getUsersFromTeam(String id) throws ServiceException {
        try {
            return teamRepository.getUsersFromTeam(id);
        } catch (RepositoryException e) {
            throw new ServiceException("Couldn't get user from team");
        }
    }

    /**
     * Creates the team.
     *
     * @param team the team
     * @return the team
     * @throws ServiceException the service exception
     */
    public Team createTeam(Team team) throws ServiceException {
        try {
            long generatedId = teamRepository.create(team);
            return Team.teamBuilder(team.getName()).setId(String.valueOf(generatedId)).build();

        } catch (RepositoryException e) {
            throw new ServiceException("Failed to create team");
        }
    }

    /**
     * Update team.
     *
     * @param team the team
     * @throws ServiceException the service exception
     */
    public void updateTeam(Team team) throws ServiceException {
        try {
            if (teamRepository.read(team.getId()) == null) {
                throw new ServiceException("Cannot update team, id doesn't exist");
            }
            if (!team.isActive()) {
                throw new ServiceException("The team is not active and can not be updated");
            }
            teamRepository.update(team);
        } catch (RepositoryException e) {
            throw new ServiceException("Failed to update team");
        }
    }

    /**
     * Inactivate team.
     *
     * @param id the id
     * @throws ServiceException the service exception
     */
    public void inactivateTeam(String id) throws ServiceException {
        try {
            teamRepository.changeStatus(false, id);
        } catch (RepositoryException e) {
            if (e.getMessage().contains("Could not update change status for id:")) {
                throw new ServiceException(e.getMessage());
            } else {
                throw new ServiceException("Couldn't inactivate team with id '" + id + "' in the database");
            }
        }
    }

    /**
     * Activate team.
     *
     * @param id the id
     * @throws ServiceException the service exception
     */
    public void activateTeam(String id) throws ServiceException {
        try {
            teamRepository.changeStatus(true, id);
        } catch (RepositoryException e) {
            throw new ServiceException("Couldn't activate team with id '" + id + "' in the database");
        }
    }

    /**
     * Get all teams.
     *
     * @return the all teams
     * @throws ServiceException the service exception
     */
    public List<Team> getAllTeams() throws ServiceException {
        try {
            return teamRepository.getAll();
        } catch (RepositoryException e) {
            throw new ServiceException("Could not get all teams", e);
        }
    }

    /**
     * Adds the user to team if user is active
     *
     * @param userId the user id
     * @throws ServiceException the service exception
     */
    public void addUserToTeam(String userId) throws ServiceException {
        boolean foundTeam = false;
        try {
            if (userRepository.read(userId).isActive()) {
                for (Team t : teamRepository.getAll()) {
                    if (teamRepository.getUsersFromTeam(t.getId()).size() < 10 && t.isActive() && !foundTeam) {
                        teamRepository.addUserToTeam(userId, t.getId());
                        foundTeam = true;
                    }
                }
                if (!foundTeam) {
                    long newTeamId = teamRepository
                            .create(new Team.TeamBuilder("Team " + (teamRepository.getAll().size() + 1)).build());
                    teamRepository.addUserToTeam(userId, String.valueOf(newTeamId));
                }
            } else {
                throw new ServiceException("Could not add user to team since it's inactive in the database");
            }
        } catch (RepositoryException e) {
            throw new ServiceException("Could not add user to team", e);
        }
    }
}
