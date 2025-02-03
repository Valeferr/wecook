package com.wecook.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "moderator_users")
public class ModeratorUser extends User {
    @Column(name = "analyzed_reports", nullable = false)
    private long analyzedReports = 0L;

    public long getAnalyzedReports() {
        return analyzedReports;
    }

    public void setAnalyzedReports(long analyzedReports) {
        this.analyzedReports = analyzedReports;
    }
}
