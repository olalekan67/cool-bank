package com.olalekan.CoolBank.service.admin;

import com.olalekan.CoolBank.model.dto.admin.request.AdminDashboardStatDto;
import com.olalekan.CoolBank.model.dto.admin.response.AdminActionLogResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface AdminDashboardService {
    AdminDashboardStatDto dashboardStat();
    Page<AdminActionLogResponseDto> auditLogs(Pageable pageable);
}
