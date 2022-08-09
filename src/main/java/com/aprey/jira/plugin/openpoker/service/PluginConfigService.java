package com.aprey.jira.plugin.openpoker.service;

import com.aprey.jira.plugin.openpoker.persistence.Config;
import com.aprey.jira.plugin.openpoker.persistence.CustomPlanning;
import com.aprey.jira.plugin.openpoker.persistence.PersistenceService;
import com.atlassian.sal.api.lifecycle.LifecycleAware;

import java.util.List;
import java.util.stream.Collectors;

public class PluginConfigService implements LifecycleAware {

    private final PersistenceService persistenceService;

    public PluginConfigService(PersistenceService persistenceService) {
        this.persistenceService = persistenceService;
    }

    @Override
    public void onStart() {
        reloadConfig();
    }

    @Override
    public void onStop() {

    }

    private List<CustomPlanning> getConfig() {
        List<Config> configs = persistenceService.getCustomPlanningConfiguration();
        return configs.stream().map(
                configItem -> new CustomPlanning(configItem.getID(), configItem.getValue(), true)
        ).collect(Collectors.toList());
    }

    public void reloadConfig() {
        List<CustomPlanning> config = getConfig();
        CustomPlanning.setValuesList(config);
    }
}
