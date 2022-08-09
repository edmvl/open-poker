/*
 * Copyright (C) 2021  Andriy Preizner
 *
 * This file is a part of Open Poker jira plugin
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package com.aprey.jira.plugin.openpoker.api;

import com.aprey.jira.plugin.openpoker.PokerSession;
import com.aprey.jira.plugin.openpoker.UserNotFoundException;
import com.aprey.jira.plugin.openpoker.persistence.PersistenceService;
import com.atlassian.jira.user.ApplicationUser;
import com.atlassian.jira.user.util.UserManager;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/session/")
public class PokerSessionResource {

    private final PersistenceService sessionService;
    @ComponentImport
    private final UserManager userManager;
    private final UserConverter userConverter;

    @Inject
    public PokerSessionResource(PersistenceService sessionService, UserManager userManager,
                                UserConverter userConverter) {
        this.sessionService = sessionService;
        this.userManager = userManager;
        this.userConverter = userConverter;
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response get(@Context HttpServletRequest request) {
        final String issueId = request.getParameter("issueId");
        final ApplicationUser user = getUser(request);
        Optional<PokerSession> activeSessionOpt = sessionService.getActiveSession(issueId);
        if (!activeSessionOpt.isPresent()) {
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        PokerSession activeSession = activeSessionOpt.get();

        SessionDTO sessionDTO = new SessionDTO();
        sessionDTO.setStatus(activeSession.getStatus());
        sessionDTO.setEstimators(activeSession.getEstimates().stream()
                                              .map(e -> userConverter.buildUserDto(e.getEstimator(), user))
                                              .collect(Collectors.toList()));

        return Response.ok(sessionDTO).build();
    }

    private ApplicationUser getUser(HttpServletRequest request) {
        final Long userId = Long.parseLong(request.getParameter("userId"));

        return getUser(userId);
    }

    private ApplicationUser getUser(Long userId) {
        return userManager.getUserById(userId).orElseThrow(() -> new UserNotFoundException(
                "User with id " + userId + " is not found"));
    }
}
