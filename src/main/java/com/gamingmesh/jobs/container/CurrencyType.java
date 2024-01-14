package com.gamingmesh.jobs.container;

import com.gamingmesh.jobs.Jobs;

public enum CurrencyType {
    MONEY("Money", 1),
    EXP("Exp", 2),
    POINTS("Points", 3),
    SKILLS_EXP("SkillsExp", 4);

    private String name;
    private int id = 0;
    private boolean enabled = true;

    CurrencyType(String name, int id) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public static CurrencyType getByName(String name) {
        for (CurrencyType one : values()) {
            if (one.getName().equalsIgnoreCase(name))
                return one;
        }
        return null;
    }

    public static CurrencyType get(int id) {
        for (CurrencyType one : values()) {
            if (one.getId() == id)
                return one;
        } return null;
    }

    public int getId() {
        return id;
    }

    public String getDisplayName() {
        return Jobs.getLanguage().getMessage("general.info.paymentType." + this.toString());
    }

    public boolean isEnabled() {
        return enabled;
    }
    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }
}
