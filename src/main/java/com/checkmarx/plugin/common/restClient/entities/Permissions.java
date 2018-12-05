package com.checkmarx.plugin.common.restClient.entities;

public class Permissions {

    private boolean saveSastScan;
    private boolean manageResultsSeverity;
    private boolean manageResultsExploitability;

    public Permissions(boolean saveSastScan, boolean manageResultsSeverity, boolean manageResultsExploitability) {
        this.saveSastScan = saveSastScan;
        this.manageResultsSeverity = manageResultsSeverity;
        this.manageResultsExploitability = manageResultsExploitability;
    }

    public boolean isSaveSastScan() {
        return saveSastScan;
    }

    public boolean isManageResultsSeverity() {
        return manageResultsSeverity;
    }

    public boolean isManageResultsExploitability() {
        return manageResultsExploitability;
    }
}