package com.aprey.jira.plugin.openpoker.action;

import com.aprey.jira.plugin.openpoker.persistence.PersistenceService;
import com.aprey.jira.plugin.openpoker.service.PluginConfigService;
import com.atlassian.jira.security.request.RequestMethod;
import com.atlassian.jira.security.request.SupportedMethods;
import com.atlassian.jira.web.action.JiraWebActionSupport;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Objects;
import java.util.stream.Collectors;

@SupportedMethods(RequestMethod.GET)
public class ConfigAction extends JiraWebActionSupport {

    private final PersistenceService persistenceService;
    @Autowired
    private final PluginConfigService pluginConfigService;

    @Getter
    @Setter
    private String configs;

    public ConfigAction(PersistenceService persistenceService, PluginConfigService pluginConfigService) {
        this.persistenceService = persistenceService;
        this.pluginConfigService = pluginConfigService;
    }

    @Override
    public String execute() throws Exception {
        if (Objects.nonNull(configs)) {
            String trimmedConfigs = configs.trim();
            if (persistenceService.isCustomScaleInUse()) {
                return ERROR;
            } else {
                persistenceService.saveCustomPlanningConfiguration(trimmedConfigs);
                pluginConfigService.reloadConfig();
            }
        }
        return super.execute();
    }


    public String getAllConfigValue() {
        return persistenceService.getCustomPlanningConfiguration().stream().map(
                config -> config.getValue()
        ).collect(Collectors.joining("\r"));
    }

}
