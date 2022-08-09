package com.aprey.jira.plugin.openpoker.persistence;

import com.aprey.jira.plugin.openpoker.EstimationGrade;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;

import static java.util.stream.Collectors.toMap;

public class CustomPlanning implements EstimationGrade {

    private final int id;
    private final String value;
    private final boolean applicable;

    private static Map<Integer, EstimationGrade> ID_TO_INSTANCE_MAP;

    public CustomPlanning(int id, String value, boolean applicable) {
        this.id = id;
        this.value = value;
        this.applicable = applicable;

    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getValue() {
        return value;
    }

    @Override
    public boolean isApplicable() {
        return applicable;
    }

    public static EstimationGrade findById(int id) {
        return ID_TO_INSTANCE_MAP.get(id);
    }

    public static List<EstimationGrade> getValuesList() {
        return new ArrayList<>(ID_TO_INSTANCE_MAP.values());
    }

    public static void setValuesList(List<CustomPlanning> estimationGrades){
        CustomPlanning.ID_TO_INSTANCE_MAP = estimationGrades.stream()
                .collect(toMap(
                        EstimationGrade::getId,
                        Function.identity())
                );
    }
}
