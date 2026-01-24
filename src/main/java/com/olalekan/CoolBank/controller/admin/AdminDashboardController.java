package com.olalekan.CoolBank.controller.admin;

import com.olalekan.CoolBank.model.dto.admin.AdminActionLogResponseDto;
import com.olalekan.CoolBank.model.dto.admin.AdminDashboardStatDto;
import com.olalekan.CoolBank.service.admin.AdminDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/admin")
public class AdminDashboardController {

    private final AdminDashboardService dashboardService;

    @GetMapping("dashoard/stat")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<AdminDashboardStatDto> dashboardStat(){
        AdminDashboardStatDto statistic = dashboardService.dashboardStat();
        return new ResponseEntity<>(statistic, HttpStatus.OK);
    }

    @GetMapping("audit-log")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Page<AdminActionLogResponseDto>> auditLogs(Pageable pageable){
        Page<AdminActionLogResponseDto> auditLogsResponse = dashboardService.auditLogs(pageable);
        return new ResponseEntity<>(auditLogsResponse, HttpStatus.OK);
    }
}
