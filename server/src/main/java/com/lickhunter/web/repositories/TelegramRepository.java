package com.lickhunter.web.repositories;

import com.lickhunter.web.entities.tables.records.TelegramUsersRecord;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jooq.DSLContext;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static com.lickhunter.web.entities.tables.TelegramUsers.TELEGRAM_USERS;

@RequiredArgsConstructor
@Transactional(transactionManager = "transactionManager")
@Component
@Slf4j
public class TelegramRepository {

    private final DSLContext dsl;

    public void insertOrUpdate(String username, Long chatId) {
        Optional<TelegramUsersRecord> record = dsl.selectFrom(TELEGRAM_USERS)
                .where(TELEGRAM_USERS.USERNAME.in(username))
                .fetchOptional();
        if(record.isPresent()) {
            this.update(username, chatId);
        } else {
            this.insert(username, chatId);
        }
    }

    public void update(String username, Long chatId) {
        dsl.update(TELEGRAM_USERS)
                .set(TELEGRAM_USERS.CHAT_ID, chatId)
                .where(TELEGRAM_USERS.USERNAME.in(username))
                .execute();
    }

    public void insert(String username, Long chatId) {
        dsl.insertInto(TELEGRAM_USERS)
                .set(TELEGRAM_USERS.USERNAME, username)
                .set(TELEGRAM_USERS.CHAT_ID, chatId)
                .execute();
    }

    public Optional<TelegramUsersRecord> findByUsername(String username) {
        return dsl.selectFrom(TELEGRAM_USERS)
                .where(TELEGRAM_USERS.USERNAME.eq(username))
                .fetchOptional();
    }
}
