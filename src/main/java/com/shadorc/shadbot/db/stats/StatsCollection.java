package com.shadorc.shadbot.db.stats;

import com.mongodb.client.model.Filters;
import com.mongodb.client.model.UpdateOptions;
import com.mongodb.client.model.Updates;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.reactivestreams.client.MongoDatabase;
import com.shadorc.shadbot.core.command.BaseCmd;
import com.shadorc.shadbot.db.DatabaseCollection;
import com.shadorc.shadbot.db.stats.bean.DailyCommandStatsBean;
import com.shadorc.shadbot.db.stats.entity.DailyCommandStats;
import com.shadorc.shadbot.utils.Utils;
import org.bson.Document;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.Logger;
import reactor.util.Loggers;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

public class StatsCollection extends DatabaseCollection {

    public static final Logger LOGGER = Loggers.getLogger("shadbot.database.stats");

    public StatsCollection(MongoDatabase database) {
        super(database.getCollection("stats"));
    }

    public Mono<UpdateResult> logCommand(BaseCmd cmd) {
        LOGGER.debug("[Commands stats] Logging `{}` usage", cmd.getName());

        return Mono.from(this.getCollection()
                .updateOne(Filters.eq("_id", TimeUnit.MILLISECONDS.toDays(Instant.now().toEpochMilli())),
                        Updates.inc(String.format("command_stats.%s", cmd.getName()), 1),
                        new UpdateOptions().upsert(true)))
                .doOnNext(result -> LOGGER.debug("[Commands stats] Logging `{}` usage result: {}", cmd.getName(), result));
    }

    public Flux<DailyCommandStats> getCommandStats() {
        LOGGER.debug("[Command stats] Request");

        final Publisher<Document> request = this.getCollection().find();

        return Flux.from(request)
                .map(document -> document.toJson(Utils.JSON_WRITER_SETTINGS))
                .flatMap(json -> Mono.fromCallable(() -> Utils.MAPPER.readValue(json, DailyCommandStatsBean.class)))
                .map(DailyCommandStats::new);
    }
}
