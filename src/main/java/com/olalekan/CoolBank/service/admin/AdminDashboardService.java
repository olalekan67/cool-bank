package com.olalekan.CoolBank.service.admin;

import com.olalekan.CoolBank.model.dto.response.ApiResponse;
import org.springframework.data.domain.Pageable;

public interface AdminDashboardService {
    ApiResponse dashboardStat();
    ApiResponse auditLogs(Pageable pageable);
}
