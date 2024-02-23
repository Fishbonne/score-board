package com.sportradar.core.scoreboard.config;

import com.sportradar.core.scoreboard.dao.ScoreBoardDao;
import com.sportradar.core.scoreboard.dao.impl.InMemoryScoreBoardDao;
import com.sportradar.core.scoreboard.service.ScoreBoard;
import com.sportradar.core.scoreboard.service.impl.InMemoryScoreBoard;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ScoreBoardConfig {
    @Bean(name = "inMemoryScoreBoardDao")
    public ScoreBoardDao scoreBoardDao() {
        return new InMemoryScoreBoardDao();
    }

    @ConditionalOnClass(InMemoryScoreBoardDao.class)
    @Bean(name = "inMemoryScoreBoard")
    public ScoreBoard scoreBoard(ScoreBoardDao scoreBoardDao) {
        return new InMemoryScoreBoard(scoreBoardDao);
    }
}
