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

package com.aprey.jira.plugin.openpoker.persistence;

import com.aprey.jira.plugin.openpoker.SessionStatus;
import javax.inject.Named;
import net.java.ao.Query;

@Named
public class QueryBuilderService {

    Query sessionWhereIssueIdAndStatus(String issueId, SessionStatus status) {
        return Query.select().where("ISSUE_ID = ? AND SESSION_STATUS = ?", issueId, status);
    }

    Query estimateWhereEstimatorIdAndSessionId(Long estimatorId, PokerSessionEntity session) {
        return Query.select().where("POKER_SESSION_ID = ? AND ESTIMATOR_ID = ?", session, estimatorId);
    }

    Query whereIssuerId(String issueId) {
        return Query.select().where("ISSUE_ID = ?", issueId);
    }
}
