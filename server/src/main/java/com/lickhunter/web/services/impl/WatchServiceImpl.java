package com.lickhunter.web.services.impl;

import com.lickhunter.web.constants.ApplicationConstants;
import com.lickhunter.web.scheduler.LickHunterScheduledTasks;
import com.lickhunter.web.services.LickHunterService;
import com.lickhunter.web.services.WatchService;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.file.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class WatchServiceImpl implements WatchService {

    private final LickHunterScheduledTasks lickHunterScheduledTasks;
    private final LickHunterService lickHunterService;

    @SneakyThrows
    public void fileWatcher() {
        final Path path = FileSystems.getDefault().getPath("./");

        try (final java.nio.file.WatchService watchService = FileSystems.getDefault().newWatchService()) {
            final WatchKey watchKey = path.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY, StandardWatchEventKinds.ENTRY_DELETE);
            while (true) {
                final WatchKey wk = watchService.take();
                for (WatchEvent<?> event : wk.pollEvents()) {
                    final Path changed = (Path) event.context();
                    if (event.kind().name().equalsIgnoreCase(StandardWatchEventKinds.ENTRY_MODIFY.name())
                        && (changed.endsWith(ApplicationConstants.SETTINGS.getValue())
                        || changed.endsWith(ApplicationConstants.WEB_SETTINGS.getValue())
                        || changed.endsWith(ApplicationConstants.TICKER_QUERY.getValue()))) {
                        log.info("File changed:" + changed);
                        lickHunterScheduledTasks.writeToCoinsJson();
                    }
                    if (event.kind().name().equalsIgnoreCase(StandardWatchEventKinds.ENTRY_DELETE.name())
                        && changed.endsWith(ApplicationConstants.COINS.getValue())) {
                        log.info("File deleted:" + changed);
                        lickHunterScheduledTasks.writeToCoinsJson();
                    }
                }
                // reset the key
                boolean valid = wk.reset();
                if (!valid) {
                    System.out.println("Key has been unregistered");
                }
            }
        }
    }
}
