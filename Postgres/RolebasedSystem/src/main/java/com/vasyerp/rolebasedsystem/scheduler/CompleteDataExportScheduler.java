package com.vasyerp.rolebasedsystem.scheduler;

import com.vasyerp.rolebasedsystem.service.CompleteDataExcelExportService;
import com.vasyerp.rolebasedsystem.service.RedisLockService;
import com.vasyerp.rolebasedsystem.service.ScheduledExportContextService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.time.Duration;
import java.util.List;
import java.util.UUID;

@Component
@ConditionalOnProperty(
        name = "app.export.scheduled.enabled",
        havingValue = "true",
        matchIfMissing = false
)
public class CompleteDataExportScheduler {

    private static final Logger LOGGER = LoggerFactory.getLogger(CompleteDataExportScheduler.class);
    private static final String EXPORT_LOCK_KEY = "lock:scheduler:complete-data-export";
    private static final Duration EXPORT_LOCK_TTL = Duration.ofMinutes(30);

    private final CompleteDataExcelExportService completeDataExcelExportService;
    private final RedisLockService redisLockService;
    private final ScheduledExportContextService scheduledExportContextService;

    public CompleteDataExportScheduler(
            CompleteDataExcelExportService completeDataExcelExportService,
            RedisLockService redisLockService,
            ScheduledExportContextService scheduledExportContextService
    ) {
        this.completeDataExcelExportService = completeDataExcelExportService;
        this.redisLockService = redisLockService;
        this.scheduledExportContextService = scheduledExportContextService;
    }

    @Scheduled(cron = "${app.export.cron:0 10 16 * * *}")
    public void exportCompleteDataDaily() {
        String lockOwnerToken = UUID.randomUUID().toString();
        boolean lockAcquired = false;

        try {
            lockAcquired = tryAcquireLock(lockOwnerToken);
            if (!lockAcquired) {
                LOGGER.info("Skipped complete data export because another instance holds the lock");
                return;
            }

            List<ScheduledExportContextService.ExportContext> exportContexts =
                    scheduledExportContextService.getAllContexts();
            if (exportContexts.isEmpty()) {
                LOGGER.info("Skipped scheduled export because no logged-in export contexts are available");
                return;
            }

            int successCount = 0;
            int failureCount = 0;
            for (ScheduledExportContextService.ExportContext exportContext : exportContexts) {
                if (exportContext == null || exportContext.userId() == null) {
                    failureCount++;
                    continue;
                }
                try {
                    boolean isSystemAdmin = "SYSTEM_ADMIN".equalsIgnoreCase(exportContext.userType());
                    Path exportedFile = completeDataExcelExportService.exportScheduledDataByUser(
                            exportContext.userId(),
                            isSystemAdmin,
                            null,
                            exportContext.userType()
                    );
                    successCount++;
                    LOGGER.info("Complete data export completed for {} (userId={}): {}",
                            exportContext.userType(), exportContext.userId(), exportedFile.toAbsolutePath());
                } catch (Exception ex) {
                    failureCount++;
                    LOGGER.error("Complete data export failed for {} (userId={})",
                            exportContext.userType(), exportContext.userId(), ex);
                }
            }
            LOGGER.info("Scheduled export cycle finished. Success: {}, Failed: {}, Active contexts: {}",
                    successCount, failureCount, exportContexts.size());
        } catch (Exception ex) {
            LOGGER.error("Complete data export failed", ex);
        } finally {
            if (lockAcquired) {
                releaseLock(lockOwnerToken);
            }
        }
    }

    private boolean tryAcquireLock(String lockOwnerToken) {
        try {
            return redisLockService.tryLock(EXPORT_LOCK_KEY, lockOwnerToken, EXPORT_LOCK_TTL);
        } catch (Exception ex) {
            LOGGER.warn("Redis lock unavailable. Proceeding scheduled export without distributed lock.", ex);
            return true;
        }
    }

    private void releaseLock(String lockOwnerToken) {
        try {
            redisLockService.unlock(EXPORT_LOCK_KEY, lockOwnerToken);
        } catch (Exception ex) {
            LOGGER.warn("Failed to release export lock {}", EXPORT_LOCK_KEY, ex);
        }
    }
}
