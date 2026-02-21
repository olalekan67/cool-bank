package com.olalekan.CoolBank.controller.admin;

import com.olalekan.CoolBank.model.dto.response.ApiResponse;
import com.olalekan.CoolBank.service.admin.AdminDashboardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/admin")
@Secured("ROLE_ADMIN")
public class AdminDashboardController {

    private final AdminDashboardService dashboardService;

    @GetMapping("dashboard/stats")
    public ResponseEntity<ApiResponse> dashboardStats(){
//        AdminDashboardStatDto statistic = dashboardService.dashboardStat();
        return ResponseEntity.status(HttpStatus.OK)
                .body(dashboardService.dashboardStat());
    }

    @GetMapping("audit-log")
    public ResponseEntity<ApiResponse> auditLogs(Pageable pageable){
//        Page<AdminActionLogResponseDto> auditLogsResponse = dashboardService.auditLogs(pageable);
        return ResponseEntity.status(HttpStatus.OK)
                .body(dashboardService.auditLogs(pageable));
    }
}
