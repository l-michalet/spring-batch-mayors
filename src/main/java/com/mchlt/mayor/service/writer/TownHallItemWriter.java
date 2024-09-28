package com.mchlt.mayor.service.writer;


import com.mchlt.mayor.model.TownHall;
import com.mchlt.mayor.service.PersonTownHall;
import lombok.AllArgsConstructor;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;

import java.util.List;
import java.util.stream.Collectors;

@AllArgsConstructor
public class TownHallItemWriter implements ItemWriter<PersonTownHall> {

    private final JdbcBatchItemWriter<TownHall> delegate;

    @Override
    public void write(Chunk<? extends PersonTownHall> chunk) throws Exception {
        List<TownHall> townHalls = chunk.getItems().stream()
            .map(PersonTownHall::getTownHall)
            .collect(Collectors.toList());
        delegate.write(new Chunk<>(townHalls));
    }

}