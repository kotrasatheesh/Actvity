package com.example.asutosh.aebug.bean;

/**
 * Created by Asutosh on 24-08-2017.
 */

public class IssueListBean {
    public String issueTitle;
    public String reporterNmae;
    public String assignedName;
    public String createdDate;
    public String description;
    public String TicketId;

    public String getAssigneeID() {
        return assigneeID;
    }

    public void setAssigneeID(String assigneeID) {
        this.assigneeID = assigneeID;
    }

    public String getEnterdBy() {
        return EnterdBy;
    }

    public void setEnterdBy(String enterdBy) {
        EnterdBy = enterdBy;
    }

    public String assigneeID;
    public String EnterdBy;

    public String getEnvironment() {
        return environment;
    }

    public void setEnvironment(String environment) {
        this.environment = environment;
    }

    public String environment;

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTicketId() {
        return TicketId;
    }

    public void setTicketId(String ticketId) {
        TicketId = ticketId;
    }

    public String getProjectId() {
        return ProjectId;
    }

    public void setProjectId(String projectId) {
        ProjectId = projectId;
    }

    public String ProjectId;

    public String getIssueTitle() {
        return issueTitle;
    }

    public void setIssueTitle(String issueTitle) {
        this.issueTitle = issueTitle;
    }

    public String getReporterNmae() {
        return reporterNmae;
    }

    public void setReporterNmae(String reporterNmae) {
        this.reporterNmae = reporterNmae;
    }

    public String getAssignedName() {
        return assignedName;
    }

    public void setAssignedName(String assignedName) {
        this.assignedName = assignedName;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String modifiedDate;
    public String status;
    public String priority;
}
