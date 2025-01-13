package com.wecook.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "moderator_users")
public class ModeratorUser extends User {
    @Column(name = "analyzed_reports", nullable = false)
    private Long analyzedReports = 0L;

    public Long getAnalyzedReports() {
        return analyzedReports;
    }

    public void setAnalyzedReports(Long analyzedReports) {
        this.analyzedReports = analyzedReports;
    }
}
