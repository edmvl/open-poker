package com.aprey.jira.plugin.openpoker.persistence;

import net.java.ao.Entity;

public interface Config extends Entity {

    String getValue();
    void setValue(String value);

    String getApplicable();
    void setApplicable(String applicable);

}
